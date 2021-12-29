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
import kotlin.reflect.full.memberProperties

data class UpdatableScriptureReferenceFields(
    val book_start: String?,
    val book_end: String?,
    val chapter_start: Int?,
    val chapter_end: Int?,
    val verse_start: Int?,
    val verse_end: Int?,
)

data class ScriptureReferenceUpdateRequest(
    val token: String? = null,
    val id: Int? = null,
    val updatedFields: UpdatableScriptureReferenceFields
)

data class ScriptureReferenceUpdateResponse(
    val error: ErrorType,
    val response: ScriptureReference?,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScriptureReferenceUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {

    @PostMapping("common/scripture-references/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: ScriptureReferenceUpdateRequest): ScriptureReferenceUpdateResponse {

        if (req.token == null) return ScriptureReferenceUpdateResponse(ErrorType.TokenNotFound, null)
        if (!util.isAdmin(req.token)) return ScriptureReferenceUpdateResponse(ErrorType.AdminOnly, null)

        if (req.id == null) return ScriptureReferenceUpdateResponse(ErrorType.MissingId, null)
        var updatedScriptureReference: ScriptureReference? = null

        this.ds.connection.use { conn ->
            try {

                val reqValues: MutableList<Any> = mutableListOf()
                var updateSql = "update common.scripture_references set"
                var counter = 1
                for (prop in UpdatableScriptureReferenceFields::class.memberProperties) {
                    val propValue = prop.get(req.updatedFields)
                    if (propValue != null) {
                        if(counter > 1) updateSql = ", $updateSql"
                        if(prop.name == "book_start" || prop.name == "book_end") {
                            updateSql = "$updateSql ${prop.name} = ?::common.book_name"
                        } else {
                            updateSql = "$updateSql ${prop.name} = ?"
                        }
                        reqValues.add(propValue)
                        counter++
                    }
                }
                updateSql = "$updateSql where id = ? returning *"
                println(updateSql)
                val updateStatement = conn.prepareCall(
                    updateSql
                )
                counter = 1
                reqValues.forEach { value ->
                    when (value) {
                        is Int -> updateStatement.setInt(counter, value)
                        is String -> updateStatement.setString(counter, value)
                    }
                    counter++
                }
                println("counter+++++++++++++++=")
                println(counter)
                updateStatement.setInt(counter, req.id)
                val updateStatementResult = updateStatement.executeQuery()

                if (updateStatementResult.next()) {
                    val id = updateStatementResult.getInt("id")
                    val bookStart = updateStatementResult.getString("book_start")
                    val bookEnd = updateStatementResult.getString("book_end")
                    val chapterStart = updateStatementResult.getInt("chapter_start")
                    val chapterEnd = updateStatementResult.getInt("chapter_end")
                    val verseStart = updateStatementResult.getInt("verse_start")
                    val verseEnd = updateStatementResult.getInt("verse_end")
                    updatedScriptureReference = ScriptureReference(
                        id,
                        bookStart,
                        bookEnd,
                        chapterStart,
                        chapterEnd,
                        verseStart,
                        verseEnd
                    )

                    println("updated row's id: $id")
                }
            }
            catch (e: SQLException) {
                println(e.message)
                return ScriptureReferenceUpdateResponse(ErrorType.SQLUpdateError, null)
            }
        }

        return ScriptureReferenceUpdateResponse(ErrorType.NoError, updatedScriptureReference)
    }
}
