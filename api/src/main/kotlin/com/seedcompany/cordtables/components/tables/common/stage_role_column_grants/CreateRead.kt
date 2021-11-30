package com.seedcompany.cordtables.components.tables.common.stage_role_column_grants

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.stage_role_column_grants.*
import com.seedcompany.cordtables.components.tables.common.stage_role_column_grants.CommonStageRoleColumnGrantsCreateRequest
import com.seedcompany.cordtables.components.tables.common.stage_role_column_grants.Create
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonStageRoleColumnGrantsCreateReadRequest(
    val token: String? = null,
    val stageRoleColumnGrant: stageRoleColumnGrantInput,
)

data class CommonStageRoleColumnGrantsCreateReadResponse(
    val error: ErrorType,
    val stageRoleColumnGrant: stageRoleColumnGrant? = null,
)


@Controller("CommonStageRoleColumnGrantsCreateRead")
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
    @PostMapping("common-stage-role-column-grants/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: CommonStageRoleColumnGrantsCreateReadRequest): CommonStageRoleColumnGrantsCreateReadResponse {

        val createResponse = create.createHandler(
            CommonStageRoleColumnGrantsCreateRequest(
                token = req.token,
                stageRoleColumnGrant = req.stageRoleColumnGrant
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return CommonStageRoleColumnGrantsCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            CommonStageRoleColumnGrantsReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return CommonStageRoleColumnGrantsCreateReadResponse(error = readResponse.error, stageRoleColumnGrant = readResponse.stageRoleColumnGrant)
    }
}