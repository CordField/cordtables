package com.seedcompany.cordtables.components.tables.common.stages

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.stages.CommonStagesReadRequest
import com.seedcompany.cordtables.components.tables.common.stages.CommonStagesUpdateRequest
import com.seedcompany.cordtables.components.tables.common.stages.stage
import com.seedcompany.cordtables.components.tables.common.stages.stageInput
import com.seedcompany.cordtables.components.tables.sc.locations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonStagesUpdateReadRequest(
    val token: String?,
    val id: Int? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class CommonStagesUpdateReadResponse(
    val error: ErrorType,
    val stage: stage? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("CommonStagesUpdateRead")
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
    @PostMapping("common-stages/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: CommonStagesUpdateReadRequest): CommonStagesUpdateReadResponse {

        val updateResponse = update.updateHandler(
            CommonStagesUpdateRequest(
                token = req.token,
                column = req.column,
                id = req.id,
                value = req.value,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return CommonStagesUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
            CommonStagesReadRequest(
                token = req.token,
                id = req.id!!
            )
        )

        return CommonStagesUpdateReadResponse(error = readResponse.error, readResponse.stage)
    }
}