package com.gbossoufolly.elearningadmin.constants;

public class JWTUtils {
    public static final long EXPIRE_ACCESS_TOKEN = 60*1000;
    public static final long EXPIRE_REFRESH_TOKEN = 120*60*1000;
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String ISSUER = "elearning";
    public static final String SECRET = "bdZKebVduarVciW7ut7x5R3p8IE23RH0V7XxtVwqEEadb6IjHCFlOXrU6JbtreZ1b5g5OTf8pf9ZXHaKaMBC7Q==";

    public static final String AUTH_HEADER = "Authorization";
}
