package ControlClass;

import EntityClass.Application;
import EntityClass.CareerStaff;
import EntityClass.Student;
import EntityClass.WithdrawalRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/*
 * Manages withdrawal requests including creation, approval, and rejection
 */
public class WithdrawalManager {

    private final List<WithdrawalRequest> requests = new ArrayList<>();

    /* 
     * Student requests withdrawal from an application
     */
    public WithdrawalRequest requestWithdrawal(Student student, Application application, String reason) {
        String id = UUID.randomUUID().toString();
        WithdrawalRequest request = new WithdrawalRequest(id, application, reason);
        request.submitRequest();
        requests.add(request);
        System.out.println("Withdrawal request submitted with ID: " + id);
        return request;
    }

    /* 
     * Approves a withdrawal request by a CareerStaff member
     */
    public void approveWithdrawal(CareerStaff staff, String requestId) {
        WithdrawalRequest req = findByIdOrThrow(requestId);
        if (req.isProcessed()) {
            System.out.println("Request already processed.");
            return;
        }
        req.approve(staff);

        System.out.println("Withdrawal request " + requestId + " approved by " + staff.getUserId());
    }

    /* 
     * Rejects a withdrawal request by a CareerStaff member
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

    public List<WithdrawalRequest> getAllRequests() {
        return new ArrayList<>(requests);
    }

    /*
     * Finds a withdrawal request by its ID or throws an exception if not found
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
