package com.seedcompany.cordtables.components.user

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Regions
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder
import com.amazonaws.services.simpleemail.model.*
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.admin.users.user
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
import java.sql.SQLException
import javax.sql.DataSource

data class SendResetMailRequest(
  val email: String
)

data class SendResetMailResponse(
  val status: String,
  val email: String,
)

//@Component
//@DependsOn("BootstrapDB")
@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller()
class SendResetEmail(
  @Autowired
  val ds: DataSource,
  @Autowired
  val appConfig: AppConfig,
  @Autowired
  val util: Utility,
  @Autowired
  val publisher: ApplicationEventPublisher

) {

//  val awsCreds = BasicAWSCredentials(appConfig.awsAccessKeyId, appConfig.awsSecretAccessKey)

//  val sesClient = AmazonSimpleEmailServiceClientBuilder.standard()
//    .withCredentials(AWSStaticCredentialsProvider(awsCreds)).withRegion(Regions.US_EAST_2).build();

//  @EventListener
//  fun sendResetEmailHandler(event: UserSendResetEmailRequest) {
//
//    val canSendError = util.isEmailSendable(event.email)
//
//    if (canSendError != ErrorType.NoError) {
//      publisher.publishEvent(
//        UserSendResetEmailResponse(
//          error = canSendError,
//          sessionId = event.sessionId,
//          type = MessageType.UserSendResetEmailResponse,
//        )
//      )
//
//      return
//    }
//
//    val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
//
//    val emailToken = (1..64)
//      .map { i -> kotlin.random.Random.nextInt(0, charPool.size) }
//      .map(charPool::get)
//      .joinToString("")
//
//    val result = sendResetEmailFn(event.email, emailToken)
//
//    if (result.errorType === ErrorType.NoError && result.avatar !== null) {
//
//      sendResetEmail(
//        email = event.email, avatar = result.avatar, emailToken = emailToken
//      )
//    }
//
//    publisher.publishEvent(
//      UserSendResetEmailResponse(
//        error = result.errorType,
//        sessionId = event.sessionId,
//        type = MessageType.UserSendResetEmailResponse,
//      )
//    )
//  }


  @PostMapping(path=["user/send-reset-eamil"], consumes = ["application/json"], produces = ["application/json"])
  @ResponseBody
  fun sendResetEmailHandler(req: SendResetMailRequest): SendResetMailResponse {

    this.ds.connection.use { conn ->
      try {
          val userQuery = conn.prepareCall("select * from admin.users where email = ?;")
          userQuery.setString(1, req.email)
          val userQueryResult = userQuery.executeQuery()
          if (userQueryResult.next()){
            println(" user exists")
          }
          else{
            println("no user exists")
          }
      }
      catch (e: SQLException){

      }
    }

    return SendResetMailResponse(email = "", status = "")

  }

  fun sendResetEmail(email: String, avatar: String, emailToken: String) {

//    val emailFrom = "no-reply@crowdaltar.com"
//    val emailSubject = "Password Reset - Crowd Altar"
//    val encodedToken = URLEncoder.encode(emailToken, StandardCharsets.UTF_8.toString())
//
//    val emailHtmlBody = """
//      <h1>Hello ${avatar}!</h1>
//      <p>To reset your password, please click <a href="${appConfig.emailServer}/reset/${encodedToken}">here</a>.</p>
//      <p>If you didn't request your password to be reset, you do not need to do anything.</p>
//    """.trimIndent()
//
//    val emailTextBody = """
//      Hello ${avatar}!
//
//      To reset your password, please click this link:
//
//      ${appConfig.emailServer}/reset/${encodedToken}
//
//      If you didn't request your password to be reset, you do not need to do anything.
//
//    """.trimIndent()

//    val emailRequest = SendEmailRequest()
//      .withDestination(Destination().withToAddresses(email))
//      .withMessage(
//        Message()
//          .withBody(
//            Body()
//              .withHtml(
//                Content().withCharset("UTF-8")
//                  .withData(emailHtmlBody)
//              )
//              .withText(
//                Content().withCharset("UTF-8")
//                  .withData(emailTextBody)
//              )
//          )
//          .withSubject(
//            Content().withCharset("UTF-8").withData(emailSubject)
//          )
//      )
//      .withSource(emailFrom)

//    try {
//
//      val result = sesClient.sendEmail(emailRequest)
//
//      this.ds.connection.use { conn ->
//        //language=SQL
//        val statement = conn.prepareStatement("""
//          insert into emails_sent("email", "message_id", "type") values (?, ?, 'PasswordReset');
//        """.trimIndent())
//
//        statement.setString(1, email)
//        statement.setString(2, result.messageId)
//
//        statement.execute()
//      }
//
//    } catch (e: Exception) {
//      println("SES Exception: ${e.localizedMessage}")
//
//    }
  }

  data class SendResetEmailReturn(
    val errorType: ErrorType,
    val avatar: String?,
  )

  fun sendResetEmailFn(email: String, token: String): SendResetEmailReturn {

    var errorType = ErrorType.UnknownError

    var avatar: String? = null

    //language=SQL
    val checkIfEmailExistsSQL = """
            CALL send_reset_email(?, ?, ?, ?);
        """.trimIndent()

    this.ds.connection.use { conn ->
      val statement = conn.prepareCall(checkIfEmailExistsSQL)

      statement.setString(1, email)
      statement.setString(2, token)
      statement.setNull(3, java.sql.Types.NULL)
      statement.setString(4, errorType.name)
      statement.registerOutParameter(3, java.sql.Types.VARCHAR)
      statement.registerOutParameter(4, java.sql.Types.VARCHAR)

      statement.execute()

      try {
        errorType = ErrorType.valueOf(statement.getString(4))

        if (errorType === ErrorType.NoError) {
          avatar = statement.getString(3)
        }

      } catch (ex: IllegalArgumentException) {
        errorType = ErrorType.UnknownError
        println("verify email query failed")
      }

      statement.close()

      return SendResetEmailReturn(errorType = errorType, avatar = avatar)
    }
  }
}
