package com.seedcompany.cordtables.components.tables.common.directories


import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.locations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonDirectoriesUpdateReadRequest(
    val token: String?,
    val id: Int? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class CommonDirectoriesUpdateReadResponse(
    val error: ErrorType,
    val directory: CommonDirectory? = null,
)


@Controller("CommonDirectoriesUpdateRead")
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
    @PostMapping("common-directories/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: CommonDirectoriesUpdateReadRequest): CommonDirectoriesUpdateReadResponse {

        val updateResponse = update.updateHandler(
            CommonDirectoriesUpdateRequest(
                token = req.token,
                column = req.column,
                id = req.id,
                value = req.value,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return CommonDirectoriesUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
            CommonDirectoriesReadRequest(
                token = req.token,
                id = req.id!!
            )
        )

        return CommonDirectoriesUpdateReadResponse(error = readResponse.error, readResponse.directory)
    }
}
