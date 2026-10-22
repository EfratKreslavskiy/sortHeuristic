package kreslavsky.sort.test;

import kreslavsky.sort.MergeSort;
import kreslavsky.sort.QuickSort;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class QuickSortTest {
    @Test
    void quickSort_Integers()
    {
        Integer[] sorted = {1, 2, 3, 4, 5};
        Integer[][] testArrays = {sorted.clone(),
                {5, 4, 3, 2, 1},
                {2, 4, 5, 3, 1}};

        for (int i = 0; i < testArrays.length; i++)
        {
            QuickSort.quickSort(testArrays[i], 0, testArrays[i].length);
            assertArrayEquals(testArrays[i], sorted);
        }
    }

    @Test
    void quickSort_Strings()
    {
        String[] sorted = {"aa", "bb", "cc", "dd", "ee"};
        String[][] testArrays = {sorted.clone(),
                {"ee", "dd", "cc", "bb", "aa"},
                {"bb", "dd", "ee", "cc", "aa"}};

        for (int i = 0; i < testArrays.length; i++)
        {
            QuickSort.quickSort(testArrays[i], 0, testArrays[i].length);
            assertArrayEquals(testArrays[i], sorted);
        }
    }

    @Test
    void quickSort_Doubles()
    {
        Double[] sorted = {0.1, 0.2, 0.3, 0.4, 0.5};
        Double[][] testArrays = {sorted.clone(),
                {0.5, 0.4, 0.3, 0.2, 0.1},
                {0.2, 0.4, 0.5, 0.3, 0.1}};

        for (int i = 0; i < testArrays.length; i++)
        {
            QuickSort.quickSort(testArrays[i], 0, testArrays[i].length);
            assertArrayEquals(testArrays[i], sorted);
        }
    }
}
