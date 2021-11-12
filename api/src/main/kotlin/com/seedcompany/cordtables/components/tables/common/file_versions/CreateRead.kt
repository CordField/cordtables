package com.seedcompany.cordtables.components.tables.common.file_versions

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.file_versions.*
import com.seedcompany.cordtables.components.tables.common.file_versions.CommonFileVersionsCreateRequest
import com.seedcompany.cordtables.components.tables.common.file_versions.Create
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonFileVersionsCreateReadRequest(
    val token: String? = null,
    val fileVersion: CommonFileVersionInput,
)

data class CommonFileVersionsCreateReadResponse(
    val error: ErrorType,
    val fileVersion: CommonFileVersion? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("CommonFileVersionsCreateRead")
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
    @PostMapping("common-file-versions/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: CommonFileVersionsCreateReadRequest): CommonFileVersionsCreateReadResponse {

        val createResponse = create.createHandler(
            CommonFileVersionsCreateRequest(
                token = req.token,
                fileVersion = req.fileVersion
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return CommonFileVersionsCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            CommonFileVersionsReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return CommonFileVersionsCreateReadResponse(error = readResponse.error, fileVersion = readResponse.fileVersion)
    }
}