import java.time.DayOfWeek;

/**
 *A nurse, who takes blood samples and provides immunisations and wound care.
 *
 *@author Shuo Lu (25259133)
 */
public class Nurse extends HealthProfessional {

    private static final String DEFAULT_REGISTRATION_TYPE = "Registered Nurse";
    private String registrationType;

    public Nurse(int id, String name, DayOfWeek[] workingDays, String registrationType) {
        super(id, name, workingDays);
        this.registrationType = requireText(registrationType, "Registration type");
    }

    public Nurse(int id, String name, DayOfWeek[] workingDays) {
        this(id, name, workingDays, DEFAULT_REGISTRATION_TYPE);
    }

    public String getRegistrationType() {
        return registrationType;
    }

    public void setRegistrationType(String registrationType) {
        this.registrationType = requireText(registrationType, "Registration type");
    }

    public String takeBloodSample(String patientName) {
        if (isBlank(patientName)) {
            return "WARNING: A blood sample can only be taken from a named patient.";
        }
        return getName() + " (" + registrationType + ") took a blood sample from "
                + patientName.trim() + ".";
    }

    @Override
    public String getProfessionalType() {
        return "Nurse";
    }

    @Override
    public String describeServices() {
        return "Blood samples, immunisations, wound care and monitoring of vital signs.";
    }

    @Override
    public String toString() {
        return super.toString()
                + formatDetail("Registration", registrationType);
    }
}



