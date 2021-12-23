package com.seedcompany.cordtables.components.tables.common.people_to_org_relationships

import com.seedcompany.cordtables.components.tables.common.people_to_org_relationships.Delete as CommonDelete
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.people_to_org_relationships.CommonPeopleToOrgRelationshipsDeleteRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource

data class CommonPeopleToOrgRelationshipsDeleteRequest(
    val id: String,
    val token: String?,
)

data class CommonPeopleToOrgRelationshipsDeleteResponse(
    val error: ErrorType,
    val id: String?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonPeopleToOrgRelationshipsDelete")
class Delete(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("common/people-to-org-relationships/delete")
    @ResponseBody
    fun deleteHandler(@RequestBody req: CommonPeopleToOrgRelationshipsDeleteRequest): CommonPeopleToOrgRelationshipsDeleteResponse {

        if (req.token == null) return CommonPeopleToOrgRelationshipsDeleteResponse(ErrorType.TokenNotFound, null)
        if(!util.userHasDeletePermission(req.token, "common.people_to_org_relationships"))
            return CommonPeopleToOrgRelationshipsDeleteResponse(ErrorType.DoesNotHaveDeletePermission, null)

        println("req: $req")
        var deletedLocationExId: String? = null

        this.ds.connection.use { conn ->
            try {

                val deleteStatement = conn.prepareCall(
                    "delete from common.people_to_org_relationships where id = ? returning id"
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

                return CommonPeopleToOrgRelationshipsDeleteResponse(ErrorType.SQLDeleteError, null)
            }
        }

        return CommonPeopleToOrgRelationshipsDeleteResponse(ErrorType.NoError,deletedLocationExId)
    }
}
