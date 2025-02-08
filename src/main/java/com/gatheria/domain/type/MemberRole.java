package com.gatheria.domain.type;

import lombok.Getter;

@Getter
public enum MemberRole {
    STUDENT("student"),
    INSTRUCTOR("instructor");

    private final String value;

    MemberRole( String value) {
        this.value = value;
    }

    public static MemberRole fromString(String role) {
        for(MemberRole memberRole : MemberRole.values()) {
            if (memberRole.value.equalsIgnoreCase(role)) {
                return  memberRole;
            }
        }
        throw new IllegalArgumentException("unknown role");
    }


}
