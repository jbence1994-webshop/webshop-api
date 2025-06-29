package com.github.jbence1994.webshop.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static com.github.jbence1994.webshop.user.AddressTestObject.addressAfterMappingFromDto;
import static com.github.jbence1994.webshop.user.ChangePasswordRequestTestObject.changePasswordRequest;
import static com.github.jbence1994.webshop.user.ProfileTestObject.profileAfterMappingFromDto;
import static com.github.jbence1994.webshop.user.RegistrationRequestTestObject.registrationRequest;
import static com.github.jbence1994.webshop.user.RegistrationResponseTestObject.registrationResponse;
import static com.github.jbence1994.webshop.user.UserDtoTestObject.userDto;
import static com.github.jbence1994.webshop.user.UserTestObject.user;
import static com.github.jbence1994.webshop.user.UserTestObject.userAfterMappingFromDto;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTests {
    @Mock
    private UserQueryService userQueryService;

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserController userController;

    @Test
    public void getUserTest_HappyPath() {
        when(userQueryService.getUser(anyLong())).thenReturn(user());
        when(userMapper.toDto(any(User.class))).thenReturn(userDto());

        var result = userController.getUser(1L);

        assertThat(result.id(), equalTo(userDto().id()));
        assertThat(result.email(), equalTo(userDto().email()));
        assertThat(result.profile().firstName(), equalTo(userDto().profile().firstName()));
        assertThat(result.profile().middleName(), equalTo(userDto().profile().middleName()));
        assertThat(result.profile().lastName(), equalTo(userDto().profile().lastName()));
        assertThat(result.profile().dateOfBirth(), equalTo(userDto().profile().dateOfBirth()));
        assertThat(result.profile().phoneNumber(), equalTo(userDto().profile().phoneNumber()));
        assertThat(result.profile().address().addressLine(), equalTo(userDto().profile().address().addressLine()));
        assertThat(result.profile().address().municipality(), equalTo(userDto().profile().address().municipality()));
        assertThat(result.profile().address().province(), equalTo(userDto().profile().address().province()));
        assertThat(result.profile().address().postalCode(), equalTo(userDto().profile().address().postalCode()));
        assertThat(result.profile().address().country(), equalTo(userDto().profile().address().country()));
    }

    @Test
    public void getUserTest_UnhappyPath_UserNotFoundException() {
        when(userQueryService.getUser(anyLong())).thenThrow(new UserNotFoundException(1L));

        var result = assertThrows(
                UserNotFoundException.class,
                () -> userController.getUser(1L)
        );

        assertThat(result.getMessage(), equalTo("No user was found with the given ID: #1."));
    }

    @Test
    public void registerUserTest() {
        when(userMapper.toEntity(any(RegistrationRequest.AddressDto.class))).thenReturn(addressAfterMappingFromDto());
        when(userMapper.toEntity(any(RegistrationRequest.ProfileDto.class))).thenReturn(profileAfterMappingFromDto());
        when(userMapper.toEntity(any(RegistrationRequest.UserDto.class))).thenReturn(userAfterMappingFromDto());
        when(userService.registerUser(any())).thenReturn(user());

        var result = userController.registerUser(registrationRequest());

        assertThat(result.getStatusCode(), equalTo(HttpStatus.CREATED));
        assertThat(result.getBody(), not(nullValue()));
        assertThat(result.getBody().id(), equalTo(registrationResponse().id()));
        assertThat(result.getBody().email(), equalTo(registrationResponse().email()));
        assertThat(result.getBody().message(), equalTo(registrationResponse().message()));
    }

    @Test
    public void changePasswordTest() {
        assertDoesNotThrow(() -> userController.changePassword(1L, changePasswordRequest()));
    }

    @Test
    public void deleteUserTest() {
        doNothing().when(userService).deleteUser(any());

        var result = userController.deleteUser(1L);

        assertThat(result.getStatusCode(), equalTo(HttpStatus.NO_CONTENT));
        assertThat(result.getBody(), is(nullValue()));
    }
}
