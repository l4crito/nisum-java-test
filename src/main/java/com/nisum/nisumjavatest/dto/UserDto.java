package com.nisum.nisumjavatest.dto;

import com.nisum.nisumjavatest.entity.Phone;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;
import java.util.UUID;
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class UserDto {

    private UUID id;

    @NotNull
    private String name;

    @NotNull
    private String email;

    @NotNull
    private String password;

    private Set<Phone> phones;

    private boolean isActive;

}
