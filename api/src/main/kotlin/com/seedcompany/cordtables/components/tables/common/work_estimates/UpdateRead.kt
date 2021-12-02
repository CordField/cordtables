package com.seedcompany.cordtables.components.tables.common.work_estimates

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonWorkEstimateUpdateReadRequest(
    val token: String?,
    val id: Int? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class CommonWorkEstimateUpdateReadResponse(
    val error: ErrorType,
    val work_estimate: CommonWorkEstimates? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonWorkEstimatesUpdateRead")
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
    @PostMapping("common-work-estimates/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: CommonWorkEstimateUpdateReadRequest): CommonWorkEstimateUpdateReadResponse {

        val updateResponse = update.updateHandler(
            CommonWorkEstimateUpdateRequest(
                token = req.token,
                column = req.column,
                id = req.id,
                value = req.value,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return CommonWorkEstimateUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
            CommonWorkEstimateReadRequest(
                token = req.token,
                id = req.id!!
            )
        )

        return CommonWorkEstimateUpdateReadResponse(error = readResponse.error, readResponse.work_estimate)
    }
}
