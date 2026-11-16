import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *Demonstrates the health service appointment system with a scripted simulation
 *followed by a guided interactive booking.
 *
 *@author Shuo Lu (25259133)
 */
public class AssignmentOne {

    private static final int LINE_WIDTH = 70;
    private static final String[] PROFESSIONAL_TYPES = {
            "General Practitioner", "Specialist", "Nurse", "Dietitian"
    };

    public static void main(String[] args) {
        printHeading("PROG2004 Assessment 1 - Health Service Appointment System");

        printHeading("Part 3 - Working with health professional objects");

        GeneralPractitioner gp1 = new GeneralPractitioner(1001, "Dr Emily Chen",
                new DayOfWeek[] {DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY}, true);
        GeneralPractitioner gp2 = new GeneralPractitioner(1002, "Dr Liam O'Brien", false);
        GeneralPractitioner gp3 = new GeneralPractitioner(1003, "Dr Aisha Patel",
                new DayOfWeek[] {DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.THURSDAY}, true);

        Specialist specialist1 = new Specialist(2001, "Dr Marcus Webb",
                new DayOfWeek[] {DayOfWeek.MONDAY, DayOfWeek.TUESDAY}, "Cardiology");
        Specialist specialist2 = new Specialist(2002, "Dr Sofia Rossi",
                new DayOfWeek[] {DayOfWeek.MONDAY, DayOfWeek.THURSDAY}, "Sports Medicine", false);

        Nurse nurse1 = new Nurse(3001, "Hannah Brooks",
                new DayOfWeek[] {DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY});
        Nurse nurse2 = new Nurse(3002, "Daniel Kim",
                new DayOfWeek[] {DayOfWeek.MONDAY, DayOfWeek.FRIDAY}, "Enrolled Nurse");

        Dietitian dietitian1 = new Dietitian(4001, "Olivia Martin",
                new DayOfWeek[] {DayOfWeek.TUESDAY, DayOfWeek.THURSDAY}, "Diabetes management");
        Dietitian dietitian2 = new Dietitian(4002, "Noah Wilson",
                new DayOfWeek[] {DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY});

        //One list of the base type holds every kind of professional.
        ArrayList<HealthProfessional> professionals = new ArrayList<>();
        professionals.add(gp1);
        professionals.add(gp2);
        professionals.add(gp3);
        professionals.add(specialist1);
        professionals.add(specialist2);
        professionals.add(nurse1);
        professionals.add(nurse2);
        professionals.add(dietitian1);
        professionals.add(dietitian2);

        printSubheading("3.1 Full details of every health professional");
        printProfessionals(professionals);

        printSubheading("3.2 Type-specific behaviour");
        System.out.println(gp1.prescribeMedication("Jack Taylor", "amoxicillin 500 mg"));
        System.out.println(gp2.issueReferral("Jack Taylor", "cardiology"));
        System.out.println(specialist1.orderDiagnosticTest("Jack Taylor", "12-lead ECG"));
        System.out.println(nurse1.takeBloodSample("Mia Nguyen"));
        System.out.println(dietitian1.createMealPlan("Mia Nguyen", "stable blood sugar levels"));
        System.out.println(gp3.issueReferral("", "dermatology"));



        printSubheading("3.3 Every kind of doctor can prescribe (inherited from Doctor)");
        ArrayList<Doctor> doctors = new ArrayList<>();
        doctors.add(gp3);
        doctors.add(specialist2);
        for (Doctor doctor : doctors) {
            System.out.println(doctor.prescribeMedication("Ethan Lee", "ibuprofen 200 mg"));
        }

        printSubheading("3.4 Objects cannot be put into an invalid state");
        try {
            gp2.setName("   ");
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected name change: " + e.getMessage());
        }
        try {
            nurse2.setWorkingDays(new DayOfWeek[] {DayOfWeek.MONDAY, DayOfWeek.MONDAY});
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected working days: " + e.getMessage());
        }
        DayOfWeek[] copyOfDays = dietitian1.getWorkingDays();
        copyOfDays[0] = DayOfWeek.SUNDAY;
        System.out.println("Changing the returned array does not affect the object -> "
                + dietitian1.getName() + " works on Sunday? " + dietitian1.worksOn(DayOfWeek.SUNDAY));
        System.out.println("Name of " + gp2.getId() + " is still: " + gp2.getName());

        printHeading("Part 4 - Modelling an appointment");

        printSubheading("4.1 The clinic's fixed daily schedule");
        System.out.println("Schedule  : " + ClinicSchedule.describe());
        System.out.println("Time slots: " + ClinicSchedule.getTimeSlots());

        printSubheading("4.2 Creating appointments with different professional types");
        Appointment sampleWithSpecialist = new Appointment("Jack Taylor", "0412 345 678",
                specialist1, LocalTime.of(10, 0));
        Patient mia = new Patient("Mia Nguyen", "0423456789");
        Appointment sampleWithNurse = new Appointment(mia, nurse1, LocalTime.of(9, 30));
        System.out.println(sampleWithSpecialist);
        System.out.println();
        System.out.println(sampleWithNurse);




        printSubheading("4.3 Comparing appointments by time (Comparable)");
        System.out.println("09:30 appointment compared with 10:00 appointment -> "
                + sampleWithNurse.compareTo(sampleWithSpecialist) + " (negative = earlier)");



        printSubheading("4.4 Incomplete or invalid appointments are rejected");
        try {
            new Appointment("", "0412345678", gp1, LocalTime.of(8, 0));
        } catch (IllegalArgumentException e) {
            System.out.println("Missing patient name  -> " + e.getMessage());
        }
        try {
            new Appointment("Ava Smith", "12345", gp1, LocalTime.of(8, 0));
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid mobile number -> " + e.getMessage());
        }
        try {
            new Appointment("Ava Smith", "0412345678", null, LocalTime.of(8, 0));
        } catch (IllegalArgumentException e) {
            System.out.println("No professional       -> " + e.getMessage());
        }
        try {
            new Appointment("Ava Smith", "0412345678", gp1, LocalTime.of(9, 15));
        } catch (IllegalArgumentException e) {
            System.out.println("Not a time slot       -> " + e.getMessage());
        }

        printHeading("Part 6 - Working with appointments");

        //A fixed booking day keeps the output the same on every run
        AppointmentManager manager = new AppointmentManager(DayOfWeek.MONDAY);
        System.out.println("Bookings are for: " + ClinicSchedule.formatDay(manager.getBookingDay()));
        System.out.println("Clinic schedule : " + ClinicSchedule.describe());



        printSubheading("6.1 Adding appointments across all professional types");
        System.out.println(manager.addAppointment("Jack Taylor", "0412 345 678", gp1, LocalTime.of(9, 0)));
        System.out.println(manager.addAppointment("Oliver Brown", "0456789012", gp2, LocalTime.of(9, 0)));
        System.out.println(manager.addAppointment("Lucas Green", "0478901234", gp3, LocalTime.of(8, 0)));
        System.out.println(manager.addAppointment("Ethan Lee", "0434567890", specialist1, LocalTime.of(11, 0)));

        System.out.println(manager.addAppointment("Jack Taylor", "0412345678", specialist1, LocalTime.of(14, 0)));
        System.out.println(manager.addAppointment("Sophie Clark", "0489012345", specialist2, LocalTime.of(12, 30)));
        System.out.println(manager.addAppointment("Mia Nguyen", "0423456789", nurse1, LocalTime.of(8, 30)));
        System.out.println(manager.addAppointment("Grace Hall", "0467890123", nurse2, LocalTime.of(15, 30)));
        System.out.println(manager.addAppointment("Chloe Wang", "0445678901", dietitian2, LocalTime.of(10, 0)));
        System.out.println(manager.addAppointment("Mia Nguyen", "0423456789", gp1, LocalTime.of(13, 30)));
        System.out.println(manager.addAppointment(sampleWithSpecialist));
        System.out.println(manager.addAppointment(sampleWithNurse));



        printSubheading("6.2 Bookings that cannot be made");
        System.out.println("Double-booking a professional:");
        System.out.println("  " + manager.addAppointment("Ava Smith", "0491234567", gp1, LocalTime.of(9, 0)));
        HealthProfessional sameAsGp1 = new GeneralPractitioner(1001, "Dr Emily Chen", true);
        System.out.println("  Is a new object with ID 1001 equal to gp1? " + sameAsGp1.equals(gp1));
        System.out.println("  " + manager.addAppointment("Ava Smith", "0491234567", sameAsGp1, LocalTime.of(9, 0)));
        System.out.println("Double-booking a patient:");


        System.out.println("  " + manager.addAppointment("Jack Taylor", "0412345678", gp3, LocalTime.of(9, 0)));
        System.out.println("Professional not working today:");
        System.out.println("  " + manager.addAppointment("Ava Smith", "0491234567", dietitian1, LocalTime.of(10, 0)));
        System.out.println("Times that are not clinic slots:");
        System.out.println("  " + manager.addAppointment("Ava Smith", "0491234567", gp2, LocalTime.of(9, 15)));
        System.out.println("  " + manager.addAppointment("Ava Smith", "0491234567", gp2, LocalTime.of(7, 30)));
        System.out.println("  " + manager.addAppointment("Ava Smith", "0491234567", gp2, LocalTime.of(16, 0)));
        System.out.println("Missing or invalid details:");
        System.out.println("  " + manager.addAppointment("  ", "0491234567", gp2, LocalTime.of(10, 0)));
        System.out.println("  " + manager.addAppointment("Ava Smith", "0391234567", gp2, LocalTime.of(10, 0)));
        System.out.println("  " + manager.addAppointment("Ava Smith", "0491234567", null, LocalTime.of(10, 0)));
        System.out.println("  " + manager.addAppointment("Ava Smith", "0491234567", gp2, null));
        System.out.println("  " + manager.addAppointment(null));



        printSubheading("6.3 Viewing all appointments (booking order, full details)");
        System.out.println(AppointmentManager.formatAppointments("All appointments",
                manager.getAllAppointments()));


        printSubheading("6.4 Sorting all appointments by time");
        System.out.println(AppointmentManager.formatSummary("All appointments, earliest first",
                manager.getAllAppointmentsSortedByTime()));

        printSubheading("6.5 Filtering by health professional ID, then sorting the subset");
        ArrayList<Appointment> cardiologyBookings =
                manager.getAppointmentsByProfessionalId(specialist1.getId());
        System.out.println(AppointmentManager.formatSummary(
                "Professional ID " + specialist1.getId() + " (booking order)", cardiologyBookings));
        System.out.println(AppointmentManager.formatSummary(
                "Professional ID " + specialist1.getId() + " (sorted by time)",
                AppointmentManager.sortByTime(cardiologyBookings)));
        System.out.println(AppointmentManager.formatSummary("Professional ID 9999",
                manager.getAppointmentsByProfessionalId(9999)));


        printSubheading("6.6 Filtering by patient mobile number, then sorting the subset");
        System.out.println(AppointmentManager.formatSummary("Mobile 0412345678 (sorted by time)",
                AppointmentManager.sortByTime(manager.getAppointmentsByPatientMobile("0412345678"))));
        System.out.println(AppointmentManager.formatSummary("Mobile '0423 456 789' (spaces ignored)",
                AppointmentManager.sortByTime(manager.getAppointmentsByPatientMobile("0423 456 789"))));
        System.out.println(AppointmentManager.formatSummary("Mobile 0499999999",
                manager.getAppointmentsByPatientMobile("0499999999")));



        printSubheading("6.7 Cancelling appointments");
        System.out.println("By professional ID and time:");
        System.out.println("  " + manager.cancelAppointment(gp1.getId(), LocalTime.of(9, 0)));
        System.out.println("By patient mobile number:");
        System.out.println("  " + manager.cancelAppointmentsByPatientMobile("0423456789"));
        System.out.println("Appointments that do not exist:");
        System.out.println("  " + manager.cancelAppointment(gp1.getId(), LocalTime.of(9, 0)));
        System.out.println("  " + manager.cancelAppointment(specialist2.getId(), LocalTime.of(8, 0)));
        System.out.println("  " + manager.cancelAppointmentsByPatientMobile("0400000000"));
        System.out.println("  " + manager.cancelAppointmentsByPatientMobile("12345"));



        printSubheading("6.8 Appointments after cancelling (sorted by time)");
        System.out.println(AppointmentManager.formatSummary("Remaining appointments",
                manager.getAllAppointmentsSortedByTime()));
        System.out.println();
        System.out.println("The freed 09:00 slot with " + gp1.getName() + " can now be booked again:");
        System.out.println("  " + manager.addAppointment("Ava Smith", "0491234567", gp1, LocalTime.of(9, 0)));

        printSubheading("6.9 Final list of appointments (sorted by time, full details)");
        System.out.println(AppointmentManager.formatAppointments("Final appointments",
                manager.getAllAppointmentsSortedByTime()));

        // Continue directly into the guided booking workflow after the scripted demo.
        runInteractiveDemo();
    }



