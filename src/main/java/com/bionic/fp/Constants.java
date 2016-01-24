package com.bionic.fp;

public final class Constants {

    public static final String FILE_SEPERATOR = System.getProperty("file.separator");

	public static class RoleConstants {
		public final static Long OWNER = 1L;
		public final static Long ADMIN = 2L;
		public final static Long MEMBER = 3L;
	}


	/**
     * Holds string constants for the client-server communication
     *
     * @author Sergiy Gabriel
     */
    public static final class RestConstants {

        public interface PATH {
            String API = "/api";
            String V1 = "/v1";

            String AUTH = "/auth";
            String ACCOUNTS = "/accounts";
            String ACCOUNT_ID = "/{"+ ACCOUNT.ID + ":[\\d]+}";
            String EVENTS = "/events";
            String EVENT_ID = "/{"+ EVENT.ID + ":[\\d]+}";
            String COMMENTS = "/comments";
            String COMMENT_ID = "/{"+ COMMENT.ID + ":[\\d]+}";
            String PHOTOS = "/photos";
            String PHOTO_ID = "/{"+ PHOTO.ID + ":[\\d]+}";
            String ROLES = "/roles";
            String ROLE_ID = "/{"+ ROLE.ID + ":[\\d]+}";

            String OWNER = "/owner";
            String LOGIN = "/login";
            String LOGOUT = "/logout";
            String REGISTER = "/register";
            String FB = "/fb";
            String VK = "/vk";
            String FILE = "/file";
            String ID = "/id";
            String SELF = "/self";
        }

        public interface PARAM {
            String FIELDS = "fields";
            String OWNER_ID = "owner_id";
            String USER_ID = "user_id";
            String FB_ID = "fbId";
            String FB_TOKEN = "token";
            String VK_ID = "vkId";
            String VK_TOKEN = "token";
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
            String EMAIL = "email";
            String PASSWORD = "password";
            String TOKEN = "token";
            String USERNAME = "username";
            String IMAGE_URL = "image_url";
            String LIST = "accounts";
            String SOCIAL_ID = "social_id";
            String LAST_NAME = "last_name";
            String FIRST_NAME = "first_name";
        }

        public interface ROLE {
            String ID = "role_id";
            String NAME = "role";
            String CAN_ASSIGN_ROLES = "can_assign_roles";
            String CAN_CHANGE_SETTINGS = "can_change_settings";
            String CAN_VIEW_PHOTOS = "can_view_photos";
            String CAN_ADD_COMMENTS = "can_add_comments";
            String CAN_VIEW_COMMENTS = "can_view_comments";
            String CAN_ADD_PHOTOS = "can_add_photos";
            String LIST = "roles";
        }

        public interface PHOTO {
            String ID = "photo_id";
            String FILE = "file";
            String URL = "url";
            String NAME = "name";
            String DESCRIPTION = "description";
            String LIST = "photos";
        }

        public interface COMMENT {
            String ID = "comment_id";
            String TEXT = "comment_text";
            String AUTHOR_ID = "author_id";
            String DATE = "date";
            String LIST = "comments";
        }

    }
}
