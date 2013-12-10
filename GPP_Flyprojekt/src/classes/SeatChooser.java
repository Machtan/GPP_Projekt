/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package classes;

import interfaces.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

/**
 *
 * @author Stinus Thomsen
 * @email smot@itu.dk
 * @version 27-11-2013
 */
public class SeatChooser extends JPanel implements ISeatChooser
{
    private IFlight flight;
    private ISeating seating;
    int numberOfSeats = 1;
    int seatSize = 12;
    private HashMap<Point, Point> positionToSeat;
    URL url;
    
    
    public SeatChooser()
    {
        super();
        this.addMouseListener(new MouseListener() 
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                onMouseClicked(e);
            }
            @Override
            public void mousePressed(MouseEvent e){}
            @Override
            public void mouseReleased(MouseEvent e){}
            @Override
            public void mouseEntered(MouseEvent e){}
            @Override
            public void mouseExited(MouseEvent e){}
        });
        
        this.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        this.setPreferredSize(new Dimension(200,400));
    }
    
    public SeatChooser(IFlight flight)
    {
        this();
        setFlight(flight);
    }
    
    @Override
    public void setFlight(IFlight flight)
    {
        this.flight = flight;
        this.seating = new Seating(flight);
        positionToSeat = new HashMap<Point, Point>();
        
        Iterator<Point> iter = seating.getSeatIterator();
        while(iter.hasNext())
        {
            Point iterPoint = iter.next();
            positionToSeat.put(seating.getSeatPosition(iterPoint), iterPoint);
        }
        
        // - TODO integra dette i databasen / med databasen
        try {
            url = SeatChooser.class.getClassLoader().getResource("images/Plane.png");
        } catch (Exception ex) {
            System.out.println("DU ØDELAGDE MIT PROGRAM DIN LORTE-EXCEPTION!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
        
        repaint();
    }
    
    private void onMouseClicked(MouseEvent e) 
    {
        if(flight == null)
            return;
        Point clickPosition = new Point(e.getX(),e.getY());
	if (indenfor(clickPosition))
            choseSeat(positionToSeat.get(clickToSeatPosition(clickPosition)));
        
        repaint();
    }
    
    //test if the click is inside the SeatChocser
    private boolean indenfor(Point clickPosition) 
    { 
        try
        {
            //denne her skal tjekke om man har tryket paa en at knaperne/plaserne
            return 0 <= clickPosition.x && 
                    clickPosition.x < ImageIO.read(url).getWidth() && 
                    0 <= clickPosition.y && 
                    clickPosition.y < ImageIO.read(url).getHeight();
        }
        catch (IOException ex)
        {
            Logger.getLogger(SeatChooser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    //get the position of the seat closet to the click position
    private Point clickToSeatPosition(Point clickPosition)
    {
        Iterator<Point> iter = positionToSeat.keySet().iterator();
        Point seatPosition = iter.next();
        while(iter.hasNext())
        {
            Point iterPoint = iter.next();
            if(Math.sqrt(Math.pow((clickPosition.x - iterPoint.x),2)+
                    Math.pow((clickPosition.y - iterPoint.y),2)) < 
                    Math.sqrt(Math.pow((clickPosition.x - seatPosition.x),2)+
                    Math.pow((clickPosition.y - seatPosition.y),2)))
                seatPosition = iterPoint;
                
        }
        return seatPosition;
    }
    
    //chose one or multibel seats
    private void choseSeat(Point seat)
    {
        //if you click on a seat you have chosen, then unchose it
        for(Point seatNr : seating.getChosenSeats())
        {
            if (seatNr.x == seat.x && 
                    seatNr.y == seat.y)
            {
                seating.removeChosen(seat);
                return;
            }
        }
        
        //chose a seat if more seats is needet
        if (seating.getChosenSeats().size() < numberOfSeats)
        {
            if(seating.getSeatFree(seat))
                seating.setChosen(seat);
            else
                return;
            
            //chose seats in same row
            Point TMPseat = (Point) seat.clone();
            int TMPnumberOfSeats = seating.getChosenSeats().size();
            while(numberOfSeats > seating.getChosenSeats().size())
            {
                TMPseat.x--;
                if(seating.getSeatExists(TMPseat))
                    seating.setChosen(TMPseat);
                if(TMPnumberOfSeats == seating.getChosenSeats().size())
                    break;
                TMPnumberOfSeats = seating.getChosenSeats().size();
            }
            TMPseat = (Point) seat.clone();
            while(numberOfSeats > seating.getChosenSeats().size())
            {
                TMPseat.x++;
                if(seating.getSeatExists(TMPseat))
                    seating.setChosen(TMPseat);
                if(TMPnumberOfSeats == seating.getChosenSeats().size())
                    break;
                TMPnumberOfSeats = seating.getChosenSeats().size();
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g); //To change body of generated methods, choose Tools | Templates.
        
        if(flight == null) // - TODO der skal vare en text som forteler at der ikke er noglen flight
            return;
        
        //paint text for how many free seats ther is
        g.drawString("" + seating.getFreeSeats().size() + " of " + (seating.getFreeSeats().size() + seating.getTakenSeats().size()) + " are free", 0, 0);
        
        //paint a pikture of the airplane
        try
        {
            g.drawImage(ImageIO.read(url), 0, 0, this);
        }
        catch (IOException ex)
        {
            Logger.getLogger(SeatChooser.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Paint all the seats in difrint colors: red = taken, green = free, blue = chosen.
        Iterator<Point> iter = seating.getSeatIterator();
        while(iter.hasNext())
        {
            Point iterPoint = iter.next();
            
            g.setColor(Color.red);
            if(seating.getSeatFree(iterPoint))
                g.setColor(Color.green);
            if(seating.getSeatChosen(iterPoint))
                g.setColor(Color.blue);
            
            g.fillRect(seating.getSeatPosition(iterPoint).x*seatSize+1 - seatSize/2, seating.getSeatPosition(iterPoint).y*seatSize+1 - seatSize/2, seatSize-1, seatSize-1);
            g.setColor(Color.black);
            g.drawRect(seating.getSeatPosition(iterPoint).x*seatSize+1 - seatSize/2, seating.getSeatPosition(iterPoint).y*seatSize+1 - seatSize/2, seatSize-1, seatSize-1);
        }
    }

    @Override
    public void setChosen(ArrayList<Point> seats)
    {
        try
        {
            seating.changeTakenToChosen(seats);
        }
        catch (UnsupportedOperationException e)
        {
            System.out.println("here is a error i am no sure how to conter");
        }
    }

    @Override
    public ArrayList<Point> getSeats(int numberOfSeats)
    {
        if(seating.getChosenSeats().size() == numberOfSeats)
            return seating.getChosenSeats();
        else
            throw new UnsupportedOperationException("Fail not supported yet.");
            //throw new NotEnoughSeatsException();
        //return null;
    }

    @Override
    public void setRequestedSeats(int number)
    {
        numberOfSeats = number;
    }
}
