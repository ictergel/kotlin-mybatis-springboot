package com.demo.web.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.server.WebServerFactoryCustomizer
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.stereotype.Component

@Component
@Configuration
@PropertySource("classpath:config/server.properties")
class ServerConfig : WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {
    @Value("\${server.port}")
    private val serverPort: String? = null

    @Value("\${server.servlet.context-path}")
    private val serverContextPath: String? = null

    override fun customize(server: ConfigurableServletWebServerFactory) {
        println("==========================" +  serverPort)
        server.setPort((serverPort ?: "8080").toInt())
        server.setContextPath(serverContextPath?:"/test")
    }
}