package com.barogo.java.delivery.poc.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;

@Entity
@Builder
@Getter
@Table(name = "users")
public class User extends BaseEntity {

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String name;

	public User() {

	}
	public User(String email, String password, String name) {
		this.email = email;
		this.password = password;
		this.name = name;
	}

}