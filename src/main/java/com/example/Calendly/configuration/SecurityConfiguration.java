package com.example.Calendly.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import javax.sql.DataSource;

@Configuration
public class SecurityConfiguration {
    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource){
        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
        userDetailsManager.setUsersByUsernameQuery("select email, password, true as enabled from users where email=?");
        userDetailsManager.setAuthoritiesByUsernameQuery("select email,name from users where email=?");
        return userDetailsManager;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests(configure -> configure
                        .requestMatchers("/signIn","/signUp","/saveRegisteredUser", "/").permitAll()
                        .requestMatchers("/scheduled-meet/{eventId}", "/schedule-meet").permitAll()
                        .requestMatchers("/dashboard").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form ->
                        form
                                .loginPage("/signIn")
                                .loginProcessingUrl("/authenticateTheUser")
                                .defaultSuccessUrl("/dashboard")
                                .permitAll()
                )
                .logout(logout -> logout.permitAll());

        return http.build();
    }
}
