package com.bionic.fp;

public final class Constants {

	public static class RoleConstants {
		public final static Integer OWNER = 1;
		public final static Integer ADMIN = 2;
		public final static Integer MEMBER = 3;
	}


	/**
     * Holds string constants for the client-server communication
     *
     * @author Sergiy Gabriel
     */
    public static final class RestConstants {

        public interface PATH {
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
            String EMAIL = "email";
            String PASSWORD = "password";
        }

        public interface ROLE {
            String ID = "role_id";
        }

        public interface PHOTO {
            String ID = "photo_id";
            String FILE = "file";
            String NAME = "name";
            String DESCRIPTION = "description";
            String LIST = "photos";
        }

        public interface COMMENT {
            String ID = "comment_id";
            String TEXT = "comment_text";
        }

    }
}
