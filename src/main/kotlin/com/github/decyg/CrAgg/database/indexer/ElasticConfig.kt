package com.github.decyg.CrAgg.database.indexer

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.elasticsearch.client.Client
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate
import org.springframework.data.elasticsearch.core.EntityMapper
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories
import java.net.InetAddress

/**
 * Created by declan on 17/03/2017.
 */
@Configuration
@EnableElasticsearchRepositories(basePackages = arrayOf("com.github.decyg.CrAgg.database.indexer"))
class ElasticConfig {


    @Bean
    fun client() : Client {
        return TransportClient.builder()
                .settings(Settings.EMPTY)
                .build()
                .addTransportAddress(
                        InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300)
                )

    }

}