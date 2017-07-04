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
   private JButton help;
   private JPanel startPanel;
   private JLabel aboutLabel;
   private JButton start;
   
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
      
      modeLabel= new JLabel("Select Game Mode:");
      modeLabelPanel.add(modeLabel);
      
      theFrame.add(modeLabelPanel);
      
      radioPanel= new JPanel();
      radioPanel.setLayout(new BoxLayout(radioPanel, BoxLayout.X_AXIS));
            
      modes= new JRadioButton[2];
      modes[0]= new JRadioButton("Standard"); //default game mode
      radioPanel.add(modes[0]);
      modes[0].setSelected(true);
      modes[1]= new JRadioButton("Salvo");
      radioPanel.add(modes[1]);
      
      modeGroup= new ButtonGroup();
      modeGroup.add(modes[0]);
      modeGroup.add(modes[1]);
      
      ClientListener clientListener= new ClientListener();
      
      help= new JButton("Help");
      help.addActionListener(clientListener);
      help.setActionCommand("help");
      radioPanel.add(help);
            
      theFrame.add(radioPanel);      
       
      theFrame.add(new JSeparator(SwingConstants.HORIZONTAL));
            
      startPanel= new JPanel(); //credits and start button
      startPanel.setLayout(new BoxLayout(startPanel, BoxLayout.X_AXIS)); 
       
      aboutLabel= new JLabel(" By: Jeremy Manin, ©Hasbro");
      startPanel.add(aboutLabel);
      startPanel.add(Box.createHorizontalStrut(10));
            
      start= new JButton("Start Game");
      start.addActionListener(clientListener);
      start.setActionCommand("start");
      startPanel.add(start);
      
      theFrame.add(startPanel); 
            
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
      
   private class ClientListener implements ActionListener
   {
      public void actionPerformed(ActionEvent e) //starts server
      {
         if(e.getActionCommand().equals("start"))
         {
            if(hostField.getText().equals("") || port1Field.getText().equals("") || port2Field.getText().equals(""))
            {
               JOptionPane.showMessageDialog(theFrame ,"Please provide a valid hostname and port numbers.", "Invalid Input", JOptionPane.WARNING_MESSAGE);          
            }
            else
            {
               int modeSelected= 0;
            
               if(modes[1].isSelected())
                  modeSelected= 1;
                           
               BSClientThread theThread= new BSClientThread(hostField.getText(), Integer.parseInt(port1Field.getText()), Integer.parseInt(port2Field.getText()), modeSelected);
               theThread.start();
            
               theFrame.setVisible(false);
               theFrame.dispose();
            }
         }
         else if(e.getActionCommand().equals("help"))
         {
            new HelpDialog();
         }
      }
   }
   
   private class HelpDialog extends JDialog implements ActionListener //displays instructions
   {
      private JPanel helpPanel;
      private JLabel helpLabel1;
      private JLabel helpLabel2;
      private JLabel helpLabel3;
      private JLabel helpLabel4;
      private JLabel helpLabel5;
      private JLabel helpLabel6;
      private JLabel helpLabel7;
      private JLabel helpLabel8;
      private JLabel helpLabel9;
      private JLabel helpLabel10;
      private JButton contButton;
      
      public HelpDialog()
      {
         super(theFrame,"Help",false);
         helpPanel= new JPanel();
         helpPanel.setLayout(new BoxLayout(helpPanel, BoxLayout.Y_AXIS));
         helpPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
         
         helpLabel1= new JLabel("Game Mode Help:");         
         helpLabel1.setAlignmentX(Component.CENTER_ALIGNMENT);
         helpPanel.add(helpLabel1);
         
         helpPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
         
         helpLabel2= new JLabel("Standard Mode:");         
         helpLabel2.setAlignmentX(Component.CENTER_ALIGNMENT);
         helpPanel.add(helpLabel2);
      
         helpLabel3= new JLabel("Tried and true. The classic");
         helpLabel3.setAlignmentX(Component.CENTER_ALIGNMENT);
         helpPanel.add(helpLabel3);
      
         helpLabel4= new JLabel("Battleship experience.");
         helpLabel4.setAlignmentX(Component.CENTER_ALIGNMENT);
         helpPanel.add(helpLabel4);
         
         helpPanel.add(Box.createVerticalStrut(6));
         
         helpLabel5= new JLabel("Salvo Mode:");
         helpLabel5.setAlignmentX(Component.CENTER_ALIGNMENT);
         helpPanel.add(helpLabel5);
      
         helpLabel6= new JLabel("For more experienced admirals.");         
         helpLabel6.setAlignmentX(Component.CENTER_ALIGNMENT);
         helpPanel.add(helpLabel6);
         
         helpLabel7= new JLabel("Each turn you send attacks");         
         helpLabel7.setAlignmentX(Component.CENTER_ALIGNMENT);
         helpPanel.add(helpLabel7);
      
         helpLabel8= new JLabel("equal to the number of ships");         
         helpLabel8.setAlignmentX(Component.CENTER_ALIGNMENT);
         helpPanel.add(helpLabel8);
         
         helpLabel9= new JLabel("you have remaining. Which ships");
         helpLabel9.setAlignmentX(Component.CENTER_ALIGNMENT);
         helpPanel.add(helpLabel9);
      
         helpLabel10= new JLabel("are hit is no longer disclosed.");         
         helpLabel10.setAlignmentX(Component.CENTER_ALIGNMENT);
         helpPanel.add(helpLabel10);
                  
         helpPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
         
         contButton= new JButton("Continue");
         contButton.addActionListener(this);
         contButton.setAlignmentX(Component.CENTER_ALIGNMENT);
         helpPanel.add(contButton);
         
         this.setContentPane(helpPanel);
         this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
         this.pack();
         this.setResizable(false);
         this.setLocationRelativeTo(theFrame);
         this.setVisible(true);
      }
         
      public void actionPerformed(ActionEvent e)
      {
         this.setVisible(false);
         this.dispose();
      }   
   }
         
   public static void main(String args[])
   {
      new BattleshipClient();
   }
}