package com.learning.quizeapp.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class TestUtil {
    public static <T> ResponseEntity<T> getOkResponse(T t) {
        return new ResponseEntity<>(t, HttpStatus.OK);
    }
}
