package com.bionic.fp.web.rest;

/**
 * Holds string constants for the client-server communication
 *
 * @author Sergiy Gabriel
 */
public interface RestConstants {

    //----------------------------------------
    //---------------  PATHS  ----------------
    //----------------------------------------

    String ACCOUNT = "/account";
    String EVENT = "/event";
    String COMMENT = "/comment";
//    String EVENT_TYPE = "type";
    String PHOTO = "/photo";
    String ROLE = "/role";
    String OWNER = "/owner";
    String LIST = "/list";

    //----------------------------------------
    //------  ATTRIBUTES (snake_case)  -------
    //----------------------------------------

    String FIELDS = "fields";
    // ID
    String ACCOUNT_ID = "account_id";
    String OWNER_ID = "owner_id";
    String USER_ID = "user_id";
    String ROLE_ID = "role_id";
    String EVENT_ID = "event_id";
    // EVENT
    String EVENTS = "events";
    String EVENT_NAME = "name";
    String EVENT_DESCRIPTION = "description";
    String EVENT_DATE = "date";
    String EVENT_EXPIRE_DATE = "expire_date";
    String EVENT_TYPE_ID = "type_id";
    String EVENT_LATITUDE = "lat";
    String EVENT_LONGITUDE = "lng";
    String EVENT_RADIUS = "radius";
    String EVENT_GEO = "geo";
    String EVENT_VISIBLE = "visible";
    String EVENT_PRIVATE = "private";
    String EVENT_PASSWORD = "password";

}
