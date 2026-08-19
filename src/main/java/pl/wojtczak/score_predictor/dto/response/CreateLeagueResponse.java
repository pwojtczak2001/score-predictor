package pl.wojtczak.score_predictor.dto.response;

public class CreateLeagueResponse {

    private Integer leagueId;
    private String leagueName;
    private String inviteCode;

    public CreateLeagueResponse(Integer leagueId, String leagueName, String inviteCode) {
        this.leagueId = leagueId;
        this.leagueName = leagueName;
        this.inviteCode = inviteCode;
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
