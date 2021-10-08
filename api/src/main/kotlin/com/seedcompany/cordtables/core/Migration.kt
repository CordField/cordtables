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
    val priority: Float,
    val language_name: String,
    val iso: String,
    val location_details: String,
    val island: String,
    val province: String,
    val language_population: String,
    val population_value: Float,
    val egids_level: String,
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

//    init {
//
//        val items: List<MasterRow> = readCsvFile("./src/main/resources/local/master1.csv")
//
//        items.forEach {
//
//            val language_name = it.language_name.substringBefore('[').trim()
//
//            jdbcTemplate.update(
//                """
//                insert into sc.languages_ex(
//                    language_name,
//                    population_value,
//                    egids_value,
//                    least_reached_value,
//                    partner_interest_value,
//                    multiple_languages_leverage_linguistic_value,
//                    multiple_languages_leverage_joint_training_value,
//                    lang_comm_int_in_language_development_value,
//                    lang_comm_int_in_scripture_translation_value,
//                    access_to_scripture_in_lwc_value,
//                    begin_work_geo_challenges_value,
//                    begin_work_rel_pol_obstacles_value,
//
//                    created_by,
//                    modified_by,
//                    owning_person,
//                    owning_group
//                )
//                values (
//                    ?,
//                    ?,
//                    ?,
//                    ?,
//                    ?,
//                    ?,
//                    ?,
//                    ?,
//                    ?,
//                    ?,
//                    ?,
//                    ?,
//
//                    1,
//                    1,
//                    1,
//                    1
//                );
//            """.trimIndent(),
//                language_name,
//                it.population_value,
//                it.egids_value,
//                it.least_reached_progress_value,
//                it.partner_interest_value,
//                it.multi_lang_leverage_linguistic_value,
//                it.multi_lang_leverage_training_value,
//                it.community_interest_language_development_value,
//                it.community_interest_translation_value,
//                it.lwc_access_value,
//                it.begin_work_geo_challenges_value,
//                it.access_religious_value,
//            )
//        }
//    }

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