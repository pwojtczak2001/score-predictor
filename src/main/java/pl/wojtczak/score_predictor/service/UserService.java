package pl.wojtczak.score_predictor.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.wojtczak.score_predictor.dto.auth.RegisterRequest;
import pl.wojtczak.score_predictor.dto.response.UserResponse;
import pl.wojtczak.score_predictor.dto.response.UserStatsResponse;
import pl.wojtczak.score_predictor.entity.League;
import pl.wojtczak.score_predictor.entity.User;
import pl.wojtczak.score_predictor.enums.RegistrationStatus;
import pl.wojtczak.score_predictor.exception.LeagueNotFoundException;
import pl.wojtczak.score_predictor.exception.UserNotFoundException;
import pl.wojtczak.score_predictor.exception.UserNotMemberOfLeagueException;
import pl.wojtczak.score_predictor.repository.*;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    private final PredictionService predictionService;

    private final AchievementService achievementService;

    private final UserAchievementRepository userAchievementRepository;

    private final PredictionRepository predictionRepository;

    private final LeagueMemberRepository leagueMemberRepository;

    private final LeagueRepository leagueRepository;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, PredictionService predictionService, AchievementService achievementService, UserAchievementRepository userAchievementRepository, PredictionRepository predictionRepository, LeagueMemberRepository leagueMemberRepository, LeagueRepository leagueRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.predictionService = predictionService;
        this.achievementService = achievementService;
        this.userAchievementRepository = userAchievementRepository;
        this.predictionRepository = predictionRepository;
        this.leagueMemberRepository = leagueMemberRepository;
        this.leagueRepository = leagueRepository;
    }

    public RegistrationStatus registerUser(RegisterRequest request) {
        if (!request.getEmail().contains("@")) {
            return RegistrationStatus.INVALID_EMAIL;
        }
        if (request.getPassword().length() < 8) {
            return RegistrationStatus.WEAK_PASSWORD;
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            return RegistrationStatus.EMAIL_TAKEN;
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            return RegistrationStatus.USERNAME_TAKEN;
        }

        User user = new User(
                request.getUsername(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword())
        );

        userRepository.save(user);
        return RegistrationStatus.SUCCESS;
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    public User getUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }


    public UserResponse getCurrentUserProfile(){
        User currentUser = getCurrentUser();
        return
                new UserResponse(
                        currentUser.getUserId(),
                        currentUser.getUsername(),
                        currentUser.getEmail(),
                        currentUser.getXp(),
                        currentUser.getCoins(),
                        currentUser.getLevel(),
                        achievementService.getAllAchievements(currentUser),
                        userAchievementRepository.countByUser(currentUser));
    }

    public UserStatsResponse getCurrentUserStats(Integer leagueId) {
        User currentUser = getCurrentUser();

        League league = leagueRepository.findById(leagueId)
                .orElseThrow(() -> new LeagueNotFoundException(leagueId));

        if (!leagueMemberRepository.existsByLeagueAndUser(league, currentUser)) {
            throw new UserNotMemberOfLeagueException(leagueId);
        }

        return predictionService.getUserStats(currentUser, league);
    }

}
