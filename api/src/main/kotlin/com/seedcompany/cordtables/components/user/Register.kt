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
    val readableTables: MutableList<String> = mutableListOf(),
    val isAdmin: Boolean = false
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
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

        val pash = util.encoder.encode(req.password)
        val token = util.createToken()

        val result = registerDB(req.email, pash, token)

        return if (result === ErrorType.NoError){
            RegisterReturn(result, token, util.getReadableTables(token), util.isAdmin(token))
        } else {
            RegisterReturn(result)
        }

    }

    fun registerDB(email: String, pash: String, token: String): ErrorType{

        var errorType = ErrorType.UnknownError

        this.ds.connection.use{conn ->
            val statement = conn.prepareCall("call admin.register(?, ?, ?, ?);")
            statement.setString(1, email)
            statement.setString(2, pash)
            statement.setString(3, token)
            statement.setString(4, errorType.name)
            statement.registerOutParameter(4, java.sql.Types.VARCHAR)

            statement.execute()

            try {
                errorType = ErrorType.valueOf(statement.getString(4))
            } catch (ex: IllegalArgumentException) {
                errorType = ErrorType.UnknownError
            }

            if (errorType != ErrorType.NoError) {
                println("register query failed")
            }

            statement.close()
        }

        return errorType
    }
}