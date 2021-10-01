package com.seedcompany.cordspringstencil.components.tables.languageex

import com.seedcompany.cordspringstencil.common.ErrorType
import com.seedcompany.cordspringstencil.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource

data class LanguageEx(
    val id: Int,
    val createdAt: String,
    val createdBy: Int,
    val modifiedAt: String,
    val modifiedBy: Int,
    val langName: String,
    val langCode: String,
    val location: String,
    val firstLangPopulation: Int,
    val population: Int,
    val egidsLevel: Int,
    val egidsValue: Int,
    val leastReachedProgressJpsScale: Int,
    val leastReachedValue: Int,
    val partnerInterest: Int,
    val partnerInterestDescription: String,
    val partnerInterestSource: String,
    val multiLangLeverage: Int,
    val multiLangLeverageDescription: String,
    val multiLangLeverageSource: String,
    val communityInterest: Int,
    val communityInterestDescription: String,
    val communityInterestSource: String,
    val communityInterestValue: Int,
    val communityInterestScriptureDescription: String,
    val communityInterestScriptureSource: String,
    val lwcScriptureAccess: Int,
    val lwcScriptureDescription: String,
    val lwcScriptureSource: String,
    val accessToBegin: Int,
    val accessToBeginDescription: String,
    val accessToBeginSource: String,
    val suggestedStrategies: String,
    val comments: String,
    val prioritization: Int,
    val progressBible: Int,
)

data class ReadLanguageExResponse(
    val error: ErrorType,
    val data: MutableList<LanguageEx>?
)

@CrossOrigin(origins = ["http://localhost:3333"])
@Controller("LanguageExRead")
class Read(
    @Autowired
    val util: Utility,
    @Autowired
    val ds: DataSource,
) {
    @PostMapping("language_ex/read")
    @ResponseBody
    fun ReadHandler(): ReadLanguageExResponse {
        //mutableList as we need to add each global role as an element to it
        var data: MutableList<LanguageEx> = mutableListOf()


        this.ds.connection.use { conn ->
            val listStatement = conn.prepareCall(
                "select * from sc.languages_ex"
            )
            try {
                val listStatementResult = listStatement.executeQuery()
                while (listStatementResult.next()) {

                    val id = listStatementResult.getInt("id")
                    val createdAt = listStatementResult.getString("created_at")

                    val createdBy = listStatementResult.getInt("created_by")
                    val modifiedAt = listStatementResult.getString("modified_at")
                    val modifiedBy = listStatementResult.getInt("modified_by")
                    val lang_name = listStatementResult.getString("lang_name")
                    val lang_code = listStatementResult.getString("lang_code")
                    val location = listStatementResult.getString("location")
                    val first_lang_population = listStatementResult.getInt("first_lang_population")
                    val population = listStatementResult.getInt("population")
                    val egids_level = listStatementResult.getInt("egids_level")
                    val egids_value = listStatementResult.getInt("egids_value")
                    val least_reached_progress_jps_scale = listStatementResult.getInt("least_reached_progress_jps_scale")
                    val least_reached_value = listStatementResult.getInt("least_reached_value")
                    val partner_interest = listStatementResult.getInt("partner_interest")
                    val partner_interest_description = listStatementResult.getString("partner_interest_description")
                    val partner_interest_source = listStatementResult.getString("partner_interest_source")
                    val multi_lang_leverage = listStatementResult.getInt("multi_lang_leverage")
                    val multi_lang_leverage_description = listStatementResult.getString("multi_lang_leverage_description")
                    val multi_lang_leverage_source = listStatementResult.getString("multi_lang_leverage_source")
                    val community_interest = listStatementResult.getInt("community_interest")
                    val community_interest_description = listStatementResult.getString("community_interest_description")
                    val community_interest_source = listStatementResult.getString("community_interest_source")
                    val community_interest_value = listStatementResult.getInt("community_interest_value")
                    val community_interest_scripture_description = listStatementResult.getString("community_interest_scripture_description")
                    val community_interest_scripture_source = listStatementResult.getString("community_interest_scripture_source")
                    val lwc_scripture_access = listStatementResult.getInt("lwc_scripture_access")
                    val lwc_scripture_description = listStatementResult.getString("lwc_scripture_description")
                    val lwc_scripture_source = listStatementResult.getString("lwc_scripture_source")
                    val access_to_begin = listStatementResult.getInt("access_to_begin")
                    val access_to_begin_description = listStatementResult.getString("access_to_begin_description")
                    val access_to_begin_source = listStatementResult.getString("access_to_begin_source")
                    val suggested_strategies = listStatementResult.getString("suggested_strategies")
                    val comments = listStatementResult.getString("comments")
                    val prioritization = listStatementResult.getInt("prioritization")
                    val progress_bible = listStatementResult.getInt("progress_bible")
                    data.add(LanguageEx(id, createdAt, createdBy, modifiedAt, modifiedBy,lang_name,lang_code,location,first_lang_population,population,
                        egids_level,egids_value,least_reached_progress_jps_scale,least_reached_value,partner_interest,partner_interest_description,partner_interest_source,
                        multi_lang_leverage,multi_lang_leverage_description,multi_lang_leverage_source,community_interest,community_interest_description,community_interest_scripture_source,community_interest_value,community_interest_scripture_description,community_interest_source,lwc_scripture_access,lwc_scripture_description,lwc_scripture_source,access_to_begin,access_to_begin_description,
                        access_to_begin_source,suggested_strategies,comments,prioritization,progress_bible
                    ))
                }
            } catch (e: SQLException) {
                println("error while listing ${e.message}")
                return ReadLanguageExResponse(ErrorType.SQLReadError, null)
            }
        }
        return ReadLanguageExResponse(ErrorType.NoError, data)
    }
}