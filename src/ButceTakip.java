import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ButceTakip extends JFrame {

    private JLabel lTotal, lDesc, lAmount;
    private RoundedTextField tDesc, tAmount;
    
    private RoundedButton bPlus, bMinus;
    private RoundedButton bUndo, bReset;

    private JTextArea tHistory;
    private double currentBal = 0.0;
    
    private Connection con = null;
    private Statement st = null;

    public ButceTakip() {
        setTitle("Student Budget Planner v6.0");
        setSize(420, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        startDB();

        JMenuBar bar = new JMenuBar();
        JMenu m = new JMenu("Themes");
        JMenuItem i1 = new JMenuItem("Retro Gold");
        JMenuItem i2 = new JMenuItem("Neon Berry");
        JMenuItem i3 = new JMenuItem("Natural Light");

        m.add(i1); m.add(i2); m.add(i3);
        bar.add(m);
        setJMenuBar(bar);

        lTotal = new JLabel("Wallet: 0.0 $");
        lTotal.setBounds(20, 20, 300, 40);
        lTotal.setFont(new Font("Arial", Font.BOLD, 24));
        add(lTotal);

        lDesc = new JLabel("Description:");
        lDesc.setBounds(20, 80, 100, 30);
        add(lDesc);

        tDesc = new RoundedTextField(15);
        tDesc.setBounds(130, 80, 250, 30);
        add(tDesc);

        lAmount = new JLabel("Amount ($):");
        lAmount.setBounds(20, 130, 100, 30);
        add(lAmount);

        tAmount = new RoundedTextField(15);
        tAmount.setBounds(130, 130, 250, 30);
        add(tAmount);

        bPlus = new RoundedButton("Add Income (+)");
        bPlus.setBounds(20, 180, 170, 40);
        add(bPlus);

        bMinus = new RoundedButton("Add Expense (-)");
        bMinus.setBounds(210, 180, 170, 40);
        add(bMinus);

        bUndo = new RoundedButton("Undo Last");
        bUndo.setBounds(20, 230, 170, 40);
        add(bUndo);

        bReset = new RoundedButton("Reset Wallet");
        bReset.setBounds(210, 230, 170, 40);
        add(bReset);

        tHistory = new JTextArea();
        tHistory.setEditable(false);
        JScrollPane scr = new JScrollPane(tHistory);
        scr.setBounds(20, 280, 360, 300);
        scr.setBorder(null);
        add(scr);

        i1.addActionListener(e -> updateTheme(Color.decode("#46244C"), Color.decode("#D49B54"), Color.decode("#C74B50"), Color.decode("#D49B54"), Color.WHITE));
        i2.addActionListener(e -> updateTheme(Color.decode("#180A0A"), Color.decode("#F10086"), Color.decode("#711A75"), Color.decode("#F582A7"), Color.WHITE));
        i3.addActionListener(e -> updateTheme(Color.decode("#EEEEEE"), Color.decode("#8B9A46"), Color.decode("#541212"), Color.BLACK, Color.BLACK));

        updateTheme(Color.decode("#190a14"), Color.decode("#ea5485"), Color.decode("#692d91"), Color.decode("#f3aebf"), Color.WHITE);

        bPlus.addActionListener(e -> addTrans(true));
        bMinus.addActionListener(e -> addTrans(false));
        
        bUndo.addActionListener(e -> undoLast());
        bReset.addActionListener(e -> resetAll());

        refreshList();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void startDB() {
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:proje.db");
            st = con.createStatement();
            
            String q = "CREATE TABLE IF NOT EXISTS records " +
                       "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                       " txt TEXT, " +
                       " val REAL, " +
                       " kind TEXT)";
            st.executeUpdate(q);
            
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Driver Error! Please add sqlite-jdbc jar.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "DB Connection Error: " + e.getMessage());
        }
    }

    private void addTrans(boolean isInc) {
        if (con == null) {
            JOptionPane.showMessageDialog(null, "Database not connected!");
            return;
        }

        try {
            String desc = tDesc.getText();
            double amt = Double.parseDouble(tAmount.getText());

            if (!desc.isEmpty()) {
                if (!isInc && amt > currentBal) {
                    JOptionPane.showMessageDialog(null, "Not enough money bro :(");
                    return;
                }

                String q = "INSERT INTO records(txt, val, kind) VALUES(?, ?, ?)";
                PreparedStatement ps = con.prepareStatement(q);
                ps.setString(1, desc);
                ps.setDouble(2, amt);
                ps.setString(3, isInc ? "Inc" : "Exp");
                ps.executeUpdate();

                refreshList(); 
                tDesc.setText("");
                tAmount.setText("");
            }
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(null, "Please enter a valid number!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }

    private void undoLast() {
        if(con == null) return;
        try {
            ResultSet rs = st.executeQuery("SELECT id FROM records ORDER BY id DESC LIMIT 1");
            if (rs.next()) {
                int lastID = rs.getInt("id");
                st.executeUpdate("DELETE FROM records WHERE id = " + lastID);
                refreshList();
            } else {
                JOptionPane.showMessageDialog(null, "Nothing to undo");
            }
        } catch (Exception e) {
        }
    }

    private void resetAll() {
        if(con == null) return;
        int opt = JOptionPane.showConfirmDialog(null, "Delete everything?", "Reset", JOptionPane.YES_NO_OPTION);
        if (opt == JOptionPane.YES_OPTION) {
            try {
                st.executeUpdate("DELETE FROM records");
                refreshList();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void refreshList() {
        try {
            if(st == null) return;
            
            tHistory.setText("");
            currentBal = 0.0;
            
            ResultSet rs = st.executeQuery("SELECT * FROM records");
            
            while (rs.next()) {
                String d = rs.getString("txt");
                double a = rs.getDouble("val");
                String k = rs.getString("kind");
                
                if (k.equals("Inc")) {
                    currentBal += a;
                    tHistory.append("+ " + d + ": " + a + " $\n");
                } else {
                    currentBal -= a;
                    tHistory.append("- " + d + ": " + a + " $\n");
                }
            }
            lTotal.setText("Wallet: " + currentBal + " $");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateTheme(Color bg, Color c1, Color c2, Color txt, Color lbl) {
        getContentPane().setBackground(bg);
        lTotal.setForeground(txt);
        lDesc.setForeground(lbl);
        lAmount.setForeground(lbl);
        
        bPlus.setBackground(c1);
        bMinus.setBackground(c2);
        bUndo.setBackground(c2);
        bReset.setBackground(c2);
        
        tDesc.setForeground(Color.BLACK);
        tDesc.setCaretColor(Color.BLACK);
        
        tAmount.setForeground(Color.BLACK);
        tAmount.setCaretColor(Color.BLACK);

        tHistory.setBackground(bg);
        tHistory.setForeground(lbl);
        repaint();
    }

    public static void main(String[] args) {
        new ButceTakip();
    }
}