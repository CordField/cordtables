package com.seedcompany.cordtables.components.tables.common.coalitions

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.coalitions.*
import com.seedcompany.cordtables.components.tables.common.coalitions.CommonCoalitionsCreateRequest
import com.seedcompany.cordtables.components.tables.common.coalitions.Create
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonCoalitionsCreateReadRequest(
    val token: String? = null,
    val coalition: coalitionInput,
)

data class CommonCoalitionsCreateReadResponse(
    val error: ErrorType,
    val coalition: coalition? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonCoalitionsCreateRead")
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
    @PostMapping("common/coalitions/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: CommonCoalitionsCreateReadRequest): CommonCoalitionsCreateReadResponse {

        val createResponse = create.createHandler(
            CommonCoalitionsCreateRequest(
                token = req.token,
                coalition = req.coalition
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return CommonCoalitionsCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            CommonCoalitionsReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return CommonCoalitionsCreateReadResponse(error = readResponse.error, coalition = readResponse.coalition)
    }
}
