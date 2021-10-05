package com.seedcompany.cordspringstencil.components.tables.languageex

import com.seedcompany.cordspringstencil.common.ErrorType
import com.seedcompany.cordspringstencil.common.Utility
import com.seedcompany.cordspringstencil.components.tables.globalroles.UpdatableGlobalRoleFields
import com.seedcompany.cordspringstencil.components.tables.groups.GroupUpdateResponse
import com.seedcompany.cordspringstencil.components.user.GlobalRole
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
                val getUserIdStatement = conn.prepareCall("select person from public.tokens where token = ?")
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
                    if (propValue != null) {
                        updateSql = "$updateSql ${prop.name} = ?,"
                        reqValues.add(propValue)
                        columnNames.add(prop.name)
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
                        is String -> updateStatement.setString(counter, value)
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
                    val id = updateStatementResult.getInt("id")
                    val createdAt = updateStatementResult.getString("created_at")
                    val createdBy = updateStatementResult.getInt("created_by")
                    val modifiedAt = updateStatementResult.getString("modified_at")
                    val modifiedBy = updateStatementResult.getInt("modified_by")
                    val lang_name = updateStatementResult.getString("lang_name")
                    val lang_code = updateStatementResult.getString("lang_code")
                    val location = updateStatementResult.getString("location")
                    val first_lang_population = updateStatementResult.getInt("first_lang_population")
                    val population = updateStatementResult.getInt("population")
                    val egids_level = updateStatementResult.getInt("egids_level")
                    val egids_value = updateStatementResult.getInt("egids_value")
                    val least_reached_progress_jps_scale = updateStatementResult.getInt("least_reached_progress_jps_scale")
                    val least_reached_value = updateStatementResult.getInt("least_reached_value")
                    val partner_interest = updateStatementResult.getInt("partner_interest")
                    val partner_interest_description = updateStatementResult.getString("partner_interest_description")
                    val partner_interest_source = updateStatementResult.getString("partner_interest_source")
                    val multi_lang_leverage = updateStatementResult.getInt("multi_lang_leverage")
                    val multi_lang_leverage_description = updateStatementResult.getString("multi_lang_leverage_description")
                    val multi_lang_leverage_source = updateStatementResult.getString("multi_lang_leverage_source")
                    val community_interest = updateStatementResult.getInt("community_interest")
                    val community_interest_description = updateStatementResult.getString("community_interest_description")
                    val community_interest_source = updateStatementResult.getString("community_interest_source")
                    val community_interest_value = updateStatementResult.getInt("community_interest_value")
                    val community_interest_scripture_description = updateStatementResult.getString("community_interest_scripture_description")
                    val community_interest_scripture_source = updateStatementResult.getString("community_interest_scripture_source")
                    val lwc_scripture_access = updateStatementResult.getInt("lwc_scripture_access")
                    val lwc_scripture_description = updateStatementResult.getString("lwc_scripture_description")
                    val lwc_scripture_source = updateStatementResult.getString("lwc_scripture_source")
                    val access_to_begin = updateStatementResult.getInt("access_to_begin")
                    val access_to_begin_description = updateStatementResult.getString("access_to_begin_description")
                    val access_to_begin_source = updateStatementResult.getString("access_to_begin_source")
                    val suggested_strategies = updateStatementResult.getString("suggested_strategies")
                    val comments = updateStatementResult.getString("comments")
                    val prioritization = updateStatementResult.getInt("prioritization")
                    val progress_bible = updateStatementResult.getInt("progress_bible")
                    updatedLanguageEx = LanguageEx(id, createdAt, createdBy, modifiedAt, modifiedBy,lang_name,lang_code,location,first_lang_population,population,
                        egids_level,egids_value,least_reached_progress_jps_scale,least_reached_value,partner_interest,partner_interest_description,partner_interest_source,
                        multi_lang_leverage,multi_lang_leverage_description,multi_lang_leverage_source,community_interest,community_interest_description,community_interest_scripture_source,community_interest_value,community_interest_scripture_description,community_interest_source,lwc_scripture_access,lwc_scripture_description,lwc_scripture_source,access_to_begin,access_to_begin_description,
                        access_to_begin_source,suggested_strategies,comments,prioritization,progress_bible
                    )
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
