package com.internship.bookstore.security;

import com.internship.bookstore.common.exceptions.UserNotFoundException;
import com.internship.bookstore.persistence.entity.UserEntity;
import com.internship.bookstore.persistence.repository.UserRepository;
import com.internship.bookstore.security.session.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import java.util.List;

import static com.internship.bookstore.security.session.SessionUser.SESSION_USER_KEY;


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
        storeSessionUser(user);
        return new User(user.getUsername(), user.getPassword(), getAuthorities(user));
    }

    private void storeSessionUser(UserEntity user) {
        CurrentUser currentUser = new CurrentUser(user);
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        servletRequestAttributes.getRequest().getSession().setAttribute(SESSION_USER_KEY, currentUser.getSessionUser());
    }

    private List<SimpleGrantedAuthority> getAuthorities(UserEntity user) {
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(user.getRole().getName());
        return List.of(simpleGrantedAuthority);
    }
}
