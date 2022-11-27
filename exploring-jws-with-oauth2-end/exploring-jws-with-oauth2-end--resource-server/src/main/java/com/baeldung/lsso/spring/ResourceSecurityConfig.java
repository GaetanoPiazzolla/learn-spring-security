package com.baeldung.lsso.spring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ResourceSecurityConfig {

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwkSetUri;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {// @formatter:off
        http.authorizeHttpRequests(authorize -> authorize
	              .antMatchers(HttpMethod.GET, "/api/projects/**")
	                .hasAuthority("SCOPE_read")
	              .antMatchers(HttpMethod.POST, "/api/projects")
	                .hasAuthority("SCOPE_write")
	              .anyRequest()
	                .authenticated())
              .oauth2ResourceServer()
                .jwt();
        return http.build();
    }//@formatter:on

    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri(this.jwkSetUri)
            .jwsAlgorithm(SignatureAlgorithm.RS512)
            .jwsAlgorithm(SignatureAlgorithm.RS256)
            .build();
    }
}