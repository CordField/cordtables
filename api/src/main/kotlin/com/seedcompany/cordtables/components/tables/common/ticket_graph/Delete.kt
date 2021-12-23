package com.seedcompany.cordtables.components.tables.common.ticket_graph

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

data class CommonTicketGraphDeleteRequest(
        val id: String,
        val token: String?,
)

data class CommonTicketGraphDeleteResponse(
        val error: ErrorType,
        val id: String?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonTicketGraphDelete")
class Delete(
        @Autowired
        val util: Utility,
        @Autowired
        val ds: DataSource,
) {
    @PostMapping("common/ticket-graph/delete")
    @ResponseBody
    fun deleteHandler(@RequestBody req: CommonTicketGraphDeleteRequest): CommonTicketGraphDeleteResponse {

        if (req.token == null) return CommonTicketGraphDeleteResponse(ErrorType.TokenNotFound, null)
        if(!util.userHasDeletePermission(req.token, "common.ticket_graph"))
            return CommonTicketGraphDeleteResponse(ErrorType.DoesNotHaveDeletePermission, null)

        var deletedTicketId: String? = null

        this.ds.connection.use { conn ->
            try {

                val deleteStatement = conn.prepareCall(
                        "delete from common.ticket_graph where id = ? returning id"
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

                return CommonTicketGraphDeleteResponse(ErrorType.SQLDeleteError, null)
            }
        }
        return CommonTicketGraphDeleteResponse(ErrorType.NoError,deletedTicketId)
    }
}
