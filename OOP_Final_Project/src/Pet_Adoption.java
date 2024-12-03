import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Pet_Adoption extends JDialog {
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
    private JComboBox<String> cmbPetList;
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
    private JComboBox<String> cmbSpecie;
    private JTable table2;
    private JComboBox<String> cmbAdoptedList;
    private JTextArea txtReceipt;
    private JButton btnPay;
    private JButton btnPrintReceipt;
    private JTextField txtAdoptionFee;
    private JTextField txtPayment;

    public Pet_Adoption() {
        // Load data when the application starts
        loadTableData();

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

        // Save data when the window closes
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                saveTableData();
                System.exit(0);
            }
        });

        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void saveTableData() {
        System.out.println("Saving data to data.csv...");
        String filePath = "data.csv";  // Relative path
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            DefaultTableModel model = (DefaultTableModel) table1.getModel();

            // Write column headers
            for (int col = 0; col < model.getColumnCount(); col++) {
                writer.print(model.getColumnName(col) + (col == model.getColumnCount() - 1 ? "" : ","));
            }
            writer.println();

            // Write data rows
            for (int row = 0; row < model.getRowCount(); row++) {
                for (int col = 0; col < model.getColumnCount(); col++) {
                    writer.print(model.getValueAt(row, col) + (col == model.getColumnCount() - 1 ? "" : ","));
                }
                writer.println();
            }
            System.out.println("Data saved to: " + filePath);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void loadTableData() {
        System.out.println("Loading data from data.csv...");
        String filePath = "C:/Users/Selwyn/Documents/yow/OOP_Final_Project_Version_2/data.csv";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            DefaultTableModel model = (DefaultTableModel) table1.getModel();

            // Clear existing data
            model.setRowCount(0);

            // Read and parse the CSV file
            String line = reader.readLine(); // Skip the header row
            while ((line = reader.readLine()) != null) {
                String[] rowData = line.split(",");
                model.addRow(rowData);
            }
            System.out.println("Data loaded from: " + filePath);
        } catch (FileNotFoundException ex) {
            System.out.println("No saved data found.");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
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

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateTime = dateFormat.format(new Date());

        Object[] row = {petManagement.getCount(), pname.toUpperCase(), breed, color, specie, gender, healthStatus, dateTime};
        DefaultTableModel petTableModel = (DefaultTableModel) table1.getModel();
        petTableModel.addRow(row);

        currentPet = petManagement.addPet(pname, breed, color, specie, gender, healthStatus);

        // Add pet name to comboBox
        cmbPetList.insertItemAt(pname.toUpperCase(), xrow);
        xrow++;

        // Save pet data to CSV
        savePetDataToCSV(row);

        clearPetInputFields();
    }
    private String getResidenceType() {
        if (chkApartment.isSelected()) {
            return "Apartment";
        } else if (chkCondo.isSelected()) {
            return "Condo";
        } else if (chkHouse.isSelected()) {
            return "House";
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
        chkApartment.setSelected(false);
        chkCondo.setSelected(false);
        chkHouse.setSelected(false);
        chkRent.setSelected(false);
    }


    private void onSaveAdoption() {
        String adoptedPet = cmbPetList.getSelectedItem().toString();
        String lastName = txtLastName.getText().trim();
        String firstName = txtFirstName.getText().trim();
        String address = txtAddress.getText().trim();
        String phone = txtPhone.getText().trim();
        String email = txtEmail.getText().trim();
        String residenceType = getResidenceType();
        String ownedPets = txtOwnedPets.getText().trim();

        if (lastName.isEmpty() || firstName.isEmpty() || address.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all the required fields.", "Missing Information", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DefaultTableModel adoptionTableModel = (DefaultTableModel) table2.getModel();
        Object[] rowData = {adoptedPet, lastName, firstName, address, phone, email, residenceType, ownedPets};
        adoptionTableModel.addRow(rowData);

        // Save adoption data to CSV
        saveAdoptionDataToCSV(rowData);

        clearInputFields();
    }

    private void saveAdoptionDataToCSV(Object[] rowData) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("adoption.csv", true))) {
            for (Object data : rowData) {
                writer.print(data + ",");
            }
            writer.println();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void savePetDataToCSV(Object[] row) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("pets.csv", true))) {
            for (Object data : row) {
                writer.print(data + ",");
            }
            writer.println();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void onSave() {
        // Implementation for saving adoption information
        JOptionPane.showMessageDialog(this, "Save function triggered.");
        saveTableData();
    }

    private void onCancel() {
        dispose();
    }

    private void onPAY() {
        try {
            double adoptionFee = Double.parseDouble(txtAdoptionFee.getText());
            double payment = Double.parseDouble(txtPayment.getText());

            if (payment >= adoptionFee) {
                double change = payment - adoptionFee;
                JOptionPane.showMessageDialog(this, "Payment successful. Change: " + formatCurrency(change), "Payment Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Insufficient payment. Please enter a valid amount.", "Payment Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter valid numeric values.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onPrintReceipt() {
        String adoptedPet = cmbAdoptedList.getSelectedItem().toString();
        String adoptionFee = txtAdoptionFee.getText().trim();
        String payment = txtPayment.getText().trim();

        if (adoptedPet.isEmpty() || adoptionFee.isEmpty() || payment.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all the required fields.", "Missing Information", JOptionPane.ERROR_MESSAGE);
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateTime = dateFormat.format(new Date());

        String receipt = "---------------------------------------------------------\n\n" +
                "Tails of Hope\n" +
                "Connecting Hearts, Saving Lives\n" +
                "University Site, Lucena City\n\n" +
                "Date and Time: " + dateTime + "\n\n" +
                "Thank you for choosing Tails of Hope!\n" +
                "Adoption Receipt\n\n" +
                "Adopted Pet Name: " + adoptedPet + "\n" +
                "Adoption Fee: " + adoptionFee + "\n" +
                "Payment: " + payment + "\n" +
                "\n---------------------------------------------------------\n" +
                "Thank you for adopting from Tails of Hope!\n";

        txtReceipt.setText(receipt);

        // Save receipt data to CSV
        saveReceiptDataToCSV(new String[]{adoptedPet, adoptionFee, payment, dateTime});
    }

    private void saveReceiptDataToCSV(String[] rowData) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("fees.csv", true))) {
            for (String data : rowData) {
                writer.print(data + ",");
            }
            writer.println();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void addFemale() {
        gender = "F";
        chkMale.setSelected(false);
    }

    private void addMale() {
        gender = "M";
        chkFemale.setSelected(false);
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

    private String formatCurrency(double amount) {
        DecimalFormat currencyFormat = new DecimalFormat("#,##0.00");
        return currencyFormat.format(amount);
    }

    // Placeholder for displaying selected pet's information.
    private void displayPetInformation() {
        int selectedIndex = cmbPetList.getSelectedIndex();

        // Check if a valid pet is selected
        if (selectedIndex >= 0) {
            DefaultTableModel model = (DefaultTableModel) table1.getModel();
            if (selectedIndex < model.getRowCount()) {
                // Retrieve the selected pet's information from the table
                String petName = (String) model.getValueAt(selectedIndex, 1);
                String breed = (String) model.getValueAt(selectedIndex, 2);
                String color = (String) model.getValueAt(selectedIndex, 3);
                String species = (String) model.getValueAt(selectedIndex, 4);
                String gender = (String) model.getValueAt(selectedIndex, 5);
                String healthStatus = (String) model.getValueAt(selectedIndex, 6);

                // Display the pet information in the text area
                String petInformation = "Pet Name: " + petName + "\n"
                        + "Breed: " + breed + "\n"
                        + "Color: " + color + "\n"
                        + "Species: " + species + "\n"
                        + "Gender: " + gender + "\n"
                        + "Health Status: " + healthStatus;

                txtPetInformation.setText(petInformation);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a valid pet from the list.", "Invalid Selection", JOptionPane.ERROR_MESSAGE);
        }
    }


    public JPanel getMainPanel() {
        return contentPane; // Return the main content panel
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Initialize the Login Form
            JFrame loginFrame = new JFrame("Login");
            LoginForm loginForm = new LoginForm();

            // Add listener for the Login button using the getter
            loginForm.getLoginButton().addActionListener(e -> {
                String username = loginForm.getUsernameField1().getText();
                String password = new String(loginForm.getPasswordField().getPassword());

                // Simple authentication logic
                if (username.equals("admin") && password.equals("admin123")) {
                    JOptionPane.showMessageDialog(null, "Login Successful!");

                    // Close the login frame
                    loginFrame.dispose();

                    // Launch the Pet Adoption Interface
                    Pet_Adoption dialog = new Pet_Adoption();
                    dialog.setTitle("Tails of Hope - Pet Adoption Management System");
                    dialog.pack();
                    dialog.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Username or Password!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            // Display the login form
            loginFrame.setContentPane(loginForm.getLoginPanel());
            loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            loginFrame.pack();
            loginFrame.setVisible(true);
        });
    }
}
