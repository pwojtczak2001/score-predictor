package pl.wojtczak.score_predictor.dto.request;

public class CreateLeagueRequest {

    private String name;

    public CreateLeagueRequest() {
    }

    public CreateLeagueRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
