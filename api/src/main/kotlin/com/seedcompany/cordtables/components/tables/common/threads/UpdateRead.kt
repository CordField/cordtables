package com.seedcompany.cordtables.components.tables.common.threads

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonThreadsUpdateReadRequest(
        val token: String?,
        val id: Int? = null,
        val column: String? = null,
        val value: Any? = null,
)

data class CommonThreadsUpdateReadResponse(
        val error: ErrorType,
        val thread: Thread? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonThreadsUpdateRead")
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
    @PostMapping("common/threads/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: CommonThreadsUpdateReadRequest): CommonThreadsUpdateReadResponse {

        val updateResponse = update.updateHandler(
                CommonThreadsUpdateRequest(
                        token = req.token,
                        column = req.column,
                        id = req.id,
                        value = req.value,
                )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return CommonThreadsUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
                CommonThreadsReadRequest(
                        token = req.token,
                        id = req.id!!
                )
        )

        return CommonThreadsUpdateReadResponse(error = readResponse.error, readResponse.thread)
    }
}
