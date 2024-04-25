package com.example.sbjconvert.service;

import com.example.sbjconvert.model.GeoLocationResponse;
import com.example.sbjconvert.model.RequestLog;
import com.example.sbjconvert.repository.RequestLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.example.sbjconvert.common.Constants.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestLogService {

    private final RequestLogRepository requestLogRepository;

    public void log(HttpServletRequest request, HttpServletResponse response) {
        var geoLocationDetails = (GeoLocationResponse) request.getAttribute(GEOLOCATION_DETAILS_ATTR);
        long startTime = (long) request.getAttribute(REQUEST_START_TIME_ATTR);
        long endTime = (long) request.getAttribute(REQUEST_END_TIME_ATTR);
        var requestLog = new RequestLog();

        requestLog.setIp(request.getRemoteAddr());
        requestLog.setUri(request.getRequestURI());
        requestLog.setTimeLapsed((endTime - startTime) / 1000.0);
        requestLog.setTimestamp(new java.sql.Timestamp(endTime));
        requestLog.setHttpResponseCode(response.getStatus());
        requestLog.setProvider(geoLocationDetails.getIsp());
        requestLog.setCountryCode(geoLocationDetails.getCountryCode());

        log.info("Storing RequestLog={}", requestLog);
        requestLogRepository.save(requestLog);
    }
}
