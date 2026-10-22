package kreslavsky.sort.test;

import kreslavsky.sort.TimSort;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class TimSortTest {

    @Test
    void timSort_Integers()
    {
        Integer[] sorted = {1, 2, 3, 4, 5};
        Integer[][] testArrays = {sorted.clone(),
                {5, 4, 3, 2, 1},
                {2, 4, 5, 3, 1}};

        for (int i = 0; i < testArrays.length; i++)
        {
            TimSort.timSort(testArrays[i]);
            assertArrayEquals(testArrays[i], sorted);
        }
    }

    @Test
    void timSort_Strings()
    {
        String[] sorted = {"aa", "bb", "cc", "dd", "ee"};
        String[][] testArrays = {sorted.clone(),
                {"ee", "dd", "cc", "bb", "aa"},
                {"bb", "dd", "ee", "cc", "aa"}};

        for (int i = 0; i < testArrays.length; i++)
        {
            TimSort.timSort(testArrays[i]);
            assertArrayEquals(testArrays[i], sorted);
        }
    }

    @Test
    void timSort_Doubles()
    {
        Double[] sorted = {0.1, 0.2, 0.3, 0.4, 0.5};
        Double[][] testArrays = {sorted.clone(),
                {0.5, 0.4, 0.3, 0.2, 0.1},
                {0.2, 0.4, 0.5, 0.3, 0.1}};

        for (int i = 0; i < testArrays.length; i++)
        {
            TimSort.timSort(testArrays[i]);
            assertArrayEquals(testArrays[i], sorted);
        }
    }
}
