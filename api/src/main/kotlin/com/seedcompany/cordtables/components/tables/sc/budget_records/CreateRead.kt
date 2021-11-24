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

data class ScBudgetRecordsCreateReadRequest(
    val token: String? = null,
    val budgetrecord: BudgetRecordInput,
)

data class ScBudgetRecordsCreateReadResponse(
    val error: ErrorType,
    val budgetrecord: BudgetRecord? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("ScBudgetRecordsCreateRead")
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
    @PostMapping("sc-budgetrecords/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: ScBudgetRecordsCreateReadRequest): ScBudgetRecordsCreateReadResponse {

        val createResponse = create.createHandler(
            ScBudgetRecordsCreateRequest(
                token = req.token,
                budgetrecord = req.budgetrecord
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return ScBudgetRecordsCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            ScBudgetRecordsReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return ScBudgetRecordsCreateReadResponse(error = readResponse.error, budgetrecord = readResponse.budgetrecord)
    }
}
