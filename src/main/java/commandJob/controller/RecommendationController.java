package commandJob.controller;

import commandJob.dto.RecommendationDto;
import commandJob.recommendation.RecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/recommendation")
public class RecommendationController {

    private final RecommendationService service;

    public RecommendationController(RecommendationService service) {
        this.service = service;
    }

    @GetMapping("/{user_id}")
    public ResponseEntity<List<RecommendationDto>> getRecommendations(
            @PathVariable("user_id") UUID userId) {
        List<RecommendationDto> result = service.getRecommendations(userId);
        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(result);
    }
}