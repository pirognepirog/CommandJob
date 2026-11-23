package commandJob.rule.repository;

import commandJob.rule.entity.DynamicRuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DynamicRuleRepository extends JpaRepository<DynamicRuleEntity, UUID> {

}