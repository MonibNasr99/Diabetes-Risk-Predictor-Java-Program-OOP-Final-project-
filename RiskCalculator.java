/**
 * RiskCalculator.java
 * Abstract base class for risk calculation strategies.
 */
public abstract class RiskCalculator {

    // Abstract method - subclasses must provide their own implementation
    public abstract RiskResult calculate(Patient patient);

    /* 
     Template method shared by all subclasses - it takes the given number and 
     change it to a cumber between 1 and a 0. 
    */
    protected double sigmoid(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    }

    // Returns a display name for the calculator type
    public abstract String getModelName();
}
