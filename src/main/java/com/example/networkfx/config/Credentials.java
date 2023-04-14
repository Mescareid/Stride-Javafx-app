package com.example.networkfx.config;

public class Credentials {
    public String getUrl() {
    return "jdbc:postgresql://localhost:5432/network";
}

    public String getUsername() {
        return "postgres";
    }

    public String getPassword() {
        return "master";
    }

}
