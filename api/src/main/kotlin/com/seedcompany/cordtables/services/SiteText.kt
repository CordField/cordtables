package com.seedcompany.cordtables.services

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ClassPathResource
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSource
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils
import org.springframework.stereotype.Service
import java.io.BufferedReader
import java.io.InputStreamReader
import java.sql.PreparedStatement
import java.sql.SQLException
import javax.sql.DataSource


data class SiteTextLanguage(
  val language: Int,
  val language_name: String? = null,
)

data class SiteTextString(
  val id: Int,
  val english: String,
  val comment: String?
)

data class SiteTextTranslation(
  val language: Int,
  val translations: MutableList<SiteTextTranslationExceptLanguage>?
)

@Serializable
data class SiteTextTranslationExceptLanguage(
  val id: Int,
  val site_text: Int,
  val translation: String
)

open class SiteTextLanguageResponse(
  val error: ErrorType,
  val data: MutableList<SiteTextLanguage>?,
)

open class SiteTextStringResponse(
  val error: ErrorType,
  val data: MutableList<SiteTextString>?,
)

open class SiteTextTranslationResponse(
  val error: ErrorType,
  val data: MutableList<SiteTextTranslation>?,
)

@Service
class SiteTextService(
  @Autowired
  val ds: DataSource,
  @Autowired
  val util: Utility

  ) {
  var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

  fun getSiteTextLanguages(): SiteTextLanguageResponse {
    val data: MutableList<SiteTextLanguage> = mutableListOf()

    val paramSource = MapSqlParameterSource()

    val query = """
            select sl.language, li.name
            from common.site_text_languages sl
            inner join sil.language_index li on li.common_id = stl.language
        """.replace('\n', ' ')

    try {
      val jdbcResult = jdbcTemplate.queryForRowSet(query, paramSource)
      while (jdbcResult.next()) {

        val language: Int = jdbcResult.getInt("language")

        var language_name: String? = jdbcResult.getString("name")
        if (jdbcResult.wasNull()) language_name = null

        data.add(
          SiteTextLanguage(
            language = language,
            language_name = language_name
          )
        )
      }
    } catch (e: SQLException) {
      println("error while listing ${e.message}")
      return SiteTextLanguageResponse(ErrorType.SQLReadError, mutableListOf())
    }
    return SiteTextLanguageResponse(ErrorType.NoError, data)
  }

  fun getSiteTextStrings(): SiteTextStringResponse {
    val data: MutableList<SiteTextString> = mutableListOf()

    val paramSource = MapSqlParameterSource()

    val query = """
            select sts.id, sts.english, sts.comment
            from common.site_text_strings sts
        """.replace('\n', ' ')

    try {
      val jdbcResult = jdbcTemplate.queryForRowSet(query, paramSource)
      while (jdbcResult.next()) {

        val id: Int = jdbcResult.getInt("id")

        var english: String? = jdbcResult.getString("english")
        if (jdbcResult.wasNull()) english = null

        var comment: String? = jdbcResult.getString("comment")
        if (jdbcResult.wasNull()) comment = null

        data.add(
          SiteTextString(
            id = id,
            english = english!!,
            comment = comment
          )
        )
      }
    } catch (e: SQLException) {
      println("error while listing ${e.message}")
      return SiteTextStringResponse(ErrorType.SQLReadError, mutableListOf())
    }
    return SiteTextStringResponse(ErrorType.NoError, data)
  }

  fun getSiteTextTranslations(): SiteTextTranslationResponse {
    val data: MutableList<SiteTextTranslation> = mutableListOf()

    val paramSource = MapSqlParameterSource()

    val query = """
            select
                stt.language,
                coalesce(
                    jsonb_agg(
                        jsonb_build_object(
                            'id', stt.id,
                            'site_text', stt.site_text,
                            'translation', stt.translation
                        )
                    ) FILTER (WHERE stt.id IS NOT NULL), '[]') AS translations
            from common.site_text_translations stt
            group by stt.language
        """.replace('\n', ' ')

    try {
      val jdbcResult = jdbcTemplate.queryForRowSet(query, paramSource)
      while (jdbcResult.next()) {

        val language: Int = jdbcResult.getInt("language")

        val translations: String = jdbcResult.getObject("translations").toString()

        val translationsObject = Json.decodeFromString<MutableList<SiteTextTranslationExceptLanguage>>(translations)
        data.add(
          SiteTextTranslation(
            language = language,
            translations = translationsObject
          )
        )
      }
    } catch (e: SQLException) {
      println("error while listing ${e.message}")
      return SiteTextTranslationResponse(ErrorType.SQLReadError, mutableListOf())
    }
    return SiteTextTranslationResponse(ErrorType.NoError, data)
  }

  fun loadSiteTextStrings(token: String) {
    // ====================  site_text_strings.tab load ===========================

    val readBuffer = BufferedReader(InputStreamReader(ClassPathResource("data/sample_site_text_strings.tab").inputStream))

    var count = 0
    var text = readBuffer.readLines()

    this.ds.connection.use { conn ->
      try {

        val insertSQL = """insert into common.site_text_strings(english, created_by, modified_by, owning_person, owning_group) 
          values (
            ?, 
            (
              select person 
              from admin.tokens 
              where token = ?
            ),
            (
              select person 
              from admin.tokens 
              where token = ?
            ),
            (
              select person 
              from admin.tokens 
              where token = ?
            ),
            1)""".trimIndent()
        val insertStmt: PreparedStatement = conn.prepareStatement(insertSQL)

        for (line in text) {
          val splitArray = line.split("\t")
          count++
          if (count == 1) continue

          val english = splitArray[0]
          insertStmt.setString(1, english)
          insertStmt.setString(2, token)
          insertStmt.setString(3, token)
          insertStmt.setString(4, token)

          insertStmt.addBatch()
        }
        insertStmt.executeBatch()

        println("site_text_strings.tab load success")
      } catch (ex: Exception) {
        println(ex)

        println("site_text_strings.tab load filed")
      }
    }
  }

  fun getKeyFromTranslationFileName(fileName: String): LanguageIndexKey {
    val splits = fileName.substring(30, fileName.length - 4).split("_");
    return LanguageIndexKey(
      lang = splits[0],
      country = splits[1],
      name_type = splits[2],
      name = util.convertHexToString(splits[3]!!)!!
    )
  }

  fun createSiteTextLanguageRow(language: Int, token: String) {
    try {

      val paramSource = MapSqlParameterSource()

      val query = """
        insert into common.site_text_languages(
          language, created_by, modified_by, owning_person, owning_group
        ) 
        values (
          :language, 
          (
            select person 
            from admin.tokens 
            where token = :token
          ),
          (
            select person 
            from admin.tokens 
            where token = :token
          ),
          (
            select person 
            from admin.tokens 
            where token = :token
          ),
          1)""".trimIndent()

      paramSource.addValue("language", language)
      paramSource.addValue("token", token)

      val status = jdbcTemplate.update(query, paramSource)

      println("status ${status}")

    } catch(e: Exception) {
      println(e)
    }
  }

  fun loadSiteTextTranslations(fileName: String, language: Int, token: String) {
    try {

      val readBuffer = BufferedReader(InputStreamReader(ClassPathResource("data/translations/${fileName}").inputStream))

      var count = 0
      var text = readBuffer.readLines()

      val translations = mutableListOf<SiteTextTranslationInput>()

      for (line in text) {
        val splitArray = line.split("\t")
        count++
        if (count == 1) continue

        val english = splitArray[0]
        val translation = splitArray[1]

        val stringParamSource = MapSqlParameterSource()
        var siteTextId: Int

        stringParamSource.addValue("english", english)
        val findSiteTextStringQuery = """
          select stt.id
          from common.site_text_strings stt
          where stt.english = :english
        """.trimIndent()

        val result = jdbcTemplate.queryForRowSet(findSiteTextStringQuery, stringParamSource)
        if(result.next()) {
          siteTextId = result.getInt(1)
          translations.add(SiteTextTranslationInput(
            language = language,
            site_text = siteTextId,
            translation = translation,
            token = token
          ))
        }
      }

      val batch: Array<SqlParameterSource> = SqlParameterSourceUtils.createBatch(translations)

      val query = """
        insert into common.site_text_translations(
          language, site_text, translation, created_by, modified_by, owning_person, owning_group
        )
        values (
          :language,
          :site_text,
          :translation,
          (
            select person 
            from admin.tokens 
            where token = :token
          ),
          (
            select person 
            from admin.tokens 
            where token = :token
          ),
          (
            select person 
            from admin.tokens 
            where token = :token
          ),
          1)""".trimIndent()

      jdbcTemplate.batchUpdate(query, batch)

    } catch(e: Exception) {
      println(e)
    }

  }

  fun loadTranslations(token: String) {
    val file = ClassPathResource("/data/translations").file
    file.list().forEach {
      var indexInfo: LanguageIndexKey
      try {
        indexInfo = getKeyFromTranslationFileName(it)
      } catch(e: Exception) {
        println("${it} file name or file has issues ")
        return@forEach
      }

      var commonLanguageId: Int

      try {

        val paramSource = MapSqlParameterSource()

        val query = """
            select
                li.common_id as language
            from sil.language_index li
            where lang = :lang and country = :country and name_type = :name_type::sil.language_name_type and name = :name
        """.trimIndent()

        paramSource.addValue("lang", indexInfo.lang)
        paramSource.addValue("country", indexInfo.country)
        paramSource.addValue("name_type", indexInfo.name_type)
        paramSource.addValue("name", indexInfo.name)

        val result = jdbcTemplate.queryForRowSet(query, paramSource)
        if(result.next()) {
          commonLanguageId = result.getInt(1)
        }
        else return@forEach
      } catch(e: Exception) {
        println(e)
        return@forEach
      }

      createSiteTextLanguageRow(commonLanguageId, token)
      loadSiteTextTranslations(it, commonLanguageId, token)
    }
  }

  fun init(token: String) {
    loadSiteTextStrings(token)
    loadTranslations(token)
  }
}
