package EntityClass;

// for students to submit a request to withdraw their application for an internship

public class WithdrawalRequest {

    private final String requestId;
    private final Application application;
    private final String reason;

    private boolean approved = false;
    private boolean processed = false;
    private CareerStaff processedBy;   // null until processed

    public WithdrawalRequest(String requestId, Application application, String reason) {
        this.requestId = requestId;
        this.application = application;
        this.reason = reason;
    }

    public String getRequestId() {
        return requestId;
    }

    public Application getApplication() {
        return application;
    }

    public String getReason() {
        return reason;
    }

    public boolean isApproved() {
        return approved;
    }

    public boolean isProcessed() {
        return processed;
    }

    public CareerStaff getProcessedBy() {
        return processedBy;
    }

    // hareesh add extra logic here plz
    public void submitRequest() {
    }

    public void approve(CareerStaff staff) {
        this.approved = true;
        this.processed = true;
        this.processedBy = staff;
    }

    public void reject(CareerStaff staff) {
        this.approved = false;
        this.processed = true;
        this.processedBy = staff;
    }

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
