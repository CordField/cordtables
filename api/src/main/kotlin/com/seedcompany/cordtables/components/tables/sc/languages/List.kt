package com.seedcompany.cordtables.components.tables.sc.languages

import com.seedcompany.cordtables.common.CommonSensitivity
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
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
import javax.sql.rowset.serial.SerialArray


data class ScLanguagesListRequest(
        val token: String?,
        val page: Int,
        val resultsPerPage: Int
)

data class ScLanguagesListResponse(
        val error: ErrorType,
        val size: Int,
        val languages: MutableList<Language>?,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScLanguagesList")
class List(
        @Autowired
        val util: Utility,

        @Autowired
        val ds: DataSource,

        @Autowired
        val secureList: GetSecureListQuery,
) {

        var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)
        var totalRows: Int = 0;

        @PostMapping("sc/languages/list")
        @ResponseBody
        fun listHandler(@RequestBody req: ScLanguagesListRequest): ScLanguagesListResponse {
                var data: MutableList<Language> = mutableListOf()
                if (req.token == null) return ScLanguagesListResponse(ErrorType.TokenNotFound, size=0, mutableListOf())

                var offset = (req.page-1)*req.resultsPerPage


                val paramSource = MapSqlParameterSource()
                paramSource.addValue("token", req.token)
                paramSource.addValue("limit", req.resultsPerPage)
                paramSource.addValue("offset", offset)
                //language=SQL
                val query = """with row_level_access as
(
           select     row
           from       admin.group_row_access  as a
           inner join admin.group_memberships as b
           on         a.group_id = b.group_id
           inner join admin.tokens as c
           on         b.person = c.person
           where      a.table_name = 'sc.languages'
           and        c.token = :token ), public_row_level_access as
(
           select     row
           from       admin.group_row_access  as a
           inner join admin.group_memberships as b
           on         a.group_id = b.group_id
           inner join admin.tokens as c
           on         b.person = c.person
           where      a.table_name = 'sc.languages'
           and        c.token = 'public' ), column_level_access as
(
           select     column_name
           from       admin.role_column_grants a
           inner join admin.role_memberships b
           on         a.role = b.role
           inner join admin.tokens c
           on         b.person = c.person
           where      a.table_name = 'sc.languages'
           and        c.token = :token ), public_column_level_access as
(
           select     column_name
           from       admin.role_column_grants a
           inner join admin.role_memberships b
           on         a.role = b.role
           inner join admin.tokens c
           on         b.person = c.person
           where      a.table_name = 'sc.languages'
           and        c.token = 'public' )
select
         case
                  when 'id' in
                           (
                                  select column_name
                                  from   column_level_access) then id
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = '${util.adminRole()}')) then id
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then id
                  when 'id' in
                           (
                                  select column_name
                                  from   public_column_level_access) then id
                  else null
         end as id ,
         case
                  when 'ethnologue' in
                           (
                                  select column_name
                                  from   column_level_access) then ethnologue
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = '${util.adminRole()}')) then ethnologue
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then ethnologue
                  when 'ethnologue' in
                           (
                                  select column_name
                                  from   public_column_level_access) then ethnologue
                  else null
         end as ethnologue ,
         case
                  when 'name' in
                           (
                                  select column_name
                                  from   column_level_access) then name
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = '${util.adminRole()}')) then name
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then name
                  when 'name' in
                           (
                                  select column_name
                                  from   public_column_level_access) then name
                  else null
         end as name ,
         case
                  when 'display_name' in
                           (
                                  select column_name
                                  from   column_level_access) then display_name
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = '${util.adminRole()}')) then display_name
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then display_name
                  when 'display_name' in
                           (
                                  select column_name
                                  from   public_column_level_access) then display_name
                  else null
         end as display_name ,
         case
                  when 'display_name_pronunciation' in
                           (
                                  select column_name
                                  from   column_level_access) then display_name_pronunciation
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = '${util.adminRole()}')) then display_name_pronunciation
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then display_name_pronunciation
                  when 'display_name_pronunciation' in
                           (
                                  select column_name
                                  from   public_column_level_access) then display_name_pronunciation
                  else null
         end as display_name_pronunciation ,
         case
                  when 'tags' in
                           (
                                  select column_name
                                  from   column_level_access) then tags
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = '${util.adminRole()}')) then tags
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then tags
                  when 'tags' in
                           (
                                  select column_name
                                  from   public_column_level_access) then tags
                  else null
         end as tags ,
         case
                  when 'preset_inventory' in
                           (
                                  select column_name
                                  from   column_level_access) then preset_inventory
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)

                                                and    role = '${util.adminRole()}')) then preset_inventory
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then preset_inventory
                  when 'preset_inventory' in
                           (
                                  select column_name
                                  from   public_column_level_access) then preset_inventory
                  else null
         end as preset_inventory ,
         case
                  when 'is_dialect' in
                           (
                                  select column_name
                                  from   column_level_access) then is_dialect
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = '${util.adminRole()}')) then is_dialect
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then is_dialect
                  when 'is_dialect' in
                           (
                                  select column_name
                                  from   public_column_level_access) then is_dialect
                  else null
         end as is_dialect ,
         case
                  when 'is_sign_language' in
                           (
                                  select column_name
                                  from   column_level_access) then is_sign_language
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = '${util.adminRole()}')) then is_sign_language
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then is_sign_language
                  when 'is_sign_language' in
                           (
                                  select column_name
                                  from   public_column_level_access) then is_sign_language
                  else null
         end as is_sign_language ,
         case
                  when 'is_least_of_these' in
                           (
                                  select column_name
                                  from   column_level_access) then is_least_of_these
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                       and    role = '${util.adminRole()}')) then is_least_of_these
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then is_least_of_these
                  when 'is_least_of_these' in
                           (
                                  select column_name
                                  from   public_column_level_access) then is_least_of_these
                  else null
         end as is_least_of_these ,
         case
                  when 'least_of_these_reason' in
                           (
                                  select column_name
                                  from   column_level_access) then least_of_these_reason
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = '${util.adminRole()}')) then least_of_these_reason
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then least_of_these_reason
                  when 'least_of_these_reason' in
                           (
                                  select column_name
                                  from   public_column_level_access) then least_of_these_reason
                  else null
         end as least_of_these_reason ,
         case
                  when 'population_override' in
                           (
                                  select column_name
                                  from   column_level_access) then population_override
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = '${util.adminRole()}')) then population_override
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then population_override
                  when 'population_override' in
                           (
                                  select column_name
                                  from   public_column_level_access) then population_override
                  else null
         end as population_override ,
         case
                  when 'registry_of_dialects_code' in
                           (
                                  select column_name
                                  from   column_level_access) then registry_of_dialects_code
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = '${util.adminRole()}')) then registry_of_dialects_code
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then registry_of_dialects_code
                  when 'registry_of_dialects_code' in
                           (
                                  select column_name
                                  from   public_column_level_access) then registry_of_dialects_code
                  else null
         end as registry_of_dialects_code ,
         case
                  when 'sensitivity' in
                           (
                                  select column_name
                                  from   column_level_access) then sensitivity
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = '${util.adminRole()}')) then sensitivity
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then sensitivity
                  when 'sensitivity' in
                           (
                                  select column_name
                                  from   public_column_level_access) then sensitivity
                  else null
         end as sensitivity ,
         case
                  when 'sign_language_code' in
                           (
                                  select column_name
                                  from   column_level_access) then sign_language_code
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = '${util.adminRole()}')) then sign_language_code
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then sign_language_code
                  when 'sign_language_code' in
                           (
                                  select column_name
                                  from   public_column_level_access) then sign_language_code
                  else null
         end as sign_language_code ,
         case
                  when 'sponsor_estimated_end_date' in
                           (
                                  select column_name
                                  from   column_level_access) then sponsor_estimated_end_date
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = '${util.adminRole()}')) then sponsor_estimated_end_date
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then sponsor_estimated_end_date
                  when 'sponsor_estimated_end_date' in
                           (
                                  select column_name
                                  from   public_column_level_access) then sponsor_estimated_end_date
                  else null
         end as sponsor_estimated_end_date ,
         case
                  when 'prioritization' in
                           (
                                  select column_name
                                  from   column_level_access) then prioritization
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = '${util.adminRole()}')) then prioritization
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then prioritization
                  when 'prioritization' in
                           (
                                  select column_name
                                  from   public_column_level_access) then prioritization
                  else null
         end as prioritization ,
         case
                  when 'progress_bible' in
                           (
                                  select column_name
                                  from   column_level_access) then progress_bible
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = '${util.adminRole()}')) then progress_bible
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then progress_bible
                  when 'progress_bible' in
                           (
                                  select column_name
                                  from   public_column_level_access) then progress_bible
                  else null
         end as progress_bible ,
         case
                  when 'location_long' in
                           (
                                  select column_name
                                  from   column_level_access) then location_long
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = '${util.adminRole()}')) then location_long
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then location_long
                  when 'location_long' in
                           (
                                  select column_name
                                  from   public_column_level_access) then location_long
                  else null
         end as location_long ,
         case
                  when 'island' in
                           (
                                  select column_name
                                  from   column_level_access) then island
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = '${util.adminRole()}')) then island
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then island
                  when 'island' in
                           (
                                  select column_name
                                  from   public_column_level_access) then island
                  else null
         end as island ,
         case
                  when 'province' in
                           (
                                  select column_name
                                  from   column_level_access) then province
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                      and    role = '${util.adminRole()}')) then province
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then province
                  when 'province' in
                           (
                                  select column_name
                                  from   public_column_level_access) then province
                  else null
         end as province ,
         case
                  when 'first_language_population' in
                           (
                                  select column_name
                                  from   column_level_access) then first_language_population
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = '${util.adminRole()}')) then first_language_population
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then first_language_population
                  when 'first_language_population' in
                           (
                                  select column_name
                                  from   public_column_level_access) then first_language_population
                  else null
         end as first_language_population ,
         case
                  when 'population_value' in
                           (
                                  select column_name
                                  from   column_level_access) then population_value
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = '${util.adminRole()}')) then population_value
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then population_value
                  when 'population_value' in
                           (
                                  select column_name
                                  from   public_column_level_access) then population_value
                  else null
         end as population_value ,
         case
                  when 'egids_level' in
                           (
                                  select column_name
                                  from   column_level_access) then egids_level
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = '${util.adminRole()}')) then egids_level
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then egids_level
                  when 'egids_level' in
                           (
                                  select column_name
                                  from   public_column_level_access) then egids_level
                  else null
         end as egids_level ,
         case
                  when 'egids_value' in
                           (
                                  select column_name
                                  from   column_level_access) then egids_value
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role ='${util.adminRole()}')) then egids_value
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then egids_value
                  when 'egids_value' in
                           (
                                  select column_name
                                  from   public_column_level_access) then egids_value
                  else null
         end as egids_value ,
         case
                  when 'least_reached_progress_jps_level' in
                           (
                                  select column_name
                                  from   column_level_access) then least_reached_progress_jps_level
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role ='${util.adminRole()}')) then least_reached_progress_jps_level
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then least_reached_progress_jps_level
                  when 'least_reached_progress_jps_level' in
                           (
                                  select column_name
                                  from   public_column_level_access) then least_reached_progress_jps_level
                  else null
         end as least_reached_progress_jps_level ,
         case
                  when 'least_reached_value' in
                           (
                                  select column_name
                                  from   column_level_access) then least_reached_value
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = '${util.adminRole()}')) then least_reached_value
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then least_reached_value
                  when 'least_reached_value' in
                           (
                                  select column_name
                                  from   public_column_level_access) then least_reached_value
                  else null
         end as least_reached_value ,
         case
                  when 'partner_interest_level' in
                           (
                                  select column_name
                                  from   column_level_access) then partner_interest_level
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = '${util.adminRole()}')) then partner_interest_level
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then partner_interest_level
                  when 'partner_interest_level' in
                           (
                                  select column_name
                                  from   public_column_level_access) then partner_interest_level
                  else null
         end as partner_interest_level ,
         case
                  when 'partner_interest_value' in
                           (
                                  select column_name
                                  from   column_level_access) then partner_interest_value
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = '${util.adminRole()}')) then partner_interest_value
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then partner_interest_value
                  when 'partner_interest_value' in
                           (
                                  select column_name
                                  from   public_column_level_access) then partner_interest_value
                  else null
         end as partner_interest_value ,
         case
                  when 'partner_interest_description' in
                           (
                                  select column_name
                                  from   column_level_access) then partner_interest_description
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = '${util.adminRole()}')) then partner_interest_description
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then partner_interest_description
                  when 'partner_interest_description' in
                           (
                                  select column_name
                                  from   public_column_level_access) then partner_interest_description
                  else null
         end as partner_interest_description ,
         case
                  when 'partner_interest_source' in
                           (
                                  select column_name
                                  from   column_level_access) then partner_interest_source
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = '${util.adminRole()}')) then partner_interest_source
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then partner_interest_source
                  when 'partner_interest_source' in
                           (
                                  select column_name
                                  from   public_column_level_access) then partner_interest_source
                  else null
         end as partner_interest_source ,
         case
                  when 'multiple_languages_leverage_linguistic_level' in
                           (
                                  select column_name
                                  from   column_level_access) then multiple_languages_leverage_linguistic_level
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = '${util.adminRole()}')) then multiple_languages_leverage_linguistic_level
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then multiple_languages_leverage_linguistic_level
                  when 'multiple_languages_leverage_linguistic_level' in
                           (
                                  select column_name
                                  from   public_column_level_access) then multiple_languages_leverage_linguistic_level
                  else null
         end as multiple_languages_leverage_linguistic_level ,
         case
                  when 'multiple_languages_leverage_linguistic_value' in
                           (
                                  select column_name
                                  from   column_level_access) then multiple_languages_leverage_linguistic_value
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = '${util.adminRole()}')) then multiple_languages_leverage_linguistic_value
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then multiple_languages_leverage_linguistic_value
                  when 'multiple_languages_leverage_linguistic_value' in
                           (
                                  select column_name
                                  from   public_column_level_access) then multiple_languages_leverage_linguistic_value
                  else null
         end as multiple_languages_leverage_linguistic_value ,
         case
                  when 'multiple_languages_leverage_linguistic_description' in
                           (
                                  select column_name
                                  from   column_level_access) then multiple_languages_leverage_linguistic_description
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = '${util.adminRole()}')) then multiple_languages_leverage_linguistic_description
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then multiple_languages_leverage_linguistic_description
                  when 'multiple_languages_leverage_linguistic_description' in
                           (
                                  select column_name
                                  from   public_column_level_access) then multiple_languages_leverage_linguistic_description
                  else null
         end as multiple_languages_leverage_linguistic_description ,
         case
                  when 'multiple_languages_leverage_linguistic_source' in
                           (
                                  select column_name
                                  from   column_level_access) then multiple_languages_leverage_linguistic_source
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = '${util.adminRole()}')) then multiple_languages_leverage_linguistic_source
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then multiple_languages_leverage_linguistic_source
                  when 'multiple_languages_leverage_linguistic_source' in
                           (
                                  select column_name
                                  from   public_column_level_access) then multiple_languages_leverage_linguistic_source
                  else null
         end as multiple_languages_leverage_linguistic_source ,
         case
                  when 'multiple_languages_leverage_joint_training_level' in
                           (
                                  select column_name
                                  from   column_level_access) then multiple_languages_leverage_joint_training_level
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = '${util.adminRole()}')) then multiple_languages_leverage_joint_training_level
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then multiple_languages_leverage_joint_training_level
                  when 'multiple_languages_leverage_joint_training_level' in
                           (
                                  select column_name
                                  from   public_column_level_access) then multiple_languages_leverage_joint_training_level
                  else null
         end as multiple_languages_leverage_joint_training_level ,
         case
                  when 'multiple_languages_leverage_joint_training_value' in
                           (
                                  select column_name
                                  from   column_level_access) then multiple_languages_leverage_joint_training_value
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = '${util.adminRole()}')) then multiple_languages_leverage_joint_training_value
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then multiple_languages_leverage_joint_training_value
                  when 'multiple_languages_leverage_joint_training_value' in
                           (
                                  select column_name
                                  from   public_column_level_access) then multiple_languages_leverage_joint_training_value
                  else null
         end as multiple_languages_leverage_joint_training_value ,
         case
                  when 'multiple_languages_leverage_joint_training_description' in
                           (
                                  select column_name
                                  from   column_level_access) then multiple_languages_leverage_joint_training_description
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = '${util.adminRole()}')) then multiple_languages_leverage_joint_training_description
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then multiple_languages_leverage_joint_training_description
                  when 'multiple_languages_leverage_joint_training_description' in
                           (
                                  select column_name
                                  from   public_column_level_access) then multiple_languages_leverage_joint_training_description
                  else null
         end as multiple_languages_leverage_joint_training_description ,
         case
                  when 'multiple_languages_leverage_joint_training_source' in
                           (
                                  select column_name
                                  from   column_level_access) then multiple_languages_leverage_joint_training_source
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = '${util.adminRole()}')) then multiple_languages_leverage_joint_training_source
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then multiple_languages_leverage_joint_training_source
                  when 'multiple_languages_leverage_joint_training_source' in
                           (
                                  select column_name
                                  from   public_column_level_access) then multiple_languages_leverage_joint_training_source
                  else null
         end as multiple_languages_leverage_joint_training_source ,
         case
                  when 'lang_comm_int_in_language_development_level' in
                           (
                                  select column_name
                                  from   column_level_access) then lang_comm_int_in_language_development_level
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = '${util.adminRole()}')) then lang_comm_int_in_language_development_level
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then lang_comm_int_in_language_development_level
                  when 'lang_comm_int_in_language_development_level' in
                           (
                                  select column_name
                                  from   public_column_level_access) then lang_comm_int_in_language_development_level
                  else null
         end as lang_comm_int_in_language_development_level ,
         case
                  when 'lang_comm_int_in_language_development_value' in
                           (
                                  select column_name
                                  from   column_level_access) then lang_comm_int_in_language_development_value
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = '${util.adminRole()}')) then lang_comm_int_in_language_development_value
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then lang_comm_int_in_language_development_value
                  when 'lang_comm_int_in_language_development_value' in
                           (
                                  select column_name
                                  from   public_column_level_access) then lang_comm_int_in_language_development_value
                  else null
         end as lang_comm_int_in_language_development_value ,
         case
                  when 'lang_comm_int_in_language_development_description' in
                           (
                                  select column_name
                                  from   column_level_access) then lang_comm_int_in_language_development_description
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = '${util.adminRole()}')) then lang_comm_int_in_language_development_description
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then lang_comm_int_in_language_development_description
                  when 'lang_comm_int_in_language_development_description' in
                           (
                                  select column_name
                                  from   public_column_level_access) then lang_comm_int_in_language_development_description
                  else null
         end as lang_comm_int_in_language_development_description ,
         case
                  when 'lang_comm_int_in_language_development_source' in
                           (
                                  select column_name
                                  from   column_level_access) then lang_comm_int_in_language_development_source
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = '${util.adminRole()}')) then lang_comm_int_in_language_development_source
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then lang_comm_int_in_language_development_source
                  when 'lang_comm_int_in_language_development_source' in
                           (
                                  select column_name
                                  from   public_column_level_access) then lang_comm_int_in_language_development_source
                  else null
         end as lang_comm_int_in_language_development_source ,
         case
                  when 'lang_comm_int_in_scripture_translation_level' in
                           (
                                  select column_name
                                  from   column_level_access) then lang_comm_int_in_scripture_translation_level
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = '${util.adminRole()}')) then lang_comm_int_in_scripture_translation_level
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then lang_comm_int_in_scripture_translation_level
                  when 'lang_comm_int_in_scripture_translation_level' in
                           (
                                  select column_name
                                  from   public_column_level_access) then lang_comm_int_in_scripture_translation_level
                  else null
         end as lang_comm_int_in_scripture_translation_level ,
         case
                  when 'lang_comm_int_in_scripture_translation_value' in
                           (
                                  select column_name
                                  from   column_level_access) then lang_comm_int_in_scripture_translation_value
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = '${util.adminRole()}')) then lang_comm_int_in_scripture_translation_value
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then lang_comm_int_in_scripture_translation_value
                  when 'lang_comm_int_in_scripture_translation_value' in
                           (
                                  select column_name
                                  from   public_column_level_access) then lang_comm_int_in_scripture_translation_value
                  else null
         end as lang_comm_int_in_scripture_translation_value ,
         case
                  when 'lang_comm_int_in_scripture_translation_description' in
                           (
                                  select column_name
                                  from   column_level_access) then lang_comm_int_in_scripture_translation_description
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = '${util.adminRole()}')) then lang_comm_int_in_scripture_translation_description
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then lang_comm_int_in_scripture_translation_description
                  when 'lang_comm_int_in_scripture_translation_description' in
                           (
                                  select column_name
                                  from   public_column_level_access) then lang_comm_int_in_scripture_translation_description
                  else null
         end as lang_comm_int_in_scripture_translation_description ,
         case
                  when 'lang_comm_int_in_scripture_translation_source' in
                           (
                                  select column_name
                                  from   column_level_access) then lang_comm_int_in_scripture_translation_source
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = '${util.adminRole()}')) then lang_comm_int_in_scripture_translation_source
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then lang_comm_int_in_scripture_translation_source
                  when 'lang_comm_int_in_scripture_translation_source' in
                           (
                                  select column_name
                                  from   public_column_level_access) then lang_comm_int_in_scripture_translation_source
                  else null
         end as lang_comm_int_in_scripture_translation_source ,
         case
                  when 'access_to_scripture_in_lwc_level' in
                           (
                                  select column_name
                                  from   column_level_access) then access_to_scripture_in_lwc_level
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = '${util.adminRole()}')) then access_to_scripture_in_lwc_level
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then access_to_scripture_in_lwc_level
                  when 'access_to_scripture_in_lwc_level' in
                           (
                                  select column_name
                                  from   public_column_level_access) then access_to_scripture_in_lwc_level
                  else null
         end as access_to_scripture_in_lwc_level ,
         case
                  when 'access_to_scripture_in_lwc_value' in
                           (
                                  select column_name
                                  from   column_level_access) then access_to_scripture_in_lwc_value
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = '${util.adminRole()}')) then access_to_scripture_in_lwc_value
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then access_to_scripture_in_lwc_value
                  when 'access_to_scripture_in_lwc_value' in
                           (
                                  select column_name
                                  from   public_column_level_access) then access_to_scripture_in_lwc_value
                  else null
         end as access_to_scripture_in_lwc_value ,
         case
                  when 'access_to_scripture_in_lwc_description' in
                           (
                                  select column_name
                                  from   column_level_access) then access_to_scripture_in_lwc_description
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = '${util.adminRole()}')) then access_to_scripture_in_lwc_description
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then access_to_scripture_in_lwc_description
                  when 'access_to_scripture_in_lwc_description' in
                           (
                                  select column_name
                                  from   public_column_level_access) then access_to_scripture_in_lwc_description
                  else null
         end as access_to_scripture_in_lwc_description ,
         case
                  when 'access_to_scripture_in_lwc_source' in
                           (
                                  select column_name
                                  from   column_level_access) then access_to_scripture_in_lwc_source
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = '${util.adminRole()}')) then access_to_scripture_in_lwc_source
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then access_to_scripture_in_lwc_source
                  when 'access_to_scripture_in_lwc_source' in
                           (
                                  select column_name
                                  from   public_column_level_access) then access_to_scripture_in_lwc_source
                  else null
         end as access_to_scripture_in_lwc_source ,
         case
                  when 'begin_work_geo_challenges_level' in
                           (
                                  select column_name
                                  from   column_level_access) then begin_work_geo_challenges_level
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = '${util.adminRole()}')) then begin_work_geo_challenges_level
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then begin_work_geo_challenges_level
                  when 'begin_work_geo_challenges_level' in
                           (
                                  select column_name
                                  from   public_column_level_access) then begin_work_geo_challenges_level
                  else null
         end as begin_work_geo_challenges_level ,
         case
                  when 'begin_work_geo_challenges_value' in
                           (
                                  select column_name
                                  from   column_level_access) then begin_work_geo_challenges_value
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = '${util.adminRole()}')) then begin_work_geo_challenges_value
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then begin_work_geo_challenges_value
                  when 'begin_work_geo_challenges_value' in
                           (
                                  select column_name
                                  from   public_column_level_access) then begin_work_geo_challenges_value
                  else null
         end as begin_work_geo_challenges_value ,
         case
                  when 'begin_work_geo_challenges_description' in
                           (
                                  select column_name
                                  from   column_level_access) then begin_work_geo_challenges_description
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = '${util.adminRole()}')) then begin_work_geo_challenges_description
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then begin_work_geo_challenges_description
                  when 'begin_work_geo_challenges_description' in
                           (
                                  select column_name
                                  from   public_column_level_access) then begin_work_geo_challenges_description
                  else null
         end as begin_work_geo_challenges_description ,
         case
                  when 'begin_work_geo_challenges_source' in
                           (
                                  select column_name
                                  from   column_level_access) then begin_work_geo_challenges_source
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = '${util.adminRole()}')) then begin_work_geo_challenges_source
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then begin_work_geo_challenges_source
                  when 'begin_work_geo_challenges_source' in
                           (
                                  select column_name
                                  from   public_column_level_access) then begin_work_geo_challenges_source
                  else null
         end as begin_work_geo_challenges_source ,
         case
                  when 'begin_work_rel_pol_obstacles_level' in
                           (
                                  select column_name
                                  from   column_level_access) then begin_work_rel_pol_obstacles_level
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = '${util.adminRole()}')) then begin_work_rel_pol_obstacles_level
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then begin_work_rel_pol_obstacles_level
                  when 'begin_work_rel_pol_obstacles_level' in
                           (
                                  select column_name
                                  from   public_column_level_access) then begin_work_rel_pol_obstacles_level
                  else null
         end as begin_work_rel_pol_obstacles_level ,
         case
                  when 'begin_work_rel_pol_obstacles_value' in
                           (
                                  select column_name
                                  from   column_level_access) then begin_work_rel_pol_obstacles_value
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = '${util.adminRole()}')) then begin_work_rel_pol_obstacles_value
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then begin_work_rel_pol_obstacles_value
                  when 'begin_work_rel_pol_obstacles_value' in
                           (
                                  select column_name
                                  from   public_column_level_access) then begin_work_rel_pol_obstacles_value
                  else null
         end as begin_work_rel_pol_obstacles_value ,
         case
                  when 'begin_work_rel_pol_obstacles_description' in
                           (
                                  select column_name
                                  from   column_level_access) then begin_work_rel_pol_obstacles_description
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = '${util.adminRole()}')) then begin_work_rel_pol_obstacles_description
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then begin_work_rel_pol_obstacles_description
                  when 'begin_work_rel_pol_obstacles_description' in
                           (
                                  select column_name
                                  from   public_column_level_access) then begin_work_rel_pol_obstacles_description
                  else null
         end as begin_work_rel_pol_obstacles_description ,
         case
                  when 'begin_work_rel_pol_obstacles_source' in
                           (
                                  select column_name
                                  from   column_level_access) then begin_work_rel_pol_obstacles_source
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = '${util.adminRole()}')) then begin_work_rel_pol_obstacles_source
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then begin_work_rel_pol_obstacles_source
                  when 'begin_work_rel_pol_obstacles_source' in
                           (
                                  select column_name
                                  from   public_column_level_access) then begin_work_rel_pol_obstacles_source
                  else null
         end as begin_work_rel_pol_obstacles_source ,
         case
                  when 'suggested_strategies' in
                           (
                                  select column_name
                                  from   column_level_access) then suggested_strategies
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = '${util.adminRole()}')) then suggested_strategies
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then suggested_strategies
                  when 'suggested_strategies' in
                           (
                                  select column_name
                                  from   public_column_level_access) then suggested_strategies
                  else null
         end as suggested_strategies ,
         case
                  when 'comments' in
                           (
                                  select column_name
                                  from   column_level_access) then comments
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = '${util.adminRole()}')) then comments
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then comments
                  when 'comments' in
                           (
                                  select column_name
                                  from   public_column_level_access) then comments
                  else null
         end as comments ,
         case
                  when 'created_at' in
                           (
                                  select column_name
                                  from   column_level_access) then created_at
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = '${util.adminRole()}')) then created_at
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then created_at
                  when 'created_at' in
                           (
                                  select column_name
                                  from   public_column_level_access) then created_at
                  else null
         end as created_at ,
         case
                  when 'created_by' in
                           (
                                  select column_name
                                  from   column_level_access) then created_by
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = '${util.adminRole()}')) then created_by
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then created_by
                  when 'created_by' in
                           (
                                  select column_name
                                  from   public_column_level_access) then created_by
                  else null
         end as created_by ,
         case
                  when 'modified_at' in
                           (
                                  select column_name
                                  from   column_level_access) then modified_at
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = '${util.adminRole()}')) then modified_at
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then modified_at
                  when 'modified_at' in
                           (
                                  select column_name
                                  from   public_column_level_access) then modified_at
                  else null
         end as modified_at ,
         case
                  when 'modified_by' in
                           (
                                  select column_name
                                  from   column_level_access) then modified_by
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = '${util.adminRole()}')) then modified_by
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then modified_by
                  when 'modified_by' in
                           (
                                  select column_name
                                  from   public_column_level_access) then modified_by
                  else null
         end as modified_by ,
         case
                  when 'owning_person' in
                           (
                                  select column_name
                                  from   column_level_access) then owning_person
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = '${util.adminRole()}')) then owning_person
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then owning_person
                  when 'owning_person' in
                           (
                                  select column_name
                                  from   public_column_level_access) then owning_person
                  else null
         end as owning_person ,
         case
                  when 'owning_group' in
                           (
                                  select column_name
                                  from   column_level_access) then owning_group
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = '${util.adminRole()}')) then owning_group
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then owning_group
                  when 'owning_group' in
                           (
                                  select column_name
                                  from   public_column_level_access) then owning_group
                  else null
         end as owning_group,
         case
                  when 'coordinates' in
                           (
                                  select column_name
                                  from   column_level_access) then common.ST_AsLatLonText(coordinates::text)
                  when
                           (
                                  select exists
                                         (
                                                select id
                                                from   admin.role_memberships
                                                where  person =
                                                       (
                                                              select person
                                                              from   admin.tokens
                                                              where  token = :token)
                                                and    role = '${util.adminRole()}')) then common.ST_AsLatLonText(coordinates::text)
                  when owning_person =
                           (
                                  select person
                                  from   admin.tokens
                                  where  token = :token) then common.ST_AsLatLonText(coordinates::text)
                  when 'coordinates' in
                           (
                                  select column_name
                                  from   public_column_level_access) then common.ST_AsLatLonText(coordinates::text)
                  else null
         end as coordinates
from     sc.languages
where    id in
         (
                select row
                from   row_level_access)
or
         (
                select exists
                       (
                              select id
                              from   admin.role_memberships
                              where  person =
                                     (
                                            select person
                                            from   admin.tokens
                                            where  token = :token)
                              and    role = '${util.adminRole()}'))
or       owning_person =
         (
                select person
                from   admin.tokens
                where  token = :token)
or       id in
         (
                select row
                from   public_row_level_access)
order by id
""".trimIndent()

                var limitQuery = "$query LIMIT :limit OFFSET :offset ";


                try {
                        // var countQuery = "$query return id ";

                        val resultRows = jdbcTemplate.queryForRowSet(query, paramSource)
                        resultRows.last();
                        totalRows = resultRows.getRow();

                        //println(totalRows)

                        val jdbcResult = jdbcTemplate.queryForRowSet(limitQuery, paramSource)
                        while (jdbcResult.next()) {

                                var id: String? = jdbcResult.getString("id")
                                if (jdbcResult.wasNull()) id = null

                                var ethnologue: String? = jdbcResult.getString("ethnologue")
                                if (jdbcResult.wasNull()) ethnologue = null

                                var name: String? = jdbcResult.getString("name")
                                if (jdbcResult.wasNull()) name = null

                                var display_name: String? = jdbcResult.getString("display_name")
                                if (jdbcResult.wasNull()) display_name = null

                                var display_name_pronunciation: String? = jdbcResult.getString("display_name_pronunciation")
                                if (jdbcResult.wasNull()) display_name_pronunciation = null

                                // var tags: String? = jdbcResult.getString("tags")
                                var tags: Array<out Any>? = (jdbcResult.getObject("tags") as? SerialArray)?.array as Array<out Any>?
                                if (jdbcResult.wasNull()) tags = null

                                var preset_inventory: Boolean? = jdbcResult.getBoolean("preset_inventory")
                                if (jdbcResult.wasNull()) preset_inventory = null

                                var is_dialect: Boolean? = jdbcResult.getBoolean("is_dialect")
                                if (jdbcResult.wasNull()) is_dialect = null

                                var is_sign_language: Boolean? = jdbcResult.getBoolean("is_sign_language")
                                if (jdbcResult.wasNull()) is_sign_language = null

                                var is_least_of_these: Boolean? = jdbcResult.getBoolean("is_least_of_these")
                                if (jdbcResult.wasNull()) is_least_of_these = null

                                var least_of_these_reason: String? = jdbcResult.getString("least_of_these_reason")
                                if (jdbcResult.wasNull()) least_of_these_reason = null

                                var population_override: Int? = jdbcResult.getInt("population_override")
                                if (jdbcResult.wasNull()) population_override = null

                                var registry_of_dialects_code: String? = jdbcResult.getString("registry_of_dialects_code")
                                if (jdbcResult.wasNull()) registry_of_dialects_code = null

                                var sensitivity: String? = jdbcResult.getString("sensitivity")
                                if (jdbcResult.wasNull()) sensitivity = null

                                var sign_language_code: String? = jdbcResult.getString("sign_language_code")
                                if (jdbcResult.wasNull()) sign_language_code = null

                                var sponsor_estimated_end_date: String? = jdbcResult.getString("sponsor_estimated_end_date")
                                if (jdbcResult.wasNull()) sponsor_estimated_end_date = null

                                var prioritization: Double? = jdbcResult.getDouble("prioritization")
                                if (jdbcResult.wasNull()) prioritization = null

                                var progress_bible: Boolean? = jdbcResult.getBoolean("progress_bible")
                                if (jdbcResult.wasNull()) progress_bible = null

                                var island: String? = jdbcResult.getString("island")
                                if (jdbcResult.wasNull()) island = null

                                var province: String? = jdbcResult.getString("province")
                                if (jdbcResult.wasNull()) province = null

                                var first_language_population: Int? = jdbcResult.getInt("first_language_population")
                                if (jdbcResult.wasNull()) first_language_population = null

                                var population_value: Double? = jdbcResult.getDouble("population_value")
                                if (jdbcResult.wasNull()) population_value = null

                                var egids_level: String? = jdbcResult.getString("egids_level")
                                if (jdbcResult.wasNull()) egids_level = null

                                var egids_value: Double? = jdbcResult.getDouble("egids_value")
                                if (jdbcResult.wasNull()) egids_value = null

                                var least_reached_progress_jps_level: String? = jdbcResult.getString("least_reached_progress_jps_level")
                                if (jdbcResult.wasNull()) least_reached_progress_jps_level = null

                                var least_reached_value: Double? = jdbcResult.getDouble("least_reached_value")
                                if (jdbcResult.wasNull()) least_reached_value = null

                                var partner_interest_level: String? = jdbcResult.getString("partner_interest_level")
                                if (jdbcResult.wasNull()) partner_interest_level = null

                                var partner_interest_value: Double? = jdbcResult.getDouble("partner_interest_value")
                                if (jdbcResult.wasNull()) partner_interest_value = null

                                var partner_interest_description: String? = jdbcResult.getString("partner_interest_description")
                                if (jdbcResult.wasNull()) partner_interest_description = null

                                var partner_interest_source: String? = jdbcResult.getString("partner_interest_source")
                                if (jdbcResult.wasNull()) partner_interest_source = null

                                var multiple_languages_leverage_linguistic_level: String? =
                                        jdbcResult.getString("multiple_languages_leverage_linguistic_level")
                                if (jdbcResult.wasNull()) multiple_languages_leverage_linguistic_level = null

                                var multiple_languages_leverage_linguistic_value: Double? =
                                        jdbcResult.getDouble("multiple_languages_leverage_linguistic_value")
                                if (jdbcResult.wasNull()) multiple_languages_leverage_linguistic_value = null

                                var multiple_languages_leverage_linguistic_description: String? =
                                        jdbcResult.getString("multiple_languages_leverage_linguistic_description")
                                if (jdbcResult.wasNull()) multiple_languages_leverage_linguistic_description = null

                                var multiple_languages_leverage_linguistic_source: String? =
                                        jdbcResult.getString("multiple_languages_leverage_linguistic_source")
                                if (jdbcResult.wasNull()) multiple_languages_leverage_linguistic_source = null

                                var multiple_languages_leverage_joint_training_level: String? =
                                        jdbcResult.getString("multiple_languages_leverage_joint_training_level")
                                if (jdbcResult.wasNull()) multiple_languages_leverage_joint_training_level = null

                                var multiple_languages_leverage_joint_training_value: Double? =
                                        jdbcResult.getDouble("multiple_languages_leverage_joint_training_value")
                                if (jdbcResult.wasNull()) multiple_languages_leverage_joint_training_value = null

                                var multiple_languages_leverage_joint_training_description: String? =
                                        jdbcResult.getString("multiple_languages_leverage_joint_training_description")
                                if (jdbcResult.wasNull()) multiple_languages_leverage_joint_training_description = null

                                var multiple_languages_leverage_joint_training_source: String? =
                                        jdbcResult.getString("multiple_languages_leverage_joint_training_source")
                                if (jdbcResult.wasNull()) multiple_languages_leverage_joint_training_source = null

                                var lang_comm_int_in_language_development_level: String? =
                                        jdbcResult.getString("lang_comm_int_in_language_development_level")
                                if (jdbcResult.wasNull()) lang_comm_int_in_language_development_level = null

                                var lang_comm_int_in_language_development_value: Double? =
                                        jdbcResult.getDouble("lang_comm_int_in_language_development_value")
                                if (jdbcResult.wasNull()) lang_comm_int_in_language_development_value = null

                                var lang_comm_int_in_language_development_description: String? =
                                        jdbcResult.getString("lang_comm_int_in_language_development_description")
                                if (jdbcResult.wasNull()) lang_comm_int_in_language_development_description = null

                                var lang_comm_int_in_language_development_source: String? =
                                        jdbcResult.getString("lang_comm_int_in_language_development_source")
                                if (jdbcResult.wasNull()) lang_comm_int_in_language_development_source = null

                                var lang_comm_int_in_scripture_translation_level: String? =
                                        jdbcResult.getString("lang_comm_int_in_scripture_translation_level")
                                if (jdbcResult.wasNull()) lang_comm_int_in_scripture_translation_level = null

                                var lang_comm_int_in_scripture_translation_value: Double? =
                                        jdbcResult.getDouble("lang_comm_int_in_scripture_translation_value")
                                if (jdbcResult.wasNull()) lang_comm_int_in_scripture_translation_value = null

                                var lang_comm_int_in_scripture_translation_description: String? =
                                        jdbcResult.getString("lang_comm_int_in_scripture_translation_description")
                                if (jdbcResult.wasNull()) lang_comm_int_in_scripture_translation_description = null

                                var lang_comm_int_in_scripture_translation_source: String? =
                                        jdbcResult.getString("lang_comm_int_in_scripture_translation_source")
                                if (jdbcResult.wasNull()) lang_comm_int_in_scripture_translation_source = null

                                var access_to_scripture_in_lwc_level: String? = jdbcResult.getString("access_to_scripture_in_lwc_level")
                                if (jdbcResult.wasNull()) access_to_scripture_in_lwc_level = null

                                var access_to_scripture_in_lwc_value: Double? = jdbcResult.getDouble("access_to_scripture_in_lwc_value")
                                if (jdbcResult.wasNull()) access_to_scripture_in_lwc_value = null

                                var access_to_scripture_in_lwc_description: String? =
                                        jdbcResult.getString("access_to_scripture_in_lwc_description")
                                if (jdbcResult.wasNull()) access_to_scripture_in_lwc_description = null

                                var access_to_scripture_in_lwc_source: String? =
                                        jdbcResult.getString("access_to_scripture_in_lwc_source")
                                if (jdbcResult.wasNull()) access_to_scripture_in_lwc_source = null

                                var begin_work_geo_challenges_level: String? = jdbcResult.getString("begin_work_geo_challenges_level")
                                if (jdbcResult.wasNull()) begin_work_geo_challenges_level = null

                                var begin_work_geo_challenges_value: Double? = jdbcResult.getDouble("begin_work_geo_challenges_value")
                                if (jdbcResult.wasNull()) begin_work_geo_challenges_value = null

                                var begin_work_geo_challenges_description: String? =
                                        jdbcResult.getString("begin_work_geo_challenges_description")
                                if (jdbcResult.wasNull()) begin_work_geo_challenges_description = null

                                var begin_work_geo_challenges_source: String? = jdbcResult.getString("begin_work_geo_challenges_source")
                                if (jdbcResult.wasNull()) begin_work_geo_challenges_source = null

                                var begin_work_rel_pol_obstacles_level: String? =
                                        jdbcResult.getString("begin_work_rel_pol_obstacles_level")
                                if (jdbcResult.wasNull()) begin_work_rel_pol_obstacles_level = null

                                var begin_work_rel_pol_obstacles_value: Double? =
                                        jdbcResult.getDouble("begin_work_rel_pol_obstacles_value")
                                if (jdbcResult.wasNull()) begin_work_rel_pol_obstacles_value = null

                                var begin_work_rel_pol_obstacles_description: String? =
                                        jdbcResult.getString("begin_work_rel_pol_obstacles_description")
                                if (jdbcResult.wasNull()) begin_work_rel_pol_obstacles_description = null

                                var begin_work_rel_pol_obstacles_source: String? =
                                        jdbcResult.getString("begin_work_rel_pol_obstacles_source")
                                if (jdbcResult.wasNull()) begin_work_rel_pol_obstacles_source = null

                                var suggested_strategies: String? = jdbcResult.getString("suggested_strategies")
                                if (jdbcResult.wasNull()) suggested_strategies = null

                                var comments: String? = jdbcResult.getString("comments")
                                if (jdbcResult.wasNull()) comments = null

                                var created_at: String? = jdbcResult.getString("created_at")
                                if (jdbcResult.wasNull()) created_at = null

                                var created_by: String? = jdbcResult.getString("created_by")
                                if (jdbcResult.wasNull()) created_by = null

                                var modified_at: String? = jdbcResult.getString("modified_at")
                                if (jdbcResult.wasNull()) modified_at = null

                                var modified_by: String? = jdbcResult.getString("modified_by")
                                if (jdbcResult.wasNull()) modified_by = null

                                var owning_person: String? = jdbcResult.getString("owning_person")
                                if (jdbcResult.wasNull()) owning_person = null

                                var owning_group: String? = jdbcResult.getString("owning_group")
                                if (jdbcResult.wasNull()) owning_group = null

                                var coordinates: String? = jdbcResult.getString("coordinates")
                                if(jdbcResult.wasNull()) coordinates = null

                                data.add(
                                        Language(
                                                id = id,
                                                coordinates = coordinates,
                                                ethnologue = ethnologue,
                                                name = name,
                                                display_name = display_name,
                                                display_name_pronunciation = display_name_pronunciation,
                                                tags = tags,
                                                preset_inventory = preset_inventory,
                                                is_dialect = is_dialect,
                                                is_sign_language = is_sign_language,
                                                is_least_of_these = is_least_of_these,
                                                least_of_these_reason = least_of_these_reason,
                                                population_override = population_override,
                                                registry_of_dialects_code = registry_of_dialects_code,
                                                sensitivity = if (sensitivity == null) null else CommonSensitivity.valueOf(sensitivity),
                                                sign_language_code = sign_language_code,
                                                sponsor_estimated_end_date = sponsor_estimated_end_date,

                                                prioritization = prioritization,
                                                progress_bible = progress_bible,
                                                island = island,
                                                province = province,
                                                first_language_population = first_language_population,
                                                population_value = population_value,
                                                egids_level = if (egids_level == null) null else EgidsScale.valueOf(egids_level),
                                                egids_value = egids_value,
                                                least_reached_progress_jps_level = if (least_reached_progress_jps_level == null) null else LeastReachedProgressScale.valueOf(
                                                        least_reached_progress_jps_level
                                                ),
                                                least_reached_value = least_reached_value,
                                                partner_interest_level = if (partner_interest_level == null) null else PartnerInterestScale.valueOf(
                                                        partner_interest_level
                                                ),
                                                partner_interest_value = partner_interest_value,
                                                partner_interest_description = partner_interest_description,
                                                partner_interest_source = partner_interest_source,
                                                multiple_languages_leverage_linguistic_level = if (multiple_languages_leverage_linguistic_level == null) null else MultipleLanguagesLeverageLinguisticScale.valueOf(
                                                        multiple_languages_leverage_linguistic_level
                                                ),
                                                multiple_languages_leverage_linguistic_value = multiple_languages_leverage_linguistic_value,
                                                multiple_languages_leverage_linguistic_description = multiple_languages_leverage_linguistic_description,
                                                multiple_languages_leverage_linguistic_source = multiple_languages_leverage_linguistic_source,
                                                multiple_languages_leverage_joint_training_level = if (multiple_languages_leverage_joint_training_level == null) null else MultipleLanguagesLeverageJointTrainingScale.valueOf(
                                                        multiple_languages_leverage_joint_training_level
                                                ),
                                                multiple_languages_leverage_joint_training_value = multiple_languages_leverage_joint_training_value,
                                                multiple_languages_leverage_joint_training_description = multiple_languages_leverage_joint_training_description,
                                                multiple_languages_leverage_joint_training_source = multiple_languages_leverage_joint_training_source,
                                                lang_comm_int_in_language_development_level = if (lang_comm_int_in_language_development_level == null) null else LangCommIntInLanguageDevelopmentScale.valueOf(
                                                        lang_comm_int_in_language_development_level
                                                ),
                                                lang_comm_int_in_language_development_value = lang_comm_int_in_language_development_value,
                                                lang_comm_int_in_language_development_description = lang_comm_int_in_language_development_description,
                                                lang_comm_int_in_language_development_source = lang_comm_int_in_language_development_source,
                                                lang_comm_int_in_scripture_translation_level = if (lang_comm_int_in_scripture_translation_level == null) null else LangCommIntInScriptureTranslationScale.valueOf(
                                                        lang_comm_int_in_scripture_translation_level
                                                ),
                                                lang_comm_int_in_scripture_translation_value = lang_comm_int_in_scripture_translation_value,
                                                lang_comm_int_in_scripture_translation_description = lang_comm_int_in_scripture_translation_description,
                                                lang_comm_int_in_scripture_translation_source = lang_comm_int_in_scripture_translation_source,
                                                access_to_scripture_in_lwc_level = if (access_to_scripture_in_lwc_level == null) null else AccessToScriptureInLwcScale.valueOf(
                                                        access_to_scripture_in_lwc_level
                                                ),
                                                access_to_scripture_in_lwc_value = access_to_scripture_in_lwc_value,
                                                access_to_scripture_in_lwc_description = access_to_scripture_in_lwc_description,
                                                access_to_scripture_in_lwc_source = access_to_scripture_in_lwc_source,
                                                begin_work_geo_challenges_level = if (begin_work_geo_challenges_level == null) null else BeginWorkGeoChallengesScale.valueOf(
                                                        begin_work_geo_challenges_level
                                                ),
                                                begin_work_geo_challenges_value = begin_work_geo_challenges_value,
                                                begin_work_geo_challenges_description = begin_work_geo_challenges_description,
                                                begin_work_geo_challenges_source = begin_work_geo_challenges_source,
                                                begin_work_rel_pol_obstacles_level = if (begin_work_rel_pol_obstacles_level == null) null else BeginWorkRelPolObstaclesScale.valueOf(
                                                        begin_work_rel_pol_obstacles_level
                                                ),
                                                begin_work_rel_pol_obstacles_value = begin_work_rel_pol_obstacles_value,
                                                begin_work_rel_pol_obstacles_description = begin_work_rel_pol_obstacles_description,
                                                begin_work_rel_pol_obstacles_source = begin_work_rel_pol_obstacles_source,
                                                suggested_strategies = suggested_strategies,
                                                comments = comments,
                                                created_at = created_at,
                                                created_by = created_by,
                                                modified_at = modified_at,
                                                modified_by = modified_by,
                                                owning_person = owning_person,
                                                owning_group = owning_group
                                        )
                                )
                        }
                } catch (e: SQLException) {
                        println("error while listing ${e.message}")
                        return ScLanguagesListResponse(ErrorType.SQLReadError, size=0, mutableListOf())
                }

                return ScLanguagesListResponse(ErrorType.NoError, size=totalRows, data)
        }
}
