package org.andrey.taskmanager.service;

import org.andrey.taskmanager.domain.user.User;
import org.andrey.taskmanager.exception.UserNotFoundException;
import org.andrey.taskmanager.repository.UserRepository;
import org.andrey.taskmanager.security.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setPassword("password");
        user.setEmail("testUser");
    }

    @Test
    public void updateUser_ShouldUpdateUserWithoutChangingPassword() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User updatedUser = userService.updateUser(user);

        assertEquals(user.getId(), updatedUser.getId());
        assertEquals(user.getEmail(), updatedUser.getEmail());
        assertEquals(user.getPassword(), updatedUser.getPassword());
        verify(userRepository).save(user);
        assertEquals("", updatedUser.getPassword());
    }

    @Test
    public void deleteUserById_ShouldCallRepositoryDelete() {
        userService.deleteUserById(user.getId());
        verify(userRepository).deleteById(user.getId());
    }

    @Test
    public void getUserById_ShouldReturnUser_WhenUserExists() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        User foundUser = userService.getUserById(user.getId());

        assertEquals(user.getId(), foundUser.getId());
    }

    @Test
    public void getUserById_ShouldThrowUserNotFoundException_WhenUserDoesNotExist() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(user.getId()));
    }

    @Test
    public void getAllUsers_ShouldReturnListOfUsersWithoutPasswords() {
        User user2 = new User();
        user2.setId(2L);
        user2.setPassword("password2");
        user2.setEmail("testUser2");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user, user2));

        List<User> users = userService.getAllUsers();

        assertEquals(2, users.size());
        assertEquals("", users.get(0).getPassword());
        assertEquals("", users.get(1).getPassword());
    }

    @Test
    public void addAuthorityForUser_ShouldCallRepositoryAddAuthority() {
        userService.addAuthorityForUser(Role.USER.getCode(), user.getId());
        verify(userRepository).addAuthorityForUser(Role.USER.getCode(), user.getId());
    }

    @Test
    public void deleteAuthorityForUser_ShouldCallRepositoryDeleteAuthority() {
        userService.deleteAuthorityForUser(Role.USER.getCode(), user.getId());
        verify(userRepository).deleteAuthorityForUser(Role.USER.getCode(), user.getId());
    }
}
