/*
Multiplayer Battleship Game
PlaceShips
Jeremy Manin
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class PlaceShips
{
   private JFrame theFrame;
   private JPanel gridPanel;
   private BSGrid theGrid;
   private JPanel shipPanel;
   private JPanel msgPanel;
   private JPanel buttonPanel;
   private JButton modeButton;
   private JButton helpButton;
   private JButton doneButton;
   private JLabel battleship;
   private JLabel carrier;
   private JLabel cruiser;
   private JLabel destroyer;
   private JLabel submarine;
   private JButton orientation;
   private JLabel msgLabel;
   private JLabel modeLabel;
   private JLabel orientLabel;
   
   private WaitDialog waitDialog;
      
   private int mode;
   private int shipOrientation;
   private String shipSelected;
   private boolean baPlaced;
   private boolean caPlaced;
   private boolean crPlaced;
   private boolean dePlaced;
   private boolean suPlaced;
   private int shipsAdded;
   private Ship[] ships;
      
   private BSClientThread clientThread;
   
   public PlaceShips(BSClientThread cT)
   {
      theFrame= new JFrame("Ship Placement");
      theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      theFrame.setResizable(false);
      theFrame.getContentPane().setLayout(new BoxLayout(theFrame.getContentPane(), BoxLayout.Y_AXIS));
   
      msgPanel= new JPanel(new GridLayout(1,3)); //game messages
      msgPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 0, 10));
      
      msgLabel= new JLabel("Select a ship to place.");
      
      modeLabel= new JLabel("Mode: Place");
      modeLabel.setHorizontalAlignment(SwingConstants.CENTER);
            
      orientLabel= new JLabel("Orientation: Horizontal");
      orientLabel.setHorizontalAlignment(SwingConstants.RIGHT);
      
      msgPanel.add(msgLabel);
      msgPanel.add(modeLabel);
      msgPanel.add(orientLabel);
      
      theFrame.add(msgPanel);
      theFrame.add(Box.createRigidArea(new Dimension(0,5)));
                  
      buttonPanel= new JPanel(new GridLayout(1,5)); //buttons
       
      PlaceShipsListener placeShipsListener= new PlaceShipsListener(); 
       
      mode= 0; //initializes mode to "place"     
      modeButton= new JButton("Mode");
      modeButton.addActionListener(placeShipsListener);
      modeButton.setActionCommand("mode");
      
      helpButton= new JButton("Help");
      helpButton.addActionListener(placeShipsListener);
      helpButton.setActionCommand("help");
            
      doneButton= new JButton("Done");
      doneButton.addActionListener(placeShipsListener);
      doneButton.setActionCommand("done");
      doneButton.setEnabled(false);
                 
      buttonPanel.add(new JLabel());
      buttonPanel.add(modeButton);
      buttonPanel.add(helpButton);
      buttonPanel.add(doneButton);
      buttonPanel.add(new JLabel());
      
      theFrame.add(buttonPanel);
      theFrame.add(new JSeparator(SwingConstants.HORIZONTAL));
            
      gridPanel= new JPanel(); //battleship grid
      gridPanel.setLayout(new BoxLayout(gridPanel,BoxLayout.X_AXIS));
      
      theGrid= new BSGrid(10);
      theGrid.addMouseListener(new GridMouser());
      
      gridPanel.add(Box.createRigidArea(new Dimension(84,0)));
      gridPanel.add(theGrid);
      gridPanel.add(Box.createRigidArea(new Dimension(84,0)));
      
      theFrame.add(gridPanel);
      
      shipPanel= new JPanel(); //displays ships
      shipPanel.setLayout(new GridLayout(2,3));
      
      ImageIcon bsImg= new ImageIcon(getImageFile("/resources/ba_hor.png"));
      ImageIcon caImg= new ImageIcon(getImageFile("/resources/ca_hor.png"));
      ImageIcon crImg= new ImageIcon(getImageFile("/resources/cr_hor.png"));
      ImageIcon deImg= new ImageIcon(getImageFile("/resources/de_hor.png"));
      ImageIcon suImg= new ImageIcon(getImageFile("/resources/su_hor.png"));
      
      battleship= new JLabel(bsImg);
      carrier= new JLabel(caImg);
      cruiser= new JLabel(crImg);
      destroyer= new JLabel(deImg);
      submarine= new JLabel(suImg);
   
      shipOrientation= 0; //initializes placement orrientation as horizontal
      orientation= new JButton("Change Orientation");
      orientation.addActionListener(placeShipsListener);
      orientation.setActionCommand("orientation");
      
      shipPanel.add(battleship);
      shipPanel.add(carrier);
      shipPanel.add(cruiser);
      shipPanel.add(destroyer);
      shipPanel.add(submarine);
      shipPanel.add(orientation);
      
      shipPanel.addMouseListener(new ShipMouser());
      
      theFrame.add(Box.createRigidArea(new Dimension(0,10)));
      theFrame.add(new JSeparator(SwingConstants.HORIZONTAL));
      theFrame.add(shipPanel);
      theFrame.add(Box.createRigidArea(new Dimension(0,5)));
      
      theFrame.pack();
      theFrame.setLocationRelativeTo(null);
      
      waitDialog= new WaitDialog();
      
      shipSelected= null;
      baPlaced= false;
      caPlaced= false;
      crPlaced= false;
      dePlaced= false;
      suPlaced= false;
      
      shipsAdded= 0;
      ships= new Ship[5];
      
      clientThread= cT;      
   }
   
   public void start() //closes wait dialog and allows player to start ship placement after both players have connected
   {
      waitDialog.close();
      theFrame.setVisible(true);   
   }
      
   public Ship[] getShips()
   {
      return(ships);
   }
   
   private BufferedImage getImageFile(String filename) //JAR-friendly method to load image files
   {
      try
      {
         return((BufferedImage)ImageIO.read(PlaceShips.class.getResourceAsStream(filename)));
      }
      catch(Exception e)
      {
         JOptionPane.showMessageDialog(null ,"Error loading image resource.", "Load Error", JOptionPane.ERROR_MESSAGE);          
         System.exit(1);
      }
      
      return(null);
   }
      
   private class GridMouser extends MouseAdapter //handles mouse clicks on grid
   {
      public void mouseClicked(MouseEvent e)
      {         
         Point clicked= new Point(e.getX(),e.getY());
         Point topLeftCorner= new Point(30,30);
         StringBuilder imgName= new StringBuilder();
         Ship toErase;
         int i;
         
         if(mode==0) //place
         {         
            imgName.append("/resources/" + shipSelected + "_"); //starts img path with folder and ship name
                           
            if(clicked.getX()>=topLeftCorner.getX() && clicked.getY()>=topLeftCorner.getY())
            {
               if(shipSelected=="ba" && baPlaced==false)
               {
                  baPlaced= placeShip(imgName, clicked, shipOrientation, 4);
                  if(baPlaced==true)
                  {
                     msgLabel.setText("Battleship placed");
                     battleship.setEnabled(false);
                  }   
               }
               else if(shipSelected=="ca" && caPlaced==false)
               {
                  caPlaced= placeShip(imgName, clicked, shipOrientation, 5);
                  if(caPlaced==true)
                  {
                     msgLabel.setText("Carrier placed");
                     carrier.setEnabled(false);
                  }   
               }
               else if(shipSelected=="cr" && crPlaced==false)
               {
                  crPlaced= placeShip(imgName, clicked, shipOrientation, 3);
                  if(crPlaced==true)
                  {
                     msgLabel.setText("Cruiser placed");
                     cruiser.setEnabled(false);
                  }   
               }
               else if(shipSelected=="de" && dePlaced==false)
               {
                  dePlaced= placeShip(imgName, clicked, shipOrientation, 2);
                  if(dePlaced==true)
                  {
                     msgLabel.setText("Destroyer placed");
                     destroyer.setEnabled(false);
                  }   
               }
               else if(shipSelected=="su" && suPlaced==false)            
               {
                  suPlaced= placeShip(imgName, clicked, shipOrientation, 3);
                  if(suPlaced==true)
                  {
                     msgLabel.setText("Submarine placed");
                     submarine.setEnabled(false);
                  }   
               }            
            }
         
            if(shipsAdded==5)
               doneButton.setEnabled(true);
         }
         else //erase
         {
            for(i=0;i<shipsAdded;i++)
            {
               if(shipsAdded>0 && ships[i].contains(clicked))
               {
                  toErase= ships[i];
                  eraseShip(toErase);
                  
                  if(ships[i].getName()=="ba")
                  {
                     battleship.setEnabled(true);
                     baPlaced= false;
                     msgLabel.setText("Battleship Removed");
                  }
                  else if(ships[i].getName()=="ca")
                  {
                     carrier.setEnabled(true);
                     caPlaced= false;
                     msgLabel.setText("Carrier Removed");
                  }
                  else if(ships[i].getName()=="cr")
                  {
                     cruiser.setEnabled(true);
                     crPlaced= false;
                     msgLabel.setText("Cruiser Removed");
                  }
                  else if(ships[i].getName()=="de")
                  {
                     destroyer.setEnabled(true);
                     dePlaced= false;
                     msgLabel.setText("Destroyer Removed");
                  }
                  else
                  {
                     submarine.setEnabled(true);
                     suPlaced= false;
                     msgLabel.setText("Submarine Removed");
                  }
                                    
                  ships[i]= ships[shipsAdded-1];
                  ships[shipsAdded-1]= null;
                  shipsAdded--;
                  doneButton.setEnabled(false);
               
                  break;
               }   
            }
         }   
      }
      
      //places selected ship on grid. 
      //takes started img path, point clicked, orientation, and ship size
      //returns true if placed, false if error
      private boolean placeShip(StringBuilder imgName, Point tempPoint, int orientation, int size)
      {
         BufferedImage shipImg;
         Point[] points= new Point[size];
         BufferedImage bgImg= getImageFile("/resources/ocean.png");
         BufferedImage combined;
         JLabel temp;
         int xPos= 0, yPos= 0;
         int i;
         
         if(orientation==0) //horizontal
         {
            if(tempPoint.getX()<(330-((size-1)*30))) //checks for valid placement
            {               
               imgName.append("hor.png");
               shipImg= getImageFile(imgName.toString());
               
               for(i=0;i<size;i++) //creates array of points within grid spaces to be occupied by ship
               {
                  points[i]= new Point(tempPoint);
                  tempPoint.setLocation(tempPoint.getX()+30,tempPoint.getY());   
               }
            }
            else
            {
               msgLabel.setText("Too close to edge");
               return(false);
            }     
         }
         else //vertical
         {
            if(tempPoint.getY()<(330-((size-1)*30)))
            {
               imgName.append("ver.png");
               shipImg= getImageFile(imgName.toString());
               
               for(i=0;i<size;i++)
               {
                  points[i]= new Point(tempPoint);
                  tempPoint.setLocation(tempPoint.getX(),tempPoint.getY()+30);   
               }
            }
            else
            {
               msgLabel.setText("Too close to edge");
               return(false);
            }
         }
         
         ships[shipsAdded]= new Ship(shipSelected,size,shipOrientation,points); //adds new ship to ship list
         
         if(shipsAdded>0) //checks intersections if not first ship
         {
            for(i=0;i<shipsAdded;i++)
            {
               if(ships[shipsAdded].getTotalSpaceOccupied().intersects(ships[i].getTotalSpaceOccupied()))
               {
                  msgLabel.setText("Intersects other ship"); 
                  return(false);
               }
            }
         }
                
         for(i=0;i<ships[shipsAdded].getSize();i++) //overlays ship image onto background grid space by grid space
         {
            temp= (JLabel)theGrid.findComponentAt(ships[shipsAdded].getLocation(i));
            
            combined= new BufferedImage(30,30,BufferedImage.TYPE_INT_ARGB);
            Graphics g= combined.getGraphics();
               
            g.drawImage(bgImg,0,0,null);
            g.drawImage(shipImg.getSubimage(xPos,yPos,30,30),0,0,null);
                                    
            temp.setIcon(new ImageIcon(combined));
            
            if(shipOrientation==0)
               xPos= xPos+30;
            else
               yPos= yPos+30;   
         }
         
         shipsAdded++;
         return(true);
      }         
   }
      
   private void eraseShip(Ship toErase) //erases clicked ship
   {
      JLabel temp;
      int i;
         
      for(i=0;i<toErase.getSize();i++) //replaces grid spaces occupied by ship with blank background
      {
         temp= (JLabel)theGrid.findComponentAt(toErase.getLocation(i));
         temp.setIcon(new ImageIcon(getImageFile("/resources/ocean.png")));
      }         
   } 
   
   private class ShipMouser extends MouseAdapter //handles mouse clicks on ship display
   {
      public void mouseClicked(MouseEvent e)
      {
         if(mode==0) //selects ship if mode is "place"
         {
            int xPos= e.getX();
            int yPos= e.getY();
            String labelLabel;
         
            if(shipPanel.findComponentAt(xPos,yPos)==battleship)
            {
               msgLabel.setText("Battleship selected");
               shipSelected= "ba";
            }
            if(shipPanel.findComponentAt(xPos,yPos)==carrier)
            {
               msgLabel.setText("Carrier selected");
               shipSelected= "ca";
            }
            if(shipPanel.findComponentAt(xPos,yPos)==cruiser)
            {
               msgLabel.setText("Cruiser selected");
               shipSelected= "cr";
            }
            if(shipPanel.findComponentAt(xPos,yPos)==destroyer)
            {
               msgLabel.setText("Destroyer selected");
               shipSelected= "de";
            }
            if(shipPanel.findComponentAt(xPos,yPos)==submarine)
            {
               msgLabel.setText("Submarine selected");
               shipSelected= "su";
            }
         }
      }
   }
   
   private class PlaceShipsListener implements ActionListener //button listener
   {
      public void actionPerformed(ActionEvent e)
      {
         if(e.getActionCommand().equals("orientation")) //toggles orrientation
         {
            if(shipOrientation==0)
            {
               orientLabel.setText("Orientation: Veritcal");
               shipOrientation= 1;
            }   
            else
            {
               orientLabel.setText("Orientation: Horizontal");
               shipOrientation= 0;
            }     
         }
         else if(e.getActionCommand().equals("mode")) //toggles mode
         {
            if(mode==0)
            {
               mode= 1;
               modeLabel.setText("Mode: Erase");
            }
            else
            {
               mode= 0;
               modeLabel.setText("Mode: Place");
            }
         }
         else if(e.getActionCommand().equals("help")) //displays help message
         {
            new HelpDialog();
         }
         else if(e.getActionCommand().equals("done")) //finalizes ships and moves to main game
         {
            theFrame.setVisible(false);
            theFrame.dispose();   
            clientThread.setContToMain(true);
            synchronized(clientThread) //notifies server handling thread to continue game
            {
               clientThread.notifyAll();
            }
         }
      }
   }
   
   private class WaitDialog extends JDialog implements ActionListener //dialog displayed until both players join game
   {
      private JPanel waitPanel;
      private JLabel waitMsg;
      private JButton cancelButton;
   
      public WaitDialog()
      {
         super(theFrame,"Waiting",false);
         this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
      
         waitPanel= new JPanel();
         waitPanel.setLayout(new BoxLayout(waitPanel, BoxLayout.Y_AXIS));
         waitPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
         
         waitMsg= new JLabel("Waiting for other player to join.");
         waitMsg.setAlignmentX(Component.CENTER_ALIGNMENT);
         waitPanel.add(waitMsg);
         
         cancelButton= new JButton("Cancel");
         cancelButton.addActionListener(this);
         cancelButton.setAlignmentX(Component.CENTER_ALIGNMENT);
         waitPanel.add(cancelButton);
         
         this.setContentPane(waitPanel);
         this.pack();
         this.setLocationRelativeTo(null);
         this.setVisible(true);
      }
   
      public void actionPerformed(ActionEvent e) //quits if player cancels
      {
         System.exit(0);
      }
      
      public void close()
      {
         this.setVisible(false);
         this.dispose();
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
      private JLabel helpLabel11;
      private JButton contButton;
      
      public HelpDialog()
      {
         super(theFrame,"Help",false);
         helpPanel= new JPanel();
         helpPanel.setLayout(new BoxLayout(helpPanel, BoxLayout.Y_AXIS));
         helpPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
         
         helpLabel1= new JLabel("Instructions:");         
         helpLabel1.setAlignmentX(Component.CENTER_ALIGNMENT);
         helpPanel.add(helpLabel1);
         
         helpPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
         
         helpLabel2= new JLabel("Click a ship to select it.");         
         helpLabel2.setAlignmentX(Component.CENTER_ALIGNMENT);
         helpPanel.add(helpLabel2);
      
         helpLabel3= new JLabel("Click the grid to place selected ship.");
         helpLabel3.setAlignmentX(Component.CENTER_ALIGNMENT);
         helpPanel.add(helpLabel3);
      
         helpLabel4= new JLabel("Ship extends to the right or down");
         helpLabel4.setAlignmentX(Component.CENTER_ALIGNMENT);
         helpPanel.add(helpLabel4);
         
         helpLabel5= new JLabel("from where you clicked.");
         helpLabel5.setAlignmentX(Component.CENTER_ALIGNMENT);
         helpPanel.add(helpLabel5);
      
         helpLabel6= new JLabel("Click \"Change Orientation\" to switch");         
         helpLabel6.setAlignmentX(Component.CENTER_ALIGNMENT);
         helpPanel.add(helpLabel6);
         
         helpLabel7= new JLabel("between horizontal and vertical placement.");         
         helpLabel7.setAlignmentX(Component.CENTER_ALIGNMENT);
         helpPanel.add(helpLabel7);
      
         helpLabel8= new JLabel("Click \"Mode\" button to switch");         
         helpLabel8.setAlignmentX(Component.CENTER_ALIGNMENT);
         helpPanel.add(helpLabel8);
         
         helpLabel9= new JLabel("between place and erase modes.");
         helpLabel9.setAlignmentX(Component.CENTER_ALIGNMENT);
         helpPanel.add(helpLabel9);
      
         helpLabel10= new JLabel("Click ship on grid to erase it.");         
         helpLabel10.setAlignmentX(Component.CENTER_ALIGNMENT);
         helpPanel.add(helpLabel10);
         
         helpLabel11= new JLabel("Click \"Done\" button to finalize fleet.");         
         helpLabel11.setAlignmentX(Component.CENTER_ALIGNMENT);
         helpPanel.add(helpLabel11);
         
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
}