package pl.wojtczak.score_predictor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.wojtczak.score_predictor.service.TeamService;

@RestController
public class HelloController{

    private final TeamService footballService;

    @Autowired
    public HelloController(TeamService footballService) {
        this.footballService = footballService;
    }

//    @GetMapping("/hello")
//    public String hello(){
//        return footballService.getMessage();
//    }
}
