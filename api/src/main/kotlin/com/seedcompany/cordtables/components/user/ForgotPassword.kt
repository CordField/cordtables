package com.seedcompany.cordtables.components.user

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.ResultSet
import java.sql.SQLException
import javax.sql.DataSource
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.regions.Regions
import com.seedcompany.cordtables.core.AppConfig
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import com.amazonaws.services.simpleemail.model.*
import com.seedcompany.cordtables.components.tables.admin.users.user

data class ForgotPasswordRequest(
  val email: String? = null
)

data class ForgotPasswordResponse(
  val error: ErrorType,
)

data class checkEmailExistsResponse(
  val error: ErrorType,
  val id: String? = null,
  val result: ResultSet? = null
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ForgotPasswordController")
class ForgotPassword (
  @Autowired
  val util: Utility,

  @Autowired
  val ds: DataSource,

  @Autowired
  val appConfig: AppConfig,
){
  val jdbcTemplate: JdbcTemplate = JdbcTemplate(ds)
  val awsCreds = BasicAWSCredentials(appConfig.awsAccessKeyId, appConfig.awsSecretAccessKey)
  val sesClient = AmazonSimpleEmailServiceClientBuilder.standard().withCredentials(AWSStaticCredentialsProvider(awsCreds)).withRegion(Regions.US_EAST_2).build()

  @PostMapping(path=["user/forgot-password"], consumes = ["application/json"], produces = ["application/json"])
  @ResponseBody
  fun forgotPassword(@RequestBody req: ForgotPasswordRequest): ForgotPasswordResponse{
    var error: ErrorType = ErrorType.NoError
    if (req.email == null || !util.isEmailValid(req.email)) return ForgotPasswordResponse(ErrorType.InvalidEmail)

    var emailExists = this.checkEmailExists(req.email)
    if (emailExists.error == ErrorType.NoError){
      val token = util.createToken()
      val tokenId = emailExists.id?.let { this.generateTokenEntry(token, it) }
      if (tokenId!=""){
        this.sendResetEmail(req.email, "User", token)
        error = ErrorType.NoError
      }
    }
    else {
      return ForgotPasswordResponse(error = emailExists.error)
    }
    return ForgotPasswordResponse(error)
  }

  fun generateTokenEntry(token: String, userId: String): String? {
    var id: String? = ""
    try {
      id  = jdbcTemplate.queryForObject("""
                INSERT INTO admin.email_tokens (token, user_id) VALUES ( ?, ?::uuid) RETURNING  id;
            """.trimIndent(),
        String::class.java,
        token,
        userId
      )
    }
    catch (e: SQLException){
      println("exception")
    }
    return id
  }

  fun checkEmailExists(email: String): checkEmailExistsResponse {
    var error: ErrorType = ErrorType.NoError
    var result: ResultSet? = null
    var id: String = ""
    this.ds.connection.use { conn ->
      try {
        var sqlStatement = conn.prepareCall("""
                       SELECT id FROM admin.users WHERE email = ?
                    """.trimIndent())
        sqlStatement.setString(1, email)
        var queryResult = sqlStatement.executeQuery()
        if (queryResult.next()){
          error = ErrorType.NoError
          result = queryResult
          id = queryResult.getString("id")
        }
        else{
          error = ErrorType.BadCredentials
        }
      }
      catch (e: SQLException) {
        error = ErrorType.SQLReadError
      }
    }
    return checkEmailExistsResponse(error = error, id = id, result = result)
  }

  fun sendResetEmail(email: String, avatar: String, emailToken: String) {
    val emailFrom = "devops@tsco.org"
    val emailSubject = "Password Reset - Cord Tables"
    val encodedToken = URLEncoder.encode(emailToken, StandardCharsets.UTF_8.toString())

    val emailHtmlBody = """
            <h1>Hello ${avatar}!</h1>
            <p>To reset your password, please click <a href="${appConfig.emailServer}/reset-password/${encodedToken}">here</a>.</p>
            <p>If you didn't request your password to be reset, you do not need to do anything.</p>
          """.trimIndent()

    val emailTextBody = """
            Hello ${avatar}!
            
            To reset your password, please click this link:
            
            ${appConfig.emailServer}/reset-password/${encodedToken}
            
            If you didn't request your password to be reset, you do not need to do anything.
          
        """.trimIndent()

    val emailRequest = SendEmailRequest()
      .withDestination(Destination().withToAddresses(email))
      .withMessage(
        Message()
          .withBody(
            Body().withHtml(
              Content().withCharset("UTF-8")
                .withData(emailHtmlBody)
            )
              .withText(
                Content().withCharset("UTF-8")
                  .withData(emailTextBody)
              )
          )
          .withSubject(
            Content().withCharset("UTF-8").withData(emailSubject)
          )
      )
      .withSource(emailFrom)
    try {

      val result = sesClient.sendEmail(emailRequest)
//            this.ds.connection.use { conn ->
//                //language=SQL
//                val statement = conn.prepareStatement("""
//                      insert into emails_sent("email", "message_id", "type") values (?, ?, 'PasswordReset');
//                """.trimIndent())
//
//                statement.setString(1, email)
//                statement.setString(2, result.messageId)
//
//                statement.execute()
//            }

    } catch (e: Exception) {
      println("SES Exception: ${e.localizedMessage}")
    }
  }
}
