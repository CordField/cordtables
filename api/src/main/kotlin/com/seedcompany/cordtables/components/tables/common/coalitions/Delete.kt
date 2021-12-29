package com.seedcompany.cordtables.components.tables.common.coalitions

import com.seedcompany.cordtables.components.tables.common.coalitions.Delete as CommonDelete
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.coalitions.CommonCoalitionsDeleteRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource

data class CommonCoalitionsDeleteRequest(
    val id: Int,
    val token: String?,
)

data class CommonCoalitionsDeleteResponse(
    val error: ErrorType,
    val id: Int?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonCoalitionsDelete")
class Delete(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("common/coalitions/delete")
    @ResponseBody
    fun deleteHandler(@RequestBody req: CommonCoalitionsDeleteRequest): CommonCoalitionsDeleteResponse {

        if (req.token == null) return CommonCoalitionsDeleteResponse(ErrorType.TokenNotFound, null)
        if(!util.userHasDeletePermission(req.token, "common.coalitions"))
            return CommonCoalitionsDeleteResponse(ErrorType.DoesNotHaveDeletePermission, null)

        println("req: $req")
        var deletedLocationExId: Int? = null

        this.ds.connection.use { conn ->
            try {

                val deleteStatement = conn.prepareCall(
                    "delete from common.coalitions where id = ? returning id"
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

                return CommonCoalitionsDeleteResponse(ErrorType.SQLDeleteError, null)
            }
        }

        return CommonCoalitionsDeleteResponse(ErrorType.NoError,deletedLocationExId)
    }
}
