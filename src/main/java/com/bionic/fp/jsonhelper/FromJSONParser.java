package com.bionic.fp.jsonhelper;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.inject.Named;

/**
 * Created by boubdyk on 17.11.2015.
 */

@Named
public class FromJSONParser {

    public FromJSONParser() {}

    /**
     * @param input json file which need to parse
     * @return JSONObject
     */
    public JSONObject parse(final String input) {
        try {
            JSONParser parser = new JSONParser();
            Object obj = (Object)parser.parse(input);
            return (JSONObject) obj;
        } catch (ParseException pe) {
            pe.printStackTrace();
            return null;
        }
    }
}
