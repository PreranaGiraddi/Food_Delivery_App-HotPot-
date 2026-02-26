package com.hotpot.repository.auth;

import com.hotpot.entity.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

   
    Optional<User> findByEmail(String email);

    
    boolean existsByEmail(String email);

    
    Optional<User> findByEmailAndIsActive(String email, boolean isActive);

    
    List<User> findByRole(User.Role role);

    
    Optional<User> findByContactNumber(String contactNumber);

   
    @Query("SELECT u FROM User u WHERE u.name LIKE %:name%")
    List<User> searchByName(String name);
}

