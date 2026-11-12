package kreslavsky.sort;

import java.util.Arrays;

public class TimSort
{
    private static final int MIN_MERGE = 32;

    public static <T extends Comparable<T>> void timSort(T[] arr)
    {
        int size = arr.length;
        int minRun = minRunLength(size);
        T[] buffer = arr.clone();

        // sort small chunks with insertion sort
        for (int i = 0; i < size; i += minRun)
        {
            InsertionSort.insertionSort(arr, i, Math.min(i + minRun - 1, size - 1));
        }

        // merge the chunks, doubling the chunk size each time
        for (int run = minRun; run < size; run *= 2)
        {
            for (int left = 0; left < size - run; left += 2 * run)
            {
                int mid = left + run - 1;
                int right = Math.min(left + 2 * run - 1, size - 1);

                if (arr[mid].compareTo(arr[mid + 1]) > 0)
                {
                    mergeHalves(arr, left, mid + 1, right + 1, buffer);
                }
            }
        }
    }

    private static <T extends Comparable<T>> void mergeHalves(
            T[] arr,
            int left,
            int middle,
            int end,
            T[] buffer)
    {
        System.arraycopy(arr, left, buffer, left, middle - left);
        int first = left;
        int second = middle;
        int write = left;

        while (first < middle && second < end)
        {
            if (buffer[first].compareTo(arr[second]) <= 0)
            {
                arr[write++] = buffer[first++];
            }
            else
            {
                arr[write++] = arr[second++];
            }
        }

        while (first < middle)
        {
            arr[write++] = buffer[first++];
        }
    }

    private static int minRunLength(int n)
    {
        int r = 0;
        while (n >= MIN_MERGE)
        {
            r |= n & 1;
            n >>= 1;
        }
        return n + r;
    }

}
