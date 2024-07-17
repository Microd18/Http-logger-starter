package com.example.httploggerstarter.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.StopWatch;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HttpLoggerFilterTest {

    @Mock
    private FilterChain filterChain;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private HttpLoggerFilter httpLoggerFilter;

    @BeforeEach
    void setUp() {
        httpLoggerFilter = new HttpLoggerFilter();
        filterChain = org.mockito.Mockito.mock(FilterChain.class);
    }

    @Test
    void contextLoads() {
    }

    @Test
    void testDoFilterInternal() throws ServletException, IOException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        when(request.getRequestURI()).thenReturn("/api/v1/test");
        when(request.getMethod()).thenReturn("GET");
        when(request.getProtocol()).thenReturn("HTTP/1.1");
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/v1/test"));
        when(response.getStatus()).thenReturn(200);
        when(request.getHeaderNames()).thenReturn(Collections.enumeration(Collections.singletonList("Host")));
        when(request.getHeader("Host")).thenReturn("localhost:8080");

        doAnswer(invocation -> {
            responseWrapper.setStatus(200);
            responseWrapper.getWriter().write("Response body");
            return null;
        }).when(filterChain).doFilter(any(ContentCachingRequestWrapper.class), any(ContentCachingResponseWrapper.class));

        httpLoggerFilter.doFilterInternal(requestWrapper, responseWrapper, filterChain);

        verify(filterChain, times(1)).doFilter(any(ContentCachingRequestWrapper.class), any(ContentCachingResponseWrapper.class));

        String result = httpLoggerFilter.requestBuilder(requestWrapper, responseWrapper, new StopWatch());
        assert result.contains("Method: GET /api/v1/test HTTP/1.1");
        assert result.contains("Url:http://localhost:8080/api/v1/test");
        assert result.contains("Status: 200");
        assert result.contains("Response body: Response body");
    }
}
