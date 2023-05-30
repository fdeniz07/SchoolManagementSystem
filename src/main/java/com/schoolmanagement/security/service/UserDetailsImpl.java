package com.schoolmanagement.security.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private Long id;

    private String username;

    private String name;

    private Boolean isAdvisor;

    @JsonIgnore //passwordu ön tarafa plaintext olarak gönderme
    private String password;

    private Collection<? extends  GrantedAuthority> authorities;

    //GrantedAuthority'i constructor olarak almiyor, bizdeki role'ü GrantedAuthority'e dönüstürüyoruz
    public UserDetailsImpl(Long id, String username, String name, Boolean isAdvisor, String password, String role) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.isAdvisor = isAdvisor;
        this.password = password;
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(role));
        this.authorities = grantedAuthorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    //iki tane userdetails türünde nesne gelecekse ve birbiriyle kiyaslanacaksa ve bu kiyaslanma kriterini kendimizi göre özellestireceksek;
    public boolean equals(Object o){

        if (this==o)//kendisi ile kiyasliyorsak
            return true;

        if (o ==null || getClass() != o.getClass())
            return false;

            UserDetailsImpl user = (UserDetailsImpl) o;
            return Objects.equals(id,user.id); // id ile kiyaslama

    }
}
