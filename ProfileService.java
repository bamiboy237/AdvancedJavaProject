/*
 * Guy-robert Bogning
 * Service for creating and persisting student profiles.
 */

import java.nio.file.Path;

public class ProfileService {

    private final PersistenceManager persistenceManager;
    private final Path profileIndexPath;

    public ProfileService(PersistenceManager persistenceManager, Path profileIndexPath) {
        this.persistenceManager = persistenceManager;
        this.profileIndexPath = profileIndexPath;
    }

    public StudentProfile createProfile(ProfileIndex index, String firstName, String lastName) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name cannot be empty");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be empty");
        }

        ProfileIndex safeIndex = index == null ? new ProfileIndex() : index;
        String studentId = StudentIdGenerator.generateNextId(safeIndex);
        StudentProfile profile = new StudentProfile(firstName, lastName, studentId, new StudentRecord());
        safeIndex.addProfile(profile);
        persistenceManager.saveProfileIndex(safeIndex, profileIndexPath);
        return profile;
    }
}
