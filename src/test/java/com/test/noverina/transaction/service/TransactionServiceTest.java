package com.test.noverina.transaction.service;

import com.test.noverina.transaction.dto.TransactionListDto;
import com.test.noverina.transaction.entity.Account;
import com.test.noverina.transaction.entity.User;
import com.test.noverina.transaction.enums.Role;
import com.test.noverina.transaction.enums.TransactionType;
import com.test.noverina.transaction.exception.BadLogicException;
import com.test.noverina.transaction.repository.AccountRepository;
import com.test.noverina.transaction.repository.TransactionRepository;
import com.test.noverina.transaction.util.ExchangeRateHelper;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
import java.time.OffsetDateTime;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepo;

    @Mock
    private AccountRepository accountRepo;

    @Mock
    private ExchangeRateHelper rateHelper;

    @InjectMocks
    private TransactionService service;

    private final String accountId = "test-account-id";
    private final int page = 1;
    private final int pageSize = 10;

    private User mockUser;

    @BeforeEach
    void setUpSecurityContext() {
        mockUser = mock(User.class);
        when(mockUser.getUserId()).thenReturn("mock-user-id");
        when(mockUser.getRole()).thenReturn(Role.USER);

        var auth = new UsernamePasswordAuthenticationToken(mockUser, null, List.of());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);

        var mockAccount = mock(Account.class);
        when(mockAccount.getUser()).thenReturn(mockUser);
        when(accountRepo.findById(accountId)).thenReturn(Optional.of(mockAccount));
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void findAll_shouldReturnTransactionsAndTotals_whenTransactionsExist() throws AccessDeniedException, AccessDeniedException {
        var year = 2025;
        var month = 8;

        var pageable = PageRequest.of(page - 1, pageSize, Sort.by("date").descending());
        var start = YearMonth.of(year, month).atDay(1).atStartOfDay(ZoneOffset.UTC).toOffsetDateTime();
        var end = YearMonth.of(year, month).plusMonths(1).atDay(1).atStartOfDay(ZoneOffset.UTC).toOffsetDateTime();

        var amount = Money.of(100, "USD");
        var transaction = new TransactionListDto("tx-001", OffsetDateTime.now(), TransactionType.INCOMING, amount, "IBAN123", "EUR", "Test transaction");

        var pageResult = new PageImpl<>(List.of(transaction), pageable, 1);

        when(transactionRepo.findAllByAccountWithinAMonth(accountId, start, end, pageable)).thenReturn(pageResult);
        when(rateHelper.getRate("USD", "EUR")).thenReturn(BigDecimal.valueOf(0.9));

        var result = service.findAll(accountId, month, year, page, pageSize);

        assertEquals(1, result.getContent().size());
        assertEquals("EUR 90", result.getCredit());
        assertEquals("EUR 0", result.getDebit());
        assertEquals("tx-001", result.getContent().getFirst().getTransactionId());
    }

    @Test
    void findAll_shouldReturnEmpty_whenNoTransactionFound() throws AccessDeniedException {
        var year = 2025;
        var month = 8;

        var pageable = PageRequest.of(page - 1, pageSize, Sort.by("date").descending());
        var start = YearMonth.of(year, month).atDay(1).atStartOfDay(ZoneOffset.UTC).toOffsetDateTime();
        var end = YearMonth.of(year, month).plusMonths(1).atDay(1).atStartOfDay(ZoneOffset.UTC).toOffsetDateTime();

        var emptyPage = new PageImpl<TransactionListDto>(List.of(), pageable, 0);

        when(transactionRepo.findAllByAccountWithinAMonth(accountId, start, end, pageable)).thenReturn(emptyPage);

        var result = service.findAll(accountId, month, year, page, pageSize);

        assertTrue(result.getContent().isEmpty());
        assertEquals("0", result.getDebit());
        assertEquals("0", result.getCredit());
        assertEquals(0, result.getTotalElements());
    }

    @Test
    void findAll_shouldThrowException_whenDateIsMoreThan10Years() {
        var year = OffsetDateTime.now().minusYears(11).getYear();
        var month = OffsetDateTime.now().getMonthValue();

        assertThrows(BadLogicException.class, () -> {
            service.findAll(accountId, year, month, page, pageSize);
        });
    }
}
