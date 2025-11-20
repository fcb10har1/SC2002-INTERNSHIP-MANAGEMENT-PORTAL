package EntityClass;

// for students to submit a request to withdraw their application for an internship

public class WithdrawalRequest {

    private final String requestId;
    private final Application application;
    private final String reason;

    private boolean approved = false;
    private boolean processed = false;
    private CareerStaff processedBy;

    /**
     * Creates a new withdrawal request with the given details.
     */
    public WithdrawalRequest(String requestId, Application application, String reason) {
        this.requestId = requestId;
        this.application = application;
        this.reason = reason;
    }

    /**
     * Returns the request ID.
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * Returns the application associated with the withdrawal request.
     */
    public Application getApplication() {
        return application;
    }

    /**
     * Returns the reason for the withdrawal request.
     */
    public String getReason() {
        return reason;
    }

    /**
     * Returns whether the withdrawal request is approved.
     */
    public boolean isApproved() {
        return approved;
    }

    /**
     * Returns whether the withdrawal request has been processed.
     */
    public boolean isProcessed() {
        return processed;
    }

    /**
     * Returns the staff member who processed the withdrawal request.
     */
    public CareerStaff getProcessedBy() {
        return processedBy;
    }

    /**  
     * * Submits the withdrawal request.
     */
    public void submitRequest() {
    }

    /**  
     * Approves the withdrawal request.
     */
    public void approve(CareerStaff staff) {
        this.approved = true;
        this.processed = true;
        this.processedBy = staff;
    }

    /**  
     * Rejects the withdrawal request.
     */
    public void reject(CareerStaff staff) {
        this.approved = false;
        this.processed = true;
        this.processedBy = staff;
    }

    /**   
     * Returns a string representation of the withdrawal request.
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
