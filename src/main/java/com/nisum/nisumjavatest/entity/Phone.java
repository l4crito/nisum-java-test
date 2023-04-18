package com.nisum.nisumjavatest.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "phone")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Phone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Schema(example = "+593958717611")
    private String number;
    @Schema(example = "032")
    private String citycode;
    @Schema(example = "+593")
    private String countrycode;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    @JsonBackReference
    private User user;


}
