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
    val c1: String,
    val c2: String,
    val c3: String,
    val c4: String,
    val c5: String,
    val c6: String,
    val c7: String,
    val c8: String,
    val c9: String,
    val c10: String,
    val c11: String,
    val c12: String,
    val c13: String,
    val c14: String,
    val c15: String,
    val c16: String,
    val c17: String,
    val c18: String,
    val c19: String,
    val c20: String,
    val c21: String,
    val c22: String,
    val c23: String,
    val c24: String,
    val c25: String,
    val c26: String,
    val c27: String,
    val c28: String,
    val c29: String,
    val c30: String,
    val c31: String,
    val c32: String,
    val c33: String,
    val c34: String,
    val c35: String,
    val c36: String,
    val c37: String,
    val c38: String,
)

data class LanguageExRow (
    val language_name: String,
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
                language_name = it.c1
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