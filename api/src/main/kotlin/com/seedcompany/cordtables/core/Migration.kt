package com.seedcompany.cordtables.core

import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.dataformat.csv.CsvSchema
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import java.io.FileReader
import java.io.FileWriter
import javax.sql.DataSource

enum class EgidsScale {
    `0`,
    `1`,
    `2`,
    `3`,
    `4`,
    `5`,
    `6a`,
    `6b`,
    `7`,
    `8a`,
    `8b`,
    `9`,
    `10`,
}

data class MasterRow(
    val priority: Float,
    val language_name: String,
    val iso: String,
    val location_details: String,
    val island: String,
    val province: String,
    val language_population: String,
    val population_value: Float,
    val egids_level: EgidsScale,
    val egids_value: Float,
    val least_reached_progress_level: String,
    val least_reached_progress_value: Float,
    val partner_interest_value: Float,
    val partner_interest_description: String,
    val partner_interest_source: String,
    val multi_lang_leverage_linguistic_value: Float,
    val multi_lang_leverage_linguistic_description: String,
    val multi_lang_leverage_linguistic_source: String,
    val multi_lang_leverage_training_value: Float,
    val multi_lang_leverage_training_description: String,
    val multi_lang_leverage_training_source: String,
    val community_interest_language_development_value: Float,
    val community_interest_language_development_description: String,
    val community_interest_language_development_source: String,
    val community_interest_translation_value: Float,
    val community_interest_translation_description: String,
    val community_interest_translation_source: String,
    val lwc_access_value: Float,
    val lwc_access_description: String,
    val lwc_access_source: String,
    val begin_work_geo_challenges_value: Float,
    val begin_work_geo_challenges_description: String,
    val begin_work_geo_challenges_source: String,
    val access_religious_value: Float,
    val access_religious_description: String,
    val access_religious_source: String,
    val strategies: String,
    val comments: String,
)