    private static void printProfessionals(ArrayList<HealthProfessional> professionals) {
        for (int i = 0; i < professionals.size(); i++) {
            System.out.println("[" + (i + 1) + "] " + professionals.get(i));
            System.out.println();
        }
    }



    private static void printHeading(String title) {
        System.out.println();
        System.out.println("=".repeat(LINE_WIDTH));
        System.out.println(title);
        System.out.println("=".repeat(LINE_WIDTH));
    }


    private static void printSubheading(String title) {
        System.out.println();
        System.out.println(title);
        System.out.println("-".repeat(title.length()));
    }

    /**
     * Runs the guided booking workflow after the required scripted demonstration.
     */
    private static void runInteractiveDemo() {
        ArrayList<HealthProfessional> professionals = createInteractiveProfessionals();
        AppointmentManager manager = new AppointmentManager(DayOfWeek.MONDAY);

        //Occupy one slot so the availability display has something to exclude.
        System.out.println(manager.addAppointment("Existing Patient", "0411111111",
                professionals.get(0), LocalTime.of(9, 0)));

        Scanner input = new Scanner(System.in);
        System.out.println("\nInteractive appointment demonstration");
        System.out.println("Booking day: Monday");
        System.out.println("Schedule: " + ClinicSchedule.describe());
        System.out.println("Dr Emily Chen's existing 09:00 booking will not appear as free.");

        //This is a single guided booking task, not a general application menu.
        System.out.println("\nChoose a type of health professional:");
        for (int i = 0; i < PROFESSIONAL_TYPES.length; i++) {
            System.out.println((i + 1) + ". " + PROFESSIONAL_TYPES[i]);
        }
        int typeChoice = readChoice(input, "Professional type (1-4): ",
                PROFESSIONAL_TYPES.length);
        if (typeChoice == -1) {
            return;
        }
        String selectedType = PROFESSIONAL_TYPES[typeChoice - 1];

        //Only show matching professionals who work today and have a free slot.
        ArrayList<HealthProfessional> availableProfessionals = new ArrayList<>();
        System.out.println("\nAvailable " + selectedType + " professionals:");
        for (HealthProfessional professional : professionals) {
            if (professional.getProfessionalType().equals(selectedType)) {
                ArrayList<LocalTime> freeSlots = manager.getAvailableTimeSlots(professional);
                if (!freeSlots.isEmpty()) {
                    availableProfessionals.add(professional);
                    System.out.println(availableProfessionals.size() + ". "
                            + professional.getName() + " (ID " + professional.getId() + ")");
                    System.out.println("   Free slots: " + freeSlots);
                }
            }
        }

        if (availableProfessionals.isEmpty()) {
            System.out.println(AppointmentManager.WARNING
                    + "No professionals of this type have free slots on Monday.");
            return;
        }

        int professionalChoice = readChoice(input,
                "Choose a professional (1-" + availableProfessionals.size() + "): ",
                availableProfessionals.size());
        if (professionalChoice == -1) {
            return;
        }
        HealthProfessional chosenProfessional =
                availableProfessionals.get(professionalChoice - 1);

        //Read the slots again so the booking always uses the manager's latest state.
        ArrayList<LocalTime> chosenSlots = manager.getAvailableTimeSlots(chosenProfessional);
        System.out.println("\nFree slots for " + chosenProfessional.getName() + ":");
        for (int i = 0; i < chosenSlots.size(); i++) {
            LocalTime start = chosenSlots.get(i);
            System.out.println((i + 1) + ". " + start + " - "
                    + start.plusMinutes(ClinicSchedule.SLOT_LENGTH_MINUTES));
        }

        int slotChoice = readChoice(input,
                "Choose a time slot (1-" + chosenSlots.size() + "): ", chosenSlots.size());
        if (slotChoice == -1) {
            return;
        }
        LocalTime chosenTime = chosenSlots.get(slotChoice - 1);

        //Keep the same professional and slot while allowing incorrect details
        //to be corrected. AppointmentManager still performs every validation.
        while (true) {
            String patientName = readText(input, "Patient name: ");
            if (patientName == null) {
                return;
            }
            String mobileNumber = readText(input, "Patient mobile number: ");
            if (mobileNumber == null) {
                return;
            }

            String bookingResult = manager.addAppointment(patientName, mobileNumber,
                    chosenProfessional, chosenTime);
            System.out.println("\n" + bookingResult);

            if (bookingResult.startsWith(AppointmentManager.SUCCESS)) {
                System.out.println("\n" + AppointmentManager.formatSummary(
                        "Appointments after the interactive booking",
                        manager.getAllAppointmentsSortedByTime()));
                return;
            }

            //A failed attempt must not silently discard the user's booking task.
            String retry = readText(input, "Try the booking again? (Y/N): ");
            if (retry == null || !(retry.equalsIgnoreCase("Y")
                    || retry.equalsIgnoreCase("YES"))) {
                System.out.println(AppointmentManager.WARNING
                        + "Booking attempt ended; no appointment was added.");
                return;
            }
        }
    }

