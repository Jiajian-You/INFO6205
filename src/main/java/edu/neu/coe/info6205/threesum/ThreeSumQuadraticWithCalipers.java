package edu.neu.coe.info6205.threesum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class ThreeSumQuadraticWithCalipers implements ThreeSum {
    public ThreeSumQuadraticWithCalipers(int[] ints) {
        this.a = ints;
        length = ints.length;
    }

    @Override
    public Triple[] getTriples() {
        List<Triple> triples = new ArrayList<>();
        for (int i = 0; i < length - 2; i++) {
            triples.addAll(calipers(a, i, Triple::sum));
        }
        Collections.sort(triples);
        return triples.stream().distinct().toArray(Triple[]::new);
    }

    public static List<Triple> calipers(int[] a, int i, Function<Triple, Integer> function) {
        List<Triple> triples = new ArrayList<>();
        int j = i + 1; 
        int k = a.length - 1; 
        while (j < k) {
            int[] values = {a[i], a[j], a[k]};
            Arrays.sort(values);
            Triple triple = new Triple(values[0], values[1], values[2]);
            int sum = a[i] + a[j] + a[k];
            if (sum == 0) {
                triples.add(triple);
                int currentJ = a[j];
                int currentK = a[k];
                while (j < k && a[j] == currentJ) j++;
                while (j < k && a[k] == currentK) k--;
            } else if (sum < 0) {
                j++;
            } else {
                k--;
            }
        }
        return triples;
    }
    private final int[] a;
    private final int length;
}