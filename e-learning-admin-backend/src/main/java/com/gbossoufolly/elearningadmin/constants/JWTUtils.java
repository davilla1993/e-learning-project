package com.gbossoufolly.elearningadmin.constants;

public class JWTUtils {
    public static final long EXPIRE_ACCESS_TOKEN = 10 * 60 * 1000;  // 10 minutes
    public static final long EXPIRE_REFRESH_TOKEN = 24 * 60 * 60 * 1000;  // 1 jour
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String ISSUER = "elearning";
    public static final String SECRET = "bdZKebVduarVciW7ut7x5R3p8IE23RH0V7XxtVwqEEadb6IjHCFlOXrU6JbtreZ1b5g5OTf8pf9ZXHaKaMBC7Q==";

    public static final String AUTH_HEADER = "Authorization";
}
