import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;

/**
 *Manages all of the appointments for one day: adding, filtering, sorting and
 *cancelling. Every operation reports back whether it succeeded.
 *
 *@author Shuo Lu (25259133)
 */
public class AppointmentManager {

    public static final String SUCCESS = "SUCCESS: ";
    public static final String WARNING = "WARNING: ";
    //An ArrayList suits a collection that grows and shrinks as bookings change.
    private final ArrayList<Appointment> appointments;
    private final DayOfWeek bookingDay;

    public AppointmentManager(DayOfWeek bookingDay) {
        if (bookingDay == null) {
            throw new IllegalArgumentException("The booking day must be provided.");
        }
        this.bookingDay = bookingDay;
        this.appointments = new ArrayList<>();
    }

    public AppointmentManager() {
        this(LocalDate.now().getDayOfWeek());
    }

    public DayOfWeek getBookingDay() {
        return bookingDay;
    }

    public int getAppointmentCount() {
        return appointments.size();
    }

    public String addAppointment(String patientName, String mobileNumber,
                                 HealthProfessional professional, LocalTime time) {
        if (!Patient.isValidName(patientName)) {
            return WARNING + "Cannot book: the patient name is missing.";
        }
        String name = patientName.trim();
        if (!Patient.isValidMobileNumber(mobileNumber)) {
            return WARNING + "Cannot book " + name + ": mobile number '" + mobileNumber
                    + "' is not 10 digits starting with 04.";
        }
        if (professional == null) {
            return WARNING + "Cannot book " + name + ": no health professional was chosen.";
        }
        if (time == null) {
            return WARNING + "Cannot book " + name + ": no time was chosen.";
        }
        if (!ClinicSchedule.isValidTimeSlot(time)) {
            return WARNING + "Cannot book " + name + ": " + time
                    + " is not a clinic time slot. Schedule: " + ClinicSchedule.describe() + ".";
        }
        return addAppointment(new Appointment(patientName, mobileNumber, professional, time));
    }

    public String addAppointment(Appointment appointment) {
        if (appointment == null) {
            return WARNING + "Cannot book: no appointment was provided.";
        }
        HealthProfessional professional = appointment.getProfessional();
        Patient patient = appointment.getPatient();
        LocalTime time = appointment.getTime();

        if (!professional.worksOn(bookingDay)) {
            return WARNING + "Cannot book " + patient.getName() + ": " + professional.getName()
                    + " does not work on " + ClinicSchedule.formatDay(bookingDay) + ".";
        }
        for (Appointment existing : appointments) {
            if (existing.getTime().equals(time)) {
                //equals() compares IDs, so this catches a double-booked professional.
                if (existing.getProfessional().equals(professional)) {
                    return WARNING + "Cannot book " + patient.getName() + ": "
                            + professional.getName() + " (ID " + professional.getId()
                            + ") is already booked at " + time + ".";
                }
                if (existing.getPatient().hasMobileNumber(patient.getMobileNumber())) {
                    return WARNING + "Cannot book " + patient.getName()
                            + ": this patient already has an appointment at " + time + ".";
                }
            }
        }
        appointments.add(appointment);
        return SUCCESS + "Booked " + patient.getName() + " with " + professional.getName()
                + " (" + professional.getProfessionalType() + ") at " + time + ".";
    }

    public ArrayList<Appointment> getAllAppointments() {
        //Return a copy so callers cannot add or remove bookings directly.
        return new ArrayList<>(appointments);
    }

    /**
     *Finds the slots that are free for one professional on the booking day.
     *An empty result means that the professional does not work that day or
     *that every clinic slot has already been booked.
     */
    public ArrayList<LocalTime> getAvailableTimeSlots(HealthProfessional professional) {
        ArrayList<LocalTime> availableSlots = new ArrayList<>();
        if (professional == null || !professional.worksOn(bookingDay)) {
            return availableSlots;
        }

        //Check every clinic slot against the bookings already in the manager.
        for (LocalTime slot : ClinicSchedule.getTimeSlots()) {
            boolean alreadyBooked = false;
            for (Appointment existing : appointments) {
                if (existing.getTime().equals(slot)
                        && existing.getProfessional().equals(professional)) {
                    alreadyBooked = true;
                    break;
                }
            }
            if (!alreadyBooked) {
                availableSlots.add(slot);
            }
        }
        return availableSlots;
    }

