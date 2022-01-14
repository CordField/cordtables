package com.seedcompany.cordtables.components.tables.sc.budget_records_partnerships

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.budget_records_partnerships.*
import com.seedcompany.cordtables.components.tables.sc.budget_records_partnerships.ScBudgetRecordsPartnershipsCreateRequest
import com.seedcompany.cordtables.components.tables.sc.budget_records_partnerships.Create
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScBudgetRecordsPartnershipsCreateReadRequest(
    val token: String? = null,
    val budgetRecordsPartnership: budgetRecordsPartnershipInput,
)

data class ScBudgetRecordsPartnershipsCreateReadResponse(
    val error: ErrorType,
    val budgetRecordsPartnership: budgetRecordsPartnership? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScBudgetRecordsPartnershipsCreateRead")
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
    @PostMapping("sc/budget-records-partnerships/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: ScBudgetRecordsPartnershipsCreateReadRequest): ScBudgetRecordsPartnershipsCreateReadResponse {

      if (req.token == null) return ScBudgetRecordsPartnershipsCreateReadResponse(ErrorType.InputMissingToken)
      if (!util.isAdmin(req.token)) return ScBudgetRecordsPartnershipsCreateReadResponse(ErrorType.AdminOnly)

        val createResponse = create.createHandler(
            ScBudgetRecordsPartnershipsCreateRequest(
                token = req.token,
                budgetRecordsPartnership = req.budgetRecordsPartnership
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return ScBudgetRecordsPartnershipsCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            ScBudgetRecordsPartnershipsReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return ScBudgetRecordsPartnershipsCreateReadResponse(error = readResponse.error, budgetRecordsPartnership = readResponse.budgetRecordsPartnership)
    }
}