    /**
     *Supplies a small fixed set of objects for the console demonstration.
     *Some professionals do not work on Monday, so the availability filter is
     *meaningful rather than simply printing every object.
     */
    private static ArrayList<HealthProfessional> createInteractiveProfessionals() {
        ArrayList<HealthProfessional> professionals = new ArrayList<>();
        professionals.add(new GeneralPractitioner(1001, "Dr Emily Chen",
                new DayOfWeek[] {DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY,
                        DayOfWeek.FRIDAY}, true));
        professionals.add(new GeneralPractitioner(1002, "Dr Liam O'Brien",
                new DayOfWeek[] {DayOfWeek.TUESDAY, DayOfWeek.THURSDAY}, false));
        professionals.add(new GeneralPractitioner(1003, "Dr Aisha Patel",
                new DayOfWeek[] {DayOfWeek.MONDAY, DayOfWeek.TUESDAY,
                        DayOfWeek.THURSDAY}, true));
        professionals.add(new Specialist(2001, "Dr Marcus Webb",
                new DayOfWeek[] {DayOfWeek.MONDAY, DayOfWeek.TUESDAY}, "Cardiology"));
        professionals.add(new Nurse(3001, "Hannah Brooks",
                new DayOfWeek[] {DayOfWeek.MONDAY, DayOfWeek.TUESDAY,
                        DayOfWeek.WEDNESDAY}));
        professionals.add(new Dietitian(4002, "Noah Wilson",
                new DayOfWeek[] {DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY}));
        return professionals;
    }

    /**
     *Reads a numbered selection and rejects non-numeric or out-of-range input
     *without throwing an exception back to the user.
     */
    private static int readChoice(Scanner input, String prompt, int maximum) {
        while (true) {
            System.out.print(prompt);
            if (!input.hasNextLine()) {
                System.out.println("\n" + AppointmentManager.WARNING
                        + "Input ended before a selection was made.");
                return -1;
            }

            String enteredText = input.nextLine().trim();
            try {
                int choice = Integer.parseInt(enteredText);
                if (choice >= 1 && choice <= maximum) {
                    return choice;
                }
            } catch (NumberFormatException e) {
                //The warning below handles both non-numbers and invalid numbers.
            }
            System.out.println(AppointmentManager.WARNING
                    + "Enter a whole number from 1 to " + maximum + ".");
        }
    }

    /**
     *Returns null when no more input exists, preventing an endless retry loop
     *if the console input stream is closed unexpectedly.
     */
    private static String readText(Scanner input, String prompt) {
        System.out.print(prompt);
        if (input.hasNextLine()) {
            return input.nextLine();
        }
        System.out.println("\n" + AppointmentManager.WARNING
                + "Input ended before the booking demonstration was complete.");
        return null;
    }
}










