package com.teamproject.back.repository;


import com.teamproject.back.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<Users, Long> {


    Users findByEmail(String email);
}
