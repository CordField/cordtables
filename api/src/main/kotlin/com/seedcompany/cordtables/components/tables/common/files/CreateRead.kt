package com.seedcompany.cordtables.components.tables.common.files


import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.files.*
import com.seedcompany.cordtables.components.tables.common.files.Create
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonFilesCreateReadRequest(
    val token: String? = null,
    val file: CommonFileInput,
)

data class CommonFilesCreateReadResponse(
    val error: ErrorType,
    val file: CommonFile? = null,
)


@Controller("CommonFilesCreateRead")
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
    @PostMapping("common-files/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: CommonFilesCreateReadRequest): CommonFilesCreateReadResponse {

        val createResponse = create.createHandler(
            CommonFilesCreateRequest(
                token = req.token,
                file = req.file
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return CommonFilesCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            CommonFilesReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return CommonFilesCreateReadResponse(error = readResponse.error, file = readResponse.file)
    }
}