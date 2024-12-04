import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginForm {

    private JPanel loginPanel;
    private JTextField usernameField1; // Username field
    private JPasswordField passwordField; // Password field
    private JButton loginButton; // Login button

    public LoginForm() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField1.getText();
                String password = new String(passwordField.getPassword());

                if (username.equals("admin") && password.equals("admin123")) {
                    JOptionPane.showMessageDialog(null, "Login Successful!");

                    JFrame mainFrame = new JFrame("Pet Adoption System");
                    Pet_Adoption petAdoptionDialog = new Pet_Adoption();
                    mainFrame.setContentPane(petAdoptionDialog.getMainPanel());
                    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    mainFrame.pack();
                    mainFrame.setVisible(true);

                    SwingUtilities.getWindowAncestor(loginPanel).dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Username or Password!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public JPanel getLoginPanel() {
        return loginPanel;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Login");
        frame.setContentPane(new LoginForm().getLoginPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
