/**
 * Patient.java
 * Represents a patient's health data.
 * Demonstrates: Encapsulation (private fields, getters/setters), Objects
 */
public class Patient {

    // Private fields (Encapsulation) 
    private String name;
    private int    age;
    private double glucose;
    private double bloodPressure;
    private double bmi;
    private double insulin;
    private double skinThickness;
    private double diabetesPedigree;
    private int    pregnancies;

    // Constructor 
    public Patient(String name, int age, double glucose, double bloodPressure,
                   double bmi, double insulin, double skinThickness,
                   double diabetesPedigree, int pregnancies) {
        this.name             = name;
        this.age              = age;
        this.glucose          = glucose;
        this.bloodPressure    = bloodPressure;
        this.bmi              = bmi;
        this.insulin          = insulin;
        this.skinThickness    = skinThickness;
        this.diabetesPedigree = diabetesPedigree;
        this.pregnancies      = pregnancies;
    }

    // Default constructor
    public Patient() {
        this("Unknown", 0, 0, 0, 0, 0, 0, 0, 0);
    }

    // Getters 
    public String getName()             { return name; }
    public int    getAge()              { return age; }
    public double getGlucose()          { return glucose; }
    public double getBloodPressure()    { return bloodPressure; }
    public double getBmi()              { return bmi; }
    public double getInsulin()          { return insulin; }
    public double getSkinThickness()    { return skinThickness; }
    public double getDiabetesPedigree() { return diabetesPedigree; }
    public int    getPregnancies()      { return pregnancies; }

    // Setters 
    public void setName(String name)                       { this.name = name; }
    public void setAge(int age)                            { this.age = age; }
    public void setGlucose(double glucose)                 { this.glucose = glucose; }
    public void setBloodPressure(double bloodPressure)     { this.bloodPressure = bloodPressure; }
    public void setBmi(double bmi)                         { this.bmi = bmi; }
    public void setInsulin(double insulin)                 { this.insulin = insulin; }
    public void setSkinThickness(double skinThickness)     { this.skinThickness = skinThickness; }
    public void setDiabetesPedigree(double diabetesPedigree) { this.diabetesPedigree = diabetesPedigree; }
    public void setPregnancies(int pregnancies)            { this.pregnancies = pregnancies; }

    // toString for file saving 
    @Override
    public String toString() {
        return String.format("%s,%d,%.2f,%.2f,%.2f,%.2f,%.2f,%.4f,%d",
            name, age, glucose, bloodPressure, bmi,
            insulin, skinThickness, diabetesPedigree, pregnancies);
    }
}
