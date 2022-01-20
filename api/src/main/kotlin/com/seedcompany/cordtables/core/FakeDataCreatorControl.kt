package com.seedcompany.cordtables.core


import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import javax.sql.DataSource


@Component
class FakeDataCreatorControl (
  @Autowired
  val appConfig: AppConfig,

  @Autowired
  val ds: DataSource,

  @Autowired
  val util: Utility,
) {
  val jdbcTemplate: JdbcTemplate = JdbcTemplate(ds)

  fun loadCommonFakeData() {
    var adminPeopleId = ""
    var adminGroupId: String? = util.adminGroupId() ?: return

    this.ds.connection.use { conn ->
      try {

        val getAdminIdStatement =
          conn.prepareCall(
            """
              select admin.people.id as id 				from admin.people
              inner join admin.role_memberships 	on admin.role_memberships.person = admin.people.id
              inner join admin.roles 				on admin.role_memberships.role = admin.roles.id
              where admin.roles.name = 'Administrator'
              order by admin.people.created_at asc
              limit 1;
          """.trimIndent()
          )

        val result = getAdminIdStatement.executeQuery()
        if (result.next()) {
          adminPeopleId = result.getString("id")
        }
        getAdminIdStatement.close()
      } catch (ex: IllegalArgumentException) {
        println("admin people not found!")
      }
    }

    loadCommonLocationsNamesFakeData(adminPeopleId, adminGroupId!!)
    loadCommonOrganizationsFakeData(adminPeopleId, adminGroupId!!)


  }

  private fun loadCommonLocationsNamesFakeData(adminPeopleId: String, adminGroupId: String){

    val url = URL("https://raw.githubusercontent.com/CordField/datasets/main/LocationsNames.tab")
    val urlConnection = url.openConnection() as HttpURLConnection

    try {

      val readBuffer = BufferedReader(
        InputStreamReader(
          urlConnection.inputStream
        )
      );

      var commonLocationsNamesQuery =
        "insert into common.locations(sensitivity, name, type, iso_alpha3, created_by, modified_by, owning_person, owning_group) values "
      var count = 0
      var text: List<String> = readBuffer.readLines()


      for (line in text) {
        val splitArray = line.split(":")
        val sensitivity = splitArray[0]
        val name = splitArray[1].replace("^\\s+".toRegex(), "")
        val isoAlpha3 = splitArray[2].replace("^\\s+".toRegex(), "")
        val type = splitArray[3].replace("^\\s+".toRegex(), "")

        count++
        if (count == 1) continue
        commonLocationsNamesQuery += "('${sensitivity}', '${name}', '${type}' , '${isoAlpha3}',  '${adminPeopleId}'::uuid, '${adminPeopleId}'::uuid, '${adminPeopleId}'::uuid, '${adminGroupId}'::uuid), "
      }
      commonLocationsNamesQuery = commonLocationsNamesQuery.dropLast(2) + ";"

      try {
        runSqlString(commonLocationsNamesQuery)
        println("LocationsNames.tab load successfully")
      } catch (ex: Exception) {
        println(ex)

        println("LocationsNames.tab load failed")
      }

    } catch (ex: Exception) {
      println("exception ${ex}")
    } finally {
      urlConnection.disconnect()
    }
  }

  private fun loadCommonOrganizationsFakeData(adminPeopleId: String, adminGroupId: String){

    val url = URL("https://raw.githubusercontent.com/CordField/datasets/main/OrganizationNames.tab")
    val urlConnection = url.openConnection() as HttpURLConnection


    var commonLocationsId = ""

    this.ds.connection.use { conn ->
      try {

        val getLocationsIdStatement =
          conn.prepareCall(
            """
              select common.locations.id as id from common.locations limit 1;
          """.trimIndent()
          )

        val result = getLocationsIdStatement.executeQuery()
        if (result.next()) {
          commonLocationsId = result.getString("id")
        }

        getLocationsIdStatement.close()
      } catch (ex: IllegalArgumentException) {
        println("locations table is empty!")
      }
    }

    try {

      val readBuffer = BufferedReader(
        InputStreamReader(
          urlConnection.inputStream
        )
      );


      var commonOrganizationsNamesQuery =
        "insert into common.organizations(sensitivity, name, primary_location, created_by, modified_by, owning_person, owning_group) values "
      var count = 0
      var text: List<String> = readBuffer.readLines()


      for (line in text) {
        val splitArray = line.split(":")
        val sensitivity = splitArray[0]
        val name = splitArray[1].replace("^\\s+".toRegex(), "")

        count++
        if (count == 1) continue
<<<<<<< HEAD
        commonOrganizationsNamesQuery += "('${sensitivity}', '${name}', '${adminPeopleId}', '${adminPeopleId}', '${adminPeopleId}', '${adminGroupId}'), "
=======
        commonOrganizationsNamesQuery += "('${sensitivity}', '${name}', '${commonLocationsId}', '${adminPeopleId}'::uuid, '${adminPeopleId}'::uuid, '${adminPeopleId}'::uuid, '${adminGroupId}'::uuid), "
>>>>>>> 265ec00a093154c2e6e210200e49d926a3d0ac92
      }

      commonOrganizationsNamesQuery = commonOrganizationsNamesQuery.dropLast(2) + ";"

      try {
        runSqlString(commonOrganizationsNamesQuery)
        println("OrganizationNames.tab load successfully")
      } catch (ex: Exception) {
        println(ex)

        println("OrganizationNames.tab load failed")
      }

    } catch (ex: Exception) {
      println("exception ${ex}")
    } finally {
      urlConnection.disconnect()
    }
  }

  private fun runSqlString(sql: String) {
    jdbcTemplate.execute(sql)
  }

}
