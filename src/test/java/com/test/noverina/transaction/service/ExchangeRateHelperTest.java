package com.test.noverina.transaction.service;

import com.test.noverina.transaction.dto.OpenErDto;
import com.test.noverina.transaction.dto.RateCacheDto;
import com.test.noverina.transaction.util.ExchangeRateHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExchangeRateHelperTest {

    @InjectMocks
    private ExchangeRateHelper exchangeRateHelper;

    @Mock
    private RestTemplate restTemplate;

    private final String FROM = "GBP";
    private final String TO = "EUR";
    private final BigDecimal RATE = new BigDecimal("1.158014");

    @Test
    void shouldFetchAndCacheRateWhenCacheIsEmpty() {
        var rates = Map.of(TO, RATE);
        var response = new OpenErDto();
        response.setRates(rates);

        when(restTemplate.getForObject("https://open.er-api.com/v6/latest/" + FROM, OpenErDto.class))
                .thenReturn(response);

        var rate = exchangeRateHelper.getRate(FROM, TO);

        assertEquals(RATE, rate);
        assertEquals(RATE, exchangeRateHelper.getRate(FROM, TO));
        verify(restTemplate, times(1)).getForObject(anyString(), eq(OpenErDto.class));
    }

    @Test
    void shouldReturnCachedRateWhenCacheIsFresh() {
        var rates = Map.of(TO, RATE);
        var cached = new RateCacheDto(LocalDate.now(), rates);

        // Manually inject cache
        var cacheField = ReflectionTestUtils.getField(exchangeRateHelper, "cache");
        ((ConcurrentHashMap<String, RateCacheDto>) cacheField).put(FROM, cached);

        var result = exchangeRateHelper.getRate(FROM, TO);

        assertEquals(RATE, result);
        verify(restTemplate, never()).getForObject(anyString(), eq(OpenErDto.class));
    }

    @Test
    void shouldThrowExceptionWhenResponseIsInvalid() {
        when(restTemplate.getForObject("https://open.er-api.com/v6/latest/" + FROM, OpenErDto.class))
                .thenReturn(null); // Simulate failure

        assertThrows(IllegalArgumentException.class, () -> exchangeRateHelper.getRate(FROM, TO));
    }
}