package com.seedcompany.cordtables.components.tables.sc.internship_engagements

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.internship_engagements.*
import com.seedcompany.cordtables.components.tables.sc.internship_engagements.ScInternshipEngagementsCreateRequest
import com.seedcompany.cordtables.components.tables.sc.internship_engagements.Create
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScInternshipEngagementsCreateReadRequest(
    val token: String? = null,
    val internshipEngagement: internshipEngagementInput,
)

data class ScInternshipEngagementsCreateReadResponse(
    val error: ErrorType,
    val internshipEngagement: internshipEngagement? = null,
)


@Controller("ScInternshipEngagementsCreateRead")
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
    @PostMapping("sc-internship-engagements/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: ScInternshipEngagementsCreateReadRequest): ScInternshipEngagementsCreateReadResponse {

        val createResponse = create.createHandler(
            ScInternshipEngagementsCreateRequest(
                token = req.token,
                internshipEngagement = req.internshipEngagement
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return ScInternshipEngagementsCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            ScInternshipEngagementsReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return ScInternshipEngagementsCreateReadResponse(error = readResponse.error, internshipEngagement = readResponse.internshipEngagement)
    }
}