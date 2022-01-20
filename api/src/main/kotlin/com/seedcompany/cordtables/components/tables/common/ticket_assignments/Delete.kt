package com.seedcompany.cordtables.components.tables.common.ticket_assignments

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

data class CommonTicketAssignmentDeleteRequest(
        val id: String,
        val token: String?,
)

data class CommonTicketAssignmentDeleteResponse(
        val error: ErrorType,
        val id: String?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonTicketAssignmentsDelete")
class Delete(
        @Autowired
        val util: Utility,
        @Autowired
        val ds: DataSource,
) {
    @PostMapping("common/ticket-assignments/delete")
    @ResponseBody
    fun deleteHandler(@RequestBody req: CommonTicketAssignmentDeleteRequest): CommonTicketAssignmentDeleteResponse {

        if (req.token == null) return CommonTicketAssignmentDeleteResponse(ErrorType.TokenNotFound, null)
        if(!util.userHasDeletePermission(req.token, "common.ticket_assignments"))
            return CommonTicketAssignmentDeleteResponse(ErrorType.DoesNotHaveDeletePermission, null)

        var deletedTicketId: String? = null

        this.ds.connection.use { conn ->
            try {

                val deleteStatement = conn.prepareCall(
                        "delete from common.ticket_assignments where id = ? returning id"
                )
                deleteStatement.setString(1, req.id)

                deleteStatement.setString(1,req.id)


                val deleteStatementResult = deleteStatement.executeQuery()

                if (deleteStatementResult.next()) {
                    deletedTicketId = deleteStatementResult.getString("id")
                }
            }
            catch (e:SQLException ){
                println(e.message)

                return CommonTicketAssignmentDeleteResponse(ErrorType.SQLDeleteError, null)
            }
        }
        return CommonTicketAssignmentDeleteResponse(ErrorType.NoError,deletedTicketId)
    }
}
