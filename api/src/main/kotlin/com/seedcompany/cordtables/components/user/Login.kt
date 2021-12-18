package com.seedcompany.cordtables.components.user

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.scripture_references.ScriptureReferenceCreateResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.io.File
import java.nio.file.Paths
import java.sql.SQLException
import javax.sql.DataSource

data class LoginRequest(
    val email: String? = null,
    val password: String? = null,
)

data class LoginReturn(
    val error: ErrorType,
    val token: String? = null,
    val readableTables: List<String> = listOf(),
    val isAdmin: Boolean = false,
    val userId: Int? = null
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller()
class Login (
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
){

    val encoder = Argon2PasswordEncoder(16, 32, 1, 4096, 3)

    @PostMapping(path=["user/login"], consumes = ["application/json"], produces = ["application/json"])
    @ResponseBody
    fun LoginHandler(@RequestBody req: LoginRequest):LoginReturn{

        if (req.email == null || !util.isEmailValid(req.email)) return LoginReturn(ErrorType.InvalidEmail)
        if (req.password == null || req.password.length < 8) return LoginReturn(ErrorType.PasswordTooShort)
        if (req.password.length > 32) return LoginReturn(ErrorType.PasswordTooLong)



        var response = LoginReturn(ErrorType.UnknownError)
        var token: String?
        var errorType: ErrorType
        var userId: Int?
//        val path = context.filesDir.absolutePath
//
//        println(home.resolve("src/Dockerfile").toAbsolutePath())
        this.ds.connection.use { conn ->
            try {
                val pashStatement = conn.prepareCall("select password from admin.users where email = ?;")
                pashStatement.setString(1, req.email)

                val getPashResult = pashStatement.executeQuery()

              println(pashStatement)
                var pash: String? = null
                if (getPashResult.next()) {
                    pash = getPashResult.getString("password")
                    val matches = encoder.matches(req.password, pash)
                    if (matches) {
                        token = util.createToken()

                        val returnList = loginDB(req.email, token!!)
                        errorType = returnList[0] as ErrorType
                        userId = returnList[1] as Int?
                        if(userId == -1) userId = null

                        if (errorType === ErrorType.NoError) {
                            response = LoginReturn(errorType, token, util.getReadableTables(token!!), util.isAdmin(token!!), userId)
                        }
                    } else {
                        response = LoginReturn(ErrorType.BadCredentials, null)
                    }
                } else {
                    response = LoginReturn(ErrorType.InvalidEmail, null)
                }
            }
            catch (e: SQLException){
              println("within exception catch")
                println(e.message)
                return LoginReturn(ErrorType.SQLInsertError, null)
            }
        }

        return response
    }

    fun loginDB(email: String, token: String): List<Any>{

        var errorType = ErrorType.UnknownError
        var userId:Int = -1

        this.ds.connection.use{conn ->
            val statement = conn.prepareCall("call admin.login(?, ?, ?,?);")
            statement.setString(1, email)
            statement.setString(2, token)
            statement.setString(3, errorType.name)
            statement.setInt(4,userId)

            statement.registerOutParameter(3, java.sql.Types.VARCHAR)
            statement.registerOutParameter(4, java.sql.Types.INTEGER)

            statement.execute()

            try {
                errorType = ErrorType.valueOf(statement.getString(3))
                userId = statement.getInt(4)
            } catch (ex: IllegalArgumentException) {
                throw ex
            }

            if (errorType != ErrorType.NoError) {
                println("Login query failed")
            }

            statement.close()
        }

        return listOf<Any>(errorType,userId as Any)
    }
}
