package port.management.system.service;

import port.management.system.dto.ExpenseResponse;

public interface ExpenseService {

    ExpenseResponse getAllExpenses(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    String  getTotalAmount();

}
