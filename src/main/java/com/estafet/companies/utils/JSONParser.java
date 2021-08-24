package com.estafet.companies.utils;

import com.estafet.companies.configuration.ObjectMapperConfiguration;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom JSON parser util class, uses a preconfigured Object mapper.
 *
 * @see ObjectMapperConfiguration#objectMapper()
 */
@Configuration
public class JSONParser
{
    private static ObjectMapper objectMapper;

    public JSONParser(ObjectMapper objectMapper) { JSONParser.objectMapper = objectMapper;}

    public static <T> T parseString(String jsonContents, Class<T> className) throws IOException
    {
        JsonFactory factory = new JsonFactory();
        factory.setCodec(objectMapper);
        JsonParser parser = factory.createParser(jsonContents);
        return parser.readValueAs(className);
    }

    public static <T> List<T> parseList(byte[] jsonContents, Class<T> className) throws IOException
    {
        TypeFactory t = TypeFactory.defaultInstance();

        return objectMapper.readValue(jsonContents, t.constructCollectionType(ArrayList.class, className));
    }
}
