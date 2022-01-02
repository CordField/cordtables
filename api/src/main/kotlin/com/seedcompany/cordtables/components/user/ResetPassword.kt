package com.seedcompany.cordtables.components.user

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.admin.users.user
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource

data class ValidateTokenRequest(
    val token: String? = null
)

data class ValidateTokenResponse(
    val error: ErrorType,
    val  valid: Boolean = false
)

data class ResetPasswordRequest(
    val token: String? = null,
    val newPassword: String? = null,
    val confirmPassword: String? = null
)

data class ResetPasswordResponse(
    val error: ErrorType
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ResetPasswordController")
class ResetPassword (
  @Autowired
  val util: Utility,

  @Autowired
  val ds: DataSource,
) {
    val jdbcTemplate: JdbcTemplate = JdbcTemplate(ds)

    @PostMapping(path=["user/validate-token"], consumes = ["application/json"], produces = ["application/json"])
    @ResponseBody
    fun validateToken(@RequestBody req: ValidateTokenRequest): ValidateTokenResponse {
        var valid: Boolean = false
        var error: ErrorType = ErrorType.NoError
        if ( req.token == null) return ValidateTokenResponse(error = ErrorType.InvalidToken)

        var userId: String? = this.isTokenValid(req.token)
        if (userId != null){
            valid = true
        }
        else{
            error = ErrorType.InvalidToken
        }
        return ValidateTokenResponse(error = error, valid = valid)
    }

    @PostMapping(path = ["user/reset-password"], consumes = ["application/json"], produces = ["application/json"])
    @ResponseBody
    fun resetPassword(@RequestBody req: ResetPasswordRequest): ResetPasswordResponse{
        if (req.token == null) return ResetPasswordResponse(error = ErrorType.InvalidToken)
        if (req.newPassword == null) return ResetPasswordResponse(error = ErrorType.PasswordTooShort)
        if (req.newPassword == null || req.newPassword.length < 8) return ResetPasswordResponse(ErrorType.PasswordTooShort)
        if (req.newPassword.length > 32) return ResetPasswordResponse(ErrorType.PasswordTooLong)
        if (req.newPassword != req.confirmPassword) return ResetPasswordResponse(error = ErrorType.PasswordTooShort)
        val userId: String? = this.isTokenValid(req.token)
        if (userId != null){
             return try {
                  var passHash: String = util.encoder.encode(req.newPassword)
                  jdbcTemplate.update("""
                          UPDATE admin.users SET password = ? WHERE id=?::uuid
                      """.trimIndent(),
                      passHash,
                      userId
                  )
                  ResetPasswordResponse(error = ErrorType.NoError)
             } catch (e: SQLException) {
                  ResetPasswordResponse(error = ErrorType.SQLUpdateError)
             }
        }
        else{
            return ResetPasswordResponse(error = ErrorType.InvalidToken)
        }
    }

    fun isTokenValid(token: String): String? {
        var valid:Boolean = false
        var userId: String? = null
        this.ds.connection.use { conn ->
            try {
                var tokenStatement = conn.prepareCall("""
                    SELECT id, user_id FROM admin.email_tokens WHERE token = ?
                """.trimIndent())
                tokenStatement.setString(1, token)

                var result = tokenStatement.executeQuery()
                if (result.next()){
                    valid = true
                    userId = result.getString("id")
                }
            }
            catch (e: SQLException){
                println("SQL Error")
            }
        }
        return userId
    }

}
