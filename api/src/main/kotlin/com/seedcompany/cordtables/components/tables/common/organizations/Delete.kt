package com.seedcompany.cordtables.components.tables.common.organizations

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource

data class CommonOrganizationsDeleteRequest(
        val id: Int,
        val token: String?,
)

data class CommonOrganizationsDeleteResponse(
        val error: ErrorType,
        val id: Int?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonOrganizationsDelete")
class Delete(
        @Autowired
        val util: Utility,
        @Autowired
        val ds: DataSource,
) {
    @PostMapping("common/organizations/delete")
    @ResponseBody
    fun deleteHandler(@RequestBody req: CommonOrganizationsDeleteRequest): CommonOrganizationsDeleteResponse {

        if (req.token == null) return CommonOrganizationsDeleteResponse(ErrorType.TokenNotFound, null)
        if(!util.userHasDeletePermission(req.token, "common.organizations"))
            return CommonOrganizationsDeleteResponse(ErrorType.DoesNotHaveDeletePermission, null)

        var deletedOrganizationId: Int? = null

        this.ds.connection.use { conn ->
            try {

                val deleteStatement = conn.prepareCall(
                        "delete from common.organizations where id = ? returning id"
                )
                deleteStatement.setInt(1, req.id)

                deleteStatement.setInt(1,req.id)


                val deleteStatementResult = deleteStatement.executeQuery()

                if (deleteStatementResult.next()) {
                    deletedOrganizationId  = deleteStatementResult.getInt("id")
                }
            }
            catch (e:SQLException ){
                println(e.message)

                return CommonOrganizationsDeleteResponse(ErrorType.SQLDeleteError, null)
            }
        }
        return CommonOrganizationsDeleteResponse(ErrorType.NoError,deletedOrganizationId)
    }
}
