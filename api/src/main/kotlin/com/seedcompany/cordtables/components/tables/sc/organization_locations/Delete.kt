package com.seedcompany.cordtables.components.tables.sc.organization_locations

import com.seedcompany.cordtables.components.tables.sc.organization_locations.Delete as CommonDelete
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.organization_locations.ScOrganizationLocationsDeleteRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource

data class ScOrganizationLocationsDeleteRequest(
    val id: Int,
    val token: String?,
)

data class ScOrganizationLocationsDeleteResponse(
    val error: ErrorType,
    val id: Int?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScOrganizationLocationsDelete")
class Delete(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("sc/organization-locations/delete")
    @ResponseBody
    fun deleteHandler(@RequestBody req: ScOrganizationLocationsDeleteRequest): ScOrganizationLocationsDeleteResponse {

        if (req.token == null) return ScOrganizationLocationsDeleteResponse(ErrorType.TokenNotFound, null)
        if(!util.userHasDeletePermission(req.token, "sc.organization_locations"))
            return ScOrganizationLocationsDeleteResponse(ErrorType.DoesNotHaveDeletePermission, null)

        println("req: $req")
        var deletedLocationExId: Int? = null

        this.ds.connection.use { conn ->
            try {

                val deleteStatement = conn.prepareCall(
                    "delete from sc.organization_locations where id = ? returning id"
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

                return ScOrganizationLocationsDeleteResponse(ErrorType.SQLDeleteError, null)
            }
        }

        return ScOrganizationLocationsDeleteResponse(ErrorType.NoError,deletedLocationExId)
    }
}
