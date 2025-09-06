import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

class Color {
    static final String def = "\u001b[0;1m";
    static final String bgmagneta = "\u001B[45m";
    static final String bgcyan = "\u001B[46m";	
    static final String white = "\u001B[46m";	
    static final String magneta = "\u001B[35;1m";
    static final String cyan = "\u001B[36;1m";	
    static final String blink = "\u001b[5m";
    static final String red =  "\u001b[31;1m";
    static final String green = "\u001b[32;1m";
    static final String yellow = "\u001b[33;1m";
    static final String blue = "\u001b[34;1m";
    static final String purple = "\u001b[35;1m";
    static final String skblue = "\u001b[36;1m";
    static final String bgred =  "\u001b[101;1m";
    static final String bggreen = "\u001b[102;1m";
    static final String bgyellow = "\u001b[103;1m";
    static final String bgblue = "\u001b[104;1m";
    static final String bgpurple = "\u001b[105;1m";
    static final String bgskblue = "\u001b[106;1m";
    static final String reset = "\u001B[0m";
    static final String bgwhite = "\u001B[47m";
}

class Movies {
    static String[] movies = {"The Greatest of All Times", "Saripodha Sanivaaram", "35 (2024)", "Uruku Patela"};
    static double ticketPrice = 299.00; 

    static void Movies_name() {
        Scanner sc = new Scanner(System.in);
        System.out.println();
        System.out.println(Color.cyan + "The list of movies we provided" + Color.def);
        System.out.println();
        for (int i = 0; i < movies.length; i++) {
            System.out.println(Color.cyan + (i + 1) + ". " + movies[i] + " Rating - " + (8 + i * 0.5) + "/10" + Color.def);
        }
        int n = sc.nextInt();
        String selectedMovie = "";

        while (true) {
            if (n >= 1 && n <= 4) {
                selectedMovie = movies[n - 1];
                Theater.selection(selectedMovie);  // Pass the movie name to the Theater
                break;
            } else {
                System.out.println("Please select a valid movie in the given range..!");
                n = sc.nextInt();
            }
        }
    }
}

class Theater {
    static String[] theaters = {"PVR Cinemas", "INOX", "Carnival Cinemas", "Cinepolis"};
    static LocalTime[][] showTimes = {
        {LocalTime.of(10, 0), LocalTime.of(12, 0), LocalTime.of(14, 0), LocalTime.of(16, 0)},
        {LocalTime.of(11, 0), LocalTime.of(13, 0), LocalTime.of(15, 0), LocalTime.of(17, 0)},
        {LocalTime.of(10, 30), LocalTime.of(12, 30), LocalTime.of(14, 30), LocalTime.of(16, 30)},
        {LocalTime.of(11, 30), LocalTime.of(13, 30), LocalTime.of(15, 30), LocalTime.of(17, 30)}
    };

    static void selection(String movieName) {
        Scanner sc = new Scanner(System.in);
        System.out.println(Color.blue + "Available Theaters:");
        
        // Displaying Theaters
        for (int i = 0; i < theaters.length; i++) {
            System.out.println((i + 1) + ". " + theaters[i]);
        }

        // Theater Selection
        System.out.print(Color.purple + "Please select a theater (1 to 4): " + Color.yellow);
        int theaterChoice = sc.nextInt();
        if (theaterChoice < 1 || theaterChoice > 4) {
            System.out.println(Color.red + "Invalid theater selection.");
            selection(movieName);
            return;
        }

        // Display dates
        LocalDate today = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");

        System.out.println("\nShow timings for " + theaters[theaterChoice - 1] + " (Next 5 days):");
        
        for (int i = 0; i < 5; i++) {
            LocalDate showDate = today.plusDays(i);
            System.out.println(Color.cyan + "Date: " + showDate.format(dateFormatter));
            for (int j = 0; j < 4; j++) {
                System.out.println("  Show: " + showTimes[theaterChoice - 1][j].format(timeFormatter));
            }
            System.out.println();
        }

        // Date selection
        System.out.print(Color.purple + "Please select a date (1-5): " + Color.yellow);
        int dateChoice = sc.nextInt();
        while (dateChoice < 1 || dateChoice > 5) {
            System.out.println(Color.red + "Invalid date selection.");
            dateChoice = sc.nextInt();
        }

        // Show timing selection
        System.out.print(Color.purple + "Please select a show timing (1-4): " + Color.yellow);
        int showChoice = sc.nextInt();
        while (showChoice < 1 || showChoice > 4) {
            System.out.println(Color.red + "Invalid show timing selection.");
            showChoice = sc.nextInt();
        }

        LocalDate selectedDate = today.plusDays(dateChoice - 1);
        LocalTime selectedTime = showTimes[theaterChoice - 1][showChoice - 1];

        // Seat Booking
        SeatBooking.initializeSeats(); // Ensure seats are initialized
        SeatBooking.displaySeats();
        String bookedSeat = "";
        int n = sc.nextInt();
        for(int i=1;i<=n;i++)
           bookedSeat = bookedSeat + SeatBooking.bookSeat()+"   ";

        // Payment Process
        Payment.processPayment(movieName, theaters[theaterChoice - 1], selectedDate, selectedTime, bookedSeat);
    }
}

