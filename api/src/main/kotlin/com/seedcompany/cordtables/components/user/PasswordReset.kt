package com.seedcompany.cordtables.components.user


import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Regions
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder
import com.amazonaws.services.simpleemail.model.*
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.core.AppConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.DependsOn
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseBody
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import javax.sql.DataSource

data class PasswordResetRequest(
  val email: String
)

data class PasswordResetReturn(
  val errorType: ErrorType,
  val email: String?,
  val avatar: String?,
)

@Component
@DependsOn("BootstrapDB")
class PasswordReset(
  @Autowired
  val ds: DataSource,
  @Autowired
  val appConfig: AppConfig,
  @Autowired
  val util: Utility,
  @Autowired
  val publisher: ApplicationEventPublisher

) {

//  @EventListener
//  fun passwordReset(event: UserResetPasswordRequest) {
//
//    val result = passwordResetFn(event.token, event.password)
//
//    publisher.publishEvent(
//      UserResetPasswordResponse(
//        error = result.errorType,
//        email = result.email,
//        avatar = result.avatar,
//        sessionId = event.sessionId,
//        type = MessageType.UserResetPasswordResponse,
//      )
//    )
//
//  }


//  @PostMapping(path=["user/login"], consumes = ["application/json"], produces = ["application/json"])
//  @ResponseBody
  fun passwordResetFn(token: String, password: String): PasswordResetReturn {

    if (token.length < 64) return PasswordResetReturn(ErrorType.InvalidToken, null, null)
    if (password.length < 8) return PasswordResetReturn(ErrorType.PasswordTooShort, null, null)
    if (password.length > 32) return PasswordResetReturn(ErrorType.PasswordTooLong, null, null)

    var email: String? = null
    var avatar: String? = null

    val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

    val newToken = (1..64)
      .map { i -> kotlin.random.Random.nextInt(0, charPool.size) }
      .map(charPool::get)
      .joinToString("")

    var errorType = ErrorType.UnknownError

    //language=SQL
    val registerUserSql = """
            CALL password_reset(?, ?, ?, ?, ?, ?);
        """.trimIndent()

    this.ds.connection.use { conn ->
      val statement = conn.prepareCall(registerUserSql);
      statement.setString(1, token)
      statement.setString(2, newToken)
      statement.setString(3, password)
      statement.setNull(4, java.sql.Types.NULL)
      statement.setNull(5, java.sql.Types.NULL)
      statement.setString(6, errorType.name)

      statement.registerOutParameter(4, java.sql.Types.VARCHAR)
      statement.registerOutParameter(5, java.sql.Types.VARCHAR)
      statement.registerOutParameter(6, java.sql.Types.VARCHAR)

      statement.execute()

      try {
        errorType = ErrorType.valueOf(statement.getString(6))
        email = statement.getString(4)
        avatar = statement.getString(5)
      } catch (ex: IllegalArgumentException){
        errorType = ErrorType.UnknownError
      }

      if (errorType != ErrorType.NoError) {
        println("password reset query failed")
      }

      statement.close()
    }

    return PasswordResetReturn(
      errorType = errorType,
      email = email,
      avatar = avatar,
    )
  }
}
