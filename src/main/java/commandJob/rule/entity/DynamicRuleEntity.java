package commandJob.rule.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "dynamic_rules")
public class DynamicRuleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Column(name = "product_text", nullable = false, length = 1000)
    private String productText;

    // JSON-массив правил храним как TEXT
    @Column(name = "rule_json", nullable = false, columnDefinition = "TEXT")
    private String ruleJson;

    // Геттеры и сеттеры
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public UUID getProductId() { return productId; }
    public void setProductId(UUID productId) { this.productId = productId; }

    public String getProductText() { return productText; }
    public void setProductText(String productText) { this.productText = productText; }

    public String getRuleJson() { return ruleJson; }
    public void setRuleJson(String ruleJson) { this.ruleJson = ruleJson; }
}