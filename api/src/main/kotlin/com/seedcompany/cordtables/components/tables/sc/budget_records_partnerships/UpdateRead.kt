package com.seedcompany.cordtables.components.tables.sc.budget_records_partnerships

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.budget_records_partnerships.ScBudgetRecordsPartnershipsReadRequest
import com.seedcompany.cordtables.components.tables.sc.budget_records_partnerships.ScBudgetRecordsPartnershipsUpdateRequest
import com.seedcompany.cordtables.components.tables.sc.budget_records_partnerships.budgetRecordsPartnership
import com.seedcompany.cordtables.components.tables.sc.budget_records_partnerships.budgetRecordsPartnershipInput
import com.seedcompany.cordtables.components.tables.sc.locations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScBudgetRecordsPartnershipsUpdateReadRequest(
    val token: String?,
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class ScBudgetRecordsPartnershipsUpdateReadResponse(
    val error: ErrorType,
    val budgetRecordsPartnership: budgetRecordsPartnership? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScBudgetRecordsPartnershipsUpdateRead")
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
    @PostMapping("sc/budget-records-partnerships/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: ScBudgetRecordsPartnershipsUpdateReadRequest): ScBudgetRecordsPartnershipsUpdateReadResponse {

      if (req.token == null) return ScBudgetRecordsPartnershipsUpdateReadResponse(ErrorType.InputMissingToken)
      if (!util.isAdmin(req.token)) return ScBudgetRecordsPartnershipsUpdateReadResponse(ErrorType.AdminOnly)

        val updateResponse = update.updateHandler(
            ScBudgetRecordsPartnershipsUpdateRequest(
                token = req.token,
                column = req.column,
                id = req.id,
                value = req.value,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return ScBudgetRecordsPartnershipsUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
            ScBudgetRecordsPartnershipsReadRequest(
                token = req.token,
                id = req.id!!
            )
        )

        return ScBudgetRecordsPartnershipsUpdateReadResponse(error = readResponse.error, readResponse.budgetRecordsPartnership)
    }
}
