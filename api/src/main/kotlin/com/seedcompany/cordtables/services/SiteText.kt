package com.seedcompany.cordtables.services

import com.seedcompany.cordtables.common.ErrorType
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.sql.SQLException
import javax.sql.DataSource

data class SiteTextLanguage(
    val common_id: Int,
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
            select li.common_id, li.name
            from (
                select distinct(stt.language) as common_id
                from common.site_text_translations stt
            ) as stl
            inner join sil.language_index li on li.common_id = stl.common_id
        """.replace('\n', ' ')

        try {
            val jdbcResult = jdbcTemplate.queryForRowSet(query, paramSource)
            while (jdbcResult.next()) {

                var common_id: Int = jdbcResult.getInt("common_id")

                var language_name: String? = jdbcResult.getString("name")
                if (jdbcResult.wasNull()) language_name = null

                data.add(
                        SiteTextLanguage(
                                common_id = common_id,
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
