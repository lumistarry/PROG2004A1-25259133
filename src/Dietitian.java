import java.time.DayOfWeek;

/**
 * A dietitian, who gives nutrition advice and writes meal plans.
 *
 * @author Shuo Lu (25259133)
 */
public class Dietitian extends HealthProfessional {

    private static final String DEFAULT_FOCUS_AREA = "General nutrition";
    private String focusArea;

    public Dietitian(int id, String name, DayOfWeek[] workingDays, String focusArea) {
        super(id, name, workingDays);
        this.focusArea = requireText(focusArea, "Focus area");
    }

    public Dietitian(int id, String name, DayOfWeek[] workingDays) {
        this(id, name, workingDays, DEFAULT_FOCUS_AREA);
    }

    public String getFocusArea() {
        return focusArea;
    }

    public void setFocusArea(String focusArea) {
        this.focusArea = requireText(focusArea, "Focus area");
    }

    public String createMealPlan(String patientName, String goal) {
        if (isBlank(patientName) || isBlank(goal)) {
            return "WARNING: A meal plan needs both a patient name and a goal.";
        }
        return getName() + " created a meal plan for " + patientName.trim()
                + " with the goal: " + goal.trim() + ".";
    }

    @Override
    public String getProfessionalType() {
        return "Dietitian";
    }

    @Override
    public String describeServices() {
        return "Nutrition assessments, dietary advice and personalised meal plans.";
    }

    @Override
    public String toString() {
        return super.toString()
                + formatDetail("Focus area", focusArea);
    }
}



