import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PatientRecord.java
 * Manages a collection of patients and their results.
 * Demonstrates: Collections (ArrayList, HashMap), File I/O (data persistence)
 */
public class PatientRecord {

    private static final String FILE_PATH = "C:\\study\\City Universty Study\\Semester 5\\3 - Object Oriented Programming\\Assignmets\\Final Project\\The project\\3- the project\\patient_records.csv";

    // Collections to store patients and their results
    private final ArrayList<Patient>           patients = new ArrayList<>();
    private final HashMap<String, RiskResult>  results  = new HashMap<>();

    // ── Add a patient and their result ───────────────────────────
    public void addRecord(Patient patient, RiskResult result) {
        patients.add(patient);
        results.put(patient.getName() + "_" + patients.size(), result);
    }

    // ── Get all patients ─────────────────────────────────────────
    public List<Patient> getPatients() {
        return new ArrayList<>(patients);
    }

    // ── Get all results ──────────────────────────────────────────
    public Map<String, RiskResult> getResults() {
        return new HashMap<>(results);
    }

    // ── Save records to CSV file ─────────────────────────────────
    public void saveToFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_PATH, true))) {
            for (int i = 0; i < patients.size(); i++) {
                Patient    p = patients.get(i);
                RiskResult r = results.get(p.getName() + "_" + (i + 1));
                if (r != null) {
                    pw.printf("%s,%.1f,%s%n",
                        p.toString(),
                        r.getRiskScore(),
                        r.getRiskLevel().getLabel());
                }
            }
            System.out.println("Records saved to " + FILE_PATH);
        } catch (IOException e) {
            System.err.println("Error saving records: " + e.getMessage());
        }
    }

    // ── Load records from CSV file ────────────────────────────────
    public void loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 9) {
                    try {
                        Patient p = new Patient(
                            parts[0],
                            Integer.parseInt(parts[1].trim()),
                            Double.parseDouble(parts[2].trim()),
                            Double.parseDouble(parts[3].trim()),
                            Double.parseDouble(parts[4].trim()),
                            Double.parseDouble(parts[5].trim()),
                            Double.parseDouble(parts[6].trim()),
                            Double.parseDouble(parts[7].trim()),
                            Integer.parseInt(parts[8].trim())
                        );
                        double score = parts.length >= 10
                            ? Double.parseDouble(parts[9].trim()) : 0;
                        patients.add(p);
                        results.put(p.getName() + "_" + patients.size(),
                                    new RiskResult(score));
                    } catch (NumberFormatException ignored) { /* skip bad rows */ }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading records: " + e.getMessage());
        }
    }

    // ── Count of stored records ───────────────────────────────────
    public int size() {
        return patients.size();
    }
}