class SeatBooking {
    static Scanner sc = new Scanner(System.in);
    static String[][] seats = new String[5][10];
    static boolean[][] bookedSeats = new boolean[5][10]; // Changed to boolean array

    // Initialize the seats with labels
    static void initializeSeats() {
        char row = 'A';
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 10; j++) {
                seats[i][j] = row + "" + (j + 1);
            }
            row++;
        }
    }

    public static boolean isSeatBooked(String seat) {
        int row = seat.charAt(0) - 'A'; // Convert row character to index
        int column = Integer.parseInt(seat.substring(1)) - 1; // Convert column to index
        return bookedSeats[row][column]; // Return booking status from the boolean array
    }

    public static String getValidSeat() {
        String seat;
        while (true) {
            System.out.print(Color.purple + "Enter Seat Label: " + Color.yellow);
            seat = sc.next();
            if (isValidSeat(seat) && !isSeatBooked(seat)) {
                return seat;
            }
            System.out.println(Color.red + "Invalid or already booked seat. Please try again." + Color.def);
        }
    }

    public static boolean isValidSeat(String seat) {
        return seat.length() == 2 && seat.charAt(0) >= 'A' && seat.charAt(0) <= 'E'
                && Integer.parseInt(seat.substring(1)) >= 1 && Integer.parseInt(seat.substring(1)) <= 10;
    }

    public static String bookSeat() {
        String seat = getValidSeat();
        int row = seat.charAt(0) - 'A'; // Convert row character to index
        int column = Integer.parseInt(seat.substring(1)) - 1; // Convert column to index
        bookedSeats[row][column] = true; // Mark seat as booked
        System.out.println(Color.green + "Seat " + seat + " has been booked successfully!" + Color.def);
        return seat; // Return the booked seat
    }

    public static void displaySeats() {
        System.out.println(Color.blue + "Available Seats:" + Color.def);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 10; j++) {
                String seatLabel = seats[i][j];
                if (isSeatBooked(seatLabel)) {
                    System.out.print(Color.red + " X " + Color.def); // Booked seat
                } else {
                    System.out.print(Color.green + " " + seatLabel + " " + Color.def); // Available seat
                }
            }
            System.out.println();
        }
    }
}

class Payment {
    static void processPayment(String movieName, String theater, LocalDate date, LocalTime time, String seat) {
        double totalPrice = Movies.ticketPrice; // Here, you could calculate the total price based on the number of seats booked
        System.out.println(Color.cyan + "Movie: " + movieName);
        System.out.println("Theater: " + theater);
        System.out.println("Show Date: " + date);
        System.out.println("Show Time: " + time);
        System.out.println("Booked Seat: " + seat);
        System.out.println("Total Price: $" + totalPrice);
        System.out.println(Color.green + "Payment processed successfully!" + Color.def);
    }
}

public class Main {
    public static void main(String[] args) {
        Movies.Movies_name();
    }
}
