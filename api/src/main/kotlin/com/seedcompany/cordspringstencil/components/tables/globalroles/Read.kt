package com.seedcompany.cordspringstencil.components.user

import com.seedcompany.cordspringstencil.common.ErrorType
import com.seedcompany.cordspringstencil.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.management.relation.Role
import javax.sql.DataSource
import kotlin.reflect.jvm.internal.impl.load.java.JavaClassFinder

//data class RolesRequest()
data class GlobalRole(
    val id: Int,
    val createdAt: String,
    val createdBy: Int,
    val modifiedAt: String,
    val modifiedBy: Int,
    val name: String,
    val org: Int
)

data class RolesResponse(
    val error: ErrorType?  = null,
    val data: MutableList<GlobalRole>
//    val token: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333"])
@Controller()
class Read(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("globalroles/read")
    @ResponseBody
    fun ReadHandler(): RolesResponse {

//        if (req.email == null || !util.isEmailValid(req.email)) return LoginReturn(ErrorType.InvalidEmail)
//        if (req.password == null || req.password.length < 8) return LoginReturn(ErrorType.PasswordTooShort)
//        if (req.password.length > 32) return LoginReturn(ErrorType.PasswordTooLong)

//        var response = RolesResponse(ErrorType.UnknownError)
        var token: String? = null
        var errorType = ErrorType.UnknownError
        var data: MutableList<GlobalRole> = mutableListOf()


        this.ds.connection.use { conn ->
            val pashStatement = conn.prepareCall(
                "select id, created_at, created_by,modified_at,modified_by, name, org from public.global_roles;"
            )
//            pashStatement.setString(1, req.email)

            val getPashResult = pashStatement.executeQuery()
//            var pash: String? = null
            while (getPashResult.next()) {
//                pash = getPashResult.getString("password")

                val id = getPashResult.getInt("id")
                val name = getPashResult.getString("name")
                val createdBy = getPashResult.getInt("created_by")
                val modifiedBy = getPashResult.getInt("modified_by")
                val org = getPashResult.getInt("org")
                val createdAt = getPashResult.getString("created_at")
                val modifiedAt = getPashResult.getString("modified_at")
                data.add(GlobalRole(id,createdAt,createdBy,modifiedAt,modifiedBy,name,org))
            }
        }
        var response = RolesResponse(null,data)
        return response
    }

//    fun loginDB(email: String, token: String): ErrorType {
//
//        var errorType = ErrorType.UnknownError
//
//        this.ds.connection.use { conn ->
//            val statement = conn.prepareCall("call Login(?, ?, ?);")
//            statement.setString(1, email)
//            statement.setString(2, token)
//            statement.setString(3, errorType.name)
//            statement.registerOutParameter(3, java.sql.Types.VARCHAR)
//
//            statement.execute()
//
//            try {
//                errorType = ErrorType.valueOf(statement.getString(3))
//            } catch (ex: IllegalArgumentException) {
//                errorType = ErrorType.UnknownError
//            }
//
//            if (errorType != ErrorType.NoError) {
//                println("Login query failed")
//            }
//
//            statement.close()
//        }
//
//        return errorType
//    }
}