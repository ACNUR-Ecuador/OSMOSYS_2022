package com.sagatechs.generics.webservice.jsonSerializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalTime;


public class LocalTimeSerializer extends JsonSerializer<LocalTime> {

	@Override
	public void serialize(LocalTime localTime, JsonGenerator generator, SerializerProvider serializers) throws IOException {
		if (localTime != null) {
			generator.writeString(localTime.toString());
		}else {
			generator.writeNull();
		}
		
	}


}