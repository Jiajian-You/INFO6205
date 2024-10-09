package edu.neu.coe.info6205.sort.elementary;

import edu.neu.coe.info6205.sort.elementary.InsertionSortBasic;
import edu.neu.coe.info6205.util.Benchmark;
import edu.neu.coe.info6205.util.Benchmark_Timer;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Main {

    public static void main(String[] args) {
        new Main().runBenchmarks();
    }

    private void runBenchmarks() {
        int[] sizes = {500, 1000, 2000, 4000, 8000};
        for (int size : sizes) {
            System.out.println("Array Size: " + size);
            benchmarkSort("Random Order", size, RandomArray(size));
            benchmarkSort("Ordered", size, OrderedArray(size));
            benchmarkSort("Partially-Ordered", size, PartiallyOrderedArray(size));
            benchmarkSort("Reverse-Ordered", size, ReverseOrderedArray(size));
            System.out.println();
        }
    }

    private void benchmarkSort(String description, int size, Supplier<Integer[]> arraySupplier) {
        Benchmark<Integer[]> benchmark = new Benchmark_Timer<>(
                "InsertionSort " + description + " Array",
                array -> {
                	InsertionSortBasic<Integer> sorter = new InsertionSortBasic<>((a, b) -> a.compareTo(b));
                	sorter.sort(array);
                }
        );
        int runs = 10;
        double time = benchmark.runFromSupplier(arraySupplier, runs);
        System.out.printf("%s: Average time: %.2f ms%n", description, time);
    }

    private Supplier<Integer[]> RandomArray(int size) {
        return () -> {
            Random random = new Random();
            Integer[] array = new Integer[size];
            for(int i = 0; i < size; i++) {
                array[i] = random.nextInt();
            }
            return array;
        };
    }

    private Supplier<Integer[]> OrderedArray(int size) {
        return () -> {
            Integer[] array = new Integer[size];
            for(int i = 0; i < size; i++) {
                array[i] = i;
            }
            return array;
        };
    }

    private Supplier<Integer[]> PartiallyOrderedArray(int size) {
        return () -> {
            Integer[] array = OrderedArray(size).get();
            Random random = new Random();
            int swaps = Math.max(size / 10, 1);
            for(int i = 0; i < swaps; i++) {
                int index1 = random.nextInt(size);
                int index2 = random.nextInt(size);
                int temp = array[index1];
                array[index1] = array[index2];
                array[index2] = temp;
            }
            return array;
        };
    }

    private Supplier<Integer[]> ReverseOrderedArray(int size) {
        return () -> {
            Integer[] array = new Integer[size];
            for(int i = 0; i < size; i++) {
                array[i] = size - i;
            }
            return array;
        };
    }
}