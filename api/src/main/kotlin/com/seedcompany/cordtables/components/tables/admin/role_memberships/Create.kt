package com.seedcompany.cordtables.components.tables.globalrolememberships

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

data class GlobalRoleMembershipsCreateRequest(
        val token: String? = null,
        val role: Int,
        val person: Int,
        val owning_group: Int,
)

data class GlobalRoleMembershipsCreateReturn(
        val error: ErrorType,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("GlobalRoleMembershipsCreate")
class Create(
        @Autowired
        val util: Utility,

        @Autowired
        val ds: DataSource,
) {

    @PostMapping("role-memberships/create")
    @ResponseBody
    fun createHandler(@RequestBody req: GlobalRoleMembershipsCreateRequest): GlobalRoleMembershipsCreateReturn {

        if (req.token == null) return GlobalRoleMembershipsCreateReturn(ErrorType.TokenNotFound)
        if (!util.isAdmin(req.token)) return GlobalRoleMembershipsCreateReturn(ErrorType.AdminOnly)

        var errorType = ErrorType.UnknownError
        var userId = 0

        this.ds.connection.use { conn ->
            try {
                val getUserIdStatement = conn.prepareCall("select person from admin.tokens where token = ?")
                getUserIdStatement.setString(1, req.token)
                val getUserIdResult = getUserIdStatement.executeQuery()
                if (getUserIdResult.next()) {
                    userId = getUserIdResult.getInt("person")
                    println("userId: $userId")
                }
                else {
                    throw SQLException("User not found")
                }
            }
            catch(e: SQLException){
                println(e.message)
                errorType = ErrorType.UserTokenNotFound
                return GlobalRoleMembershipsCreateReturn(errorType)
            }

            val statement = conn.prepareStatement(
                    """
                        insert into admin.role_memberships(role, person, created_by, modified_by, owning_person, owning_group) 
                            values(?, ? , ? ,? , ? ,?);
                        """.trimIndent()
            )

            statement.setInt(1, req.role)
            statement.setInt(2, req.person)
            statement.setInt(3, userId)
            statement.setInt(4, userId)
            statement.setInt(5, userId)
            statement.setInt(6, req.owning_group)

            statement.execute()

        }

        return GlobalRoleMembershipsCreateReturn(ErrorType.NoError)
    }

}