/*
Multiplayer Battleship Game
Battleship Client
Jeremy Manin and John Dott
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class BattleshipClient
{
   private JFrame theFrame;
   private JPanel logoPanel;
   private JLabel logo;
   private JPanel hostPanel;
   private JLabel hostLabel;
   private JTextField hostField;
   private JPanel portPanel;
   private JLabel port1Label;
   private JTextField port1Field;
   private JLabel port2Label;
   private JTextField port2Field;
   private JPanel confirmPanel;
   private JLabel aboutLabel;
   private JButton confirm;
   
   public BattleshipClient()
   {
      theFrame= new JFrame("Welcome to Battleship");
      theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      theFrame.setResizable(false);
      theFrame.getContentPane().setLayout(new BoxLayout(theFrame.getContentPane(), BoxLayout.Y_AXIS));
      
      logoPanel= new JPanel();
      logoPanel.setLayout(new FlowLayout());
      
      logo= new JLabel(new ImageIcon("resources/battleship_logo.png"));
      logoPanel.add(logo);
      
      theFrame.add(logoPanel);
      
      hostPanel= new JPanel();
      hostPanel.setLayout(new FlowLayout());
      
      hostLabel= new JLabel("Host Name:");
      hostPanel.add(hostLabel);
      
      hostField= new JTextField(10);
      hostPanel.add(hostField);
      
      theFrame.add(hostPanel);
      
      portPanel= new JPanel();
      portPanel.setLayout(new FlowLayout());
      
      port1Label= new JLabel("Port 1:");
      portPanel.add(port1Label);
      
      port1Field= new JTextField("4444");
      portPanel.add(port1Field);
      
      port2Label= new JLabel("Port 2:");
      portPanel.add(port2Label);
      
      port2Field= new JTextField("4443");
      portPanel.add(port2Field);
      
      theFrame.add(portPanel);
      
      confirmPanel= new JPanel();
      confirmPanel.setLayout(new BorderLayout());
       
      aboutLabel= new JLabel("By: Jeremy Manin, ©Hasbro");
      confirmPanel.add(aboutLabel, BorderLayout.WEST);
            
      confirm= new JButton("Start Game");
      confirm.addActionListener(new confirmListener());
      confirmPanel.add(confirm, BorderLayout.EAST);
      
      theFrame.add(confirmPanel);      
            
      theFrame.pack();
      theFrame.setLocationRelativeTo(null);
      theFrame.setVisible(true);
   }
   
   private class confirmListener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         BSClientThread theThread= new BSClientThread(hostField.getText(), Integer.parseInt(port1Field.getText()), Integer.parseInt(port2Field.getText()));
         theThread.start();
         
         theFrame.setVisible(false);
         theFrame.dispose();
      }
   }
         
   public static void main(String args[])
   {
      new BattleshipClient();
   }
}