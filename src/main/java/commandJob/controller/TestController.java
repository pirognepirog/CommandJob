package commandJob.controller;

import commandJob.repository.RecommendationsRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class TestController {

    private final RecommendationsRepository recommendationsRepository;

    public TestController(RecommendationsRepository recommendationsRepository) {
        this.recommendationsRepository = recommendationsRepository;
    }

    @GetMapping("/test-amount")
    public int testAmount(@RequestParam UUID userId) {
        return recommendationsRepository.getRandomTransactionAmount(userId);
    }
}