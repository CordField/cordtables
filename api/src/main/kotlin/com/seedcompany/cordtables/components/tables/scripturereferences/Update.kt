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
import kotlin.reflect.full.memberProperties

data class UpdatableScriptureReferenceFields(
    val book_start: String?,
    var book_end: String?,
    var chapter_start: Int?,
    var chapter_end: Int?,
    var verse_start: Int?,
    var verse_end: Int?,
)

data class ScriptureReferenceUpdateRequest(
    val token: String? = null,
    val id: Int? = null,
    val updatedFields: UpdatableScriptureReferenceFields
)

data class ScriptureReferenceUpdateResponse(
    val error: ErrorType,
    var data: ScriptureReference?,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("ScriptureReferenceUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {

    @PostMapping("table/common-scripture-references/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: ScriptureReferenceUpdateRequest): ScriptureReferenceUpdateResponse {

        if (req.token == null) return ScriptureReferenceUpdateResponse(ErrorType.TokenNotFound, null)
        if (!util.isAdmin(req.token)) return ScriptureReferenceUpdateResponse(ErrorType.AdminOnly, null)

        if (req.id == null) return ScriptureReferenceUpdateResponse(ErrorType.MissingId, null)
        var updatedScriptureReference: ScriptureReference? = null

        try {
            this.ds.connection.use { conn ->
                val reqValues: MutableList<Any> = mutableListOf()
                var updateSql = "update common.scripture_reference set"
                for (prop in UpdatableScriptureReferenceFields::class.memberProperties) {
                    val propValue = prop.get(req.updatedFields)
                    println("$propValue ${prop.name}")
                    if (propValue != null) {
                        updateSql = "$updateSql ${prop.name} = ?,"
                        reqValues.add(propValue)
                    }
                }
                updateSql = "$updateSql where id = ? returning *"
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

                updateStatement.setInt(counter+2, req.id)
                val updateStatementResult = updateStatement.executeQuery()

                if (updateStatementResult.next()) {
                    val id = updateStatement.getInt("id")
                    val bookStart = updateStatement.getString("book_start")
                    val bookEnd = updateStatement.getString("book_end")
                    val chapterStart = updateStatement.getInt("chapter_start")
                    val chapterEnd = updateStatement.getInt("chapter_end")
                    val verseStart = updateStatement.getInt("verse_start")
                    val verseEnd = updateStatement.getInt("verse_end")
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
        }
        catch (e: SQLException) {
            println(e.message)
            return ScriptureReferenceUpdateResponse(ErrorType.SQLUpdateError, null)
        }

        return ScriptureReferenceUpdateResponse(ErrorType.NoError, updatedScriptureReference)
    }
}