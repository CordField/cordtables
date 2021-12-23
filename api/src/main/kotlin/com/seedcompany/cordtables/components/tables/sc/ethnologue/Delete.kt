package com.seedcompany.cordtables.components.tables.sc.ethnologue

import com.seedcompany.cordtables.components.tables.sc.ethnologue.Delete as CommonDelete
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.ethnologue.ScEthnologueDeleteRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource

data class ScEthnologueDeleteRequest(
    val id: String,
    val token: String?,
)

data class ScEthnologueDeleteResponse(
    val error: ErrorType,
    val id: String?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScEthnologueDelete")
class Delete(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("sc-ethnologue/delete")
    @ResponseBody
    fun deleteHandler(@RequestBody req: ScEthnologueDeleteRequest): ScEthnologueDeleteResponse {

        if (req.token == null) return ScEthnologueDeleteResponse(ErrorType.TokenNotFound, null)
        if(!util.userHasDeletePermission(req.token, "sc.ethnologue"))
            return ScEthnologueDeleteResponse(ErrorType.DoesNotHaveDeletePermission, null)

        println("req: $req")
        var deletedLocationExId: String? = null

        this.ds.connection.use { conn ->
            try {

                val deleteStatement = conn.prepareCall(
                    "delete from sc.ethnologue where id = ? returning id"
                )
                deleteStatement.setString(1, req.id)

                deleteStatement.setString(1,req.id)


                val deleteStatementResult = deleteStatement.executeQuery()

                if (deleteStatementResult.next()) {
                    deletedLocationExId  = deleteStatementResult.getString("id")
                }
            }
            catch (e:SQLException ){
                println(e.message)

                return ScEthnologueDeleteResponse(ErrorType.SQLDeleteError, null)
            }
        }

        return ScEthnologueDeleteResponse(ErrorType.NoError,deletedLocationExId)
    }
}
