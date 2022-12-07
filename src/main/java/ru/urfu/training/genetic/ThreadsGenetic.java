package ru.urfu.training.genetic;

import lombok.AllArgsConstructor;
import ru.urfu.network.Network;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.*;

@AllArgsConstructor
public class ThreadsGenetic {
    private final int THREADS_COUNT;
    private final List<Network> CHAMPIONS = new ArrayList<>();

    public void threadsTraining() {
        if (THREADS_COUNT > Runtime.getRuntime().availableProcessors()) {
            throw new IllegalArgumentException("Threads count can't be greater than available processors");
        }
        ExecutorService executor = Executors.newFixedThreadPool(THREADS_COUNT);
        List<Callable<Network>> tasks = new ArrayList<>();
        for (int i = 0; i < THREADS_COUNT; i++) {
            tasks.add(() -> {
                Genetic genetic = new Genetic(100, 300, 30);
                return genetic.training();
            });
        }
        List<Future<Network>> futures = new ArrayList<>();
        try {
            futures = executor.invokeAll(tasks);
        } catch (InterruptedException e) {
            System.out.println("Some problems occurred while waiting for tasks to complete " + e.getMessage());
        } finally {
            executor.shutdown();
        }
        if (futures.size() == THREADS_COUNT) {
            for (Future<Network> future : futures) {
                try {
                    CHAMPIONS.add(future.get());
                } catch (InterruptedException | ExecutionException e) {
                    System.out.println("Future not completed " + e.getMessage());
                }
            }
        }
        CHAMPIONS.stream()
                .forEach(System.out::println);
    }

    public void threadsTesting() {
        System.out.println("TESTING");
        CHAMPIONS.sort(Comparator.comparingDouble(Network::getERROR));
        Genetic.testing(CHAMPIONS.get(0));
    }

}
