package com.sagatechs.generics.webservice.jsonSerializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class LocalDateSerializer extends JsonSerializer<LocalDate> {

	@Override
	public void serialize(LocalDate localDate, JsonGenerator generator, SerializerProvider serializers) throws IOException {
		if (localDate != null) {
			generator.writeString(localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'00:00:00.000")));
		} else {
			generator.writeNull();
		}
		
	}


}