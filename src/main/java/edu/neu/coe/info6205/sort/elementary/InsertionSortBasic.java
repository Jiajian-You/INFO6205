package edu.neu.coe.info6205.sort.elementary;

import java.util.Comparator;

public class InsertionSortBasic<S> {

    public static <X> InsertionSortBasic<X> create() {
        //noinspection unchecked
        return new InsertionSortBasic<>((o1, o2) ->  ((Comparable<X>) o1).compareTo(o2));
    }

    public void sort(S[] a) {
        sort(a, 0, a.length);
    }

    public void sort(S[] a, int from, int to) {
        for (int i = from + 1; i < to; i++) insert(from, i, a);
    }

    public InsertionSortBasic(Comparator<S> comparator) {
        this.comparator = comparator;
    }

    private void insert(int from, int i, S[] a) {
        // TO BE IMPLEMENTED  : implement inner loop of insertion sort using comparator
    	 for (int j = i; j > from && comparator.compare(a[j - 1], a[j]) > 0; j--) {
    	        swap(a, j - 1, j);
    	 }
        // END SOLUTION
    }

    private void swap(Object[] a, int j, int i) {
        Object temp = a[j];
        a[j] = a[i];
        a[i] = temp;
    }

    private final Comparator<S> comparator;
}