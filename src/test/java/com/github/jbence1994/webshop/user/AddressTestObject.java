package com.github.jbence1994.webshop.user;

import java.time.LocalDateTime;

import static com.github.jbence1994.webshop.user.AddressTestConstants.ADDRESS_LINE;
import static com.github.jbence1994.webshop.user.AddressTestConstants.COUNTRY;
import static com.github.jbence1994.webshop.user.AddressTestConstants.MUNICIPALITY;
import static com.github.jbence1994.webshop.user.AddressTestConstants.POSTAL_CODE;
import static com.github.jbence1994.webshop.user.AddressTestConstants.PROVINCE;

public final class AddressTestObject {
    public static Address address() {
        return buildAddress(1L, LocalDateTime.now(), LocalDateTime.now());
    }

    public static Address addressAfterMappingFromDto() {
        return buildAddress(null, null, null);
    }

    private static Address buildAddress(Long profileId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new Address(
                profileId,
                null,
                ADDRESS_LINE,
                MUNICIPALITY,
                PROVINCE,
                POSTAL_CODE,
                COUNTRY,
                createdAt,
                updatedAt
        );
    }
}
