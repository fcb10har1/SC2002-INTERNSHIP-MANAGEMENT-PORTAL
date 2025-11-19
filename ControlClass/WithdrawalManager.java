package ControlClass;

import EntityClass.Application;
import EntityClass.CareerStaff;
import EntityClass.Student;
import EntityClass.WithdrawalRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

// for creation and approving//rejecting withdrawal requests
public class WithdrawalManager {

    private final List<WithdrawalRequest> requests = new ArrayList<>();

    // student requests withdrawal
    public WithdrawalRequest requestWithdrawal(Student student, Application application, String reason) {
        String id = UUID.randomUUID().toString();
        WithdrawalRequest request = new WithdrawalRequest(id, application, reason);
        request.submitRequest();
        requests.add(request);
        System.out.println("Withdrawal request submitted with ID: " + id);
        return request;
    }

    // career staff approves!
    public void approveWithdrawal(CareerStaff staff, String requestId) {
        WithdrawalRequest req = findByIdOrThrow(requestId);
        if (req.isProcessed()) {
            System.out.println("Request already processed.");
            return;
        }
        req.approve(staff);

        System.out.println("Withdrawal request " + requestId + " approved by " + staff.getUserId());
    }

    // career staff rejects!
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
