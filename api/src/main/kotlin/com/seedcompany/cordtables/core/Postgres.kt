package com.seedcompany.cordtables.core

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.queryForObject
import org.springframework.stereotype.Component
import org.springframework.util.FileCopyUtils
import java.io.IOException
import java.io.InputStreamReader
import java.io.UncheckedIOException
import javax.sql.DataSource

@Component("BootstrapDB")
class BootstrapDB(
    @Autowired
    val appConfig: AppConfig,

    @Autowired
    val ds: DataSource,

    @Autowired
    val util: Utility,

    @Autowired
    val vc: DatabaseVersionControl,

    ) {
    val jdbcTemplate: JdbcTemplate = JdbcTemplate(ds)

    init {
        vc.initDatabase()
    }
}

@Configuration
class DataSourceConfiguration {
    @Bean
    @ConfigurationProperties("spring.datasource")
    fun cfDataSource(): DataSource {
        return DataSourceBuilder.create().build()
    }
}
