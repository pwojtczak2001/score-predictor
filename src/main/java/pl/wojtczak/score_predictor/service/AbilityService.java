package pl.wojtczak.score_predictor.service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import pl.wojtczak.score_predictor.dto.request.AbilityActivationRequest;
import pl.wojtczak.score_predictor.dto.request.SpyTargetRequest;
import pl.wojtczak.score_predictor.dto.response.SpyPredictionResponse;
import pl.wojtczak.score_predictor.dto.response.SpyTargetUserResponse;
import pl.wojtczak.score_predictor.entity.*;
import pl.wojtczak.score_predictor.enums.AbilityType;
import pl.wojtczak.score_predictor.exception.*;
import pl.wojtczak.score_predictor.logging.GameEventLogger;
import pl.wojtczak.score_predictor.repository.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AbilityService {

    private final UserService userService;
    private final AbilityRepository abilityRepository;
    private final LeagueRepository leagueRepository;
    private final LeagueMemberRepository leagueMemberRepository;
    private final MatchRepository matchRepository;
    private final AbilityUsageRepository abilityUsageRepository;
    private final PredictionRepository predictionRepository;
    private final GameEventLogger gameEventLogger;
    private final UserRepository userRepository;

    public AbilityService(UserService userService,
                          AbilityRepository abilityRepository,
                          LeagueRepository leagueRepository,
                          LeagueMemberRepository leagueMemberRepository,
                          MatchRepository matchRepository, AbilityUsageRepository abilityUsageRepository, PredictionRepository predictionRepository, GameEventLogger gameEventLogger, UserRepository userRepository) {
        this.userService = userService;
        this.abilityRepository = abilityRepository;
        this.leagueRepository = leagueRepository;
        this.leagueMemberRepository = leagueMemberRepository;
        this.matchRepository = matchRepository;
        this.abilityUsageRepository = abilityUsageRepository;
        this.predictionRepository = predictionRepository;
        this.gameEventLogger = gameEventLogger;
        this.userRepository = userRepository;
    }

    private void validateUserLevel(
            User user,
            Ability ability
    ) {

        if (user.getLevel() < ability.getUnlockLevel()) {
            throw new BadRequestException(
                    "User level is too low for this ability"
            );
        }
    }

    private void validateUserCoins(
            User user,
            Ability ability
    ) {

        if (user.getCoins() < ability.getPriceCoins()) {
            throw new BadRequestException(
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
            throw new BadRequestException(
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

            throw new BadRequestException(
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

            throw new BadRequestException(
                    "User has already used an ability of this type in the selected stage"
            );
        }
    }

    private void activateHotStreak(User currentUser, Ability ability){

        if (abilityUsageRepository.existsByUserAndAbility_Code(currentUser, "HOT_STREAK")) {
            throw new BadRequestException(
                    "Hot Streak is already activated");
        }

        AbilityUsage abilityUsage = new AbilityUsage(
                currentUser,
                ability,
                null,
                null,
                null,
                null,
                null);

        abilityUsageRepository.save(abilityUsage);
    }

    public List<SpyTargetUserResponse> getSpyTargetUsers(Integer leagueId, String stage) {

        User currentUser = userService.getCurrentUser();

        if (!matchRepository.existsByStageAndStatus(
                stage,
                "NOT STARTED"
        )) {
            throw new BadRequestException(
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
                        new BadRequestException(
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
            throw new BadRequestException(
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
                                new BadRequestException(
                                        "SPY is not active for this stage"
                                )
                        );

        if (spyUsage.getTargetUser() != null) {
            throw new BadRequestException(
                    "SPY target has already been selected"
            );
        }

        User targetUser = userRepository.findById(
                request.getTargetUserId()
        ).orElseThrow(() ->
                new UserNotFoundException(request.getTargetUserId())
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
            throw new BadRequestException(
                    "Target user has no predictions in this stage"
            );
        }

        spyUsage.setTargetUser(targetUser);

        abilityUsageRepository.save(spyUsage);

        gameEventLogger.logAbilityUsed(
                currentUser,
                "SPY",
                targetUser,
                null
        );
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
            throw new BadRequestException(
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
                                new BadRequestException(
                                        "SPY is not active for this stage"
                                )
                        );

        if (spyUsage.getTargetUser() == null) {
            throw new BadRequestException(
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
            throw new BadRequestException(
                    "Stage is required for this ability"
            );
        }

        String selectedStage = request.getStage();

        if (!matchRepository.existsByStageAndStatus(
                request.getStage(),
                "NOT STARTED"
        )) {
            throw new BadRequestException(
                    "Selected stage is no longer active"
            );
        }

        if (request.getLeagueId() == null) {
            throw new BadRequestException(
                    "League is required for this ability"
            );
        }

        League league = leagueRepository.findById(request.getLeagueId())
                .orElseThrow(() -> new LeagueNotFoundException(request.getLeagueId()));

        validateUserLeagueMembership(league, currentUser);

        Match targetMatch = null;
        User targetUser = null;
        Boolean consumed = null;

        switch (ability.getCode()) {

            case "SPY":

                validateAbilityTypeUsage(
                        currentUser,
                        league,
                        selectedStage,
                        ability
                );

                if (request.getTargetMatchId() != null) {
                    throw new BadRequestException(
                            "Target match is not allowed for Spy"
                    );
                }

                if (request.getTargetUserId() != null) {
                    throw new BadRequestException(
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
                    throw new BadRequestException(
                            "Target match is required for this ability"
                    );
                }

                if (request.getTargetUserId() == null) {
                    throw new BadRequestException(
                            "Target user is required for this ability"
                    );
                }

                targetMatch = matchRepository
                        .findById(request.getTargetMatchId())
                        .orElseThrow(() ->
                                new MatchNotFoundException(request.getTargetMatchId())
                        );

                targetUser = userRepository
                        .findById(request.getTargetUserId())
                        .orElseThrow(() ->
                                new UserNotFoundException(request.getTargetUserId())
                        );

                validateTargetUserLeagueMembership(
                        league,
                        targetUser);

                validateTargetUserIsNotCurrentUser(
                        currentUser,
                        targetUser
                );

                if (!targetMatch.getStage().equals(selectedStage)) {
                    throw new BadRequestException(
                            "Target match is not in the selected stage"
                    );
                }

                if (!targetMatch.getStatus().equals("NOT STARTED")) {
                    throw new BadRequestException(
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
                        throw new BadRequestException(
                                "This prediction is already locked"
                        );
                    }
                }

                Optional<AbilityUsage> shieldUsage =
                        abilityUsageRepository
                                .findByUserAndLeagueAndStageAndConsumedAndAbility_Code(
                                        targetUser,
                                        league,
                                        selectedStage,
                                        false,
                                        "SHIELD"
                                );

                if (shieldUsage.isPresent()) {
                    shieldUsage.get().setConsumed(true);
                    abilityUsageRepository.save(shieldUsage.get());
                    currentUser.setCoins(
                            currentUser.getCoins() - ability.getPriceCoins()
                    );
                    userRepository.save(currentUser);

                    gameEventLogger.logAbilityUsed(
                            currentUser,
                            ability.getCode(),
                            targetUser,
                            targetMatch
                    );

                    return;
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
                    throw new BadRequestException(
                            "Target match is required for this ability"
                    );
                }

                if (request.getTargetUserId() != null) {
                    throw new BadRequestException(
                            "Target user is not allowed for this ability"
                    );
                }

                targetMatch = matchRepository
                        .findById(request.getTargetMatchId())
                        .orElseThrow(() ->
                                new MatchNotFoundException(request.getTargetMatchId())
                        );

                if (!targetMatch.getStage().equals(selectedStage)) {
                    throw new BadRequestException(
                            "Target match is not in the selected stage"
                    );
                }

                if (!targetMatch.getStatus().equals("NOT STARTED")) {
                    throw new BadRequestException(
                            "Target match is no longer available"
                    );
                }

                if ("KINGS_ORDER".equals(ability.getCode())) {
                    List<Prediction> predictions = predictionRepository.findByMatchAndLeague(targetMatch, league);
                    for (Prediction prediction : predictions) {

                        if (prediction.getUser().getUserId()
                                .equals(currentUser.getUserId())) {
                            continue;
                        }

                        Optional<AbilityUsage> shieldUsageForUser =
                                abilityUsageRepository
                                        .findByUserAndLeagueAndStageAndConsumedAndAbility_Code(
                                                prediction.getUser(),
                                                league,
                                                selectedStage,
                                                false,
                                                "SHIELD"
                                        );

                        if (shieldUsageForUser.isPresent()) {
                            shieldUsageForUser.get().setConsumed(true);
                            abilityUsageRepository.save(shieldUsageForUser.get());
                        } else {
                            predictionRepository.delete(prediction);
                        }
                    }
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

                    throw new BadRequestException(
                            "Shield does not require targets"
                    );
                }

                consumed = false;

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

                    throw new BadRequestException(
                            "Joker does not require targets"
                    );
                }

                break;

            default:
                throw new BadRequestException(
                        "Unsupported ability code"
                );
        }

        AbilityUsage usage = new AbilityUsage(
                currentUser,
                ability,
                league,
                selectedStage,
                targetMatch,
                targetUser,
                consumed);



        abilityUsageRepository.save(usage);
        currentUser.setCoins(
                currentUser.getCoins() - ability.getPriceCoins()
        );
        userRepository.save(currentUser);

        gameEventLogger.logAbilityUsed(
                currentUser,
                ability.getCode(),
                targetUser,
                targetMatch
        );

    }

}
