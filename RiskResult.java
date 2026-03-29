/**
 * RiskResult.java
 * Stores the outcome of a diabetes risk prediction.
 * Demonstrates: Encapsulation, Objects
 */
public class RiskResult {

    private final double    riskScore;   // 0–100
    private final RiskLevel riskLevel;
    private final String    message;

    // ── Constructor ───────────────────────────────────────────────
    public RiskResult(double riskScore) {
        this.riskScore = Math.min(Math.max(riskScore, 1), 99);
        this.riskLevel = classify(this.riskScore);
        this.message   = buildMessage();
    }

    // ── Classify score into level ─────────────────────────────────
    private RiskLevel classify(double score) {
        if (score < 30) return RiskLevel.LOW;
        if (score < 70) return RiskLevel.MEDIUM;
        return RiskLevel.HIGH;
    }

    // ── Build display message ─────────────────────────────────────
    private String buildMessage() {
        switch (riskLevel) {
            case LOW:    return "✅ Low Risk — " + riskLevel.getAdvice();
            case MEDIUM: return "⚠️ Medium Risk — " + riskLevel.getAdvice();
            case HIGH:   return "❌ High Risk — " + riskLevel.getAdvice();
            default:     return riskLevel.getAdvice();
        }
    }

    // ── Getters ───────────────────────────────────────────────────
    public double    getRiskScore() { return riskScore; }
    public RiskLevel getRiskLevel() { return riskLevel; }
    public String    getMessage()   { return message; }

    @Override
    public String toString() {
        return String.format("RiskScore=%.1f%% Level=%s", riskScore, riskLevel.getLabel());
    }
}
