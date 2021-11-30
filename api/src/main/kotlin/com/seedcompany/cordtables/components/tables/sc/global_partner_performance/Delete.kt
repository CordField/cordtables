package com.seedcompany.cordtables.components.tables.sc.global_partner_performance

import com.seedcompany.cordtables.components.tables.sc.global_partner_performance.Delete as CommonDelete
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.global_partner_performance.ScGlobalPartnerPerformanceDeleteRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource

data class ScGlobalPartnerPerformanceDeleteRequest(
    val id: Int,
    val token: String?,
)

data class ScGlobalPartnerPerformanceDeleteResponse(
    val error: ErrorType,
    val id: Int?
)


@Controller("ScGlobalPartnerPerformanceDelete")
class Delete(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("sc-global-partner-performance/delete")
    @ResponseBody
    fun deleteHandler(@RequestBody req: ScGlobalPartnerPerformanceDeleteRequest): ScGlobalPartnerPerformanceDeleteResponse {

        if (req.token == null) return ScGlobalPartnerPerformanceDeleteResponse(ErrorType.TokenNotFound, null)
        if(!util.userHasDeletePermission(req.token, "sc.global_partner_performance"))
            return ScGlobalPartnerPerformanceDeleteResponse(ErrorType.DoesNotHaveDeletePermission, null)

        println("req: $req")
        var deletedLocationExId: Int? = null

        this.ds.connection.use { conn ->
            try {

                val deleteStatement = conn.prepareCall(
                    "delete from sc.global_partner_performance where id = ? returning id"
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

                return ScGlobalPartnerPerformanceDeleteResponse(ErrorType.SQLDeleteError, null)
            }
        }

        return ScGlobalPartnerPerformanceDeleteResponse(ErrorType.NoError,deletedLocationExId)
    }
}