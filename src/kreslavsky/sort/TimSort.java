package kreslavsky.sort;

public class TimSort
{
    private static final int MIN_MERGE = 32;

    public static <T extends Comparable<T>> void timSort(T[] arr)
    {
        int size = arr.length;
        int minRun = minRunLength(size);

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
                    MergeSort.mergeHalves(arr, left, mid + 1, right + 1);
                }
            }
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
