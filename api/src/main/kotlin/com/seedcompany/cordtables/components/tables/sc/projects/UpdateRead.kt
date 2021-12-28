package com.seedcompany.cordtables.components.tables.sc.projects

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.projects.ScProjectsReadRequest
import com.seedcompany.cordtables.components.tables.sc.projects.ScProjectsUpdateRequest
import com.seedcompany.cordtables.components.tables.sc.projects.project
import com.seedcompany.cordtables.components.tables.sc.projects.projectInput
import com.seedcompany.cordtables.components.tables.sc.locations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScProjectsUpdateReadRequest(
    val token: String?,
    val id: Int? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class ScProjectsUpdateReadResponse(
    val error: ErrorType,
    val project: project? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScProjectsUpdateRead")
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
    @PostMapping("sc/projects/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: ScProjectsUpdateReadRequest): ScProjectsUpdateReadResponse {

        val updateResponse = update.updateHandler(
            ScProjectsUpdateRequest(
                token = req.token,
                column = req.column,
                id = req.id,
                value = req.value,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return ScProjectsUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
            ScProjectsReadRequest(
                token = req.token,
                id = req.id!!
            )
        )

        return ScProjectsUpdateReadResponse(error = readResponse.error, readResponse.project)
    }
}
