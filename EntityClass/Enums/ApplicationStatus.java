package EntityClass.Enums;

/**
 * Enum representing the status of an application.
 */

public enum ApplicationStatus {
    /** Application submitted and awaiting company representative review. */
    Pending,
    /** Application approved by company representative (offer extended to student). */
    Successful,
    /** Application rejected by company representative or auto-rejected when student accepts another offer. */
    Unsuccessful,
    /** Application withdrawn by student via withdrawal request approval. */
    Withdrawn;
}
