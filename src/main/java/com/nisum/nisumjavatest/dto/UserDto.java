package com.nisum.nisumjavatest.dto;

import com.nisum.nisumjavatest.entity.Phone;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
@Schema()
public class UserDto {
    private UUID id;

    @NotNull
    @Size(min = 2, max = 50)
    @Schema(example = "Christian Caicedo")
    private String name;

    @NotNull
    @Size(min = 2, max = 50)
    @Schema(example = "l4crito@gmail.com")
    private String email;

    @NotNull
    @Schema(example = "password1")
    private String password;

    private Set<Phone> phones;

    private boolean isActive;

}
