package com.smarttask.taskallocator.domain.model.member;

import java.util.Objects;
import java.util.UUID;

/**
 * Identidad del agregado {@link Member}.
 */
public record MemberId(UUID value) {

    public MemberId {
        Objects.requireNonNull(value, "El id del miembro no puede ser null");
    }

    public static MemberId generate() {
        return new MemberId(UUID.randomUUID());
    }

    public static MemberId of(UUID value) {
        return new MemberId(value);
    }

    public static MemberId of(String value) {
        return new MemberId(UUID.fromString(value));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
