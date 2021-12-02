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

data class CommonWorkRecordCreateReadRequest(
    val token: String? = null,
    val work_record: CommonWorkRecordInput,
)

data class CommonWorkRecordCreateReadResponse(
    val error: ErrorType,
    val work_record: CommonWorkRecords? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonWorkRecordsCreateRead")
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
    @PostMapping("common-work-records/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: CommonWorkRecordCreateReadRequest): CommonWorkRecordCreateReadResponse {

        val createResponse = create.createHandler(
            CommonWorkRecordCreateRequest(
                token = req.token,
                work_record = req.work_record
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return CommonWorkRecordCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            CommonWorkRecordReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return CommonWorkRecordCreateReadResponse(error = readResponse.error, work_record = readResponse.work_record)
    }
}
