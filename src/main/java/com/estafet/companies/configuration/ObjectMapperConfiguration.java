package com.estafet.companies.configuration;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.format.DateTimeFormatter;

/**
 * Custom configuration for JSON objectmapper
 *
 * @see com.estafet.companies.utils.JSONParser
 */
@Configuration
public class ObjectMapperConfiguration
{
    private static final String DATETIME_FORMAT = "dd-MM-yyyy HH:mm";
    private static final LocalDateTimeSerializer LOCAL_DATETIME_SERIALIZER = new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DATETIME_FORMAT));

    @Bean
    @Primary
    public ObjectMapper objectMapper()
    {
        JavaTimeModule module = new JavaTimeModule();
        module.addSerializer(LOCAL_DATETIME_SERIALIZER);

        ObjectMapper mapper = new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .registerModule(module)
                .enable(SerializationFeature.INDENT_OUTPUT)
                .disable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
                .enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        mapper.configOverride(java.util.Date.class)
                .setFormat(JsonFormat.Value.forPattern("dd-MM-yyyy"));

        return mapper;
    }
}
