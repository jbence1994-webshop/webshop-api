package com.github.jbence1994.webshop.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@ConfirmUserPassword
public class RegistrationRequest {

    @Valid
    private UserDto user;

    public ProfileDto getProfile() {
        return user.profile;
    }

    public AddressDto getAddress() {
        return user.profile.address;
    }

    @AllArgsConstructor
    @Getter
    public static class UserDto {
        @NotNull(message = "Email must be provided.")
        @NotBlank(message = "Email must be not empty.")
        @Email(message = "Email must be valid")
        private String email;

        @NotNull(message = "Password must be provided.")
        @NotBlank(message = "Password must be not empty.")
        @Size(min = 8, max = 12, message = "Password must be between 8 to 12 characters long.")
        private String password;

        @NotNull(message = "Confirm password must be provided.")
        @NotBlank(message = "Confirm password must be not empty.")
        private String confirmPassword;

        @Valid
        private ProfileDto profile;
    }

    @AllArgsConstructor
    @Getter
    public static class ProfileDto {
        @NotNull(message = "First name must be provided.")
        @NotBlank(message = "First name must be not empty.")
        private String firstName;

        private String middleName;

        @NotNull(message = "Last name must be provided.")
        @NotBlank(message = "Last name must be not empty.")
        private String lastName;

        @NotNull(message = "Date of Birth must be provided.")
        private LocalDate dateOfBirth;

        private String phoneNumber;

        @Valid
        private AddressDto address;
    }

    @AllArgsConstructor
    @Getter
    public static class AddressDto {
        @NotNull(message = "Address line must be provided.")
        @NotBlank(message = "Address line must be not empty.")
        private String addressLine;

        @NotNull(message = "Municipality must be provided.")
        @NotBlank(message = "Municipality must be not empty.")
        private String municipality;

        @NotNull(message = "Province must be provided.")
        @NotBlank(message = "Province must be not empty.")
        private String province;

        @NotNull(message = "Postal code must be provided.")
        @NotBlank(message = "Postal code must be not empty.")
        private String postalCode;

        @NotNull(message = "Country must be provided.")
        @NotBlank(message = "Country must be not empty.")
        private String country;
    }
}
