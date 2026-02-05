import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ButceTakip extends JFrame {

    private JLabel totalLabel;
    private JLabel label1;
    private JLabel label2;
    private RoundedTextField descField;
    private RoundedTextField amountField;
    private RoundedButton incBtn;
    private RoundedButton expBtn;
    private JTextArea historyArea;
    private double balance = 0.0;
    private ArrayList<String> dataList = new ArrayList<>();

    public ButceTakip() {
        setTitle("Student Budget Planner v3.0");
        setSize(420, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JMenuBar mb = new JMenuBar();
        JMenu m = new JMenu("Themes");
        JMenuItem t1 = new JMenuItem("Retro Gold");
        JMenuItem t2 = new JMenuItem("Neon Berry");
        JMenuItem t3 = new JMenuItem("Natural Light");

        m.add(t1);
        m.add(t2);
        m.add(t3);
        mb.add(m);
        setJMenuBar(mb);

        totalLabel = new JLabel("Wallet: 0.0 $");
        totalLabel.setBounds(20, 20, 300, 40);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(totalLabel);

        label1 = new JLabel("Description:");
        label1.setBounds(20, 80, 100, 30);
        add(label1);

        descField = new RoundedTextField(15);
        descField.setBounds(130, 80, 250, 30);
        add(descField);

        label2 = new JLabel("Amount:");
        label2.setBounds(20, 130, 100, 30);
        add(label2);

        amountField = new RoundedTextField(15);
        amountField.setBounds(130, 130, 250, 30);
        add(amountField);

        incBtn = new RoundedButton("Add Income +");
        incBtn.setBounds(20, 180, 170, 40);
        add(incBtn);

        expBtn = new RoundedButton("Add Expense -");
        expBtn.setBounds(210, 180, 170, 40);
        add(expBtn);

        historyArea = new JTextArea();
        historyArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(historyArea);
        scroll.setBounds(20, 240, 360, 300);
        scroll.setBorder(null);
        add(scroll);

        t1.addActionListener(e -> changeColor(
            Color.decode("#46244C"), 
            Color.decode("#D49B54"), 
            Color.decode("#C74B50"), 
            Color.decode("#D49B54"), 
            Color.WHITE
        ));

        t2.addActionListener(e -> changeColor(
            Color.decode("#180A0A"), 
            Color.decode("#F10086"), 
            Color.decode("#711A75"), 
            Color.decode("#F582A7"), 
            Color.WHITE
        ));

        t3.addActionListener(e -> changeColor(
            Color.decode("#EEEEEE"), 
            Color.decode("#8B9A46"), 
            Color.decode("#541212"), 
            Color.BLACK, 
            Color.BLACK
        ));

        changeColor(Color.decode("#190a14"), Color.decode("#ea5485"), Color.decode("#692d91"), Color.decode("#f3aebf"), Color.WHITE);

        incBtn.addActionListener(e -> {
            try {
                String text = descField.getText();
                double val = Double.parseDouble(amountField.getText());
                
                if(!text.isEmpty()) {
                    balance += val;
                    totalLabel.setText("Wallet: " + balance + " $");
                    dataList.add("+ " + text + ": " + val + " $");
                    updateList();
                    clearFields();
                }
            } catch(Exception ex) {
                JOptionPane.showMessageDialog(null, "Check inputs");
            }
        });

        expBtn.addActionListener(e -> {
            try {
                String text = descField.getText();
                double val = Double.parseDouble(amountField.getText());
                
                if(!text.isEmpty()) {
                    if(val <= balance) {
                        balance -= val;
                        totalLabel.setText("Wallet: " + balance + " $");
                        dataList.add("- " + text + ": " + val + " $");
                        updateList();
                        clearFields();
                    } else {
                        JOptionPane.showMessageDialog(null, "Not enough money");
                    }
                }
            } catch(Exception ex) {
                JOptionPane.showMessageDialog(null, "Check inputs");
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void changeColor(Color bg, Color b1, Color b2, Color txt, Color lbl) {
        getContentPane().setBackground(bg);
        totalLabel.setForeground(txt);
        label1.setForeground(lbl);
        label2.setForeground(lbl);
        incBtn.setBackground(b1);
        expBtn.setBackground(b2);
        descField.setForeground(lbl);
        descField.setCaretColor(lbl);
        amountField.setForeground(lbl);
        amountField.setCaretColor(lbl);
        historyArea.setBackground(bg);
        historyArea.setForeground(lbl);
        repaint();
    }

    private void updateList() {
        historyArea.setText("");
        for(String s : dataList) {
            historyArea.append(s + "\n");
        }
    }

    private void clearFields() {
        descField.setText("");
        amountField.setText("");
    }

    public static void main(String[] args) {
        new ButceTakip();
    }
}