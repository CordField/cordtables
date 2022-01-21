package com.seedcompany.cordtables.components.user

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class RegisterRequest(
    val email: String? = null,
    val password: String? = null,
)

data class RegisterReturn(
    val error: ErrorType,
    val token: String? = null,
    val readableTables: List<String> = listOf(),
    val isAdmin: Boolean = false,
    val userId: String? = null
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller()
class Register (
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
){


    @PostMapping("user/register")
    @ResponseBody
    fun registerHandler(@RequestBody req: RegisterRequest):RegisterReturn{

        if (req.email == null || !util.isEmailValid(req.email)) return RegisterReturn(ErrorType.InvalidEmail)
        if (req.password == null || req.password.length < 8) return RegisterReturn(ErrorType.PasswordTooShort)
        if (req.password.length > 32) return RegisterReturn(ErrorType.PasswordTooLong)


        var errorType: ErrorType
        var userId: String?
        val pash = util.encoder.encode(req.password)
        val token = util.createToken()

      val adminGroupId = util.adminGroupId()

        val resultList = registerDB(req.email, pash, token)
        errorType = resultList[0] as ErrorType
        userId = resultList[1] as String?
        if(userId == "") userId = null

        return if (errorType === ErrorType.NoError){
            RegisterReturn(errorType, token, util.getReadableTables(token), util.isAdmin(token), userId)
        } else {
            RegisterReturn(errorType)
        }

    }

    fun registerDB(email: String, pash: String, token: String): List<Any>{

        var errorType = ErrorType.UnknownError
        var userId:String = ""
        this.ds.connection.use{conn ->
            val statement = conn.prepareCall("call admin.register(?, ?, ?, ?, ?);")
            statement.setString(1, email)
            statement.setString(2, pash)
            statement.setString(3, token)
            statement.setString(4, errorType.name)
            statement.setString(5, null)
            statement.registerOutParameter(4, java.sql.Types.VARCHAR)
            statement.registerOutParameter(5, java.sql.Types.VARCHAR)

            statement.execute()

            try {
                errorType = ErrorType.valueOf(statement.getString(4))
                userId = statement.getString(5)
            } catch (ex: IllegalArgumentException) {
                errorType = ErrorType.UnknownError
            }

            if (errorType != ErrorType.NoError) {
                println("register query failed")
            }

            statement.close()
        }

        return  listOf<Any>(errorType,userId as Any)
    }
}
