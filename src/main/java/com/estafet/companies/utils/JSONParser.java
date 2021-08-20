package com.estafet.companies.utils;

import java.io.IOException;

import com.estafet.companies.model.InvoiceRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JSONParser {
	
	private ObjectMapper objectMapper;
	
	public JSONParser() {
		this.objectMapper = new ObjectMapper();
		this.objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		this.objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
		this.objectMapper.configOverride(java.util.Date.class)
	       .setFormat(JsonFormat.Value.forPattern("dd-MM-yyyy"));
	
	}
	
	
	public <T> T parseString(String jsonContents, Class<T> theclass) throws IOException, JsonParseException{
		JsonFactory factory = new JsonFactory();
		factory.setCodec(this.objectMapper);
		JsonParser parser = factory.createParser(jsonContents);
		return parser.readValueAs(theclass);
	}

}
