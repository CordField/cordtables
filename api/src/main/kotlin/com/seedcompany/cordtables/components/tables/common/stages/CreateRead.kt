package com.seedcompany.cordtables.components.tables.common.stages

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.stages.*
import com.seedcompany.cordtables.components.tables.common.stages.CommonStagesCreateRequest
import com.seedcompany.cordtables.components.tables.common.stages.Create
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonStagesCreateReadRequest(
    val token: String? = null,
    val stage: stageInput,
)

data class CommonStagesCreateReadResponse(
    val error: ErrorType,
    val stage: stage? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("CommonStagesCreateRead")
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
    @PostMapping("common-stages/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: CommonStagesCreateReadRequest): CommonStagesCreateReadResponse {

        val createResponse = create.createHandler(
            CommonStagesCreateRequest(
                token = req.token,
                stage = req.stage
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return CommonStagesCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            CommonStagesReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return CommonStagesCreateReadResponse(error = readResponse.error, stage = readResponse.stage)
    }
}