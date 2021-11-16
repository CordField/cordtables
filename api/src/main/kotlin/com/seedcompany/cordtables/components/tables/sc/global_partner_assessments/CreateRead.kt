package com.seedcompany.cordtables.components.tables.sc.global_partner_assessments

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.global_partner_assessments.*
import com.seedcompany.cordtables.components.tables.sc.global_partner_assessments.ScGlobalPartnerAssessmentsCreateRequest
import com.seedcompany.cordtables.components.tables.sc.global_partner_assessments.Create
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScGlobalPartnerAssessmentsCreateReadRequest(
    val token: String? = null,
    val globalPartnerAssessment: globalPartnerAssessmentInput,
)

data class ScGlobalPartnerAssessmentsCreateReadResponse(
    val error: ErrorType,
    val globalPartnerAssessment: globalPartnerAssessment? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("ScGlobalPartnerAssessmentsCreateRead")
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
    @PostMapping("sc-global-partner-assessments/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: ScGlobalPartnerAssessmentsCreateReadRequest): ScGlobalPartnerAssessmentsCreateReadResponse {

        val createResponse = create.createHandler(
            ScGlobalPartnerAssessmentsCreateRequest(
                token = req.token,
                globalPartnerAssessment = req.globalPartnerAssessment
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return ScGlobalPartnerAssessmentsCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            ScGlobalPartnerAssessmentsReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return ScGlobalPartnerAssessmentsCreateReadResponse(error = readResponse.error, globalPartnerAssessment = readResponse.globalPartnerAssessment)
    }
}