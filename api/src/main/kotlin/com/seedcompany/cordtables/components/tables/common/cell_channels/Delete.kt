package com.seedcompany.cordtables.components.tables.common.cell_channels

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

data class CommonCellChannelsDeleteRequest(
        val id: String,
        val token: String?,
)

data class CommonCellChannelsDeleteResponse(
        val error: ErrorType,
        val id: String?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonCellChannelsDelete")
class Delete(
        @Autowired
        val util: Utility,
        @Autowired
        val ds: DataSource,
) {
    @PostMapping("common/cell-channels/delete")
    @ResponseBody
    fun deleteHandler(@RequestBody req: CommonCellChannelsDeleteRequest): CommonCellChannelsDeleteResponse {

        if (req.token == null) return CommonCellChannelsDeleteResponse(ErrorType.TokenNotFound, null)
        if(!util.userHasDeletePermission(req.token, "common.cell-channels"))
            return CommonCellChannelsDeleteResponse(ErrorType.DoesNotHaveDeletePermission, null)

        var deletedTicketId: String? = null

        this.ds.connection.use { conn ->
            try {

                val deleteStatement = conn.prepareCall(
                        "delete from common.cell_channels where id = ?::uuid returning id"
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

                return CommonCellChannelsDeleteResponse(ErrorType.SQLDeleteError, null)
            }
        }
        return CommonCellChannelsDeleteResponse(ErrorType.NoError,deletedTicketId)
    }
}
