package com.seedcompany.cordtables.components.tables.scripturereferences

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

data class ScriptureReferenceListRequest(
    val token: String? = null,
)

data class ScriptureReferenceListResponse(
    val error: ErrorType,
    val response: MutableList<ScriptureReference>?,
)

data class ScriptureReference(
    val id: Int,
    val book_start: String,
    val book_end: String,
    val chapter_start: Int,
    val chapter_end: Int,
    val verse_start: Int,
    val verse_end: Int,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("ScriptureReferenceList")
class List(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {

    @PostMapping("table/common-scripture-references/list")
    @ResponseBody
    fun listHandler(@RequestBody req: ScriptureReferenceListRequest): ScriptureReferenceListResponse {

        if (req.token == null) return ScriptureReferenceListResponse(ErrorType.TokenNotFound, null)
        if (!util.isAdmin(req.token)) return ScriptureReferenceListResponse(ErrorType.AdminOnly, null)

        var data: MutableList<ScriptureReference> = mutableListOf()

        this.ds.connection.use { conn ->
            try {

                val listStatement = conn.prepareCall("""
                    select
                        id,
                        book_start,
                        book_end,
                        chapter_start,
                        chapter_end,
                        verse_start,
                        verse_end
                     from common.scripture_references
                    """.trimIndent()
                )
                val listStatementResult = listStatement.executeQuery()

                while (listStatementResult.next()) {
                    val id = listStatementResult.getInt("id")
                    val bookStart = listStatementResult.getString("book_start")
                    val bookEnd = listStatementResult.getString("book_end")
                    val chapterStart = listStatementResult.getInt("chapter_start")
                    val chapterEnd = listStatementResult.getInt("chapter_end")
                    val verseStart = listStatementResult.getInt("verse_start")
                    val verseEnd = listStatementResult.getInt("verse_end")
                    data.add(ScriptureReference(
                        id,
                        bookStart,
                        bookEnd,
                        chapterStart,
                        chapterEnd,
                        verseStart,
                        verseEnd)
                    )
                }
            }
            catch(e: SQLException){
                println("error while listing ${e.message}")
                return ScriptureReferenceListResponse(ErrorType.SQLReadError, null)
            }
        }

        return ScriptureReferenceListResponse(ErrorType.NoError, data)
    }
}