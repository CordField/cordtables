package com.seedcompany.cordtables.components.tables.languageex

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource


data class LanguageEx(
    val id: Int?,
    val language_name: String?,
    val iso: String?,
    val prioritization: Double?,
    val progress_bible: Boolean?,
    val island: String?,
    val province: String?,
    val first_language_population: Int?,
    val population_value: Double?,
    val egids_level: EgidsScale?,
    val egids_value: Double?,
    val least_reached_progress_jps_level: LeastReachedProgressScale?,
    val least_reached_value: Double?,
    val partner_interest_level: PartnerInterestScale?,
    val partner_interest_value: Double?,
    val partner_interest_description: String?,
    val partner_interest_source: String?,
    val multiple_languages_leverage_linguistic_level: MultipleLanguagesLeverageLinguisticScale?,
    val multiple_languages_leverage_linguistic_value: Double?,
    val multiple_languages_leverage_linguistic_description: String?,
    val multiple_languages_leverage_linguistic_source: String?,
    val multiple_languages_leverage_joint_training_level: MultipleLanguagesLeverageJointTrainingScale?,
    val multiple_languages_leverage_joint_training_value: Double?,
    val multiple_languages_leverage_joint_training_description: String?,
    val multiple_languages_leverage_joint_training_source: String?,
    val lang_comm_int_in_language_development_level: LangCommIntInLanguageDevelopmentScale?,
    val lang_comm_int_in_language_development_value: Double?,
    val lang_comm_int_in_language_development_description: String?,
    val lang_comm_int_in_language_development_source: String?,
    val lang_comm_int_in_scripture_translation_level: LangCommIntInScriptureTranslationScale?,
    val lang_comm_int_in_scripture_translation_value: Double?,
    val lang_comm_int_in_scripture_translation_description: String?,
    val lang_comm_int_in_scripture_translation_source: String?,
    val access_to_scripture_in_lwc_level: AccessToScriptureInLwcScale?,
    val access_to_scripture_in_lwc_value: Double?,
    val access_to_scripture_in_lwc_description: String?,
    val access_to_scripture_in_lwc_source: String?,
    val begin_work_geo_challenges_level: BeginWorkGeoChallengesScale?,
    val begin_work_geo_challenges_value: Double?,
    val begin_work_geo_challenges_description: String?,
    val begin_work_geo_challenges_source: String?,
    val begin_work_rel_pol_obstacles_level: BeginWorkRelPolObstaclesScale?,
    val begin_work_rel_pol_obstacles_value: Double?,
    val begin_work_rel_pol_obstacles_description: String?,
    val begin_work_rel_pol_obstacles_source: String?,
    val suggested_strategies: String?,
    val comments: String?,
    val created_at: String?,
    val created_by: Int?,
    val modified_at: String?,
    val modified_by: Int?,
    val owning_person: Int?,
    val owning_group: Int?
)

data class ReadLanguageExResponse(
    val error: ErrorType,
    val data: MutableList<LanguageEx>?
)

