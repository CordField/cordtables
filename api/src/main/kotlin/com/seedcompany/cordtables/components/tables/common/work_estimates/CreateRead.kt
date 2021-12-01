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

data class CommonWorkEstimateCreateReadRequest(
    val token: String? = null,
    val work_estimate: CommonWorkEstimateInput,
)

data class CommonWorkEstimateCreateReadResponse(
    val error: ErrorType,
    val work_estimate: CommonWorkEstimates? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonWorkEstimatesCreateRead")
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
    @PostMapping("common-work-estimates/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: CommonWorkEstimateCreateReadRequest): CommonWorkEstimateCreateReadResponse {

        val createResponse = create.createHandler(
            CommonWorkRecordCreateRequest(
                token = req.token,
                work_estimate = req.work_estimate
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return CommonWorkEstimateCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            CommonWorkEstimateReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return CommonWorkEstimateCreateReadResponse(error = readResponse.error, work_estimate = readResponse.work_estimate)
    }
}
