package com.seedcompany.cordtables.components.tables.admin.users

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

data class AdminUserListResponse(
    val error: ErrorType,
    val data: MutableList<AdminUser>?
)

data class AdminUserListRequest(
    val token: String?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("AdminUserList")
class List<T>(
    @Autowired
    val util: Utility,
    @Autowired
    val ds: DataSource,
) {

    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("table/admin-users")
    @ResponseBody
    fun listHandler(@RequestBody req: AdminUserListRequest): AdminUserListResponse {
        var data: MutableList<AdminUser> = mutableListOf()
        if (req.token == null) return AdminUserListResponse(ErrorType.TokenNotFound, mutableListOf())

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)

        //language=SQL
        val listSQL = """               
            with row_level_access as 
            (
                select row 
                from admin.group_row_access as a  
                inner join admin.group_memberships as b 
                on a.group_id = b.group_id 
                inner join admin.tokens as c 
                on b.person = c.person
                where a.table_name = 'sc.languages_ex'
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
                where a.table_name = 'sc.languages_ex'
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
                when 'person' in (select column_name from column_level_access) then person 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then person
                else null 
            end as person,
            case
                when 'email' in (select column_name from column_level_access) then email
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then email
                else null 
            end as email,
            case
                when 'chat' in (select column_name from column_level_access) then chat 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then chat
                else null 
            end as chat,
            case
                when 'created_at' in (select column_name from column_level_access) then created_at 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then created_at
                else null 
            end as created_at,
            case
                when 'created_by' in (select column_name from column_level_access) then created_by 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then created_by
                else null 
            end as created_by,
            case
                when 'modified_at' in (select column_name from column_level_access) then modified_at 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then modified_at
                else null 
            end as modified_at,
            case
                when 'modified_by' in (select column_name from column_level_access) then modified_by 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then modified_by
                else null 
            end as modified_by,
            case
                when 'owning_person' in (select column_name from column_level_access) then owning_person 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then owning_person
                else null 
            end as owning_person,
            case
                when 'owning_group' in (select column_name from column_level_access) then owning_group 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then owning_group
                else null 
            end as owning_group,
            case
                when 'peer' in (select column_name from column_level_access) then peer 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then peer
                else null 
            end as peer
            from admin.users
            where id in (select row from row_level_access) or 
                (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1)) or
        """.trimIndent()

        try {
            val jdbcResult = jdbcTemplate.queryForRowSet(listSQL, paramSource)
            while (jdbcResult.next()) {

                var id: Int? = jdbcResult.getInt("id")
                if (jdbcResult.wasNull()) id = null

                var person: Int? = jdbcResult.getInt("person")
                if (jdbcResult.wasNull()) person = null

                var email: String? = jdbcResult.getString("iso")
                if (jdbcResult.wasNull()) email = null

                var chat: Int? = jdbcResult.getInt("chat")
                if (jdbcResult.wasNull()) chat = null

                var createdAt: String? = jdbcResult.getString("created_at")
                if (jdbcResult.wasNull()) createdAt = null

                var createdBy: Int? = jdbcResult.getInt("created_by")
                if (jdbcResult.wasNull()) createdBy = null

                var modifiedAt: String? = jdbcResult.getString("modified_at")
                if (jdbcResult.wasNull()) modifiedAt = null

                var modifiedBy: Int? = jdbcResult.getInt("modified_by")
                if (jdbcResult.wasNull()) modifiedBy = null

                var owningPerson: Int? = jdbcResult.getInt("owning_person")
                if (jdbcResult.wasNull()) owningPerson = null

                var owningGroup: Int? = jdbcResult.getInt("owning_group")
                if (jdbcResult.wasNull()) owningGroup = null

                var peer: Int? = jdbcResult.getInt("peer")
                if (jdbcResult.wasNull()) peer = null

                data.add(
                    AdminUser(
                        id= id,
                        person = person,
                        email = email,
                        password = null,
                        chat = chat,
                        created_at = createdAt,
                        created_by = createdBy,
                        modified_at = modifiedAt,
                        modified_by = modifiedBy,
                        owning_person= owningPerson,
                        owning_group =  owningGroup,
                        peer = peer,
                    )
                )
            }
        } catch (e: SQLException) {
            println("error while listing ${e.message}")
            return AdminUserListResponse(ErrorType.SQLReadError, mutableListOf())
        }

        return AdminUserListResponse(ErrorType.NoError, data)
    }
}