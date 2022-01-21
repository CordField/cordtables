package com.seedcompany.cordtables.components.tables.admin.people

import com.seedcompany.cordtables.components.tables.admin.people.Delete as CommonDelete
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.admin.people.AdminPeopleDeleteRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource

data class AdminPeopleDeleteRequest(
    val id: String,
    val token: String?,
)

data class AdminPeopleDeleteResponse(
    val error: ErrorType,
    val id: String? = null
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("AdminPeopleDelete")
class Delete(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("admin/people/delete")
    @ResponseBody
    fun deleteHandler(@RequestBody req: AdminPeopleDeleteRequest): AdminPeopleDeleteResponse {

      if (req.token == null) return AdminPeopleDeleteResponse(ErrorType.InputMissingToken)
      if (!util.isAdmin(req.token)) return AdminPeopleDeleteResponse(ErrorType.AdminOnly)

        if(!util.userHasDeletePermission(req.token, "admin.people"))
            return AdminPeopleDeleteResponse(ErrorType.DoesNotHaveDeletePermission, null)

        println("req: $req")
        var deletedLocationExId: String? = null

        this.ds.connection.use { conn ->
            try {

                val deleteStatement = conn.prepareCall(
                    "delete from admin.people where id = ? returning id"
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

                return AdminPeopleDeleteResponse(ErrorType.SQLDeleteError, null)
            }
        }

        return AdminPeopleDeleteResponse(ErrorType.NoError,deletedLocationExId)
    }
}
