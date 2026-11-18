package kreslavsky.sort;

import static kreslavsky.sort.MergeSort.swap;

public class QuickSort {
    private static final int INSERTION_SORT_CUTOFF = 16;

    public static <T extends Comparable<T>> void quickSort(T[] arr, int start, int end) {
        while (end - start > 1)
        {
            if (end - start <= INSERTION_SORT_CUTOFF)
            {
                InsertionSort.insertionSort(arr, start, end - 1);
                return;
            }
            int lt = start;
            int ix = start;
            int gt = end - 1;
            int mid = start + (end - start) / 2;
            if (arr[mid].compareTo(arr[start]) < 0) swap(arr, mid, start);
            if (arr[end - 1].compareTo(arr[start]) < 0) swap(arr, end - 1, start);
            if (arr[mid].compareTo(arr[end - 1]) < 0) swap(arr, mid, end - 1);
            T pivot = arr[end - 1];

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

            int leftEnd = lt;
            int rightStart = gt + 1;
            if (leftEnd - start < end - rightStart)
            {
                quickSort(arr, start, leftEnd);
                start = rightStart;
            }
            else
            {
                quickSort(arr, rightStart, end);
                end = leftEnd;
            }
        }
    }

    public static void quickSort(int[] arr)
    {
        quickSort(arr, 0, arr.length);
    }

    public static void quickSort(int[] arr, int start, int end)
    {
        while (end - start > 1)
        {
            if (end - start <= INSERTION_SORT_CUTOFF)
            {
                insertionSortInt(arr, start, end - 1);
                return;
            }
            int lt = start;
            int ix = start;
            int gt = end - 1;
            int mid = start + (end - start) / 2;
            if (arr[mid] < arr[start]) swapInt(arr, mid, start);
            if (arr[end - 1] < arr[start]) swapInt(arr, end - 1, start);
            if (arr[mid] < arr[end - 1]) swapInt(arr, mid, end - 1);
            int pivot = arr[end - 1];
            while (ix <= gt)
            {
                if (arr[ix] < pivot) swapInt(arr, lt++, ix++);
                else if (arr[ix] > pivot) swapInt(arr, ix, gt--);
                else ++ix;
            }
            int leftSize = lt - start;
            int rightSize = end - (gt + 1);
            if (leftSize < rightSize)
            {
                quickSort(arr, start, lt);
                start = gt + 1;
            }
            else
            {
                quickSort(arr, gt + 1, end);
                end = lt;
            }
        }
    }

    public static void quickSort(double[] arr)
    {
        quickSort(arr, 0, arr.length);
    }

    public static void quickSort(double[] arr, int start, int end)
    {
        while (end - start > 1)
        {
            if (end - start <= INSERTION_SORT_CUTOFF)
            {
                insertionSortDouble(arr, start, end - 1);
                return;
            }
            int lt = start;
            int ix = start;
            int gt = end - 1;
            int mid = start + (end - start) / 2;
            if (Double.compare(arr[mid], arr[start]) < 0) swapDouble(arr, mid, start);
            if (Double.compare(arr[end - 1], arr[start]) < 0) swapDouble(arr, end - 1, start);
            if (Double.compare(arr[mid], arr[end - 1]) < 0) swapDouble(arr, mid, end - 1);
            double pivot = arr[end - 1];
            while (ix <= gt)
            {
                int comparison = Double.compare(arr[ix], pivot);
                if (comparison < 0) swapDouble(arr, lt++, ix++);
                else if (comparison > 0) swapDouble(arr, ix, gt--);
                else ++ix;
            }
            int leftSize = lt - start;
            int rightSize = end - (gt + 1);
            if (leftSize < rightSize)
            {
                quickSort(arr, start, lt);
                start = gt + 1;
            }
            else
            {
                quickSort(arr, gt + 1, end);
                end = lt;
            }
        }
    }

    private static void insertionSortInt(int[] arr, int left, int right)
    {
        for (int i = left + 1; i <= right; ++i)
        {
            int item = arr[i];
            int ix = i - 1;
            while (ix >= left && arr[ix] > item)
            {
                arr[ix + 1] = arr[ix];
                --ix;
            }
            arr[ix + 1] = item;
        }
    }

    private static void insertionSortDouble(double[] arr, int left, int right)
    {
        for (int i = left + 1; i <= right; ++i)
        {
            double item = arr[i];
            int ix = i - 1;
            while (ix >= left && Double.compare(arr[ix], item) > 0)
            {
                arr[ix + 1] = arr[ix];
                --ix;
            }
            arr[ix + 1] = item;
        }
    }

    private static void swapInt(int[] arr, int ix1, int ix2)
    {
        int temp = arr[ix1];
        arr[ix1] = arr[ix2];
        arr[ix2] = temp;
    }

    private static void swapDouble(double[] arr, int ix1, int ix2)
    {
        double temp = arr[ix1];
        arr[ix1] = arr[ix2];
        arr[ix2] = temp;
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
