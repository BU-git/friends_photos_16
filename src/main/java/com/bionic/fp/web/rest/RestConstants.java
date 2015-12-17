package com.bionic.fp.web.rest;

/**
 * Holds string constants for the client-server communication
 *
 * @author Sergiy Gabriel
 */
public final class RestConstants {

    public interface PATH {
        String ACCOUNTS = "/accounts";
        String ACCOUNT_ID = "/{"+ RestConstants.ACCOUNT.ID + ":[\\d]+}";
        String EVENTS = "/events";
        String EVENT_ID = "/{"+ RestConstants.EVENT.ID + ":[\\d]+}";
        String COMMENTS = "/comments";
        String COMMENT_ID = "/{"+ RestConstants.COMMENT.ID + ":[\\d]+}";
        String PHOTOS = "/photos";
        String PHOTO_ID = "/{"+ RestConstants.PHOTO.ID + ":[\\d]+}";
        String ROLES = "/roles";
        String ROLE_ID = "/{"+ RestConstants.ROLE.ID + ":[\\d]+}";

        String OWNER = "/owner";
        String LOGIN = "/login";
        String LOGOUT = "/logout";
        String REGISTER = "/register";
        String FB = "/fb";
        String VK = "/vk";
        String FILE = "/file";
    }

    public interface PARAM {
        String FIELDS = "fields";
        String OWNER_ID = "owner_id";
        String USER_ID = "user_id";
        String FB_ID = "fbId";
        String FB_TOKEN = "fbToken";
        String VK_ID = "vkId";
        String VK_TOKEN = "vkToken";
    }

    public interface EVENT {
        String ID = "event_id";
        String NAME = "name";
        String DESCRIPTION = "description";
        String DATE = "date";
        String EXPIRE_DATE = "expire_date";
        String TYPE_ID = "type_id";
        String LATITUDE = "lat";
        String LONGITUDE = "lng";
        String RADIUS = "radius";
        String GEO = "geo";
        String VISIBLE = "visible";
        String PRIVATE = "private";
        String PASSWORD = "password";
        String LIST = "events";
    }

    public interface ACCOUNT {
        String ID = "account_id";
    }

    public interface ROLE {
        String ID = "role_id";
    }

    public interface PHOTO {
        String ID = "photo_id";
        String FILE = "file";
        String NAME = "name";
        String DESCRIPTION = "description";

    }

    public interface COMMENT {
        String ID = "comment_id";
    }

}
