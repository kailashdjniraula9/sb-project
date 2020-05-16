package io.badri.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.badri.app.entity.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

	Optional<VerificationToken> findByToken(String token);

}
