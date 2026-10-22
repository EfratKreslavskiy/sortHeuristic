package kreslavsky.sort;

import static kreslavsky.sort.MergeSort.swap;

public class QuickSort {
    public static <T extends Comparable<T>> void quickSort(T[] arr, int start, int end) {
        int size = end - start;
        if (size <= 2) //base case
        {
            if (size == 2 && arr[start].compareTo(arr[start + 1]) > 0) {
                swap(arr, start, start + 1);
            }
        }
        else //recursive case
        {
            int pivotIx = partition(arr, start, end); //pivot ends up in its final sorted spot

            quickSort(arr, start, pivotIx);
            quickSort(arr, pivotIx + 1, end); //skip the pivot, it's already in place
        }

    }

    public static <T extends Comparable<T>> int partition(T[] arr, int start, int end)
    {
        int mid = start + (end - start) / 2;
        swap(arr, mid, end -1); //place the pivot at the end, out of the way
        T pivot = arr[end - 1];

        int boundary = start; //everything in [start, boundary) is less than the pivot
        for (int i = start; i < end - 1; ++i)
        {
            if(arr[i].compareTo(pivot) < 0)
            {
                swap(arr, i, boundary);
                ++boundary;
            }
        }

        swap(arr, boundary, end - 1); //drop the pivot into its final position
        return boundary;
    }
}
