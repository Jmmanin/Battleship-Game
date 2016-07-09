/*
Multiplayer Battleship Game
Battleship Server
Jeremy Manin and John Dott
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class BattleshipServer
{
   private JFrame theFrame;
   private JPanel leftPanel;
   private JPanel rightPanel;
   private JPanel portPanel;
   private JPanel buttonPanel;
   private JLabel logoLabel;
   private JLabel titleLabel;
   private JLabel byLabel;
   private JLabel infoLabel;
   private JLabel infoLabel2;
   private JLabel infoLabel3;
   private JLabel infoLabel4;
   private JLabel port1Label;
   private JTextField port1Field;
   private JLabel port2Label;
   private JTextField port2Field;
   private JButton startButton;
   private JButton stopButton;
   private JTextArea console;
         
   public BattleshipServer()
   {
      theFrame= new JFrame("Battleship Server");
      //theFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
      theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      theFrame.setResizable(false);
      theFrame.getContentPane().setLayout(new BoxLayout(theFrame.getContentPane(), BoxLayout.X_AXIS));
   
      theFrame.add(Box.createRigidArea(new Dimension(5,0)));
   
      leftPanel= new JPanel();
      leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
            
      leftPanel.add(Box.createRigidArea(new Dimension(0,5)));
      
      logoLabel= new JLabel(new ImageIcon("resources/battleship_logo.png"));
      logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
      leftPanel.add(logoLabel);
      
      titleLabel= new JLabel("Game Server");
      titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
      leftPanel.add(titleLabel);
      
      byLabel= new JLabel("By: Jeremy Manin, ©Hasbro");
      byLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
      leftPanel.add(byLabel);
      
      leftPanel.add(Box.createRigidArea(new Dimension(0,70)));
      
      infoLabel= new JLabel("Instructions:");
      infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
      leftPanel.add(infoLabel);
      
      infoLabel2= new JLabel("Enter port numbers and click start.");
      infoLabel2.setAlignmentX(Component.CENTER_ALIGNMENT);
      leftPanel.add(infoLabel2);
      
      infoLabel3= new JLabel("Click stop to close server.");
      infoLabel3.setAlignmentX(Component.CENTER_ALIGNMENT);
      leftPanel.add(infoLabel3);
      
      infoLabel4= new JLabel("**This program does not map ports**");
      infoLabel4.setAlignmentX(Component.CENTER_ALIGNMENT);
      leftPanel.add(infoLabel4);
            
      portPanel= new JPanel();
      portPanel.setLayout(new FlowLayout());
      portPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
      
      port1Label= new JLabel("Port 1:");
      portPanel.add(port1Label);
      
      port1Field= new JTextField("4444");
      portPanel.add(port1Field);
      
      port2Label= new JLabel("Port 2:");
      portPanel.add(port2Label);
      
      port2Field= new JTextField("4443");
      portPanel.add(port2Field);
      
      leftPanel.add(portPanel);
      
      buttonPanel= new JPanel();
      buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
      buttonPanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
      
      startButton= new JButton("Start");
      startButton.addActionListener(new StartButtonListener());
      buttonPanel.add(startButton);
      
      buttonPanel.add(Box.createRigidArea(new Dimension(40,0)));
      
      stopButton= new JButton("Stop");
      stopButton.addActionListener(new StopButtonListener());
      stopButton.setEnabled(false);
      buttonPanel.add(stopButton);
      
      leftPanel.add(buttonPanel);
      
      leftPanel.add(Box.createRigidArea(new Dimension(0,20)));
                  
      theFrame.add(leftPanel);
      
      theFrame.add(new JSeparator(SwingConstants.VERTICAL));
      
      rightPanel= new JPanel();
      
      console= new JTextArea(25,25);
      console.setEditable(false);
      console.append("Welcome to the Battleship Multiplayer Server\n");
      console.append("Press start to open server\n");
      rightPanel.add(console);
      
      theFrame.add(rightPanel);
            
      theFrame.pack();
      theFrame.setLocationRelativeTo(null);
      theFrame.setVisible(true);      
   }
   
   private class StartButtonListener implements ActionListener
   {         
      public void actionPerformed(ActionEvent e)
      {
         console.append("\nServer starting with ports " + port1Field.getText() + " and " + port2Field.getText() + ".\n");
         
         (new ServerThread()).start();
         
         port1Field.setEnabled(false);
         port2Field.setEnabled(false);
         startButton.setEnabled(false);
         stopButton.setEnabled(true);
      }
   }   

   private class StopButtonListener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
      
      }
   }
   
   private class ServerThread extends Thread
   {
      private ServerSocket serverSocket1, serverSocket2;
      private Socket p1Socket, p2Socket;
      private ObjectOutputStream p1Out, p2Out;
      private ObjectInputStream p1In, p2In;
      private boolean isP1Turn;
      
      public void run()
      {
         try
         {            
            isP1Turn= true;
            
            serverSocket1= new ServerSocket(Integer.parseInt(port1Field.getText()));
            serverSocket2= new ServerSocket(Integer.parseInt(port2Field.getText()));
         
            p1Socket= serverSocket1.accept();
            console.append("\nPlayer 1 has connected: " + p1Socket.getInetAddress().toString() + "\n");
            p2Socket= serverSocket1.accept();
                  
            p1Out= new ObjectOutputStream(p1Socket.getOutputStream());
            p1Out.flush();
            p1In= new ObjectInputStream(p1Socket.getInputStream());
            
            p2Out= new ObjectOutputStream(p2Socket.getOutputStream());
            p2Out.flush();
            
            p1Out.writeBoolean(true);
            p1Out.flush();      
            p2Out.writeBoolean(false);
            p2Out.flush();      
         
            p2Socket.close();
            p2Socket= serverSocket2.accept();
            console.append("Player 2 has connected: " + p2Socket.getInetAddress().toString() + "\n");
         
            p2Out= new ObjectOutputStream(p2Socket.getOutputStream());
            p2Out.flush();
            p2In= new ObjectInputStream(p2Socket.getInputStream());
            
            console.append("Both players have connected\n\nEntering main game.\n");
                        
            /*while(true)
            {
               if(isP1Turn)
               {
                  Object fromUser= p1In.readObject();
                  
                  
               }      
            }*/
         }
         catch(IOException e)
         {
            console.append("\nError: " + e.getMessage());
         }
         /*catch(ClassNotFoundException e)
         {
            console.append("\nError: " + e.getMessage());
         }*/
      }
   }
       
   public static void main(String args[])
   {
      new BattleshipServer();
   }
}