package com.seedcompany.cordtables.components.tables.sc.budget_records

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScBudgetRecordsUpdateRequest(
    val token: String?,
    val id: Int? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class ScBudgetRecordsUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("ScBudgetRecordsUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("sc-budgetrecords/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: ScBudgetRecordsUpdateRequest): ScBudgetRecordsUpdateResponse {

        if (req.token == null) return ScBudgetRecordsUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return ScBudgetRecordsUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return ScBudgetRecordsUpdateResponse(ErrorType.MissingId)

        when (req.column) {
            "neo4j_id" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.budgetrecords",
                    column = "neo4j_id",
                    id = req.id,
                    value = req.value,
                )
            }

            "budget" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.budgetrecords",
                    column = "budget",
                    id = req.id,
                    value = req.value,
                )
            }

            "change_to_plan" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.budgetrecords",
                    column = "change_to_plan",
                    id = req.id,
                    value = req.value,
                )
            }

            "active" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.budgetrecords",
                    column = "active",
                    id = req.id,
                    value = req.value,
                )
            }

            "amount" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.budgetrecords",
                    column = "amount",
                    id = req.id,
                    value = req.value,
                )
            }

            "fiscal_year" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.budgetrecords",
                    column = "fiscal_year",
                    id = req.id,
                    value = req.value,
                )
            }

            "partnership" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.budgetrecords",
                    column = "partnership",
                    id = req.id,
                    value = req.value,
                )
            }

            "owning_person" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.budgetrecords",
                    column = "owning_person",
                    id = req.id,
                    value = req.value,
                )
            }

            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.budgetrecords",
                    column = "owning_group",
                    id = req.id,
                    value = req.value,
                )
            }

//            else -> null
        }

        return ScBudgetRecordsUpdateResponse(ErrorType.NoError)
    }

}