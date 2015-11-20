package com.bionic.fp.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDateTime;

import static com.bionic.fp.util.DateTimeFormatterConstants.LOCAL_DATE_TIME;

/**
 * This class serializes {@link LocalDateTime} into JSON
 * <p>@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") doesn't work</p>
 *
 * @author Sergiy Gabriel
 */
public class LocalDateTimeJsonSerializer extends JsonSerializer<LocalDateTime> {

    @Override
    public void serialize(LocalDateTime value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        jgen.writeString(value.format(LOCAL_DATE_TIME));
    }
}