@Component
class Migration(
    @Autowired
    val appConfig: AppConfig,

    @Autowired
    val ds: DataSource,

    @Autowired
    val util: Utility,
) {
    val jdbcTemplate: JdbcTemplate = JdbcTemplate(ds)

    init {

        val items: List<MasterRow> = readCsvFile("./src/main/resources/local/master1.csv")

        items.forEach {

            val language_name = it.language_name.substringBefore('[').trim()

            val least_reached_progress_level = if (it.least_reached_progress_level.isEmpty()) null else it.least_reached_progress_level

            var partner_interest_level: String? = null
            if (it.partner_interest_value.equals(0.00)) partner_interest_level = "No Partner Interest"
            if (it.partner_interest_value.equals(0.33)) partner_interest_level = "Some"
            if (it.partner_interest_value.equals(0.66)) partner_interest_level = "Significant"
            if (it.partner_interest_value.equals(1.00)) partner_interest_level = "Considerable"

            var multi_lang_leverage_linguistic_level: String? = null
            if (it.multi_lang_leverage_linguistic_value.equals(0.0)) multi_lang_leverage_linguistic_level = "None"
            if (it.multi_lang_leverage_linguistic_value.equals(0.2)) multi_lang_leverage_linguistic_level = "Some"
            if (it.multi_lang_leverage_linguistic_value.equals(0.4)) multi_lang_leverage_linguistic_level = "Significant"
            if (it.multi_lang_leverage_linguistic_value.equals(0.6)) multi_lang_leverage_linguistic_level = "Considerable"
            if (it.multi_lang_leverage_linguistic_value.equals(0.8)) multi_lang_leverage_linguistic_level = "Large"
            if (it.multi_lang_leverage_linguistic_value.equals(1.0)) multi_lang_leverage_linguistic_level = "Vast"

            var community_interest_translation_level: String? = null
            if (it.community_interest_translation_value.equals(0.00)) community_interest_translation_level = "No Interest"
            if (it.community_interest_translation_value.equals(0.25)) community_interest_translation_level = "Some"
            if (it.community_interest_translation_value.equals(0.50)) community_interest_translation_level = "Expressed Need"
            if (it.community_interest_translation_value.equals(0.75)) community_interest_translation_level = "Significant"
            if (it.community_interest_translation_value.equals(1.00)) community_interest_translation_level = "Considerable"

            var lwc_access_level: String? = null
            if (it.lwc_access_value.equals(0.00)) lwc_access_level = "Full Access"
            if (it.lwc_access_value.equals(0.16)) lwc_access_level = "Vast Majority"
            if (it.lwc_access_value.equals(0.33)) lwc_access_level = "Large Majority"
            if (it.lwc_access_value.equals(0.50)) lwc_access_level = "Majority"
            if (it.lwc_access_value.equals(0.66)) lwc_access_level = "Significant"
            if (it.lwc_access_value.equals(0.83)) lwc_access_level = "Some"
            if (it.lwc_access_value.equals(1.00)) lwc_access_level = "Few"

            var begin_work_geo_challenges_level: String? = null
            if (it.begin_work_geo_challenges_value .equals(-0.50)) begin_work_geo_challenges_level = "None"
            if (it.begin_work_geo_challenges_value.equals(0.00)) begin_work_geo_challenges_level = "Very Difficult"
            if (it.begin_work_geo_challenges_value.equals(0.33)) begin_work_geo_challenges_level = "Difficult"
            if (it.begin_work_geo_challenges_value.equals(0.66)) begin_work_geo_challenges_level = "Moderate"
            if (it.begin_work_geo_challenges_value.equals(1.00)) begin_work_geo_challenges_level = "Easy"

            this.ds.connection.use { conn ->
                val statement = conn.prepareStatement("""
                    insert into sc.languages_ex(
                        language_name,
                        iso,
                        
                        location_long,
                        island,
                        province,
                        
                        first_language_population,
                        population_value,
                        
                        egids_level,
                        egids_value,
                        
                        least_reached_progress_jps_level,
                        least_reached_value,
                        
                        partner_interest_level,
                        partner_interest_value,
                        partner_interest_description,
                        partner_interest_source,
                        
                        multiple_languages_leverage_linguistic_level,
                        multiple_languages_leverage_linguistic_value,
                        multiple_languages_leverage_linguistic_description,
                        multiple_languages_leverage_linguistic_source,
                        
                        lang_comm_int_in_scripture_translation_level,
                        lang_comm_int_in_scripture_translation_value,
                        lang_comm_int_in_scripture_translation_description,
                        lang_comm_int_in_scripture_translation_source,
                        
                        access_to_scripture_in_lwc_level,
                        access_to_scripture_in_lwc_value,
                        access_to_scripture_in_lwc_description,
                        access_to_scripture_in_lwc_source,
                        
                        begin_work_geo_challenges_level,
                        begin_work_geo_challenges_value,
                        begin_work_geo_challenges_description,
                        begin_work_geo_challenges_source,

                        created_by,
                        modified_by,
                        owning_person,
                        owning_group
                    )
                    values (
                        ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,
                        1,1,1,1
                    );
                """.trimIndent())

                statement.setString(1, language_name)
                statement.setString(2, it.iso)

                statement.setString(3, it.location_details)
                statement.setString(4, it.island)
                statement.setString(5, it.province)

                statement.setInt(6, Integer.parseInt(it.language_population.replace(",", "")))
                statement.setFloat(7, it.population_value)

                statement.setObject(8, it.egids_level, java.sql.Types.OTHER)
                statement.setFloat(9, it.egids_value)

                statement.setObject(10, least_reached_progress_level, java.sql.Types.OTHER)
                statement.setFloat(11, it.least_reached_progress_value)

                statement.setObject(12, partner_interest_level, java.sql.Types.OTHER)
                statement.setFloat(13, it.partner_interest_value)
                statement.setString(14, it.partner_interest_description)
                statement.setString(15, it.partner_interest_source)

                statement.setObject(16, multi_lang_leverage_linguistic_level, java.sql.Types.OTHER)
                statement.setFloat(17, it.multi_lang_leverage_linguistic_value)
                statement.setString(18, it.multi_lang_leverage_linguistic_description)
                statement.setString(19, it.multi_lang_leverage_linguistic_source)

                statement.setObject(20, community_interest_translation_level, java.sql.Types.OTHER)
                statement.setFloat(21, it.community_interest_translation_value)
                statement.setString(22, it.community_interest_translation_description)
                statement.setString(23, it.community_interest_translation_source)

                statement.setObject(24, lwc_access_level, java.sql.Types.OTHER)
                statement.setFloat(25, it.lwc_access_value)
                statement.setString(26, it.lwc_access_description)
                statement.setString(27, it.lwc_access_source)

                statement.setObject(28, begin_work_geo_challenges_level, java.sql.Types.OTHER)
                statement.setFloat(29, it.begin_work_geo_challenges_value)
                statement.setString(30, it.begin_work_geo_challenges_description)
                statement.setString(31, it.begin_work_geo_challenges_source)

                statement.execute()
            }


        }
    }

}

val csvMapper = CsvMapper().apply {
    registerModule(KotlinModule())
}

inline fun <reified T> readCsvFile(fileName: String): List<T> {
    FileReader(fileName).use { reader ->
        return csvMapper
            .readerFor(T::class.java)
            .with(CsvSchema.emptySchema().withHeader())
            .readValues<T>(reader)
            .readAll()
            .toList()
    }
}

private inline fun <reified T> writeCsvFile(data: Collection<T>, fileName: String) {
    FileWriter(fileName).use { writer ->
        csvMapper.writer(csvMapper.schemaFor(T::class.java).withHeader())
            .writeValues(writer)
            .writeAll(data)
            .close()
    }
}