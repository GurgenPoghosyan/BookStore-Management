package com.internship.bookstore.security;

import com.internship.bookstore.common.exceptions.UserNotFoundException;
import com.internship.bookstore.persistence.entity.UserEntity;
import com.internship.bookstore.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author Gurgen Poghosyan
 */
@Service
@RequiredArgsConstructor
public class JwtUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException(username);
        }
        return new User(user.getUsername(), user.getPassword(), getAuthorities(user));
    }

    private List<SimpleGrantedAuthority> getAuthorities(UserEntity user) {
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(user.getRole().getName());
        return List.of(simpleGrantedAuthority);
    }
}
