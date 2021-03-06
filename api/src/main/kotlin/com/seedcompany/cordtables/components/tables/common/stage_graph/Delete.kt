package com.seedcompany.cordtables.components.tables.common.stage_graph

import com.seedcompany.cordtables.components.tables.common.stage_graph.Delete as CommonDelete
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.stage_graph.CommonStageGraphDeleteRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource

data class CommonStageGraphDeleteRequest(
    val id: String,
    val token: String?,
)

data class CommonStageGraphDeleteResponse(
    val error: ErrorType,
    val id: String?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonStageGraphDelete")
class Delete(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("common/stage-graph/delete")
    @ResponseBody
    fun deleteHandler(@RequestBody req: CommonStageGraphDeleteRequest): CommonStageGraphDeleteResponse {

        if (req.token == null) return CommonStageGraphDeleteResponse(ErrorType.TokenNotFound, null)
        if(!util.userHasDeletePermission(req.token, "common.stage_graph"))
            return CommonStageGraphDeleteResponse(ErrorType.DoesNotHaveDeletePermission, null)

        println("req: $req")
        var deletedLocationExId: String? = null

        this.ds.connection.use { conn ->
            try {

                val deleteStatement = conn.prepareCall(
                    "delete from common.stage_graph where id = ? returning id"
                )
                deleteStatement.setString(1, req.id)

                deleteStatement.setString(1,req.id)


                val deleteStatementResult = deleteStatement.executeQuery()

                if (deleteStatementResult.next()) {
                    deletedLocationExId  = deleteStatementResult.getString("id")
                }
            }
            catch (e:SQLException ){
                println(e.message)

                return CommonStageGraphDeleteResponse(ErrorType.SQLDeleteError, null)
            }
        }

        return CommonStageGraphDeleteResponse(ErrorType.NoError,deletedLocationExId)
    }
}
