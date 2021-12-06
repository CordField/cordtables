package com.seedcompany.cordtables.services

import com.seedcompany.cordtables.common.ErrorType
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.sql.SQLException
import javax.sql.DataSource
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString

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
}
