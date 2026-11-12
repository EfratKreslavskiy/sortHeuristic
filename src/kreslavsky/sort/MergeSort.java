package kreslavsky.sort;

public class MergeSort
{
    public static <T extends Comparable<T>> void mergeSort(T[] arr, int start, int end) //goes from start until (not including) end
    {
        mergeSort(arr, start, end, arr.clone());
    }

    private static <T extends Comparable<T>> void mergeSort(
            T[] arr,
            int start,
            int end,
            T[] buffer)
    {
        int size = end - start;
        if (size <= 2) //base case
        {
            if (size == 2 && arr[start].compareTo(arr[start + 1]) > 0)
                swap(arr, start, start + 1);
        }
        else //recursive case
        {
            int half = start + size / 2;

            mergeSort(arr, start, half, buffer);
            mergeSort(arr, half, end, buffer);
            if (arr[half - 1].compareTo(arr[half]) > 0)
            {
                mergeHalves(arr, start, half, end, buffer);
            }
        }
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

        mergeHalves(arr, l, r, end, arr.clone());
    }

    private static <T extends Comparable<T>> void mergeHalves(
            T[] arr,
            int l,
            int r,
            int end,
            T[] buffer)
    {
        System.arraycopy(arr, l, buffer, l, r - l);
        int left = l;
        int right = r;
        int write = l;

        while (left < r && right < end)
        {
            if (buffer[left].compareTo(arr[right]) <= 0)
            {
                arr[write++] = buffer[left++];
            }
            else
            {
                arr[write++] = arr[right++];
            }
        }

        while (left < r)
        {
            arr[write++] = buffer[left++];
        }
    }
}
