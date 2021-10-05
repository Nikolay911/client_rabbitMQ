package com.example.auction1_client_with_rabbitMQ.security;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final OurUserDetailsService userDetailsService;

    public WebSecurityConfig(OurUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests().antMatchers("/client/customer/**").authenticated()
                .antMatchers("/client/location/customer/**").authenticated()
                .antMatchers("/client/product/**").authenticated()
                .antMatchers("/client/auction/**").authenticated()
                .antMatchers("/client/bid/**").authenticated()
                .antMatchers("/client/users/**").hasRole("admin")
                .antMatchers("/client/all/customer/**").hasRole("admin")
                .antMatchers("/user/**").permitAll()
                .antMatchers("/client/auction/**").authenticated()
                .antMatchers("/client/auctions").authenticated()
                .and().httpBasic();
    }

    @Override
    public void configure(AuthenticationManagerBuilder builder) throws Exception { builder.userDetailsService(userDetailsService);}

}
