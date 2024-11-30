import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.DecimalFormat;

interface PaymentProcessor {
    boolean processPayment(double adoptionFee, double payment);
}

public class Pet_Adoption extends JDialog implements PaymentProcessor {
    private int count = 0, xrow = 0;
    private PetManagement petManagement = new PetManagement();
    private String gender = " ";
    private Pet currentPet;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTabbedPane tabbedPane1;
    private JTextField txtPetName;
    private JTextField txtBreed;
    private JCheckBox chkMale;
    private JCheckBox chkFemale;
    private JTextField txtColor;
    private JTextField txtHealthStatus;
    private JComboBox cmbPetList;
    private JTextArea txtPetInformation;
    private JButton btnAddPet;
    private JButton btnCancel;
    private JTable table1;
    private JTextField txtLastName;
    private JTextField txtFirstName;
    private JTextField txtAddress;
    private JTextField txtPhone;
    private JTextField txtEmail;
    private JCheckBox chkApartment;
    private JCheckBox chkHouse;
    private JCheckBox chkRent;

    private JTextField txtOwnedPets;
    private JButton btnSave;
    private JCheckBox chkCondo;
    private JComboBox cmbSpecie;
    private JTable table2;
    private JComboBox cmbAdoptedList;

    private JTextArea txtReceipt;
    private JButton btnPay;
    private JButton btnPrintReceipt;
    private JTextField txtAdoptionFee;

    private JTextField txtPayment;

    public Pet_Adoption() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(btnSave);

        table1.setModel(new DefaultTableModel(
                null,
                new String[]{"Pet ID", "Pet Name", "Breed", "Color", "Species", "Gender", "Health Status", "Date and Time"}
        ));

        table2.setModel(new DefaultTableModel(
                null,
                new String[]{"Adopted Pet", "Last Name", "First Name", "Address", "Phone Number", "Email Address", "Residence Type", "Owned Pets"}
        ));

        btnAddPet.addActionListener(e -> onAddPet());

        btnSave.addActionListener(e -> onSave());
        chkFemale.addActionListener(e -> addFemale());
        chkMale.addActionListener(e -> addMale());

        buttonCancel.addActionListener(e -> onCancel());

        cmbPetList.addActionListener(e -> displayPetInformation());

        btnPay.addActionListener(e -> onPAY());

        btnPrintReceipt.addActionListener(e -> onPrintReceipt());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void addFemale() {
        gender = "F";
        chkMale.setSelected(false);
    }

    private void addMale() {
        gender = "M";
        chkFemale.setSelected(false);
    }

