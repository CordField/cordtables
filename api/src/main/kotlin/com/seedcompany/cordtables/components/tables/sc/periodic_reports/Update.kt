package com.seedcompany.cordtables.components.tables.sc.periodic_reports

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScPeriodicReportsUpdateRequest(
    val token: String?,
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class ScPeriodicReportsUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScPeriodicReportsUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("sc/periodic-reports/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: ScPeriodicReportsUpdateRequest): ScPeriodicReportsUpdateResponse {

      if (req.token == null) return ScPeriodicReportsUpdateResponse(ErrorType.InputMissingToken)
      if (!util.isAdmin(req.token)) return ScPeriodicReportsUpdateResponse(ErrorType.AdminOnly)
        if (req.column == null) return ScPeriodicReportsUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return ScPeriodicReportsUpdateResponse(ErrorType.MissingId)

        when (req.column) {
            "directory" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.periodic_reports",
                    column = "directory",
                    id = req.id,
                    value = req.value
                )
            }
            "end_at" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.periodic_reports",
                    column = "end_at",
                    id = req.id,
                    value = req.value,
                    cast = "::timestamp"
                )
            }
            "report_file" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.periodic_reports",
                    column = "report_file",
                    id = req.id,
                    value = req.value
                )
            }
            "start_at" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.periodic_reports",
                    column = "start_at",
                    id = req.id,
                    value = req.value,
                    cast = "::timestamp"
                )
            }
            "type" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.periodic_reports",
                    column = "type",
                    id = req.id,
                    value = req.value,
                    cast = "::sc.periodic_report_type"
                )
            }
            "skipped_reason" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.periodic_reports",
                    column = "skipped_reason",
                    id = req.id,
                    value = req.value,
                )
            }
            "owning_person" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.periodic_reports",
                    column = "owning_person",
                    id = req.id,
                    value = req.value
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.periodic_reports",
                    column = "owning_group",
                    id = req.id,
                    value = req.value
                )
            }
        }

        return ScPeriodicReportsUpdateResponse(ErrorType.NoError)
    }

}
