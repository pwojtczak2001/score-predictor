package pl.wojtczak.score_predictor.dto.request;

public class JoinLeagueRequest {

    private String inviteCode;

    public JoinLeagueRequest() {
    }

    public JoinLeagueRequest(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }
}
