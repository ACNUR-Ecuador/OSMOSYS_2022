package com.sagatechs.generics.webservice.jsonSerializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.LocalTime;


public class LocalTimeDeserializer extends JsonDeserializer<LocalTime> {


	@Override
	public LocalTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {

		String time =jsonParser.getText();

		if(StringUtils.isEmpty(time)) return null;

		return LocalTime.parse(time);
	}
}