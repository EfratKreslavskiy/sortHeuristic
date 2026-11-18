package kreslavsky.sort;

public class MergeSort
{
    private static final int INSERTION_SORT_CUTOFF = 16;

    public static <T extends Comparable<T>> void mergeSort(T[] arr, int start, int end) //goes from start until (not including) end
    {
        if (arr == null || end <= start)
            return;
        @SuppressWarnings("unchecked")
        T[] auxiliary = (T[]) java.lang.reflect.Array.newInstance(
                arr.getClass().getComponentType(), end - start);
        mergeSort(arr, auxiliary, start, end);
    }

    private static <T extends Comparable<T>> void mergeSort(T[] arr, T[] auxiliary, int start, int end)
    {
        int size = end - start;
        if (size <= INSERTION_SORT_CUTOFF)
        {
            InsertionSort.insertionSort(arr, start, end - 1);
            return;
        }

        int half = start + size / 2;
        mergeSort(arr, auxiliary, start, half);
        mergeSort(arr, auxiliary, half, end);
        mergeHalves(arr, auxiliary, start, half, end);
    }

    public static void mergeSort(int[] arr)
    {
        mergeSort(arr, 0, arr.length);
    }

    public static void mergeSort(int[] arr, int start, int end)
    {
        if (arr == null || end <= start)
            return;
        mergeSortInt(arr, new int[end - start], start, end);
    }

    private static void mergeSortInt(int[] arr, int[] auxiliary, int start, int end)
    {
        int size = end - start;
        if (size <= INSERTION_SORT_CUTOFF)
        {
            insertionSortInt(arr, start, end - 1);
            return;
        }

        int half = start + size / 2;
        mergeSortInt(arr, auxiliary, start, half);
        mergeSortInt(arr, auxiliary, half, end);
        mergeHalvesInt(arr, auxiliary, start, half, end);
    }

    public static void mergeSort(double[] arr)
    {
        mergeSort(arr, 0, arr.length);
    }

    public static void mergeSort(double[] arr, int start, int end)
    {
        if (arr == null || end <= start)
            return;
        mergeSortDouble(arr, new double[end - start], start, end);
    }

    private static void mergeSortDouble(double[] arr, double[] auxiliary, int start, int end)
    {
        int size = end - start;
        if (size <= INSERTION_SORT_CUTOFF)
        {
            insertionSortDouble(arr, start, end - 1);
            return;
        }

        int half = start + size / 2;
        mergeSortDouble(arr, auxiliary, start, half);
        mergeSortDouble(arr, auxiliary, half, end);
        mergeHalvesDouble(arr, auxiliary, start, half, end);
    }

    public static <T> void swap(T[] arr, int ix1, int ix2)
    {
        T temp = arr[ix1];
        arr[ix1] = arr[ix2];
        arr[ix2] = temp;
    }

    public static <T extends Comparable<T>> void mergeHalves(T[] arr, int l, int r, int end)
    {
        if (r <= l || r >= end || arr[r - 1].compareTo(arr[r]) <= 0)
        {
            return;
        }
        @SuppressWarnings("unchecked")
        T[] auxiliary = (T[]) java.lang.reflect.Array.newInstance(
                arr.getClass().getComponentType(), r - l);
        mergeHalves(arr, auxiliary, l, r, end);
    }

    public static <T extends Comparable<T>> void mergeHalves(T[] arr, T[] auxiliary, int l, int r, int end)
    {
        if (r <= l || r >= end || arr[r - 1].compareTo(arr[r]) <= 0)
            return;

        System.arraycopy(arr, l, auxiliary, 0, r - l);
        int left = 0;
        int leftEnd = r - l;
        int right = r;
        int write = l;
        while (left < leftEnd && right < end)
            arr[write++] = auxiliary[left].compareTo(arr[right]) <= 0
                    ? auxiliary[left++] : arr[right++];
        while (left < leftEnd)
            arr[write++] = auxiliary[left++];
    }

    private static void mergeHalvesInt(int[] arr, int[] auxiliary, int l, int r, int end)
    {
        if (r <= l || r >= end || arr[r - 1] <= arr[r])
            return;
        System.arraycopy(arr, l, auxiliary, 0, r - l);
        int left = 0;
        int leftEnd = r - l;
        int right = r;
        int write = l;
        while (left < leftEnd && right < end)
            arr[write++] = auxiliary[left] <= arr[right] ? auxiliary[left++] : arr[right++];
        while (left < leftEnd)
            arr[write++] = auxiliary[left++];
    }

    private static void mergeHalvesDouble(double[] arr, double[] auxiliary, int l, int r, int end)
    {
        if (r <= l || r >= end || arr[r - 1] <= arr[r])
            return;
        System.arraycopy(arr, l, auxiliary, 0, r - l);
        int left = 0;
        int leftEnd = r - l;
        int right = r;
        int write = l;
        while (left < leftEnd && right < end)
            arr[write++] = Double.compare(auxiliary[left], arr[right]) <= 0 ? auxiliary[left++] : arr[right++];
        while (left < leftEnd)
            arr[write++] = auxiliary[left++];
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
}
