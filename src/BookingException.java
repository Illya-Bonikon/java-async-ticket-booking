/**
 * BookingException.java
 * A custom exception class to handle errors in the ticketing system.
*/
public class BookingException extends Exception {
    /**
     * A constructor that accepts error messages.
     * @param message error details
	*/
    public BookingException(String message) {
        super(message);
    }
	
}