    public ArrayList<Appointment> getAppointmentsByProfessionalId(int professionalId) {
        ArrayList<Appointment> result = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getProfessional().getId() == professionalId) {
                result.add(appointment);
            }
        }
        return result;
    }

    public ArrayList<Appointment> getAppointmentsByPatientMobile(String mobileNumber) {
        ArrayList<Appointment> result = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getPatient().hasMobileNumber(mobileNumber)) {
                result.add(appointment);
            }
        }
        return result;
    }

    public ArrayList<Appointment> getAllAppointmentsSortedByTime() {
        return sortByTime(appointments);
    }

    //Sort a copy, so this works on all appointments or on a filtered subset.
    public static ArrayList<Appointment> sortByTime(ArrayList<Appointment> list) {
        ArrayList<Appointment> sorted = new ArrayList<>(list);
        Collections.sort(sorted);
        return sorted;
    }

    public static String formatAppointments(String title, ArrayList<Appointment> appointments) {
        if (appointments == null || appointments.isEmpty()) {
            return WARNING + "No appointments found (" + title + ").";
        }
        String result = title + " - " + appointments.size() + " appointment(s):";
        for (int i = 0; i < appointments.size(); i++) {
            result += "\n\n" + (i + 1) + ". " + appointments.get(i);
        }
        return result;
    }

    public static String formatSummary(String title, ArrayList<Appointment> appointments) {
        if (appointments == null || appointments.isEmpty()) {
            return WARNING + "No appointments found (" + title + ").";
        }
        String result = title + " - " + appointments.size() + " appointment(s):";
        for (Appointment appointment : appointments) {
            HealthProfessional professional = appointment.getProfessional();
            result += String.format("\n  %s  %-14s %-12s with %s (%s, ID %d)",
                    appointment.getTime(), appointment.getPatient().getName(),
                    appointment.getPatient().getMobileNumber(), professional.getName(),
                    professional.getProfessionalType(), professional.getId());
        }
        return result;
    }

    public String cancelAppointment(int professionalId, LocalTime time) {
        if (time == null) {
            return WARNING + "Cannot cancel: no time was given.";
        }
        for (int i = 0; i < appointments.size(); i++) {
            Appointment appointment = appointments.get(i);
            if (appointment.getProfessional().getId() == professionalId
                    && appointment.getTime().equals(time)) {
                appointments.remove(i);
                return SUCCESS + "Cancelled " + appointment.getPatient().getName()
                        + "'s appointment with " + appointment.getProfessional().getName()
                        + " at " + time + ".";
            }
        }
        return WARNING + "Cannot cancel: no appointment found for professional ID "
                + professionalId + " at " + time + ".";
    }

    public String cancelAppointmentsByPatientMobile(String mobileNumber) {
        if (!Patient.isValidMobileNumber(mobileNumber)) {
            return WARNING + "Cannot cancel: '" + mobileNumber + "' is not a valid mobile number.";
        }
        int cancelled = 0;
        //Work backwards, so removing an item does not skip the next one.
        for (int i = appointments.size() - 1; i >= 0; i--) {
            if (appointments.get(i).getPatient().hasMobileNumber(mobileNumber)) {
                appointments.remove(i);
                cancelled++;
            }
        }
        if (cancelled == 0) {
            return WARNING + "Cannot cancel: no appointments found for mobile number "
                    + mobileNumber + ".";
        }
        return SUCCESS + "Cancelled " + cancelled + " appointment(s) for mobile number "
                + mobileNumber + ".";
    }
}





