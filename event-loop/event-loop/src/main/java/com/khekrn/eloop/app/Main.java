package com.khekrn.eloop.app;

import com.khekrn.eloop.model.Event;
import com.khekrn.eloop.service.EventLoop;

public class Main {

    public static void main(String[] args) {
        var eventLoop = EventLoop.create();
        new Thread(() -> {
            for (int n = 1; n <= 6; n++) {
                delay(1000);
                eventLoop.dispatch(Event.of("tick", n));
            }
            eventLoop.dispatch(Event.of("stop", null));
        }).start();

        new Thread(() -> {
            delay(2500);
            eventLoop.dispatch(Event.of("hello", "beautiful world"));
            delay(800);
            eventLoop.dispatch(Event.of("hello", "beautiful universe"));
        }).start();

        eventLoop.dispatch(Event.of("hello", "world!"));
        eventLoop.dispatch(Event.of("foo", "bar"));

        eventLoop.on("hello", s -> System.out.println("Hello " + s))
                .on("tick", t -> System.out.println("Tick # " + t))
                .on("stop", v -> eventLoop.stop())
                .run();

        System.out.println("Bye!");
    }

    private static void delay(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
