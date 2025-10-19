import java.time.LocalTime;
import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 * TicketBookingService.java
 * Main class for the ticket booking Service. It manages the limited ticket resource
 * using a Semaphore and enforces the time-based booking rule.
*/
public class TicketBookingService {
    // Limited number of tickets (to test resource control)
    private static final int TOTAL_TICKETS = 10;
    // Semaphore to control concurrent access to the limited number of tickets.
    private final Semaphore ticketSemaphore = new Semaphore(TOTAL_TICKETS, true);
    
    // Counter for booked tickets (just for output display || debug)
    private int bookedCount = 0;
    
    private final Random random = new Random();
    
    // Constants defining the forbidden booking time window
    private static final LocalTime START_FORBIDDEN = LocalTime.of(0, 0); // 00:00
    private static final LocalTime END_FORBIDDEN = LocalTime.of(6, 0);   // 06:00

    /**
     * Checks if booking is allowed at the time.
     * In a real Service, this would use LocalTime.now(). Here, we use a random time, just to test.
     * @return true if booking is allowed, false otherwise.
	*/
    private boolean isBookingTimeAllowed() {
        LocalTime currentTime = LocalTime.of(random.nextInt(24), random.nextInt(60));
        boolean isForbidden = !currentTime.isBefore(START_FORBIDDEN) && currentTime.isBefore(END_FORBIDDEN);
    
		System.out.println("Time check: " + currentTime.toString() + ". \nBooking Forbidden: " + isForbidden);        
        return !isForbidden;
    }

    /**
     * Method for booking a ticket.
     * Uses Semaphore to synchronize access to the ticket resource.
     * @param clientName the name of the client 
     * @throws BookingException if a business rule failure occurs
     * @throws InterruptedException if the thread is interrupted
	*/ 
    public void bookTicket(String clientName) throws BookingException, InterruptedException {
        // Step 1: Check the time constraint condition
        if (!isBookingTimeAllowed()) {
            throw new BookingException("We\'re sorry, but booking is not available from " + START_FORBIDDEN + " to " + END_FORBIDDEN + ".");
        }
        
        // Step 2: Attempt to acquire a permit from the Semaphore.
        // tryAcquire() checks availability without blocking, allowing us to manage the "sold out" state.
        if (ticketSemaphore.tryAcquire()) {
            try {
                
                // Simulate the booking processing time
                Thread.sleep(200); 
                bookedCount++;
                System.out.println("SUCCESS: Client " + clientName + " booked ticket #" + bookedCount + 
                                   ". Remaining tickets: " + ticketSemaphore.availablePermits());
                
            } catch (InterruptedException e) {
                // If the thread is interrupted during processing, the permit must be released
                ticketSemaphore.release(); 
                throw e; // Re-throw the exception
            } finally {
                // Step 3: Release the permit (ticket) back to the Semaphore.
                // This is placed in the Client's run method finally block instead for safety
                // if other exceptions were possible, but here we keep the release inside
                // the critical section's flow since tryAcquire was successful.
            }
        } else {
            // If tryAcquire() failed (no permits available)
            throw new BookingException("All " + TOTAL_TICKETS + " tickets are currently sold out or being processed.");
        }
    }

    /**
     * Main method to run the simulation.
	*/
    public static void main(String[] args) {
        System.out.println("---  Ticket Booking Service ---");
        
        TicketBookingService Service = new TicketBookingService();
        int numberOfClients = 15;
        
        Thread[] clientThreads = new Thread[numberOfClients];
        
        // Creating client threads
        for (int i = 0; i < numberOfClients; i++) {
            Client client = new Client("Client-" + (i + 1), Service);
            // Creating Thread object with a Runnable task
            clientThreads[i] = new Thread(client, "ClientThread-" + (i + 1));
            // Output initial thread state (NEW)
            System.out.println("Created thread for Client-" + (i + 1) + ". \nState: " + clientThreads[i].getState());
        }
        
        System.out.println("\n--- Starting Booking Simulation ---\n");
        
        // Starting all threads
        for (Thread thread : clientThreads) {
            thread.start();
        }
        
        // Wait for all threads to finish execution (join)
        for (Thread thread : clientThreads) {
            try {
                // Output state before joining (RUNNABLE or WAITING/BLOCKED)
                if (thread.isAlive()) {
                    System.out.println("📢 Waiting for " + thread.getName() + " to finish. State: " + thread.getState());
                }
                thread.join(); // Wait until the thread transitions to TERMINATED state
            } catch (InterruptedException e) {
                System.err.println("Main thread was interrupted: " + e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
        
        System.out.println("\n--- Simulation Completed ---");
        System.out.println("Total tickets successfully booked: " + Service.bookedCount + " out of " + TOTAL_TICKETS + ".");
    }
}
