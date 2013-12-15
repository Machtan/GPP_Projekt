package classes;

import interfaces.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
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
 * The seat chooser shows the layout of a given flight and lets the user pick
 * a requested amount of seats by using their mouse to select and deselect
 * seats.
 * @author Stinus Thomsen
 * @email smot@itu.dk
 * @version 27-11-2013
 */
public class SeatChooser extends JPanel implements ISeatChooser
{
    private IFlight flight;
    private ISeating seating;
    int numberOfSeats = 1;
    int seatSize = 11;
    private HashMap<Point, Point> positionToSeat;
    URL url;
    int ImageWidth = 150;
    int ImageHeight = 700;
    
    
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
        this.setPreferredSize(new Dimension(ImageWidth,ImageHeight));
    }
    
    /**
     * The constructor for a SeatChooser
     * @param flight The flight to load after initializing
     */
    public SeatChooser(IFlight flight)
    {
        this();
        setFlight(flight);
    }
    
    /**
     * Sets the flight of this seat chooser and updates the seat chooser to 
     * show this layout and the taken seats for this flight.
     * @param flight The flight to load
     */
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
            ClassLoader cl = SeatChooser.class.getClassLoader();
            String path = flight.getPlane().airplaneLayout.placementImagePath;
            url = cl.getResource(path);
        } catch (Exception ex) {
            System.out.println("The image for this layout couldn't be loaded."+
                    "\nTry reloading NetBeans, it usually helps.");
        }
        
        repaint();
    }
    
    /**
     * Handles mouse events
     * @param e A mouse event
     */
    private void onMouseClicked(MouseEvent e) 
    {
        if(flight == null)
            return;
        Point clickPosition = new Point(e.getX(),e.getY());
	if (indenfor(clickPosition))
            choseSeat(positionToSeat.get(clickToSeatPosition(clickPosition)));
        
        repaint();
    }
    
    /**
     * Tests if a click is inside the SeatChooser's window
     * @param clickPosition The position of the cursor
     * @return Whether the click is inside
     */
    private boolean indenfor(Point clickPosition) 
    { 
        return 0 <= clickPosition.x && 
            clickPosition.x < ImageWidth && 
            0 <= clickPosition.y && 
            clickPosition.y < ImageHeight;
    }
    
    /**
     * Returns the position of the seat closest to the click position
     * @param clickPosition The position which was clicked
     * @return The nearest seat to the cursor when the mouse was clicked
     */
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
    
    /**
     * Handles when a seat is clicked, and selects or deselects it based on its 
     * previous state or does nothing if the seat is taken
     * @param seat The seat to change
     */
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
                TMPseat.y--;
                if(seating.getSeatExists(TMPseat))
                    seating.setChosen(TMPseat);
                if(TMPnumberOfSeats == seating.getChosenSeats().size())
                    break;
                TMPnumberOfSeats = seating.getChosenSeats().size();
            }
            TMPseat = (Point) seat.clone();
            while(numberOfSeats > seating.getChosenSeats().size())
            {
                TMPseat.y++;
                if(seating.getSeatExists(TMPseat))
                    seating.setChosen(TMPseat);
                if(TMPnumberOfSeats == seating.getChosenSeats().size())
                    break;
                TMPnumberOfSeats = seating.getChosenSeats().size();
            }
        }
    }

    /**
     * Override of the paintComponent method to render the layout and the seats
     * using the component's graphics object.
     * @param g the supplied graphics object
     */
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g); 
        
        if(flight == null)
        {
            Graphics2D g2d = (Graphics2D)g;
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
            Font font = new Font("Calibri", Font.PLAIN, 24);
            g2d.setFont(font);
            g2d.drawString("Ingen afgang valgt", 60, 40);
            return;
        }
        
        //paint a picture of the airplane
        try
        {
            if (url != null) {
                g.drawImage(ImageIO.read(url), 0, 0, this);
            } else {
                System.out.println("No working url :(");
            }
            
        }
        catch (IOException ex)
        {
            Logger.getLogger(SeatChooser.class.getName()).log(Level.SEVERE, 
                    null, ex);
        }

        
        //Paint all the seats in difrint colors: 
        //red = taken, green = free, blue = chosen.
        Iterator<Point> iter = seating.getSeatIterator();
        while(iter.hasNext())
        {
            Point iterPoint = iter.next();
            
            g.setColor(Color.red);
            if(seating.getSeatFree(iterPoint))
                g.setColor(Color.green);
            if(seating.getSeatChosen(iterPoint))
                g.setColor(Color.blue);
            
            g.fillRect(seating.getSeatPosition(iterPoint).x+1 - seatSize/2, 
                    seating.getSeatPosition(iterPoint).y+1 - seatSize/2, 
                    seatSize-1, seatSize-1);
            g.setColor(Color.black);
            g.drawRect(seating.getSeatPosition(iterPoint).x+1 - seatSize/2, 
                    seating.getSeatPosition(iterPoint).y+1 - seatSize/2, 
                    seatSize-1, seatSize-1);
        }
    }

    /**
     * Attempts to set taken seats to chosen, usually when a reservation is 
     * loaded (and seats previously taken should be shown as chosen)
     * @param seats The seats to change the internal state of
     */
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

    /**
     * Returns the chosen seats of the seat chooser, or throws an exception if
     * fewer than the requested amount of seats were chosen
     * @param numberOfSeats The number of seats requested
     * @return A list of the chosen seats
     * @throws interfaces.ISeatChooser.NotEnoughSeatsException if fewer seats 
     * have been chosen
     */
    @Override
    public ArrayList<Point> getSeats(int numberOfSeats) throws NotEnoughSeatsException
    {
        if(seating.getChosenSeats().size() == numberOfSeats)
            return seating.getChosenSeats();
        else
            throw new NotEnoughSeatsException(seating.getChosenSeats().size(), 
                    numberOfSeats);
    }

    /**
     * Changes the number of selectable seats at a time for the seat chooser
     * @param number The number of seats that can be chosen, usually 
     * corresponding to the amount of passengers of a reservation
     */
    @Override
    public void setRequestedSeats(int number)
    {
        numberOfSeats = number;
        if (flight != null)
            if(numberOfSeats < seating.getChosenSeats().size())
                for(Point seat : seating.getChosenSeats())
                {
                    choseSeat(seat);
                    if(numberOfSeats == seating.getChosenSeats().size())
                    {
                        repaint();
                        return;
                    }
                }
        repaint();
    }
}
