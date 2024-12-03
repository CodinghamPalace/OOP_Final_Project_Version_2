import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Dimension;

public class LoginForm {

    private JPanel loginPanel;
    private JTextField usernameField1; // Username field
    private JPasswordField passwordField; // Password field
    private JButton loginButton; // Login button

    public LoginForm() {


            // Set preferred size for the components
            usernameField1.setPreferredSize(new Dimension(5, 40)); // Username field size
            passwordField.setPreferredSize(new Dimension(5, 40)); // Password field size
            loginButton.setPreferredSize(new Dimension(5, 40));   // Button size

            // Other initialization logic if needed


        // Add action listener for the login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Retrieve username and password inputs
                String username = usernameField1.getText();
                String password = new String(passwordField.getPassword());

                // Hardcoded username and password
                String hardcodedUsername = "admin";
                String hardcodedPassword = "admin123";

                // Check credentials
                if (username.equals(hardcodedUsername) && password.equals(hardcodedPassword)) {
                    JOptionPane.showMessageDialog(null, "Login Successful!");
                    // Navigate to your main application (Pet_Adoption)
                    JFrame mainFrame = new JFrame("Pet Adoption System");
                    mainFrame.setContentPane(new Pet_Adoption().getMainPanel());
                    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    mainFrame.pack();
                    mainFrame.setVisible(true);

                    // Close the login window
                    SwingUtilities.getWindowAncestor(loginPanel).dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Username or Password!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    // Getter methods for encapsulation
    public JPanel getLoginPanel() {
        return loginPanel;
    }

    public JTextField getUsernameField1() {
        return usernameField1;
    }

    public JPasswordField getPasswordField() {
        return passwordField;
    }

    public JButton getLoginButton() {
        return loginButton;
    }

    public static void main(String[] args) {
        // Show login form
        JFrame frame = new JFrame("Login");
        frame.setContentPane(new LoginForm().getLoginPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}


