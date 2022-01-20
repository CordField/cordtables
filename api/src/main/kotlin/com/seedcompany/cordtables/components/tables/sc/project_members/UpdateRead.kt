package com.seedcompany.cordtables.components.tables.sc.project_members

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.project_members.ScProjectMembersReadRequest
import com.seedcompany.cordtables.components.tables.sc.project_members.ScProjectMembersUpdateRequest
import com.seedcompany.cordtables.components.tables.sc.project_members.projectMember
import com.seedcompany.cordtables.components.tables.sc.project_members.projectMemberInput
import com.seedcompany.cordtables.components.tables.sc.locations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScProjectMembersUpdateReadRequest(
    val token: String?,
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class ScProjectMembersUpdateReadResponse(
    val error: ErrorType,
    val projectMember: projectMember? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScProjectMembersUpdateRead")
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
    @PostMapping("sc/project-members/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: ScProjectMembersUpdateReadRequest): ScProjectMembersUpdateReadResponse {

      if (req.token == null) return ScProjectMembersUpdateReadResponse(ErrorType.InputMissingToken)
      if (!util.isAdmin(req.token)) return ScProjectMembersUpdateReadResponse(ErrorType.AdminOnly)

        val updateResponse = update.updateHandler(
            ScProjectMembersUpdateRequest(
                token = req.token,
                column = req.column,
                id = req.id,
                value = req.value,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return ScProjectMembersUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
            ScProjectMembersReadRequest(
                token = req.token,
                id = req.id!!
            )
        )

        return ScProjectMembersUpdateReadResponse(error = readResponse.error, readResponse.projectMember)
    }
}
