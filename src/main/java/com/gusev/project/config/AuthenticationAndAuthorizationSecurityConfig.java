package com.gusev.project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class AuthenticationAndAuthorizationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    @Autowired
    public AuthenticationAndAuthorizationSecurityConfig(UserDetailsService capstoneProjectUserDetailsService) {
        this.userDetailsService = capstoneProjectUserDetailsService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/home", "/login", "/h2/**").permitAll()

                .antMatchers("/users").hasAnyRole(ADMIN, TRAINER)
                .antMatchers("/users/account/*").hasAnyRole(ADMIN, USER)
                .antMatchers("/users/*/info").hasAnyRole(ADMIN, TRAINER, USER)
                .antMatchers("/users/**/cancel-registration").hasAnyRole(ADMIN, USER)
                .antMatchers(HttpMethod.GET, "/users/**").hasAnyRole(ADMIN)
                .antMatchers(HttpMethod.POST, "/users/**").hasAnyRole(ADMIN)
                .antMatchers("/users/**").hasAnyRole(ADMIN)

                .antMatchers("/trainers").hasAnyRole(ADMIN, TRAINER, USER)
                .antMatchers("/trainers/**/all-available").hasRole(ADMIN)
                .antMatchers(HttpMethod.GET, "/trainers/create", "/trainers/**/delete", "/trainers/**/registration").hasRole(ADMIN)
                .antMatchers(HttpMethod.POST, "/trainers/create").hasRole(ADMIN)
                .antMatchers("/trainers/**").hasAnyRole(ADMIN, TRAINER)

                .antMatchers("/group-classes").hasAnyRole(ADMIN, TRAINER, USER)
                .antMatchers("/group-classes/**/all-available").hasRole(ADMIN)
                .antMatchers("/group-classes/**/registration").hasRole(ADMIN)
                .antMatchers("/group-classes/*/user-registration", "/group-classes/**/cancel-registration").hasAnyRole(ADMIN, USER)
                .antMatchers(HttpMethod.GET, "/group-classes/create", "/group-classes/*/delete").hasRole(ADMIN)
                .antMatchers(HttpMethod.POST, "/group-classes/create").hasRole(ADMIN)
                .antMatchers("/group-classes/*/users").hasAnyRole(ADMIN, TRAINER)
                .antMatchers("/group-classes/**").hasAnyRole(ADMIN, TRAINER)

                .anyRequest().authenticated()

                .and()
                .formLogin().loginPage("/login").permitAll().loginProcessingUrl("/doLogin")

                .and()
                .logout().permitAll().logoutUrl("/logout")

                .and()
                .csrf().disable()
                .headers().frameOptions().disable();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
