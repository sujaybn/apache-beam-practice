package org.demo;

import io.grpc.internal.JsonUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ThreadDemo extends Thread {

    public void run() {
        System.out.println("Thread executing");
    }

    public static void main(String[] args) {
        List<String> list = Arrays.asList("This", "is", "a", "demo");

        List<String> result = (List<String>) list.stream().filter(s -> s.length() < 3).map(s -> s.toUpperCase());
        System.out.println(result);
    }
}

