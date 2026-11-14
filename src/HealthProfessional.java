import java.time.DayOfWeek;

/**
 * Base class for every health professional. It holds the data that is common to
 * all of them, and is abstract so that a professional with no specialty can
 * never be created.
 *
 * @author Shuo Lu (25259133)
 */
public abstract class HealthProfessional {

    private static final DayOfWeek[] DEFAULT_WORKING_DAYS = {
            DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY, DayOfWeek.FRIDAY
    };

    private static final int LABEL_WIDTH = 18;
    private final int id;
    private String name;
    private DayOfWeek[] workingDays;

    public HealthProfessional(int id, String name, DayOfWeek[] workingDays) {
        if (id <= 0) {
            throw new IllegalArgumentException("A health professional ID must be a positive number.");
        }
        this.id = id;
        this.name = requireText(name, "Name");
        this.workingDays = validateAndCopyDays(workingDays);
    }

    public HealthProfessional(int id, String name) {
        // No days were given, so use a standard Monday to Friday week.
        this(id, name, DEFAULT_WORKING_DAYS);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = requireText(name, "Name");
    }

    // Hand out a copy, so the caller cannot change the stored days.
    public DayOfWeek[] getWorkingDays() {
        return copyDays(workingDays);
    }

    public void setWorkingDays(DayOfWeek[] workingDays) {
        this.workingDays = validateAndCopyDays(workingDays);
    }

    public boolean worksOn(DayOfWeek day) {
        for (DayOfWeek workingDay : workingDays) {
            if (workingDay == day) {
                return true;
            }
        }
        return false;
    }

    public abstract String getProfessionalType();

    public abstract String describeServices();

    @Override
    public String toString() {
        return getProfessionalType() + " (ID: " + id + ")"
                + formatDetail("Name", name)
                + formatDetail("Working days", formatWorkingDays())
                + formatDetail("Services", describeServices());
    }

    protected static String formatDetail(String label, String value) {
        return String.format("\n  %-" + LABEL_WIDTH + "s: %s", label, value);
    }

    protected static String requireText(String value, String fieldName) {
        if (isBlank(value)) {
            throw new IllegalArgumentException(fieldName + " must not be empty.");
        }
        return value.trim();
    }

    protected static boolean isBlank(String text) {
        return text == null || text.trim().isEmpty();
    }

    private String formatWorkingDays() {
        String result = "";
        for (int i = 0; i < workingDays.length; i++) {
            String dayName = workingDays[i].toString();
            result += dayName.charAt(0) + dayName.substring(1).toLowerCase();
            if (i < workingDays.length - 1) {
                result += ", ";
            }
        }
        return result;
    }

    private static DayOfWeek[] validateAndCopyDays(DayOfWeek[] days) {
        if (days == null || days.length == 0) {
            throw new IllegalArgumentException("A health professional must work at least one day.");
        }
        for (int i = 0; i < days.length; i++) {
            if (days[i] == null) {
                throw new IllegalArgumentException("Working days must not contain an empty entry.");
            }
            for (int j = i + 1; j < days.length; j++) {
                if (days[i] == days[j]) {
                    throw new IllegalArgumentException("Working days must not contain duplicates.");
                }
            }
        }
        return copyDays(days);
    }

    private static DayOfWeek[] copyDays(DayOfWeek[] days) {
        DayOfWeek[] copy = new DayOfWeek[days.length];
        for (int i = 0; i < days.length; i++) {
            copy[i] = days[i];
        }
        return copy;
    }
}
