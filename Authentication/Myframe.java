package Authentication;

import javax.swing.*;
import java.awt.*;
import MainDashboard.MainDashboard;
public class Myframe extends JFrame  {
    public Myframe() {
        JLabel title;
        title = new JLabel("Colaraz");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        title.setBounds(5,0,400,30);

        JPanel green = new JPanel();
        green.setBackground(new Color(116, 202, 74) );
        green.setBounds(0,0,1300,35);
        green.setLayout(null);
        green.add(title);

        JButton loginButton ;
        JButton signUpButton;

        signUpButton = new JButton("Sign up");
        signUpButton.setBounds(485, 300, 150, 40);
        signUpButton.setBackground(new Color(237, 235, 235));
        signUpButton.setFont(new Font ("Serif",Font.PLAIN,20));

        loginButton = new JButton("Login");
        loginButton.setBounds(665, 300, 150, 40);
        loginButton.setFont(new Font ("Serif",Font.PLAIN,20));
        loginButton.setBackground(new Color(237, 235, 235));

        this.setSize(1300, 900);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setTitle("Sign Up");
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(signUpButton);
        this.add(loginButton);
        this.add(title);
        this.add(green);
        this.getContentPane().setBackground(Color.LIGHT_GRAY);


        JButton backButton = new JButton("Back");
        backButton.setBounds(10, 40, 70, 25);
        this.add(backButton);

        backButton.addActionListener(e -> {
            MainDashboard mainDashboard = new MainDashboard();
            dispose();
        });

        signUpButton.addActionListener(e -> {
            dispose();
            signUpFrame signUpFrame= new signUpFrame();
        });

        loginButton.addActionListener(e ->{
            dispose();
            LoginFrame loginFrame = new LoginFrame();
        });



    }



}
