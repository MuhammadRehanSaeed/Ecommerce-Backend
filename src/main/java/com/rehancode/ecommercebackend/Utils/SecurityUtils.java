package com.rehancode.ecommercebackend.Utils;

import com.rehancode.ecommercebackend.Exception.BadCredentials;
import com.rehancode.ecommercebackend.Security.UserPrinciple;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static Long getAuthenticatedUserId(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth ==null || !auth.isAuthenticated() || auth.getPrincipal() == null || "anonymousUser".equals(auth.getPrincipal()) ){
            throw new BadCredentials("User is not authenticated");
        }
        if(!(auth.getPrincipal() instanceof UserPrinciple userPrinciple)){
            throw new BadCredentials("User is not authenticated");
        }
        return userPrinciple.getPrinciple().getId();
    }
}
