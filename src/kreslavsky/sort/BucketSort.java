package kreslavsky.sort;

import java.util.Arrays;

public class BucketSort
{
    private static final long MAX_BUCKET_RANGE = 10_000_000L;

    public static void bucketSort(Integer[] arr)
    {
        int arrSize = arr.length;
        if (arrSize <= 1)
        {
            return;
        }

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
        if (range > Integer.MAX_VALUE
                || range > MAX_BUCKET_RANGE
                || range > Math.max(1024L, (long) arrSize * 64L))
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
}
