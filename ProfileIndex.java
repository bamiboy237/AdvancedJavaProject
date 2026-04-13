/*
 * Guy-robert Bogning
 * Profile index for saved student profiles.
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProfileIndex implements Serializable {

    private static final long serialVersionUID = 1L;

    private final List<StudentProfile> profiles;

    public ProfileIndex() {
        profiles = new ArrayList<>();
    }

    public List<StudentProfile> getProfiles() {
        try {
            return new ArrayList<>(profiles);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve profiles", e);
        }
    }

    public void setProfiles(List<StudentProfile> newProfiles) {
        try {
            profiles.clear();
            if (newProfiles != null) {
                profiles.addAll(newProfiles);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to set profiles", e);
        }
    }

    public void addProfile(StudentProfile profile) {
        if (profile == null) {
            throw new IllegalArgumentException("Profile cannot be null");
        }
        if (profiles.contains(profile)) {
            throw new IllegalArgumentException("Profile already exists");
        }
        try {
            profiles.add(profile);
        } catch (Exception e) {
            throw new RuntimeException("Failed to add profile", e);
        }
    }

    public boolean removeProfile(StudentProfile profile) {
        if (profile == null) {
            return false;
        }
        try {
            return profiles.remove(profile);
        } catch (Exception e) {
            throw new RuntimeException("Failed to remove profile", e);
        }
    }

    public StudentProfile getProfileById(String studentId) {
        if (studentId == null) {
            return null;
        }
        try {
            for (StudentProfile profile : profiles) {
                if (studentId.equals(profile.getStudentId())) {
                    return profile;
                }
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get profile by ID", e);
        }
    }

    public StudentProfile getProfileByName(String studentName) {
        if (studentName == null) {
            return null;
        }
        try {
            for (StudentProfile profile : profiles) {
                if (studentName.equalsIgnoreCase(profile.getStudentName())) {
                    return profile;
                }
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get profile by name", e);
        }
    }

    public boolean containsId(String studentId) {
        return getProfileById(studentId) != null;
    }

    public boolean isEmpty() {
        try {
            return profiles.isEmpty();
        } catch (Exception e) {
            throw new RuntimeException("Failed to check if empty", e);
        }
    }

    public int size() {
        try {
            return profiles.size();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get size", e);
        }
    }
}
