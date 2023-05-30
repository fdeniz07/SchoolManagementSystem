package com.schoolmanagement.security.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {

    @Value("${backendapi.app.jwtSecret}") //application.properties'den degeri al diyoruz. Bu sekilde sabit degerlerimizi kodlar arasinda degil,
                                         // ayar dosyasinda degistirebiliyoruz
    private String jwtSecret;

    @Value("${backendapi.app.jwtExpressionMS}")
    private Long jwtExpirationMs;



    //Not: Generate JWT **********************************************************


    //Not: Validate JWT **********************************************************


    //Not: getUsernameForJwt *****************************************************

}
