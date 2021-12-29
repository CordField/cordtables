package com.seedcompany.cordtables.components.tables.sc.internship_engagements

import com.seedcompany.cordtables.components.tables.sc.internship_engagements.Delete as CommonDelete
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.internship_engagements.ScInternshipEngagementsDeleteRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource

data class ScInternshipEngagementsDeleteRequest(
    val id: String,
    val token: String?,
)

data class ScInternshipEngagementsDeleteResponse(
    val error: ErrorType,
    val id: String?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScInternshipEngagementsDelete")
class Delete(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("sc/internship-engagements/delete")
    @ResponseBody
    fun deleteHandler(@RequestBody req: ScInternshipEngagementsDeleteRequest): ScInternshipEngagementsDeleteResponse {

        if (req.token == null) return ScInternshipEngagementsDeleteResponse(ErrorType.TokenNotFound, null)
        if(!util.userHasDeletePermission(req.token, "sc.internship_engagements"))
            return ScInternshipEngagementsDeleteResponse(ErrorType.DoesNotHaveDeletePermission, null)

        println("req: $req")
        var deletedLocationExId: String? = null

        this.ds.connection.use { conn ->
            try {

                val deleteStatement = conn.prepareCall(
                    "delete from sc.internship_engagements where id = ?::uuid returning id"
                )
                deleteStatement.setString(1, req.id)

                val deleteStatementResult = deleteStatement.executeQuery()

                if (deleteStatementResult.next()) {
                    deletedLocationExId  = deleteStatementResult.getString("id")
                }
            }
            catch (e:SQLException ){
                println(e.message)

                return ScInternshipEngagementsDeleteResponse(ErrorType.SQLDeleteError, null)
            }
        }

        return ScInternshipEngagementsDeleteResponse(ErrorType.NoError,deletedLocationExId)
    }
}
