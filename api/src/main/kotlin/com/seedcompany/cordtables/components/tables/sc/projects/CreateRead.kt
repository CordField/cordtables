package com.seedcompany.cordtables.components.tables.sc.projects

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.projects.*
import com.seedcompany.cordtables.components.tables.sc.projects.ScProjectsCreateRequest
import com.seedcompany.cordtables.components.tables.sc.projects.Create
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScProjectsCreateReadRequest(
    val token: String? = null,
    val project: projectInput,
)

data class ScProjectsCreateReadResponse(
    val error: ErrorType,
    val project: project? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("ScProjectsCreateRead")
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
    @PostMapping("sc-projects/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: ScProjectsCreateReadRequest): ScProjectsCreateReadResponse {

        val createResponse = create.createHandler(
            ScProjectsCreateRequest(
                token = req.token,
                project = req.project
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return ScProjectsCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            ScProjectsReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return ScProjectsCreateReadResponse(error = readResponse.error, project = readResponse.project)
    }
}