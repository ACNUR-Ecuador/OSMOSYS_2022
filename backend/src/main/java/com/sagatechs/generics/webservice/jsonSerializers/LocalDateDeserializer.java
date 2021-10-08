package com.sagatechs.generics.webservice.jsonSerializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class LocalDateDeserializer extends JsonDeserializer<LocalDate> {


	@Override
	public LocalDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {

		String date =jsonParser.getText();

		if(StringUtils.isEmpty(date)) return null;
//2019-11-19T00:00:00.000Z

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

		return LocalDate.parse(date, formatter);
	}
}