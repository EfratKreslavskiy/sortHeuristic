package kreslavsky.sort.test;

import kreslavsky.sort.BucketSort;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BucketSortTest
{
    @Test
    void bucketSort_Integers()
    {
        Integer[] sorted = {1, 2, 3, 4, 5};
        Integer[][] testArrays = {sorted.clone(),
                                 {5, 4, 3, 2, 1},
                                 {2, 4, 5, 3, 1}};

        for (int i = 0; i < testArrays.length; i++)
        {
            BucketSort.bucketSort(testArrays[i]);
            assertArrayEquals(testArrays[i], sorted);
        }
    }

    @Test
    void bucketSort_PrimitiveArray()
    {
        int[] ints = {5, -1, 3, 3, 0};
        BucketSort.bucketSort(ints);
        assertArrayEquals(new int[] {-1, 0, 3, 3, 5}, ints);
    }
}