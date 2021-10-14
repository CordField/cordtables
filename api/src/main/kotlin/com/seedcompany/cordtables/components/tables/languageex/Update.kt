package com.seedcompany.cordtables.components.tables.languageex

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.globalroles.UpdatableGlobalRoleFields
import com.seedcompany.cordtables.components.tables.groups.GroupUpdateResponse
import com.seedcompany.cordtables.components.user.GlobalRole
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import java.sql.Time
import java.sql.Timestamp
import javax.sql.DataSource
import kotlin.reflect.full.memberProperties
import kotlin.collections.mutableListOf as mutableListOf
import java.time.Instant

data class UpdateLanguageExResponse(
    val error: ErrorType,
    val data: LanguageEx?
)

data class UpdateLanguageExRequest(
    val updatedFields: LanguageEx,
    val token: String?,
    val id: Int
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("LanguageExUpdate")
class Update(
    @Autowired
    val util: Utility,
    val languageExUtil: LanguageExUtil,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("language_ex/update")
    @ResponseBody
    fun UpdateHandler(@RequestBody req: UpdateLanguageExRequest): UpdateLanguageExResponse {
        if (req.token == null) return UpdateLanguageExResponse(ErrorType.TokenNotFound,null)
        //temporary isAdmin check
        if (!util.isAdmin(req.token)) return UpdateLanguageExResponse(ErrorType.AdminOnly, null)
        println("req: $req")
        var updatedLanguageEx: LanguageEx? = null
        var userId = 0


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
                return UpdateLanguageExResponse(ErrorType.UserNotFound, null)
            }
            try {
                var reqValues: MutableList<Any> = mutableListOf()
                var columnNames: MutableList<String> = mutableListOf()
                var updateSql = "update sc.languages_ex set"
                for (prop in LanguageEx::class.memberProperties) {
                    val propValue = prop.get(req.updatedFields)
                    println("$propValue ${prop.name}")
                    if (propValue != null && prop.name !in languageExUtil.nonMutableColumns) {
                        updateSql = "$updateSql ${prop.name} = ?,"
                        reqValues.add(propValue)
                        columnNames.add(prop.name)
                        if(prop.name == "egids_level"){
                            updateSql = "$updateSql egids_value = ?,"
                            reqValues.add(languageExUtil.getEgidsValue(propValue as egidsScale))
                        }
                        if(prop.name == "least_reached_progress_jps_level"){
                            updateSql = "$updateSql least_reached_value = ?,"
                            reqValues.add(languageExUtil.getLeastReachedValue(propValue as leastReachedProgressScale))
                        }
                        if(prop.name == "partner_interest_level"){
                            updateSql = "$updateSql partner_interest_value = ?,"
                            reqValues.add(languageExUtil.getPartnerInterestValue(propValue as partnerInterestScale))
                        }
                        if(prop.name == "multiple_languages_leverage_linguistic_level"){
                            updateSql = "$updateSql multiple_languages_leverage_linguistic_value = ?,"
                            reqValues.add(languageExUtil.getMultipleLanguagesLeverageLinguisticValue(propValue as multipleLanguagesLeverageLinguisticScale))
                        }
                        if(prop.name == "multiple_languages_leverage_joint_training_level"){
                            updateSql = "$updateSql multiple_languages_leverage_joint_training_value = ?,"
                            reqValues.add(languageExUtil.getMultipleLanguagesLeverageJointTrainingValue(propValue as multipleLanguagesLeverageJointTrainingScale))
                        }
                        if(prop.name == "lang_comm_int_in_language_development_level"){
                            updateSql = "$updateSql lang_comm_int_in_language_development_value = ?,"
                            reqValues.add(languageExUtil.getLangCommIntInLanguageDevelopmentValue(propValue as langCommIntInLanguageDevelopmentScale))
                        }
                        if(prop.name == "lang_comm_int_in_scripture_translation_level"){
                            updateSql = "$updateSql lang_comm_int_in_scripture_translation_value = ?,"
                            reqValues.add(languageExUtil.getLangCommIntInScriptureTranslationValue(propValue as langCommIntInScriptureTranslationScale))
                        }
                        if(prop.name == "access_to_scripture_in_lwc_level"){
                            updateSql = "$updateSql access_to_scripture_in_lwc_value = ?,"
                            reqValues.add(languageExUtil.getAccessToScriptureInLwcValue(propValue as accessToScriptureInLwcScale))
                        }
                        if(prop.name == "begin_work_geo_challenges_level"){
                            updateSql = "$updateSql begin_work_geo_challenges_value = ?,"
                            reqValues.add(languageExUtil.getBeginWorkGeoChallengesValue(propValue as beginWorkGeoChallengesScale))
                        }
                        if(prop.name == "begin_work_rel_pol_obstacles_level"){
                            updateSql = "$updateSql begin_work_rel_pol_obstacles_value = ?,"
                            reqValues.add(languageExUtil.getBeginWorkRelPolObstaclesValue(propValue as beginWorkRelPolObstaclesScale))
                        }
                    }
                }
                if(!util.userHasUpdatePermission(req.token,"sc.languages_ex",columnNames)){
                    return UpdateLanguageExResponse(ErrorType.DoesNotHaveUpdatePermission, null)
                }
                updateSql = "$updateSql modified_by = ?, modified_at = ? where id = ? returning *"
                println(updateSql)
                val updateStatement = conn.prepareCall(
                    updateSql
                )
                var counter = 1
                reqValues.forEach { value ->
                    when (value) {
                        is Int -> updateStatement.setInt(counter, value)
                        is String -> updateStatement.setObject(counter, value, java.sql.Types.OTHER)
                        is Double -> updateStatement.setDouble(counter,value)
                    }
                    counter += 1
                }
//                modified_by, modified_at, id
                updateStatement.setInt(counter, userId)
                updateStatement.setTimestamp(counter+1,  Timestamp(Instant.now().toEpochMilli()))
                updateStatement.setInt(counter+2, req.id)
                val updateStatementResult = updateStatement.executeQuery()
//
                if (updateStatementResult.next()) {
                    var id:Int? = updateStatementResult.getInt("id")
                    if(updateStatementResult.wasNull()) id = null
                    var language_name:String? = updateStatementResult.getString("language_name")
                    if(updateStatementResult.wasNull()) language_name = null
                    var iso:String? = updateStatementResult.getString("iso")
                    if(updateStatementResult.wasNull()) iso = null
                    var prioritization:Double? = updateStatementResult.getDouble("prioritization")
                    if(updateStatementResult.wasNull()) prioritization = null
                    var progress_bible:Boolean? = updateStatementResult.getBoolean("progress_bible")
                    if(updateStatementResult.wasNull()) progress_bible = null
                    var island:String? = updateStatementResult.getString("island")
                    if(updateStatementResult.wasNull()) island = null
                    var province:String? = updateStatementResult.getString("province")
                    if(updateStatementResult.wasNull()) province = null
                    var first_language_population:Int? = updateStatementResult.getInt("first_language_population")
                    if(updateStatementResult.wasNull()) first_language_population = null
                    var population_value:Double? = updateStatementResult.getDouble("population_value")
                    if(updateStatementResult.wasNull()) population_value = null
                    var egids_level:String? = updateStatementResult.getString("egids_level")
                    if(updateStatementResult.wasNull()) egids_level = null
                    var egids_value:Double? = updateStatementResult.getDouble("egids_value")
                    if(updateStatementResult.wasNull()) egids_value = null
                    var least_reached_progress_jps_level:String? = updateStatementResult.getString("least_reached_progress_jps_level")
                    if(updateStatementResult.wasNull()) least_reached_progress_jps_level = null
                    var least_reached_value:Double? = updateStatementResult.getDouble("least_reached_value")
                    if(updateStatementResult.wasNull()) least_reached_value = null
                    var partner_interest_level:String? = updateStatementResult.getString("partner_interest_level")
                    if(updateStatementResult.wasNull()) partner_interest_level = null
                    var partner_interest_value:Double? = updateStatementResult.getDouble("partner_interest_value")
                    if(updateStatementResult.wasNull()) partner_interest_value = null
                    var partner_interest_description:String? = updateStatementResult.getString("partner_interest_description")
                    if(updateStatementResult.wasNull()) partner_interest_description = null
                    var partner_interest_source:String? = updateStatementResult.getString("partner_interest_source")
                    if(updateStatementResult.wasNull()) partner_interest_source = null
                    var multiple_languages_leverage_linguistic_level:String? = updateStatementResult.getString("multiple_languages_leverage_linguistic_level")
                    if(updateStatementResult.wasNull()) multiple_languages_leverage_linguistic_level = null
                    var multiple_languages_leverage_linguistic_value:Double? = updateStatementResult.getDouble("multiple_languages_leverage_linguistic_value")
                    if(updateStatementResult.wasNull()) multiple_languages_leverage_linguistic_value = null
                    var multiple_languages_leverage_linguistic_description:String? = updateStatementResult.getString("multiple_languages_leverage_linguistic_description")
                    if(updateStatementResult.wasNull()) multiple_languages_leverage_linguistic_description = null
                    var multiple_languages_leverage_linguistic_source:String? = updateStatementResult.getString("multiple_languages_leverage_linguistic_source")
                    if(updateStatementResult.wasNull()) multiple_languages_leverage_linguistic_source = null
                    var multiple_languages_leverage_joint_training_level:String? = updateStatementResult.getString("multiple_languages_leverage_joint_training_level")
                    if(updateStatementResult.wasNull()) multiple_languages_leverage_joint_training_level = null
                    var multiple_languages_leverage_joint_training_value:Double? = updateStatementResult.getDouble("multiple_languages_leverage_joint_training_value")
                    if(updateStatementResult.wasNull()) multiple_languages_leverage_joint_training_value = null
                    var multiple_languages_leverage_joint_training_description:String? = updateStatementResult.getString("multiple_languages_leverage_joint_training_description")
                    if(updateStatementResult.wasNull()) multiple_languages_leverage_joint_training_description = null
                    var multiple_languages_leverage_joint_training_source:String? = updateStatementResult.getString("multiple_languages_leverage_joint_training_source")
                    if(updateStatementResult.wasNull()) multiple_languages_leverage_joint_training_source = null
                    var lang_comm_int_in_language_development_level:String? = updateStatementResult.getString("lang_comm_int_in_language_development_level")
                    if(updateStatementResult.wasNull()) lang_comm_int_in_language_development_level = null
                    var lang_comm_int_in_language_development_value:Double? = updateStatementResult.getDouble("lang_comm_int_in_language_development_value")
                    if(updateStatementResult.wasNull()) lang_comm_int_in_language_development_value = null
                    var lang_comm_int_in_language_development_description:String? = updateStatementResult.getString("lang_comm_int_in_language_development_description")
                    if(updateStatementResult.wasNull()) lang_comm_int_in_language_development_description = null
                    var lang_comm_int_in_language_development_source:String? = updateStatementResult.getString("lang_comm_int_in_language_development_source")
                    if(updateStatementResult.wasNull()) lang_comm_int_in_language_development_source = null
                    var lang_comm_int_in_scripture_translation_level:String? = updateStatementResult.getString("lang_comm_int_in_scripture_translation_level")
                    if(updateStatementResult.wasNull()) lang_comm_int_in_scripture_translation_level = null
                    var lang_comm_int_in_scripture_translation_value:Double? = updateStatementResult.getDouble("lang_comm_int_in_scripture_translation_value")
                    if(updateStatementResult.wasNull()) lang_comm_int_in_scripture_translation_value = null
                    var lang_comm_int_in_scripture_translation_description:String? = updateStatementResult.getString("lang_comm_int_in_scripture_translation_description")
                    if(updateStatementResult.wasNull()) lang_comm_int_in_scripture_translation_description = null
                    var lang_comm_int_in_scripture_translation_source:String? = updateStatementResult.getString("lang_comm_int_in_scripture_translation_source")
                    if(updateStatementResult.wasNull()) lang_comm_int_in_scripture_translation_source = null
                    var access_to_scripture_in_lwc_level:String? = updateStatementResult.getString("access_to_scripture_in_lwc_level")
                    if(updateStatementResult.wasNull()) access_to_scripture_in_lwc_level = null
                    var access_to_scripture_in_lwc_value:Double? = updateStatementResult.getDouble("access_to_scripture_in_lwc_value")
                    if(updateStatementResult.wasNull()) access_to_scripture_in_lwc_value = null
                    var access_to_scripture_in_lwc_description:String? = updateStatementResult.getString("access_to_scripture_in_lwc_description")
                    if(updateStatementResult.wasNull()) access_to_scripture_in_lwc_description = null
                    var access_to_scripture_in_lwc_source:String? = updateStatementResult.getString("access_to_scripture_in_lwc_source")
                    if(updateStatementResult.wasNull()) access_to_scripture_in_lwc_source = null
                    var begin_work_geo_challenges_level:String? = updateStatementResult.getString("begin_work_geo_challenges_level")
                    if(updateStatementResult.wasNull()) begin_work_geo_challenges_level = null
                    var begin_work_geo_challenges_value:Double? = updateStatementResult.getDouble("begin_work_geo_challenges_value")
                    if(updateStatementResult.wasNull()) begin_work_geo_challenges_value = null
                    var begin_work_geo_challenges_description:String? = updateStatementResult.getString("begin_work_geo_challenges_description")
                    if(updateStatementResult.wasNull()) begin_work_geo_challenges_description = null
                    var begin_work_geo_challenges_source:String? = updateStatementResult.getString("begin_work_geo_challenges_source")
                    if(updateStatementResult.wasNull()) begin_work_geo_challenges_source = null
                    var begin_work_rel_pol_obstacles_level:String? = updateStatementResult.getString("begin_work_rel_pol_obstacles_level")
                    if(updateStatementResult.wasNull()) begin_work_rel_pol_obstacles_level = null
                    var begin_work_rel_pol_obstacles_value:Double? = updateStatementResult.getDouble("begin_work_rel_pol_obstacles_value")
                    if(updateStatementResult.wasNull()) begin_work_rel_pol_obstacles_value = null
                    var begin_work_rel_pol_obstacles_description:String? = updateStatementResult.getString("begin_work_rel_pol_obstacles_description")
                    if(updateStatementResult.wasNull()) begin_work_rel_pol_obstacles_description = null
                    var begin_work_rel_pol_obstacles_source:String? = updateStatementResult.getString("begin_work_rel_pol_obstacles_source")
                    if(updateStatementResult.wasNull()) begin_work_rel_pol_obstacles_source = null
                    var suggested_strategies:String? = updateStatementResult.getString("suggested_strategies")
                    if(updateStatementResult.wasNull()) suggested_strategies = null
                    var comments:String? = updateStatementResult.getString("comments")
                    if(updateStatementResult.wasNull()) comments = null
                    var created_at:String? = updateStatementResult.getString("created_at")
                    if(updateStatementResult.wasNull()) created_at = null
                    var created_by:Int? = updateStatementResult.getInt("created_by")
                    if(updateStatementResult.wasNull()) created_by = null
                    var modified_at:String? = updateStatementResult.getString("modified_at")
                    if(updateStatementResult.wasNull()) modified_at = null
                    var modified_by:Int? = updateStatementResult.getInt("modified_by")
                    if(updateStatementResult.wasNull()) modified_by = null
                    var owning_person:Int? = updateStatementResult.getInt("owning_person")
                    if(updateStatementResult.wasNull()) owning_person = null
                    var owning_group:Int? = updateStatementResult.getInt("owning_group")
                    if(updateStatementResult.wasNull()) owning_group = null
                    updatedLanguageEx = LanguageEx(id,
                            language_name,
                            iso,
                            prioritization,
                            progress_bible,
                            island,
                            province,
                            first_language_population,
                            population_value,
                            egids_level as egidsScale,
                            egids_value,
                            least_reached_progress_jps_level as leastReachedProgressScale,
                            least_reached_value,
                            partner_interest_level as partnerInterestScale,
                            partner_interest_value,
                            partner_interest_description,
                            partner_interest_source,
                            multiple_languages_leverage_linguistic_level as multipleLanguagesLeverageLinguisticScale,
                            multiple_languages_leverage_linguistic_value,
                            multiple_languages_leverage_linguistic_description,
                            multiple_languages_leverage_linguistic_source,
                            multiple_languages_leverage_joint_training_level as multipleLanguagesLeverageJointTrainingScale,
                            multiple_languages_leverage_joint_training_value,
                            multiple_languages_leverage_joint_training_description,
                            multiple_languages_leverage_joint_training_source,
                            lang_comm_int_in_language_development_level as langCommIntInLanguageDevelopmentScale,
                            lang_comm_int_in_language_development_value,
                            lang_comm_int_in_language_development_description,
                            lang_comm_int_in_language_development_source,
                            lang_comm_int_in_scripture_translation_level as langCommIntInScriptureTranslationScale,
                            lang_comm_int_in_scripture_translation_value,
                            lang_comm_int_in_scripture_translation_description,
                            lang_comm_int_in_scripture_translation_source,
                            access_to_scripture_in_lwc_level as accessToScriptureInLwcScale,
                            access_to_scripture_in_lwc_value,
                            access_to_scripture_in_lwc_description,
                            access_to_scripture_in_lwc_source,
                            begin_work_geo_challenges_level as beginWorkGeoChallengesScale,
                            begin_work_geo_challenges_value,
                            begin_work_geo_challenges_description,
                            begin_work_geo_challenges_source,
                            begin_work_rel_pol_obstacles_level as beginWorkRelPolObstaclesScale,
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
                            owning_group)
                    println("updated row's id: $id")
                }
            } catch (e: SQLException) {
                println(e.message)
                return UpdateLanguageExResponse(ErrorType.SQLUpdateError, null)
            }
        }
        return UpdateLanguageExResponse(ErrorType.NoError, updatedLanguageEx)
    }
}
