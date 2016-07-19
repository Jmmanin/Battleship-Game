/*
Multiplayer Battleship Game
Battleship Server
Jeremy Manin
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

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
   private JScrollPane scrollConsole;
   
   private ServerThread serverThread;
         
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
      
      logoLabel= new JLabel(new ImageIcon(getImageFile("/resources/battleship_logo.png")));
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
      console.append("Welcome to the Battleship Multiplayer Server");
      console.append("\nPress start to open server");
      
      scrollConsole= new JScrollPane(console);
      rightPanel.add(scrollConsole);
      
      theFrame.add(rightPanel);
            
      theFrame.pack();
      theFrame.setLocationRelativeTo(null);
      theFrame.setVisible(true);      
   }
   
   private BufferedImage getImageFile(String filename)
   {
      try
      {
         return((BufferedImage)ImageIO.read(getClass().getResourceAsStream(filename)));
      }
      catch(Exception e)
      {
         return(null);
      }
   }
      
   private class StartButtonListener implements ActionListener
   {         
      public void actionPerformed(ActionEvent e)
      {
         console.append("\n\nServer starting with ports " + port1Field.getText() + " and " + port2Field.getText() + ".");
         
         serverThread= new ServerThread();
         serverThread.start();
         
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
         serverThread.closeConnection();
         System.exit(0);
      }
   }
   
   private class ServerThread extends Thread
   {
      private ServerSocket serverSocket1, serverSocket2;
      private Socket p1Socket, p2Socket;
      private ObjectOutputStream p1Out, p2Out;
      private ObjectInputStream p1In, p2In;
      
      public void run()
      {
         boolean connecting= true;
         boolean endGame= false;
         boolean isP1Turn= true;
         Attack fromClient= null;
          
         while(connecting)
         {
            try
            {            
               serverSocket1= new ServerSocket(Integer.parseInt(port1Field.getText()));
               serverSocket2= new ServerSocket(Integer.parseInt(port2Field.getText()));
            
               p1Socket= serverSocket1.accept();
               console.append("\n\nPlayer 1 has connected: " + p1Socket.getInetAddress().toString());
               console.setCaretPosition(console.getDocument().getLength());
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
               console.append("\nPlayer 2 has connected: " + p2Socket.getInetAddress().toString());
               console.setCaretPosition(console.getDocument().getLength());
            
               p2Out= new ObjectOutputStream(p2Socket.getOutputStream());
               p2Out.flush();
               p2In= new ObjectInputStream(p2Socket.getInputStream());
               
               connecting= false;
            }
            catch(IOException e)
            {
               console.append("\nConnection Error: " + e.toString());
               console.append("\nPlayer 1 has aborted connection.");
               console.append("\nRestarting connection process.");
               console.setCaretPosition(console.getDocument().getLength());
               closeConnection();
            }
         }
                     
         console.append("\nBoth players have connected\n\nEntering main game.\n");
         console.setCaretPosition(console.getDocument().getLength());
                     
         try
         {                           
            while(!endGame)
            {
               if(isP1Turn)
               {
                  fromClient= (Attack)p1In.readObject();
                  console.append("\nP1 attacks " + fromClient.getCoordName() + ".");
                  console.setCaretPosition(console.getDocument().getLength());
                  
                  p2Out.writeObject(fromClient);
                  p2Out.flush();
                  
                  fromClient= (Attack)p2In.readObject();
                  if(fromClient.getIsHit())
                  {
                     console.append("\nP2 confirms " + fromClient.getCoordName() + " hits " + fromClient.getShipName() + ".");
                     console.setCaretPosition(console.getDocument().getLength());
                  }
                  else
                  {
                     console.append("\nP2 confirms " + fromClient.getCoordName() + " Miss.");
                     console.setCaretPosition(console.getDocument().getLength());
                  }
                  if(fromClient.getShipSunk())
                  {
                     console.append("\n  P2 " + fromClient.getShipName() + " sunk.");
                     console.setCaretPosition(console.getDocument().getLength());
                  }
                  if(fromClient.getEndGame())
                  {
                     console.append("\n  P1 Wins!");
                     console.setCaretPosition(console.getDocument().getLength());
                  }
                  
                  p1Out.writeObject(fromClient);
                  p1Out.flush();
               }      
               else
               {
                  fromClient= (Attack)p2In.readObject();
                  console.append("\nP2 attacks " + fromClient.getCoordName() + ".");
                  console.setCaretPosition(console.getDocument().getLength());
                  
                  p1Out.writeObject(fromClient);
                  p1Out.flush();
                  
                  fromClient= (Attack)p1In.readObject();
                  if(fromClient.getIsHit())
                  {
                     console.append("\nP1 confirms " + fromClient.getCoordName() + " hits " + fromClient.getShipName() + ".");
                     console.setCaretPosition(console.getDocument().getLength());
                  }
                  else
                  {
                     console.append("\nP1 confirms " + fromClient.getCoordName() + " Miss.");
                     console.setCaretPosition(console.getDocument().getLength());
                  }
                  if(fromClient.getShipSunk())
                  {
                     console.append("\n  P1 " + fromClient.getShipName() + " sunk.");
                     console.setCaretPosition(console.getDocument().getLength());
                  }
                  if(fromClient.getEndGame())
                  {
                     console.append("\n  P2 Wins!");
                     console.setCaretPosition(console.getDocument().getLength());
                  }
                  
                  p2Out.writeObject(fromClient);
                  p2Out.flush();
               }
               
               console.append("\n");
               endGame= fromClient.getEndGame();
               isP1Turn= !isP1Turn;
            }
            
            console.append("\nGame has ended.\nPress stop button to close server.");
            console.setCaretPosition(console.getDocument().getLength());
         }
         catch(IOException e)
         {
            console.append("\nCommunication Error: " + e.toString());
            console.setCaretPosition(console.getDocument().getLength());
            try
            {
               p1Out.writeObject(null);
            }
            catch(IOException e2){}
            try
            {
               p2Out.writeObject(null);
            }
            catch(IOException e2){}
         }
         catch(ClassNotFoundException e)
         {
            console.append("\nCommunication Error: " + e.toString());
            console.setCaretPosition(console.getDocument().getLength());
         }
      }
      
      public void closeConnection()
      {
         try
         {
            p1Socket.close();
            p2Socket.close();
            serverSocket1.close();
            serverSocket2.close();            
         }
         catch(IOException e){}
         catch(NullPointerException e){}        
      }
   }
       
   public static void main(String args[])
   {
      new BattleshipServer();
   }
}