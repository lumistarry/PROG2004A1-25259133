import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 *The clinic's fixed daily schedule of appointment slots. Every slot is worked
 *out from the three constants below, so the schedule can be changed in one
 *place and the rest of the system follows.
 *
 *@author Shuo Lu (25259133)
 */
public final class ClinicSchedule {

    public static final LocalTime OPENING_TIME = LocalTime.of(8, 0);

    public static final LocalTime CLOSING_TIME = LocalTime.of(16, 0);

    public static final int SLOT_LENGTH_MINUTES = 30;

    private ClinicSchedule() {
    }

    public static ArrayList<LocalTime> getTimeSlots() {
        ArrayList<LocalTime> slots = new ArrayList<>();
        LocalTime slot = OPENING_TIME;
        //Keep a slot only if the whole appointment finishes by closing time.
        while (!slot.plusMinutes(SLOT_LENGTH_MINUTES).isAfter(CLOSING_TIME)) {
            slots.add(slot);
            slot = slot.plusMinutes(SLOT_LENGTH_MINUTES);
        }
        return slots;
    }

    public static boolean isValidTimeSlot(LocalTime time) {
        return time != null && getTimeSlots().contains(time);
    }

    public static String describe() {
        return OPENING_TIME + " - " + CLOSING_TIME + ", " + SLOT_LENGTH_MINUTES
                + "-minute slots (" + getTimeSlots().size() + " slots)";
    }

    public static String formatDay(DayOfWeek day) {
        String dayName = day.toString();
        return dayName.charAt(0) + dayName.substring(1).toLowerCase();
    }
}



