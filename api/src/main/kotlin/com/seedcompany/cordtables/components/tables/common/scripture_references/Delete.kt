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

data class ScriptureReferenceDeleteRequest(
    val token: String? = null,
    val id: Int? = null,
)

data class ScriptureReferenceDeleteResponse(
    val error: ErrorType,
)


@Controller("ScriptureReferenceDelete")
class Delete(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {

    @PostMapping("table/common-scripture-references/delete")
    @ResponseBody
    fun deleteHandler(@RequestBody req: ScriptureReferenceDeleteRequest): ScriptureReferenceDeleteResponse {

        if (req.token == null) return ScriptureReferenceDeleteResponse(ErrorType.TokenNotFound)
        if (!util.isAdmin(req.token)) return ScriptureReferenceDeleteResponse(ErrorType.AdminOnly)

        if (req.id == null) return ScriptureReferenceDeleteResponse(ErrorType.MissingId)

        this.ds.connection.use { conn ->
            try {
                //language=SQL
                val deleteStatement = conn.prepareStatement(
                        """
                    delete from common.scripture_references where id = ?;
                """.trimIndent()
                )

                deleteStatement.setInt(1, req.id)

                deleteStatement.execute()
            }
            catch (e: SQLException){
                println(e.message)
                return ScriptureReferenceDeleteResponse(ErrorType.SQLDeleteError)
            }
        }
        return ScriptureReferenceDeleteResponse(ErrorType.NoError)
    }

}