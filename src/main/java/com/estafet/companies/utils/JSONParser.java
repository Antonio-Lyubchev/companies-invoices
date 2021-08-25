package com.estafet.companies.utils;

import com.estafet.companies.configuration.ObjectMapperConfiguration;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.context.annotation.Scope;

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
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class JSONParser
{
    @Autowired
    private ObjectMapper objectMapper;

    public <T> T fromJsonToObject(String jsonContents, Class<T> className) throws IOException
    {
        JsonFactory factory = new JsonFactory();
        factory.setCodec(objectMapper);
        JsonParser parser = factory.createParser(jsonContents);
        return parser.readValueAs(className);
    }

    public <T> List<T> fromJsonToList(byte[] jsonContents, Class<T> className) throws IOException
    {
        TypeFactory t = TypeFactory.defaultInstance();

        return objectMapper.readValue(jsonContents, t.constructCollectionType(ArrayList.class, className));
    }

    public String fromObjectToJsonString(Object obj)
    {
        try
        {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e)
        {
            throw new RuntimeException(e);
        }
    }

    public <T> String fromObjectListToJsonString(List<T> objectList)
    {
        try
        {
            return objectMapper.writeValueAsString(objectList);
        } catch (JsonProcessingException e)
        {
            throw new RuntimeException(e);
        }
    }


    /**
     * Setter in case we are using the mapper out of Spring context
     *
     * @param objectMapper preferably instantiated from the preconfigured {@link com.estafet.companies.configuration.ObjectMapperConfiguration}
     */
    public void setObjectMapper(ObjectMapper objectMapper)
    {
        this.objectMapper = objectMapper;
    }
}
