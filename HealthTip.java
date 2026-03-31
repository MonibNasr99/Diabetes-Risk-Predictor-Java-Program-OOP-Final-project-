/**
 * HealthTip.java
 * Represents a single health/prevention tip.
 */
public class HealthTip {

    private final String icon;
    private final String tip;

    public HealthTip(String icon, String tip) {
        this.icon = icon;
        this.tip  = tip;
    }

    public String getIcon() { return icon; } //geters
    public String getTip()  { return tip; }

    @Override
    public String toString() {
        return icon + "  " + tip;
    }

    //  Default tips list (used in the UI) -----------------------------------------------------
    public static HealthTip[] getDefaultTips() {
        return new HealthTip[] {
            new HealthTip("-   ", "Maintain a balanced diet with low sugar intake"),
            new HealthTip("-   ", "Exercise at least 30 minutes daily"),
            new HealthTip("-   ", "Regularly monitor your blood glucose levels"),
            new HealthTip("-   ", "Avoid smoking and limit alcohol consumption"),
            new HealthTip("-   ", "Get 7-8 hours of quality sleep each night"),
            new HealthTip("-   ", "Increase fibre intake with vegetables and whole grains"),
            new HealthTip("-   ", "Stay hydrated with water instead of sugary drinks"),
        };
    }
}
