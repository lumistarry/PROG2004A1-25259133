/**
 * A patient who books appointments. Immutable, so the details of a patient can
 * never become invalid after the object is created.
 *
 * @author Shuo Lu (25259133)
 */
public class Patient {

    private static final int MOBILE_LENGTH = 10;
    private static final String MOBILE_PREFIX = "04";
    private final String name;
    private final String mobileNumber;

    public Patient(String name, String mobileNumber) {
        if (!isValidName(name)) {
            throw new IllegalArgumentException("Patient name must not be empty.");
        }
        if (!isValidMobileNumber(mobileNumber)) {
            throw new IllegalArgumentException("Mobile number must be 10 digits starting with 04.");
        }
        this.name = name.trim();
        this.mobileNumber = normaliseMobileNumber(mobileNumber);
    }

    public String getName() {
        return name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public boolean hasMobileNumber(String otherMobileNumber) {
        return otherMobileNumber != null
                && mobileNumber.equals(normaliseMobileNumber(otherMobileNumber));
    }

    public static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty();
    }

    public static boolean isValidMobileNumber(String mobileNumber) {
        if (mobileNumber == null) {
            return false;
        }
        String digits = normaliseMobileNumber(mobileNumber);
        if (digits.length() != MOBILE_LENGTH || !digits.startsWith(MOBILE_PREFIX)) {
            return false;
        }
        for (int i = 0; i < digits.length(); i++) {
            if (!Character.isDigit(digits.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    // Ignore spaces, so "0412 345 678" and "0412345678" are one number.
    private static String normaliseMobileNumber(String mobileNumber) {
        return mobileNumber.replace(" ", "");
    }

    @Override
    public String toString() {
        return name + " (mobile: " + mobileNumber + ")";
    }
}
