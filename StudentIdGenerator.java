/*
 * Guy-robert Bogning
 * Student ID generator for local student profiles.
 */

public final class StudentIdGenerator {

    private static final String PREFIX = "STU-";
    private static final int START_NUMBER = 1001;

    private StudentIdGenerator() {}

    public static String generateNextId(ProfileIndex index) {
        int highestNumber = START_NUMBER - 1;

        if (index != null) {
            for (StudentProfile profile : index.getProfiles()) {
                int currentNumber = extractNumericPart(profile.getStudentId());
                if (currentNumber > highestNumber) {
                    highestNumber = currentNumber;
                }
            }
        }

        return PREFIX + (highestNumber + 1);
    }

    private static int extractNumericPart(String studentId) {
        if (studentId == null || !studentId.startsWith(PREFIX)) {
            return START_NUMBER - 1;
        }

        try {
            return Integer.parseInt(studentId.substring(PREFIX.length()));
        } catch (NumberFormatException ex) {
            return START_NUMBER - 1;
        }
    }
}
