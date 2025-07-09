package port.management.system.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import port.management.system.dto.ExpenseDTO;
import port.management.system.dto.ExpenseResponse;
import port.management.system.dto.UserDTO2;
import port.management.system.exception.ApiException;
import port.management.system.model.Expense;
import port.management.system.repository.ExpenseRepository;

import java.util.List;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Override
    @Transactional
    public ExpenseResponse getAllExpenses(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Expense> expensePage = expenseRepository.findAll(pageDetails);

        List<Expense> expenses = expensePage.getContent();

        if (expenses.isEmpty()) {

            throw new ApiException("No Expenses Found.");

        }

        List<ExpenseDTO> expenseDTOList = expenses.stream()
                .map(e -> ExpenseDTO.builder()
                        .expenseId(e.getExpenseId())
                        .paymentDate(e.getPaymentDate())
                        .amountPaid(e.getAmountPaid())
                        .userDTO2(UserDTO2.builder()
                                .userId(e.getUser().getUserId())
                                .username(e.getUser().getUsername())
                                .email(e.getUser().getEmail())
                                .role(e.getUser().getRole())
                                .mobile(e.getUser().getMobile())
                                .build())
                        .build())
                .toList();

        ExpenseResponse expenseResponse = new ExpenseResponse();

        expenseResponse.setExpenseDTOList(expenseDTOList);
        expenseResponse.setPageNumber(expensePage.getNumber());
        expenseResponse.setPageSize(expensePage.getSize());
        expenseResponse.setTotalPages(expensePage.getTotalPages());
        expenseResponse.setTotalElements(expensePage.getTotalElements());
        expenseResponse.setLastPage(expensePage.isLast());

        return expenseResponse;

    }

    @Override
    @Transactional
    public String getTotalAmount() {

        List<Expense> expenses = expenseRepository.findAll();

        if (expenses.isEmpty()) {

            throw new ApiException("No Expenses Found.");

        }

        double totalAmount = expenses.stream().mapToDouble(Expense::getAmountPaid).sum();

        return "The Total Amount Present in the Account is : " + totalAmount;

    }

}
