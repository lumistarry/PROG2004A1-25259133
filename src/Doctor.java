import java.time.DayOfWeek;

/**
 * A health professional who is allowed to prescribe medication. Abstract,
 * because every doctor in the system is a particular kind of doctor.
 *
 * @author Shuo Lu (25259133)
 */
public abstract class Doctor extends HealthProfessional {

    public Doctor(int id, String name, DayOfWeek[] workingDays) {
        super(id, name, workingDays);
    }

    public Doctor(int id, String name) {
        super(id, name);
    }

    // Written once here, so every kind of doctor inherits it.
    public String prescribeMedication(String patientName, String medication) {
        if (isBlank(patientName) || isBlank(medication)) {
            return "WARNING: A prescription needs both a patient name and a medication.";
        }
        return getName() + " (" + getProfessionalType() + ") prescribed "
                + medication.trim() + " for " + patientName.trim() + ".";
    }
}
