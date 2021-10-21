package com.seedcompany.cordtables.components.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody

data class GetSecureListQueryRequest(
    val tableName: String,
    val columns: Array<String>,
)

data class GetSecureListQueryResponse(
    var query: String,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
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

        response.query += columns.joinToString()

        response.query += """
            from ${req.tableName} 
            where id in (select row from row_level_access) or
                (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1)) or
                owning_person = (select person from admin.tokens where token = :token) or
                id in (select row from public_row_level_access);
        """.replace('\n', ' ')

        return response
    }

}