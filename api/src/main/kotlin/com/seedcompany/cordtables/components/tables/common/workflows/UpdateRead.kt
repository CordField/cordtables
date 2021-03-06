package com.seedcompany.cordtables.components.tables.common.workflows

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.workflows.CommonWorkflowsReadRequest
import com.seedcompany.cordtables.components.tables.common.workflows.CommonWorkflowsUpdateRequest
import com.seedcompany.cordtables.components.tables.common.workflows.workflow
import com.seedcompany.cordtables.components.tables.common.workflows.workflowInput
import com.seedcompany.cordtables.components.tables.sc.locations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonWorkflowsUpdateReadRequest(
    val token: String?,
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class CommonWorkflowsUpdateReadResponse(
    val error: ErrorType,
    val workflow: workflow? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonWorkflowsUpdateRead")
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
    @PostMapping("common/workflows/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: CommonWorkflowsUpdateReadRequest): CommonWorkflowsUpdateReadResponse {

        val updateResponse = update.updateHandler(
            CommonWorkflowsUpdateRequest(
                token = req.token,
                column = req.column,
                id = req.id,
                value = req.value,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return CommonWorkflowsUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
            CommonWorkflowsReadRequest(
                token = req.token,
                id = req.id!!
            )
        )

        return CommonWorkflowsUpdateReadResponse(error = readResponse.error, readResponse.workflow)
    }
}
