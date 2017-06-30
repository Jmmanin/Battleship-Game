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
   
      leftPanel= new JPanel(); //left side of window
      leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
            
      leftPanel.add(Box.createRigidArea(new Dimension(0,5)));
      
      logoLabel= new JLabel(new ImageIcon(getImageFile("/resources/battleship_logo.png"))); //logo
      logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
      leftPanel.add(logoLabel);
      
      titleLabel= new JLabel("Game Server"); //title
      titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
      leftPanel.add(titleLabel);
      
      byLabel= new JLabel("By: Jeremy Manin, ©Hasbro"); //credits
      byLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
      leftPanel.add(byLabel);
      
      leftPanel.add(Box.createRigidArea(new Dimension(0,70)));
      
      infoLabel= new JLabel("Instructions:"); //instructions
      infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
      leftPanel.add(infoLabel);
      
      infoLabel2= new JLabel("Enter port numbers and click start.");
      infoLabel2.setAlignmentX(Component.CENTER_ALIGNMENT);
      leftPanel.add(infoLabel2);
      
      infoLabel3= new JLabel("Click stop to close server.");
      infoLabel3.setAlignmentX(Component.CENTER_ALIGNMENT);
      leftPanel.add(infoLabel3);
                  
      portPanel= new JPanel(); //ports
      portPanel.setLayout(new FlowLayout());
      portPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
      
      port1Label= new JLabel("Port 1:");
      portPanel.add(port1Label);
      
      port1Field= new JTextField("4444"); //default port number 1
      portPanel.add(port1Field);
      
      port2Label= new JLabel("Port 2:");
      portPanel.add(port2Label);
      
      port2Field= new JTextField("4443"); //default port number 2
      portPanel.add(port2Field);
      
      leftPanel.add(portPanel);
      
      buttonPanel= new JPanel(); //buttons
      buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
      buttonPanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
      
      ServerListener serverListener= new ServerListener();
      
      startButton= new JButton("Start");
      startButton.addActionListener(serverListener);
      startButton.setActionCommand("start");
      buttonPanel.add(startButton);
      
      buttonPanel.add(Box.createRigidArea(new Dimension(40,0)));
      
      stopButton= new JButton("Stop");
      stopButton.addActionListener(serverListener);
      stopButton.setActionCommand("stop");
      stopButton.setEnabled(false);
      buttonPanel.add(stopButton);
      
      leftPanel.add(buttonPanel);
      
      leftPanel.add(Box.createRigidArea(new Dimension(0,20)));
                  
      theFrame.add(leftPanel);
      
      theFrame.add(new JSeparator(SwingConstants.VERTICAL));
      
      rightPanel= new JPanel(); //right side of window
      
      console= new JTextArea(25,25); //console
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
   
   private BufferedImage getImageFile(String filename) //JAR-friendly method to load image files
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
   
   private class ServerListener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         if(e.getActionCommand().equals("start")) //start button, starts server thread
         {
            console.append("\n\nServer starting with ports " + port1Field.getText() + " and " + port2Field.getText() + ".");
         
            serverThread= new ServerThread();
            serverThread.start();
         
            port1Field.setEnabled(false);
            port2Field.setEnabled(false);
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
         }
         else if(e.getActionCommand().equals("stop")) //stop button, closes connections and exits program
         {
            serverThread.closeConnection();
            System.exit(0);
         }
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
          
         while(connecting) //loops while connecting to both players
         {
            try
            {            
               serverSocket1= new ServerSocket(Integer.parseInt(port1Field.getText()));
               serverSocket2= new ServerSocket(Integer.parseInt(port2Field.getText()));
            
               p1Socket= serverSocket1.accept(); //establishes connection with P1 via port 1
               console.append("\n\nPlayer 1 has connected: " + p1Socket.getInetAddress().toString());
               console.setCaretPosition(console.getDocument().getLength());
               p2Socket= serverSocket1.accept(); //establishes connection with P2 via port 1
                  
               p1Out= new ObjectOutputStream(p1Socket.getOutputStream());
               p1Out.flush();
               p1In= new ObjectInputStream(p1Socket.getInputStream());
            
               p2Out= new ObjectOutputStream(p2Socket.getOutputStream());
               p2Out.flush();
            
               p1Out.writeBoolean(true); //sends messages telling clients which player is P1 and which is P2
               p1Out.flush();      
               p2Out.writeBoolean(false);
               p2Out.flush();      
            
               p2Socket.close(); //closes P2 connection via port 1
               p2Socket= serverSocket2.accept(); //opens P2 connection on port 2
               console.append("\nPlayer 2 has connected: " + p2Socket.getInetAddress().toString());
               console.setCaretPosition(console.getDocument().getLength());
            
               p2Out= new ObjectOutputStream(p2Socket.getOutputStream());
               p2Out.flush();
               p2In= new ObjectInputStream(p2Socket.getInputStream());
               
               connecting= false;
            }
            catch(IOException e) //restarts connection process if P1 disconnects before P2 has connected
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
            while(!endGame) //loops until game is over
            {
               if(isP1Turn) //P1's turn
               {
                  fromClient= (Attack)p1In.readObject(); //gets P1's attack
                  console.append("\nP1 attacks " + fromClient.getCoordName() + ".");
                  console.setCaretPosition(console.getDocument().getLength());
                  
                  p2Out.writeObject(fromClient); //passes P1's attack to P2
                  p2Out.flush();
                  
                  fromClient= (Attack)p2In.readObject(); //gets P2's response
                  
                  if(fromClient.getIsHit()) //prints results of attack to console
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
                  
                  p1Out.writeObject(fromClient); //sends P2's response to P1
                  p1Out.flush();
               }      
               else //P2's turn
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
               endGame= fromClient.getEndGame(); //checks if game over
               isP1Turn= !isP1Turn; //toggles whose turn it is
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
               p1Out.writeObject(null); //sends null to P1 to propagate error
            }
            catch(IOException e2){}
            try
            {
               p2Out.writeObject(null); //sends null to P2 to propagate error
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