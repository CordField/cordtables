package com.seedcompany.cordtables.components.tables.sc.periodic_reports

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.periodic_reports.ScPeriodicReportsReadRequest
import com.seedcompany.cordtables.components.tables.sc.periodic_reports.ScPeriodicReportsUpdateRequest
import com.seedcompany.cordtables.components.tables.sc.periodic_reports.periodicReport
import com.seedcompany.cordtables.components.tables.sc.periodic_reports.periodicReportInput
import com.seedcompany.cordtables.components.tables.sc.locations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScPeriodicReportsUpdateReadRequest(
    val token: String?,
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class ScPeriodicReportsUpdateReadResponse(
    val error: ErrorType,
    val periodicReport: periodicReport? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScPeriodicReportsUpdateRead")
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
    @PostMapping("sc/periodic-reports/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: ScPeriodicReportsUpdateReadRequest): ScPeriodicReportsUpdateReadResponse {

      if (req.token == null) return ScPeriodicReportsUpdateReadResponse(ErrorType.InputMissingToken)
      if (!util.isAdmin(req.token)) return ScPeriodicReportsUpdateReadResponse(ErrorType.AdminOnly)

        val updateResponse = update.updateHandler(
            ScPeriodicReportsUpdateRequest(
                token = req.token,
                column = req.column,
                id = req.id,
                value = req.value,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return ScPeriodicReportsUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
            ScPeriodicReportsReadRequest(
                token = req.token,
                id = req.id!!
            )
        )

        return ScPeriodicReportsUpdateReadResponse(error = readResponse.error, readResponse.periodicReport)
    }
}
