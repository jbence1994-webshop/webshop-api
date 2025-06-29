package com.github.jbence1994.webshop.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import static com.github.jbence1994.webshop.user.UserTestConstants.HASHED_PASSWORD;
import static com.github.jbence1994.webshop.user.UserTestConstants.INVALID_OLD_PASSWORD;
import static com.github.jbence1994.webshop.user.UserTestConstants.NEW_HASHED_PASSWORD;
import static com.github.jbence1994.webshop.user.UserTestConstants.NEW_PASSWORD;
import static com.github.jbence1994.webshop.user.UserTestConstants.OLD_PASSWORD;
import static com.github.jbence1994.webshop.user.UserTestObject.user;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTests {

    @Mock
    private UserQueryService userQueryService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordManager passwordManager;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void registerUserTest_HappyPath() {
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(passwordManager.encode(any())).thenReturn(HASHED_PASSWORD);
        when(userRepository.save(any())).thenReturn(user());

        assertDoesNotThrow(() -> userService.registerUser(user()));

        verify(userRepository, times(1)).existsByEmail(any());
        verify(passwordManager, times(1)).encode(any());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    public void registerUserTest_UnhappyPath_EmailAlreadyExistsException() {
        when(userRepository.existsByEmail(any())).thenReturn(true);

        var result = assertThrows(
                EmailAlreadyExistsException.class,
                () -> userService.registerUser(user())
        );

        assertThat(result.getMessage(), equalTo("Email address 'juhasz.bence.zsolt@gmail.com' is already in use. Please use a different."));

        verify(userRepository, times(1)).existsByEmail(any());
        verify(userRepository, never()).existsByProfilePhoneNumber(any());
        verify(passwordManager, never()).encode(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    public void registerUserTest_UnhappyPath_PhoneNumberAlreadyExistsException() {
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(userRepository.existsByProfilePhoneNumber(any())).thenReturn(true);

        var result = assertThrows(
                PhoneNumberAlreadyExistsException.class,
                () -> userService.registerUser(user())
        );

        assertThat(result.getMessage(), equalTo("Phone number '+36501323566' is already registered. Please use a different."));

        verify(userRepository, times(1)).existsByEmail(any());
        verify(userRepository, times(1)).existsByProfilePhoneNumber(any());
        verify(passwordManager, never()).encode(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    public void changePasswordTest_HappyPath() {
        when(userQueryService.getUser(anyLong())).thenReturn(user());
        when(passwordManager.verify(any(), any())).thenReturn(true);
        when(passwordManager.encode(any())).thenReturn(NEW_HASHED_PASSWORD);
        when(userRepository.save(any())).thenReturn(user());

        assertDoesNotThrow(() -> userService.changePassword(1L, OLD_PASSWORD, NEW_PASSWORD));

        verify(userQueryService, times(1)).getUser(anyLong());
        verify(passwordManager, times(1)).verify(any(), any());
        verify(passwordManager, times(1)).encode(any());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    public void changePasswordTest_UnhappyPath_AccessDeniedException() {
        when(userQueryService.getUser(anyLong())).thenReturn(user());
        when(passwordManager.verify(any(), any())).thenReturn(false);

        var result = assertThrows(
                AccessDeniedException.class,
                () -> userService.changePassword(1L, INVALID_OLD_PASSWORD, NEW_PASSWORD)
        );

        assertThat(result.getMessage(), equalTo("Invalid old password."));

        verify(userQueryService, times(1)).getUser(anyLong());
        verify(passwordManager, times(1)).verify(any(), any());
        verify(passwordManager, never()).encode(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    public void updateUserTest() {
        when(userRepository.save(any())).thenReturn(user());

        assertDoesNotThrow(() -> userService.updateUser(user()));
    }

    @Test
    public void deleteUserTest() {
        doNothing().when(userRepository).deleteById(any());

        assertDoesNotThrow(() -> userService.deleteUser(any()));
    }
}
