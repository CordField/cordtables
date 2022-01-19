package com.seedcompany.cordtables.components.tables.sc.language_engagements

import com.seedcompany.cordtables.components.tables.sc.language_engagements.Delete as CommonDelete
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.language_engagements.ScLanguageEngagementsDeleteRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource

data class ScLanguageEngagementsDeleteRequest(
    val id: String,
    val token: String?,
)

data class ScLanguageEngagementsDeleteResponse(
    val error: ErrorType,
    val id: String?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScLanguageEngagementsDelete")
class Delete(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("sc/language-engagements/delete")
    @ResponseBody
    fun deleteHandler(@RequestBody req: ScLanguageEngagementsDeleteRequest): ScLanguageEngagementsDeleteResponse {

        if (req.token == null) return ScLanguageEngagementsDeleteResponse(ErrorType.TokenNotFound, null)
        if(!util.userHasDeletePermission(req.token, "sc.language_engagements"))
            return ScLanguageEngagementsDeleteResponse(ErrorType.DoesNotHaveDeletePermission, null)

        println("req: $req")
        var deletedLocationExId: String? = null

        this.ds.connection.use { conn ->
            try {

                val deleteStatement = conn.prepareCall(
                    "delete from sc.language_engagements where id = ? returning id"
                )
                deleteStatement.setString(1, req.id)

                val deleteStatementResult = deleteStatement.executeQuery()

                if (deleteStatementResult.next()) {
                    deletedLocationExId  = deleteStatementResult.getString("id")
                }
            }
            catch (e:SQLException ){
                println(e.message)

                return ScLanguageEngagementsDeleteResponse(ErrorType.SQLDeleteError, null)
            }
        }

        return ScLanguageEngagementsDeleteResponse(ErrorType.NoError,deletedLocationExId)
    }
}
