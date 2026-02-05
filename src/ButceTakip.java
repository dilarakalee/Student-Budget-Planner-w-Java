import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ButceTakip extends JFrame {

    Color color1 = Color.decode("#190a14"); 
    Color color2 = Color.decode("#ea5485");
    Color color3 = Color.decode("#692d91");
    Color color4 = Color.decode("#f3aebf");
    
    private JLabel totalLabel;
    private RoundedTextField descField;
    private RoundedTextField amountField;
    private JTextArea historyArea;
    private double balance = 0.0;
    private ArrayList<String> dataList = new ArrayList<>();

    public ButceTakip() {
        setTitle("Student Budget Planner v2.1");
        setSize(420, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(color1);

        totalLabel = new JLabel("Wallet: 0.0 $");
        totalLabel.setBounds(20, 20, 300, 40);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 24));
        totalLabel.setForeground(color4);
        add(totalLabel);

        JLabel label1 = new JLabel("Description:");
        label1.setBounds(20, 80, 100, 30);
        label1.setForeground(Color.WHITE);
        add(label1);

        descField = new RoundedTextField(15);
        descField.setBounds(130, 80, 250, 30);
        add(descField);

        JLabel label2 = new JLabel("Amount:");
        label2.setBounds(20, 130, 100, 30);
        label2.setForeground(Color.WHITE);
        add(label2);

        amountField = new RoundedTextField(15);
        amountField.setBounds(130, 130, 250, 30);
        add(amountField);

        RoundedButton incBtn = new RoundedButton("Add Income +");
        incBtn.setBounds(20, 180, 170, 40);
        incBtn.setBackground(color2);
        incBtn.setForeground(Color.WHITE);
        add(incBtn);

        RoundedButton expBtn = new RoundedButton("Add Expense -");
        expBtn.setBounds(210, 180, 170, 40);
        expBtn.setBackground(color3);
        expBtn.setForeground(Color.WHITE);
        add(expBtn);

        historyArea = new JTextArea();
        historyArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(historyArea);
        scroll.setBounds(20, 240, 360, 300);
        add(scroll);

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