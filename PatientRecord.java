import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PatientRecord.java
 * Manages a collection of patients and their results.
 */
public class PatientRecord {

    private static final String FILE_PATH = "patient_records.csv";

    // Collections to store patients and their results
    private final ArrayList<Patient>           patients = new ArrayList<>();
    private final HashMap<String, RiskResult>  results  = new HashMap<>();


    //  Check if a patient already exists based on name and age 
    private boolean patientExists(Patient newPatient) {

    for (Patient p : patients) {

        if (p.getName().equalsIgnoreCase(newPatient.getName())
            && p.getAge() == newPatient.getAge()) {

            return true;
        }
    }

    return false;
}

    //  Add a patient and their result 
    public void addRecord(Patient patient, RiskResult result) {

    if (patientExists(patient)) {
        System.out.println("Patient already exists. Skipping duplicate.");
        return;
    }

    patients.add(patient);
    results.put("record_" + patients.size(), result);
}

    //  Get all patients
    public List<Patient> getPatients() {
        return new ArrayList<>(patients);
    }

    // - Get all results
    public Map<String, RiskResult> getResults() {
        return new HashMap<>(results);
    }

    // == Save records to CSV file -----------------------------------------------------
    public void saveToFile() {

    File file = new File(FILE_PATH);
    boolean newFile = !file.exists() || file.length() == 0;

    try (PrintWriter pw = new PrintWriter(new FileWriter(file, true))) {

        if (newFile) {
            pw.println("RecordID,Date,PatientName,Age,Pregnancies,Glucose,BloodPressure,SkinThickness,Insulin,BMI,DiabetesPedigree,RiskScore,RiskLevel,Model");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (int i = 0; i < patients.size(); i++) {

            Patient p = patients.get(i);
            RiskResult r = results.get("record_" + (i + 1));

            if (r != null) {

                pw.printf(
                    "%d,%s,%s,%d,%d,%.2f,%.2f,%.2f,%.2f,%.2f,%.4f,%.1f,%s,%s%n",
                    i + 1,
                    LocalDateTime.now().format(formatter),
                    p.getName(),
                    p.getAge(),
                    p.getPregnancies(),
                    p.getGlucose(),
                    p.getBloodPressure(),
                    p.getSkinThickness(),
                    p.getInsulin(),
                    p.getBmi(),
                    p.getDiabetesPedigree(),
                    r.getRiskScore(),
                    r.getRiskLevel().getLabel(),
                    "Logistic Risk Model"
                );
            }
        }

    } catch (IOException e) {
        System.err.println("Error saving records: " + e.getMessage());
    }
}

    //  Load records from CSV file -----------------------------------------------------
    public void loadFromFile() {

    File file = new File(FILE_PATH);
    if (!file.exists()) return;

    try (BufferedReader br = new BufferedReader(new FileReader(file))) {

        String line;

        while ((line = br.readLine()) != null) {

            if (line.startsWith("RecordID")) continue;

            String[] parts = line.split(",");

            if (parts.length >= 14) {

                Patient p = new Patient(
                    parts[2],
                    Integer.parseInt(parts[3]),
                    Double.parseDouble(parts[5]),
                    Double.parseDouble(parts[6]),
                    Double.parseDouble(parts[9]),
                    Double.parseDouble(parts[8]),
                    Double.parseDouble(parts[7]),
                    Double.parseDouble(parts[10]),
                    Integer.parseInt(parts[4])
                );

                double score = Double.parseDouble(parts[11]);

                patients.add(p);
                results.put("record_" + patients.size(), new RiskResult(score));
            }
        }

    } catch (IOException e) {
        System.err.println("Error loading records: " + e.getMessage());
    }
}

    //  Count of stored records -----------------------------------------------------
    public int size() {
        return patients.size();
    }
}
