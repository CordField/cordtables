package com.seedcompany.cordtables.components.tables.common.scripture_references

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
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

data class ScriptureReferenceListRequest(
    val token: String? = null,
)

data class ScriptureReferenceListResponse(
    val error: ErrorType,
    val response: MutableList<ScriptureReference>?,
)

data class ScriptureReference(
    val id: Int?,
    val book_start: String?,
    val book_end: String?,
    val chapter_start: Int?,
    val chapter_end: Int?,
    val verse_start: Int?,
    val verse_end: Int?,
)


@Controller("ScriptureReferenceList")
class List(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("table/common-scripture-references/list")
    @ResponseBody
    fun listHandler(@RequestBody req: ScriptureReferenceListRequest): ScriptureReferenceListResponse {

        if (req.token == null) return ScriptureReferenceListResponse(ErrorType.TokenNotFound, null)
        if (!util.isAdmin(req.token)) return ScriptureReferenceListResponse(ErrorType.AdminOnly, null)

        var data: MutableList<ScriptureReference> = mutableListOf()

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)

        try {
            //language=SQL
            val listSQL = """
                with column_level_access as 
                (
                    select  column_name 
                    from admin.role_column_grants a 
                    inner join admin.role_memberships b 
                    on a.role = b.role 
                    inner join admin.tokens c 
                    on b.person = c.person 
                    where a.table_name = 'common.scripture_references'
                    and c.token = :token
                )
                select 
                case 
                    when 'id' in 
                        (select column_name from column_level_access) 
                    then id 
                    when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1)) 
                    then id 
                    else null 
                end as id,
                case 
                    when 'book_start' in (select column_name from column_level_access) 
                    then book_start
                    when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1)) 
                    then book_start
                    else null 
                end as book_start,
                case 
                when 'book_end' in (select column_name from column_level_access) 
                    then book_end
                    when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1)) 
                    then book_end
                    else null 
                end as book_end,
                case 
                    when 'chapter_start' in (select column_name from column_level_access) 
                    then chapter_start 
                    when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1)) 
                    then chapter_start
                    else null 
                end as chapter_start,
                case 
                    when 'chapter_end' in (select column_name from column_level_access) 
                    then chapter_end
                    when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1)) 
                    then chapter_end
                    else null 
                end as chapter_end,
                case 
                    when 'verse_start' in (select column_name from column_level_access) 
                    then verse_start 
                    when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1)) 
                    then verse_start
                    else null 
                end as verse_start,
                case 
                    when 'verse_end' in (select column_name from column_level_access) 
                    then verse_end 
                    when (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1)) 
                    then verse_end
                    else null 
                end as verse_end
                from common.scripture_references
                where (select exists( select id from admin.role_memberships where person = (select person from admin.tokens where token = :token) and role = 1));
            """.trimIndent()

            val jdbcResult = jdbcTemplate.queryForRowSet(listSQL, paramSource)
    
            while (jdbcResult.next()) {
    
                var id: Int? = jdbcResult.getInt("id")
                if (jdbcResult.wasNull()) id = null
    
                var bookStart: String? = jdbcResult.getString("book_start")
                if (jdbcResult.wasNull()) bookStart = null
    
                var bookEnd: String? = jdbcResult.getString("book_end")
                if (jdbcResult.wasNull()) bookEnd = null
    
                var chapterStart: Int? = jdbcResult.getInt("chapter_start")
                if (jdbcResult.wasNull()) chapterStart = null
    
                var chapterEnd: Int? = jdbcResult.getInt("chapter_end")
                if (jdbcResult.wasNull()) chapterEnd = null
    
                var verseStart: Int? = jdbcResult.getInt("verse_start")
                if (jdbcResult.wasNull()) verseStart = null
    
                var verseEnd: Int? = jdbcResult.getInt("verse_end")
                if (jdbcResult.wasNull()) verseEnd = null
    
                data.add(
                    ScriptureReference(
                        id,
                        bookStart,
                        bookEnd,
                        chapterStart,
                        chapterEnd,
                        verseStart,
                        verseEnd
                    )
                )
            }
        }
        catch(e: SQLException){
            println("error while listing ${e.message}")
            return ScriptureReferenceListResponse(ErrorType.SQLReadError, null)
        }

        return ScriptureReferenceListResponse(ErrorType.NoError, data)
    }
}