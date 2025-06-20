package com.github.jbence1994.webshop.coupon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CouponDto {
    private String code;
    private String type;
    private BigDecimal value;
    private String description;
    private LocalDateTime expirationDate;
}
