package interfaces;

import classes.Reservation;

/**
 * The IReservationEditor interface <More docs goes here>
 * @author Jakob Lautrup Nysom (jaln@itu.dk)
 * @version 22-Nov-2013
 */
public interface IReservationEditor {
    void open();
    void open(Reservation res);
}
