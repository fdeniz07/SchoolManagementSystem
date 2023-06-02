package com.schoolmanagement.controller;

import com.schoolmanagement.entity.enums.RoleType;
import com.schoolmanagement.payload.request.LoginRequest;
import com.schoolmanagement.payload.response.AuthResponse;
import com.schoolmanagement.security.jwt.JwtUtils;
import com.schoolmanagement.security.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {

    public final JwtUtils jwtUtils;
    public final AuthenticationManager authenticationManager;


    @PostMapping("/login") //http://localhost:8080/auth/login
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody @Valid LoginRequest loginRequest) {

        //!!! Gelen request'in icinden username ve password bilgisi aliniyor
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        //!!! authenticationManager üzerinden kullaniciyi valide ediyoruz
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        //!!! Valide edilen kullanici context'e atiliyor
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //!!! JWT Token olusturuluyor
        String token = "Bearer " + jwtUtils.generateJwtToken(authentication); //Client tarafina önünde "Bearer " eklenerek gidecek


        //Buradan asagasi Opsiyonel ve bu projeye özgü!

        //!!! GranteAuthority türündeki role yapisi String türüne ceviriliyor
        //authentication.getPrincipal() --> Anlik olarak login olan kullanicinin security katmanindaki userDetails bilgisini döndürür
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Set<String> roles = userDetails
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority) //rolleri bizim anlayacagimiz türe(Admin,Manager...) ceviriyor
                .collect(Collectors.toSet());

        Optional<String> role = roles.stream().findFirst();

        //Find gibi metotlar hata vermeye acik oldugu icin Option türünde alarak NullPointerException'unun önüne gecmis oluruz.


        //!!! AuthResponse
        //Bu uzun yol ama methodChain yapisinda yukaridaki gibi yapilabilir.
        AuthResponse.AuthResponseBuilder authResponse = AuthResponse.builder();
        authResponse.username(userDetails.getUsername());
        authResponse.token(token);
        authResponse.name(userDetails.getName());


        //!!! Role mevcutsa ve Teacher ise Advisor durumu setleniyor
        if (role.isPresent()) { //isPresent() metodu bur role mevcutsa (null degilse)
            authResponse.role(role.get());
            if (role.get().equalsIgnoreCase(RoleType.TEACHER.name())) { //bu role Teacher'in kendisi ise
                authResponse.isAdvisor(userDetails.getIsAdvisor().toString());
            }
        }

        //!!! AuthResponse nesnesi ResponseEntity ile döndürüyoruz
        return ResponseEntity.ok(authResponse.build());

    }

}
















