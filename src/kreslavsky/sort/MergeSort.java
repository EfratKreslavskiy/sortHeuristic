package kreslavsky.sort;

import java.util.Arrays;

public class MergeSort
{
    public static <T extends Comparable<T>> void mergeSort(T[] arr, int start, int end) //goes from start until (not including) end
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

            mergeSort(arr, start, half);
            mergeSort(arr, half, end);
            mergeHalves(arr, start, half, end);
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
        T[] leftHalf = Arrays.copyOfRange(arr, l, r);
        int left = 0;
        int right = r;
        int write = l;

        while (left < leftHalf.length && right < end)
        {
            if (leftHalf[left].compareTo(arr[right]) <= 0)
            {
                arr[write++] = leftHalf[left++];
            }
            else
            {
                arr[write++] = arr[right++];
            }
        }

        while (left < leftHalf.length)
        {
            arr[write++] = leftHalf[left++];
        }
    }
}
