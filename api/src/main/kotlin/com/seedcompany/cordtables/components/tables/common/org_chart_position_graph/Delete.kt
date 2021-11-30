package com.seedcompany.cordtables.components.tables.common.org_chart_position_graph

import com.seedcompany.cordtables.components.tables.common.org_chart_position_graph.Delete as CommonDelete
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.org_chart_position_graph.CommonOrgChartPositionGraphDeleteRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource

data class CommonOrgChartPositionGraphDeleteRequest(
    val id: Int,
    val token: String?,
)

data class CommonOrgChartPositionGraphDeleteResponse(
    val error: ErrorType,
    val id: Int?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("CommonOrgChartPositionGraphDelete")
class Delete(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("common-org-chart-position-graph/delete")
    @ResponseBody
    fun deleteHandler(@RequestBody req: CommonOrgChartPositionGraphDeleteRequest): CommonOrgChartPositionGraphDeleteResponse {

        if (req.token == null) return CommonOrgChartPositionGraphDeleteResponse(ErrorType.TokenNotFound, null)
        if(!util.userHasDeletePermission(req.token, "common.org_chart_position_graph"))
            return CommonOrgChartPositionGraphDeleteResponse(ErrorType.DoesNotHaveDeletePermission, null)

        println("req: $req")
        var deletedLocationExId: Int? = null

        this.ds.connection.use { conn ->
            try {

                val deleteStatement = conn.prepareCall(
                    "delete from common.org_chart_position_graph where id = ? returning id"
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

                return CommonOrgChartPositionGraphDeleteResponse(ErrorType.SQLDeleteError, null)
            }
        }

        return CommonOrgChartPositionGraphDeleteResponse(ErrorType.NoError,deletedLocationExId)
    }
}