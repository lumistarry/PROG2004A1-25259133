import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *Optional console demonstration requested in class. It lets the user choose
 *a professional type, inspect available professionals and their free slots,
 *and then attempt one booking. AssignmentOne remains the scripted demonstration
 *required by the assessment brief.
 *
 *@author Shuo Lu (25259133)
 */
public class InteractiveAppointmentDemo {


    private static final String[] PROFESSIONAL_TYPES = {
            "General Practitioner", "Specialist", "Nurse", "Dietitian"
    };

    public static void main(String[] args) {
        ArrayList<HealthProfessional> professionals = createProfessionals();
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
    private static ArrayList<HealthProfessional> createProfessionals() {
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
