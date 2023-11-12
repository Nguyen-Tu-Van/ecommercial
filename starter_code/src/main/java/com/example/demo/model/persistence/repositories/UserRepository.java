package com.example.demo.model.persistence.repositories;

import com.example.demo.model.persistence.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.persistence.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);
}
