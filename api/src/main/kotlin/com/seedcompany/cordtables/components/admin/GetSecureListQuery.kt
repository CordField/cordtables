package com.seedcompany.cordtables.components.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody

data class GetSecureListQueryRequest(
  val tableName: String,
  val columns: Array<String>,
  val joinColumns: MutableMap<String, MutableMap<String, String>> = mutableMapOf(),
  val joinTables: String = "",
  val custom_columns: String? = null,
  val filter: String = "",
  val searchField: String = "",
  val searchKeyword: String = "",
  val getList: Boolean = true, // get read if false
  val whereClause: String = ""
)

data class GetSecureListQueryResponse(
    var query: String,

)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("GetSecureListQuery")
class GetSecureListQuery() {


    @PostMapping("admin/get-secure-list-query")
    @ResponseBody
    fun getSecureListQueryHandler(@RequestBody req: GetSecureListQueryRequest): GetSecureListQueryResponse {
        val response = GetSecureListQueryResponse("")

        response.query = """
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


        response.query += columns.joinToString()
        if(req.custom_columns!=null) {
            response.query += ','
            response.query += req.custom_columns.replace('\n', ' ')
        }

        if(req.joinTables!=""){
            response.query += """
              from ${req.joinTables} 
            """.trimIndent()
        }
        else {
            response.query += """
              from ${req.tableName}  
            """.trimIndent()
        }

         if (req.getList) {
            response.query += """
            where (${req.tableName}.id in (select row from row_level_access) or
                (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = (SELECT id FROM admin.roles WHERE name='Administrator'))) or
                ${req.tableName}.owning_person = (select person from admin.tokens where token = :token) or
                ${req.tableName}.id in (select row from public_row_level_access))
            """.replace('\n', ' ')
        } else {
            response.query += """
            where
                ${req.tableName}.id = :id::uuid and
                ((${req.tableName}.id in (select row from row_level_access) or
                (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = (SELECT id FROM admin.roles WHERE name='Administrator'))) or
                ${req.tableName}.owning_person = (select person from admin.tokens where token = :token) or
                ${req.tableName}.id in (select row from public_row_level_access)))
            """.replace('\n', ' ')
        }


      if(req.whereClause!=="") {
        response.query += """
            and ${req.whereClause}
            
            ${req.filter}            ;
            ;
            """.trimIndent().replace('\n', ' ')
      }
      else {
        response.query+="""
          ${req.filter};
          """.trimIndent().replace('\n',' ')
      }
      println(response.query)
        return response
    }

}
