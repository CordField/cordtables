package com.seedcompany.cordtables.components.tables.languageex

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import com.seedcompany.cordtables.components.admin.GetSecureListQueryRequest
import com.seedcompany.cordtables.components.tables.sc.language_ex.*
import com.seedcompany.cordtables.components.tables.sc.language_ex.LanguageEx
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


data class ReadLanguageExResponse(
    val error: ErrorType,
    val data: MutableList<LanguageEx>?
)

data class ReadLanguageExRequest(
    val token: String?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("LanguageExList")
class List(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {

    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("language_ex/read")
    @ResponseBody
    fun listHandler(@RequestBody req: ReadLanguageExRequest): ReadLanguageExResponse {
        var data: MutableList<LanguageEx> = mutableListOf()
        if (req.token == null) return ReadLanguageExResponse(ErrorType.TokenNotFound, mutableListOf())

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)

        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "sc.languages_ex",
                columns = arrayOf(
                    "id",
                    "language_name",
                    "iso",
                    "prioritization",
                    "progress_bible",
                    "location_long",
                    "island",
                    "province",
                    "first_language_population",
                    "population_value",
                    "egids_level",
                    "egids_value",
                    "least_reached_progress_jps_level",
                    "least_reached_value",
                    "partner_interest_level",
                    "partner_interest_value",
                    "partner_interest_description",
                    "partner_interest_source",
                    "multiple_languages_leverage_linguistic_level",
                    "multiple_languages_leverage_linguistic_value",
                    "multiple_languages_leverage_linguistic_description",
                    "multiple_languages_leverage_linguistic_source",
                    "multiple_languages_leverage_joint_training_level",
                    "multiple_languages_leverage_joint_training_value",
                    "multiple_languages_leverage_joint_training_description",
                    "multiple_languages_leverage_joint_training_source",
                    "lang_comm_int_in_language_development_level",
                    "lang_comm_int_in_language_development_value",
                    "lang_comm_int_in_language_development_description",
                    "lang_comm_int_in_language_development_source",
                    "lang_comm_int_in_scripture_translation_level",
                    "lang_comm_int_in_scripture_translation_value",
                    "lang_comm_int_in_scripture_translation_description",
                    "lang_comm_int_in_scripture_translation_source",
                    "access_to_scripture_in_lwc_level",
                    "access_to_scripture_in_lwc_value",
                    "access_to_scripture_in_lwc_description",
                    "access_to_scripture_in_lwc_source",
                    "begin_work_geo_challenges_level",
                    "begin_work_geo_challenges_value",
                    "begin_work_geo_challenges_description",
                    "begin_work_geo_challenges_source",
                    "begin_work_rel_pol_obstacles_level",
                    "begin_work_rel_pol_obstacles_value",
                    "begin_work_rel_pol_obstacles_description",
                    "begin_work_rel_pol_obstacles_source",
                    "suggested_strategies",
                    "comments",
                    "chat",
                    "created_at",
                    "created_by",
                    "modified_at",
                    "modified_by",
                    "owning_person",
                    "owning_group",
                    "peer"
                )
            )
        ).query

        try {
            val jdbcResult = jdbcTemplate.queryForRowSet(query, paramSource)
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
                        id = id,
                        language_name = language_name,
                        iso = iso,
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
            return ReadLanguageExResponse(ErrorType.SQLReadError, mutableListOf())
        }

        return ReadLanguageExResponse(ErrorType.NoError, data)
    }
}