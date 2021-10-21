package com.seedcompany.cordtables.components.tables.globalrolememberships
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
import javax.sql.DataSource
import kotlin.collections.List


data class GlobalRoleMemberships(
        val id: Int?,
        val globalRole: Int?,
        val person: Int?,
        val createdAt: String?,
        val createdBy: Int?,
        val modifiedAt: String?,
        val modifiedBy: Int?,
        val owningPerson: Int?,
        val owningGroup: Int?,
)

data class GlobalRoleMembershipsRequest(
        val token: String? = null,
)

data class GlobalRoleMembershipsReturn(
        val error: ErrorType,
        val globalRoleMemberships: List<out GlobalRoleMemberships>?,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("GlobalRoleMembershipsList")
class List(
        @Autowired
        val util: Utility,

        @Autowired
        val ds: DataSource,
) {
    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("role-memberships/list")
    @ResponseBody
    fun listHandler(@RequestBody req: GlobalRoleMembershipsRequest): GlobalRoleMembershipsReturn {

        if (req.token == null) return GlobalRoleMembershipsReturn(ErrorType.TokenNotFound, null)

        val items = mutableListOf<GlobalRoleMemberships>()

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
                where a.table_name = 'admin.role_memberships'
                and c.token = :token
            ), 
            column_level_access as 
            (
                select  column_name 
                from admin.role_column_grants a 
                inner join admin.role_memberships b 
                on a.role = b.role 
                inner join admin.tokens c 
                on b.person = c.person 
                where a.table_name = 'admin.role_memberships'
                and c.token = :token
            )
            select 
            case 
                when 'id' in 
                    (select column_name from column_level_access) 
                then id 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1)) 
                then id 
                else null 
            end as id,
            case 
                when 'role' in (select column_name from column_level_access) 
                then  role
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1)) 
                then  role
                else null 
            end as role,
            case
                when  'person' in (select column_name from column_level_access)
                then  person
                when (select exists( select person from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))
                then  person
                else null
            end as person,
            case 
            when 'created_at' in (select column_name from column_level_access) 
                then created_at 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1)) 
                then created_at
                else null 
            end as created_at,
            case 
                when 'created_by' in (select column_name from column_level_access) 
                then created_by 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1)) 
                then created_by
                else null 
            end as created_by,
            case 
                when 'modified_at' in (select column_name from column_level_access) 
                then modified_at 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1)) 
                then modified_at
                else null 
            end as modified_at,
            case 
                when 'modified_by' in (select column_name from column_level_access) 
                then modified_by 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1)) 
                then modified_by
                else null 
            end as modified_by,
            case 
                when 'owning_person' in (select column_name from column_level_access) 
                then owning_person 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1)) 
                then owning_person
                else null 
            end as owning_person,
            case 
                when 'owning_group' in (select column_name from column_level_access) 
                then owning_group 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1)) 
                then owning_group
                else null 
            end as owning_group
            from admin.role_memberships
            where id in (select row from row_level_access) or 
                (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1));
        """.trimIndent()

        val jdbcResult = jdbcTemplate.queryForRowSet(listSQL, paramSource)

        while (jdbcResult.next()) {

            var id: Int? = jdbcResult.getInt("id")
            if (jdbcResult.wasNull()) id = null

            var globalRole: Int? = jdbcResult.getInt("role")
            if (jdbcResult.wasNull()) globalRole = null

            var person: Int? = jdbcResult.getInt("person")
            if (jdbcResult.wasNull()) person = null

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

            items.add(
                    GlobalRoleMemberships(
                            id = id,
                            globalRole = globalRole,
                            person = person,
                            createdAt = createdAt,
                            createdBy = createdBy,
                            modifiedAt = modifiedAt,
                            modifiedBy = modifiedBy,
                            owningPerson = owningPerson,
                            owningGroup = owningGroup,
                    )
            )
        }


        return GlobalRoleMembershipsReturn(ErrorType.NoError, items)
    }

}