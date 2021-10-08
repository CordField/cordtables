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

    init {

        val items: List<MasterRow> = readCsvFile("./src/main/resources/local/master1.csv")

        items.forEach {

            var partner_interest_level = ""
            if (it.partner_interest_value.equals(0.00)) partner_interest_level = "No Partner Interest"
            if (it.partner_interest_value.equals(0.33)) partner_interest_level = "Some"
            if (it.partner_interest_value.equals(0.66)) partner_interest_level = "Significant"
            if (it.partner_interest_value.equals(1.00)) partner_interest_level = "Considerable"

            var multi_lang_leverage_linguistic_level = ""
            if (it.multi_lang_leverage_linguistic_value.equals(0.0)) multi_lang_leverage_linguistic_level = "None"
            if (it.multi_lang_leverage_linguistic_value.equals(0.2)) multi_lang_leverage_linguistic_level = "Some"
            if (it.multi_lang_leverage_linguistic_value.equals(0.4)) multi_lang_leverage_linguistic_level = "Significant"
            if (it.multi_lang_leverage_linguistic_value.equals(0.6)) multi_lang_leverage_linguistic_level = "Considerable"
            if (it.multi_lang_leverage_linguistic_value.equals(0.8)) multi_lang_leverage_linguistic_level = "Large"
            if (it.multi_lang_leverage_linguistic_value.equals(1.0)) multi_lang_leverage_linguistic_level = "Vast"

//            var multi_lang_leverage_training_level = ""
//            if (it.multi_lang_leverage_training_value .equals(0.0)) multi_lang_leverage_training_level = "None"
//            if (it.multi_lang_leverage_training_value.equals(0.2)) multi_lang_leverage_training_level = "Some"
//            if (it.multi_lang_leverage_training_value.equals(0.4)) multi_lang_leverage_training_level = "Significant"
//            if (it.multi_lang_leverage_training_value.equals(0.6)) multi_lang_leverage_training_level = "Considerable"
//            if (it.multi_lang_leverage_training_value.equals(0.8)) multi_lang_leverage_training_level = "Large"
//            if (it.multi_lang_leverage_training_value.equals(1.0)) multi_lang_leverage_training_level = "Vast"

            var community_interest_translation_level = ""
            if (it.community_interest_translation_value.equals(0.00)) community_interest_translation_level = "No Interest"
            if (it.community_interest_translation_value.equals(0.25)) community_interest_translation_level = "No Partner Interest"
            if (it.community_interest_translation_value.equals(0.50)) community_interest_translation_level = "Some"
            if (it.community_interest_translation_value.equals(0.75)) community_interest_translation_level = "Significant"
            if (it.community_interest_translation_value.equals(1.00)) community_interest_translation_level = "Considerable"



            val language_name = it.language_name.substringBefore('[').trim()

            jdbcTemplate.update(
                """
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
                    
                    access_to_scripture_in_lwc_value,
                    
                    begin_work_geo_challenges_value,
                    
                    begin_work_rel_pol_obstacles_value,

                    created_by,
                    modified_by,
                    owning_person,
                    owning_group
                )
                values (
                    ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,
                    1,1,1,1
                );
            """.trimIndent(),
                language_name,
                it.iso,

                it.location_details,
                it.island,
                it.province,

                Integer.parseInt(it.language_population.replace(",", "")),
                it.population_value,

                it.egids_level,
                it.egids_value,

                it.least_reached_progress_level,
                it.least_reached_progress_value,

                partner_interest_level,
                it.partner_interest_value,
                it.partner_interest_description,
                it.partner_interest_source,

                multi_lang_leverage_linguistic_level,
                it.multi_lang_leverage_linguistic_value,
                it.multi_lang_leverage_linguistic_description,
                it.multi_lang_leverage_linguistic_source,

                community_interest_translation_level,
                it.community_interest_translation_value,
                it.community_interest_translation_description,
                it.community_interest_translation_source,

                it.lwc_access_value,

                it.begin_work_geo_challenges_value,

                it.access_religious_value,
            )
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