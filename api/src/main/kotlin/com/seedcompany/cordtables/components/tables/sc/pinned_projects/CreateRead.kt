package com.seedcompany.cordtables.components.tables.sc.pinned_projects

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.pinned_projects.*
import com.seedcompany.cordtables.components.tables.sc.pinned_projects.ScPinnedProjectsCreateRequest
import com.seedcompany.cordtables.components.tables.sc.pinned_projects.Create
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScPinnedProjectsCreateReadRequest(
    val token: String? = null,
    val pinnedProject: pinnedProjectInput,
)

data class ScPinnedProjectsCreateReadResponse(
    val error: ErrorType,
    val pinnedProject: pinnedProject? = null,
)


@Controller("ScPinnedProjectsCreateRead")
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
    @PostMapping("sc-pinned-projects/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: ScPinnedProjectsCreateReadRequest): ScPinnedProjectsCreateReadResponse {

        val createResponse = create.createHandler(
            ScPinnedProjectsCreateRequest(
                token = req.token,
                pinnedProject = req.pinnedProject
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return ScPinnedProjectsCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            ScPinnedProjectsReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return ScPinnedProjectsCreateReadResponse(error = readResponse.error, pinnedProject = readResponse.pinnedProject)
    }
}