package org.ac.cst8277.williams.roy.util;

public class JwtResponse {

    private String jwttoken;

    public JwtResponse() {}

    public JwtResponse(String jwttoken) {
        this.jwttoken = jwttoken;
    }

    public String getToken() {
        return this.jwttoken;
    }
}
