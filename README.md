# java-async-ticket-booking

## Project Goal

This project was developed to study and demonstrate the principles of **multithreading and concurrency control** in Java, focusing on managing access to a limited shared resource by multiple competing processes.

-----

## Implementation Details

The project simulates a ticket booking system where multiple clients (threads) compete for a fixed, limited number of tickets (10 total).

### Key Technologies and Concepts

| Technology / Concept | Purpose |
| :--- | :--- |
| **Java Multithreading** | Uses the **`Thread`** class and the **`Runnable`** interface (`Client.java`) to create concurrent client processes. |
| **`Semaphore`** | Utilized for **synchronization** and limiting the number of simultaneous booking operations, effectively protecting the resource (10 tickets) from race conditions. |
| **Thread States** | The output explicitly tracks and displays various **thread states** (**NEW**, **RUNNABLE**, etc.) throughout the simulation. |
| **`BookingException`** | A custom **checked exception** used for handling business-logic errors (e.g., "tickets sold out" or "forbidden booking time"). |
| **Business Constraint** | Implements a rule that forbids booking during a specific time window (**00:00 to 06:00**), showcasing conditional resource access logic. |

-----

## Project Structure

```
java-async-ticket-booking/
├── src/
│   ├── TicketBookingService.java    // Main logic, Semaphore control, time constraints.
│   ├── Client.java                 // Runnable implementation for client threads.
│   └── BookingException.java       // Custom checked exception.
└── README.md
```

-----

## How to Run

1.  Clone the repository:
    ```bash
    git clone https://github.com/YourUsername/java-async-ticket-booking.git
    cd java-async-ticket-booking
    ```
2.  Compile the Java files:
    ```bash
    javac src/*.java
    ```
3.  Execute the main simulation class:
    ```bash
    java src/TicketBookingService
    ```

*The console output demonstrates concurrency, thread states, and the successful application of the Semaphore to enforce the 10-ticket limit.*