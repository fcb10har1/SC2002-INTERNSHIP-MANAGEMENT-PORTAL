package EntityClass;


/**
 * Represents a student's request to withdraw an existing application.
 */
public class WithdrawalRequest {

    private final String requestId;
    private final Application application;
    private final String reason;

    private boolean approved = false;
    private boolean processed = false;
    private CareerStaff processedBy;

    /**
     * Creates a new withdrawal request.
     * @param requestId unique id
     * @param application application to withdraw
     * @param reason rationale for withdrawal
     */
    public WithdrawalRequest(String requestId, Application application, String reason) {
        this.requestId = requestId;
        this.application = application;
        this.reason = reason;
    }

    /**
     * Returns request id.
     * @return request id
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * Returns application being withdrawn.
     * @return application instance
     */
    public Application getApplication() {
        return application;
    }

    /**
     * Returns withdrawal reason.
     * @return reason text
     */
    public String getReason() {
        return reason;
    }

    /**
     * Indicates approval status.
     * @return true if approved
     */
    public boolean isApproved() {
        return approved;
    }

    /**
     * Indicates whether request processed.
     * @return true if processed
     */
    public boolean isProcessed() {
        return processed;
    }

    /**
     * Returns processing staff member if processed.
     * @return staff or null
     */
    public CareerStaff getProcessedBy() {
        return processedBy;
    }

    /**
     * Submits the withdrawal request (placeholder - no logic yet).
     */
    public void submitRequest() {
    }

    /**
     * Approves the withdrawal request.
     * @param staff staff member approving
     */
    public void approve(CareerStaff staff) {
        this.approved = true;
        this.processed = true;
        this.processedBy = staff;
    }

    /**
     * Rejects the withdrawal request.
     * @param staff staff member rejecting
     */
    public void reject(CareerStaff staff) {
        this.approved = false;
        this.processed = true;
        this.processedBy = staff;
    }

    /**
     * Returns string summary of request.
     * @return formatted string
     */
    @Override
    public String toString() {
        String status = !processed ? "PENDING" : (approved ? "APPROVED" : "REJECTED");
        return "WithdrawalRequest{" +
                "requestId='" + requestId + '\'' +
                ", application=" + application +
                ", reason='" + reason + '\'' +
                ", status=" + status +
                ", processedBy=" + (processedBy != null ? processedBy.getUserId() : "N/A") +
                '}';
    }
}
