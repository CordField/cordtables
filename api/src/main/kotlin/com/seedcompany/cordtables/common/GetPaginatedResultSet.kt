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
  val tableName: String,
  val columns: Array<String>,
  val token: String,
  val whereClause: String = "",
  val filter: String = "",
  val resultsPerPage: Int = 50,
  val page: Int = 1,
  val custom_columns: String? = null,
  val getList: Boolean = true, // get read if false
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

      val columns = req.columns.map {
          """
              case
                  when '$it' in (select column_name from column_level_access) then $it 
                  when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1)) then $it
                  when owning_person = (select person from admin.tokens where token = :token) then $it 
                  when '$it' in (select column_name from public_column_level_access) then $it 
                  else null 
              end as $it
          """.replace('\n', ' ')
      }
      query += columns.joinToString()
      if(req.custom_columns!=null) {
          query += ','
          query += req.custom_columns.replace('\n', ' ')
      }

      if (req.getList) {
          query += """
              from ${req.tableName} 
              where (id in (select row from row_level_access) or
                  (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1)) or
                  owning_person = (select person from admin.tokens where token = :token) or
                  id in (select row from public_row_level_access))  
          """.replace('\n', ' ')
      } else {
          query += """
              from ${req.tableName} 
              where
                  id = :id and
                  ((id in (select row from row_level_access) or
                  (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1)) or
                  owning_person = (select person from admin.tokens where token = :token) or
                  id in (select row from public_row_level_access)))
              """.replace('\n', ' ')
      }

      if(req.whereClause!=="") {
        query += """
              and ${req.whereClause}
              
              ${req.filter}            ;
              ;
              """.trimIndent().replace('\n', ' ')
      }
      else {
        query+="""
            ${req.filter};
            """.trimIndent().replace('\n',' ')
      }

      var offset = (req.page-1)*req.resultsPerPage
      var limitQuery = "$query LIMIT :limit OFFSET :offset ";
      val paramSource = MapSqlParameterSource()
      paramSource.addValue("token", req.token)
      paramSource.addValue("limit", req.resultsPerPage)
      paramSource.addValue("offset", offset)
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
