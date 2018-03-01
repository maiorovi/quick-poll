package org.home.quickpoll.service.security.user;

import org.home.quickpoll.domain.security.User;
import org.home.quickpoll.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class QuickPollUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    public QuickPollUserDetailsService(@Autowired UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("User with the username %s doesn`t exist", username));
        }


        List<GrantedAuthority> authorities = new ArrayList<>();

        if(user.isAdmin()) {
            authorities = AuthorityUtils.createAuthorityList("ROLE_ADMIN");
        }

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);

        return userDetails;
    }
}
