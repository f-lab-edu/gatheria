package com.gatheria.domain;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class BaseMember {
    private static final Logger logger = LoggerFactory.getLogger(BaseMember.class);

    private final String email;
    private final String password;
    @Setter
    private String name;
    @Setter
    private String phone;
    private boolean isActive;

    public BaseMember(String email, String password, String name, String phone, boolean isActive) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.isActive = false;
    }

    public void activate() {
        if (this.isActive) {
            logger.info("The user is already activated.");
            return;
        }
        this.isActive = true;
        logger.info("User activated successfully.");
    }
}
