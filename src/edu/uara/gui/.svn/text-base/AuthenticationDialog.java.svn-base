/*
* This file is part of the Factbook Generator.
*
* The Factbook Generator is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* The Factbook Generator is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with The Factbook Generator.  If not, see <http://www.gnu.org/licenses/>.
*
* Copyright 2008, 2009 Bradley Brown, Dustin Yourstone, Jeffrey Hair, Paul Halvorsen, Tu Hoang
*/
package edu.uara.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * This class is a custom modal JDialog used for presenting a simple, user-friendly
 * login interface for connecting to the database. In the current implementation,
 * it appears on program startup and when the user selects Login from the server
 * menu.
 * @author jh44695
 */
public class AuthenticationDialog extends JDialog implements ActionListener {
    private JPanel panel = null;
    private JTextField username;
    private JPasswordField password;
    private JButton loginButton;

    private String usernameEntered;
    private String passwordEntered;

    /**
     * Creates a new AuthenticationDialog.
     * @param frame The parent frame to position the dialog relative to.
     * @param uName The default username to display.
     * @param pw The password to fill in automatically. Usually set to blank.
     * @param extra Any extra text to display next to the "Database Login: " label.
     */
    public AuthenticationDialog(JFrame frame, String uName, String pw, String extra) {
        super(frame, true);
        panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));

        getContentPane().add(panel);
        panel.add(new JLabel("Database Login: " + extra));

        username = new JTextField(uName);
        username.setColumns(25);
        password = new JPasswordField(pw);
        password.setColumns(25);

        password.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    getInfo();
            }
        });


        loginButton = new JButton("Login");
        loginButton.addActionListener(this);

        panel.add(username);
        panel.add(password);
        panel.add(loginButton);
      
        pack();
        setLocationRelativeTo(frame);

        //Password field set to focus by default.
        //Must be done before showing the frame
        password.requestFocus();
        
        super.setVisible(true);
    }

    /**
     * Called when the Login button is pressed. Sets information
     * from the textboxes.
     */
    private void getInfo() {
        usernameEntered = username.getText();
        char[] pw = password.getPassword();
        passwordEntered = new String(pw);

        for (int c = 0; c < pw.length; c++) {
            pw[c] = (char) 0;
        }

        setVisible(false);
    }

    public void actionPerformed(ActionEvent e) {
        getInfo();
    }

    public String getUsername() {
        return usernameEntered;
    }

    public String getPassword() {
        return passwordEntered;
    }

}