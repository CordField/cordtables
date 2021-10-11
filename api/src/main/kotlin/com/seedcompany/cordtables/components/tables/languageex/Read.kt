package com.seedcompany.cordtables.components.tables.languageex

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource



data class LanguageEx(
    val id:Int?,
    val language_name:String?,
    val iso:String?,
    val prioritization:Double?,
    val progress_bible:Boolean?,
    val island:String?,
    val province:String?,
    val first_language_population:Int?,
    val population_value:Double?,
    val egids_level:String?,
    val egids_value:Double?,
    val least_reached_progress_jps_level:String?,
    val least_reached_value:Double?,
    val partner_interest_level:String?,
    val partner_interest_value:Double?,
    val partner_interest_description:String?,
    val partner_interest_source:String?,
    val multiple_languages_leverage_linguistic_level:String?,
    val multiple_languages_leverage_linguistic_value:Double?,
    val multiple_languages_leverage_linguistic_description:String?,
    val multiple_languages_leverage_linguistic_source:String?,
    val multiple_languages_leverage_joint_training_level:String?,
    val multiple_languages_leverage_joint_training_value:Double?,
    val multiple_languages_leverage_joint_training_description:String?,
    val multiple_languages_leverage_joint_training_source:String?,
    val lang_comm_int_in_language_development_level:String?,
    val lang_comm_int_in_language_development_value:Double?,
    val lang_comm_int_in_language_development_description:String?,
    val lang_comm_int_in_language_development_source:String?,
    val lang_comm_int_in_scripture_translation_level:String?,
    val lang_comm_int_in_scripture_translation_value:Double?,
    val lang_comm_int_in_scripture_translation_description:String?,
    val lang_comm_int_in_scripture_translation_source:String?,
    val access_to_scripture_in_lwc_level:String?,
    val access_to_scripture_in_lwc_value:Double?,
    val access_to_scripture_in_lwc_description:String?,
    val access_to_scripture_in_lwc_source:String?,
    val begin_work_geo_challenges_level:String?,
    val begin_work_geo_challenges_value:Double?,
    val begin_work_geo_challenges_description:String?,
    val begin_work_geo_challenges_source:String?,
    val begin_work_rel_pol_obstacles_level:String?,
    val begin_work_rel_pol_obstacles_value:Double?,
    val begin_work_rel_pol_obstacles_description:String?,
    val begin_work_rel_pol_obstacles_source:String?,
    val suggested_strategies:String?,
    val comments:String?,
    val created_at:String?,
    val created_by:Int?,
    val modified_at:String?,
    val modified_by:Int?,
    val owning_person:Int?,
    val owning_group:Int?
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
    @PostMapping("language_ex/read")
    @ResponseBody
    fun ReadHandler(@RequestBody req: ReadLanguageExRequest): ReadLanguageExResponse {
        //mutableList as we need to add each global role as an element to it
        var data: MutableList<LanguageEx> = mutableListOf()
        if (req.token == null) return ReadLanguageExResponse(ErrorType.TokenNotFound, mutableListOf())
        if (!util.isAdmin(req.token)) return ReadLanguageExResponse(ErrorType.AdminOnly, mutableListOf())

        this.ds.connection.use { conn ->
            val listStatement = conn.prepareCall(
                "with row_level_access as \n" +
                        "(\n" +
                        "\tselect row \n" +
                        "    from admin.group_row_access as a  \n" +
                        "\tinner join admin.group_memberships as b \n" +
                        "\ton a.group_id = b.group_id \n" +
                        "\tinner join admin.tokens as c \n" +
                        "\ton b.person = c.person\n" +
                        "\twhere a.table_name = 'sc.languages_ex'\n" +
                        "\tand c.token = ?\n" +
                        "), \n" +
                        "column_level_access as \n" +
                        "(\n" +
                        "    select  column_name \n" +
                        "    from admin.global_role_column_grants a \n" +
                        "    inner join admin.global_role_memberships b \n" +
                        "    on a.global_role = b.global_role \n" +
                        "    inner join admin.tokens c \n" +
                        "    on b.person = c.person \n" +
                        "    where a.table_name = 'sc.languages_ex'\n" +
                        "\tand c.token = ?\n" +
                        ")\n" +
                        "select \n" +
                     "case when 'id' in (select column_name from column_level_access) then id \n" +
                        "when (select exists( select id from admin.global_role_memberships where person = (select person from admin.tokens where token = ?) and global_role = 1))  then id \n" +
                        "else null \n" +
                        "end as id,\n" +
                        "case when 'language_name' in (select column_name from column_level_access) then language_name \n" +
                        "when (select exists( select id from admin.global_role_memberships where person = (select person from admin.tokens where token = ?) and global_role = 1))  then language_name \n" +
                        "else null \n" +
                        "end as language_name,\n" +
                        "case when 'iso' in (select column_name from column_level_access) then iso \n" +
                        "when (select exists( select id from admin.global_role_memberships where person = (select person from admin.tokens where token = ?) and global_role = 1))  then iso \n" +
                        "else null \n" +
                        "end as iso,\n" +
                        "case when 'prioritization' in (select column_name from column_level_access) then prioritization \n" +
                        "when (select exists( select id from admin.global_role_memberships where person = (select person from admin.tokens where token = ?) and global_role = 1))  then prioritization \n" +
                        "else null \n" +
                        "end as prioritization,\n" +
                        "case when 'progress_bible' in (select column_name from column_level_access) then progress_bible \n" +
                        "when (select exists( select id from admin.global_role_memberships where person = (select person from admin.tokens where token = ?) and global_role = 1))  then progress_bible \n" +
                        "else null \n" +
                        "end as progress_bible,\n" +
                        "case when 'island' in (select column_name from column_level_access) then island \n" +
                        "when (select exists( select id from admin.global_role_memberships where person = (select person from admin.tokens where token = ?) and global_role = 1))  then island \n" +
                        "else null \n" +
                        "end as island,\n" +
                        "case when 'province' in (select column_name from column_level_access) then province \n" +
                        "when (select exists( select id from admin.global_role_memberships where person = (select person from admin.tokens where token = ?) and global_role = 1))  then province \n" +
                        "else null \n" +
                        "end as province,\n" +
                        "case when 'first_language_population' in (select column_name from column_level_access) then first_language_population \n" +
                        "when (select exists( select id from admin.global_role_memberships where person = (select person from admin.tokens where token = ?) and global_role = 1))  then first_language_population \n" +
                        "else null \n" +
                        "end as first_language_population,\n" +
                        "case when 'population_value' in (select column_name from column_level_access) then population_value \n" +
                        "when (select exists( select id from admin.global_role_memberships where person = (select person from admin.tokens where token = ?) and global_role = 1))  then population_value \n" +
                        "else null \n" +
                        "end as population_value,\n" +
                        "case when 'egids_level' in (select column_name from column_level_access) then egids_level \n" +
                        "when (select exists( select id from admin.global_role_memberships where person = (select person from admin.tokens where token = ?) and global_role = 1))  then egids_level \n" +
                        "else null \n" +
                        "end as egids_level,\n" +
                        "case when 'egids_value' in (select column_name from column_level_access) then egids_value \n" +
                        "when (select exists( select id from admin.global_role_memberships where person = (select person from admin.tokens where token = ?) and global_role = 1))  then egids_value \n" +
                        "else null \n" +
                        "end as egids_value,\n" +
                        "case when 'least_reached_progress_jps_level' in (select column_name from column_level_access) then least_reached_progress_jps_level \n" +
                        "when (select exists( select id from admin.global_role_memberships where person = (select person from admin.tokens where token = ?) and global_role = 1))  then least_reached_progress_jps_level \n" +
                        "else null \n" +
                        "end as least_reached_progress_jps_level,\n" +
                        "case when 'least_reached_value' in (select column_name from column_level_access) then least_reached_value \n" +
                        "when (select exists( select id from admin.global_role_memberships where person = (select person from admin.tokens where token = ?) and global_role = 1))  then least_reached_value \n" +
                        "else null \n" +
                        "end as least_reached_value,\n" +
                        "case when 'partner_interest_level' in (select column_name from column_level_access) then partner_interest_level \n" +
                        "when (select exists( select id from admin.global_role_memberships where person = (select person from admin.tokens where token = ?) and global_role = 1))  then partner_interest_level \n" +
                        "else null \n" +
                        "end as partner_interest_level,\n" +
                        "case when 'partner_interest_value' in (select column_name from column_level_access) then partner_interest_value \n" +
                        "when (select exists( select id from admin.global_role_memberships where person = (select person from admin.tokens where token = ?) and global_role = 1))  then partner_interest_value \n" +
                        "else null \n" +
                        "end as partner_interest_value,\n" +
                        "case when 'partner_interest_description' in (select column_name from column_level_access) then partner_interest_description \n" +
                        "when (select exists( select id from admin.global_role_memberships where person = (select person from admin.tokens where token = ?) and global_role = 1))  then partner_interest_description \n" +
                        "else null \n" +
                        "end as partner_interest_description,\n" +
                        "case when 'partner_interest_source' in (select column_name from column_level_access) then partner_interest_source \n" +
                        "when (select exists( select id from admin.global_role_memberships where person = (select person from admin.tokens where token = ?) and global_role = 1))  then partner_interest_source \n" +
                        "else null \n" +
                        "end as partner_interest_source,\n" +
                        "case when 'multiple_languages_leverage_linguistic_level' in (select column_name from column_level_access) then multiple_languages_leverage_linguistic_level \n" +
                        "when (select exists( select id from admin.global_role_memberships where person = (select person from admin.tokens where token = ?) and global_role = 1))  then multiple_languages_leverage_linguistic_level \n" +
                        "else null \n" +
                        "end as multiple_languages_leverage_linguistic_level,\n" +
                        "case when 'multiple_languages_leverage_linguistic_value' in (select column_name from column_level_access) then multiple_languages_leverage_linguistic_value \n" +
                        "when (select exists( select id from admin.global_role_memberships where person = (select person from admin.tokens where token = ?) and global_role = 1))  then multiple_languages_leverage_linguistic_value \n" +
                        "else null \n" +
                        "end as multiple_languages_leverage_linguistic_value,\n" +
                        "case when 'multiple_languages_leverage_linguistic_description' in (select column_name from column_level_access) then multiple_languages_leverage_linguistic_description \n" +
                        "when (select exists( select id from admin.global_role_memberships where person = (select person from admin.tokens where token = ?) and global_role = 1))  then multiple_languages_leverage_linguistic_description \n" +
                        "else null \n" +
                        "end as multiple_languages_leverage_linguistic_description,\n" +
                        "case when 'multiple_languages_leverage_linguistic_source' in (select column_name from column_level_access) then multiple_languages_leverage_linguistic_source \n" +
                        "when (select exists( select id from admin.global_role_memberships where person = (select person from admin.tokens where token = ?) and global_role = 1))  then multiple_languages_leverage_linguistic_source \n" +
                        "else null \n" +
                        "end as multiple_languages_leverage_linguistic_source,\n" +
                        "case when 'multiple_languages_leverage_joint_training_level' in (select column_name from column_level_access) then multiple_languages_leverage_joint_training_level \n" +
                        "when (select exists( select id from admin.global_role_memberships where person = (select person from admin.tokens where token = ?) and global_role = 1))  then multiple_languages_leverage_joint_training_level \n" +
                        "else null \n" +
                        "end as multiple_languages_leverage_joint_training_level,\n" +
                        "case when 'multiple_languages_leverage_joint_training_value' in (select column_name from column_level_access) then multiple_languages_leverage_joint_training_value \n" +
                        "when (select exists( select id from admin.global_role_memberships where person = (select person from admin.tokens where token = ?) and global_role = 1))  then multiple_languages_leverage_joint_training_value \n" +
                        "else null \n" +
                        "end as multiple_languages_leverage_joint_training_value,\n" +
                        "case when 'multiple_languages_leverage_joint_training_description' in (select column_name from column_level_access) then multiple_languages_leverage_joint_training_description \n" +
                        "when (select exists( select id from admin.global_role_memberships where person = (select person from admin.tokens where token = ?) and global_role = 1))  then multiple_languages_leverage_joint_training_description \n" +
                        "else null \n" +
                        "end as multiple_languages_leverage_joint_training_description,\n" +
                        "case when 'multiple_languages_leverage_joint_training_source' in (select column_name from column_level_access) then multiple_languages_leverage_joint_training_source \n" +
                        "when (select exists( select id from admin.global_role_memberships where person = (select person from admin.tokens where token = ?) and global_role = 1))  then multiple_languages_leverage_joint_training_source \n" +
                        "else null \n" +
                        "end as multiple_languages_leverage_joint_training_source,\n" +
                        "case when 'lang_comm_int_in_language_development_level' in (select column_name from column_level_access) then lang_comm_int_in_language_development_level \n" +
                        "when (select exists( select id from admin.global_role_memberships where person = (select person from admin.tokens where token = ?) and global_role = 1))  then lang_comm_int_in_language_development_level \n" +
                        "else null \n" +
                        "end as lang_comm_int_in_language_development_level,\n" +
                        "case when 'lang_comm_int_in_language_development_value' in (select column_name from column_level_access) then lang_comm_int_in_language_development_value \n" +
                        "when (select exists( select id from admin.global_role_memberships where person = (select person from admin.tokens where token = ?) and global_role = 1))  then lang_comm_int_in_language_development_value \n" +
                        "else null \n" +
                        "end as lang_comm_int_in_language_development_value,\n" +
                        "case when 'lang_comm_int_in_language_development_description' in (select column_name from column_level_access) then lang_comm_int_in_language_development_description \n" +
                        "when (select exists( select id from admin.global_role_memberships where person = (select person from admin.tokens where token = ?) and global_role = 1))  then lang_comm_int_in_language_development_description \n" +
                        "else null \n" +
                        "end as lang_comm_int_in_language_development_description,\n" +
                        "case when 'lang_comm_int_in_language_development_source' in (select column_name from column_level_access) then lang_comm_int_in_language_development_source \n" +
                        "when (select exists( select id from admin.global_role_memberships where person = (select person from admin.tokens where token = ?) and global_role = 1))  then lang_comm_int_in_language_development_source \n" +
                        "else null \n" +
                        "end as lang_comm_int_in_language_development_source,\n" +
                        "case when 'lang_comm_int_in_scripture_translation_level' in (select column_name from column_level_access) then lang_comm_int_in_scripture_translation_level \n" +
                        "when (select exists( select id from admin.global_role_memberships where person = (select person from admin.tokens where token = ?) and global_role = 1))  then lang_comm_int_in_scripture_translation_level \n" +
                        "else null \n" +
                        "end as lang_comm_int_in_scripture_translation_level,\n" +
                        "case when 'lang_comm_int_in_scripture_translation_value' in (select column_name from column_level_access) then lang_comm_int_in_scripture_translation_value \n" +
                        "when (select exists( select id from admin.global_role_memberships where person = (select person from admin.tokens where token = ?) and global_role = 1))  then lang_comm_int_in_scripture_translation_value \n" +
                        "else null \n" +
                        "end as lang_comm_int_in_scripture_translation_value,\n" +
                        "case when 'lang_comm_int_in_scripture_translation_description' in (select column_name from column_level_access) then lang_comm_int_in_scripture_translation_description \n" +
                        "when (select exists( select id from admin.global_role_memberships where person = (select person from admin.tokens where token = ?) and global_role = 1))  then lang_comm_int_in_scripture_translation_description \n" +
                        "else null \n" +
                        "end as lang_comm_int_in_scripture_translation_description,\n" +
                        "case when 'lang_comm_int_in_scripture_translation_source' in (select column_name from column_level_access) then lang_comm_int_in_scripture_translation_source \n" +
                        "when (select exists( select id from admin.global_role_memberships where person = (select person from admin.tokens where token = ?) and global_role = 1))  then lang_comm_int_in_scripture_translation_source \n" +
                        "else null \n" +
                        "end as lang_comm_int_in_scripture_translation_source,\n" +
                        "case when 'access_to_scripture_in_lwc_level' in (select column_name from column_level_access) then access_to_scripture_in_lwc_level \n" +
                        "when (select exists( select id from admin.global_role_memberships where person = (select person from admin.tokens where token = ?) and global_role = 1))  then access_to_scripture_in_lwc_level \n" +
                        "else null \n" +
                        "end as access_to_scripture_in_lwc_level,\n" +
                        "case when 'access_to_scripture_in_lwc_value' in (select column_name from column_level_access) then access_to_scripture_in_lwc_value \n" +
                        "when (select exists( select id from admin.global_role_memberships where person = (select person from admin.tokens where token = ?) and global_role = 1))  then access_to_scripture_in_lwc_value \n" +
                        "else null \n" +
                        "end as access_to_scripture_in_lwc_value,\n" +
                        "case when 'access_to_scripture_in_lwc_description' in (select column_name from column_level_access) then access_to_scripture_in_lwc_description \n" +
                        "when (select exists( select id from admin.global_role_memberships where person = (select person from admin.tokens where token = ?) and global_role = 1))  then access_to_scripture_in_lwc_description \n" +
                        "else null \n" +
                        "end as access_to_scripture_in_lwc_description,\n" +
                        "case when 'access_to_scripture_in_lwc_source' in (select column_name from column_level_access) then access_to_scripture_in_lwc_source \n" +
                        "when (select exists( select id from admin.global_role_memberships where person = (select person from admin.tokens where token = ?) and global_role = 1))  then access_to_scripture_in_lwc_source \n" +
                        "else null \n" +
                        "end as access_to_scripture_in_lwc_source,\n" +
                        "case when 'begin_work_geo_challenges_level' in (select column_name from column_level_access) then begin_work_geo_challenges_level \n" +
                        "when (select exists( select id from admin.global_role_memberships where person = (select person from admin.tokens where token = ?) and global_role = 1))  then begin_work_geo_challenges_level \n" +
                        "else null \n" +
                        "end as begin_work_geo_challenges_level,\n" +
                        "case when 'begin_work_geo_challenges_value' in (select column_name from column_level_access) then begin_work_geo_challenges_value \n" +
                        "when (select exists( select id from admin.global_role_memberships where person = (select person from admin.tokens where token = ?) and global_role = 1))  then begin_work_geo_challenges_value \n" +
                        "else null \n" +
                        "end as begin_work_geo_challenges_value,\n" +
                        "case when 'begin_work_geo_challenges_description' in (select column_name from column_level_access) then begin_work_geo_challenges_description \n" +
                        "when (select exists( select id from admin.global_role_memberships where person = (select person from admin.tokens where token = ?) and global_role = 1))  then begin_work_geo_challenges_description \n" +
                        "else null \n" +
                        "end as begin_work_geo_challenges_description,\n" +
                        "case when 'begin_work_geo_challenges_source' in (select column_name from column_level_access) then begin_work_geo_challenges_source \n" +
                        "when (select exists( select id from admin.global_role_memberships where person = (select person from admin.tokens where token = ?) and global_role = 1))  then begin_work_geo_challenges_source \n" +
                        "else null \n" +
                        "end as begin_work_geo_challenges_source,\n" +
                        "case when 'begin_work_rel_pol_obstacles_level' in (select column_name from column_level_access) then begin_work_rel_pol_obstacles_level \n" +
                        "when (select exists( select id from admin.global_role_memberships where person = (select person from admin.tokens where token = ?) and global_role = 1))  then begin_work_rel_pol_obstacles_level \n" +
                        "else null \n" +
                        "end as begin_work_rel_pol_obstacles_level,\n" +
                        "case when 'begin_work_rel_pol_obstacles_value' in (select column_name from column_level_access) then begin_work_rel_pol_obstacles_value \n" +
                        "when (select exists( select id from admin.global_role_memberships where person = (select person from admin.tokens where token = ?) and global_role = 1))  then begin_work_rel_pol_obstacles_value \n" +
                        "else null \n" +
                        "end as begin_work_rel_pol_obstacles_value,\n" +
                        "case when 'begin_work_rel_pol_obstacles_description' in (select column_name from column_level_access) then begin_work_rel_pol_obstacles_description \n" +
                        "when (select exists( select id from admin.global_role_memberships where person = (select person from admin.tokens where token = ?) and global_role = 1))  then begin_work_rel_pol_obstacles_description \n" +
                        "else null \n" +
                        "end as begin_work_rel_pol_obstacles_description,\n" +
                        "case when 'begin_work_rel_pol_obstacles_source' in (select column_name from column_level_access) then begin_work_rel_pol_obstacles_source \n" +
                        "when (select exists( select id from admin.global_role_memberships where person = (select person from admin.tokens where token = ?) and global_role = 1))  then begin_work_rel_pol_obstacles_source \n" +
                        "else null \n" +
                        "end as begin_work_rel_pol_obstacles_source,\n" +
                        "case when 'suggested_strategies' in (select column_name from column_level_access) then suggested_strategies \n" +
                        "when (select exists( select id from admin.global_role_memberships where person = (select person from admin.tokens where token = ?) and global_role = 1))  then suggested_strategies \n" +
                        "else null \n" +
                        "end as suggested_strategies,\n" +
                        "case when 'comments' in (select column_name from column_level_access) then comments \n" +
                        "when (select exists( select id from admin.global_role_memberships where person = (select person from admin.tokens where token = ?) and global_role = 1))  then comments \n" +
                        "else null \n" +
                        "end as comments,\n" +
                        "case when 'created_at' in (select column_name from column_level_access) then created_at \n" +
                        "when (select exists( select id from admin.global_role_memberships where person = (select person from admin.tokens where token = ?) and global_role = 1))  then created_at \n" +
                        "else null \n" +
                        "end as created_at,\n" +
                        "case when 'created_by' in (select column_name from column_level_access) then created_by \n" +
                        "when (select exists( select id from admin.global_role_memberships where person = (select person from admin.tokens where token = ?) and global_role = 1))  then created_by \n" +
                        "else null \n" +
                        "end as created_by,\n" +
                        "case when 'modified_at' in (select column_name from column_level_access) then modified_at \n" +
                        "when (select exists( select id from admin.global_role_memberships where person = (select person from admin.tokens where token = ?) and global_role = 1))  then modified_at \n" +
                        "else null \n" +
                        "end as modified_at,\n" +
                        "case when 'modified_by' in (select column_name from column_level_access) then modified_by \n" +
                        "when (select exists( select id from admin.global_role_memberships where person = (select person from admin.tokens where token = ?) and global_role = 1))  then modified_by \n" +
                        "else null \n" +
                        "end as modified_by,\n" +
                        "case when 'owning_person' in (select column_name from column_level_access) then owning_person \n" +
                        "when (select exists( select id from admin.global_role_memberships where person = (select person from admin.tokens where token = ?) and global_role = 1))  then owning_person \n" +
                        "else null \n" +
                        "end as owning_person,\n" +
                        "case when 'owning_group' in (select column_name from column_level_access) then owning_group \n" +
                        "when (select exists( select id from admin.global_role_memberships where person = (select person from admin.tokens where token = ?) and global_role = 1))  then owning_group \n" +
                        "else null \n" +
                        "end as owning_group\n"+
                        "from sc.languages_ex \n" +
                        "where id in (select row from row_level_access);"
            )

            for (i in 1..55) {
                listStatement.setString(i, req.token)
            }
//
            try {
                val listStatementResult = listStatement.executeQuery()
                while (listStatementResult.next()) {

                    var id:Int? = listStatementResult.getInt("id")
                    if(listStatementResult.wasNull()) id = null
                    var language_name:String? = listStatementResult.getString("language_name")
                    if(listStatementResult.wasNull()) language_name = null
                    var iso:String? = listStatementResult.getString("iso")
                    if(listStatementResult.wasNull()) iso = null
                    var prioritization:Double? = listStatementResult.getDouble("prioritization")
                    if(listStatementResult.wasNull()) prioritization = null
                    var progress_bible:Boolean? = listStatementResult.getBoolean("progress_bible")
                    if(listStatementResult.wasNull()) progress_bible = null
                    var island:String? = listStatementResult.getString("island")
                    if(listStatementResult.wasNull()) island = null
                    var province:String? = listStatementResult.getString("province")
                    if(listStatementResult.wasNull()) province = null
                    var first_language_population:Int? = listStatementResult.getInt("first_language_population")
                    if(listStatementResult.wasNull()) first_language_population = null
                    var population_value:Double? = listStatementResult.getDouble("population_value")
                    if(listStatementResult.wasNull()) population_value = null
                    var egids_level:String? = listStatementResult.getString("egids_level")
                    if(listStatementResult.wasNull()) egids_level = null
                    var egids_value:Double? = listStatementResult.getDouble("egids_value")
                    if(listStatementResult.wasNull()) egids_value = null
                    var least_reached_progress_jps_level:String? = listStatementResult.getString("least_reached_progress_jps_level")
                    if(listStatementResult.wasNull()) least_reached_progress_jps_level = null
                    var least_reached_value:Double? = listStatementResult.getDouble("least_reached_value")
                    if(listStatementResult.wasNull()) least_reached_value = null
                    var partner_interest_level:String? = listStatementResult.getString("partner_interest_level")
                    if(listStatementResult.wasNull()) partner_interest_level = null
                    var partner_interest_value:Double? = listStatementResult.getDouble("partner_interest_value")
                    if(listStatementResult.wasNull()) partner_interest_value = null
                    var partner_interest_description:String? = listStatementResult.getString("partner_interest_description")
                    if(listStatementResult.wasNull()) partner_interest_description = null
                    var partner_interest_source:String? = listStatementResult.getString("partner_interest_source")
                    if(listStatementResult.wasNull()) partner_interest_source = null
                    var multiple_languages_leverage_linguistic_level:String? = listStatementResult.getString("multiple_languages_leverage_linguistic_level")
                    if(listStatementResult.wasNull()) multiple_languages_leverage_linguistic_level = null
                    var multiple_languages_leverage_linguistic_value:Double? = listStatementResult.getDouble("multiple_languages_leverage_linguistic_value")
                    if(listStatementResult.wasNull()) multiple_languages_leverage_linguistic_value = null
                    var multiple_languages_leverage_linguistic_description:String? = listStatementResult.getString("multiple_languages_leverage_linguistic_description")
                    if(listStatementResult.wasNull()) multiple_languages_leverage_linguistic_description = null
                    var multiple_languages_leverage_linguistic_source:String? = listStatementResult.getString("multiple_languages_leverage_linguistic_source")
                    if(listStatementResult.wasNull()) multiple_languages_leverage_linguistic_source = null
                    var multiple_languages_leverage_joint_training_level:String? = listStatementResult.getString("multiple_languages_leverage_joint_training_level")
                    if(listStatementResult.wasNull()) multiple_languages_leverage_joint_training_level = null
                    var multiple_languages_leverage_joint_training_value:Double? = listStatementResult.getDouble("multiple_languages_leverage_joint_training_value")
                    if(listStatementResult.wasNull()) multiple_languages_leverage_joint_training_value = null
                    var multiple_languages_leverage_joint_training_description:String? = listStatementResult.getString("multiple_languages_leverage_joint_training_description")
                    if(listStatementResult.wasNull()) multiple_languages_leverage_joint_training_description = null
                    var multiple_languages_leverage_joint_training_source:String? = listStatementResult.getString("multiple_languages_leverage_joint_training_source")
                    if(listStatementResult.wasNull()) multiple_languages_leverage_joint_training_source = null
                    var lang_comm_int_in_language_development_level:String? = listStatementResult.getString("lang_comm_int_in_language_development_level")
                    if(listStatementResult.wasNull()) lang_comm_int_in_language_development_level = null
                    var lang_comm_int_in_language_development_value:Double? = listStatementResult.getDouble("lang_comm_int_in_language_development_value")
                    if(listStatementResult.wasNull()) lang_comm_int_in_language_development_value = null
                    var lang_comm_int_in_language_development_description:String? = listStatementResult.getString("lang_comm_int_in_language_development_description")
                    if(listStatementResult.wasNull()) lang_comm_int_in_language_development_description = null
                    var lang_comm_int_in_language_development_source:String? = listStatementResult.getString("lang_comm_int_in_language_development_source")
                    if(listStatementResult.wasNull()) lang_comm_int_in_language_development_source = null
                    var lang_comm_int_in_scripture_translation_level:String? = listStatementResult.getString("lang_comm_int_in_scripture_translation_level")
                    if(listStatementResult.wasNull()) lang_comm_int_in_scripture_translation_level = null
                    var lang_comm_int_in_scripture_translation_value:Double? = listStatementResult.getDouble("lang_comm_int_in_scripture_translation_value")
                    if(listStatementResult.wasNull()) lang_comm_int_in_scripture_translation_value = null
                    var lang_comm_int_in_scripture_translation_description:String? = listStatementResult.getString("lang_comm_int_in_scripture_translation_description")
                    if(listStatementResult.wasNull()) lang_comm_int_in_scripture_translation_description = null
                    var lang_comm_int_in_scripture_translation_source:String? = listStatementResult.getString("lang_comm_int_in_scripture_translation_source")
                    if(listStatementResult.wasNull()) lang_comm_int_in_scripture_translation_source = null
                    var access_to_scripture_in_lwc_level:String? = listStatementResult.getString("access_to_scripture_in_lwc_level")
                    if(listStatementResult.wasNull()) access_to_scripture_in_lwc_level = null
                    var access_to_scripture_in_lwc_value:Double? = listStatementResult.getDouble("access_to_scripture_in_lwc_value")
                    if(listStatementResult.wasNull()) access_to_scripture_in_lwc_value = null
                    var access_to_scripture_in_lwc_description:String? = listStatementResult.getString("access_to_scripture_in_lwc_description")
                    if(listStatementResult.wasNull()) access_to_scripture_in_lwc_description = null
                    var access_to_scripture_in_lwc_source:String? = listStatementResult.getString("access_to_scripture_in_lwc_source")
                    if(listStatementResult.wasNull()) access_to_scripture_in_lwc_source = null
                    var begin_work_geo_challenges_level:String? = listStatementResult.getString("begin_work_geo_challenges_level")
                    if(listStatementResult.wasNull()) begin_work_geo_challenges_level = null
                    var begin_work_geo_challenges_value:Double? = listStatementResult.getDouble("begin_work_geo_challenges_value")
                    if(listStatementResult.wasNull()) begin_work_geo_challenges_value = null
                    var begin_work_geo_challenges_description:String? = listStatementResult.getString("begin_work_geo_challenges_description")
                    if(listStatementResult.wasNull()) begin_work_geo_challenges_description = null
                    var begin_work_geo_challenges_source:String? = listStatementResult.getString("begin_work_geo_challenges_source")
                    if(listStatementResult.wasNull()) begin_work_geo_challenges_source = null
                    var begin_work_rel_pol_obstacles_level:String? = listStatementResult.getString("begin_work_rel_pol_obstacles_level")
                    if(listStatementResult.wasNull()) begin_work_rel_pol_obstacles_level = null
                    var begin_work_rel_pol_obstacles_value:Double? = listStatementResult.getDouble("begin_work_rel_pol_obstacles_value")
                    if(listStatementResult.wasNull()) begin_work_rel_pol_obstacles_value = null
                    var begin_work_rel_pol_obstacles_description:String? = listStatementResult.getString("begin_work_rel_pol_obstacles_description")
                    if(listStatementResult.wasNull()) begin_work_rel_pol_obstacles_description = null
                    var begin_work_rel_pol_obstacles_source:String? = listStatementResult.getString("begin_work_rel_pol_obstacles_source")
                    if(listStatementResult.wasNull()) begin_work_rel_pol_obstacles_source = null
                    var suggested_strategies:String? = listStatementResult.getString("suggested_strategies")
                    if(listStatementResult.wasNull()) suggested_strategies = null
                    var comments:String? = listStatementResult.getString("comments")
                    if(listStatementResult.wasNull()) comments = null
                    var created_at:String? = listStatementResult.getString("created_at")
                    if(listStatementResult.wasNull()) created_at = null
                    var created_by:Int? = listStatementResult.getInt("created_by")
                    if(listStatementResult.wasNull()) created_by = null
                    var modified_at:String? = listStatementResult.getString("modified_at")
                    if(listStatementResult.wasNull()) modified_at = null
                    var modified_by:Int? = listStatementResult.getInt("modified_by")
                    if(listStatementResult.wasNull()) modified_by = null
                    var owning_person:Int? = listStatementResult.getInt("owning_person")
                    if(listStatementResult.wasNull()) owning_person = null
                    var owning_group:Int? = listStatementResult.getInt("owning_group")
                    if(listStatementResult.wasNull()) owning_group = null

                    data.add(LanguageEx(id,
                        language_name,
                        iso,
                        prioritization,
                        progress_bible,
                        island,
                        province,
                        first_language_population,
                        population_value,
                        egids_level,
                        egids_value,
                        least_reached_progress_jps_level,
                        least_reached_value,
                        partner_interest_level,
                        partner_interest_value,
                        partner_interest_description,
                        partner_interest_source,
                        multiple_languages_leverage_linguistic_level,
                        multiple_languages_leverage_linguistic_value,
                        multiple_languages_leverage_linguistic_description,
                        multiple_languages_leverage_linguistic_source,
                        multiple_languages_leverage_joint_training_level,
                        multiple_languages_leverage_joint_training_value,
                        multiple_languages_leverage_joint_training_description,
                        multiple_languages_leverage_joint_training_source,
                        lang_comm_int_in_language_development_level,
                        lang_comm_int_in_language_development_value,
                        lang_comm_int_in_language_development_description,
                        lang_comm_int_in_language_development_source,
                        lang_comm_int_in_scripture_translation_level,
                        lang_comm_int_in_scripture_translation_value,
                        lang_comm_int_in_scripture_translation_description,
                        lang_comm_int_in_scripture_translation_source,
                        access_to_scripture_in_lwc_level,
                        access_to_scripture_in_lwc_value,
                        access_to_scripture_in_lwc_description,
                        access_to_scripture_in_lwc_source,
                        begin_work_geo_challenges_level,
                        begin_work_geo_challenges_value,
                        begin_work_geo_challenges_description,
                        begin_work_geo_challenges_source,
                        begin_work_rel_pol_obstacles_level,
                        begin_work_rel_pol_obstacles_value,
                        begin_work_rel_pol_obstacles_description,
                        begin_work_rel_pol_obstacles_source,
                        suggested_strategies,
                        comments,
                        created_at,
                        created_by,
                        modified_at,
                        modified_by,
                        owning_person,
                        owning_group))
                }
            } catch (e: SQLException) {
                println("error while listing ${e.message}")
                return ReadLanguageExResponse(ErrorType.SQLReadError, mutableListOf())
            }
        }
        return ReadLanguageExResponse(ErrorType.NoError, data)
    }
}