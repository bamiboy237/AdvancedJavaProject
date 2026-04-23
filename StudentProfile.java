/*
 * Guy-robert Bogning
 * Student profile object for local sign in, sign up, generated student ID, and saved student record.
 */

import java.io.Serializable;
import java.util.Objects;

public class StudentProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    private String firstName;
    private String lastName;
    private String studentId;
    private StudentRecord record;

    public StudentProfile(
        String firstName,
        String lastName,
        String studentId,
        StudentRecord record
    ) {
        this.firstName = normalize(firstName);
        this.lastName = normalize(lastName);
        this.studentId = studentId;
        this.record = record;
    }

    private static String normalize(String value) {
        return (value == null) ? "" : value.trim();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = normalize(firstName);
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = normalize(lastName);
    }

    public StudentRecord getRecord() {
        return record;
    }

    public void setRecord(StudentRecord record) {
        this.record = record;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return (firstName + " " + lastName).trim();
    }

    @Override
    public String toString() {
        return studentId + " - " + getStudentName();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof StudentProfile)) {
            return false;
        }

        StudentProfile other = (StudentProfile) obj;
        return Objects.equals(studentId, other.studentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId);
    }
}
