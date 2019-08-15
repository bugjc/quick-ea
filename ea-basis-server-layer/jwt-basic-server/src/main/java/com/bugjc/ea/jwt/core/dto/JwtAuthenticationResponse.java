package com.bugjc.ea.jwt.core.dto;

import java.io.Serializable;

/**
 *
 * @author aoki
 */
public class JwtAuthenticationResponse implements Serializable {

    private final String token;

    public JwtAuthenticationResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }
}
