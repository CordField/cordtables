package com.seedcompany.cordtables.core

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ClassPathResource
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.queryForObject
import org.springframework.stereotype.Component
import org.springframework.util.FileCopyUtils
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.sql.PreparedStatement
import java.sql.Timestamp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.sql.DataSource


@Component
class DatabaseVersionControl(
  @Autowired
  val appConfig: AppConfig,

  @Autowired
  val ds: DataSource,

  @Autowired
  val util: Utility,
) {
  val jdbcTemplate: JdbcTemplate = JdbcTemplate(ds)

  fun initDatabase() {
    if (!isDbInit()) {
      toVersion1()
      if (appConfig.loadDbVersion > 1) {
        updateSchemaIdempotent(appConfig.loadDbVersion)
      }
      updateHistoryTables()
      updatePeerTables()
      println("database schema init complete")
    } else {
      println("db is ready")
    }
  }

  fun updateSchemaIdempotent(version: Int) {
    while (getSchemaVersion() < version) {
      when (getSchemaVersion()) {
        1 -> {
          println("upgrading schema to version 2")
          toVersion2()
        }
        2 -> {
          println("upgrading schema to version 3")
          toVersion3()
        }
        3 -> {
          println("upgrading schema to version 4")
          toVersion4()
        }
        else -> {
          break
        }
      }
    }

  }

  fun loadRoles() {
    runSqlFile("sql/data/roles.data.sql")
  }

  fun loadSilData() {
    var adminPeopleId = ""
    var adminGroupId: String? = util.adminGroupId() ?: return

    println("loadSil Data")

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

    loadCountryCodes(adminPeopleId, adminGroupId!!)
    loadLanguageCodes(adminPeopleId, adminGroupId!!)
    loadLanguageIndexes(adminPeopleId, adminGroupId!!)
    loadIso_639_3_Name_Index(adminPeopleId, adminGroupId!!)
    loadIso_639_3(adminPeopleId, adminGroupId!!)
    loadIso_639_3_Retirements(adminPeopleId, adminGroupId!!)
    loadIso_639_3_Macrolanguages(adminPeopleId, adminGroupId!!)
  }

  private fun updateHistoryTables() {
    runSqlFile("sql/version-control/history.sql")
  }

  private fun updatePeerTables() {
    runSqlFile("sql/version-control/peer.sql")
  }

  private fun getSchemaVersion(): Int {
    return jdbcTemplate.queryForObject(
      """
                select version 
                from admin.database_version_control 
                order by version 
                desc limit 1;
            """.trimIndent(),
      Int::class.java
    ) ?: return 0
  }

  private fun isDbInit(): Boolean {
    return jdbcTemplate.queryForObject(
      """
               SELECT EXISTS (
               SELECT FROM information_schema.tables 
               WHERE  table_schema = 'admin'
               AND    table_name   = 'database_version_control'
               );
            """.trimIndent()
    )
  }

  private fun toVersion4() {
    // sc
//    runSqlFile("sql/schemas/sc/sc.v4.sql")
    setVersionNumber(4)
  }

  private fun toVersion3() {
    // sc
    runSqlFile("sql/schemas/sc/sc.v3.sql")
    setVersionNumber(3)
  }

  private fun toVersion2() {
    // admin

//    runSqlFile("sql/schemas/admin/admin.v2.sql")

    // common

//    runSqlFile("sql/schemas/common/common.v2.sql")

    setVersionNumber(2)
  }

  private fun toVersion1() {
//    runSqlFile("sql/helpers/uuid.sql")

    println("version 1 not found. creating schema.")

    // admin
    runSqlFile("sql/schemas/admin/admin.v1.sql")

    // common
    runSqlFile("sql/schemas/common/common.v1.sql")

    // sil
    runSqlFile("sql/schemas/sil/sil.v1.sql")
    runSqlFile("sql/migration/language_index.migration.sql")
//    runSqlFile("sql/migration/ethnologue.migration.sql")

    // up (unceasing prayer)
    runSqlFile("sql/schemas/up/up.v1.sql")

    // load data functions
    runSqlFile("sql/data/bootstrap.data.sql")

    // user
    runSqlFile("sql/modules/user/register.sql")
    runSqlFile("sql/modules/user/login.sql")

    // prep and bootstrap the db
    val pash = util.encoder.encode(appConfig.cordAdminPassword)

    var errorType = ErrorType.UnknownError

    this.ds.connection.use { conn ->
      val bootstrapStatement = conn.prepareCall("call bootstrap(?, ?, ?);")
      bootstrapStatement.setString(1, "devops@tsco.org")
      bootstrapStatement.setString(2, pash)
      bootstrapStatement.setString(3, errorType.name)
      bootstrapStatement.registerOutParameter(3, java.sql.Types.VARCHAR)

      bootstrapStatement.execute()

      try {
        errorType = ErrorType.valueOf(bootstrapStatement.getString(3))
      } catch (ex: IllegalArgumentException) {
        errorType = ErrorType.UnknownError
      }

      if (errorType != ErrorType.NoError) {
        println("bootstrap query failed")
      }

      bootstrapStatement.close()
    }

    // update version control table
    jdbcTemplate.execute(
      """
            insert into admin.database_version_control(version, status, started, completed)
                values(1, 'Completed', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
            """.trimIndent()
    )

  }

  private fun loadCountryCodes(adminPeopleId: String, adminGroupId: String) {

    var url = URL("https://raw.githubusercontent.com/CordField/datasets/main/CountryCodes.tab")
    var urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection

    try {

      val readBuffer = BufferedReader(
        InputStreamReader(
          urlConnection.inputStream
        )
      );

      var countryCodesQuery =
        "insert into sil.country_codes(country, name, area, created_by, modified_by, owning_person, owning_group) values "

      var count = 0
      var text: List<String> = readBuffer.readLines()

      for (line in text) {
        val splitArray = line.split("\t")
        val countryIdEntry = splitArray[0]
        val nameEntry = splitArray[1]
        val areaEntry = splitArray[2]
        count++
        if (count == 1) continue
        countryCodesQuery += "('${countryIdEntry}', '${nameEntry}', '${areaEntry}', '${adminPeopleId}'::uuid, '${adminPeopleId}'::uuid, '${adminPeopleId}'::uuid, '${adminGroupId}'::uuid), "
      }

      countryCodesQuery = countryCodesQuery.dropLast(2) + ";"

      try {
        runSqlString(countryCodesQuery)
        println("CountryCodes.tab load success")
      } catch (ex: Exception) {
        println(ex)
        println("CountryCodes.tab load filed")
      }

    } catch (ex: Exception) {
      println("exception ${ex}")
    } finally {
      urlConnection.disconnect()
    }
  }

  private fun loadLanguageCodes(adminPeopleId: String, adminGroupId: String) {

    val url = URL("https://raw.githubusercontent.com/CordField/datasets/main/LanguageCodes.tab")
    val urlConnection = url.openConnection() as HttpURLConnection

    try {

      val readBuffer = BufferedReader(
        InputStreamReader(
          urlConnection.inputStream
        )
      );

      var languageCodesQuery =
        "insert into sil.language_codes(lang, country, lang_status, name, created_by, modified_by, owning_person, owning_group) values "
      var count = 0
      var text: List<String> = readBuffer.readLines()

      for (line in text) {
        val splitArray = line.split("\t")
        val lang = splitArray[0]
        val country = splitArray[1]
        val langStatus = splitArray[2]
        val name = splitArray[3]
        count++
        if (count == 1) continue
        languageCodesQuery += "('${lang}', '${country}', '${langStatus}', '${name}', '${adminPeopleId}'::uuid, '${adminPeopleId}'::uuid, '${adminPeopleId}'::uuid, '${adminGroupId}'::uuid), "
      }
      languageCodesQuery = languageCodesQuery.dropLast(2) + ";"

      try {
        runSqlString(languageCodesQuery)
        println("LanguageCodes.tab load success")
      } catch (ex: Exception) {
        println(ex)

        println("LanguageCodes.tab load filed")
      }

    } catch (ex: Exception) {
      println("exception ${ex}")
    } finally {
      urlConnection.disconnect()
    }
  }

  private fun loadLanguageIndexes(adminPeopleId: String, adminGroupId: String) {

    val url = URL("https://raw.githubusercontent.com/CordField/datasets/main/LanguageIndex.tab")
    val urlConnection = url.openConnection() as HttpURLConnection

    try {
      val readBuffer = BufferedReader(
        InputStreamReader(
          urlConnection.inputStream
        )
      );
      var count = 0
      var text = readBuffer.readLines()
      var languageIndexQuery = ""
      for (line in text) {
        val splitArray = line.split("\t")
        val lang = splitArray[0]
        val country = splitArray[1]
        val nameType = splitArray[2]
        val name = splitArray[3]

        count++
        if (count == 1) continue
        languageIndexQuery += """
                    call sil.sil_migrate_language_index('${lang}', '${country}', '${nameType}', '$name');
                """.trimIndent()
      }

      try {
        runSqlString(languageIndexQuery)
        println("LanguageIndex.tab load success")
      } catch (ex: Exception) {
        println(ex)
        println("LanguageIndex.tab load filed")
      }

    } catch (ex: Exception) {
      println("exception ${ex}")
    } finally {
      urlConnection.disconnect()
    }
  }

  private fun loadIso_639_3_Name_Index(adminPeopleId: String, adminGroupId: String) {

    val url = URL("https://raw.githubusercontent.com/CordField/datasets/main/iso-639-3_Name_Index.tab")
    val urlConnection = url.openConnection() as HttpURLConnection

    this.ds.connection.use { conn ->
      try {

        val readBuffer = BufferedReader(
          InputStreamReader(
            urlConnection.inputStream
          )
        )

        var count = 0
        var text: List<String> = readBuffer.readLines()

        this.ds.connection.use { conn ->
          try {

            val insertSQL =
              ("insert into sil.iso_639_3_names(_id, print_name, inverted_name, created_by, modified_by, owning_person, owning_group) values (?, ?, ?, ?::uuid, ?::uuid, ?::uuid, ?::uuid)")
            val insertStmt: PreparedStatement = conn.prepareStatement(insertSQL)

            for (line in text) {
              val splitArray = line.split("\t")
              val id = splitArray[0]
              val printName = splitArray[1]
              val invertedName = splitArray[2]
              count++
              if (count == 1) continue
              insertStmt.setString(1, id)
              insertStmt.setString(2, printName)
              insertStmt.setString(3, invertedName)
              insertStmt.setString(4, adminPeopleId)
              insertStmt.setString(5, adminPeopleId)
              insertStmt.setString(6, adminPeopleId)
              insertStmt.setString(7, adminGroupId)

              insertStmt.addBatch()
            }
            insertStmt.executeBatch()

            println("iso-639-3_Name_Index.tab load success")
          } catch (ex: Exception) {
            println(ex)
            println("iso-639-3_Name_Index.tab load filed")
          }
        }
      } catch (ex: Exception) {
        println("exception ${ex}")
      } finally {
        urlConnection.disconnect()
      }
    }

  }

  private fun loadIso_639_3(adminPeopleId: String, adminGroupId: String) {

    val url = URL("https://raw.githubusercontent.com/CordField/datasets/main/iso-639-3.tab")
    val urlConnection = url.openConnection() as HttpURLConnection

    try {

      val readBuffer = BufferedReader(
        InputStreamReader(
          urlConnection.inputStream
        )
      )

      var count = 0
      var text: List<String> = readBuffer.readLines()

      this.ds.connection.use { conn ->
        try {

          val insertSQL = """
                        insert into sil.iso_639_3(_id, part_2b, part_2t, part_1, scope, type, ref_name, comment, created_by, modified_by, owning_person, owning_group)
                        values  (?, ?, ?, ?, ?::sil.iso_639_3_scope_options, ?::sil.iso_639_3_type_options, ?, ?, ?::uuid, ?::uuid, ?::uuid, ?::uuid)"""
          val insertStmt: PreparedStatement = conn.prepareStatement(insertSQL)

          for (line in text) {
            val splitArray = line.split("\t")
            val id = splitArray[0]
            val part2b = splitArray[1]
            val part2t = splitArray[2]
            val part1 = splitArray[3]
            val scope = splitArray[4]
            val type = splitArray[5]
            val refName = splitArray[6]
            val comment = splitArray[7]

            count++
            if (count == 1) continue
            insertStmt.setString(1, id)
            insertStmt.setString(2, part2b)
            insertStmt.setString(3, part2t)
            insertStmt.setString(4, part1)
            insertStmt.setString(5, scope)
            insertStmt.setString(6, type)
            insertStmt.setString(7, refName)
            insertStmt.setString(8, comment)
            insertStmt.setString(9, adminPeopleId)
            insertStmt.setString(10, adminPeopleId)
            insertStmt.setString(11, adminPeopleId)
            insertStmt.setString(12, adminGroupId)

            insertStmt.addBatch()
          }
          insertStmt.executeBatch()

          println("iso-639-3.tab load success")
        } catch (ex: Exception) {
          println(ex)
          println("iso-639-3.tab load filed")
        }
      }
    } catch (ex: Exception) {
      println("exception ${ex}")
    } finally {
      urlConnection.disconnect()
    }
  }

  private fun loadIso_639_3_Retirements(adminPeopleId: String, adminGroupId: String) {

    val url = URL("https://raw.githubusercontent.com/CordField/datasets/main/iso-639-3_Retirements.tab")
    val urlConnection = url.openConnection() as HttpURLConnection

    try {

      val readBuffer = BufferedReader(
        InputStreamReader(
          urlConnection.inputStream
        )
      );

      var count = 0
      var text: List<String> = readBuffer.readLines()

      this.ds.connection.use { conn ->
        try {

          val insertSQL =
            "insert into sil.iso_639_3_retirements(_id, ref_name, ret_reason, change_to, ret_remedy, effective, created_by, modified_by, owning_person, owning_group) values (?, ?, ?::sil.iso_639_3_retirement_reason_options, ?, ?, ?, ?::uuid, ?::uuid, ?::uuid, ?::uuid)"
          val insertStmt: PreparedStatement = conn.prepareStatement(insertSQL)

          for (line in text) {
            val splitArray = line.split("\t")
            count++
            if (count == 1) continue

            val id = splitArray[0]
            val refName = splitArray[1]
            var retReason: String? = splitArray[2]
            if (retReason!!.isEmpty()) retReason = null
            val changeTo = splitArray[3]
            val retRemedy = splitArray[4]
            val effective = splitArray[5]
            val pattern = "yyyy-MM-dd"
            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(pattern)
            val localDate: LocalDate = LocalDate.from(formatter.parse(effective))
            val timestamp = Timestamp.valueOf(localDate.atStartOfDay())

            insertStmt.setString(1, id)
            insertStmt.setString(2, refName)
            insertStmt.setString(3, retReason)
            insertStmt.setString(4, changeTo)
            insertStmt.setString(5, retRemedy)
            insertStmt.setTimestamp(6, timestamp)
            insertStmt.setString(7, adminPeopleId)
            insertStmt.setString(8, adminPeopleId)
            insertStmt.setString(9, adminPeopleId)
            insertStmt.setString(10, adminGroupId)

            insertStmt.addBatch()

          }
          insertStmt.executeBatch()

          println("iso-639-3_Retirements.tab load success")
        } catch (ex: Exception) {
          println(ex)

          println("iso-639-3_Retirements.tab load filed")
        }
      }
    } catch (ex: Exception) {
      println("exception ${ex}")
    } finally {
      urlConnection.disconnect()
    }
  }

  private fun loadIso_639_3_Macrolanguages(adminPeopleId: String, adminGroupId: String) {

    val url = URL("https://raw.githubusercontent.com/CordField/datasets/main/iso-639-3-macrolanguages.tab")
    val urlConnection = url.openConnection() as HttpURLConnection

    try {

      val readBuffer = BufferedReader(
        InputStreamReader(
          urlConnection.inputStream
        )
      )

      var count = 0
      var text: List<String> = readBuffer.readLines()

      this.ds.connection.use { conn ->
        try {

          val insertSQL =
            "insert into sil.iso_639_3_macrolanguages(m_id, i_id, i_status, created_by, modified_by, owning_person, owning_group) values (?, ?, ?::sil.iso_639_3_status_options, ?::uuid, ?::uuid, ?::uuid, ?::uuid)"
          val insertStmt: PreparedStatement = conn.prepareStatement(insertSQL)

          for (line in text) {
            val splitArray = line.split("\t")
            val m_id = splitArray[0]
            val i_id = splitArray[1]
            var i_status: String? = splitArray[2]
            if (i_status!!.isEmpty()) i_status = null

            count++
            if (count == 1) continue
            insertStmt.setString(1, m_id)
            insertStmt.setString(2, i_id)
            insertStmt.setString(3, i_status)
            insertStmt.setString(4, adminPeopleId)
            insertStmt.setString(5, adminPeopleId)
            insertStmt.setString(6, adminPeopleId)
            insertStmt.setString(7, adminGroupId)

            insertStmt.addBatch()

          }
          insertStmt.executeBatch()

          println("iso-639-3-macrolanguages.tab load success")
        } catch (ex: Exception) {
          println(ex)

          println("iso-639-3-macrolanguages.tab load filed")
        }
      }

    } catch (ex: Exception) {
      println("exception ${ex}")
    } finally {
      urlConnection.disconnect()
    }
  }

  private fun setVersionNumber(newVersion: Int) {
    jdbcTemplate.update(
      """
                insert into admin.database_version_control(version, status, started, completed)
                    values(?, 'Completed', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
            """.trimIndent(),
      newVersion
    )
  }

  private fun asString(resource: ClassPathResource): String? {
    try {
      InputStreamReader(resource.inputStream, Charsets.UTF_8).use { reader ->
        return FileCopyUtils.copyToString(
          reader
        )
      }
    } catch (e: IOException) {
      throw UncheckedIOException(e)
    }
  }

  private fun runSqlFile(fileName: String) {

    val sql = asString(ClassPathResource(fileName))
    if (sql !== null) {
      jdbcTemplate.execute(sql)
      println("$fileName successfully run")
    }
  }

  private fun runSqlString(sql: String) {
    jdbcTemplate.execute(sql)
  }
}
