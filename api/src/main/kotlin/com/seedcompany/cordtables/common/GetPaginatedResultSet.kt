package com.seedcompany.cordtables.common

import com.seedcompany.cordtables.components.admin.GetSecureListQueryResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.rowset.SqlRowSet
import org.springframework.stereotype.Component
import java.sql.SQLException
import javax.sql.DataSource


data class GetPaginatedResultSetRequest(
  val token: String,
  val tableName: String,
  val columns: Array<String>,
  val joinColumns: MutableMap<String, MutableMap<String, String>> = mutableMapOf(),
  val joinTables: String = "",
  val custom_columns: String? = null,
  val id: String? = null,
  val filter: String = "",
  val searchColumns: Array<String> = arrayOf(),
  val searchKeyword: String = "",
  val getList: Boolean = true, // get read if false
  val whereClause: String = "",
  val resultsPerPage: Int = 50,
  val page: Int = 1,
)

data class  GetPaginatedResultSetResponse(
  val errorType: ErrorType,
  val result: SqlRowSet? = null,
  val size: Int
)

@Component
class GetPaginatedResultSet (
  @Autowired
  val ds: DataSource,
) {
  var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

  fun getPaginatedResultSetHandler(req: GetPaginatedResultSetRequest) :GetPaginatedResultSetResponse {

    val getAdminRoleIdSubQueryText = """
      select id from admin.roles where name = 'Administrator'
    """.trimIndent()

    var query = """
          with row_level_access as 
          (
              select row 
              from admin.group_row_access as a  
              inner join admin.group_memberships as b 
              on a.group_id = b.group_id 
              inner join admin.tokens as c 
              on b.person = c.person
              where a.table_name = '${req.tableName}'
              and c.token = :token
          ), 
          public_row_level_access as 
          (
              select row 
              from admin.group_row_access as a  
              inner join admin.group_memberships as b 
              on a.group_id = b.group_id 
              inner join admin.tokens as c 
              on b.person = c.person
              where a.table_name = '${req.tableName}'
              and c.token = 'public'
          ), 
          column_level_access as 
          (
              select column_name 
              from admin.role_column_grants a 
              inner join admin.role_memberships b 
              on a.role = b.role 
              inner join admin.tokens c 
              on b.person = c.person 
              where a.table_name = '${req.tableName}'
              and c.token = :token
          ),
          public_column_level_access as 
          (
              select column_name 
              from admin.role_column_grants a 
              inner join admin.role_memberships b 
              on a.role = b.role 
              inner join admin.tokens c 
              on b.person = c.person 
              where a.table_name = '${req.tableName}'
              and c.token = 'public'
          )
          select
      """.replace('\n', ' ')

    var columns: List<String> = listOf()
    if(req.joinColumns.isNotEmpty()){
      for (table in req.joinColumns){
        for (field in table.value){
          columns += """
                        case
                            when '${field.key}' in (select column_name from column_level_access) then ${table.key}.${field.key}
                            when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = (SELECT id FROM admin.roles WHERE name='Administrator'))) then ${table.key}.${field.key}
                            when ${req.tableName}.owning_person = (select person from admin.tokens where token = :token) then ${table.key}.${field.key}
                            when '${field.key}' in (select column_name from public_column_level_access) then ${table.key}.${field.key}
                            else null
                        end as ${field.value} 
                    """.trimIndent()
        }
      }
    }
    else{
      columns = req.columns.map {
        """
                    case
                        when '$it' in (select column_name from column_level_access) then $it 
                        when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = (SELECT id FROM admin.roles WHERE name='Administrator'))) then $it
                        when owning_person = (select person from admin.tokens where token = :token) then $it 
                        when '$it' in (select column_name from public_column_level_access) then $it 
                        else null 
                    end as $it
                """.replace('\n', ' ')
      }
    }

//      val columns = req.columns.map {
//          """
//              case
//                  when '$it' in (select column_name from column_level_access) then $it
//                  when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = ($getAdminRoleIdSubQueryText))) then $it
//                  when owning_person = (select person from admin.tokens where token = :token) then $it
//                  when '$it' in (select column_name from public_column_level_access) then $it
//                  else null""
//              end as $it
//          """.replace('\n', ' ')
//      }
    query += columns.joinToString()


    if(req.custom_columns!=null) {
      query += ','
      query += req.custom_columns.replace('\n', ' ')
    }

    if(req.joinTables!=""){
      query += """
            from ${req.joinTables} 
          """.trimIndent()
    }
    else {
      query += """
          from ${req.tableName}  
        """.trimIndent()
    }

    if (req.getList) {
      query += """
              where (${req.tableName}.id in (select row from row_level_access) or
                  (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = (SELECT id FROM admin.roles WHERE name='Administrator'))) or
                  ${req.tableName}.owning_person = (select person from admin.tokens where token = :token) or
                  ${req.tableName}.id in (select row from public_row_level_access))
              """.replace('\n', ' ')
    } else {
      query += """
              where
                  ${req.tableName}.id = :id and
                  ((${req.tableName}.id in (select row from row_level_access) or
                  (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = (SELECT id FROM admin.roles WHERE name='Administrator'))) or
                  ${req.tableName}.owning_person = (select person from admin.tokens where token = :token) or
                  ${req.tableName}.id in (select row from public_row_level_access)))
              """.replace('\n', ' ')
    }

    if (req.searchColumns.isNotEmpty()){
      var searchQueryString = ""
      for (searchField in req.searchColumns){
        if (searchQueryString != "") searchQueryString += " OR "
        searchQueryString += "$searchField ILIKE '%${req.searchKeyword}%'"
      }
      query += " AND ($searchQueryString) "
    }

    if(req.whereClause!=="") {
      query += """
              and ${req.whereClause}
              
              ${req.filter}
              """.trimIndent().replace('\n', ' ')
    }
    else {
      query+="""
            ${req.filter}
            """.trimIndent().replace('\n',' ')
    }

    var offset = (req.page-1)*req.resultsPerPage
    var limitQuery = query
    if(req.getList){
        limitQuery = "$query LIMIT :limit OFFSET :offset ";
    }
    val paramSource = MapSqlParameterSource()
    paramSource.addValue("token", req.token)
    if (req.getList) {
      paramSource.addValue("limit", req.resultsPerPage)
      paramSource.addValue("offset", offset)
    }
    else{
      paramSource.addValue("id", req.id)
    }
    return try {
      val resultRows = jdbcTemplate.queryForRowSet(query, paramSource)
      resultRows.last();
      val totalRows = resultRows.getRow();

      val jdbcResult = jdbcTemplate.queryForRowSet(limitQuery, paramSource)
      GetPaginatedResultSetResponse(ErrorType.NoError, result = jdbcResult, size = totalRows)
    } catch (e: SQLException) {
      println("error while listing ${e.message}")
      GetPaginatedResultSetResponse(ErrorType.SQLReadError, size = 0)
    }
  }
}
