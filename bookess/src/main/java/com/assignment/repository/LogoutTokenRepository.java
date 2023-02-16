package com.assignment.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.assignment.entity.LogoutToken;

@Repository
public interface LogoutTokenRepository extends JpaRepository<LogoutToken, Long> {
	
	Optional<LogoutToken> findByToken(String token);

	boolean existsByToken(String token);

}
