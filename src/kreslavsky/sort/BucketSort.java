package kreslavsky.sort;

public class BucketSort
{
    public static void bucketSort(Integer[] arr)
    {
        int arrSize = arr.length;
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

        int[] frequencies = new int[max - min + 1]; //allocates array of zeros
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
