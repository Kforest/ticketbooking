package com.darkhorse.ticketbooking.order.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class JSONUtils {
    private JSONUtils() {
    }

    public static String objectToString(Object request) {
        try {
            return new ObjectMapper().writeValueAsString(request);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
        return "";
    }

    public static <T> T stringToObject(String contentUTF8, Class<T> clazz) {
        try {
            return new ObjectMapper().readValue(contentUTF8, clazz);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
}