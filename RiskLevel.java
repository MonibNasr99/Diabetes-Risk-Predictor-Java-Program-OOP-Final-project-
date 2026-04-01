/**
 * RiskLevel.java
 * Enum
 */
public enum RiskLevel {
    LOW    ("Low",    "Maintain a healthy lifestyle and stay active."),
    MEDIUM ("Medium", "Reduce sugar intake and monitor your glucose regularly."),
    HIGH   ("High",   "Please consult a doctor as soon as possible.");

    private final String label;
    private final String advice;

    RiskLevel(String label, String advice) {
        this.label  = label;
        this.advice = advice;
    }

    public String getLabel()  { return label; }
    public String getAdvice() { return advice; }
}
