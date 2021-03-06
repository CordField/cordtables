package com.seedcompany.cordtables.components.tables.sc.global_partner_performance

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.global_partner_performance.ScGlobalPartnerPerformanceReadRequest
import com.seedcompany.cordtables.components.tables.sc.global_partner_performance.ScGlobalPartnerPerformanceUpdateRequest
import com.seedcompany.cordtables.components.tables.sc.global_partner_performance.globalPartnerPerformance
import com.seedcompany.cordtables.components.tables.sc.global_partner_performance.globalPartnerPerformanceInput
import com.seedcompany.cordtables.components.tables.sc.locations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScGlobalPartnerPerformanceUpdateReadRequest(
    val token: String?,
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class ScGlobalPartnerPerformanceUpdateReadResponse(
    val error: ErrorType,
    val globalPartnerPerformance: globalPartnerPerformance? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScGlobalPartnerPerformanceUpdateRead")
class UpdateRead(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val update: Update,

    @Autowired
    val read: Read,
) {
    @PostMapping("sc/global-partner-performance/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: ScGlobalPartnerPerformanceUpdateReadRequest): ScGlobalPartnerPerformanceUpdateReadResponse {

        val updateResponse = update.updateHandler(
            ScGlobalPartnerPerformanceUpdateRequest(
                token = req.token,
                column = req.column,
                id = req.id,
                value = req.value,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return ScGlobalPartnerPerformanceUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
            ScGlobalPartnerPerformanceReadRequest(
                token = req.token,
                id = req.id!!
            )
        )

        return ScGlobalPartnerPerformanceUpdateReadResponse(error = readResponse.error, readResponse.globalPartnerPerformance)
    }
}
