package com.github.jbence1994.webshop.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum MembershipTier {
    BRONZE(0, 4_999),
    SILVER(5_000, 9_999),
    GOLD(10_000, 19_999),
    PLATINUM(20_000, Integer.MAX_VALUE);

    private final int minLoyaltyPoints;
    private final int maxLoyaltyPoints;

    public static MembershipTier fromPoints(int points) {
        return Stream.of(values())
                .filter(tier -> points >= tier.minLoyaltyPoints && points <= tier.maxLoyaltyPoints)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("Invalid value: %d.", points)));
    }
}
