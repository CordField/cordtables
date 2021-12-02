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

data class CommonThreadsCreateReadRequest(
        val token: String? = null,
        val thread: ThreadInput,
)

data class CommonThreadsCreateReadResponse(
        val error: ErrorType,
        val thread: Thread? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonThreadsCreateRead")
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
    @PostMapping("common-threads/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: CommonThreadsCreateReadRequest): CommonThreadsCreateReadResponse {

        val createResponse = create.createHandler(
                CommonThreadsCreateRequest(
                        token = req.token,
                        thread = req.thread
                )
        )

        if (createResponse.error != ErrorType.NoError) {
            return CommonThreadsCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
                CommonThreadsReadRequest(
                        token = req.token,
                        id = createResponse!!.id
                )
        )

        return CommonThreadsCreateReadResponse(error = readResponse.error, thread = readResponse.thread)
    }
}
