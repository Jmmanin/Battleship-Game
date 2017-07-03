/*
Multiplayer Battleship Game
Battleship Client
Jeremy Manin
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

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
   private JPanel modeLabelPanel;
   private JLabel modeLabel;
   private JPanel radioPanel;
   private ButtonGroup modeGroup;
   private JRadioButton[] modes;
   private JPanel confirmPanel;
   private JLabel aboutLabel;
   private JButton confirm;
   
   public BattleshipClient()
   {      
      theFrame= new JFrame("Welcome to Battleship");
      theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      theFrame.setResizable(false);
      theFrame.getContentPane().setLayout(new BoxLayout(theFrame.getContentPane(), BoxLayout.Y_AXIS));
      
      logoPanel= new JPanel(); //splash logo
      logoPanel.setLayout(new FlowLayout());
      
      logo= new JLabel(new ImageIcon(getImageFile("/resources/battleship_logo.png")));
      logoPanel.add(logo);
      
      theFrame.add(logoPanel);
      
      theFrame.add(new JSeparator(SwingConstants.HORIZONTAL));
      
      hostPanel= new JPanel(); //host name
      hostPanel.setLayout(new FlowLayout());
      
      hostLabel= new JLabel("Host Name:");
      hostPanel.add(hostLabel);
      
      hostField= new JTextField(10);
      hostPanel.add(hostField);
      
      theFrame.add(hostPanel);
      
      portPanel= new JPanel(); //ports
      portPanel.setLayout(new FlowLayout());
      
      port1Label= new JLabel("Port 1:");
      portPanel.add(port1Label);
      
      port1Field= new JTextField("4444"); //default port 1
      portPanel.add(port1Field);
      
      port2Label= new JLabel("Port 2:");
      portPanel.add(port2Label);
      
      port2Field= new JTextField("4443"); //default port 2
      portPanel.add(port2Field);
      
      theFrame.add(portPanel);
      
      theFrame.add(new JSeparator(SwingConstants.HORIZONTAL));
      
      modeLabelPanel= new JPanel(); //game modes
      modeLabelPanel.setLayout(new FlowLayout());
      
      modeLabel= new JLabel("Select Game Mode");
      modeLabelPanel.add(modeLabel);
      
      theFrame.add(modeLabelPanel);
      
      radioPanel= new JPanel();
      radioPanel.setLayout(new FlowLayout());
            
      modes= new JRadioButton[2];
      modes[0]= new JRadioButton("Standard"); //default game mode
      radioPanel.add(modes[0]);
      modes[0].setSelected(true);
      modes[1]= new JRadioButton("Salvo");
      radioPanel.add(modes[1]);
      
      modeGroup= new ButtonGroup();
      modeGroup.add(modes[0]);
      modeGroup.add(modes[1]);
      
      radioPanel.add(new JButton("Help"));
            
      theFrame.add(radioPanel);      
       
      theFrame.add(new JSeparator(SwingConstants.HORIZONTAL));
            
      confirmPanel= new JPanel(); //credits and start button
      confirmPanel.setLayout(new BorderLayout());
       
      aboutLabel= new JLabel(" By: Jeremy Manin, ©Hasbro");
      confirmPanel.add(aboutLabel, BorderLayout.WEST);
            
      confirm= new JButton("Start Game");
      confirm.addActionListener(new confirmListener());
      confirmPanel.add(confirm, BorderLayout.EAST);
      
      theFrame.add(confirmPanel); 
            
      theFrame.pack();
      theFrame.setLocationRelativeTo(null);
      theFrame.setVisible(true);
   }
   
   private BufferedImage getImageFile(String filename) //JAR-friendly method to load image files
   {
      try
      {
         return((BufferedImage)ImageIO.read(BattleshipClient.class.getResourceAsStream(filename)));
      }
      catch(Exception e)
      {
         JOptionPane.showMessageDialog(null ,"Error loading image resource.", "Load Error", JOptionPane.ERROR_MESSAGE);          
         System.exit(1);
      } 
      
      return(null);     
   }
      
   private class confirmListener implements ActionListener
   {
      public void actionPerformed(ActionEvent e) //starts server
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