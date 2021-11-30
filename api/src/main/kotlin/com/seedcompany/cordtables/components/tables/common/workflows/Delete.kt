package com.seedcompany.cordtables.components.tables.common.workflows

import com.seedcompany.cordtables.components.tables.common.workflows.Delete as CommonDelete
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.workflows.CommonWorkflowsDeleteRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource

data class CommonWorkflowsDeleteRequest(
    val id: Int,
    val token: String?,
)

data class CommonWorkflowsDeleteResponse(
    val error: ErrorType,
    val id: Int?
)


@Controller("CommonWorkflowsDelete")
class Delete(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("common-workflows/delete")
    @ResponseBody
    fun deleteHandler(@RequestBody req: CommonWorkflowsDeleteRequest): CommonWorkflowsDeleteResponse {

        if (req.token == null) return CommonWorkflowsDeleteResponse(ErrorType.TokenNotFound, null)
        if(!util.userHasDeletePermission(req.token, "common.workflows"))
            return CommonWorkflowsDeleteResponse(ErrorType.DoesNotHaveDeletePermission, null)

        println("req: $req")
        var deletedLocationExId: Int? = null

        this.ds.connection.use { conn ->
            try {

                val deleteStatement = conn.prepareCall(
                    "delete from common.workflows where id = ? returning id"
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

                return CommonWorkflowsDeleteResponse(ErrorType.SQLDeleteError, null)
            }
        }

        return CommonWorkflowsDeleteResponse(ErrorType.NoError,deletedLocationExId)
    }
}