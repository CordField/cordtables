package com.seedcompany.cordtables.components.tables.common.stage_role_column_grants

import com.seedcompany.cordtables.components.tables.common.stage_role_column_grants.Delete as CommonDelete
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.stage_role_column_grants.CommonStageRoleColumnGrantsDeleteRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource

data class CommonStageRoleColumnGrantsDeleteRequest(
    val id: Int,
    val token: String?,
)

data class CommonStageRoleColumnGrantsDeleteResponse(
    val error: ErrorType,
    val id: Int?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("CommonStageRoleColumnGrantsDelete")
class Delete(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("common-stage-role-column-grants/delete")
    @ResponseBody
    fun deleteHandler(@RequestBody req: CommonStageRoleColumnGrantsDeleteRequest): CommonStageRoleColumnGrantsDeleteResponse {

        if (req.token == null) return CommonStageRoleColumnGrantsDeleteResponse(ErrorType.TokenNotFound, null)
        if(!util.userHasDeletePermission(req.token, "common.stage_role_column_grants"))
            return CommonStageRoleColumnGrantsDeleteResponse(ErrorType.DoesNotHaveDeletePermission, null)

        println("req: $req")
        var deletedLocationExId: Int? = null

        this.ds.connection.use { conn ->
            try {

                val deleteStatement = conn.prepareCall(
                    "delete from common.stage_role_column_grants where id = ? returning id"
                )
                deleteStatement.setInt(1, req.id)

                deleteStatement.setInt(1,req.id)


                val deleteStatementResult = deleteStatement.executeQuery()

                if (deleteStatementResult.next()) {
                    deletedLocationExId  = deleteStatementResult.getInt("id")
                }
            }
            catch (e:SQLException ){
                println(e.message)

                return CommonStageRoleColumnGrantsDeleteResponse(ErrorType.SQLDeleteError, null)
            }
        }

        return CommonStageRoleColumnGrantsDeleteResponse(ErrorType.NoError,deletedLocationExId)
    }
}