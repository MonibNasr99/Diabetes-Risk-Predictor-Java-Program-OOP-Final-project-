import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * DiabetesRiskPredictor.java
 * Main GUI application for diabetes risk prediction.
 */
public class Main extends JFrame {

    //  Palette -----------------------------------------------------
    private static final Color BG_SIDEBAR   = new Color(245, 248, 252);
    private static final Color BG_MAIN      = Color.WHITE;
    private static final Color ACCENT_BLUE  = new Color(52, 107, 193);
    private static final Color TEXT_DARK    = new Color(30, 40, 55);
    private static final Color TEXT_MUTED   = new Color(110, 120, 140);
    private static final Color BORDER_COLOR = new Color(210, 218, 230);

    private static final Color GREEN  = new Color(22, 135, 90);
    private static final Color AMBER  = new Color(200, 120, 10);
    private static final Color RED    = new Color(185, 40, 40);

    // == Input fields -----------------------------------------------------
    private JTextField tfPregnancies, tfGlucose, tfBloodPressure,
                       tfSkinThickness, tfInsulin, tfBMI,
                       tfPedigree, tfAge, tfName;

    //  Result display -----------------------------------------------------
    private JProgressBar riskBar;
    private JLabel       lblRiskPercent, lblRiskLevel;
    private JTextArea    taMessage;

    //  Domain objects
    private final RiskCalculator calculator = new LogisticRiskCalculator();
    private final PatientRecord  records    = new PatientRecord();

