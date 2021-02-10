package com.miro.widgets.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

public class JacksonUtility {
	public static String toJson(Object object) {
		ObjectMapper mapper = new ObjectMapper().registerModules(new ParameterNamesModule(), new Jdk8Module(), new JavaTimeModule()) ;
		try {
			return mapper.writer().writeValueAsString(object) ;
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e) ;
		}
	}
	
	public static <T> T fromJson(String json, Class<T> objectClass) {
		ObjectMapper mapper = new ObjectMapper().registerModules(new ParameterNamesModule(), new Jdk8Module(), new JavaTimeModule()) ;
		try {
			return mapper.readValue(json, objectClass) ;
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e) ;
		}
	}
}
