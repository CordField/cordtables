package com.seedcompany.cordtables.components.tables.common.workflows

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.workflows.*
import com.seedcompany.cordtables.components.tables.common.workflows.CommonWorkflowsCreateRequest
import com.seedcompany.cordtables.components.tables.common.workflows.Create
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonWorkflowsCreateReadRequest(
    val token: String? = null,
    val workflow: workflowInput,
)

data class CommonWorkflowsCreateReadResponse(
    val error: ErrorType,
    val workflow: workflow? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonWorkflowsCreateRead")
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
    @PostMapping("common-workflows/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: CommonWorkflowsCreateReadRequest): CommonWorkflowsCreateReadResponse {

        val createResponse = create.createHandler(
            CommonWorkflowsCreateRequest(
                token = req.token,
                workflow = req.workflow
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return CommonWorkflowsCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            CommonWorkflowsReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return CommonWorkflowsCreateReadResponse(error = readResponse.error, workflow = readResponse.workflow)
    }
}