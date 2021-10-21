package com.seedcompany.cordtables.components.tables.languageex

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource
import kotlin.reflect.full.memberProperties


data class CreateLanguageExResponse(
        val error: ErrorType,
        val data: LanguageEx?
)

data class CreateLanguageExRequest(
        val insertedFields: LanguageEx,
        val token: String?,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("LanguageExCreate")
class Create(
        @Autowired
        val util: Utility,
        val languageExUtil: LanguageExUtil,

        @Autowired
        val ds: DataSource,
) {
    @PostMapping("language_ex/create")
    @ResponseBody
    fun CreateHandler(@RequestBody req: CreateLanguageExRequest): CreateLanguageExResponse {

        if (req.token == null) return CreateLanguageExResponse(ErrorType.TokenNotFound, null)
        if (!util.isAdmin(req.token)) return CreateLanguageExResponse(ErrorType.AdminOnly, null)

        println("req: $req")
        var insertedLanguageEx: LanguageEx? = null
        var userId = 0
        val reqValues: MutableList<Any> = mutableListOf()
        this.ds.connection.use { conn ->
            try {
                val getUserIdStatement = conn.prepareCall("select person from admin.tokens where token = ?")
                getUserIdStatement.setString(1, req.token)
                val getUserIdResult = getUserIdStatement.executeQuery()
                if (getUserIdResult.next()) {
                    userId = getUserIdResult.getInt("person")
                    println("userId: $userId")
                } else {
                    throw SQLException("User not found")
                }
            } catch (e: SQLException) {
                println(e.message)
                return CreateLanguageExResponse(ErrorType.UserNotFound, null)
            }
            try {
                var insertStatementKeys = "insert into sc.languages_ex("
                var insertStatementValues = " values("
                for (prop in LanguageEx::class.memberProperties) {
                    val propValue = prop.get(req.insertedFields)
                    println("$propValue ${prop.name}")
                    if (propValue != null && prop.name !in languageExUtil.nonMutableColumns) {
                        insertStatementKeys = "$insertStatementKeys ${prop.name},"
                        insertStatementValues = "$insertStatementValues ?,"
                        reqValues.add(propValue)
                        if (prop.name == "egids_level") {
                            insertStatementKeys = "$insertStatementKeys egids_value,"
                            insertStatementValues = "$insertStatementValues ?,"
                            reqValues.add(languageExUtil.getEgidsValue(propValue as EgidsScale))
                        }
                        if (prop.name == "least_reached_progress_jps_level") {
                            insertStatementKeys = "$insertStatementKeys least_reached_value,"
                            insertStatementValues = "$insertStatementValues ?,"
                            reqValues.add(languageExUtil.getLeastReachedValue(propValue as LeastReachedProgressScale))
                        }
                        if (prop.name == "partner_interest_level") {
                            insertStatementKeys = "$insertStatementKeys partner_interest_value,"
                            insertStatementValues = "$insertStatementValues ?,"
                            reqValues.add(languageExUtil.getPartnerInterestValue(propValue as PartnerInterestScale))
                        }
                        if (prop.name == "multiple_languages_leverage_linguistic_level") {
                            insertStatementKeys = "$insertStatementKeys multiple_languages_leverage_linguistic_value,"
                            insertStatementValues = "$insertStatementValues ?,"
                            reqValues.add(languageExUtil.getMultipleLanguagesLeverageLinguisticValue(propValue as MultipleLanguagesLeverageLinguisticScale))
                        }
                        if (prop.name == "multiple_languages_leverage_joint_training_level") {
                            insertStatementKeys = "$insertStatementKeys multiple_languages_leverage_joint_training_value,"
                            insertStatementValues = "$insertStatementValues ?,"
                            reqValues.add(languageExUtil.getMultipleLanguagesLeverageJointTrainingValue(propValue as MultipleLanguagesLeverageJointTrainingScale))
                        }
                        if (prop.name == "lang_comm_int_in_language_development_level") {
                            insertStatementKeys = "$insertStatementKeys lang_comm_int_in_language_development_value,"
                            insertStatementValues = "$insertStatementValues ?,"
                            reqValues.add(languageExUtil.getLangCommIntInLanguageDevelopmentValue(propValue as LangCommIntInLanguageDevelopmentScale))
                        }
                        if (prop.name == "lang_comm_int_in_scripture_translation_level") {
                            insertStatementKeys = "$insertStatementKeys lang_comm_int_in_scripture_translation_value,"
                            insertStatementValues = "$insertStatementValues ?,"
                            reqValues.add(languageExUtil.getLangCommIntInScriptureTranslationValue(propValue as LangCommIntInScriptureTranslationScale))
                        }
                        if (prop.name == "access_to_scripture_in_lwc_level") {
                            insertStatementKeys = "$insertStatementKeys access_to_scripture_in_lwc_value,"
                            insertStatementValues = "$insertStatementValues ?,"
                            reqValues.add(languageExUtil.getAccessToScriptureInLwcValue(propValue as AccessToScriptureInLwcScale))
                        }
                        if (prop.name == "begin_work_geo_challenges_level") {
                            insertStatementKeys = "$insertStatementKeys begin_work_geo_challenges_value,"
                            insertStatementValues = "$insertStatementValues ?,"
                            reqValues.add(languageExUtil.getBeginWorkGeoChallengesValue(propValue as BeginWorkGeoChallengesScale))
                        }
                        if (prop.name == "begin_work_rel_pol_obstacles_level") {
                            insertStatementKeys = "$insertStatementKeys begin_work_rel_pol_obstacles_value,"
                            insertStatementValues = "$insertStatementValues ?,"
                            reqValues.add(languageExUtil.getBeginWorkRelPolObstaclesValue(propValue as BeginWorkRelPolObstaclesScale))
                        }
                    }
                }
                insertStatementKeys = "$insertStatementKeys modified_by,created_by)"
                insertStatementValues = "$insertStatementValues ?,?) returning *;"
                val insertStatementSQL = "$insertStatementKeys $insertStatementValues"
                println(insertStatementSQL)

                val insertStatement = conn.prepareCall(
                        insertStatementSQL
                )

                var counter = 1
                reqValues.forEach { value ->
                    when (value) {
                        is Int -> insertStatement.setInt(counter, value)
                        is String -> insertStatement.setString(counter, value)
                        is Double -> insertStatement.setDouble(counter, value)
                        else -> insertStatement.setObject(counter, value, java.sql.Types.OTHER)
                    }
                    counter += 1
                }
                insertStatement.setInt(counter, userId)
                insertStatement.setInt(counter + 1, userId)


                val insertStatementResult = insertStatement.executeQuery()

                if (insertStatementResult.next()) {
                    var id: Int? = insertStatementResult.getInt("id")
                    if (insertStatementResult.wasNull()) id = null
                    var language_name: String? = insertStatementResult.getString("language_name")
                    if (insertStatementResult.wasNull()) language_name = null
                    var iso: String? = insertStatementResult.getString("iso")
                    if (insertStatementResult.wasNull()) iso = null
                    var prioritization: Double? = insertStatementResult.getDouble("prioritization")
                    if (insertStatementResult.wasNull()) prioritization = null
                    var progress_bible: Boolean? = insertStatementResult.getBoolean("progress_bible")
                    if (insertStatementResult.wasNull()) progress_bible = null
                    var island: String? = insertStatementResult.getString("island")
                    if (insertStatementResult.wasNull()) island = null
                    var province: String? = insertStatementResult.getString("province")
                    if (insertStatementResult.wasNull()) province = null
                    var first_language_population: Int? = insertStatementResult.getInt("first_language_population")
                    if (insertStatementResult.wasNull()) first_language_population = null
                    var population_value: Double? = insertStatementResult.getDouble("population_value")
                    if (insertStatementResult.wasNull()) population_value = null
                    var egids_level: String? = insertStatementResult.getString("egids_level")
                    if (insertStatementResult.wasNull()) egids_level = null
                    var egids_value: Double? = insertStatementResult.getDouble("egids_value")
                    if (insertStatementResult.wasNull()) egids_value = null
                    var least_reached_progress_jps_level: String? = insertStatementResult.getString("least_reached_progress_jps_level")
                    if (insertStatementResult.wasNull()) least_reached_progress_jps_level = null
                    var least_reached_value: Double? = insertStatementResult.getDouble("least_reached_value")
                    if (insertStatementResult.wasNull()) least_reached_value = null
                    var partner_interest_level: String? = insertStatementResult.getString("partner_interest_level")
                    if (insertStatementResult.wasNull()) partner_interest_level = null
                    var partner_interest_value: Double? = insertStatementResult.getDouble("partner_interest_value")
                    if (insertStatementResult.wasNull()) partner_interest_value = null
                    var partner_interest_description: String? = insertStatementResult.getString("partner_interest_description")
                    if (insertStatementResult.wasNull()) partner_interest_description = null
                    var partner_interest_source: String? = insertStatementResult.getString("partner_interest_source")
                    if (insertStatementResult.wasNull()) partner_interest_source = null
                    var multiple_languages_leverage_linguistic_level: String? = insertStatementResult.getString("multiple_languages_leverage_linguistic_level")
                    if (insertStatementResult.wasNull()) multiple_languages_leverage_linguistic_level = null
                    var multiple_languages_leverage_linguistic_value: Double? = insertStatementResult.getDouble("multiple_languages_leverage_linguistic_value")
                    if (insertStatementResult.wasNull()) multiple_languages_leverage_linguistic_value = null
                    var multiple_languages_leverage_linguistic_description: String? = insertStatementResult.getString("multiple_languages_leverage_linguistic_description")
                    if (insertStatementResult.wasNull()) multiple_languages_leverage_linguistic_description = null
                    var multiple_languages_leverage_linguistic_source: String? = insertStatementResult.getString("multiple_languages_leverage_linguistic_source")
                    if (insertStatementResult.wasNull()) multiple_languages_leverage_linguistic_source = null
                    var multiple_languages_leverage_joint_training_level: String? = insertStatementResult.getString("multiple_languages_leverage_joint_training_level")
                    if (insertStatementResult.wasNull()) multiple_languages_leverage_joint_training_level = null
                    var multiple_languages_leverage_joint_training_value: Double? = insertStatementResult.getDouble("multiple_languages_leverage_joint_training_value")
                    if (insertStatementResult.wasNull()) multiple_languages_leverage_joint_training_value = null
                    var multiple_languages_leverage_joint_training_description: String? = insertStatementResult.getString("multiple_languages_leverage_joint_training_description")
                    if (insertStatementResult.wasNull()) multiple_languages_leverage_joint_training_description = null
                    var multiple_languages_leverage_joint_training_source: String? = insertStatementResult.getString("multiple_languages_leverage_joint_training_source")
                    if (insertStatementResult.wasNull()) multiple_languages_leverage_joint_training_source = null
                    var lang_comm_int_in_language_development_level: String? = insertStatementResult.getString("lang_comm_int_in_language_development_level")
                    if (insertStatementResult.wasNull()) lang_comm_int_in_language_development_level = null
                    var lang_comm_int_in_language_development_value: Double? = insertStatementResult.getDouble("lang_comm_int_in_language_development_value")
                    if (insertStatementResult.wasNull()) lang_comm_int_in_language_development_value = null
                    var lang_comm_int_in_language_development_description: String? = insertStatementResult.getString("lang_comm_int_in_language_development_description")
                    if (insertStatementResult.wasNull()) lang_comm_int_in_language_development_description = null
                    var lang_comm_int_in_language_development_source: String? = insertStatementResult.getString("lang_comm_int_in_language_development_source")
                    if (insertStatementResult.wasNull()) lang_comm_int_in_language_development_source = null
                    var lang_comm_int_in_scripture_translation_level: String? = insertStatementResult.getString("lang_comm_int_in_scripture_translation_level")
                    if (insertStatementResult.wasNull()) lang_comm_int_in_scripture_translation_level = null
                    var lang_comm_int_in_scripture_translation_value: Double? = insertStatementResult.getDouble("lang_comm_int_in_scripture_translation_value")
                    if (insertStatementResult.wasNull()) lang_comm_int_in_scripture_translation_value = null
                    var lang_comm_int_in_scripture_translation_description: String? = insertStatementResult.getString("lang_comm_int_in_scripture_translation_description")
                    if (insertStatementResult.wasNull()) lang_comm_int_in_scripture_translation_description = null
                    var lang_comm_int_in_scripture_translation_source: String? = insertStatementResult.getString("lang_comm_int_in_scripture_translation_source")
                    if (insertStatementResult.wasNull()) lang_comm_int_in_scripture_translation_source = null
                    var access_to_scripture_in_lwc_level: String? = insertStatementResult.getString("access_to_scripture_in_lwc_level")
                    if (insertStatementResult.wasNull()) access_to_scripture_in_lwc_level = null
                    var access_to_scripture_in_lwc_value: Double? = insertStatementResult.getDouble("access_to_scripture_in_lwc_value")
                    if (insertStatementResult.wasNull()) access_to_scripture_in_lwc_value = null
                    var access_to_scripture_in_lwc_description: String? = insertStatementResult.getString("access_to_scripture_in_lwc_description")
                    if (insertStatementResult.wasNull()) access_to_scripture_in_lwc_description = null
                    var access_to_scripture_in_lwc_source: String? = insertStatementResult.getString("access_to_scripture_in_lwc_source")
                    if (insertStatementResult.wasNull()) access_to_scripture_in_lwc_source = null
                    var begin_work_geo_challenges_level: String? = insertStatementResult.getString("begin_work_geo_challenges_level")
                    if (insertStatementResult.wasNull()) begin_work_geo_challenges_level = null
                    var begin_work_geo_challenges_value: Double? = insertStatementResult.getDouble("begin_work_geo_challenges_value")
                    if (insertStatementResult.wasNull()) begin_work_geo_challenges_value = null
                    var begin_work_geo_challenges_description: String? = insertStatementResult.getString("begin_work_geo_challenges_description")
                    if (insertStatementResult.wasNull()) begin_work_geo_challenges_description = null
                    var begin_work_geo_challenges_source: String? = insertStatementResult.getString("begin_work_geo_challenges_source")
                    if (insertStatementResult.wasNull()) begin_work_geo_challenges_source = null
                    var begin_work_rel_pol_obstacles_level: String? = insertStatementResult.getString("begin_work_rel_pol_obstacles_level")
                    if (insertStatementResult.wasNull()) begin_work_rel_pol_obstacles_level = null
                    var begin_work_rel_pol_obstacles_value: Double? = insertStatementResult.getDouble("begin_work_rel_pol_obstacles_value")
                    if (insertStatementResult.wasNull()) begin_work_rel_pol_obstacles_value = null
                    var begin_work_rel_pol_obstacles_description: String? = insertStatementResult.getString("begin_work_rel_pol_obstacles_description")
                    if (insertStatementResult.wasNull()) begin_work_rel_pol_obstacles_description = null
                    var begin_work_rel_pol_obstacles_source: String? = insertStatementResult.getString("begin_work_rel_pol_obstacles_source")
                    if (insertStatementResult.wasNull()) begin_work_rel_pol_obstacles_source = null
                    var suggested_strategies: String? = insertStatementResult.getString("suggested_strategies")
                    if (insertStatementResult.wasNull()) suggested_strategies = null
                    var comments: String? = insertStatementResult.getString("comments")
                    if (insertStatementResult.wasNull()) comments = null
                    var created_at: String? = insertStatementResult.getString("created_at")
                    if (insertStatementResult.wasNull()) created_at = null
                    var created_by: Int? = insertStatementResult.getInt("created_by")
                    if (insertStatementResult.wasNull()) created_by = null
                    var modified_at: String? = insertStatementResult.getString("modified_at")
                    if (insertStatementResult.wasNull()) modified_at = null
                    var modified_by: Int? = insertStatementResult.getInt("modified_by")
                    if (insertStatementResult.wasNull()) modified_by = null
                    var owning_person: Int? = insertStatementResult.getInt("owning_person")
                    if (insertStatementResult.wasNull()) owning_person = null
                    var owning_group: Int? = insertStatementResult.getInt("owning_group")
                    if (insertStatementResult.wasNull()) owning_group = null
                    insertedLanguageEx = LanguageEx(
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
                    println("newly inserted id: $id")
                }
            } catch (e: SQLException) {
                println(e.message)
                return CreateLanguageExResponse(ErrorType.SQLInsertError, null)
            }
        }
        return CreateLanguageExResponse(ErrorType.NoError, insertedLanguageEx)
    }
}
