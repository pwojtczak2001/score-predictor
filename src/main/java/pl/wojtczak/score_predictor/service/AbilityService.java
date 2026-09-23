package pl.wojtczak.score_predictor.service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import pl.wojtczak.score_predictor.dto.request.AbilityActivationRequest;
import pl.wojtczak.score_predictor.dto.request.SpyTargetRequest;
import pl.wojtczak.score_predictor.dto.response.SpyPredictionResponse;
import pl.wojtczak.score_predictor.dto.response.SpyTargetUserResponse;
import pl.wojtczak.score_predictor.entity.*;
import pl.wojtczak.score_predictor.enums.AbilityType;
import pl.wojtczak.score_predictor.exception.AbilityNotFoundException;
import pl.wojtczak.score_predictor.exception.LeagueNotFoundException;
import pl.wojtczak.score_predictor.exception.PredictionNotFoundException;
import pl.wojtczak.score_predictor.exception.UserNotMemberOfLeagueException;
import pl.wojtczak.score_predictor.repository.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AbilityService {

    private final UserService userService;

    private final MatchService matchService;
    private final AbilityRepository abilityRepository;
    private final LeagueRepository leagueRepository;
    private final LeagueMemberRepository leagueMemberRepository;
    private final MatchRepository matchRepository;
    private final AbilityUsageRepository abilityUsageRepository;
    private final PredictionRepository predictionRepository;

    private final UserRepository userRepository;

    public AbilityService(UserService userService,
                          MatchService matchService, AbilityRepository abilityRepository,
                          LeagueRepository leagueRepository,
                          LeagueMemberRepository leagueMemberRepository,
                          MatchRepository matchRepository, AbilityUsageRepository abilityUsageRepository, PredictionRepository predictionRepository, UserRepository userRepository) {
        this.userService = userService;
        this.matchService = matchService;
        this.abilityRepository = abilityRepository;
        this.leagueRepository = leagueRepository;
        this.leagueMemberRepository = leagueMemberRepository;
        this.matchRepository = matchRepository;
        this.abilityUsageRepository = abilityUsageRepository;
        this.predictionRepository = predictionRepository;
        this.userRepository = userRepository;
    }

    private void validateUserLevel(
            User user,
            Ability ability
    ) {

        if (user.getLevel() < ability.getUnlockLevel()) {
            throw new IllegalArgumentException(
                    "User level is too low for this ability"
            );
        }
    }

    private void validateUserCoins(
            User user,
            Ability ability
    ) {

        if (user.getCoins() < ability.getPriceCoins()) {
            throw new IllegalArgumentException(
                    "Not enough coins to activate ability"
            );
        }
    }

    private void validateUserLeagueMembership(
            League league,
            User user
    ) {

        if (!leagueMemberRepository.existsByLeagueAndUser(
                league,
                user
        )) {
            throw new UserNotMemberOfLeagueException(league.getLeagueId());
        }
    }

    private void validateTargetUserLeagueMembership(
            League league,
            User targetUser
    ) {

        if (!leagueMemberRepository.existsByLeagueAndUser(
                league,
                targetUser
        )) {
            throw new IllegalArgumentException(
                    "Target user is not a member of the league"
            );
        }
    }

    private void validateTargetUserIsNotCurrentUser(
            User currentUser,
            User targetUser
    ) {

        if (currentUser.getUserId()
                .equals(targetUser.getUserId())) {

            throw new IllegalArgumentException(
                    "User cannot target themselves"
            );
        }
    }

    private void validateAbilityTypeUsage(
            User user,
            League league,
            String stage,
            Ability ability
    ) {

        if (ability.getType() == AbilityType.PASSIVE) {
            return;
        }

        if (abilityUsageRepository
                .existsByUserAndLeagueAndStageAndAbility_Type(
                        user,
                        league,
                        stage,
                        ability.getType()
                )) {

            throw new IllegalArgumentException(
                    "User has already used an ability of this type in the selected stage"
            );
        }
    }

    private void activateHotStreak(User currentUser, Ability ability){

        if (abilityUsageRepository.existsByUserAndAbility_Code(currentUser, "HOT_STREAK")) {
            throw new IllegalArgumentException(
                    "Hot Streak is already activated");
        }

        AbilityUsage abilityUsage = new AbilityUsage(
                currentUser,
                ability,
                null,
                null,
                null,
                null
        );

        abilityUsageRepository.save(abilityUsage);
    }

    public List<SpyTargetUserResponse> getSpyTargetUsers(Integer leagueId, String stage) {

        User currentUser = userService.getCurrentUser();

        if (!matchRepository.existsByStageAndStatus(
                stage,
                "NOT STARTED"
        )) {
            throw new IllegalArgumentException(
                    "Selected stage is no longer active"
            );
        }

        League league = leagueRepository.findById(leagueId)
                .orElseThrow(() ->
                        new LeagueNotFoundException(leagueId)
                );

        validateUserLeagueMembership(league, currentUser);

        abilityUsageRepository.findByUserAndLeagueAndStageAndAbility_Code(
                        currentUser,
                        league,
                        stage,
                        "SPY"
                )
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "SPY is not active for this stage"
                        )
                );

        long totalMatches = matchRepository.countByStage(stage);

        List<Object[]> results =
                predictionRepository.findSpyTargetUsers(
                        league,
                        stage,
                        currentUser
                );

        List<SpyTargetUserResponse> response = new ArrayList<>();

        for (Object[] row : results) {

            SpyTargetUserResponse user = new SpyTargetUserResponse(
                    (Integer) row[0],
                    (String) row[1],
                    (Long) row[2],
                    totalMatches
            );

            response.add(user);
        }

        return response;
    }

    @Transactional
    public void selectSpyTarget(SpyTargetRequest request) {

        User currentUser = userService.getCurrentUser();

        if (!matchRepository.existsByStageAndStatus(
                request.getStage(),
                "NOT STARTED"
        )) {
            throw new IllegalArgumentException(
                    "Selected stage is no longer active"
            );
        }

        League league = leagueRepository.findById(request.getLeagueId())
                .orElseThrow(() ->
                        new LeagueNotFoundException(request.getLeagueId())
                );

        validateUserLeagueMembership(
                league,
                currentUser
        );

        AbilityUsage spyUsage =
                abilityUsageRepository
                        .findByUserAndLeagueAndStageAndAbility_Code(
                                currentUser,
                                league,
                                request.getStage(),
                                "SPY"
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "SPY is not active for this stage"
                                )
                        );

        if (spyUsage.getTargetUser() != null) {
            throw new IllegalArgumentException(
                    "SPY target has already been selected"
            );
        }

        User targetUser = userRepository.findById(
                request.getTargetUserId()
        ).orElseThrow(() ->
                new IllegalArgumentException(
                        "Target user not found"
                )
        );

        validateTargetUserLeagueMembership(
                league,
                targetUser
        );

        validateTargetUserIsNotCurrentUser(
                currentUser,
                targetUser
        );

        boolean hasPrediction =
                predictionRepository.existsByUserAndLeagueAndMatch_Stage(
                        targetUser,
                        league,
                        request.getStage()
                );

        if (!hasPrediction) {
            throw new IllegalArgumentException(
                    "Target user has no predictions in this stage"
            );
        }

        spyUsage.setTargetUser(targetUser);

        abilityUsageRepository.save(spyUsage);
    }

    public List<SpyPredictionResponse> getSpyPredictions(
            Integer leagueId,
            String stage
    ) {
        User currentUser = userService.getCurrentUser();

        if (!matchRepository.existsByStageAndStatus(
                stage,
                "NOT STARTED"
        )) {
            throw new IllegalArgumentException(
                    "Selected stage is no longer active"
            );
        }

        League league = leagueRepository.findById(leagueId)
                .orElseThrow(() ->
                        new LeagueNotFoundException(leagueId)
                );

        validateUserLeagueMembership(
                league,
                currentUser
        );

        AbilityUsage spyUsage =
                abilityUsageRepository
                        .findByUserAndLeagueAndStageAndAbility_Code(
                                currentUser,
                                league,
                                stage,
                                "SPY"
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "SPY is not active for this stage"
                                )
                        );

        if (spyUsage.getTargetUser() == null) {
            throw new IllegalArgumentException(
                    "SPY target has not been selected"
            );
        }

        User targetUser = spyUsage.getTargetUser();

        List<Prediction> predictions =
                predictionRepository
                        .findByUserAndLeagueAndMatch_Stage(
                                targetUser,
                                league,
                                stage
                        );

        return predictions.stream()
                .map(prediction -> new SpyPredictionResponse(
                        prediction.getMatch().getExternalMatchId(),
                        prediction.getMatch().getHomeTeam().getName(),
                        prediction.getMatch().getAwayTeam().getName(),
                        prediction.getMatch().getStage(),
                        prediction.getMatch().getHomeTeam().getLogoUrl(),
                        prediction.getMatch().getAwayTeam().getLogoUrl(),
                        prediction.getMatch().getMatchDate(),
                        prediction.getPredictedHomeScore(),
                        prediction.getPredictedAwayScore()
                ))
                .toList();
    }

    @Transactional
    public void activateAbility(AbilityActivationRequest request){

        User currentUser = userService.getCurrentUser();

        Ability ability = abilityRepository.findByCode(request.getAbilityCode())
                .orElseThrow(() -> new AbilityNotFoundException(request.getAbilityCode())
                );

        validateUserLevel(currentUser, ability);
        validateUserCoins(currentUser, ability);

        if (ability.getCode().equals("HOT_STREAK")) {
            activateHotStreak(currentUser, ability);
            currentUser.setCoins(
                    currentUser.getCoins() - ability.getPriceCoins()
            );
            userRepository.save(currentUser);
            return;
        }

        if (request.getStage() == null) {
            throw new IllegalArgumentException(
                    "Stage is required for this ability"
            );
        }

        String selectedStage = request.getStage();

        if (!matchRepository.existsByStageAndStatus(
                request.getStage(),
                "NOT STARTED"
        )) {
            throw new IllegalArgumentException(
                    "Selected stage is no longer active"
            );
        }

        if (request.getLeagueId() == null) {
            throw new IllegalArgumentException(
                    "League is required for this ability"
            );
        }

        League league = leagueRepository.findById(request.getLeagueId())
                .orElseThrow(() -> new LeagueNotFoundException(request.getLeagueId()));

        validateUserLeagueMembership(league, currentUser);

        Match targetMatch = null;
        User targetUser = null;

        switch (ability.getCode()) {

            case "SPY":

                validateAbilityTypeUsage(
                        currentUser,
                        league,
                        selectedStage,
                        ability
                );

                if (request.getTargetMatchId() != null) {
                    throw new IllegalArgumentException(
                            "Target match is not allowed for Spy"
                    );
                }

                if (request.getTargetUserId() != null) {
                    throw new IllegalArgumentException(
                            "Target user is not allowed when activating Spy"
                    );
                }

                break;

            case "LOCK":
            case "BOMB":

                validateAbilityTypeUsage(
                        currentUser,
                        league,
                        selectedStage,
                        ability
                );

                if (request.getTargetMatchId() == null) {
                    throw new IllegalArgumentException(
                            "Target match is required for this ability"
                    );
                }

                if (request.getTargetUserId() == null) {
                    throw new IllegalArgumentException(
                            "Target user is required for this ability"
                    );
                }

                targetMatch = matchRepository
                        .findById(request.getTargetMatchId())
                        .orElseThrow(() ->
                                new IllegalArgumentException("Match not found")
                        );

                targetUser = userRepository
                        .findById(request.getTargetUserId())
                        .orElseThrow(() ->
                                new IllegalArgumentException("Target user not found")
                        );

                validateTargetUserLeagueMembership(
                        league,
                        targetUser);

                validateTargetUserIsNotCurrentUser(
                        currentUser,
                        targetUser
                );

                if (!targetMatch.getStage().equals(selectedStage)) {
                    throw new IllegalArgumentException(
                            "Target match is not in the selected stage"
                    );
                }

                if (!targetMatch.getStatus().equals("NOT STARTED")) {
                    throw new IllegalArgumentException(
                            "Target match is no longer available"
                    );
                }

                if ("LOCK".equals(ability.getCode()) && !predictionRepository.existsByMatchAndUserAndLeague(targetMatch, targetUser, league)) {
                    throw new PredictionNotFoundException(targetMatch.getMatchId(), targetUser.getUsername(), league.getLeagueId());
                }

                if ("LOCK".equals(ability.getCode())) {

                    boolean alreadyLockedByAnotherUser =
                            abilityUsageRepository
                                    .existsByTargetUserAndTargetMatchAndLeagueAndAbility_Code(
                                            targetUser,
                                            targetMatch,
                                            league,
                                            "LOCK"
                                    );

                    if (alreadyLockedByAnotherUser) {
                        throw new IllegalArgumentException(
                                "This prediction is already locked"
                        );
                    }
                }


                if ("BOMB".equals(ability.getCode())) {

                    Optional<Prediction> prediction =
                            predictionRepository.findByMatchAndUserAndLeague(
                                    targetMatch,
                                    targetUser,
                                    league
                            );

                    prediction.ifPresent(predictionRepository::delete);
                }

                break;

            case "DOUBLE_POINTS":
            case "KINGS_ORDER":

                validateAbilityTypeUsage(
                        currentUser,
                        league,
                        selectedStage,
                        ability
                );

                if (request.getTargetMatchId() == null) {
                    throw new IllegalArgumentException(
                            "Target match is required for this ability"
                    );
                }

                if (request.getTargetUserId() != null) {
                    throw new IllegalArgumentException(
                            "Target user is not allowed for this ability"
                    );
                }

                targetMatch = matchRepository
                        .findById(request.getTargetMatchId())
                        .orElseThrow(() ->
                                new IllegalArgumentException("Match not found")
                        );

                if (!targetMatch.getStage().equals(selectedStage)) {
                    throw new IllegalArgumentException(
                            "Target match is not in the selected stage"
                    );
                }

                if (!targetMatch.getStatus().equals("NOT STARTED")) {
                    throw new IllegalArgumentException(
                            "Target match is no longer available"
                    );
                }

                break;

            case "SHIELD":

                validateAbilityTypeUsage(
                        currentUser,
                        league,
                        selectedStage,
                        ability
                );

                if (request.getTargetMatchId() != null
                        || request.getTargetUserId() != null) {

                    throw new IllegalArgumentException(
                            "Shield does not require targets"
                    );
                }

                break;

            case "JOKER":

                validateAbilityTypeUsage(
                        currentUser,
                        league,
                        selectedStage,
                        ability
                );

                if (request.getTargetMatchId() != null
                        || request.getTargetUserId() != null) {

                    throw new IllegalArgumentException(
                            "Joker does not require targets"
                    );
                }

                break;

            default:
                throw new IllegalArgumentException(
                        "Unsupported ability code"
                );
        }

        AbilityUsage usage = new AbilityUsage(
                currentUser,
                ability,
                league,
                selectedStage,
                targetMatch,
                targetUser
        );

        abilityUsageRepository.save(usage);
        currentUser.setCoins(
                currentUser.getCoins() - ability.getPriceCoins()
        );
        userRepository.save(currentUser);
    }

}
