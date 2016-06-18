/*
Multiplayer Battleship Game
PlaceShips
Jeremy Manin and John Dott
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PlaceShips
{
   private JFrame theFrame;
   private JPanel gridPanel;
   private BSGrid theGrid;
   private JPanel shipPanel;
   private JPanel msgPanel;
   private JLabel battleship;
   private JLabel carrier;
   private JLabel cruiser;
   private JLabel destroyer;
   private JLabel submarine;
   private JButton orientation;
   private JLabel msgTxt;
   private JButton done;
   private JLabel orientTxt;
   
   private int shipOrientation;
   private String shipSelected;
   private boolean baPlaced;
   private boolean caPlaced;
   private boolean crPlaced;
   private boolean dePlaced;
   private boolean suPlaced;
   private int shipsAdded;
   private Ship[] ships;
   
   public PlaceShips()
   {
      theFrame= new JFrame("Ship Placement");
      theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      theFrame.setResizable(false);
      theFrame.getContentPane().setLayout(new BoxLayout(theFrame.getContentPane(), BoxLayout.Y_AXIS));

      msgPanel= new JPanel();
      msgPanel.setLayout(new GridLayout(1,3));
      
      msgTxt= new JLabel("Select a ship to place.");
      
      done= new JButton("Done");
      done.addActionListener(new DoneButtonHandler());
      done.setEnabled(false);
            
      orientTxt= new JLabel("Orientation: Horizontal");
      orientTxt.setHorizontalAlignment(SwingConstants.RIGHT);
      
      msgPanel.add(msgTxt);
      msgPanel.add(done);
      msgPanel.add(orientTxt);
      
      theFrame.add(msgPanel);
      
      gridPanel= new JPanel();
      gridPanel.setLayout(new BoxLayout(gridPanel,BoxLayout.X_AXIS));
      
      theGrid= new BSGrid(10);
      theGrid.addMouseListener(new GridMouser());
      
      gridPanel.add(Box.createRigidArea(new Dimension(84,0)));
      gridPanel.add(theGrid);
      gridPanel.add(Box.createRigidArea(new Dimension(84,0)));
      
      theFrame.add(gridPanel);
      
      shipPanel= new JPanel();
      shipPanel.setLayout(new GridLayout(2,3));
      
      ImageIcon bsImg= new ImageIcon("resources/battleship.png");
      ImageIcon caImg= new ImageIcon("resources/carrier.png");
      ImageIcon crImg= new ImageIcon("resources/cruiser.png");
      ImageIcon deImg= new ImageIcon("resources/destroyer.png");
      ImageIcon suImg= new ImageIcon("resources/submarine.png");
      
      battleship= new JLabel(bsImg);
      carrier= new JLabel(caImg);
      cruiser= new JLabel(crImg);
      destroyer= new JLabel(deImg);
      submarine= new JLabel(suImg);
   
      shipOrientation= 0;
      orientation= new JButton("Change Orientation");
      orientation.addActionListener(new OrientationButtonHandler());
      
      shipPanel.add(battleship);
      shipPanel.add(carrier);
      shipPanel.add(cruiser);
      shipPanel.add(destroyer);
      shipPanel.add(submarine);
      shipPanel.add(orientation);
      
      shipPanel.addMouseListener(new ShipMouser());
      
      theFrame.add(shipPanel);
      
      theFrame.pack();
      theFrame.setVisible(true);
      
      shipSelected= null;
      baPlaced= false;
      caPlaced= false;
      crPlaced= false;
      dePlaced= false;
      suPlaced= false;
      
      shipsAdded= 0;
      ships= new Ship[5];
   }
   
   private class GridMouser extends MouseAdapter
   {
      public void mouseClicked(MouseEvent e)
      {
         Point clicked= new Point(e.getX(),e.getY());
         Point topLeftCorner= new Point(30,30);
         StringBuilder imgName= new StringBuilder();
         int i;
                  
         System.out.println(clicked);         
         
         imgName.append("resources/" + shipSelected + "_");
                           
         if(clicked.getX()>=topLeftCorner.getX() && clicked.getY()>=topLeftCorner.getY())
         {
            if(shipSelected=="ba" && baPlaced==false)
            {
               baPlaced= placeShip(imgName, clicked, shipOrientation, 4);
               if(baPlaced==true)
                  msgTxt.setText("Battleship placed");
            }
            else if(shipSelected=="ca" && caPlaced==false)
            {
               caPlaced= placeShip(imgName, clicked, shipOrientation, 5);
               if(baPlaced==true)
                  msgTxt.setText("Aircraft Carrier placed");
            }
            else if(shipSelected=="cr" && crPlaced==false)
            {
               crPlaced= placeShip(imgName, clicked, shipOrientation, 3);
               if(baPlaced==true)
                  msgTxt.setText("Cruiser placed");
            }
            else if(shipSelected=="de" && dePlaced==false)
            {
               dePlaced= placeShip(imgName, clicked, shipOrientation, 2);
               if(baPlaced==true)
                  msgTxt.setText("Destroyer placed");
            }
            else if(shipSelected=="su" && suPlaced==false)            
            {
               suPlaced= placeShip(imgName, clicked, shipOrientation, 3);
               if(baPlaced==true)
                  msgTxt.setText("Submarine placed");
            }
         }
         
         if(shipsAdded==5)
            done.setEnabled(true);
      }
      
      public boolean placeShip(StringBuilder imgName, Point tempPoint, int orientation, int size)
      {
         Point[] points= new Point[size];
         int i;
         
         if(orientation==0)
         {
            if(tempPoint.getX()<(330-((size-1)*30)))
            {
               imgName.append("hor_");
               
               for(i=1;i<=size;i++)
               {
                  JLabel temp= (JLabel)theGrid.findComponentAt(tempPoint);
                  imgName.append(i + ".png");
                  temp.setIcon(new ImageIcon(imgName.toString()));
                  imgName.delete(17,22);
                  points[i-1]= new Point(tempPoint);
                  tempPoint.setLocation(tempPoint.getX()+30,tempPoint.getY());   
               }
            }
            else
            {
               msgTxt.setText("Location selected too close to edge");
               return(false);
            }     
         }
         else
         {
            if(tempPoint.getY()<(330-((size-1)*30)))
            {
               imgName.append("ver_");
               
               for(i=1;i<=size;i++)
               {
                  JLabel temp= (JLabel)theGrid.findComponentAt(tempPoint);
                  imgName.append(i + ".png");
                  temp.setIcon(new ImageIcon(imgName.toString()));
                  imgName.delete(17,22);
                  points[i-1]= new Point(tempPoint);
                  tempPoint.setLocation(tempPoint.getX(),tempPoint.getY()+30);   
               }
            }
            else
            {
               msgTxt.setText("Location selected too close to edge");
               return(false);
            }
         }   
         
         ships[shipsAdded]= new Ship(shipSelected,size,shipOrientation,points);
         shipsAdded++;
         return(true);
      }
   }
   
   private class ShipMouser extends MouseAdapter
   {
      public void mouseClicked(MouseEvent e)
      {
         int xPos= e.getX();
         int yPos= e.getY();
         String labelTxt;

         if(shipPanel.findComponentAt(xPos,yPos)==battleship)
         {
            msgTxt.setText("Battleship selected");
            shipSelected= "ba";
         }
         if(shipPanel.findComponentAt(xPos,yPos)==carrier)
         {
            msgTxt.setText("Aircraft Carrier selected");
            shipSelected= "ca";
         }
         if(shipPanel.findComponentAt(xPos,yPos)==cruiser)
         {
            msgTxt.setText("Cruiser selected");
            shipSelected= "cr";
         }
         if(shipPanel.findComponentAt(xPos,yPos)==destroyer)
         {
            msgTxt.setText("Destroyer selected");
            shipSelected= "de";
         }
         if(shipPanel.findComponentAt(xPos,yPos)==submarine)
         {
            msgTxt.setText("Submarine selected");
            shipSelected= "su";
         }
      }
   }
   
   private class OrientationButtonHandler implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         if(shipOrientation==0)
         {
            orientTxt.setText("Orientation: Veritcal");
            shipOrientation= 1;
         }   
         else
         {
            orientTxt.setText("Orientation: Horizontal");
            shipOrientation= 0;
         }     
      }
   }
   
   private class DoneButtonHandler implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         new BattleShipUI(ships);
         //theFrame.setVisible(false);
      }
   }   
   
   public static void main(String args[])
   {
      new PlaceShips();
   }
}