package com.demo.base.config

import com.alibaba.druid.pool.DruidDataSource
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.PropertySource
import java.sql.SQLException
import javax.sql.DataSource
import org.springframework.jdbc.datasource.DataSourceTransactionManager




@Configuration
@PropertySource("classpath:config/db.properties")
class DruidSource {
    private val logger = LoggerFactory.getLogger(DruidSource::class.java)

    @Value("\${spring.datasource.url}")
    private val dbUrl: String? = null

    @Value("\${spring.datasource.username}")
    private val username: String? = null

    @Value("\${spring.datasource.password}")
    private val password: String? = null

    @Value("\${spring.datasource.driverClassName}")
    private val driverClassName: String? = null

    @Value("\${spring.datasource.initialSize}")
    private val initialSize: Int = 0

    @Value("\${spring.datasource.minIdle}")
    private val minIdle: Int = 0

    @Value("\${spring.datasource.maxActive}")
    private val maxActive: Int = 0

    @Value("\${spring.datasource.maxWait}")
    private val maxWait: Int = 0

    @Value("\${spring.datasource.timeBetweenEvictionRunsMillis}")
    private val timeBetweenEvictionRunsMillis: Int = 0

    @Value("\${spring.datasource.minEvictableIdleTimeMillis}")
    private val minEvictableIdleTimeMillis: Int = 0

    @Value("\${spring.datasource.validationQuery}")
    private val validationQuery: String? = null

    @Value("\${spring.datasource.testWhileIdle}")
    private val testWhileIdle: Boolean = false

    @Value("\${spring.datasource.testOnBorrow}")
    private val testOnBorrow: Boolean = false

    @Value("\${spring.datasource.testOnReturn}")
    private val testOnReturn: Boolean = false

    @Value("\${spring.datasource.poolPreparedStatements}")
    private val poolPreparedStatements: Boolean = false

    @Value("\${spring.datasource.maxPoolPreparedStatementPerConnectionSize}")
    private val maxPoolPreparedStatementPerConnectionSize: Int = 0

    @Value("\${spring.datasource.filters}")
    private val filters: String? = null

    @Value("{spring.datasource.connectionProperties}")
    private val connectionProperties: String? = null

    @Bean     //声明其为Bean实例
    @Primary  //在同样的DataSource中，首先使用被标注的DataSource
    fun dataSource(): DataSource {
        val datasource = DruidDataSource()

        datasource.url = this.dbUrl
        datasource.username = username
        datasource.password = password
        datasource.driverClassName = driverClassName

        //configuration
        datasource.initialSize = initialSize
        datasource.minIdle = minIdle
        datasource.maxActive = maxActive
        datasource.maxWait = maxWait.toLong()
        datasource.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis.toLong()
        datasource.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis.toLong()
        datasource.validationQuery = validationQuery
        datasource.isTestWhileIdle = testWhileIdle
        datasource.isTestOnBorrow = testOnBorrow
        datasource.isTestOnReturn = testOnReturn
        datasource.isPoolPreparedStatements = poolPreparedStatements
        datasource.maxPoolPreparedStatementPerConnectionSize = maxPoolPreparedStatementPerConnectionSize
        try {
            datasource.setFilters(filters)
        } catch (e: SQLException) {
            logger.error("druid configuration initialization filter : {0}", e)
        }
        datasource.setConnectionProperties(connectionProperties)
        return datasource
    }

    @Bean
    @Primary
    @Throws(SQLException::class)  //配置事物管理
    fun masterTransactionManager(): DataSourceTransactionManager {
        return DataSourceTransactionManager(dataSource())
    }
}