    // -----------------------------------------------------
    public Main() {
        setTitle("Diabetes Risk Predictor"); //Titel
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // normal
        setSize(1000, 760); //scale
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 0));

        records.loadFromFile();   // load saved records on start

        add(buildSidebar(), BorderLayout.WEST); //
        add(buildMainPanel(), BorderLayout.CENTER);

        setVisible(true);
    }

    // -----------------------------------------------------
    //  SIDEBAR
    // -----------------------------------------------------
    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBackground(BG_SIDEBAR);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 0, 1, BORDER_COLOR),
            new EmptyBorder(20, 14, 20, 14)));

        //  App branding -----------------------------------------------------
        JLabel appIcon = new JLabel("🏥");
        appIcon.setFont(new Font("SansSerif", Font.PLAIN, 36));
        appIcon.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(appIcon); // adding the icon
        sidebar.add(Box.createVerticalStrut(4));

        JLabel appName = new JLabel("Diabetes Risk");
        appName.setFont(new Font("SansSerif", Font.BOLD, 17));
        appName.setForeground(ACCENT_BLUE);
        appName.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(appName);

        JLabel appSub = new JLabel("Predictor"); //the suptitel
        appSub.setFont(new Font("SansSerif", Font.PLAIN, 15));
        appSub.setForeground(TEXT_MUTED);
        appSub.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(appSub);
        sidebar.add(Box.createVerticalStrut(18));

        sidebar.add(sidebarDivider());

        //  How It Works -----------------------------------------------------
        sidebar.add(sidebarSectionTitle("How It Works"));
        sidebar.add(Box.createVerticalStrut(4));
        sidebar.add(sidebarText(
            "- Enter your health measurements.\n " +" \n- The app applies a weighted " +
            "statistical model to estimate your likelihood of diabetes."));
        sidebar.add(Box.createVerticalStrut(14));

        sidebar.add(sidebarDivider());

        //  Risk Levels -----------------------------------------------------
        sidebar.add(sidebarSectionTitle("Risk Levels"));
        sidebar.add(Box.createVerticalStrut(6));
        sidebar.add(riskLegendRow("●", "Low          ( < 30% )",   GREEN));
        sidebar.add(Box.createVerticalStrut(4));
        sidebar.add(riskLegendRow("●", "Medium   ( 30% – 70% )", AMBER));
        sidebar.add(Box.createVerticalStrut(4));
        sidebar.add(riskLegendRow("●", "High         ( > 70% )",  RED));
        sidebar.add(Box.createVerticalStrut(14));

        sidebar.add(sidebarDivider());

        // Disclaimer -----------------------------------------------------
        JTextArea disc = sidebarText(
            "⚠️ This tool provides estimates only. It is NOT a substitute " +
            "for professional medical advice. Always consult a doctor.");
        disc.setForeground(new Color(160, 100, 20));
        sidebar.add(disc);

        sidebar.add(Box.createVerticalGlue());

        // Record count -----------------------------------------------------
        JLabel recLabel = new JLabel("Saved records: " + records.size());
        recLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        recLabel.setForeground(TEXT_MUTED);
        recLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(recLabel);

        return sidebar;
    }

    // -----------------------------------------------------
    //  MAIN PANEL
    // -----------------------------------------------------
    private JScrollPane buildMainPanel() {
        JPanel main = new JPanel();
        main.setBackground(BG_MAIN);
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBorder(new EmptyBorder(24, 30, 24, 30));

        // Title
        JLabel title = new JLabel("Diabetes Risk Prediction");
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        title.setForeground(TEXT_DARK);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        main.add(title);

        JLabel subtitle = new JLabel("Enter your health details below to receive a personalised risk estimate.");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitle.setForeground(TEXT_MUTED);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        main.add(subtitle);
        main.add(Box.createVerticalStrut(20));

        main.add(buildInputPanel());
        main.add(Box.createVerticalStrut(18));
        main.add(buildResultsPanel());
        main.add(Box.createVerticalStrut(18));
        main.add(buildTipsPanel());
        main.add(Box.createVerticalStrut(18));

        JLabel footer = new JLabel("Developed for BIT1123 Object-Oriented Programming");
        footer.setFont(new Font("SansSerif", Font.PLAIN, 11));
        footer.setForeground(TEXT_MUTED);
        footer.setAlignmentX(Component.LEFT_ALIGNMENT);
        main.add(footer);

        JScrollPane scroll = new JScrollPane(main);
        scroll.setBackground(BG_MAIN);
        scroll.getVerticalScrollBar().setUnitIncrement(12);
        scroll.setBorder(null);
        return scroll;
    }

    //  Input Form -----------------------------------------------------
    private JPanel buildInputPanel() {
        JPanel wrapper = new JPanel();
        wrapper.setBackground(BG_MAIN);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBorder(panelBorder("Your Health Information"));
        wrapper.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Name field (single row)
        JPanel nameRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        nameRow.setBackground(BG_MAIN);
        nameRow.add(fieldLabel("Patient Name       "));
        tfName = new JTextField(20);
        tfName.setText(" ");
        nameRow.add(tfName);
        nameRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrapper.add(nameRow);
        wrapper.add(Box.createVerticalStrut(10));

        // Grid: 4 rows × 4 cols (label, field, label, field)
        JPanel grid = new JPanel(new GridLayout(4, 4, 14, 10));
        grid.setBackground(BG_MAIN);

        tfPregnancies   = addField(grid, "Pregnancies");
        tfGlucose       = addField(grid, "Glucose (mg/dL)");
        tfBloodPressure = addField(grid, "Blood Pressure (mmHg)");
        tfSkinThickness = addField(grid, "Skin Thickness (mm)");
        tfInsulin       = addField(grid, "Insulin (μU/mL)");
        tfBMI           = addField(grid, "BMI");
        tfPedigree      = addField(grid, "Diabetes Pedigree");
        tfAge           = addField(grid, "Age (years)");

        // Default sample values
        tfPregnancies.setText(" ");
        tfGlucose.setText(" ");
        tfBloodPressure.setText(" ");
        tfSkinThickness.setText(" ");
        tfInsulin.setText(" ");
        tfBMI.setText(" ");
        tfPedigree.setText(" ");
        tfAge.setText(" ");

        grid.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrapper.add(grid);
        wrapper.add(Box.createVerticalStrut(14));

        // Buttons row -----------------------------------------------------
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        btnRow.setBackground(BG_MAIN);

        JButton btnPredict = styledButton("  Predict Risk", ACCENT_BLUE);
        btnPredict.addActionListener(e -> predict());
        btnRow.add(btnPredict);

        btnRow.add(Box.createHorizontalStrut(10));

        JButton btnSave = styledButton("  Save Record", new Color(60, 130, 80));
        btnSave.addActionListener(e -> saveCurrentRecord());
        btnRow.add(btnSave);

        btnRow.add(Box.createHorizontalStrut(10));

        JButton btnReset = styledButton("  Reset", new Color(130, 130, 140));
        btnReset.addActionListener(e -> resetFields());
        btnRow.add(btnReset);

        btnRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrapper.add(btnRow);

        return wrapper;
    }

    //  Results Panel -----------------------------------------------------
    private JPanel buildResultsPanel() {
        JPanel wrapper = new JPanel();
        wrapper.setBackground(BG_MAIN);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBorder(panelBorder("Prediction Result"));
        wrapper.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblRiskPercent = new JLabel(" ");
        lblRiskPercent.setFont(new Font("SansSerif", Font.BOLD, 36));
        lblRiskPercent.setForeground(TEXT_MUTED);
        lblRiskPercent.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrapper.add(lblRiskPercent);
        wrapper.add(Box.createVerticalStrut(6));

        riskBar = new JProgressBar(0, 100);
        riskBar.setStringPainted(false);
        riskBar.setPreferredSize(new Dimension(0, 16));
        riskBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 16));
        riskBar.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrapper.add(riskBar);
        wrapper.add(Box.createVerticalStrut(6));

        lblRiskLevel = new JLabel("Run a prediction to see results");
        lblRiskLevel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblRiskLevel.setForeground(TEXT_MUTED);
        lblRiskLevel.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrapper.add(lblRiskLevel);
        wrapper.add(Box.createVerticalStrut(10));

        taMessage = new JTextArea(2, 40);
        taMessage.setEditable(false);
        taMessage.setLineWrap(true);
        taMessage.setWrapStyleWord(true);
        taMessage.setFont(new Font("SansSerif", Font.BOLD, 14));
        taMessage.setBackground(new Color(245, 248, 252));
        taMessage.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COLOR, 1, true),
            new EmptyBorder(10, 14, 10, 14)));
        taMessage.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrapper.add(taMessage);

        return wrapper;
    }

    //  Prevention Tips Panel -----------------------------------------------------
    private JPanel buildTipsPanel() {
        JPanel wrapper = new JPanel();
        wrapper.setBackground(BG_MAIN);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBorder(panelBorder("Prevention Tips"));
        wrapper.setAlignmentX(Component.LEFT_ALIGNMENT);

        StringBuilder sb = new StringBuilder();
        for (HealthTip tip : HealthTip.getDefaultTips()) {
            sb.append(tip.toString()).append("\n");
        }

        JTextArea taTips = new JTextArea(sb.toString().trim());
        taTips.setEditable(false);
        taTips.setFont(new Font("SansSerif", Font.PLAIN, 13));
        taTips.setForeground(TEXT_DARK);
        taTips.setBackground(new Color(240, 248, 240));
        taTips.setBorder(new EmptyBorder(10, 14, 10, 14));
        taTips.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrapper.add(taTips);

        return wrapper;
    }

    // -----------------------------------------------------
    //  ACTIONS
    // -----------------------------------------------------
    private void predict() {
        Patient patient = readFields();
        if (patient == null) return;                      // validation failed

        
        RiskResult result = calculator.calculate(patient);
        updateResults(result);
    }

    private void saveCurrentRecord() {
        Patient patient = readFields();
        if (patient == null) return;

        RiskResult result = calculator.calculate(patient);
        records.addRecord(patient, result);
        records.saveToFile();

        JOptionPane.showMessageDialog(this,
            "Record saved successfully! (" + records.size() + " total)",
            "Saved", JOptionPane.INFORMATION_MESSAGE);
    }

    private void resetFields() {
        tfName.setText(" ");
        tfPregnancies.setText(" ");
        tfGlucose.setText(" ");
        tfBloodPressure.setText(" ");
        tfSkinThickness.setText(" ");
        tfInsulin.setText(" ");
        tfBMI.setText(" ");
        tfPedigree.setText(" ");
        tfAge.setText(" ");

        lblRiskPercent.setText(" ");
        lblRiskPercent.setForeground(TEXT_MUTED);
        riskBar.setValue(0);
        riskBar.setForeground(ACCENT_BLUE);
        lblRiskLevel.setText("Run a prediction to see results");
        lblRiskLevel.setForeground(TEXT_MUTED);
        taMessage.setText("");
        taMessage.setBackground(new Color(245, 248, 252));
        taMessage.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COLOR, 1, true),
            new EmptyBorder(10, 14, 10, 14)));
    }

    // ── Parse input fields into a Patient object ──────────────────
    private Patient readFields() {
        try {
            int    pregnancies = Integer.parseInt(tfPregnancies.getText().trim());
            double glucose     = Double.parseDouble(tfGlucose.getText().trim());
            double bp          = Double.parseDouble(tfBloodPressure.getText().trim());
            double skin        = Double.parseDouble(tfSkinThickness.getText().trim());
            double insulin     = Double.parseDouble(tfInsulin.getText().trim());
            double bmi         = Double.parseDouble(tfBMI.getText().trim());
            double pedigree    = Double.parseDouble(tfPedigree.getText().trim());
            int    age         = Integer.parseInt(tfAge.getText().trim());
            String name        = tfName.getText().trim().isEmpty() ? "Anonymous" : tfName.getText().trim();

            return new Patient(name, age, glucose, bp, bmi, insulin, skin, pedigree, pregnancies);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Please enter valid numbers in all fields.",
                "Input Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    //  sending result to UI -----------------------------------------------------
    private void updateResults(RiskResult result) {
        double    risk  = result.getRiskScore();
        RiskLevel level = result.getRiskLevel();

        Color color;
        switch (level) {
            case LOW:    color = GREEN; break;
            case MEDIUM: color = AMBER; break;
            default:     color = RED;   break;
        }

        lblRiskPercent.setText(String.format("%.1f%%", risk));
        lblRiskPercent.setForeground(color);

        riskBar.setValue((int) Math.round(risk));
        riskBar.setForeground(color);

        lblRiskLevel.setText("Risk Level: " + level.getLabel());
        lblRiskLevel.setForeground(color);

        taMessage.setText(result.getMessage());
        taMessage.setForeground(color);
        taMessage.setBackground(tintColor(color, 0.07f));
        taMessage.setBorder(new CompoundBorder(
            new LineBorder(color, 1, true),
            new EmptyBorder(10, 14, 10, 14)));
    }

    // -----------------------------------------------------
    //  UI HELPERS
    // -----------------------------------------------------

    private JTextField addField(JPanel grid, String label) {
        grid.add(fieldLabel(label));
        JTextField tf = new JTextField();
        tf.setFont(new Font("SansSerif", Font.PLAIN, 13));
        grid.add(tf);
        return tf;
    }

    private JLabel fieldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.PLAIN, 13));
        l.setForeground(TEXT_DARK);
        return l;
    }

    private JButton styledButton(String text, Color bg) {
        JButton b = new JButton(text);
        b.setFont(new Font("SansSerif", Font.BOLD, 13));
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(8, 18, 8, 18));
        b.setOpaque(true);
        return b;
    }

    private TitledBorder panelBorder(String title) {
        TitledBorder b = BorderFactory.createTitledBorder(
            new LineBorder(BORDER_COLOR, 1, true), title);
        b.setTitleFont(new Font("SansSerif", Font.BOLD, 13));
        b.setTitleColor(ACCENT_BLUE);
        return b;
    }

    private JLabel sidebarSectionTitle(String text) {
        JLabel l = new JLabel(text.toUpperCase());
        l.setFont(new Font("SansSerif", Font.BOLD, 11));
        l.setForeground(ACCENT_BLUE);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private JTextArea sidebarText(String text) {
        JTextArea ta = new JTextArea(text);
        ta.setEditable(false);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.setFont(new Font("SansSerif", Font.PLAIN, 12));
        ta.setForeground(TEXT_DARK);
        ta.setBackground(BG_SIDEBAR);
        ta.setBorder(new EmptyBorder(4, 0, 4, 0));
        ta.setAlignmentX(Component.LEFT_ALIGNMENT);
        return ta;
    }

    private JSeparator sidebarDivider() {
        JSeparator sep = new JSeparator();
        sep.setForeground(BORDER_COLOR);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 10));
        return sep;
    }

    private JPanel riskLegendRow(String dot, String text, Color color) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        row.setBackground(BG_SIDEBAR);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));

        JLabel dotLbl = new JLabel(dot);
        dotLbl.setFont(new Font("SansSerif", Font.BOLD, 16));
        dotLbl.setForeground(color);

        JLabel txtLbl = new JLabel(text);
        txtLbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        txtLbl.setForeground(TEXT_DARK);

        row.add(dotLbl);
        row.add(txtLbl);
        return row;
    }

    // Returns a very light tint of the given colour for message box backgrounds
    private Color tintColor(Color base, float amount) {
        int r = (int) (base.getRed()   + (255 - base.getRed())   * (1 - amount));
        int g = (int) (base.getGreen() + (255 - base.getGreen()) * (1 - amount));
        int b = (int) (base.getBlue()  + (255 - base.getBlue())  * (1 - amount));
        return new Color(
            Math.min(r, 255),
            Math.min(g, 255),
            Math.min(b, 255));
    }

    // -----------------------------------------------------
    //  MAIN
    // -----------------------------------------------------
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}