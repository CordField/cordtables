package com.seedcompany.cordtables.components.tables.common.files

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.files.CommonFilesReadRequest
import com.seedcompany.cordtables.components.tables.common.files.CommonFilesUpdateRequest
import com.seedcompany.cordtables.components.tables.common.files.CommonFile
import com.seedcompany.cordtables.components.tables.common.files.CommonFileInput
import com.seedcompany.cordtables.components.tables.sc.locations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonFilesUpdateReadRequest(
    val token: String?,
    val id: Int? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class CommonFilesUpdateReadResponse(
    val error: ErrorType,
    val file: CommonFile? = null,
)


@Controller("CommonFilesUpdateRead")
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
    @PostMapping("common-files/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: CommonFilesUpdateReadRequest): CommonFilesUpdateReadResponse {

        val updateResponse = update.updateHandler(
            CommonFilesUpdateRequest(
                token = req.token,
                column = req.column,
                id = req.id,
                value = req.value,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return CommonFilesUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
            CommonFilesReadRequest(
                token = req.token,
                id = req.id!!
            )
        )

        return CommonFilesUpdateReadResponse(error = readResponse.error, readResponse.file)
    }
}