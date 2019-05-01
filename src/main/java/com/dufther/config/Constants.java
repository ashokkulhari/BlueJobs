package com.dufther.config;

/**
 * Application constants.
 */
public final class Constants {

    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^[_.@A-Za-z0-9-]*$";

    public static final String SYSTEM_ACCOUNT = "system";
    public static final String ANONYMOUS_USER = "anonymousUser";
    public static final String DEFAULT_LANGUAGE = "en";
    
    
    public static final String PROFILE_STATUS_PENDING="pending";
    public static final String PROFILE_STATUS_ACTIVE="active";
    public static final String PROFILE_STATUS_DELETED="deleted";
    public static final String PROFILE_STATUS_HIDDEN="hidden";
    public static final String PROFILE_STATUS_SUSPENDED="suspended";
    private Constants() {
    }
}
