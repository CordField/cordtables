package com.seedcompany.cordtables.components.tables.sc.project_memberships

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScProjectMembershipsUpdateRequest(
    val token: String?,
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class ScProjectMembershipsUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScProjectMembershipsUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("sc-project-memberships/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: ScProjectMembershipsUpdateRequest): ScProjectMembershipsUpdateResponse {

        if (req.token == null) return ScProjectMembershipsUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return ScProjectMembershipsUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return ScProjectMembershipsUpdateResponse(ErrorType.MissingId)

        when (req.column) {
            "group_id" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.project_memberships",
                    column = "group_id",
                    id = req.id,
                    value = req.value,
                    cast = "::uuid"
                )
            }
            "person" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.project_memberships",
                    column = "person",
                    id = req.id,
                    value = req.value,
                    cast = "::uuid"
                )
            }
            "owning_person" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.project_memberships",
                    column = "owning_person",
                    id = req.id,
                    value = req.value,
                    cast = "::uuid"
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.project_memberships",
                    column = "owning_group",
                    id = req.id,
                    value = req.value,
                    cast = "::uuid"
                )
            }
        }

        return ScProjectMembershipsUpdateResponse(ErrorType.NoError)
    }

}
