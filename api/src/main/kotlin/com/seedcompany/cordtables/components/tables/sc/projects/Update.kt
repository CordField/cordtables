package com.seedcompany.cordtables.components.tables.sc.projects

import com.seedcompany.cordtables.components.tables.sc.projects.ScProjectsUpdateRequest
import com.seedcompany.cordtables.components.tables.sc.projects.Update as CommonUpdate
import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.common.enumContains
import com.seedcompany.cordtables.components.tables.sc.projects.ScProjectsUpdateResponse
import com.seedcompany.cordtables.components.tables.sc.projects.projectInput
import com.seedcompany.cordtables.components.tables.sc.locations.ScLocationInput
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScProjectsUpdateRequest(
    val token: String?,
    val id: Int? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class ScProjectsUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("ScProjectsUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("sc-projects/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: ScProjectsUpdateRequest): ScProjectsUpdateResponse {

        if (req.token == null) return ScProjectsUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return ScProjectsUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return ScProjectsUpdateResponse(ErrorType.MissingId)

        when (req.column) {
            "neo4j_id" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.projects",
                    column = "neo4j_id",
                    id = req.id,
                    value = req.value,
                )
            }
            "name" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.projects",
                    column = "name",
                    id = req.id,
                    value = req.value,
                )
            }
            "change_to_plan" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.projects",
                    column = "change_to_plan",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "active" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.projects",
                    column = "active",
                    id = req.id,
                    value = req.value,
                    cast = "::BOOLEAN"
                )
            }
            "department" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.projects",
                    column = "department",
                    id = req.id,
                    value = req.value,
                )
            }
            "estimated_submission" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.projects",
                    column = "estimated_submission",
                    id = req.id,
                    value = req.value,
                    cast = "::timestamp"
                )
            }
            "field_region" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.projects",
                    column = "field_region",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "initial_mou_end" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.projects",
                    column = "initial_mou_end",
                    id = req.id,
                    value = req.value,
                    cast = "::timestamp"
                )
            }
            "marketing_location" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.projects",
                    column = "marketing_location",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "mou_start" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.projects",
                    column = "mou_start",
                    id = req.id,
                    value = req.value,
                    cast = "::timestamp"
                )
            }
            "mou_end" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.projects",
                    column = "mou_end",
                    id = req.id,
                    value = req.value,
                    cast = "::timestamp"
                )
            }
            "owning_organization" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.projects",
                    column = "owning_organization",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "periodic_reports_directory" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.projects",
                    column = "periodic_reports_directory",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "posts_directory" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.projects",
                    column = "posts_directory",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "primary_location" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.projects",
                    column = "primary_location",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "root_directory" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.projects",
                    column = "root_directory",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "status" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.projects",
                    column = "status",
                    id = req.id,
                    value = req.value,
                    cast = "::sc.project_status"
                )
            }
            "status_changed_at" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.projects",
                    column = "status_changed_at",
                    id = req.id,
                    value = req.value,
                    cast = "::timestamp"
                )
            }
            "step" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.projects",
                    column = "step",
                    id = req.id,
                    value = req.value,
                    cast = "::sc.project_step"
                )
            }


            "owning_person" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.projects",
                    column = "owning_person",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.projects",
                    column = "owning_group",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
        }

        return ScProjectsUpdateResponse(ErrorType.NoError)
    }

}