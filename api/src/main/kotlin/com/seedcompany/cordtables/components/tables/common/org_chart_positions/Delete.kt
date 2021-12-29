package com.seedcompany.cordtables.components.tables.common.org_chart_positions

import com.seedcompany.cordtables.components.tables.common.org_chart_positions.Delete as CommonDelete
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.org_chart_positions.CommonOrgChartPositionsDeleteRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource

data class CommonOrgChartPositionsDeleteRequest(
    val id: String,
    val token: String?,
)

data class CommonOrgChartPositionsDeleteResponse(
    val error: ErrorType,
    val id: String?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonOrgChartPositionsDelete")
class Delete(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("common-org-chart-positions/delete")
    @ResponseBody
    fun deleteHandler(@RequestBody req: CommonOrgChartPositionsDeleteRequest): CommonOrgChartPositionsDeleteResponse {

        if (req.token == null) return CommonOrgChartPositionsDeleteResponse(ErrorType.TokenNotFound, null)
        if(!util.userHasDeletePermission(req.token, "common.org_chart_positions"))
            return CommonOrgChartPositionsDeleteResponse(ErrorType.DoesNotHaveDeletePermission, null)

        println("req: $req")
        var deletedLocationExId: String? = null

        this.ds.connection.use { conn ->
            try {

                val deleteStatement = conn.prepareCall(
                    "delete from common.org_chart_positions where id = ?::uuid returning id"
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

                return CommonOrgChartPositionsDeleteResponse(ErrorType.SQLDeleteError, null)
            }
        }

        return CommonOrgChartPositionsDeleteResponse(ErrorType.NoError,deletedLocationExId)
    }
}
