package commandJob.rule.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import commandJob.rule.entity.DynamicRuleEntity;
import commandJob.rule.model.DynamicRuleDto;
import commandJob.rule.model.RuleQuery;
import commandJob.rule.repository.DynamicRuleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DynamicRuleService {

    private final DynamicRuleRepository repo;
    private final ObjectMapper mapper = new ObjectMapper();

    public DynamicRuleService(DynamicRuleRepository repo) {
        this.repo = repo;
    }

    public DynamicRuleDto create(DynamicRuleDto dto) throws Exception {
        DynamicRuleEntity e = new DynamicRuleEntity();
        e.setProductName(dto.getProductName());
        e.setProductId(dto.getProductId());
        e.setProductText(dto.getProductText());

        // Превращаем список правил в JSON-строку для хранения в базе
        e.setRuleJson(mapper.writeValueAsString(dto.getRule()));

        e = repo.save(e);
        dto.setId(e.getId());
        return dto;
    }

    public List<DynamicRuleDto> findAll() {
        return repo.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public void delete(UUID productId) {
        repo.findAll().stream()
                .filter(e -> productId.equals(e.getProductId()))
                .forEach(repo::delete);
    }

    private DynamicRuleDto toDto(DynamicRuleEntity e) {
        try {
            DynamicRuleDto dto = new DynamicRuleDto();
            dto.setId(e.getId());
            dto.setProductName(e.getProductName());
            dto.setProductId(e.getProductId());
            dto.setProductText(e.getProductText());

            // Достаем JSON из базы и превращаем обратно в список объектов
            dto.setRule(mapper.readValue(e.getRuleJson(), new TypeReference<List<RuleQuery>>() {}));
            return dto;
        } catch (Exception ex) {
            throw new RuntimeException("Ошибка при чтении JSON из базы", ex);
        }
    }
}