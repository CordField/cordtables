package com.seedcompany.cordtables.components.tables.sc.project_memberships

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.project_memberships.*
import com.seedcompany.cordtables.components.tables.sc.project_memberships.ScProjectMembershipsCreateRequest
import com.seedcompany.cordtables.components.tables.sc.project_memberships.Create
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScProjectMembershipsCreateReadRequest(
    val token: String? = null,
    val projectMembership: projectMembershipInput,
)

data class ScProjectMembershipsCreateReadResponse(
    val error: ErrorType,
    val projectMembership: projectMembership? = null,
)


@Controller("ScProjectMembershipsCreateRead")
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
    @PostMapping("sc-project-memberships/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: ScProjectMembershipsCreateReadRequest): ScProjectMembershipsCreateReadResponse {

        val createResponse = create.createHandler(
            ScProjectMembershipsCreateRequest(
                token = req.token,
                projectMembership = req.projectMembership
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return ScProjectMembershipsCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            ScProjectMembershipsReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return ScProjectMembershipsCreateReadResponse(error = readResponse.error, projectMembership = readResponse.projectMembership)
    }
}