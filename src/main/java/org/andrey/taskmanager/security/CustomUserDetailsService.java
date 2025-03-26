package org.andrey.taskmanager.security;

import org.andrey.taskmanager.domain.user.User;
import org.andrey.taskmanager.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация UserDetailsService для аутентификации пользователей
 */
public class CustomUserDetailsService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;

    public CustomUserDetailsService(UserRepository repository,
                                    PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = repository.findUserByUsername(username);
        if (u == null) {
            throw new UsernameNotFoundException("Пользователь " + username + " не найден!");
        }
        List<Integer> authorities = repository.findAuthoritiesByUser(u.getId());
        SecurityUser sc = new SecurityUser(u, authorities
                .stream()
                .map(a -> new SimpleGrantedAuthority("ROLE_" + Role.fromCode(a)))
                .collect(Collectors.toList()));

        return sc;
    }


    public void createDefaultAdmin(String adminUsername, String adminPassword) {
        try {
            loadUserByUsername(adminUsername);
        } catch (UsernameNotFoundException ex) {
            User user = new User();
            user.setEmail(adminUsername);
            user.setFirstName(adminUsername);
            user.setLastName(adminUsername);
            user.setPassword(passwordEncoder.encode(adminPassword));

            user = repository.save(user);
            repository.addAuthorityForUser(Role.ADMIN.getCode(), user.getId());
        }
    }

}
