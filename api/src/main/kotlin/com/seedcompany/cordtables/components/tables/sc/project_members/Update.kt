package com.seedcompany.cordtables.components.tables.sc.project_members

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScProjectMembersUpdateRequest(
    val token: String?,
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class ScProjectMembersUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScProjectMembersUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("sc/project-members/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: ScProjectMembersUpdateRequest): ScProjectMembersUpdateResponse {

      if (req.token == null) return ScProjectMembersUpdateResponse(ErrorType.InputMissingToken)
      if (!util.isAdmin(req.token)) return ScProjectMembersUpdateResponse(ErrorType.AdminOnly)
        if (req.column == null) return ScProjectMembersUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return ScProjectMembersUpdateResponse(ErrorType.MissingId)

        when (req.column) {
            "project" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.project_members",
                    column = "project",
                    id = req.id,
                    value = req.value,
                    cast = "::uuid"
                )
            }
            "person" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.project_members",
                    column = "person",
                    id = req.id,
                    value = req.value,
                    cast = "::uuid"
                )
            }

          "group_id" -> {
            util.updateField(
              token = req.token,
              table = "sc.project_members",
              column = "group_id",
              id = req.id,
              value = req.value,
              cast = "::uuid"
            )
          }

          "role" -> {
            util.updateField(
              token = req.token,
              table = "sc.project_members",
              column = "role",
              id = req.id,
              value = req.value,
              cast = "::uuid"
            )
          }

          "sensitivity" -> {
            util.updateField(
              token = req.token,
              table = "sc.project_members",
              column = "sensitivity",
              id = req.id,
              value = req.value,
              cast = "::common.sensitivity"
            )
          }
            "owning_person" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.project_members",
                    column = "owning_person",
                    id = req.id,
                    value = req.value,
                    cast = "::uuid"
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.project_members",
                    column = "owning_group",
                    id = req.id,
                    value = req.value,
                    cast = "::uuid"
                )
            }
        }

        return ScProjectMembersUpdateResponse(ErrorType.NoError)
    }

}
