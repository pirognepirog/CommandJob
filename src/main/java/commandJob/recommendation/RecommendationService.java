package commandJob.recommendation;

import commandJob.dto.RecommendationDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    private final List<RecommendationRuleSet> ruleSets;

    public RecommendationService(List<RecommendationRuleSet> ruleSets) {
        this.ruleSets = ruleSets;
    }

    public List<RecommendationDto> getRecommendations(UUID userId) {
        return ruleSets.stream()
                .map(rule -> {
                    Optional<RecommendationDto> res = rule.check(userId);
                    System.out.println(">>> Rule: " + rule.getClass().getSimpleName() + " -> " + (res.isPresent() ? "OK" : "empty"));
                    return res;
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}