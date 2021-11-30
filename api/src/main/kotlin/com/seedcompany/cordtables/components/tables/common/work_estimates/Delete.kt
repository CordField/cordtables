package com.seedcompany.cordtables.components.tables.common.work_estimates

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

data class CommonWorkEstimateDeleteRequest(
    val id: Int,
    val token: String?,
)

data class CommonWorkEstimateDeleteResponse(
    val error: ErrorType,
    val id: Int?
)


@Controller("CommonWorkEstimatesDelete")
class Delete(
    @Autowired
    val util: Utility,
    @Autowired
    val ds: DataSource,
) {
    @PostMapping("common-work-estimates/delete")
    @ResponseBody
    fun deleteHandler(@RequestBody req: CommonWorkEstimateDeleteRequest): CommonWorkEstimateDeleteResponse {

        if (req.token == null) return CommonWorkEstimateDeleteResponse(ErrorType.TokenNotFound, null)
        if(!util.userHasDeletePermission(req.token, "common.work_estimates"))
            return CommonWorkEstimateDeleteResponse(ErrorType.DoesNotHaveDeletePermission, null)

        var deletedTicketId: Int? = null

        this.ds.connection.use { conn ->
            try {

                val deleteStatement = conn.prepareCall(
                    "delete from common.work_estimates where id = ? returning id"
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

                return CommonWorkEstimateDeleteResponse(ErrorType.SQLDeleteError, null)
            }
        }
        return CommonWorkEstimateDeleteResponse(ErrorType.NoError,deletedTicketId)
    }
}