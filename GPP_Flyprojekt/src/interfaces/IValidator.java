package interfaces;

/**
 * The IValidator interface is the interface for a class which can validate the
 * fields on a person based on a given identifier
 * @author Jakob Lautrup Nysom (jaln@itu.dk)
 * @version 02-Dec-2013
 */
public interface IValidator {
    public <T> boolean validate(T field, String value);
}
