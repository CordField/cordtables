package com.seedcompany.cordtables.services

import com.seedcompany.cordtables.common.ErrorType
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.sql.SQLException
import javax.sql.DataSource

data class SiteTextLanguage(
    val id: Int,
    val ethnologue: Int,
    val language_name: String? = null,
)

open class SiteTextLanguageResponse(
    val error: ErrorType,
    val data: MutableList<SiteTextLanguage>?,
)

@Service
class SiteTextService(
    @Autowired
    val ds: DataSource,

) {
    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    fun getSiteTextLanguages(): SiteTextLanguageResponse {
        var data: MutableList<SiteTextLanguage> = mutableListOf()

        val paramSource = MapSqlParameterSource()

        val query = """
            select l.id, l.ethnologue, tol.language_name
            from common.languages l
            left join sil.table_of_languages tol on st.ethnologue = tol.id
        """.replace('\n', ' ')

        try {
            val jdbcResult = jdbcTemplate.queryForRowSet(query, paramSource)
            while (jdbcResult.next()) {

                var id: Int = jdbcResult.getInt("id")

                var ethnologue: Int = jdbcResult.getInt("ethnologue")

                var language_name: String? = jdbcResult.getString("language_name")
                if (jdbcResult.wasNull()) language_name = null

                data.add(
                        SiteTextLanguage(
                                id = id,
                                ethnologue = ethnologue,
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
}
