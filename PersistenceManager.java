/*
 * Guy-robert Bogning
 * Persistence manager for saving and loading profile index and student profile data.
 */

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class PersistenceManager {

    private Path dataDir;

    public PersistenceManager(Path dataDirectory) {
        this.dataDir = dataDirectory;
        if (!Files.exists(dataDir)) {
            try {
                Files.createDirectories(dataDir);
            } catch (Exception e) {
                throw new RuntimeException(
                    "Failed to create data directory",
                    e
                );
            }
        }
    }

    public void saveProfileIndex(ProfileIndex index, Path filePath) {
        try (
            ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(filePath.toFile())
            )
        ) {
            oos.writeObject(index);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save profile index", e);
        }
    }

    public ProfileIndex loadProfileIndex(Path filePath) {
        if (!Files.exists(filePath)) {
            return new ProfileIndex();
        }
        try (
            ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(filePath.toFile())
            )
        ) {
            return (ProfileIndex) ois.readObject();
        } catch (Exception e) {
            throw new RuntimeException("Failed to load profile index", e);
        }
    }

    public void saveStudentProfile(StudentProfile profile, Path filePath) {
        try (
            ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(filePath.toFile())
            )
        ) {
            oos.writeObject(profile);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save student profile", e);
        }
    }

    public StudentProfile loadStudentProfile(Path filePath) {
        if (!Files.exists(filePath)) {
            return null;
        }
        try (
            ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(filePath.toFile())
            )
        ) {
            return (StudentProfile) ois.readObject();
        } catch (Exception e) {
            throw new RuntimeException("Failed to load student profile", e);
        }
    }
}
