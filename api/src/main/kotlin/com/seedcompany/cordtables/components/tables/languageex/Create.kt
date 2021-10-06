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

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("language_ex/create")
    @ResponseBody
    fun CreateHandler(@RequestBody req: CreateLanguageExRequest): CreateLanguageExResponse {

        if (req.token == null) return CreateLanguageExResponse(ErrorType.TokenNotFound, null)
        if(!util.userHasCreatePermission(req.token, "sc.languages_ex"))
            return CreateLanguageExResponse(ErrorType.DoesNotHaveCreatePermission, null)

        println("req: $req")
        var errorType = ErrorType.UnknownError
        var insertedLanguageEx: LanguageEx? = null
        var userId = 0
        val reqValues: MutableList<Any> = mutableListOf()
        this.ds.connection.use { conn ->
            try {
                val getUserIdStatement = conn.prepareCall("select person from public.tokens where token = ?")
                getUserIdStatement.setString(1, req.token)
                val getUserIdResult = getUserIdStatement.executeQuery()
                if (getUserIdResult.next()) {
                    userId = getUserIdResult.getInt("person")
                    println("userId: $userId")
                }
                else {
                    throw SQLException("User not found")
                }
            }
            catch(e:SQLException){
                println(e.message)
                errorType = ErrorType.UserNotFound
                return CreateLanguageExResponse(errorType, null)
            }
            try {
                var insertStatementKeys = "insert into sc.languages_ex("
                var insertStatementValues = " values("
                for (prop in LanguageEx::class.memberProperties) {
                    val propValue = prop.get(req.insertedFields)
                    println("$propValue ${prop.name}")
                    if (propValue != null && prop.name != "modified_by" && prop.name != "modified_at"
                        && prop.name != "created_by" && prop.name != "created_at" && prop.name != "id") {
                        insertStatementKeys = "$insertStatementKeys ${prop.name},"
                        insertStatementValues = "$insertStatementValues ?,"
                        reqValues.add(propValue)
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
                    }
                    counter += 1
                }
                insertStatement.setInt(counter, userId)
                insertStatement.setInt(counter+1,  userId)


                val insertStatementResult = insertStatement.executeQuery()

                if (insertStatementResult.next()) {
                    val id = insertStatementResult.getInt("id")
                    val createdAt = insertStatementResult.getString("created_at")
                    val createdBy = insertStatementResult.getInt("created_by")
                    val modifiedAt = insertStatementResult.getString("modified_at")
                    val modifiedBy = insertStatementResult.getInt("modified_by")
                    val lang_name = insertStatementResult.getString("lang_name")
                    val lang_code = insertStatementResult.getString("lang_code")
                    val location = insertStatementResult.getString("location")
                    val first_lang_population = insertStatementResult.getInt("first_lang_population")
                    val population = insertStatementResult.getInt("population")
                    val egids_level = insertStatementResult.getInt("egids_level")
                    val egids_value = insertStatementResult.getInt("egids_value")
                    val least_reached_progress_jps_scale = insertStatementResult.getInt("least_reached_progress_jps_scale")
                    val least_reached_value = insertStatementResult.getInt("least_reached_value")
                    val partner_interest = insertStatementResult.getInt("partner_interest")
                    val partner_interest_description = insertStatementResult.getString("partner_interest_description")
                    val partner_interest_source = insertStatementResult.getString("partner_interest_source")
                    val multi_lang_leverage = insertStatementResult.getInt("multi_lang_leverage")
                    val multi_lang_leverage_description = insertStatementResult.getString("multi_lang_leverage_description")
                    val multi_lang_leverage_source = insertStatementResult.getString("multi_lang_leverage_source")
                    val community_interest = insertStatementResult.getInt("community_interest")
                    val community_interest_description = insertStatementResult.getString("community_interest_description")
                    val community_interest_source = insertStatementResult.getString("community_interest_source")
                    val community_interest_value = insertStatementResult.getInt("community_interest_value")
                    val community_interest_scripture_description = insertStatementResult.getString("community_interest_scripture_description")
                    val community_interest_scripture_source = insertStatementResult.getString("community_interest_scripture_source")
                    val lwc_scripture_access = insertStatementResult.getInt("lwc_scripture_access")
                    val lwc_scripture_description = insertStatementResult.getString("lwc_scripture_description")
                    val lwc_scripture_source = insertStatementResult.getString("lwc_scripture_source")
                    val access_to_begin = insertStatementResult.getInt("access_to_begin")
                    val access_to_begin_description = insertStatementResult.getString("access_to_begin_description")
                    val access_to_begin_source = insertStatementResult.getString("access_to_begin_source")
                    val suggested_strategies = insertStatementResult.getString("suggested_strategies")
                    val comments = insertStatementResult.getString("comments")
                    val prioritization = insertStatementResult.getInt("prioritization")
                    val progress_bible = insertStatementResult.getInt("progress_bible")
                    insertedLanguageEx = LanguageEx(id, createdAt, createdBy, modifiedAt, modifiedBy,lang_name,lang_code,location,first_lang_population,population,
                        egids_level,egids_value,least_reached_progress_jps_scale,least_reached_value,partner_interest,partner_interest_description,partner_interest_source,
                        multi_lang_leverage,multi_lang_leverage_description,multi_lang_leverage_source,community_interest,community_interest_description,community_interest_scripture_source,community_interest_value,community_interest_scripture_description,community_interest_source,lwc_scripture_access,lwc_scripture_description,lwc_scripture_source,access_to_begin,access_to_begin_description,
                        access_to_begin_source,suggested_strategies,comments,prioritization,progress_bible
                    )
                    println("newly inserted id: $id")
                }
            }
            catch (e:SQLException ){
                println(e.message)
                errorType = ErrorType.SQLInsertError
                return CreateLanguageExResponse(errorType, null)
            }
        }
        return CreateLanguageExResponse(ErrorType.NoError,insertedLanguageEx)
    }
}