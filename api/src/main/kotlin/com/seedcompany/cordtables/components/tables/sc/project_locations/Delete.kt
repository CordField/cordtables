package com.seedcompany.cordtables.components.tables.sc.project_locations

import com.seedcompany.cordtables.components.tables.sc.project_locations.Delete as CommonDelete
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.project_locations.ScProjectLocationsDeleteRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource

data class ScProjectLocationsDeleteRequest(
    val id: String,
    val token: String?,
)

data class ScProjectLocationsDeleteResponse(
    val error: ErrorType,
    val id: String?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScProjectLocationsDelete")
class Delete(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("sc/project-locations/delete")
    @ResponseBody
    fun deleteHandler(@RequestBody req: ScProjectLocationsDeleteRequest): ScProjectLocationsDeleteResponse {

        if (req.token == null) return ScProjectLocationsDeleteResponse(ErrorType.TokenNotFound, null)
        if(!util.userHasDeletePermission(req.token, "sc.project_locations"))
            return ScProjectLocationsDeleteResponse(ErrorType.DoesNotHaveDeletePermission, null)

        println("req: $req")
        var deletedLocationExId: String? = null

        this.ds.connection.use { conn ->
            try {

                val deleteStatement = conn.prepareCall(
                    "delete from sc.project_locations where id = ? returning id"
                )
                deleteStatement.setString(1, req.id)
                val deleteStatementResult = deleteStatement.executeQuery()

                if (deleteStatementResult.next()) {
                    deletedLocationExId  = deleteStatementResult.getString("id")
                }
            }
            catch (e:SQLException ){
                println(e.message)

                return ScProjectLocationsDeleteResponse(ErrorType.SQLDeleteError, null)
            }
        }

        return ScProjectLocationsDeleteResponse(ErrorType.NoError,deletedLocationExId)
    }
}
