package com.khekrn.eloop.model;

public record Event<T>(String key, T data) {

    public static <T> Event<T> of(String key, T data) {
        return new Event<>(key, data);
    }
}
