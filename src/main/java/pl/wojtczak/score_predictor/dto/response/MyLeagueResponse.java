package pl.wojtczak.score_predictor.dto.response;

public class MyLeagueResponse {

    private Integer leagueId;
    private String leagueName;
    private String inviteCode;
    private Integer membersCount;
    private Integer currentPoints;
    private Integer position;

    public MyLeagueResponse(Integer leagueId, String leagueName, String inviteCode, Integer membersCount, Integer currentPoints, Integer position) {
        this.leagueId = leagueId;
        this.leagueName = leagueName;
        this.inviteCode = inviteCode;
        this.membersCount = membersCount;
        this.currentPoints = currentPoints;
        this.position = position;
    }

    public MyLeagueResponse() {
    }

    public Integer getLeagueId() {
        return leagueId;
    }

    public String getLeagueName() {
        return leagueName;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public Integer getMembersCount() {
        return membersCount;
    }

    public Integer getCurrentPoints() {
        return currentPoints;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public void setCurrentPoints(Integer currentPoints) {
        this.currentPoints = currentPoints;
    }

    public void setMembersCount(Integer membersCount) {
        this.membersCount = membersCount;
    }

    public void setLeagueId(Integer leagueId) {
        this.leagueId = leagueId;
    }

    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

}
