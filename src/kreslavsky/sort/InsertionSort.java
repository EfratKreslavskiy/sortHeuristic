package kreslavsky.sort;

public class InsertionSort {
    public static <T extends Comparable<T>> void insertionSort(T[] arr)
    {
        insertionSort(arr, 0, arr.length - 1);
    }

    public static <T extends Comparable<T>> void insertionSort(T[] arr, int left, int right)
    {
        for (int i = left + 1; i <= right; ++i) //everything in [left, i) is already sorted
        {
            T item = arr[i];
            int ix = i - 1;
            while (ix >= left && arr[ix].compareTo(item) > 0)
            {
                arr[ix + 1] = arr[ix];
                --ix;
            }
            arr[ix + 1] = item;
        }
    }
}
