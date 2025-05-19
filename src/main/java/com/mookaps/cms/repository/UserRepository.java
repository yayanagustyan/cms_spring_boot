package com.mookaps.cms.repository;

import com.mookaps.cms.models.User;

import org.springframework.stereotype.Repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query(value = "SELECT * FROM tb_users u WHERE email = ?1", nativeQuery = true)
    User findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.email LIKE %:keyword% OR u.name LIKE %:keyword% ")
    List<User> findByKeyword(@Param("keyword") String keyword);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.token = :token WHERE u.email = :email")
    int updateToken(@Param("token") String token, @Param("email") String email);
}
