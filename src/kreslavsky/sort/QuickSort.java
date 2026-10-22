package kreslavsky.sort;

import static kreslavsky.sort.MergeSort.swap;

public class QuickSort {
    public static <T extends Comparable<T>> void quickSort(T[] arr, int start, int end) {
        if (end - start <= 1)
        {
            return;
        }

        int lt = start;
        int ix = start;
        int gt = end - 1;
        T pivot = arr[start + (end - start) / 2];

        while (ix <= gt)
        {
            int comparison = arr[ix].compareTo(pivot);
            if (comparison < 0)
            {
                swap(arr, lt++, ix++);
            }
            else if (comparison > 0)
            {
                swap(arr, ix, gt--);
            }
            else
            {
                ++ix;
            }
        }

        quickSort(arr, start, lt);
        quickSort(arr, gt + 1, end);
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
