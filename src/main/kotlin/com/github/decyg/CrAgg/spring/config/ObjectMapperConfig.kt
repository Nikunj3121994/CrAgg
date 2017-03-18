package com.github.decyg.CrAgg.spring.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.elasticsearch.client.Client
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate
import org.springframework.data.elasticsearch.core.EntityMapper
import org.springframework.stereotype.Component

/**
 * Created by declan on 18/03/2017.
 */
@Configuration
@Component
@Primary
open class ObjectMapperConfig {

    @Primary
    @Bean
    fun objectMapper() : ObjectMapper{
        return jacksonObjectMapper()
    }


    class CustomEntityMapper() : EntityMapper {
        override fun <T : Any?> mapToObject(sourceStr: String?, sourceClass: Class<T>?): T {

            println(sourceStr)
            return jacksonObjectMapper().readValue(sourceStr, sourceClass)
        }

        override fun mapToString(valString: Any?): String {
            return jacksonObjectMapper().writeValueAsString(valString)
        }

    }

    @Bean
    @Autowired
    fun mapper(objectMapper : ObjectMapper) : EntityMapper {
        return CustomEntityMapper()
    }


    @Bean
    fun elasticsearchTemplate(client : Client, mapper : EntityMapper) : ElasticsearchOperations {
        return ElasticsearchTemplate(client, mapper)
    }
}