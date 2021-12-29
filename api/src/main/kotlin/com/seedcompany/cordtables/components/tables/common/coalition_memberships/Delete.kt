package com.seedcompany.cordtables.components.tables.common.coalition_memberships

import com.seedcompany.cordtables.components.tables.common.coalition_memberships.Delete as CommonDelete
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.coalition_memberships.CommonCoalitionMembershipsDeleteRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource

data class CommonCoalitionMembershipsDeleteRequest(
    val id: Int,
    val token: String?,
)

data class CommonCoalitionMembershipsDeleteResponse(
    val error: ErrorType,
    val id: Int?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonCoalitionMembershipsDelete")
class Delete(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("common/coalition-memberships/delete")
    @ResponseBody
    fun deleteHandler(@RequestBody req: CommonCoalitionMembershipsDeleteRequest): CommonCoalitionMembershipsDeleteResponse {

        if (req.token == null) return CommonCoalitionMembershipsDeleteResponse(ErrorType.TokenNotFound, null)
        if(!util.userHasDeletePermission(req.token, "common.coalition_memberships"))
            return CommonCoalitionMembershipsDeleteResponse(ErrorType.DoesNotHaveDeletePermission, null)

        println("req: $req")
        var deletedLocationExId: Int? = null

        this.ds.connection.use { conn ->
            try {

                val deleteStatement = conn.prepareCall(
                    "delete from common.coalition_memberships where id = ? returning id"
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

                return CommonCoalitionMembershipsDeleteResponse(ErrorType.SQLDeleteError, null)
            }
        }

        return CommonCoalitionMembershipsDeleteResponse(ErrorType.NoError,deletedLocationExId)
    }
}
