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

data class MasterRow(
    val priority: String,
    val language_name: String,
    val iso: String,
    val location_details: String,
    val island: String,
    val province: String,
    val language_population: String,
    val population_value: String,
    val egids_level: String,
    val egids_value: String,
    val least_reached_progress_level: String,
    val least_reached_progress_value: String,
    val partner_interest_value: String,
    val partner_interest_description: String,
    val partner_interest_source: String,
    val multi_lang_leverage_linguistic_value: String,
    val multi_lang_leverage_linguistic_description: String,
    val multi_lang_leverage_linguistic_source: String,
    val multi_lang_leverage_training_value: String,
    val multi_lang_leverage_training_description: String,
    val multi_lang_leverage_training_source: String,
    val community_interest_language_development_value: String,
    val community_interest_language_development_description: String,
    val community_interest_language_development_source: String,
    val community_interest_translation_value: String,
    val community_interest_translation_description: String,
    val community_interest_translation_source: String,
    val lwc_access_value: String,
    val lwc_access_description: String,
    val lwc_access_source: String,
    val begin_work_geo_challenges_value: String,
    val begin_work_geo_challenges_description: String,
    val begin_work_geo_challenges_source: String,
    val access_religious_value: String,
    val access_religious_description: String,
    val access_religious_source: String,
    val strategies: String,
    val comments: String,
)

data class LanguageExRow (
    val id: String? = null,
    val language_name: String? = null,
    val iso: String? = null,
    val prioritization: String? = null,
    val progress_bible: String? = null,
    val island: String? = null,
    val province: String? = null,
    val first_language_population: String? = null,
    val population_value: String? = null,
    val egids_level: String? = null,
    val egids_value: String? = null,
    val least_reached_progress_jps_level: String? = null,
    val least_reached_value: String? = null,
    val partner_interest_level: String? = null,
    val partner_interest_value: String? = null,
    val partner_interest_description: String? = null,
    val partner_interest_source: String? = null,
    val multiple_languages_leverage_linguistic_level: String? = null,
    val multiple_languages_leverage_linguistic_value: String? = null,
    val multiple_languages_leverage_linguistic_description: String? = null,
    val multiple_languages_leverage_linguistic_source: String? = null,
    val multiple_languages_leverage_joint_training_level: String? = null,
    val multiple_languages_leverage_joint_training_value: String? = null,
    val multiple_languages_leverage_joint_training_description: String? = null,
    val multiple_languages_leverage_joint_training_source: String? = null,
    val language_community_interest_in_language_development_level: String? = null,
    val language_community_interest_in_language_development_value: String? = null,
    val language_community_interest_in_language_development_description: String? = null,
    val language_community_interest_in_language_development_source: String? = null,
    val language_community_interest_in_scripture_translation_level: String? = null,
    val language_community_interest_in_scripture_translation_value: String? = null,
    val language_community_interest_in_scripture_translation_description: String? = null,
    val language_community_interest_in_scripture_translation_source: String? = null,
    val access_to_scripture_in_lwc_level: String? = null,
    val access_to_scripture_in_lwc_value: String? = null,
    val access_to_scripture_in_lwc_description: String? = null,
    val access_to_scripture_in_lwc_source: String? = null,
    val access_to_scripture_geographical_challenges_level: String? = null,
    val access_to_begin_work_geographical_challenges_value: String? = null,
    val access_to_begin_work_geographical_challenges_description: String? = null,
    val access_to_begin_work_geographical_challenges_source: String? = null,
    val access_to_begin_work_religious_and_political_obstacles_scale: String? = null,
    val access_to_begin_work_religious_and_political_obstacles_value: String? = null,
    val access_to_begin_work_religious_and_political_obstacles_description: String? = null,
    val access_to_begin_work_religious_and_political_obstacles_source: String? = null,
    val suggested_strategies: String? = null,
    val comments: String? = null,
    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
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

        var processed: List<LanguageExRow> = items.map {
            LanguageExRow(
                language_name = it.language_name.substringBefore('[').trim()
            )
        }

        writeCsvFile(processed, "./src/main/resources/local/language_ex.csv")


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