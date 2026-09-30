package pl.wojtczak.score_predictor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ScorePredictorApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScorePredictorApplication.class, args);
	}

}
