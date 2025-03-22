package org.andrey.taskmanager.security;

import org.andrey.taskmanager.domain.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Класс для описания пользователя с точки зрения SpringSecurity
 */
public class SecurityUser implements UserDetails {

    private final User user;
    private final List<GrantedAuthority> authorityList;

    public SecurityUser(User user,
                        List<GrantedAuthority> authorityList){
        this.user = user;
        this.authorityList = authorityList;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorityList;
    }

    @Override
    public String getPassword() {
        return ;
    }

    @Override
    public String getUsername() {
        return "";
    }
}
