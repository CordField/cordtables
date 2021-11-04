package com.seedcompany.cordtables.components.tables.common.scripture_references

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

data class ScriptureReferenceCreateRequest(
    val token: String?,
    val book_start: String,
    val book_end: String,
    val chapter_start: Int,
    val chapter_end: Int,
    val verse_start: Int,
    val verse_end: Int,
)

data class ScriptureReferenceCreateResponse(
    val error: ErrorType,
    val response: ScriptureReference?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("ScriptureReferenceCreate")
class Create(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {

    @PostMapping("table/common-scripture-references/create")
    @ResponseBody
    fun createHandler(@RequestBody req: ScriptureReferenceCreateRequest): ScriptureReferenceCreateResponse {

        var insertedScriptureReference: ScriptureReference? = null

        if (req.token == null) return ScriptureReferenceCreateResponse(ErrorType.TokenNotFound, null)
        if (!util.isAdmin(req.token)) return ScriptureReferenceCreateResponse(ErrorType.AdminOnly, null)

        this.ds.connection.use { conn ->
            try {
                //language=SQL
                val statement = conn.prepareStatement(
                    """
                    insert into common.scripture_references(
                        book_start,
                        book_end,
                        chapter_start,
                        chapter_end,
                        verse_start,
                        verse_end ) 
                    values (?::common.book_name, ?::common.book_name, ?, ?, ?, ?)
                    returning id
                    """.trimIndent()
                )

                statement.setString(1, req.book_start)
                statement.setString(2, req.book_end)
                statement.setInt(3, req.chapter_start)
                statement.setInt(4, req.chapter_end)
                statement.setInt(5, req.verse_start)
                statement.setInt(6, req.verse_end)

                val insertStatementResult = statement.executeQuery()
                if (insertStatementResult.next()) {
                    val id = insertStatementResult.getInt("id")
                    insertedScriptureReference = ScriptureReference(
                        id,
                        req.book_start,
                        req.book_end,
                        req.chapter_start,
                        req.chapter_end,
                        req.verse_start,
                        req.verse_end
                    )
                    println("newly inserted id: $id")
                }
            }
            catch (e:SQLException ){
                println(e.message)
                return ScriptureReferenceCreateResponse(ErrorType.SQLInsertError, null)
            }
        }
        return ScriptureReferenceCreateResponse(ErrorType.NoError, insertedScriptureReference)
    }
}