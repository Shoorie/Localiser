package database;

import android.provider.BaseColumns;

public final class UserDatabase {
    private UserDatabase(){}

    public static final class User implements BaseColumns {
        private User(){}
        public static final String USER_TABLE_NAME = "UserData";
        public static final String USER_ID = "clientId";
        public static final String USER_LOGIN = "login";
        public static final String USER_PASSWORD = "password";
        public static final String USER_EMAIL = "email";
        public static final String USER_TELEPHONE = "telephoneNumber";
    }
}
