package com.seedcompany.cordtables.components.tables.sc.global_partner_performance

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.global_partner_performance.*
import com.seedcompany.cordtables.components.tables.sc.global_partner_performance.ScGlobalPartnerPerformanceCreateRequest
import com.seedcompany.cordtables.components.tables.sc.global_partner_performance.Create
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScGlobalPartnerPerformanceCreateReadRequest(
    val token: String? = null,
    val globalPartnerPerformance: globalPartnerPerformanceInput,
)

data class ScGlobalPartnerPerformanceCreateReadResponse(
    val error: ErrorType,
    val globalPartnerPerformance: globalPartnerPerformance? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScGlobalPartnerPerformanceCreateRead")
class CreateRead(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val create: Create,

    @Autowired
    val read: Read,
) {
    @PostMapping("sc-global-partner-performance/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: ScGlobalPartnerPerformanceCreateReadRequest): ScGlobalPartnerPerformanceCreateReadResponse {

        val createResponse = create.createHandler(
            ScGlobalPartnerPerformanceCreateRequest(
                token = req.token,
                globalPartnerPerformance = req.globalPartnerPerformance
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return ScGlobalPartnerPerformanceCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            ScGlobalPartnerPerformanceReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return ScGlobalPartnerPerformanceCreateReadResponse(error = readResponse.error, globalPartnerPerformance = readResponse.globalPartnerPerformance)
    }
}