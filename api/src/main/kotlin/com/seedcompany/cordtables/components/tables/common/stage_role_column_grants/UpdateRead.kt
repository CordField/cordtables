package com.seedcompany.cordtables.components.tables.common.stage_role_column_grants

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.stage_role_column_grants.CommonStageRoleColumnGrantsReadRequest
import com.seedcompany.cordtables.components.tables.common.stage_role_column_grants.CommonStageRoleColumnGrantsUpdateRequest
import com.seedcompany.cordtables.components.tables.common.stage_role_column_grants.stageRoleColumnGrant
import com.seedcompany.cordtables.components.tables.common.stage_role_column_grants.stageRoleColumnGrantInput
import com.seedcompany.cordtables.components.tables.sc.locations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonStageRoleColumnGrantsUpdateReadRequest(
    val token: String?,
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class CommonStageRoleColumnGrantsUpdateReadResponse(
    val error: ErrorType,
    val stageRoleColumnGrant: stageRoleColumnGrant? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonStageRoleColumnGrantsUpdateRead")
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
    @PostMapping("common/stage-role-column-grants/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: CommonStageRoleColumnGrantsUpdateReadRequest): CommonStageRoleColumnGrantsUpdateReadResponse {

        val updateResponse = update.updateHandler(
            CommonStageRoleColumnGrantsUpdateRequest(
                token = req.token,
                column = req.column,
                id = req.id,
                value = req.value,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return CommonStageRoleColumnGrantsUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
            CommonStageRoleColumnGrantsReadRequest(
                token = req.token,
                id = req.id!!
            )
        )

        return CommonStageRoleColumnGrantsUpdateReadResponse(error = readResponse.error, readResponse.stageRoleColumnGrant)
    }
}
