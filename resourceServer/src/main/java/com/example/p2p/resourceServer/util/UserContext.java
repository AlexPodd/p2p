package com.example.p2p.resourceServer.util;

import org.springframework.security.core.context.SecurityContextHolder;

public class UserContext {
        public String getUserName() {
            return SecurityContextHolder.getContext().getAuthentication().getName();
        }
}
