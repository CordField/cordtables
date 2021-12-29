package com.seedcompany.cordtables.components.tables.common.people_graph

import com.seedcompany.cordtables.components.tables.common.people_graph.Delete as CommonDelete
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.people_graph.CommonPeopleGraphDeleteRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource

data class CommonPeopleGraphDeleteRequest(
    val id: Int,
    val token: String?,
)

data class CommonPeopleGraphDeleteResponse(
    val error: ErrorType,
    val id: Int?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonPeopleGraphDelete")
class Delete(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("common/people-graph/delete")
    @ResponseBody
    fun deleteHandler(@RequestBody req: CommonPeopleGraphDeleteRequest): CommonPeopleGraphDeleteResponse {

        if (req.token == null) return CommonPeopleGraphDeleteResponse(ErrorType.TokenNotFound, null)
        if(!util.userHasDeletePermission(req.token, "common.people_graph"))
            return CommonPeopleGraphDeleteResponse(ErrorType.DoesNotHaveDeletePermission, null)

        println("req: $req")
        var deletedLocationExId: Int? = null

        this.ds.connection.use { conn ->
            try {

                val deleteStatement = conn.prepareCall(
                    "delete from common.people_graph where id = ? returning id"
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

                return CommonPeopleGraphDeleteResponse(ErrorType.SQLDeleteError, null)
            }
        }

        return CommonPeopleGraphDeleteResponse(ErrorType.NoError,deletedLocationExId)
    }
}
