package com.seedcompany.cordtables.components.tables.sc.project_members

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.project_members.*
import com.seedcompany.cordtables.components.tables.sc.project_members.ScProjectMembersCreateRequest
import com.seedcompany.cordtables.components.tables.sc.project_members.Create
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScProjectMembersCreateReadRequest(
    val token: String? = null,
    val projectMember: projectMemberInput,
)

data class ScProjectMembersCreateReadResponse(
    val error: ErrorType,
    val projectMember: projectMember? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScProjectMembersCreateRead")
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
    @PostMapping("sc/project-members/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: ScProjectMembersCreateReadRequest): ScProjectMembersCreateReadResponse {

      if (req.token == null) return ScProjectMembersCreateReadResponse(ErrorType.InputMissingToken)
      if (!util.isAdmin(req.token)) return ScProjectMembersCreateReadResponse(ErrorType.AdminOnly)

        val createResponse = create.createHandler(
            ScProjectMembersCreateRequest(
                token = req.token,
                projectMember = req.projectMember
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return ScProjectMembersCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            ScProjectMembersReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return ScProjectMembersCreateReadResponse(error = readResponse.error, projectMember = readResponse.projectMember)
    }
}
