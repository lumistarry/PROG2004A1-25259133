import java.time.LocalTime;

/**
 * A single appointment for the current day, linking a patient to a chosen
 * health professional and a time slot.
 *
 * @author Shuo Lu (25259133)
 */
public class Appointment implements Comparable<Appointment> {

    private final Patient patient;
    // The general type, so an appointment works with any kind of professional.
    private final HealthProfessional professional;
    private final LocalTime time;

    public Appointment(Patient patient, HealthProfessional professional, LocalTime time) {
        if (patient == null) {
            throw new IllegalArgumentException("An appointment needs a patient.");
        }
        if (professional == null) {
            throw new IllegalArgumentException("An appointment needs a health professional.");
        }
        if (!ClinicSchedule.isValidTimeSlot(time)) {
            throw new IllegalArgumentException("An appointment needs a valid time slot: "
                    + ClinicSchedule.describe() + ".");
        }
        this.patient = patient;
        this.professional = professional;
        this.time = time;
    }

    public Appointment(String patientName, String mobileNumber,
                       HealthProfessional professional, LocalTime time) {
        this(new Patient(patientName, mobileNumber), professional, time);
    }

    public Patient getPatient() {
        return patient;
    }

    public HealthProfessional getProfessional() {
        return professional;
    }

    public LocalTime getTime() {
        return time;
    }

    public LocalTime getEndTime() {
        return time.plusMinutes(ClinicSchedule.SLOT_LENGTH_MINUTES);
    }

    // LocalTime compares correctly, so a list of appointments sorts by time.
    @Override
    public int compareTo(Appointment other) {
        return time.compareTo(other.time);
    }

    @Override
    public String toString() {
        String professionalDetails = professional.toString().replace("\n", "\n      ");
        return "Appointment " + time + " - " + getEndTime()
                + "\n    Patient      : " + patient
                + "\n    Professional : " + professionalDetails;
    }
}
