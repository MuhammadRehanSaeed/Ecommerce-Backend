package com.rehancode.ecommercebackend.RateLimiting;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimitService {
    private final ConcurrentHashMap<String, RateLimitingInfo> rateLimitInfo=new ConcurrentHashMap<>();

    public boolean allowRequests(
            String key,
            RateLimitRule rule
    ){

        long now = System.currentTimeMillis();


        RateLimitingInfo info =
                rateLimitInfo.get(key);



        if(info == null){

            rateLimitInfo.put(
                    key,
                    new RateLimitingInfo(1,now)
            );

            return true;
        }



        if(now - info.getStartWindow() >= rule.getWindow()){

            info.setRequestCount(1);
            info.setStartWindow(now);

            return true;

        }



        if(info.getRequestCount() < rule.getLimit()){

            info.setRequestCount(
                    info.getRequestCount()+1
            );

            return true;

        }


        return false;

    }

}
