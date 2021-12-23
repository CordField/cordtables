package com.seedcompany.cordtables.components.tables.sc.pinned_projects

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.pinned_projects.ScPinnedProjectsReadRequest
import com.seedcompany.cordtables.components.tables.sc.pinned_projects.ScPinnedProjectsUpdateRequest
import com.seedcompany.cordtables.components.tables.sc.pinned_projects.pinnedProject
import com.seedcompany.cordtables.components.tables.sc.pinned_projects.pinnedProjectInput
import com.seedcompany.cordtables.components.tables.sc.locations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScPinnedProjectsUpdateReadRequest(
    val token: String?,
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class ScPinnedProjectsUpdateReadResponse(
    val error: ErrorType,
    val pinnedProject: pinnedProject? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScPinnedProjectsUpdateRead")
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
    @PostMapping("sc-pinned-projects/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: ScPinnedProjectsUpdateReadRequest): ScPinnedProjectsUpdateReadResponse {

        val updateResponse = update.updateHandler(
            ScPinnedProjectsUpdateRequest(
                token = req.token,
                column = req.column,
                id = req.id,
                value = req.value,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return ScPinnedProjectsUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
            ScPinnedProjectsReadRequest(
                token = req.token,
                id = req.id!!
            )
        )

        return ScPinnedProjectsUpdateReadResponse(error = readResponse.error, readResponse.pinnedProject)
    }
}
