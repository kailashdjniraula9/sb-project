package io.badri.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.badri.app.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	public User findByUsername(String username);

	public User findByEmail(String email);

	public User findByPassword(String password);

	public User findById(int id);

	public User deleteByUsername(String username);

}
