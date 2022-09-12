package com.khekrn.eloop.service;

import com.khekrn.eloop.model.Event;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Consumer;

public final class EventLoop {

    private final ConcurrentLinkedDeque<Event<Object>> events = new ConcurrentLinkedDeque<>();

    private final ConcurrentHashMap<String, Consumer<Object>> handlers = new ConcurrentHashMap<>();

    private EventLoop() {
    }

    public static EventLoop create() {
        return new EventLoop();
    }


    public EventLoop on(String key, Consumer<Object> handler) {
        handlers.put(key, handler);
        return this;
    }

    public void dispatch(Event<Object> event) {
        events.add(event);
    }

    public void stop() {
        Thread.currentThread().interrupt();
    }

    public void run() {
        while (!(events.isEmpty() && Thread.interrupted())) {
            if (!events.isEmpty()) {
                Event<Object> event = events.pop();
                if (handlers.containsKey(event.key())) {
                    handlers.get(event.key()).accept(event.data());
                } else {
                    System.err.println("No handler for key " + event.key());
                }
            }
        }
    }
}
