package com.test.noverina.transaction.service;

import com.test.noverina.transaction.dto.PagedResponseDto;
import com.test.noverina.transaction.dto.TransactionListDto;
import com.test.noverina.transaction.dto.TransactionListViewDto;
import com.test.noverina.transaction.enums.TransactionType;
import com.test.noverina.transaction.exception.BadLogicException;
import com.test.noverina.transaction.repository.TransactionRepository;
import com.test.noverina.transaction.util.ExchangeRateHelper;
import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.money.MonetaryAmount;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository repo;
    @Autowired
    private ExchangeRateHelper rateHelper;
    public record Totals(String debit, String credit) {}

    public PagedResponseDto<TransactionListViewDto> findAll(String accountId, int month, int year, Integer page, Integer pageSize) {
        var pageable = PageRequest.of(page - 1, pageSize, Sort.by("date").descending());
        var filterDate = YearMonth.of(year, month);
        var start = filterDate.atDay(1).atStartOfDay(ZoneOffset.UTC).toOffsetDateTime();
        if (start.isBefore(OffsetDateTime.now().minusYears(10))) {
            throw new BadLogicException("Max. 10 years for date range");
        }
        var end = filterDate.plusMonths(1).atDay(1).atStartOfDay(ZoneOffset.UTC).toOffsetDateTime();

        var queryResult = repo.findAllByAccountWithinAMonth(accountId, start, end, pageable);
        var queryList = queryResult.stream().toList();
        var totals = calculateTransaction(queryList);
        var mappedAmount = queryResult.map(dto -> new TransactionListViewDto(
                        dto.getTransactionId(),
                        dto.getTransactionDate(),
                        formatAmount(dto.getAmount()),
                        dto.getIban(),
                        dto.getDescription()
                ))
                .toList();
        return new PagedResponseDto<>(mappedAmount, totals.debit, totals.credit, queryResult.getNumber(), queryResult.getSize(), queryResult.getTotalElements(), queryResult.getTotalPages(), queryResult.isLast());
    }


    private String formatAmount(MonetaryAmount amount) {
        var currency = amount.getCurrency();
        var number = amount.getNumber().numberValue(BigDecimal.class);
        return currency.getCurrencyCode() + " " + number.stripTrailingZeros().toPlainString();
    }

    private Totals calculateTransaction(List<TransactionListDto> transactions) {
        if (transactions.isEmpty()) return new Totals("0", "0");
        var target = transactions.getFirst().getAccountCurrency();
        var totalDebit = Money.of(0, target);
        var totalCredit = Money.of(0, target);

        for (var transaction : transactions) {
            var amount = transaction.getAmount();
            var from = amount.getCurrency().getCurrencyCode();
            var rate = rateHelper.getRate(from, target);
            var convertedAmount = amount.getNumber().numberValue(BigDecimal.class).multiply(rate);
            var money = Money.of(convertedAmount, target);
            if (transaction.getType() == TransactionType.INCOMING) {
                totalCredit = totalCredit.add(money);
            } else {
                totalDebit = totalDebit.add(money);
            }

        }
        return new Totals(formatAmount(totalDebit), formatAmount(totalCredit));
    }
}
