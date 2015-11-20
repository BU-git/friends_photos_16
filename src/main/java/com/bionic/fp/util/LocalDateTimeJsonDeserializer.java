package com.bionic.fp.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;

import static com.bionic.fp.util.DateTimeFormatterConstants.LOCAL_DATE_TIME;

/**
 * This class deserializes JSON into {@link LocalDateTime}
 * <p>@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") doesn't work</p>
 *
 * @author Sergiy Gabriel
 */
public class LocalDateTimeJsonDeserializer extends JsonDeserializer<LocalDateTime> {

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext cxt) throws IOException, JsonProcessingException {
        return LocalDateTime.parse(p.getText(), LOCAL_DATE_TIME);
    }
}
