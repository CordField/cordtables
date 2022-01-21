package com.seedcompany.cordtables.components.tables.sc.field_zones

import com.seedcompany.cordtables.components.tables.sc.field_zones.Delete as CommonDelete
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.field_zones.ScFieldZonesDeleteRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource

data class ScFieldZonesDeleteRequest(
    val id: String,
    val token: String?,
)

data class ScFieldZonesDeleteResponse(
    val error: ErrorType,
    val id: String?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScFieldZonesDelete")
class Delete(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("sc/field-zones/delete")
    @ResponseBody
    fun deleteHandler(@RequestBody req: ScFieldZonesDeleteRequest): ScFieldZonesDeleteResponse {

        if (req.token == null) return ScFieldZonesDeleteResponse(ErrorType.TokenNotFound, null)
        if(!util.userHasDeletePermission(req.token, "sc.field_zones"))
            return ScFieldZonesDeleteResponse(ErrorType.DoesNotHaveDeletePermission, null)

        println("req: $req")
        var deletedLocationExId: String? = null

        this.ds.connection.use { conn ->
            try {

                val deleteStatement = conn.prepareCall(
                    "delete from sc.field_zones where id = ? returning id"
                )
                deleteStatement.setString(1, req.id)

                deleteStatement.setString(1,req.id)


                val deleteStatementResult = deleteStatement.executeQuery()

                if (deleteStatementResult.next()) {
                    deletedLocationExId  = deleteStatementResult.getString("id")
                }
            }
            catch (e:SQLException ){
                println(e.message)

                return ScFieldZonesDeleteResponse(ErrorType.SQLDeleteError, null)
            }
        }

        return ScFieldZonesDeleteResponse(ErrorType.NoError,deletedLocationExId)
    }
}
