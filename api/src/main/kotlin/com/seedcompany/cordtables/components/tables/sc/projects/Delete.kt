package com.seedcompany.cordtables.components.tables.sc.projects

import com.seedcompany.cordtables.components.tables.sc.projects.Delete as CommonDelete
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.projects.ScProjectsDeleteRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource

data class ScProjectsDeleteRequest(
    val id: Int,
    val token: String?,
)

data class ScProjectsDeleteResponse(
    val error: ErrorType,
    val id: Int?
)


@Controller("ScProjectsDelete")
class Delete(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("sc-projects/delete")
    @ResponseBody
    fun deleteHandler(@RequestBody req: ScProjectsDeleteRequest): ScProjectsDeleteResponse {

        if (req.token == null) return ScProjectsDeleteResponse(ErrorType.TokenNotFound, null)
        if(!util.userHasDeletePermission(req.token, "sc.projects"))
            return ScProjectsDeleteResponse(ErrorType.DoesNotHaveDeletePermission, null)

        println("req: $req")
        var deletedLocationExId: Int? = null

        this.ds.connection.use { conn ->
            try {

                val deleteStatement = conn.prepareCall(
                    "delete from sc.projects where id = ? returning id"
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

                return ScProjectsDeleteResponse(ErrorType.SQLDeleteError, null)
            }
        }

        return ScProjectsDeleteResponse(ErrorType.NoError,deletedLocationExId)
    }
}