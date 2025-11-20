package EntityClass.Enums;

/**
 * Enum representing the status of an internship opportunity.
 */
public enum OpportunityStatus {
    /** Opportunity created and awaiting career staff approval. */
    Pending,
    /** Opportunity approved by career staff and available for student viewing (if visible). */
    Approved,
    /** Opportunity rejected by career staff and not available to students. */
    Rejected,
    /** Opportunity slots fully confirmed by accepted student applications. */
    Filled,
    /** Opportunity in draft state (minimal details, not yet submitted for approval). */
    Draft;
}
