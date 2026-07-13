package pl.wojtczak.score_predictor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wojtczak.score_predictor.entity.Team;
import pl.wojtczak.score_predictor.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> findByUsername(String username);
}
