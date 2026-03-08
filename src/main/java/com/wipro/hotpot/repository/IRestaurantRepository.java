package com.wipro.hotpot.repository;

import com.wipro.hotpot.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IRestaurantRepository extends JpaRepository<Restaurant, Long> {

    List<Restaurant> findByIsActiveTrue();

    @Query("SELECT r FROM Restaurant r WHERE r.isActive = true AND " +
           "(LOWER(r.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(r.location) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Restaurant> searchByKeyword(@Param("keyword") String keyword);

    
    @Query("SELECT r FROM Restaurant r WHERE r.owner.id = :userId")
    Optional<Restaurant> findByOwnerId(@Param("userId") Long userId);

    @Query("SELECT r FROM Restaurant r WHERE r.owner.id = :userId")
    List<Restaurant> findAllByOwnerId(@Param("userId") Long userId);
}