data class ReadLanguageExRequest(
    val token: String?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("LanguageExRead")
class Read(
    @Autowired
    val util: Utility,
    @Autowired
    val ds: DataSource,
) {

    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("language_ex/read")
    @ResponseBody
    fun ReadHandler(@RequestBody req: ReadLanguageExRequest): ReadLanguageExResponse {
        var data: MutableList<LanguageEx> = mutableListOf()
        if (req.token == null) return ReadLanguageExResponse(ErrorType.TokenNotFound, mutableListOf())

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
                when 'language_name' in (select column_name from column_level_access) then language_name 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then language_name
                when owning_person = (select person from admin.tokens where token = :token) then language_name
                else null 
            end as language_name,
            case
                when 'iso' in (select column_name from column_level_access) then iso 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then iso
                when owning_person = (select person from admin.tokens where token = :token) then iso
                else null 
            end as iso,
            case
                when 'prioritization' in (select column_name from column_level_access) then prioritization 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then prioritization 
                when owning_person = (select person from admin.tokens where token = :token) then prioritization
                else null 
            end as prioritization,
            case
                when 'progress_bible' in (select column_name from column_level_access) then progress_bible 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then progress_bible
                when owning_person = (select person from admin.tokens where token = :token) then progress_bible
                else null 
            end as progress_bible,
            case
                when 'island' in (select column_name from column_level_access) then island 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then island
                when owning_person = (select person from admin.tokens where token = :token) then island
                else null 
            end as island,
            case
                when 'province' in (select column_name from column_level_access) then province 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then province
                when owning_person = (select person from admin.tokens where token = :token) then province
                else null 
            end as province,
            case
                when 'first_language_population' in (select column_name from column_level_access) then first_language_population 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then first_language_population
                when owning_person = (select person from admin.tokens where token = :token) then first_language_population
                else null 
            end as first_language_population,
            case
                when 'population_value' in (select column_name from column_level_access) then population_value 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then population_value
                when owning_person = (select person from admin.tokens where token = :token) then population_value
                else null 
            end as population_value,
            case
                when 'egids_level' in (select column_name from column_level_access) then egids_level 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then egids_level
                when owning_person = (select person from admin.tokens where token = :token) then egids_level
                else null 
            end as egids_level,
            case
                when 'egids_value' in (select column_name from column_level_access) then egids_value 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then egids_value
                when owning_person = (select person from admin.tokens where token = :token) then egids_value
                else null 
            end as egids_value,
            case
                when 'least_reached_progress_jps_level' in (select column_name from column_level_access) then least_reached_progress_jps_level 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then least_reached_progress_jps_level
                when owning_person = (select person from admin.tokens where token = :token) then least_reached_progress_jps_level
                else null 
            end as least_reached_progress_jps_level,
            case
                when 'least_reached_value' in (select column_name from column_level_access) then least_reached_value 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then least_reached_value
                when owning_person = (select person from admin.tokens where token = :token) then least_reached_value
                else null 
            end as least_reached_value,
            case
                when 'partner_interest_level' in (select column_name from column_level_access) then partner_interest_level 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then partner_interest_level
                when owning_person = (select person from admin.tokens where token = :token) then partner_interest_level
                else null 
            end as partner_interest_level,
            case
                when 'partner_interest_value' in (select column_name from column_level_access) then partner_interest_value 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then partner_interest_value
                when owning_person = (select person from admin.tokens where token = :token) then partner_interest_value
                else null 
            end as partner_interest_value,
            case
                when 'partner_interest_description' in (select column_name from column_level_access) then partner_interest_description 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then partner_interest_description
                when owning_person = (select person from admin.tokens where token = :token) then partner_interest_description
                else null 
            end as partner_interest_description,
            case
                when 'partner_interest_source' in (select column_name from column_level_access) then partner_interest_source 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then partner_interest_source
                when owning_person = (select person from admin.tokens where token = :token) then partner_interest_source
                else null 
            end as partner_interest_source,
            case
                when 'multiple_languages_leverage_linguistic_level' in (select column_name from column_level_access) then multiple_languages_leverage_linguistic_level 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then multiple_languages_leverage_linguistic_level
                when owning_person = (select person from admin.tokens where token = :token) then multiple_languages_leverage_linguistic_level
                else null 
            end as multiple_languages_leverage_linguistic_level,
            case
                when 'multiple_languages_leverage_linguistic_value' in (select column_name from column_level_access) then multiple_languages_leverage_linguistic_value 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then multiple_languages_leverage_linguistic_value
                when owning_person = (select person from admin.tokens where token = :token) then multiple_languages_leverage_linguistic_value
                else null 
            end as multiple_languages_leverage_linguistic_value,
            case
                when 'multiple_languages_leverage_linguistic_description' in (select column_name from column_level_access) then multiple_languages_leverage_linguistic_description 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then multiple_languages_leverage_linguistic_description
                when owning_person = (select person from admin.tokens where token = :token) then multiple_languages_leverage_linguistic_description
                else null 
            end as multiple_languages_leverage_linguistic_description,
            case
                when 'multiple_languages_leverage_linguistic_source' in (select column_name from column_level_access) then multiple_languages_leverage_linguistic_source 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then multiple_languages_leverage_linguistic_source
                when owning_person = (select person from admin.tokens where token = :token) then multiple_languages_leverage_linguistic_source
                else null 
            end as multiple_languages_leverage_linguistic_source,
            case
                when 'multiple_languages_leverage_joint_training_level' in (select column_name from column_level_access) then multiple_languages_leverage_joint_training_level 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then multiple_languages_leverage_joint_training_level
                when owning_person = (select person from admin.tokens where token = :token) then multiple_languages_leverage_joint_training_level
                else null 
            end as multiple_languages_leverage_joint_training_level,
            case
                when 'multiple_languages_leverage_joint_training_value' in (select column_name from column_level_access) then multiple_languages_leverage_joint_training_value 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then multiple_languages_leverage_joint_training_value
                when owning_person = (select person from admin.tokens where token = :token) then multiple_languages_leverage_joint_training_value
                else null 
            end as multiple_languages_leverage_joint_training_value,
            case
                when 'multiple_languages_leverage_joint_training_description' in (select column_name from column_level_access) then multiple_languages_leverage_joint_training_description 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then multiple_languages_leverage_joint_training_description
                when owning_person = (select person from admin.tokens where token = :token) then multiple_languages_leverage_joint_training_description
                else null 
            end as multiple_languages_leverage_joint_training_description,
            case
                when 'multiple_languages_leverage_joint_training_source' in (select column_name from column_level_access) then multiple_languages_leverage_joint_training_source 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then multiple_languages_leverage_joint_training_source
                when owning_person = (select person from admin.tokens where token = :token) then multiple_languages_leverage_joint_training_source
                else null 
            end as multiple_languages_leverage_joint_training_source,
            case
                when 'lang_comm_int_in_language_development_level' in (select column_name from column_level_access) then lang_comm_int_in_language_development_level 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then lang_comm_int_in_language_development_level
                when owning_person = (select person from admin.tokens where token = :token) then lang_comm_int_in_language_development_level
                else null 
            end as lang_comm_int_in_language_development_level,
            case
                when 'lang_comm_int_in_language_development_value' in (select column_name from column_level_access) then lang_comm_int_in_language_development_value 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then lang_comm_int_in_language_development_value
                when owning_person = (select person from admin.tokens where token = :token) then lang_comm_int_in_language_development_value
                else null 
            end as lang_comm_int_in_language_development_value,
            case
                when 'lang_comm_int_in_language_development_description' in (select column_name from column_level_access) then lang_comm_int_in_language_development_description 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then lang_comm_int_in_language_development_description
                when owning_person = (select person from admin.tokens where token = :token) then lang_comm_int_in_language_development_description
                else null 
            end as lang_comm_int_in_language_development_description,
            case
                when 'lang_comm_int_in_language_development_source' in (select column_name from column_level_access) then lang_comm_int_in_language_development_source 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then lang_comm_int_in_language_development_source
                when owning_person = (select person from admin.tokens where token = :token) then lang_comm_int_in_language_development_source
                else null 
            end as lang_comm_int_in_language_development_source,
            case
                when 'lang_comm_int_in_scripture_translation_level' in (select column_name from column_level_access) then lang_comm_int_in_scripture_translation_level 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then lang_comm_int_in_scripture_translation_level
                when owning_person = (select person from admin.tokens where token = :token) then lang_comm_int_in_scripture_translation_level
                else null 
            end as lang_comm_int_in_scripture_translation_level,
            case
                when 'lang_comm_int_in_scripture_translation_value' in (select column_name from column_level_access) then lang_comm_int_in_scripture_translation_value 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then lang_comm_int_in_scripture_translation_value
                when owning_person = (select person from admin.tokens where token = :token) then lang_comm_int_in_scripture_translation_value
                else null 
            end as lang_comm_int_in_scripture_translation_value,
            case
                when 'lang_comm_int_in_scripture_translation_description' in (select column_name from column_level_access) then lang_comm_int_in_scripture_translation_description 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then lang_comm_int_in_scripture_translation_description
                when owning_person = (select person from admin.tokens where token = :token) then lang_comm_int_in_scripture_translation_description
                else null 
            end as lang_comm_int_in_scripture_translation_description,
            case
                when 'lang_comm_int_in_scripture_translation_source' in (select column_name from column_level_access) then lang_comm_int_in_scripture_translation_source 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then lang_comm_int_in_scripture_translation_source
                when owning_person = (select person from admin.tokens where token = :token) then lang_comm_int_in_scripture_translation_source
                else null 
            end as lang_comm_int_in_scripture_translation_source,
            case
                when 'access_to_scripture_in_lwc_level' in (select column_name from column_level_access) then access_to_scripture_in_lwc_level 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then access_to_scripture_in_lwc_level
                when owning_person = (select person from admin.tokens where token = :token) then access_to_scripture_in_lwc_level
                else null 
            end as access_to_scripture_in_lwc_level,
            case
                when 'access_to_scripture_in_lwc_value' in (select column_name from column_level_access) then access_to_scripture_in_lwc_value 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then access_to_scripture_in_lwc_value
                when owning_person = (select person from admin.tokens where token = :token) then access_to_scripture_in_lwc_value
                else null 
            end as access_to_scripture_in_lwc_value,
            case
                when 'access_to_scripture_in_lwc_description' in (select column_name from column_level_access) then access_to_scripture_in_lwc_description 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then access_to_scripture_in_lwc_description
                when owning_person = (select person from admin.tokens where token = :token) then access_to_scripture_in_lwc_description
                else null 
            end as access_to_scripture_in_lwc_description,
            case
                when 'access_to_scripture_in_lwc_source' in (select column_name from column_level_access) then access_to_scripture_in_lwc_source 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then access_to_scripture_in_lwc_source
                when owning_person = (select person from admin.tokens where token = :token) then access_to_scripture_in_lwc_source
                else null 
            end as access_to_scripture_in_lwc_source,
            case
                when 'begin_work_geo_challenges_level' in (select column_name from column_level_access) then begin_work_geo_challenges_level 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then begin_work_geo_challenges_level
                when owning_person = (select person from admin.tokens where token = :token) then begin_work_geo_challenges_level
                else null 
            end as begin_work_geo_challenges_level,
            case
                when 'begin_work_geo_challenges_value' in (select column_name from column_level_access) then begin_work_geo_challenges_value 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then begin_work_geo_challenges_value
                when owning_person = (select person from admin.tokens where token = :token) then begin_work_geo_challenges_value
                else null 
            end as begin_work_geo_challenges_value,
            case
                when 'begin_work_geo_challenges_description' in (select column_name from column_level_access) then begin_work_geo_challenges_description 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then begin_work_geo_challenges_description
                when owning_person = (select person from admin.tokens where token = :token) then begin_work_geo_challenges_description
                else null 
            end as begin_work_geo_challenges_description,
            case
                when 'begin_work_geo_challenges_source' in (select column_name from column_level_access) then begin_work_geo_challenges_source 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then begin_work_geo_challenges_source
                when owning_person = (select person from admin.tokens where token = :token) then begin_work_geo_challenges_source
                else null 
            end as begin_work_geo_challenges_source,
            case
                when 'begin_work_rel_pol_obstacles_level' in (select column_name from column_level_access) then begin_work_rel_pol_obstacles_level 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then begin_work_rel_pol_obstacles_level
                when owning_person = (select person from admin.tokens where token = :token) then begin_work_rel_pol_obstacles_level
                else null 
            end as begin_work_rel_pol_obstacles_level,
            case
                when 'begin_work_rel_pol_obstacles_value' in (select column_name from column_level_access) then begin_work_rel_pol_obstacles_value 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then begin_work_rel_pol_obstacles_value
                when owning_person = (select person from admin.tokens where token = :token) then begin_work_rel_pol_obstacles_value
                else null 
            end as begin_work_rel_pol_obstacles_value,
            case
                when 'begin_work_rel_pol_obstacles_description' in (select column_name from column_level_access) then begin_work_rel_pol_obstacles_description 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then begin_work_rel_pol_obstacles_description
                when owning_person = (select person from admin.tokens where token = :token) then begin_work_rel_pol_obstacles_description
                else null 
            end as begin_work_rel_pol_obstacles_description,
            case
                when 'begin_work_rel_pol_obstacles_source' in (select column_name from column_level_access) then begin_work_rel_pol_obstacles_source 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then begin_work_rel_pol_obstacles_source
                when owning_person = (select person from admin.tokens where token = :token) then begin_work_rel_pol_obstacles_source
                else null 
            end as begin_work_rel_pol_obstacles_source,
            case
                when 'suggested_strategies' in (select column_name from column_level_access) then suggested_strategies 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then suggested_strategies
                when owning_person = (select person from admin.tokens where token = :token) then suggested_strategies
                else null 
            end as suggested_strategies,
            case
                when 'comments' in (select column_name from column_level_access) then comments 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then comments
                when owning_person = (select person from admin.tokens where token = :token) then comments
                else null 
            end as comments,
            case
                when 'created_at' in (select column_name from column_level_access) then created_at 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then created_at
                when owning_person = (select person from admin.tokens where token = :token) then created_at
                else null 
            end as created_at,
            case
                when 'created_by' in (select column_name from column_level_access) then created_by 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then created_by
                when owning_person = (select person from admin.tokens where token = :token) then created_by
                else null 
            end as created_by,
            case
                when 'modified_at' in (select column_name from column_level_access) then modified_at 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then modified_at
                when owning_person = (select person from admin.tokens where token = :token) then modified_at
                else null 
            end as modified_at,
            case
                when 'modified_by' in (select column_name from column_level_access) then modified_by 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then modified_by
                when owning_person = (select person from admin.tokens where token = :token) then modified_by
                else null 
            end as modified_by,
            case
                when 'owning_person' in (select column_name from column_level_access) then owning_person 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then owning_person
                when owning_person = (select person from admin.tokens where token = :token) then owning_person
                else null 
            end as owning_person,
            case
                when 'owning_group' in (select column_name from column_level_access) then owning_group 
                when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1))  then owning_group
                when owning_person = (select person from admin.tokens where token = :token) then owning_group
                else null 
            end as owning_group
            from sc.languages_ex 
            where id in (select row from row_level_access) or 
                (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1)) or
                owning_person = (select person from admin.tokens where token = :token);
        """.trimIndent()


        try {
            val jdbcResult = jdbcTemplate.queryForRowSet(listSQL, paramSource)
            while (jdbcResult.next()) {

                var id: Int? = jdbcResult.getInt("id")
                if (jdbcResult.wasNull()) id = null

                var language_name: String? = jdbcResult.getString("language_name")
                if (jdbcResult.wasNull()) language_name = null

                var iso: String? = jdbcResult.getString("iso")
                if (jdbcResult.wasNull()) iso = null

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

                var created_by: Int? = jdbcResult.getInt("created_by")
                if (jdbcResult.wasNull()) created_by = null

                var modified_at: String? = jdbcResult.getString("modified_at")
                if (jdbcResult.wasNull()) modified_at = null

                var modified_by: Int? = jdbcResult.getInt("modified_by")
                if (jdbcResult.wasNull()) modified_by = null

                var owning_person: Int? = jdbcResult.getInt("owning_person")
                if (jdbcResult.wasNull()) owning_person = null

                var owning_group: Int? = jdbcResult.getInt("owning_group")
                if (jdbcResult.wasNull()) owning_group = null

                data.add(
                    LanguageEx(
                            id= id,
                            language_name= language_name,
                            iso= iso,
                            prioritization= prioritization,
                            progress_bible= progress_bible,
                            island= island,
                            province= province,
                            first_language_population= first_language_population,
                            population_value= population_value,
                            egids_level = if (egids_level == null) null else EgidsScale.valueOf(egids_level),
                            egids_value= egids_value,
                            least_reached_progress_jps_level = if(least_reached_progress_jps_level == null) null else LeastReachedProgressScale.valueOf(least_reached_progress_jps_level) ,
                            least_reached_value= least_reached_value,
                            partner_interest_level = if(partner_interest_level == null) null else PartnerInterestScale.valueOf(partner_interest_level),
                            partner_interest_value= partner_interest_value,
                            partner_interest_description= partner_interest_description,
                            partner_interest_source= partner_interest_source,
                            multiple_languages_leverage_linguistic_level = if(multiple_languages_leverage_linguistic_level == null) null else MultipleLanguagesLeverageLinguisticScale.valueOf(multiple_languages_leverage_linguistic_level),
                            multiple_languages_leverage_linguistic_value= multiple_languages_leverage_linguistic_value,
                            multiple_languages_leverage_linguistic_description= multiple_languages_leverage_linguistic_description,
                            multiple_languages_leverage_linguistic_source= multiple_languages_leverage_linguistic_source,
                            multiple_languages_leverage_joint_training_level = if(multiple_languages_leverage_joint_training_level == null) null else MultipleLanguagesLeverageJointTrainingScale.valueOf(multiple_languages_leverage_joint_training_level),
                            multiple_languages_leverage_joint_training_value= multiple_languages_leverage_joint_training_value,
                            multiple_languages_leverage_joint_training_description= multiple_languages_leverage_joint_training_description,
                            multiple_languages_leverage_joint_training_source= multiple_languages_leverage_joint_training_source,
                            lang_comm_int_in_language_development_level = if(lang_comm_int_in_language_development_level == null) null else LangCommIntInLanguageDevelopmentScale.valueOf(lang_comm_int_in_language_development_level),
                            lang_comm_int_in_language_development_value= lang_comm_int_in_language_development_value,
                            lang_comm_int_in_language_development_description= lang_comm_int_in_language_development_description,
                            lang_comm_int_in_language_development_source= lang_comm_int_in_language_development_source,
                            lang_comm_int_in_scripture_translation_level = if(lang_comm_int_in_scripture_translation_level == null) null else LangCommIntInScriptureTranslationScale.valueOf(lang_comm_int_in_scripture_translation_level),
                            lang_comm_int_in_scripture_translation_value= lang_comm_int_in_scripture_translation_value,
                            lang_comm_int_in_scripture_translation_description= lang_comm_int_in_scripture_translation_description,
                            lang_comm_int_in_scripture_translation_source= lang_comm_int_in_scripture_translation_source,
                            access_to_scripture_in_lwc_level = if(access_to_scripture_in_lwc_level == null) null else AccessToScriptureInLwcScale.valueOf(access_to_scripture_in_lwc_level),
                            access_to_scripture_in_lwc_value= access_to_scripture_in_lwc_value,
                            access_to_scripture_in_lwc_description= access_to_scripture_in_lwc_description,
                            access_to_scripture_in_lwc_source= access_to_scripture_in_lwc_source,
                            begin_work_geo_challenges_level = if(begin_work_geo_challenges_level == null) null else BeginWorkGeoChallengesScale.valueOf(begin_work_geo_challenges_level),
                            begin_work_geo_challenges_value = begin_work_geo_challenges_value,
                            begin_work_geo_challenges_description= begin_work_geo_challenges_description,
                            begin_work_geo_challenges_source= begin_work_geo_challenges_source,
                            begin_work_rel_pol_obstacles_level = if(begin_work_rel_pol_obstacles_level == null) null else BeginWorkRelPolObstaclesScale.valueOf(begin_work_rel_pol_obstacles_level),
                            begin_work_rel_pol_obstacles_value= begin_work_rel_pol_obstacles_value,
                            begin_work_rel_pol_obstacles_description= begin_work_rel_pol_obstacles_description,
                            begin_work_rel_pol_obstacles_source= begin_work_rel_pol_obstacles_source,
                            suggested_strategies= suggested_strategies,
                            comments= comments,
                            created_at= created_at,
                            created_by= created_by,
                            modified_at= modified_at,
                            modified_by= modified_by,
                            owning_person= owning_person,
                            owning_group =  owning_group
                    )
                )
            }
        } catch (e: SQLException) {
            println("error while listing ${e.message}")
            return ReadLanguageExResponse(ErrorType.SQLReadError, mutableListOf())
        }

        return ReadLanguageExResponse(ErrorType.NoError, data)
    }
}