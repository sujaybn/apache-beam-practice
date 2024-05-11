package org.demo;

import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMapExample {
    public static void main(String[] args) {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

        // Adding elements concurrently
        Runnable task1 = () -> {
            for (int i = 0; i < 1000; i++) {
                map.put("Key" + i, i);
                System.out.println("Size of ConcurrentHashMap at addition: " + map.size());
            }
        };

        // Removing elements concurrently
        Runnable task2 = () -> {
            for (int i = 0; i < 1000; i++) {
                map.remove("Key" + i);
                System.out.println("Size of ConcurrentHashMap at removal: " + map.size());
            }
        };

        Thread thread1 = new Thread(task1);
        Thread thread2 = new Thread(task2);

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Printing the size of the map
        System.out.println("Size of ConcurrentHashMap at end: " + map.size());
    }
}