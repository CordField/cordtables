package com.seedcompany.cordtables.components.tables.sc.field_regions

import com.seedcompany.cordtables.components.tables.sc.field_regions.Delete as CommonDelete
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.field_regions.ScFieldRegionsDeleteRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource

data class ScFieldRegionsDeleteRequest(
    val id: Int,
    val token: String?,
)

data class ScFieldRegionsDeleteResponse(
    val error: ErrorType,
    val id: Int?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScFieldRegionsDelete")
class Delete(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("sc/field-regions/delete")
    @ResponseBody
    fun deleteHandler(@RequestBody req: ScFieldRegionsDeleteRequest): ScFieldRegionsDeleteResponse {

        if (req.token == null) return ScFieldRegionsDeleteResponse(ErrorType.TokenNotFound, null)
        if(!util.userHasDeletePermission(req.token, "sc.field_regions"))
            return ScFieldRegionsDeleteResponse(ErrorType.DoesNotHaveDeletePermission, null)

        println("req: $req")
        var deletedLocationExId: Int? = null

        this.ds.connection.use { conn ->
            try {

                val deleteStatement = conn.prepareCall(
                    "delete from sc.field_regions where id = ? returning id"
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

                return ScFieldRegionsDeleteResponse(ErrorType.SQLDeleteError, null)
            }
        }

        return ScFieldRegionsDeleteResponse(ErrorType.NoError,deletedLocationExId)
    }
}
