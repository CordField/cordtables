package com.seedcompany.cordtables.components.tables.common.work_records

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

data class CommonWorkRecordsDeleteRequest(
    val id: Int,
    val token: String?,
)

data class CommonWorkRecordsDeleteResponse(
    val error: ErrorType,
    val id: Int?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("CommonWorkRecordsDelete")
class Delete(
    @Autowired
    val util: Utility,
    @Autowired
    val ds: DataSource,
) {
    @PostMapping("common-work-records/delete")
    @ResponseBody
    fun deleteHandler(@RequestBody req: CommonWorkRecordsDeleteRequest): CommonWorkRecordsDeleteResponse {

        if (req.token == null) return CommonWorkRecordsDeleteResponse(ErrorType.TokenNotFound, null)
        if(!util.userHasDeletePermission(req.token, "common.ticket_assignments"))
            return CommonWorkRecordsDeleteResponse(ErrorType.DoesNotHaveDeletePermission, null)

        var deletedTicketId: Int? = null

        this.ds.connection.use { conn ->
            try {

                val deleteStatement = conn.prepareCall(
                    "delete from common.work_records where id = ? returning id"
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

                return CommonWorkRecordsDeleteResponse(ErrorType.SQLDeleteError, null)
            }
        }
        return CommonWorkRecordsDeleteResponse(ErrorType.NoError,deletedTicketId)
    }
}