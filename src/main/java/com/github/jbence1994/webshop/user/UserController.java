package com.github.jbence1994.webshop.user;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private final UserQueryService userQueryService;
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable Long id) {
        var user = userQueryService.getUser(id);
        return userMapper.toDto(user);
    }

    @PostMapping
    public ResponseEntity<RegistrationResponse> registerUser(@Valid @RequestBody RegistrationRequest request) {
        var address = userMapper.toEntity(request.getAddress());
        var profile = userMapper.toEntity(request.getProfile());
        var user = userMapper.toEntity(request.getUser());

        address.setProfile(profile);
        profile.setAddress(address);

        profile.setUser(user);
        user.setProfile(profile);

        var registeredUser = userService.registerUser(user);

        var response = new RegistrationResponse(
                registeredUser.getId(),
                registeredUser.getEmail(),
                "Successful registration."
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{userId}")
    public void changePassword(
            @PathVariable Long userId,
            @Valid @RequestBody ChangePasswordRequest request
    ) {
        userService.changePassword(
                userId,
                request.getOldPassword(),
                request.getNewPassword()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }
}
