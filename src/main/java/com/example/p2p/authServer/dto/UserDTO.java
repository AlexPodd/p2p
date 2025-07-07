package com.example.p2p.authServer.dto;

public class UserDTO {
    public String username;
    public String password;

    public UserDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
