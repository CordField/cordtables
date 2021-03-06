package com.seedcompany.cordtables.components.tables.common.organizations

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


data class CommonOrganizations(
        val id: String?,
        val name: String? = null,
        val sensitivity: String?,
        val primary_location: String? = null,
        val created_at: String?,
        val created_by: String?,
        val modified_at: String?,
        val modified_by: String?,
        val owning_person: String?,
        val owning_group: String?,
)

data class CommonOrganizationsRequest(
        val token: String? = null,
)

data class CommonOrganizationsReturn(
        val error: ErrorType,
        val organizations: List<out CommonOrganizations>?,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonOrganizationsList")
class List(
        @Autowired
        val util: Utility,

        @Autowired
        val ds: DataSource,
) {
    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("common/organizations/list")
    @ResponseBody
    fun listHandler(@RequestBody req: CommonOrganizationsRequest): CommonOrganizationsReturn{

        if (req.token == null) return CommonOrganizationsReturn(ErrorType.TokenNotFound, null)
        if (!util.isAdmin(req.token)) return CommonOrganizationsReturn(ErrorType.AdminOnly, null)

        val items = mutableListOf<CommonOrganizations>()

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
                where a.table_name = 'common.organizations'
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
                where a.table_name = 'common.organizations'
                and c.token = :token
            )
            select  
            case 
                when 'id' in 
                    (select column_name from column_level_access) 
                then id 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = '${util.adminRole()}')) 
                then id 
                else null 
            end as id,
            case 
                when 'name' in (select column_name from column_level_access) 
                then  name
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = '${util.adminRole()}')) 
                then  name
                else null 
            end as name,
            case 
                when 'sensitivity' in (select column_name from column_level_access) 
                then sensitivity 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = '${util.adminRole()}')) 
                then sensitivity
                else null 
            end as sensitivity,
            case 
                when 'primary_location' in (select column_name from column_level_access) 
                then primary_location 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = '${util.adminRole()}')) 
                then primary_location
                else null 
            end as primary_location,
            case
            when 'created_at' in (select column_name from column_level_access) 
                then created_at 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = '${util.adminRole()}')) 
                then created_at
                else null 
            end as created_at,
            case 
                when 'created_by' in (select column_name from column_level_access) 
                then created_by 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = '${util.adminRole()}')) 
                then created_by
                else null 
            end as created_by,
            case 
                when 'modified_at' in (select column_name from column_level_access) 
                then modified_at 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = '${util.adminRole()}')) 
                then modified_at
                else null 
            end as modified_at,
            case 
                when 'modified_by' in (select column_name from column_level_access) 
                then modified_by 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = '${util.adminRole()}')) 
                then modified_by
                else null 
            end as modified_by,
            case 
                when 'owning_person' in (select column_name from column_level_access) 
                then owning_person 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = '${util.adminRole()}')) 
                then owning_person
                else null 
            end as owning_person,
            case 
                when 'owning_group' in (select column_name from column_level_access) 
                then owning_group 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = '${util.adminRole()}')) 
                then owning_group
                else null 
            end as owning_group
            from common.organizations
            where id in (select row from row_level_access) or 
                (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = '${util.adminRole()}'));
        """.trimIndent()

        val jdbcResult = jdbcTemplate.queryForRowSet(listSQL, paramSource)

        while (jdbcResult.next()) {

            var id: String? = jdbcResult.getString("id")
            if (jdbcResult.wasNull()) id = null

            var name: String? = jdbcResult.getString("name")
            if (jdbcResult.wasNull()) name = null

            var sensitivity: String? = jdbcResult.getString("sensitivity")
            if(jdbcResult.wasNull()) sensitivity = null

            var primaryLocation: String? = jdbcResult.getString("primary_location")
            if(jdbcResult.wasNull()) primaryLocation = null

            var createdAt: String? = jdbcResult.getString("created_at")
            if (jdbcResult.wasNull()) createdAt = null

            var createdBy: String? = jdbcResult.getString("created_by")
            if (jdbcResult.wasNull()) createdBy = null

            var modifiedAt: String? = jdbcResult.getString("modified_at")
            if (jdbcResult.wasNull()) modifiedAt = null

            var modifiedBy: String? = jdbcResult.getString("modified_by")
            if (jdbcResult.wasNull()) modifiedBy = null

            var owningPerson: String? = jdbcResult.getString("owning_person")
            if (jdbcResult.wasNull()) owningPerson = null

            var owningGroup: String? = jdbcResult.getString("owning_group")
            if (jdbcResult.wasNull()) owningGroup = null

            items.add(
                CommonOrganizations(
                    id = id,
                    name = name,
                    sensitivity = sensitivity,
                    primary_location = primaryLocation,
                    created_at = createdAt,
                    created_by = createdBy,
                    modified_at = modifiedAt,
                    modified_by = modifiedBy,
                    owning_person = owningPerson,
                    owning_group = owningGroup,
                )
            )
        }


        return CommonOrganizationsReturn(ErrorType.NoError, items)
    }

}
