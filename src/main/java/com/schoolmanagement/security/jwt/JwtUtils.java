package com.schoolmanagement.security.jwt;

import com.schoolmanagement.security.service.UserDetailsImpl;
import io.jsonwebtoken.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${backendapi.app.jwtSecret}")
    //application.properties'den degeri al diyoruz. Bu sekilde sabit degerlerimizi kodlar arasinda degil,
    // ayar dosyasinda degistirebiliyoruz
    private String jwtSecret;

    @Value("${backendapi.app.jwtExpressionMS}")
    private Long jwtExpirationMs;


    //Not: Generate JWT **********************************************************
    //JWT Loginden sonra cagirilir
    public String generateJwtToken(Authentication authentication) {

        //Anlik olarak login islemi yapan kullanici bilgisi:
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        //Username bilgisi ile Jwt Token üretiliyor
        return generateTokenFromUsername(userPrincipal.getUsername());

    }

    private String generateTokenFromUsername(String username) {

        //Burasi boilerplate bir kod. Kopyala yapistir. Sifirdan yazmaya gerek yok!
        return Jwts.builder()
                .setSubject(username) //Token icerisinde genel olarak username ile bilgiler tutariz. Sistem her ne kadar güvenli olsa da password bilgisi dahil edilmez
                .setIssuedAt(new Date()) //üretilecek token tarihi
                .setExpiration(new Date(new Date().getTime() + jwtExpirationMs)) //Burada biz anlik zamani al, daha önce belirledigimiz süreyi (1 gün) üzerine ekle ve gecerlilik süresi olarak kabul et diyoruz
                .signWith(SignatureAlgorithm.HS512, jwtSecret) //Hangi sifreleme algoritmasini kullanacagimizi seciyoruz
                .compact();
    }


    //Not: Validate JWT **********************************************************
    public boolean validateJwtToken(String authToken) {

        /* Kisayol : Asagidaki kodu yazip iki satiri sectikten sonra, yukaridaki menülerden Code-->Surround with-->try/catch secilirse otomatik tüm karsilasilacak exception'lar try/catch bloguna alinir.
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        */
        try {
            Jwts.parser().
                    setSigningKey(jwtSecret). //sifre ile tokene tersliyor
                    parseClaimsJws(authToken);
            return true;
        } catch (ExpiredJwtException e) {
            logger.error("Jwt token is expired : {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("Jwt token is unsupported : {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid Jwt token : {}", e.getMessage());
        } catch (SignatureException e) {
            logger.error("Invalid Jwt signature : {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("Jwt claims string is empty : {}", e.getMessage());
        }
        return false;
    }

    //Not: getUsernameForJwt *****************************************************
    public String getUserNameFromJwtToken(String token) {

        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }
}












