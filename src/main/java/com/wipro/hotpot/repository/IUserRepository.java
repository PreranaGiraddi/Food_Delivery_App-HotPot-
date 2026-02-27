package com.wipro.hotpot.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.wipro.hotpot.entity.User;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

	boolean existsByEmail(String email);

	Optional<User> findByEmailAndIsActive(String email, boolean isActive);

	List<User> findByRole(User.Role role);

	Optional<User> findByContactNumber(String contactNumber);

	@Query("SELECT u FROM User u WHERE u.name LIKE %:name%")
	List<User> searchByName(String name);
}
