package com.seedcompany.cordspringstencil.components.tables.languageex

import com.seedcompany.cordspringstencil.common.ErrorType
import com.seedcompany.cordspringstencil.common.Utility
import com.seedcompany.cordspringstencil.components.tables.groups.GroupUpdateResponse
import com.seedcompany.cordspringstencil.components.user.CreateGlobalRoleRequest
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
    val created_at:String?,
    val created_by:Int?,
    val modified_at:String?,
    val modified_by:Int?,
    val lang_name:String?,
    val lang_code:String?,
    val location:String?,
    val first_lang_population:Int?,
    val population:Int?,
    val egids_level:Int?,
    val egids_value:Int?,
    val least_reached_progress_jps_scale:Int?,
    val least_reached_value:Int?,
    val partner_interest:Int?,
    val partner_interest_description:String?,
    val partner_interest_source:String?,
    val multi_lang_leverage:Int?,
    val multi_lang_leverage_description:String?,
    val multi_lang_leverage_source:String?,
    val community_interest:Int?,
    val community_interest_description:String?,
    val community_interest_source:String?,
    val community_interest_value:Int?,
    val community_interest_scripture_description:String?,
    val community_interest_scripture_source:String?,
    val lwc_scripture_access:Int?,
    val lwc_scripture_description:String?,
    val lwc_scripture_source:String?,
    val access_to_begin:Int?,
    val access_to_begin_description:String?,
    val access_to_begin_source:String?,
    val suggested_strategies:String?,
    val comments:String?,
    val prioritization:Int?,
    val progress_bible:Int?
)

