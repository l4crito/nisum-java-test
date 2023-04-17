package com.nisum.nisumjavatest.repository;

import com.nisum.nisumjavatest.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.phones")
    List<User> findAllWithPhones();
}