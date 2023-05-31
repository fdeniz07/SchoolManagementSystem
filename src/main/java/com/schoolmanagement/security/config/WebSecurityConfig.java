package com.schoolmanagement.security.config;

import com.schoolmanagement.security.jwt.AuthEntryPointJwt;
import com.schoolmanagement.security.jwt.AuthTokenFilter;
import com.schoolmanagement.security.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final AuthEntryPointJwt unauthorizedHandler;

    @Bean //method seviye - singleton
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        //olusan obje üzerinden Encoder ve UserDetailService'i tanistirmaliyiz
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    protected AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()    //cors --> tarayici tarafli güvenlik protokolleri- server baska bir sunucuda, Db baska bir sunucuda,
                                              // frontend baska tarafta olabilir  //csrf.disable() etmemmizin sebebi, update metotlarimiz düzgün calismayabilir
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) //REST mimaride Session olmaz.
                .and()
                .authorizeRequests()
                .antMatchers(AUTH_WHITE_LIST)
                .permitAll()
                .anyRequest().authenticated();
        http.headers().frameOptions().sameOrigin();
        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    private static final String AUTH_WHITE_LIST[] = {
            "/",
            "/index*",
            "/static/**",
            "/*.js",
            "/*.json",
            "/contactMessages/save",
            "/auth/login"
    };

    @Bean
    public WebMvcConfigurer corsConfigurer(){
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {

                registry.addMapping("/**") // tüm URL'leri kapsayacagini söyledik
                        .allowedOrigins("*") // Tüm kaynaklara izin veriliyor
                        .allowedHeaders("*") //Tüm header'lara izin verilecegini söyledik
                        .allowedMethods("*"); //Bütün HTTP metodlarina izin verildi
            }
        };
    }
}





























