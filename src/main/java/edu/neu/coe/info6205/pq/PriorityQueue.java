package edu.neu.coe.info6205.pq;

import edu.neu.coe.info6205.util.Benchmark_Timer;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class PriorityQueue<K> implements Iterable<K> {
    public PriorityQueue(boolean max, Object[] binHeap, int first, int last, Comparator<K> comparator, boolean floyd, int d) {
        this.max = max;
        this.first = first;
        this.comparator = comparator;
        this.last = last;
        @SuppressWarnings("unchecked")
        K[] tempHeap = (K[]) binHeap;
        this.binHeap = tempHeap;
        this.floyd = floyd;
        this.d = d;
    }

    // Constructors with default arity (binary heap, d = 2)
    public PriorityQueue(boolean max, Object[] binHeap, int first, int last, Comparator<K> comparator, boolean floyd) {
        this(max, binHeap, first, last, comparator, floyd, 2);
    }

    public PriorityQueue(int n, int first, boolean max, Comparator<K> comparator, boolean floyd, int d) {
        this(max, new Object[n + first], first, 0, comparator, floyd, d);
    }

    public PriorityQueue(int n, int first, boolean max, Comparator<K> comparator, boolean floyd) {
        this(n, first, max, comparator, floyd, 2);
    }

    public PriorityQueue(int n, boolean max, Comparator<K> comparator, boolean floyd, int d) {
        this(n, 1, max, comparator, floyd, d);
    }

    public PriorityQueue(int n, boolean max, Comparator<K> comparator, boolean floyd) {
        this(n, 1, max, comparator, floyd, 2);
    }

    public PriorityQueue(int n, boolean max, Comparator<K> comparator) {
        this(n, 1, max, comparator, false, 2);
    }

    public PriorityQueue(int n, Comparator<K> comparator) {
        this(n, 1, true, comparator, true, 2);
    }

    public boolean isEmpty() {
        return last == 0;
    }

    public int size() {
        return last;
    }

    public void give(K key) {
        if (last == binHeap.length - first)
            last--; 
        last++;
        binHeap[last + first - 1] = key;
        swimUp(last + first - 1);
    }

    public K take() throws PQException {
        if (isEmpty()) throw new PQException("Priority queue is empty");
        if (floyd) return doTake(this::snake);
        else return doTake(this::sink);
    }

    K doTake(Consumer<Integer> f) {
        K result = binHeap[first];
        swap(first, last-- + first - 1); 
        f.accept(first); 
        binHeap[last + first] = null; 
        return result;
    }

    void sink(int k) {
        int i = k;
        while (firstChild(i) <= last + first - 1) {
            int maxChild = firstChild(i);
            int end = Math.min(maxChild + d - 1, last + first - 1);
            for (int j = maxChild + 1; j <= end; j++) {
                if (unordered(maxChild, j)) {
                    maxChild = j;
                }
            }
            if (!unordered(i, maxChild)) break;
            swap(i, maxChild);
            i = maxChild;
        }
    }

    private int doHeapify(int k, BiPredicate<Integer, Integer> p) {
        int i = k;
        while (firstChild(i) <= last + first - 1) {
            int maxChild = firstChild(i);
            int end = Math.min(maxChild + d - 1, last + first - 1);
            for (int j = maxChild + 1; j <= end; j++) {
                if (unordered(maxChild, j)) {
                    maxChild = j;
                }
            }
            if (p.test(i, maxChild)) break;
            swap(i, maxChild);
            i = maxChild;
        }
        return i;
    }

    void snake(int k) {
        swimUp(doHeapify(k, (a, b) -> !unordered(a, b)));
    }

    void swimUp(int k) {
        int i = k;
        while (i > first && unordered(parent(i), i)) {
            swap(i, parent(i));
            i = parent(i);
        }
    }

    private void swap(int i, int j) {
        K tmp = binHeap[i];
        binHeap[i] = binHeap[j];
        binHeap[j] = tmp;
    }

    boolean unordered(int i, int j) {
        K elemI = binHeap[i];
        K elemJ = binHeap[j];
        if (elemI == null || elemJ == null) {
            return false; 
        }
        return (comparator.compare(elemI, elemJ) > 0) ^ max;
    }

    private int parent(int k) {
        return (k - 2 - first + d) / d + first - 1;
    }

    private int firstChild(int k) {
        return d * (k - first) + first + 1;
    }

    @SuppressWarnings("unused")
    private K peek(int k) {
        return binHeap[k];
    }

    @SuppressWarnings("unused")
    private boolean getMax() {
        return max;
    }

    private final boolean max;
    private final int first;
    private final Comparator<K> comparator;
    private final K[] binHeap; 
    private int last; 
    private final boolean floyd; 
    private final int d; // Number of children per node (arity)

    public Iterator<K> iterator() {
        Collection<K> copy = new ArrayList<>(Arrays.asList(Arrays.copyOf(binHeap, last + first)));
        Iterator<K> result = copy.iterator();
        if (first > 0 && result.hasNext()) result.next(); 
        return result;
    }

    public static void main(String[] args) {
        doMain();
    }

    static void doMain() {
        String[] s1 = new String[5]; // Created a string type array with size 5
        s1[0] = "A";
        s1[1] = "B";
        s1[2] = "C";
        s1[3] = "D";
        s1[4] = "E";
        boolean max = true;
        boolean floyd = true;

        // Using default binary heap (d = 2)
        PriorityQueue<String> PQ_string_floyd = new PriorityQueue<>(max, s1, 1, 5, Comparator.comparing(String::toString), floyd, 2);
        PriorityQueue<String> PQ_string_nofloyd = new PriorityQueue<>(max, s1, 1, 5, Comparator.comparing(String::toString), false, 2);

        Integer[] s2 = new Integer[5]; 
        for (int i = 0; i < 5; i++) {
            s2[i] = i;
        }
        PriorityQueue<Integer> PQ_int_floyd = new PriorityQueue<>(max, s2, 1, 5, Comparator.comparing(Integer::intValue), floyd, 2);
        PriorityQueue<Integer> PQ_int_nofloyd = new PriorityQueue<>(max, s2, 1, 5, Comparator.comparing(Integer::intValue), false, 2);

        int arity = 4; 
        PriorityQueue<String> PQ_string_4ary = new PriorityQueue<>(max, s1, 1, 5, Comparator.comparing(String::toString), false, arity);
        PriorityQueue<String> PQ_string_4ary_floyd = new PriorityQueue<>(max, s1, 1, 5, Comparator.comparing(String::toString), floyd, arity);

        PriorityQueue<Integer> PQ_int_4ary = new PriorityQueue<>(max, s2, 1, 5, Comparator.comparing(Integer::intValue), false, arity);
        PriorityQueue<Integer> PQ_int_4ary_floyd = new PriorityQueue<>(max, s2, 1, 5, Comparator.comparing(Integer::intValue), floyd, arity);

        performBenchmark();
    }

    static void performBenchmark() {
        int M = 4095;
        int capacity = M + 1;
        int[] insertionSizes = {2000, 4000, 8000, 16000};
        int[] removalSizes = {500, 1000, 2000, 4000};
        int trials = 10;

        Comparator<Integer> comparator = Integer::compareTo;
        boolean max = false; 

        for (int i = 0; i < insertionSizes.length; i++) {
            int insertions = insertionSizes[i];
            int removals = removalSizes[i];

            System.out.println("\nInput Size: " + insertions);

            double basicHeapTime = benchmarkHeap("Basic Binary Heap", () -> new PriorityQueue<>(capacity, max, comparator, false, 2),
                    insertions, removals, M, trials);

            double floydHeapTime = benchmarkHeap("Binary Heap with Floyd's Trick", () -> new PriorityQueue<>(capacity, max, comparator, true, 2),
                    insertions, removals, M, trials);

            double fourAryHeapTime = benchmarkHeap("4-ary Heap", () -> new PriorityQueue<>(capacity, max, comparator, false, 4),
                    insertions, removals, M, trials);

            double fourAryFloydHeapTime = benchmarkHeap("4-ary Heap with Floyd's Trick", () -> new PriorityQueue<>(capacity, max, comparator, true, 4),
                    insertions, removals, M, trials);

            System.out.printf("Basic Binary Heap: %.2f ms%n", basicHeapTime);
            System.out.printf("Binary Heap with Floyd's Trick: %.2f ms%n", floydHeapTime);
            System.out.printf("4-ary Heap: %.2f ms%n", fourAryHeapTime);
            System.out.printf("4-ary Heap with Floyd's Trick: %.2f ms%n", fourAryFloydHeapTime);
        }
    }


    static double benchmarkHeap(String description, Supplier<PriorityQueue<Integer>> heapSupplier,
                                int insertions, int removals, int M, int trials) {

        Consumer<PriorityQueue<Integer>> heapOperation = heap -> {
            List<Integer> spilledElements = new ArrayList<>();
            int highestPriority = Integer.MAX_VALUE;
            Random random = new Random();

            for (int i = 0; i < insertions; i++) {
                int element = random.nextInt();
                heap.give(element);

                if (heap.size() > M) {
                    int spilled = heap.take();
                    spilledElements.add(spilled);
                    if (spilled < highestPriority) {
                        highestPriority = spilled;
                    }
                }
            }

            for (int i = 0; i < removals; i++) {
                if (!heap.isEmpty()) {
                    int spilled = heap.take();
                    spilledElements.add(spilled);
                    if (spilled < highestPriority) {
                        highestPriority = spilled;
                    }
                }
            }
            System.out.println(description + " - Highest priority spilled element: " + highestPriority);
        };

        Benchmark_Timer<PriorityQueue<Integer>> benchmark = new Benchmark_Timer<>(
                description,
                null,
                heapOperation,
                null
        );

        return benchmark.runFromSupplier(heapSupplier, trials);
    }

    public static class PQException extends RuntimeException {
        public PQException(String message) {
            super(message);
        }
    }
}
