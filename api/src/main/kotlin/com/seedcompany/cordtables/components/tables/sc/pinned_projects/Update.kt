package com.seedcompany.cordtables.components.tables.sc.pinned_projects

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScPinnedProjectsUpdateRequest(
    val token: String?,
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class ScPinnedProjectsUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScPinnedProjectsUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("sc-pinned-projects/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: ScPinnedProjectsUpdateRequest): ScPinnedProjectsUpdateResponse {

        if (req.token == null) return ScPinnedProjectsUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return ScPinnedProjectsUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return ScPinnedProjectsUpdateResponse(ErrorType.MissingId)

        when (req.column) {
            "person" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.pinned_projects",
                    column = "person",
                    id = req.id,
                    value = req.value
                )
            }
            "project" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.pinned_projects",
                    column = "project",
                    id = req.id,
                    value = req.value
                )
            }
            "owning_person" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.pinned_projects",
                    column = "owning_person",
                    id = req.id,
                    value = req.value
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.pinned_projects",
                    column = "owning_group",
                    id = req.id,
                    value = req.value
                )
            }
        }

        return ScPinnedProjectsUpdateResponse(ErrorType.NoError)
    }

}
