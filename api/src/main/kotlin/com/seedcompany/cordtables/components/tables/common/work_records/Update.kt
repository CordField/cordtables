package com.seedcompany.cordtables.components.tables.common.work_records

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonWorkRecordUpdateRequest(
  val token: String?,
  val id: Int? = null,
  val ticket: Int? = null,
  val hours: Int? = null,
  val minutes: Int? = null
)

data class CommonWorkRecordUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonWorkRecordsUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("common-work-records/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: CommonWorkRecordUpdateRequest): CommonWorkRecordUpdateResponse {

        if (req.token == null) return CommonWorkRecordUpdateResponse(ErrorType.TokenNotFound)
        if (req.id == null) return CommonWorkRecordUpdateResponse(ErrorType.MissingId)


                util.updateField(
                    token = req.token,
                    table = "common.work_records",
                    column = "hours",
                    id = req.id,
                    value = req.hours,
                    cast = "::integer"
                )

                util.updateField(
                    token = req.token,
                    table = "common.work_records",
                    column = "minutes",
                    id = req.id,
                    value = req.minutes,
                    cast = "::integer"
                )




        return CommonWorkRecordUpdateResponse(ErrorType.NoError)
    }
}
