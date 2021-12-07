package com.seedcompany.cordtables.core

import com.seedcompany.cordtables.common.Utility
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.ApplicationEventPublisher
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import javax.sql.DataSource

@Serializable
data class SNSSubscriptionPayload(
  val Type: String? = null,
  val MessageId: String? = null,
  val Token: String? = null,
  val TopicArn: String? = null,
  val Subject: String? = null,
  val Message: String? = null,
  val SubscribeURL: String? = null,
  val Timestamp: String? = null,
  val SignatureVersion: String? = null,
  val Signature: String? = null,
  val SigningCertURL: String? = null,
  val UnsubscribeURL: String? = null,
)

@Serializable
data class Recipients(
  val emailAddress: String? = null,
)

@Serializable
data class Complaint(
  val complainedRecipients: Array<Recipients>? = null,
)

@Serializable
data class Bounce(
  val bounceType: String? = null,
  val bouncedRecipients: Array<Recipients>? = null,
)

@Serializable
data class Mail(
  val source: String? = null,
  val messageId: String? = null,
  val destination: Array<String>? = null,
)

@Serializable
data class SESNotificationPayload(
  val notificationType: String? = null,
  val mail: Mail? = null,
  val bounce: Bounce? = null,
  val complaint: Complaint? = null,
)

@RestController
class SNSNotifications(

  @Autowired
  val writerDS: DataSource,

  @Autowired
  val appConfig: AppConfig,

  @Autowired
  val util: Utility,

  @Autowired
  val publisher: ApplicationEventPublisher,
) {

  val mapper = Json { ignoreUnknownKeys = true; encodeDefaults = true }

  @PostMapping("sns")
  fun snsHandler(@RequestBody notification: String) {
    notificationHandler(notification)
  }

  @PostMapping("sns-bounce")
  fun snsBounceHandler(@RequestBody notification: String) {
    notificationHandler(notification)
  }

  @PostMapping("sns-complaint")
  fun snsComplaintHandler(@RequestBody notification: String) {
    notificationHandler(notification)
  }

  fun notificationHandler(notification: String) {
    println(notification)

    try {

      val sesNotification = mapper.decodeFromString<SESNotificationPayload>(notification)

      if (sesNotification.notificationType != null) {

        println("type not null")

        if (sesNotification.notificationType == "Bounce") {

          println("is bounce")

          val email = sesNotification.bounce?.bouncedRecipients?.get(0)?.emailAddress ?: return
          val messageId = sesNotification.mail?.messageId ?: return

          println("email $email")
          println("messageId $messageId")

          // update emails_sent table
          this.writerDS.connection.use { conn ->
            //language=SQL
            val statement = conn.prepareStatement(
              """
              update emails_sent set response = 'Bounce' where message_id = ? and email = ?;
            """.trimIndent()
            )

            statement.setString(1, messageId)
            statement.setString(2, email)

            statement.execute()
          }

        } else if (sesNotification.notificationType == "Complaint") {
          println("is complaint")

          val email = sesNotification.complaint?.complainedRecipients?.get(0)?.emailAddress ?: return
          val messageId = sesNotification.mail?.messageId ?: return

          println("email $email")
          println("messageId $messageId")

          // update emails_sent table
          this.writerDS.connection.use { conn ->
            //language=SQL
            val statement = conn.prepareStatement(
              """
              update emails_sent set response = 'Complaint' where message_id = ? and email = ?;
            """.trimIndent()
            )

            statement.setString(1, messageId)
            statement.setString(2, email)

            statement.execute()
          }


        } else if (sesNotification.notificationType == "Delivery") {
          println("is delivery")

          val email = sesNotification.mail?.destination?.get(0) ?: return
          val messageId = sesNotification.mail?.messageId ?: return

          println("email $email")
          println("messageId $messageId")

          // update emails_sent table
          this.writerDS.connection.use { conn ->
            //language=SQL
            val statement = conn.prepareStatement(
              """
              update emails_sent set response = 'Delivery' where message_id = ? and email = ?;
            """.trimIndent()
            )

            statement.setString(1, messageId)
            statement.setString(2, email)

            statement.execute()
          }
        } else {
          println("SES notification not understood")
        }

      } else {

        val snsSubscriptionNotification = mapper.decodeFromString<SNSSubscriptionPayload>(notification)

        if (snsSubscriptionNotification.Type.equals("SubscriptionConfirmation")) {

          val url = URL(snsSubscriptionNotification.SubscribeURL)
          val con: HttpsURLConnection = url.openConnection() as HttpsURLConnection
          con.requestMethod = "GET"

        } else if (snsSubscriptionNotification.Type.equals("Notification")) {

          // update emails_sent table
          println("need to update database with bounce")

        } else if (snsSubscriptionNotification.Type.equals("UnsubscribeConfirmation")) {
          val url = URL(snsSubscriptionNotification.UnsubscribeURL)
          val con: HttpsURLConnection = url.openConnection() as HttpsURLConnection
          con.requestMethod = "GET"
        } else {
          println("sns notification type not understood")
        }

      }

    } catch (e: Exception) {
      println("failed to map sns bounce notification ${e.localizedMessage}")

    }
  }

}
