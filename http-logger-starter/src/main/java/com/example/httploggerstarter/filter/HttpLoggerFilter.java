package com.example.httploggerstarter.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Enumeration;

/**
 * Фильтр для логирования HTTP-запросов и ответов.
 * <p>
 * Этот фильтр используется для захвата и логирования информации о запросах и ответах, включая метод запроса, URL, заголовки, тело запроса и ответа, статус ответа и время выполнения запроса.
 * Фильтр оборачивает запрос и ответ в {@link ContentCachingRequestWrapper} и {@link ContentCachingResponseWrapper} соответственно для возможности чтения содержимого.
 * </p>
 *
 * @see OncePerRequestFilter
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class HttpLoggerFilter extends OncePerRequestFilter {

    /**
     * Основной метод фильтра, который обрабатывает запрос и логирует информацию.
     * <p>
     * Этот метод оборачивает исходный запрос и ответ в {@link ContentCachingRequestWrapper} и {@link ContentCachingResponseWrapper} соответственно.
     * Запускает таймер для измерения времени обработки запроса, вызывает следующий фильтр в цепочке и затем логирует информацию о запросе и ответе.
     * </p>
     *
     * @param request      исходный HTTP-запрос
     * @param response     исходный HTTP-ответ
     * @param filterChain  цепочка фильтров для обработки запроса
     * @throws ServletException если возникает ошибка при обработке запроса
     * @throws IOException      если возникает ошибка ввода-вывода
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        StopWatch stopWatch = new StopWatch();

        stopWatch.start();
        filterChain.doFilter(requestWrapper, responseWrapper);
        stopWatch.stop();

        log.info(requestBuilder(requestWrapper, responseWrapper, stopWatch));

        responseWrapper.copyBodyToResponse();
    }

    /**
     * Создает строковое представление логируемой информации о запросе и ответе.
     * <p>
     * В этом методе собирается информация о запросе и ответе, включая заголовки, тело запроса и ответа, статус ответа и время выполнения запроса.
     * </p>
     *
     * @param requestWrapper обернутый запрос
     * @param responseWrapper обернутый ответ
     * @param stopWatch таймер для измерения времени обработки запроса
     * @return строка с логируемой информацией о запросе и ответе
     * @throws IOException если возникает ошибка при декодировании тела запроса или ответа
     */
    protected String requestBuilder(ContentCachingRequestWrapper requestWrapper, ContentCachingResponseWrapper responseWrapper, StopWatch stopWatch) throws IOException {

        String requestBody;
        String responseBody;

        try {
            requestBody = new String(requestWrapper.getContentAsByteArray(), requestWrapper.getCharacterEncoding());
            responseBody = new String(responseWrapper.getContentAsByteArray(), requestWrapper.getCharacterEncoding());
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedEncodingException();
        }

        StringBuilder requestBuilder = new StringBuilder();

        requestBuilder
                .append("\n\nMethod: ")
                .append(requestWrapper.getMethod())
                .append(" ")
                .append(requestWrapper.getRequestURI())
                .append(" ")
                .append(requestWrapper.getProtocol())
                .append("\nUrl:")
                .append(requestWrapper.getRequestURL())
                .append("\nHeaders: \n");

        Enumeration<String> headerRequestNames = requestWrapper.getHeaderNames();

        while (headerRequestNames.hasMoreElements()) {
            String headerName = headerRequestNames.nextElement();
            requestBuilder
                    .append("\t\\_ ")
                    .append(headerName)
                    .append(": ")
                    .append(requestWrapper.getHeader(headerName))
                    .append("\n");
        }

        requestBuilder
                .append("\nRequest body: ")
                .append(requestBody)
                .append("\nHeaders:\n");

        for (String headerName : responseWrapper.getHeaderNames()) {
            Collection<String> headerValues = responseWrapper.getHeaders(headerName);
            for (String headerValue : headerValues) {
                requestBuilder.append("\t\\_ ").append(headerName).append(": ").append(headerValue).append("\n");
            }
        }
        requestBuilder
                .append("\nResponse body: ")
                .append(responseBody)
                .append("\nStatus: ")
                .append(responseWrapper.getStatus())
                .append("\nTotal time: ")
                .append(stopWatch.getTotalTimeMillis())
                .append(" ms\n");

        return requestBuilder.toString();
    }
}
