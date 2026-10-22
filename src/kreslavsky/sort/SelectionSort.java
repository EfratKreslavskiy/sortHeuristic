package kreslavsky.sort;

import static kreslavsky.sort.MergeSort.swap;

public class SelectionSort
{
    public static <T extends Comparable<T>> void selectionSort(T[] arr)
    {
        int size = arr.length;;
        for (int i = 0; i < size - 1; ++i)
        {
            int minIx = i;
            for (int j = i + 1; j < size; ++j)
            {
                if(arr[j].compareTo(arr[minIx]) < 0)
                {
                    minIx = j;
                }
            }

            if (minIx != i)
            {
                swap(arr, i, minIx);
            }
        }
    }
}
