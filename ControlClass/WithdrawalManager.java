package ControlClass;

import EntityClass.Application;
import EntityClass.CareerStaff;
import EntityClass.Student;
import EntityClass.WithdrawalRequest;
import EntityClass.InternshipOpportunity;
import EntityClass.Enums.OpportunityStatus;
import EntityClass.Enums.ApplicationStatus;
import RepositoryClass.IApplicationRepository;
import RepositoryClass.IOpportunityRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Manages withdrawal requests lifecycle: creation, approval, rejection and side-effects
 * on applications and opportunities.
 */
public class WithdrawalManager {

    private final List<WithdrawalRequest> requests = new ArrayList<>();
    private final IApplicationRepository applicationRepository;
    private final IOpportunityRepository opportunityRepository;

    /**
     * Constructs a WithdrawalManager with required repositories.
     * @param opportunityRepository opportunity repository
     * @param applicationRepository application repository
     */
    public WithdrawalManager(IOpportunityRepository opportunityRepository, IApplicationRepository applicationRepository) {
        this.opportunityRepository = opportunityRepository;
        this.applicationRepository = applicationRepository;
    }

    /**
     * Constructs a WithdrawalManager with no repositories (legacy support - limited functionality).
     */
    public WithdrawalManager() {
        this.opportunityRepository = null;
        this.applicationRepository = null;
    }

    /**
     * Creates a withdrawal request for the given application by the student.
     * Prevents duplicates if a pending/approved request already exists.
     * @param student the student requesting withdrawal
     * @param application the target application
     * @param reason explanatory reason
     * @return created WithdrawalRequest
     * @throws IllegalStateException if a request already exists
     */
    public WithdrawalRequest requestWithdrawal(Student student, Application application, String reason) {
        // Prevent duplicate or redundant withdrawal if one is pending or already approved
        boolean blocked = requests.stream().anyMatch(r -> r.getApplication().equals(application) && (!r.isProcessed() || r.isApproved()));
        if (blocked) {
            throw new IllegalStateException("A withdrawal request for this application is already pending or was approved.");
        }
        String id = UUID.randomUUID().toString();
        WithdrawalRequest request = new WithdrawalRequest(id, application, reason);
        request.submitRequest();
        requests.add(request);
        System.out.println("Withdrawal request submitted with ID: " + id);
        return request;
    }

    /**
     * Approves a pending withdrawal request performing application/opportunity side-effects.
     * @param staff approving career staff member
     * @param requestId withdrawal request identifier
     */
    public void approveWithdrawal(CareerStaff staff, String requestId) {
        WithdrawalRequest req = findByIdOrThrow(requestId);
        if (req.isProcessed()) {
            System.out.println("Request already processed.");
            return;
        }
        req.approve(staff);

        // Side-effects: mark application withdrawn (set unsuccessful) & update opportunity state
        Application app = req.getApplication();
        if (app.getStatus() == ApplicationStatus.Successful) {
            app.setStatus(ApplicationStatus.Withdrawn); // mark withdrawn distinctly
            app.revokeStudentAcceptance();
            if (applicationRepository != null) applicationRepository.update(app);
            InternshipOpportunity opp = app.getTarget();
            // If opportunity was previously filled and now has free slots, revert to Approved
            if (opp.getStatus() == OpportunityStatus.Filled && opp.confirmedCount() < opp.getSlotCap()) {
                opp.setStatus(OpportunityStatus.Approved);
                if (opportunityRepository != null) opportunityRepository.update(opp);
            }
        }
        System.out.println("Withdrawal request " + requestId + " approved by " + staff.getUserId());
    }

    /**
     * Rejects a pending withdrawal request.
     * @param staff rejecting career staff member
     * @param requestId withdrawal request identifier
     */
    public void rejectWithdrawal(CareerStaff staff, String requestId) {
        WithdrawalRequest req = findByIdOrThrow(requestId);
        if (req.isProcessed()) {
            System.out.println("Request already processed.");
            return;
        }
        req.reject(staff);
        System.out.println("Withdrawal request " + requestId + " rejected by " + staff.getUserId());
    }

    /**
     * Returns a snapshot list of all withdrawal requests (processed and pending).
     * @return list of requests
     */
    public List<WithdrawalRequest> getAllRequests() {
        return new ArrayList<>(requests);
    }

    /**
     * Finds a withdrawal request by ID or throws if absent.
     * @param requestId request identifier
     * @return matching WithdrawalRequest
     * @throws IllegalArgumentException if not found
     */
    private WithdrawalRequest findByIdOrThrow(String requestId) {
        Optional<WithdrawalRequest> opt = requests.stream()
                .filter(r -> r.getRequestId().equals(requestId))
                .findFirst();
        if (!opt.isPresent()) {
            throw new IllegalArgumentException("No withdrawal request with id: " + requestId);
        }
        return opt.get();
    }
}
