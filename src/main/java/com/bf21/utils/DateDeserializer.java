package com.bf21.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class DateDeserializer extends JsonDeserializer<Date> {

        private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        @Override
        public Date deserialize(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
            throws IOException, JsonProcessingException {
            String str = paramJsonParser.getText().trim();
            try {
                return dateFormat.parse(str);
            } catch (ParseException e) {
                // Handle exception here
                System.out.println(e.getMessage());
            }
            return paramDeserializationContext.parseDate(str);
        }
}
