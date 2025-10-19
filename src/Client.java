import java.util.Random;

/**
 * Client.java
 * Represents a client (thread) attempting to book a ticket.
 * Implements the Runnable interface for multithreading.
*/
public class Client implements Runnable {
    private final String clientName;
    private final TicketBookingService bookingService;
    private final Random random = new Random();

    /**
     * Client constructor.
     * @param name name of the client
     * @param system ref to the booking system
	*/
    public Client(String name, TicketBookingService system) {
        this.clientName = name;
        this.bookingService = system;
    }

    @Override
    public void run() {
        // Display current thread state
        System.out.println(clientName + " simulate booking attempt.\nCurrent State: " + Thread.currentThread().getState());

        try {
            // Simulate variable network delay, attempt to book a ticket
            Thread.sleep(random.nextInt(500) + 100);
            bookingService.bookTicket(clientName);
        } catch (InterruptedException e) {
            // Handle thread interruption (if the thread is forcibly stopped)
            System.err.println(clientName + " was interrupted while waiting. \nError: " + e.getMessage());
            Thread.currentThread().interrupt(); // Restore interrupted status
        } catch (BookingException e) {
            // Handle specific booking failures (no tickets, forbidden time, todo maybe more)
            System.out.println(clientName + " could not book a ticket, because of: " + e.getMessage());
        } finally {
            // Output final state, thread will be TERMINATED shortly after this print.
            System.out.println(clientName + "\'ve finished attempt. \nCurrent State: " + Thread.currentThread().getState());
        }
    }
}
