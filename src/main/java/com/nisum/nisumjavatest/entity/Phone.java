package com.nisum.nisumjavatest.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "phone")
public class Phone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number")
    private String number;

    @Column(name = "citycode")
    private String cityCode;

    @Column(name = "countrycode")
    private String countryCode;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Constructors, getters and setters

}
