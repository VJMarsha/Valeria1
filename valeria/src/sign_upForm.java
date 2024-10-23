import User.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

class SignupForm  extends JDialog{
    private JTextField tfUsername;
    private JPasswordField pfPassword;
    private JTextField tfRegnumber;
    private JButton btnsignup;
    private JButton btnLogin;
    private JPanel signupPanel;

    public SignupForm(JFrame parent){
        super(parent);
        setTitle("Signup");
        setContentPane(signupPanel);
        setMinimumSize(new Dimension(450,474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        btnsignup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String reg_number = tfRegnumber.getText();
                String password = String.valueOf(pfPassword.getPassword());

                user = getAuthenticationUser(reg_number,password);

                if (user!=null){
                    dispose();
                }
                else {
                    JOptionPane.showMessageDialog(SignupForm.this,
                            "Password or Regnumber Invalid",
                            "Try again",
                            JOptionPane.ERROR_MESSAGE);

                }
            }
        });

        setVisible(true);
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();

            }
        });
    }
    public User user;
    private User getAuthenticationUser(String reg_number, String password){
        User user = null;

        final String DB_URL = "http://localhost/phpmyadmin/index.php?route=/sql&db=my+class&table=my+class&pos=0";
        final String USERNAME = "root";
        final String PASSWORD = "";

        try{
            Connection conn = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
            //Connected to database successfully...

            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM `my class` WHERE password=? AND regnumber=?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,password);
            preparedStatement.setString(2,reg_number);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                user = new User ();
                user.username=resultSet.getString("username");
                user.password=resultSet.getString("password");
                user.reg_number=resultSet.getString("reg_number");
            }
            stmt.close();
            conn.close();

        }catch (Exception e){
            e.printStackTrace();
        }

        return user;
    }

    public static void main(String[]args){
        SignupForm signupForm = new SignupForm(null);
        User user = signupForm.user;
        if(user !=null){
            System.out.println("Successful Authentication of:"+user.username);
            System.out.println(" reg_number:"+user.reg_number);
        }
        else{
            System.out.println("Authentication cancelled");
        }
    }
}
