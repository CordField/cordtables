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

data class ScBudgetRecordsUpdateReadRequest(
    val token: String?,
    val id: Int? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class ScBudgetRecordsUpdateReadResponse(
    val error: ErrorType,
    val budget_record: BudgetRecord? = null,

)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("ScBudgetRecordsUpdateRead")
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
  
    @PostMapping("sc-budget-records/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: ScBudgetRecordsUpdateReadRequest): ScBudgetRecordsUpdateReadResponse {

        val updateResponse = update.updateHandler(
            ScBudgetRecordsUpdateRequest(
                token = req.token,
                column = req.column,
                id = req.id,
                value = req.value,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return ScBudgetRecordsUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
            ScBudgetRecordsReadRequest(
                token = req.token,
                id = req.id!!
            )
        )

        return ScBudgetRecordsUpdateReadResponse(error = readResponse.error, readResponse.budget_record)

    }
}
