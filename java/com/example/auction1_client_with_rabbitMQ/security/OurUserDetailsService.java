package com.example.auction1_client_with_rabbitMQ.security;

import com.example.auction1_client_with_rabbitMQ.client_services.login.GeneralService;
import com.example.auction1_client_with_rabbitMQ.client_models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class OurUserDetailsService implements UserDetailsService {

    private final GeneralService generalService;

    public OurUserDetailsService(GeneralService generalService) {
        this.generalService = generalService;
    }


    @Override
    public UserDetails loadUserByUsername(String username)  throws UsernameNotFoundException {

        User user = generalService.findByUsername(username);

        if(user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole()));

        return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(), authorities);
    }
}
