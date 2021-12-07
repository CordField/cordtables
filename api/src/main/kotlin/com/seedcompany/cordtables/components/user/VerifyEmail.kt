package com.seedcompany.cordtables.components.user

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.core.AppConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.DependsOn
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import javax.sql.DataSource


@Component
@DependsOn("BootstrapDB")
class VerifyEmail(
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
//  fun verifyEmail(event: UserVerifyEmailRequest) {
//
//    val result = verifyEmailFn(event.token)
//
//    if (result.errorType == ErrorType.EmailIsBlocked){
//      publisher.publishEvent(
//        UserVerifyEmailResponse(
//          error = result.errorType,
//          email = null,
//          avatar = null,
//          token = null,
//          sessionId = event.sessionId,
//          type = MessageType.UserVerifyEmailResponse,
//        )
//      )
//      return
//    }
//
//    var token: String? = null
//    if (result.errorType === ErrorType.NoError) {
//      // generate new token and replace in database
//      val newToken = util.createToken()
//
//      val replaceTokenSQL = """
//        update tokens
//        set token = ?
//        where token = (select token from sessions where session_id = ?);
//      """.trimIndent()
//
//      this.ds.connection.use { conn ->
//
//        val statement = conn.prepareCall(replaceTokenSQL);
//        statement.setString(1, newToken)
//        statement.setString(2, event.sessionId)
//
//        statement.executeQuery()
//
//        token = newToken
//
//      }
//    }
//
//    publisher.publishEvent(
//      UserVerifyEmailResponse(
//        error = result.errorType,
//        email = result.email,
//        avatar = result.avatar,
//        token = token,
//        sessionId = event.sessionId,
//        type = MessageType.UserVerifyEmailResponse,
//      )
//    )
//  }

  data class EmailVerifyReturn(
    val errorType: ErrorType,
    val email: String?,
    val avatar: String?,
  )

  fun verifyEmailFn(token: String): EmailVerifyReturn {

    var errorType = ErrorType.UnknownError
    var email: String? = null
    var avatar: String? = null

    //language=SQL
    val verifyEmailSQL = """
            CALL verify_email(?, ?, ?, ?);
        """.trimIndent()

    this.ds.connection.use { conn ->
      val statement = conn.prepareCall(verifyEmailSQL)

      statement.setString(1, token)
      statement.setNull(2, java.sql.Types.NULL)
      statement.setNull(3, java.sql.Types.NULL)
      statement.setString(4, errorType.name)
      statement.registerOutParameter(2, java.sql.Types.VARCHAR)
      statement.registerOutParameter(3, java.sql.Types.VARCHAR)
      statement.registerOutParameter(4, java.sql.Types.VARCHAR)

      statement.execute()

      try {
        errorType = ErrorType.valueOf(statement.getString(4))
        email = statement.getString(2)
        if (statement.wasNull()) {
          email = null
        }

        avatar = statement.getString(3)
        if (statement.wasNull()) {
          avatar = null
        }

      } catch (ex: IllegalArgumentException) {
        errorType = ErrorType.UnknownError
      }

      if (errorType != ErrorType.NoError && errorType != ErrorType.EmailIsBlocked) {
        println("verify email query failed")
      }

      statement.close()
    }

    return EmailVerifyReturn(
      errorType = errorType,
      email = email,
      avatar = avatar,
    )
  }
}
