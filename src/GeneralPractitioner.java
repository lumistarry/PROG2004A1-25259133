import java.time.DayOfWeek;

/**
 * A general practitioner: general consultations, prescriptions and referrals
 * to specialists.
 *
 * @author Shuo Lu (25259133)
 */
public class GeneralPractitioner extends Doctor {

    private boolean bulkBilling;

    public GeneralPractitioner(int id, String name, DayOfWeek[] workingDays, boolean bulkBilling) {
        super(id, name, workingDays);
        this.bulkBilling = bulkBilling;
    }

    public GeneralPractitioner(int id, String name, boolean bulkBilling) {
        super(id, name);
        this.bulkBilling = bulkBilling;
    }

    public boolean isBulkBilling() {
        return bulkBilling;
    }

    public void setBulkBilling(boolean bulkBilling) {
        this.bulkBilling = bulkBilling;
    }

    public String issueReferral(String patientName, String specialty) {
        if (isBlank(patientName) || isBlank(specialty)) {
            return "WARNING: A referral needs both a patient name and a specialty.";
        }
        return getName() + " referred " + patientName.trim() + " to a "
                + specialty.trim() + " specialist.";
    }

    @Override
    public String getProfessionalType() {
        return "General Practitioner";
    }

    @Override
    public String describeServices() {
        return "General consultations, health checks, prescriptions and referrals to specialists.";
    }

    @Override
    public String toString() {
        return super.toString()
                + formatDetail("Bulk billing", bulkBilling ? "Yes" : "No");
    }
}



