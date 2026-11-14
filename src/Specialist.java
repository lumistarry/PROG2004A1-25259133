import java.time.DayOfWeek;

/**
 * A specialist doctor, for example a cardiologist, who usually sees patients
 * only after a referral from a GP.
 *
 * @author Shuo Lu (25259133)
 */
public class Specialist extends Doctor {

    private String specialty;
    private boolean referralRequired;

    public Specialist(int id, String name, DayOfWeek[] workingDays, String specialty,
                      boolean referralRequired) {
        super(id, name, workingDays);
        this.specialty = requireText(specialty, "Specialty");
        this.referralRequired = referralRequired;
    }

    public Specialist(int id, String name, DayOfWeek[] workingDays, String specialty) {
        this(id, name, workingDays, specialty, true);
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = requireText(specialty, "Specialty");
    }

    public boolean isReferralRequired() {
        return referralRequired;
    }

    public void setReferralRequired(boolean referralRequired) {
        this.referralRequired = referralRequired;
    }

    public String orderDiagnosticTest(String patientName, String testName) {
        if (isBlank(patientName) || isBlank(testName)) {
            return "WARNING: A test order needs both a patient name and a test name.";
        }
        return getName() + " (" + specialty + ") ordered a " + testName.trim()
                + " for " + patientName.trim() + ".";
    }

    @Override
    public String getProfessionalType() {
        return "Specialist";
    }

    @Override
    public String describeServices() {
        return "Specialist " + specialty.toLowerCase() + " assessment and treatment"
                + (referralRequired ? " (GP referral required)." : " (no referral needed).");
    }

    @Override
    public String toString() {
        return super.toString()
                + formatDetail("Specialty", specialty)
                + formatDetail("Referral required", referralRequired ? "Yes" : "No");
    }
}



