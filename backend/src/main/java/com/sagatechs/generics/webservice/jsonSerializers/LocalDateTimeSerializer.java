package com.sagatechs.generics.webservice.jsonSerializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SuppressWarnings("unused")
public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

	@Override
	public void serialize(LocalDateTime localDateTime, JsonGenerator generator, SerializerProvider serializers) throws IOException {
		if (localDateTime != null) {
			//DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			//generator.writeString(localDateTime.format(formatter));
			generator.writeString(DateTimeFormatter.ISO_DATE_TIME.format(localDateTime));
		}else {
			generator.writeNull();
		}
		
	}


}