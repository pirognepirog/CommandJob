package commandJob.rule.model;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

/// само правило с продуктом
public class DynamicRuleDto {
    private UUID id;
/*
В JSON из ТЗ поля называются product_name, product_id, product_text (snake_case).
В Java-классе названия: productName, productId, productText (camelCase).
@JsonProperty - нужно, чтобы Jackson правильно связал JSON с полями
 */
    @JsonProperty("product_name")
    private String productName;
    @JsonProperty("product_id")
    private UUID productId;
    @JsonProperty("product_text")
    private String productText;
    private List<RuleQuery> rule;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public UUID getProductId() { return productId; }
    public void setProductId(UUID productId) { this.productId = productId; }

    public String getProductText() { return productText; }
    public void setProductText(String productText) { this.productText = productText; }

    public List<RuleQuery> getRule() { return rule; }
    public void setRule(List<RuleQuery> rule) { this.rule = rule; }
}