    private void onAddPet() {
        String pname = txtPetName.getText().trim();
        String breed = txtBreed.getText().trim();
        String color = txtColor.getText().trim();
        String specie = cmbSpecie.getSelectedItem().toString();
        String healthStatus = txtHealthStatus.getText().trim();

        if (pname.isEmpty() || breed.isEmpty() || color.isEmpty() || healthStatus.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all the required fields.", "Missing Information", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DefaultTableModel petTableModel = (DefaultTableModel) table1.getModel();
        String name = txtPetName.getText();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateTime = dateFormat.format(new Date());

        String petName = name.toUpperCase();
        String petBreed = txtBreed.getText();
        String petColor = txtColor.getText();
        String petSpecie = cmbSpecie.getSelectedItem().toString();
        String petGender = gender;
        String petHealth = txtHealthStatus.getText();
        String petDateTime = dateTime;

        Object[] row = {petManagement.getCount(), petName, petBreed, petColor, petSpecie, petGender, petHealth, petDateTime};

        petTableModel.addRow(row);

        cmbPetList.insertItemAt(petName, xrow);
        xrow++;

        currentPet = petManagement.addPet(name, txtBreed.getText(), txtColor.getText(), cmbSpecie.getSelectedItem().toString(), gender, txtHealthStatus.getText());

        clearPetInputFields();
    }

    private void onSave() {
        String alastName = txtLastName.getText().trim();
        String afirstName = txtFirstName.getText().trim();
        String aaddress = txtAddress.getText().trim();
        String aphone = txtPhone.getText().trim();
        String aemail = txtEmail.getText().trim();

        if (alastName.isEmpty() || afirstName.isEmpty() || aaddress.isEmpty() || aphone.isEmpty() || aemail.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all the required fields.", "Missing Information", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String adoptedPet = cmbPetList.getSelectedItem().toString();
        String lastName = txtLastName.getText();
        String firstName = txtFirstName.getText();
        String address = txtAddress.getText();
        String phone = txtPhone.getText();
        String email = txtEmail.getText();

        String residenceType = getResidenceType();

        String ownedPets = txtOwnedPets.getText();

        String[] rowData = {adoptedPet, lastName, firstName, address, phone, email, residenceType, ownedPets};

        DefaultTableModel tableModel = (DefaultTableModel) table2.getModel();
        tableModel.addRow(rowData);

        cmbAdoptedList.addItem(adoptedPet);

        clearInputFields();
    }

    private String getResidenceType() {
        if (chkApartment.isSelected()) {
            return "Apartment";
        } else if (chkHouse.isSelected()) {
            return "House";
        } else if (chkCondo.isSelected()) {
            return "Condo";
        } else if (chkRent.isSelected()) {
            return "Rent";
        }
        return "";
    }

    private void clearInputFields() {
        txtLastName.setText("");
        txtFirstName.setText("");
        txtAddress.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        txtOwnedPets.setText("");
    }

    private void clearPetInputFields() {
        txtPetName.setText("");
        txtBreed.setText("");
        txtColor.setText("");
        cmbSpecie.setSelectedIndex(0);
        chkMale.setSelected(false);
        chkFemale.setSelected(false);
        txtHealthStatus.setText("");
    }

    private void displayPetInformation() {
        int selectedPetIndex = cmbPetList.getSelectedIndex();

        if (selectedPetIndex >= 0) {
            DefaultTableModel tableModel = (DefaultTableModel) table1.getModel();
            String petInformation =
                    "Name: " + tableModel.getValueAt(selectedPetIndex, 1) + "\n" +
                            "Breed: " + tableModel.getValueAt(selectedPetIndex, 2) + "\n" +
                            "Color: " + tableModel.getValueAt(selectedPetIndex, 3) + "\n" +
                            "Species: " + tableModel.getValueAt(selectedPetIndex, 4) + "\n" +
                            "Gender: " + tableModel.getValueAt(selectedPetIndex, 5) + "\n" +
                            "Health Status: " + tableModel.getValueAt(selectedPetIndex, 6) + "\n";

            txtPetInformation.setText(petInformation);
        }
    }

    /* Function for PAY Button */
    private void onPAY() {
        try {
            double adoptionFee = Double.parseDouble(txtAdoptionFee.getText());
            double payment = Double.parseDouble(txtPayment.getText());

            if (processPayment(adoptionFee, payment)) {
                // Additional logic if payment is successful
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter valid numeric values.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /* Function for PRINT RECEIPT Button */
    private void onPrintReceipt() {
        String adoptedPet = cmbAdoptedList.getSelectedItem().toString();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateTime = dateFormat.format(new Date());

        String receipt = "---------------------------------------------------------\n\n";
        receipt += "Tails of Hope\n";
        receipt += "Connecting Hearts, Saving Lives\n";
        receipt += "University Site, Lucena City\n\n";
        receipt += "Date and Time: " + dateTime + "\n\n";
        receipt += "Thank you for choosing Tails of Hope!\n";
        receipt += "Adoption Receipt\n\n";
        receipt += "Adopted Pet Name: " + adoptedPet + "\n";
        receipt += "Adoption Fee: " + txtAdoptionFee.getText() + "\n";
        receipt += "Payment: " + txtPayment.getText() + "\n";
        receipt += "\n---------------------------------------------------------\n";
        receipt += "Thank you for adopting from Tails of Hope!\n";

        txtReceipt.setText(receipt);
    }

    @Override
    public boolean processPayment(double adoptionFee, double payment) {
        try {
            if (payment >= adoptionFee) {
                double change = payment - adoptionFee;
                JOptionPane.showMessageDialog(this, "Payment successful. Change: " + formatCurrency(change), "Payment Success", JOptionPane.INFORMATION_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(this, "Insufficient payment. Please enter a valid amount.", "Payment Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter valid numeric values.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private String formatCurrency(double amount) {
        DecimalFormat currencyFormat = new DecimalFormat("#,##0.00");
        return currencyFormat.format(amount);
    }

    private void onCancel() {
        dispose();
    }

    public static void main(String[] args) {
        Pet_Adoption dialog = new Pet_Adoption();
        dialog.setTitle("Tails of Hope - Pet Adoption Management System");
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