data class ReadLanguageExResponse(
    val error: ErrorType,
    val data: MutableList<LanguageEx>?
)
data class ReadLanguageExRequest(
    val token: String?
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
                        "    from public.group_row_access as a  \n" +
                        "\tinner join public.group_memberships as b \n" +
                        "\ton a.group_id = b.group_id \n" +
                        "\tinner join public.tokens as c \n" +
                        "\ton b.person = c.person\n" +
                        "\twhere a.table_name = 'sc.languages_ex'\n" +
                        "\tand c.token = ?\n" +
                        "), \n" +
                        "column_level_access as \n" +
                        "(\n" +
                        "    select  column_name \n" +
                        "    from public.global_role_column_grants a \n" +
                        "    inner join public.global_role_memberships b \n" +
                        "    on a.global_role = b.global_role \n" +
                        "    inner join public.tokens c \n" +
                        "    on b.person = c.person \n" +
                        "    where a.table_name = 'sc.languages_ex'\n" +
                        "\tand c.token = ?\n" +
                        ")\n" +
                        "select \n" +
                        "case when 'id' in (select column_name from column_level_access) then id else null end as id,\n" +
                        "case when 'created_at' in (select column_name from column_level_access) then created_at else null end as created_at,\n" +
                        "case when 'created_by' in (select column_name from column_level_access) then created_by else null end as created_by,\n" +
                        "case when 'modified_at' in (select column_name from column_level_access) then modified_at else null end as modified_at,\n" +
                        "case when 'modified_by' in (select column_name from column_level_access) then modified_by else null end as modified_by,\n" +
                        "case when 'lang_name' in (select column_name from column_level_access) then lang_name else null end as lang_name,\n" +
                        "case when 'lang_code' in (select column_name from column_level_access) then lang_code else null end as lang_code,\n" +
                        "case when 'location' in (select column_name from column_level_access) then location else null end as location,\n" +
                        "case when 'first_lang_population' in (select column_name from column_level_access) then first_lang_population else null end as first_lang_population,\n" +
                        "case when 'population' in (select column_name from column_level_access) then population else null end as population,\n" +
                        "case when 'egids_level' in (select column_name from column_level_access) then egids_level else null end as egids_level,\n" +
                        "case when 'egids_value' in (select column_name from column_level_access) then egids_value else null end as egids_value,\n" +
                        "case when 'least_reached_progress_jps_scale' in (select column_name from column_level_access) then least_reached_progress_jps_scale else null end as least_reached_progress_jps_scale,\n" +
                        "case when 'least_reached_value' in (select column_name from column_level_access) then least_reached_value else null end as least_reached_value,\n" +
                        "case when 'partner_interest' in (select column_name from column_level_access) then partner_interest else null end as partner_interest,\n" +
                        "case when 'partner_interest_description' in (select column_name from column_level_access) then partner_interest_description else null end as partner_interest_description,\n" +
                        "case when 'partner_interest_source' in (select column_name from column_level_access) then partner_interest_source else null end as partner_interest_source,\n" +
                        "case when 'multi_lang_leverage' in (select column_name from column_level_access) then multi_lang_leverage else null end as multi_lang_leverage,\n" +
                        "case when 'multi_lang_leverage_description' in (select column_name from column_level_access) then multi_lang_leverage_description else null end as multi_lang_leverage_description,\n" +
                        "case when 'multi_lang_leverage_source' in (select column_name from column_level_access) then multi_lang_leverage_source else null end as multi_lang_leverage_source,\n" +
                        "case when 'community_interest' in (select column_name from column_level_access) then community_interest else null end as community_interest,\n" +
                        "case when 'community_interest_description' in (select column_name from column_level_access) then community_interest_description else null end as community_interest_description,\n" +
                        "case when 'community_interest_source' in (select column_name from column_level_access) then community_interest_source else null end as community_interest_source,\n" +
                        "case when 'community_interest_value' in (select column_name from column_level_access) then community_interest_value else null end as community_interest_value,\n" +
                        "case when 'community_interest_scripture_description' in (select column_name from column_level_access) then community_interest_scripture_description else null end as community_interest_scripture_description,\n" +
                        "case when 'community_interest_scripture_source' in (select column_name from column_level_access) then community_interest_scripture_source else null end as community_interest_scripture_source,\n" +
                        "case when 'lwc_scripture_access' in (select column_name from column_level_access) then lwc_scripture_access else null end as lwc_scripture_access,\n" +
                        "case when 'lwc_scripture_description' in (select column_name from column_level_access) then lwc_scripture_description else null end as lwc_scripture_description,\n" +
                        "case when 'lwc_scripture_source' in (select column_name from column_level_access) then lwc_scripture_source else null end as lwc_scripture_source,\n" +
                        "case when 'access_to_begin' in (select column_name from column_level_access) then access_to_begin else null end as access_to_begin,\n" +
                        "case when 'access_to_begin_description' in (select column_name from column_level_access) then access_to_begin_description else null end as access_to_begin_description,\n" +
                        "case when 'access_to_begin_source' in (select column_name from column_level_access) then access_to_begin_source else null end as access_to_begin_source,\n" +
                        "case when 'suggested_strategies' in (select column_name from column_level_access) then suggested_strategies else null end as suggested_strategies,\n" +
                        "case when 'comments' in (select column_name from column_level_access) then comments else null end as comments,\n" +
                        "case when 'prioritization' in (select column_name from column_level_access) then prioritization else null end as prioritization,\n" +
                        "case when 'progress_bible' in (select column_name from column_level_access) then progress_bible else null end as progress_bible\n" +
                        "from sc.languages_ex \n" +
                        "where id in (select row from row_level_access);"
            )
            listStatement.setString(1, req.token)
            listStatement.setString(2,req.token)
            try {
                val listStatementResult = listStatement.executeQuery()
                while (listStatementResult.next()) {

                    val id = listStatementResult.getInt("id")
                    val created_at = listStatementResult.getString("created_at")

                    val created_by = listStatementResult.getInt("created_by")
                    val modified_at = listStatementResult.getString("modified_at")
                    val modified_by = listStatementResult.getInt("modified_by")
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
                    data.add(LanguageEx(id, created_at, created_by, modified_at, modified_by,lang_name,lang_code,location,first_lang_population,population,
                        egids_level,egids_value,least_reached_progress_jps_scale,least_reached_value,partner_interest,partner_interest_description,partner_interest_source,
                        multi_lang_leverage,multi_lang_leverage_description,multi_lang_leverage_source,community_interest,community_interest_description,community_interest_scripture_source,community_interest_value,community_interest_scripture_description,community_interest_source,lwc_scripture_access,lwc_scripture_description,lwc_scripture_source,access_to_begin,access_to_begin_description,
                        access_to_begin_source,suggested_strategies,comments,prioritization,progress_bible
                    ))
                }
            } catch (e: SQLException) {
                println("error while listing ${e.message}")
                return ReadLanguageExResponse(ErrorType.SQLReadError, mutableListOf())
            }
        }
        return ReadLanguageExResponse(ErrorType.NoError, data)
    }
}