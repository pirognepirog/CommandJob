package commandJob.rule.controller;

import commandJob.rule.model.DynamicRuleDto;
import commandJob.rule.model.RuleListResponse;
import commandJob.rule.service.DynamicRuleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/rule")

public class RuleController {

    private final DynamicRuleService service;

    public RuleController(DynamicRuleService service) {
        this.service = service;
    }

    // 1. Добавление правила
    @PostMapping
    public ResponseEntity<DynamicRuleDto> create(@RequestBody DynamicRuleDto dto) throws Exception {
        return ResponseEntity.ok(service.create(dto));
    }

    // 2. Получение списка всех правил
    @GetMapping
    public ResponseEntity<RuleListResponse> list() {
        List<DynamicRuleDto> rules = service.findAll();
        return ResponseEntity.ok(new RuleListResponse(rules));
    }

    // 3. Удаление правила по product_id
    @DeleteMapping("/{product_id}")
    public ResponseEntity<Void> delete(@PathVariable("product_id") UUID productId) {
        service.delete(productId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
