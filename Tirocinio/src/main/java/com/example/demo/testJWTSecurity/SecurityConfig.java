package com.example.demo.testJWTSecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return userDetailsService;
    }

    @Bean
    public SecurityConfigurerAdapter securityConfigurerAdapter() {
        return new SecurityConfigurerAdapter(jwtRequestFilter);
    }

    @Configuration
    public static class SecurityConfigurerAdapter extends SecurityConfigurerAdapterBase {
        private final JwtRequestFilter jwtRequestFilter;

        public SecurityConfigurerAdapter(JwtRequestFilter jwtRequestFilter) {
            this.jwtRequestFilter = jwtRequestFilter;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        }
    }

    @Configuration
    public static class SecurityConfigurerAdapterBase {
    	//@SuppressWarnings({ "deprecation", "removal" })
        protected void configure(HttpSecurity http) throws Exception {
    		http.authorizeRequests()
                .requestMatchers("/login").permitAll()
                .requestMatchers("/api/**").authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().csrf().disable();
        }
    }

}
