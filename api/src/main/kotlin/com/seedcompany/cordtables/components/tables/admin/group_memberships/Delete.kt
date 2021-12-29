package com.seedcompany.cordtables.components.tables.admin.group_memberships

import com.seedcompany.cordtables.components.tables.admin.group_memberships.Delete as CommonDelete
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.admin.group_memberships.AdminGroupMembershipsDeleteRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource

data class AdminGroupMembershipsDeleteRequest(
    val id: Int,
    val token: String?,
)

data class AdminGroupMembershipsDeleteResponse(
    val error: ErrorType,
    val id: Int?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("AdminGroupMembershipsDelete")
class Delete(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("admin/group-memberships/delete")
    @ResponseBody
    fun deleteHandler(@RequestBody req: AdminGroupMembershipsDeleteRequest): AdminGroupMembershipsDeleteResponse {

        if (req.token == null) return AdminGroupMembershipsDeleteResponse(ErrorType.TokenNotFound, null)
        if(!util.userHasDeletePermission(req.token, "admin.group_memberships"))
            return AdminGroupMembershipsDeleteResponse(ErrorType.DoesNotHaveDeletePermission, null)

        println("req: $req")
        var deletedLocationExId: Int? = null

        this.ds.connection.use { conn ->
            try {

                val deleteStatement = conn.prepareCall(
                    "delete from admin.group_memberships where id = ? returning id"
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

                return AdminGroupMembershipsDeleteResponse(ErrorType.SQLDeleteError, null)
            }
        }

        return AdminGroupMembershipsDeleteResponse(ErrorType.NoError,deletedLocationExId)
    }
}
