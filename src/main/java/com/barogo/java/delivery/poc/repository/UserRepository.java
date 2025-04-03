package com.barogo.java.delivery.poc.repository;

import org.springframework.data.repository.Repository;

import com.barogo.java.delivery.poc.entity.User;

public interface UserRepository extends Repository<User, Long> {

	void save(User user);

	User findByEmail(String email);
}
