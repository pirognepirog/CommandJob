package commandJob.rule.model;

import java.util.List;

/// RuleListResponse.java (обёртка {"data": [...]} для GET /rule):
public class RuleListResponse {
    private List<DynamicRuleDto> data;

    public RuleListResponse() {}

    public RuleListResponse(List<DynamicRuleDto> data) { this.data = data; }

    public List<DynamicRuleDto> getData() { return data; }
    public void setData(List<DynamicRuleDto> data) { this.data = data; }
}
