package com.seedcompany.cordtables.components.tables.common.notes

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.notes.*
import com.seedcompany.cordtables.components.tables.common.notes.CommonNotesCreateRequest
import com.seedcompany.cordtables.components.tables.common.notes.Create
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonNotesCreateReadRequest(
    val token: String? = null,
    val note: noteInput,
)

data class CommonNotesCreateReadResponse(
    val error: ErrorType,
    val note: note? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("CommonNotesCreateRead")
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
    @PostMapping("common-notes/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: CommonNotesCreateReadRequest): CommonNotesCreateReadResponse {

        val createResponse = create.createHandler(
            CommonNotesCreateRequest(
                token = req.token,
                note = req.note
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return CommonNotesCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            CommonNotesReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return CommonNotesCreateReadResponse(error = readResponse.error, note = readResponse.note)
    }
}