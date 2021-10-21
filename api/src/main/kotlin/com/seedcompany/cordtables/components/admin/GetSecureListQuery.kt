package com.seedcompany.cordtables.components.admin

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import javax.sql.DataSource

data class GetSecureListQueryRequest(
    val tableName: String,
    val columns: Array<String>,
)

data class GetSecureListQueryResponse(
    var query: String,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("GetSecureListQuery")
class GetSecureListQuery(
        @Autowired
        val ds: DataSource,
) {


    @PostMapping("admin/get-secure-list-query/{tableName}")
    @ResponseBody
    fun getSecureListQueryHandler(@PathVariable tableName: String): GetSecureListQueryResponse {
        val tableColumns:MutableList<String> = mutableListOf();
        val (schema, table) = tableName.split('.')
        this.ds.connection.use{conn ->
            //language=SQL
            val getColumnsQuery = conn.prepareCall("""
                select column_name 
                from information_schema.columns
                where table_name = ? 
                and table_schema = ?
            """.trimIndent())
            getColumnsQuery.setString(1,table)
            getColumnsQuery.setString(2,schema)
            val getColumnsResult = getColumnsQuery.executeQuery()
            while(getColumnsResult.next()){
                tableColumns.add(getColumnsResult.getString("column_name"))
            }
        }


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
                where a.table_name = '${tableName}'
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
                where a.table_name = '${tableName}'
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
                where a.table_name = '${tableName}'
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
                where a.table_name = '${tableName}'
                and c.token = 'public'
            )
            select
        """.replace('\n', ' ')

        val columns = tableColumns.map {
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

        response.query += columns.joinToString()

        response.query += """
            from ${tableName} 
            where id in (select row from row_level_access) or
                (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1)) or
                owning_person = (select person from admin.tokens where token = :token) or
                id in (select row from public_row_level_access);
        """.replace('\n', ' ')

        return response
    }

}