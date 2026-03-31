/**
 * LogisticRiskCalculator.java
 * Implements a logistic (sigmoid) weighted-feature risk model.
 * Demonstrates: Inheritance (extends RiskCalculator),
 *               Polymorphism (overrides calculate / getModelName),
 *               Runtime polymorphism via superclass reference
 */
public class LogisticRiskCalculator extends RiskCalculator {

    // ── Feature weights (normalised) ─────────────────────────────
    private static final double BIAS        = -8.0;
    private static final double W_GLUCOSE   =  5.5;
    private static final double W_BMI       =  3.0;
    private static final double W_AGE       =  2.0;
    private static final double W_PEDIGREE  =  1.8;
    private static final double W_PREGNANT  =  1.5;
    private static final double W_INSULIN   =  0.8;
    private static final double W_SKIN      =  0.5;
    private static final double W_BP        = -0.3;

    // ── Feature max values for normalisation ─────────────────────
    private static final double MAX_GLUCOSE   = 200.0;
    private static final double MAX_BMI       =  67.1;
    private static final double MAX_AGE       =  81.0;
    private static final double MAX_PEDIGREE  =   2.42;
    private static final double MAX_PREGNANT  =  17.0;
    private static final double MAX_INSULIN   = 846.0;
    private static final double MAX_SKIN      =  99.0;
    private static final double MAX_BP        = 122.0;

    // ── Override: core calculation ────────────────────────────────
    @Override
    public RiskResult calculate(Patient p) {
        double score = BIAS
            + (p.getGlucose()          / MAX_GLUCOSE)  * W_GLUCOSE
            + (p.getBmi()              / MAX_BMI)       * W_BMI
            + (p.getAge()              / MAX_AGE)       * W_AGE
            + (p.getDiabetesPedigree() / MAX_PEDIGREE)  * W_PEDIGREE
            + (p.getPregnancies()      / MAX_PREGNANT)  * W_PREGNANT
            + (p.getInsulin()          / MAX_INSULIN)   * W_INSULIN
            + (p.getSkinThickness()    / MAX_SKIN)      * W_SKIN
            - (p.getBloodPressure()    / MAX_BP)        * W_BP;

        double risk = sigmoid(score) * 100.0;
        return new RiskResult(risk);
    }

    // ── Override: model name ──────────────────────────────────────
    @Override
    public String getModelName() {
        return "Logistic Risk Model";
    }
}
