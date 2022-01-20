package com.seedcompany.cordtables.components.tables.sc.budget_records_partnerships

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScBudgetRecordsPartnershipsUpdateRequest(
    val token: String?,
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class ScBudgetRecordsPartnershipsUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScBudgetRecordsPartnershipsUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("sc/budget-records-partnerships/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: ScBudgetRecordsPartnershipsUpdateRequest): ScBudgetRecordsPartnershipsUpdateResponse {

      if (req.token == null) return ScBudgetRecordsPartnershipsUpdateResponse(ErrorType.InputMissingToken)
      if (!util.isAdmin(req.token)) return ScBudgetRecordsPartnershipsUpdateResponse(ErrorType.AdminOnly)
        if (req.column == null) return ScBudgetRecordsPartnershipsUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return ScBudgetRecordsPartnershipsUpdateResponse(ErrorType.MissingId)

        when (req.column) {
            "budget_record" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.budget_records_partnerships",
                    column = "budget_record",
                    id = req.id,
                    value = req.value,
                    cast = "::uuid"
                )
            }
            "partnership" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.budget_records_partnerships",
                    column = "partnership",
                    id = req.id,
                    value = req.value,
                    cast = "::uuid"
                )
            }
            "owning_person" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.budget_records_partnerships",
                    column = "owning_person",
                    id = req.id,
                    value = req.value,
                    cast = "::uuid"
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.budget_records_partnerships",
                    column = "owning_group",
                    id = req.id,
                    value = req.value,
                    cast = "::uuid"
                )
            }
        }

        return ScBudgetRecordsPartnershipsUpdateResponse(ErrorType.NoError)
    }

}
