package com.cordtables.v2.core

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.transaction.TransactionManager
import javax.sql.DataSource


@Configuration
class DataSourceConfig /*:AbstractJdbcConfiguration()*/ {

    var ds: DataSource? = null

    @Bean
    @ConfigurationProperties("spring.datasource")
    fun getDataSource(): DataSource {
        if (ds == null) {
            ds = DataSourceBuilder.create().build()
        }

        return ds!!
    }

//    @Bean
//    fun getJdbcTemplate(): JdbcTemplate {
//        return JdbcTemplate(getDataSource())
//    }
//
//    @Bean
//    fun namedParameterJdbcOperations(): NamedParameterJdbcOperations? {
//        return NamedParameterJdbcTemplate(getDataSource())
//    }
//
//    @Bean
//    fun transactionManager(dataSource: DataSource?): TransactionManager? {
//        return DataSourceTransactionManager(getDataSource())
//    }
}