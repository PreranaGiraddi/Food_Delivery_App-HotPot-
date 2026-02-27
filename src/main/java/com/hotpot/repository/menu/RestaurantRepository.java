package com.hotpot.repository.menu;

import com.hotpot.entity.menu.Restaurant;

import com.hotpot.entity.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

   
    List<Restaurant> findByIsActive(boolean isActive);

    
    Optional<Restaurant> findByOwner(User owner);

   
    Optional<Restaurant> findByName(String name);

    
    boolean existsByName(String name);

    
    List<Restaurant> findByLocation(String location);

 
    @Query("SELECT r FROM Restaurant r WHERE r.name LIKE %:keyword%")
    List<Restaurant> searchByName(String keyword);

    
    @Query("SELECT r FROM Restaurant r WHERE r.owner.id = :ownerId")
    Optional<Restaurant> findByOwnerId(Long ownerId);
}