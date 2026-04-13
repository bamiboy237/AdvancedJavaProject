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

    public StudentProfile(String firstName, String lastName, String studentId, StudentRecord record) {
        this.firstName = firstName == null ? "" : firstName.trim();
        this.lastName = lastName == null ? "" : lastName.trim();
        this.studentId = studentId;
        this.record = record;
    }

    public StudentProfile(
        String firstName,
        String lastName,
        ProfileIndex index
    ) {
        this(firstName, lastName, StudentIdGenerator.generateNextId(index), new StudentRecord());
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName == null ? "" : firstName.trim();
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName == null ? "" : lastName.trim();
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
