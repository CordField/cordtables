package com.seedcompany.cordtables.components.tables.sc.person_unavailabilities

import com.seedcompany.cordtables.components.tables.sc.person_unavailabilities.Delete as CommonDelete
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.person_unavailabilities.ScPersonUnavailabilitiesDeleteRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource

data class ScPersonUnavailabilitiesDeleteRequest(
    val id: String,
    val token: String?,
)

data class ScPersonUnavailabilitiesDeleteResponse(
    val error: ErrorType,
    val id: String?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScPersonUnavailabilitiesDelete")
class Delete(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("sc/person-unavailabilities/delete")
    @ResponseBody
    fun deleteHandler(@RequestBody req: ScPersonUnavailabilitiesDeleteRequest): ScPersonUnavailabilitiesDeleteResponse {

        if (req.token == null) return ScPersonUnavailabilitiesDeleteResponse(ErrorType.TokenNotFound, null)
        if(!util.userHasDeletePermission(req.token, "sc.person_unavailabilities"))
            return ScPersonUnavailabilitiesDeleteResponse(ErrorType.DoesNotHaveDeletePermission, null)

        println("req: $req")
        var deletedLocationExId: String? = null

        this.ds.connection.use { conn ->
            try {

                val deleteStatement = conn.prepareCall(
                    "delete from sc.person_unavailabilities where id = ? returning id"
                )
                deleteStatement.setString(1, req.id)
                val deleteStatementResult = deleteStatement.executeQuery()

                if (deleteStatementResult.next()) {
                    deletedLocationExId  = deleteStatementResult.getString("id")
                }
            }
            catch (e:SQLException ){
                println(e.message)

                return ScPersonUnavailabilitiesDeleteResponse(ErrorType.SQLDeleteError, null)
            }
        }

        return ScPersonUnavailabilitiesDeleteResponse(ErrorType.NoError,deletedLocationExId)
    }
}
