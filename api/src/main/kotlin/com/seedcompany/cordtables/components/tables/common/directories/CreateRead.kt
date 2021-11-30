package com.seedcompany.cordtables.components.tables.common.directories


import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.directories.*
import com.seedcompany.cordtables.components.tables.common.directories.Create
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonDirectoriesCreateReadRequest(
    val token: String? = null,
    val directory: CommonDirectoryInput,
)

data class CommonDirectoriesCreateReadResponse(
    val error: ErrorType,
    val directory: CommonDirectory? = null,
)


@Controller("CommonDirectoriesCreateRead")
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
    @PostMapping("common-directories/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: CommonDirectoriesCreateReadRequest): CommonDirectoriesCreateReadResponse {

        val createResponse = create.createHandler(
            CommonDirectoriesCreateRequest(
                token = req.token,
                directory = req.directory
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return CommonDirectoriesCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            CommonDirectoriesReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return CommonDirectoriesCreateReadResponse(error = readResponse.error, directory = readResponse.directory)
    }
}