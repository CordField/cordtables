package com.seedcompany.cordtables.common

import com.seedcompany.cordtables.components.admin.GetSecureListQueryResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.rowset.SqlRowSet
import org.springframework.stereotype.Component
import java.sql.ResultSet
import java.sql.SQLException
import javax.sql.DataSource


data class GetPaginatedResultSetV2Request(
  val token: String,
  val tableName: String,
  val columns: Array<String>,
  val joinColumns: MutableMap<String, MutableMap<String, String>> = mutableMapOf(),
  val joinTables: String = "",
  val custom_columns: String? = null,
  val filter: String = "",
  val searchColumns: Array<String> = arrayOf(),
  val searchKeyword: String = "",
  val getList: Boolean = true, // get read if false
  val whereClause: String = "",
  val resultsPerPage: Int = 50,
  val page: Int = 1,
  val id: String? = null
)

data class  GetPaginatedResultSetV2Response(
  val errorType: ErrorType,
  val query: String,
  val size: Int
)

@Component
class GetPaginatedResultSetV2 (
  @Autowired
  val ds: DataSource,
) {
  var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)
  // var jdbcTemplate1: JdbcTemplate = JdbcTemplate(ds)

  fun getPaginatedResultSetHandler(req: GetPaginatedResultSetV2Request) :GetPaginatedResultSetV2Response {

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
              and c.token = '${req.token}'
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
              and c.token = '${req.token}'
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
                            when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = '${req.token}') and role = (SELECT id FROM admin.roles WHERE name='Administrator'))) then ${table.key}.${field.key}
                            when ${req.tableName}.owning_person = (select person from admin.tokens where token = '${req.token}') then ${table.key}.${field.key}
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
                        when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = '${req.token}') and role = (SELECT id FROM admin.roles WHERE name='Administrator'))) then $it
                        when owning_person = (select person from admin.tokens where token = '${req.token}') then $it 
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
//                  when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = '${req.token}') and role = ($getAdminRoleIdSubQueryText))) then $it
//                  when owning_person = (select person from admin.tokens where token = '${req.token}') then $it
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
                  (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = '${req.token}') and role = (SELECT id FROM admin.roles WHERE name='Administrator'))) or
                  ${req.tableName}.owning_person = (select person from admin.tokens where token = '${req.token}') or
                  ${req.tableName}.id in (select row from public_row_level_access))
              """.replace('\n', ' ')
    } else {
      query += """
              where
                  ${req.tableName}.id = '${req.id}' and
                  ((${req.tableName}.id in (select row from row_level_access) or
                  (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = '${req.token}') and role = (SELECT id FROM admin.roles WHERE name='Administrator'))) or
                  ${req.tableName}.owning_person = (select person from admin.tokens where token = '${req.token}') or
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
    var limitQuery = "$query LIMIT ${req.resultsPerPage} OFFSET $offset ";
    val paramSource = MapSqlParameterSource()
    paramSource.addValue("token", req.token)
//      paramSource.addValue("limit", req.resultsPerPage)
//      paramSource.addValue("offset", offset)
    return try {
      val resultRows = jdbcTemplate.queryForRowSet(query, paramSource)
      resultRows.last();
      val totalRows = resultRows.getRow();

      GetPaginatedResultSetV2Response(ErrorType.NoError, query = limitQuery, size = totalRows)

    } catch (e: SQLException) {
      println("error while listing ${e.message}")
      GetPaginatedResultSetV2Response(ErrorType.SQLReadError, size = 0, query = "")
    }
  }
}
