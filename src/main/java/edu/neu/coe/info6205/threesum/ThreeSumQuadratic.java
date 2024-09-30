package edu.neu.coe.info6205.threesum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class ThreeSumQuadratic implements ThreeSum {
    public ThreeSumQuadratic(int[] a) {
        this.a = a;
        length = a.length;
    }
    
    public Triple[] getTriples() {
        List<Triple> triples = new ArrayList<>();
        for (int j = 0; j < length; j++) triples.addAll(getTriples(j));
        Collections.sort(triples);
        return triples.stream().distinct().toArray(Triple[]::new);
    }

    public List<Triple> getTriples(int j) {
        List<Triple> triples = new ArrayList<>();
        int i = j - 1; 
        int k = j + 1; 
        while (i >= 0 && k < length) {
            int sum = a[i] + a[j] + a[k];
            if (sum == 0) {
                int[] values = {a[i], a[j], a[k]};
                Arrays.sort(values);
                triples.add(new Triple(values[0], values[1], values[2]));
                int currentI = a[i];
                int currentK = a[k];
                while (i >= 0 && a[i] == currentI) i--;
                while (k < length && a[k] == currentK) k++;
            } else if (sum < 0) {
                k++;
            } else {
                i--;
            }
        }
        return triples;
    }
    private final int[] a;
    private final int length;
}