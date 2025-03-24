package com.spring.spring.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class LoginResponse {
    private String status;
    private String message;
    private User user;
    private String access_token;    
    private String token_type;      
    private int expires_in;        
}