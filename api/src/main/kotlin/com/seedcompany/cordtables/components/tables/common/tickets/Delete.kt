package com.seedcompany.cordtables.components.tables.common.tickets

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

data class CommonTicketsDeleteRequest(
        val id: Int,
        val token: String?,
)

data class CommonTicketsDeleteResponse(
        val error: ErrorType,
        val id: Int?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonTicketsDelete")
class Delete(
        @Autowired
        val util: Utility,
        @Autowired
        val ds: DataSource,
) {
    @PostMapping("common/tickets/delete")
    @ResponseBody
    fun deleteHandler(@RequestBody req: CommonTicketsDeleteRequest): CommonTicketsDeleteResponse {

        if (req.token == null) return CommonTicketsDeleteResponse(ErrorType.TokenNotFound, null)
        if(!util.userHasDeletePermission(req.token, "common.tickets"))
            return CommonTicketsDeleteResponse(ErrorType.DoesNotHaveDeletePermission, null)

        var deletedTicketId: Int? = null

        this.ds.connection.use { conn ->
            try {

                val deleteStatement = conn.prepareCall(
                        "delete from common.tickets where id = ? returning id"
                )
                deleteStatement.setInt(1, req.id)

                deleteStatement.setInt(1,req.id)


                val deleteStatementResult = deleteStatement.executeQuery()

                if (deleteStatementResult.next()) {
                    deletedTicketId = deleteStatementResult.getInt("id")
                }
            }
            catch (e:SQLException ){
                println(e.message)

                return CommonTicketsDeleteResponse(ErrorType.SQLDeleteError, null)
            }
        }
        return CommonTicketsDeleteResponse(ErrorType.NoError,deletedTicketId)
    }
}
