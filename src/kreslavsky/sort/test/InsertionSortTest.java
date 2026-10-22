package kreslavsky.sort.test;

import kreslavsky.sort.BucketSort;
import kreslavsky.sort.InsertionSort;
import kreslavsky.sort.QuickSort;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class InsertionSortTest {

    @Test
    void insertionSort_Integers()
    {
        Integer[] sorted = {1, 2, 3, 4, 5};
        Integer[][] testArrays = {sorted.clone(),
                {5, 4, 3, 2, 1},
                {2, 4, 5, 3, 1}};

        for (int i = 0; i < testArrays.length; i++)
        {
            InsertionSort.insertionSort(testArrays[i]);
            assertArrayEquals(testArrays[i], sorted);
        }
    }

    @Test
    void insertionSort_Strings()
    {
        String[] sorted = {"aa", "bb", "cc", "dd", "ee"};
        String[][] testArrays = {sorted.clone(),
                {"ee", "dd", "cc", "bb", "aa"},
                {"bb", "dd", "ee", "cc", "aa"}};

        for (int i = 0; i < testArrays.length; i++)
        {
            InsertionSort.insertionSort(testArrays[i]);
            assertArrayEquals(testArrays[i], sorted);
        }
    }

    @Test
    void insertionSort_Doubles()
    {
        Double[] sorted = {0.1, 0.2, 0.3, 0.4, 0.5};
        Double[][] testArrays = {sorted.clone(),
                {0.5, 0.4, 0.3, 0.2, 0.1},
                {0.2, 0.4, 0.5, 0.3, 0.1}};

        for (int i = 0; i < testArrays.length; i++)
        {
            InsertionSort.insertionSort(testArrays[i]);
            assertArrayEquals(testArrays[i], sorted);
        }
    }
}
