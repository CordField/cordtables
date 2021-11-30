package com.seedcompany.cordtables.components.tables.sc.internship_engagements

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.internship_engagements.ScInternshipEngagementsReadRequest
import com.seedcompany.cordtables.components.tables.sc.internship_engagements.ScInternshipEngagementsUpdateRequest
import com.seedcompany.cordtables.components.tables.sc.internship_engagements.internshipEngagement
import com.seedcompany.cordtables.components.tables.sc.internship_engagements.internshipEngagementInput
import com.seedcompany.cordtables.components.tables.sc.locations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScInternshipEngagementsUpdateReadRequest(
    val token: String?,
    val id: Int? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class ScInternshipEngagementsUpdateReadResponse(
    val error: ErrorType,
    val internshipEngagement: internshipEngagement? = null,
)


@Controller("ScInternshipEngagementsUpdateRead")
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
    @PostMapping("sc-internship-engagements/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: ScInternshipEngagementsUpdateReadRequest): ScInternshipEngagementsUpdateReadResponse {

        val updateResponse = update.updateHandler(
            ScInternshipEngagementsUpdateRequest(
                token = req.token,
                column = req.column,
                id = req.id,
                value = req.value,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return ScInternshipEngagementsUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
            ScInternshipEngagementsReadRequest(
                token = req.token,
                id = req.id!!
            )
        )

        return ScInternshipEngagementsUpdateReadResponse(error = readResponse.error, readResponse.internshipEngagement)
    }
}