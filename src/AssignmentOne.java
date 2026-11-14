import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * Demonstrates the health service appointment system by creating objects and
 * exercising their behaviour.
 *
 * @author Shuo Lu (25259133)
 */
public class AssignmentOne {

    private static final int LINE_WIDTH = 70;

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

        // One list of the base type holds every kind of professional.
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
}
