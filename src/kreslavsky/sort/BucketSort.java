package kreslavsky.sort;

import java.util.Arrays;

public class BucketSort
{
    public static void bucketSort(Integer[] arr)
    {
        int arrSize = arr.length;
        if (arrSize <= 1)
            return;

        int max = arr[0];
        int min = arr[0];

        for (int i = 1; i < arrSize; i++)
        {
            if (arr[i] > max)
            {
                max = arr[i];
            } else if (arr[i] < min)
            {
                min = arr[i];
            }
        }

        long range = (long) max - min + 1;
        if (!shouldUseBuckets(arrSize, range))
        {
            Arrays.sort(arr);
            return;
        }

        int[] frequencies = new int[(int) range]; //allocates array of zeros
        int freqSize = frequencies.length;

        for (int i = 0; i < arrSize; i++)
        {
            int n = arr[i];
            frequencies[n - min] += 1;
        }

        int ix = 0;
        for (int i = 0; i < freqSize; i++)
        {
            if (frequencies[i] > 0)
            {
                for (int j = 0; j < frequencies[i]; j++)
                {
                    arr[ix++] = i + min;
                }
            }
        }
    }

    public static void bucketSort(int[] arr)
    {
        int arrSize = arr.length;
        if (arrSize <= 1)
            return;

        int max = arr[0];
        int min = arr[0];
        for (int i = 1; i < arrSize; ++i)
        {
            if (arr[i] > max)
                max = arr[i];
            else if (arr[i] < min)
                min = arr[i];
        }

        long range = (long) max - min + 1;
        if (!shouldUseBuckets(arrSize, range))
        {
            Arrays.sort(arr);
            return;
        }

        int[] frequencies = new int[(int) range];
        for (int value : arr)
            ++frequencies[value - min];

        int ix = 0;
        for (int i = 0; i < frequencies.length; ++i)
        {
            for (int j = 0; j < frequencies[i]; ++j)
                arr[ix++] = i + min;
        }
    }

    private static boolean shouldUseBuckets(int length, long range)
    {
        if (range > Integer.MAX_VALUE)
            return false;
        long memoryBytes = range * Integer.BYTES;
        return range <= (long) length * 8 && memoryBytes <= 16L * 1024 * 1024;
    }
}
