package com.seedcompany.cordtables.components.tables.sc.partnerships

import com.seedcompany.cordtables.components.tables.sc.partnerships.Delete as CommonDelete
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.partnerships.ScPartnershipsDeleteRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource

data class ScPartnershipsDeleteRequest(
    val id: Int,
    val token: String?,
)

data class ScPartnershipsDeleteResponse(
    val error: ErrorType,
    val id: Int?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScPartnershipsDelete")
class Delete(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("sc/partnerships/delete")
    @ResponseBody
    fun deleteHandler(@RequestBody req: ScPartnershipsDeleteRequest): ScPartnershipsDeleteResponse {

        if (req.token == null) return ScPartnershipsDeleteResponse(ErrorType.TokenNotFound, null)
        if(!util.userHasDeletePermission(req.token, "sc.partnerships"))
            return ScPartnershipsDeleteResponse(ErrorType.DoesNotHaveDeletePermission, null)

        println("req: $req")
        var deletedLocationExId: Int? = null

        this.ds.connection.use { conn ->
            try {

                val deleteStatement = conn.prepareCall(
                    "delete from sc.partnerships where id = ? returning id"
                )
                deleteStatement.setInt(1, req.id)

                deleteStatement.setInt(1,req.id)


                val deleteStatementResult = deleteStatement.executeQuery()

                if (deleteStatementResult.next()) {
                    deletedLocationExId  = deleteStatementResult.getInt("id")
                }
            }
            catch (e:SQLException ){
                println(e.message)

                return ScPartnershipsDeleteResponse(ErrorType.SQLDeleteError, null)
            }
        }

        return ScPartnershipsDeleteResponse(ErrorType.NoError,deletedLocationExId)
    }
}
