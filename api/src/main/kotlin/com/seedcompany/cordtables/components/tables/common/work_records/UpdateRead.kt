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

data class CommonWorkRecordUpdateReadRequest(
    val token: String?,
    val id: Int? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class CommonWorkRecordUpdateReadResponse(
    val error: ErrorType,
    val work_record: CommonWorkRecords? = null,
)


@Controller("CommonWorkRecordsUpdateRead")
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
    @PostMapping("common-work-records/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: CommonWorkRecordUpdateReadRequest): CommonWorkRecordUpdateReadResponse {

        val updateResponse = update.updateHandler(
            CommonWorkRecordUpdateRequest(
                token = req.token,
                column = req.column,
                id = req.id,
                value = req.value,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return CommonWorkRecordUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
            CommonWorkRecordReadRequest(
                token = req.token,
                id = req.id!!
            )
        )

        return CommonWorkRecordUpdateReadResponse(error = readResponse.error, readResponse.work_record)
    }
}
