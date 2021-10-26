package com.seedcompany.cordtables.components.tables.admin.roles

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource

data class Role(
        val id: Int?,
        val created_at: String?,
        val created_by: Int?,
        val modified_at: String?,
        val modified_by: Int?,
        val name: String?,
        val owning_group: Int?,
        val owning_person:Int?,
)

data class ListRoleRequest(
        val token: String,
)

data class ListRoleResponse(
        val error: ErrorType,
        val data: MutableList<Role>?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("RoleList")
class List(
        @Autowired
        val util: Utility,

        @Autowired
        val ds: DataSource,
) {
    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)


    @PostMapping("role/read")
    @ResponseBody
    fun ListHandler(@RequestBody req: ListRoleRequest): ListRoleResponse {
        //mutableList as we need to add each global role as an element to it
        var data: MutableList<Role> = mutableListOf()
        if (req.token == null) return ListRoleResponse(ErrorType.TokenNotFound, mutableListOf())

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)

        this.ds.connection.use { conn ->
            //language=SQL
            val listStatement =

                    """
                     with row_level_access as 
                    (
                        select row 
                        from admin.group_row_access as a  
                        inner join admin.group_memberships as b 
                        on a.group_id = b.group_id 
                        inner join admin.tokens as c 
                        on b.person = c.person
                        where a.table_name = 'admin.roles'
                        and c.token = :token
                    ), 
                    column_level_access as 
                    (
                        select column_name 
                        from admin.role_column_grants a 
                        inner join admin.role_memberships b 
                        on a.role = b.role 
                        inner join admin.tokens c 
                        on b.person = c.person 
                        where a.table_name = 'admin.roles'
                        and c.token = :token
                    )
                    select       
                    case
                        when 'id' in (select column_name from column_level_access) then id 
                        when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then id 
                        when owning_person = (select person from admin.tokens where token = :token) then id 
                        else null 
                    end as id,       
                    case
                        when 'name' in (select column_name from column_level_access) then name 
                        when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then name 
                        when owning_person = (select person from admin.tokens where token = :token) then name 
                        else null 
                    end as name,     
                    case
                        when 'modified_by' in (select column_name from column_level_access) then modified_by 
                        when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then modified_by 
                        when owning_person = (select person from admin.tokens where token = :token) then modified_by 
                        else null 
                    end as modified_by, 
                    case
                        when 'created_by' in (select column_name from column_level_access) then created_by 
                        when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then created_by 
                        when owning_person = (select person from admin.tokens where token = :token) then created_by 
                        else null 
                    end as created_by, 
                    case
                        when 'created_at' in (select column_name from column_level_access) then created_at 
                        when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then created_at 
                        when owning_person = (select person from admin.tokens where token = :token) then created_at 
                        else null 
                    end as created_at,
                    case
                        when 'modified_at' in (select column_name from column_level_access) then modified_at 
                        when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then modified_at 
                        when owning_person = (select person from admin.tokens where token = :token) then modified_at 
                        else null 
                    end as modified_at,
                     case
                        when 'owning_group' in (select column_name from column_level_access) then owning_group 
                        when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then owning_group 
                        when owning_person = (select person from admin.tokens where token = :token) then owning_group 
                        else null 
                    end as owning_group,  
                    case
                        when 'owning_person' in (select column_name from column_level_access) then owning_person 
                        when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1)) then owning_person 
                        when owning_person = (select person from admin.tokens where token = :token) then owning_person 
                        else null 
                    end as owning_person       
                    from admin.roles 
                    where id in (select row from row_level_access) or 
                    (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1)) or
                    owning_person = (select person from admin.tokens where token = :token);
                    """.trimIndent()
            try {
                val listStatementResult = jdbcTemplate.queryForRowSet(listStatement, paramSource)

                while (listStatementResult.next()) {
                    var id: Int? = listStatementResult.getInt("id")
                    if(listStatementResult.wasNull()) id = null
                    var name: String? = listStatementResult.getString("name")
                    if(listStatementResult.wasNull()) name = null
                    var created_by: Int? = listStatementResult.getInt("created_by")
                    if(listStatementResult.wasNull()) created_by = null
                    var modified_by: Int? = listStatementResult.getInt("modified_by")
                    if(listStatementResult.wasNull()) modified_by = null
                    var owning_group: Int? = listStatementResult.getInt("owning_group")
                    if(listStatementResult.wasNull()) owning_group = null
                    var owning_person: Int? = listStatementResult.getInt("owning_person")
                    if(listStatementResult.wasNull()) owning_person = null
                    var created_at: String? = listStatementResult.getString("created_at")
                    if(listStatementResult.wasNull()) created_at = null
                    var modified_at: String? = listStatementResult.getString("modified_at")
                    if(listStatementResult.wasNull()) modified_at = null
                    data.add(Role(
                            id = id,
                            owning_group = owning_group,
                            owning_person = owning_person,
                            created_at = created_at,
                            created_by = created_by,
                            modified_at = modified_at,
                            modified_by = modified_by,
                            name = name))
                }
            } catch (e: SQLException) {
                println("error while listing ${e.message}")
                return ListRoleResponse(ErrorType.SQLReadError, null)
            }
        }
        return ListRoleResponse(ErrorType.NoError, data)
    }
}