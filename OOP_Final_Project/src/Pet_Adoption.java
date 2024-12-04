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
        getRootPane().setDefaultButton(btnAddPet);

        // Initialize table model for displaying pets
        table1.setModel(new DefaultTableModel(
                null,
                new String[]{"Pet ID", "Pet Name", "Breed", "Color", "Species", "Gender", "Health Status", "Date and Time"}
        ));
        table2.setModel(new DefaultTableModel(
                null,
                new String[]{"Adopted Pet", "Last Name", "First Name", "Address", "Phone Number", "Email Address", "Residence Type", "Owned Pets"}
        ));

        loadTableData(); // Load saved pets when starting the dialog

        // Button action listeners
        btnAddPet.addActionListener(e -> onAddPet());
        chkFemale.addActionListener(e -> selectFemale());
        chkMale.addActionListener(e -> selectMale());
        buttonCancel.addActionListener(e -> onCancel());
        btnSave.addActionListener(e -> onSave());
        cmbPetList.repaint(); // Force repaint to refresh the UI


        cmbPetList.addActionListener(e -> onAdoptedPetSelected());
        cmbAdoptedList.addActionListener(e -> onAdoptedPetSelected());
        btnPay.addActionListener(e -> onPay());
        btnPrintReceipt.addActionListener(e -> onPrintReceipt());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                saveTableData(); // Save all pet data to CSV on close
                System.exit(0);
            }
        });

        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onAddPet() {
        String petName = txtPetName.getText().trim();
        String breed = txtBreed.getText().trim();
        String color = txtColor.getText().trim();
        String specie = cmbSpecie.getSelectedItem().toString();
        String healthStatus = txtHealthStatus.getText().trim();


        // Validate input fields
        if (petName.isEmpty() || breed.isEmpty() || color.isEmpty() || healthStatus.isEmpty() || gender.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all the required fields.", "Missing Information", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Get current date and time
        String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        // Update the table model
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        Object[] row = {++count, petName, breed, color, specie, gender, healthStatus, dateTime};
        model.addRow(row);

        // Add to the pet list combo box (cmbPetList)
        // Check if the pet name is already in the combo box before adding it
        if (!isPetInComboBox(cmbPetList, petName)) {
            cmbPetList.addItem(petName); // Add pet name to cmbPetList
        }

        // Save pet data to CSV
        savePetDataToCSV(row);

        // Clear the input fields
        clearPetInputFields();
    }

    // Helper function to check if the pet is already in the combo box
    private boolean isPetInComboBox(JComboBox<String> comboBox, String petName) {
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            if (comboBox.getItemAt(i).equals(petName)) {
                return true; // Pet already in combo box
            }
        }
        return false; // Pet not in combo box
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

        // Check if a pet is selected in cmbPetList
        Object selectedPet = cmbPetList.getSelectedItem();
        if (selectedPet == null) {
            JOptionPane.showMessageDialog(this, "Please select a pet to adopt.", "No Pet Selected", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String adoptedPet = selectedPet.toString();
        DefaultTableModel model = (DefaultTableModel) table1.getModel();

        // Find the selected pet in table1 and remove it
        for (int row = 0; row < model.getRowCount(); row++) {
            String petName = model.getValueAt(row, 1).toString(); // Pet name is in the second column (index 1)
            if (petName.equals(adoptedPet)) {
                model.removeRow(row); // Remove the pet from table1
                break;
            }
        }

        // Add adopted pet to table2 (adopted pets list)
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

        // Remove the adopted pet from cmbPetList combo box
        cmbPetList.removeItem(adoptedPet); // Remove the pet from the combo box

        // Add to the adopted list combo box
        cmbAdoptedList.addItem(adoptedPet);

        // Save updated data back to CSV
        saveTableData();  // Ensure this method saves the latest data to CSV

        // Clear input fields after saving the adoption
        clearInputFields();
    }




    private void removePetFromTable() {
        // Get the selected row index in the JTable
        int selectedRow = table1.getSelectedRow();
        if (selectedRow != -1) {
            // Get the pet name from the selected row (assuming the pet name is in the second column)
            String petNameToRemove = (String) table1.getValueAt(selectedRow, 1);

            // Remove the row from the JTable model
            DefaultTableModel model = (DefaultTableModel) table1.getModel();
            model.removeRow(selectedRow);

            // Update the CSV file
            updateCSVFile(petNameToRemove);
        }
    }

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {
        // Save changes to table2
        saveChangesToTable2();

        // Remove the selected pet from table2 and update CSV
        removePetFromTableAndCSV();
    }

    private void saveChangesToTable2() {
        // Save logic for table2 if any (this depends on how you're editing table2)
        // For example, you might be updating a cell value or adding new data to the table
        // This would happen before removing any rows.
        // Example: Update table2 cell
        int row = table2.getSelectedRow();
        if (row != -1) {
            table2.setValueAt("Updated Value", row, 1); // Update cell content as needed
        }
    }

    private void removePetFromTableAndCSV() {
        // Get the selected row index in table2
        int selectedRow = table2.getSelectedRow();
        if (selectedRow != -1) {
            // Get the pet name from the selected row (assuming it's in the second column)
            String petNameToRemove = (String) table2.getValueAt(selectedRow, 1);

            // Remove the row from table2
            DefaultTableModel model = (DefaultTableModel) table2.getModel();
            model.removeRow(selectedRow);

            // Update the CSV file
            updateCSVFile(petNameToRemove);
        }
    }

    private void updateCSVFile(String petNameToRemove) {
        String filePath = "data.csv";
        File tempFile = new File("temp.csv"); // Create a temporary file to store updated content

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath));
             PrintWriter writer = new PrintWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] rowData = line.split(",");
                if (rowData.length > 1) {
                    String petName = rowData[1]; // Assuming the pet name is in the second column
                    if (!petName.equals(petNameToRemove)) {
                        writer.println(line); // Write lines that do not match the pet name to remove
                    }
                }
            }

            // Rename the temp file to the original file after processing
            if (tempFile.renameTo(new File(filePath))) {
                System.out.println("CSV file updated successfully.");
            } else {
                System.out.println("Error renaming temp file.");
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
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

    private void savePetDataToCSV(Object[] row) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("data.csv", true))) {
            // Write each row with proper commas between values
            for (Object data : row) {
                writer.print(data + ",");
            }
            writer.println();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void clearInputFields() {
        txtLastName.setText("");
        txtFirstName.setText("");
        txtAddress.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        txtOwnedPets.setText("");
    }


    private void loadTableData() {
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        model.setRowCount(0); // Clear existing data
        cmbPetList.removeAllItems(); // Clear existing items in combo box

        // Example data loading from the table
        // If you're loading data from a CSV, that part remains as is

        String filePath = "data.csv";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            // Skip header if present
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] rowData = line.split(",");
                model.addRow(rowData);
                cmbPetList.addItem(rowData[1]); // Add pet names to combo box
            }
        } catch (FileNotFoundException ex) {
            System.out.println("No saved data found.");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }




    private void saveTableData() {
        String filePath = "data.csv";
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            DefaultTableModel model = (DefaultTableModel) table1.getModel();

            // Write column headers
            writer.println("Pet ID,Pet Name,Breed,Color,Species,Gender,Health Status,Date and Time");

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

    private void onPay() {
        try {
            String adoptedPet = cmbAdoptedList.getSelectedItem().toString();
            double adoptionFee = Double.parseDouble(txtAdoptionFee.getText());
            double payment = Double.parseDouble(txtPayment.getText());

            if (payment >= adoptionFee) {
                double change = payment - adoptionFee;
                JOptionPane.showMessageDialog(this, "Payment successful for " + adoptedPet + ". Change: " + formatCurrency(change), "Payment Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Insufficient payment. Please enter a valid amount.", "Payment Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter valid numeric values.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NullPointerException ex) {
            JOptionPane.showMessageDialog(this, "Please select a pet for adoption.", "Error", JOptionPane.ERROR_MESSAGE);
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

    private void onAdoptedPetSelected() {
        String selectedPetName = cmbPetList.getSelectedItem().toString();
        DefaultTableModel model = (DefaultTableModel) table1.getModel();

        // Search for the selected pet name in the table
        for (int row = 0; row < model.getRowCount(); row++) {
            String petName = model.getValueAt(row, 1).toString(); // Pet name is in the second column (index 1)

            if (petName.equals(selectedPetName)) {
                // Found the matching pet, now display all details in txtPetInformation
                StringBuilder petInfo = new StringBuilder();
                petInfo.append("Pet ID: ").append(model.getValueAt(row, 0)).append("\n"); // Pet ID
                petInfo.append("Pet Name: ").append(model.getValueAt(row, 1)).append("\n"); // Pet Name
                petInfo.append("Breed: ").append(model.getValueAt(row, 2)).append("\n"); // Breed
                petInfo.append("Color: ").append(model.getValueAt(row, 3)).append("\n"); // Color
                petInfo.append("Species: ").append(model.getValueAt(row, 4)).append("\n"); // Species
                petInfo.append("Gender: ").append(model.getValueAt(row, 5)).append("\n"); // Gender
                petInfo.append("Health Status: ").append(model.getValueAt(row, 6)).append("\n"); // Health Status
                petInfo.append("Date and Time: ").append(model.getValueAt(row, 7)).append("\n"); // Date and Time

                // Set the text to the txtPetInformation JTextArea
                txtPetInformation.setText(petInfo.toString());
                break;
            }
        }
    }



    private String formatCurrency(double amount) {
        DecimalFormat currencyFormat = new DecimalFormat("#,##0.00");
        return currencyFormat.format(amount);
    }

    private void selectFemale() {
        gender = "Female";
        chkMale.setSelected(false);
    }

    private void selectMale() {
        gender = "Male";
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

    private void onCancel() {
        dispose();
    }

    public JPanel getMainPanel() {
        return contentPane;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame loginFrame = new JFrame("Login");
            LoginForm loginForm = new LoginForm();
            loginFrame.setContentPane(loginForm.getLoginPanel());
            loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            loginFrame.pack();
            loginFrame.setVisible(true);
        });
    }
}
