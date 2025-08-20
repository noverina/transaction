package com.test.noverina.transaction.util;

import com.test.noverina.transaction.dto.OpenErDto;
import com.test.noverina.transaction.dto.RateCacheDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class ExchangeRateHelper {
    private final ConcurrentHashMap<String, RateCacheDto> cache = new ConcurrentHashMap<>();
    @Autowired
    private RestTemplate restTemplate;

    public BigDecimal getRate(String from, String to) {
        var requestedRate = cache.get(from);
        if (requestedRate == null || requestedRate.getLastUpdated() == null ||
           !requestedRate.getLastUpdated().isEqual(LocalDate.now())) {
           var response = restTemplate.getForObject("https://open.er-api.com/v6/latest/" + from, OpenErDto.class);
            if (response == null || response.getRates() == null || !response.getRates().containsKey(to)) {
                log.error("Unable to fetch exchange rate from currency {} to {}", from, to);
                throw new IllegalArgumentException("Exchange rate not available for currency " + from + " to " + to);
            }
            var newCache = new RateCacheDto(LocalDate.now(), response.getRates());
            cache.put(from, newCache);
        }
        return cache.get(from).getRates().get(to);
    }
}
