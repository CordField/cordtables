package com.seedcompany.cordtables.components.tables.common.notes

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.notes.CommonNotesReadRequest
import com.seedcompany.cordtables.components.tables.common.notes.CommonNotesUpdateRequest
import com.seedcompany.cordtables.components.tables.common.notes.note
import com.seedcompany.cordtables.components.tables.common.notes.noteInput
import com.seedcompany.cordtables.components.tables.sc.locations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonNotesUpdateReadRequest(
    val token: String?,
    val id: Int? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class CommonNotesUpdateReadResponse(
    val error: ErrorType,
    val note: note? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonNotesUpdateRead")
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
    @PostMapping("common-notes/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: CommonNotesUpdateReadRequest): CommonNotesUpdateReadResponse {

        val updateResponse = update.updateHandler(
            CommonNotesUpdateRequest(
                token = req.token,
                column = req.column,
                id = req.id,
                value = req.value,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return CommonNotesUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
            CommonNotesReadRequest(
                token = req.token,
                id = req.id!!
            )
        )

        return CommonNotesUpdateReadResponse(error = readResponse.error, readResponse.note)
    }
}