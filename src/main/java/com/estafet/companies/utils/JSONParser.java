package com.estafet.companies.utils;

import com.estafet.companies.configuration.ObjectMapperConfiguration;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom JSON parser util class, uses a preconfigured Object mapper.
 * JSONComponent annotation includes this class in context when running WebMvcTest
 *
 * @see ObjectMapperConfiguration#objectMapper()
 */
@JsonComponent
public class JSONParser
{
    private static ObjectMapper objectMapper;

    @Autowired
    private JSONParser(ObjectMapper objectMapper) { JSONParser.objectMapper = objectMapper;}

    public static <T> T fromJsonToObject(String jsonContents, Class<T> className) throws IOException
    {
        JsonFactory factory = new JsonFactory();
        factory.setCodec(objectMapper);
        JsonParser parser = factory.createParser(jsonContents);
        return parser.readValueAs(className);
    }

    public static <T> List<T> fromJsonToList(byte[] jsonContents, Class<T> className) throws IOException
    {
        TypeFactory t = TypeFactory.defaultInstance();

        return objectMapper.readValue(jsonContents, t.constructCollectionType(ArrayList.class, className));
    }

    public static String fromObjectToJsonString(Object obj) throws JsonProcessingException
    {
        return objectMapper.writeValueAsString(obj);
    }

    public static <T> String fromObjectListToJsonString(List<T> objectList) throws JsonProcessingException
    {
        return objectMapper.writeValueAsString(objectList);
    }
}
