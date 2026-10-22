package kreslavsky.sort;

import static kreslavsky.sort.MergeSort.swap;

public class InsertionSort {
    public static <T extends Comparable<T>> void insertionSort(T[] arr)
    {
        insertionSort(arr, 0, arr.length - 1);
    }

    public static <T extends Comparable<T>> void insertionSort(T[] arr, int left, int right)
    {
        for (int i = left + 1; i <= right; ++i) //everything in [left, i) is already sorted
        {
            int ix = i;
            while (ix > left && arr[ix - 1].compareTo(arr[ix]) > 0)
            {
                swap(arr, ix - 1, ix);
                --ix;
            }
        }
    }
}
