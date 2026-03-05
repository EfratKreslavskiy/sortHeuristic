import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SortingMethodsTest
{

    @Test
    void mergeSort_Integers()
    {
        //given
        Integer[] sorted = {1, 2, 3, 4, 5};
        Integer[][] testArrays = {sorted.clone(),
                                 {5, 4, 3, 2, 1},
                                 {2, 4, 5, 3, 1}};

        for (int i = 0; i < testArrays.length; i++)
        {
            SortingMethods.mergeSort(testArrays[i], 0, testArrays[i].length);
            assertArrayEquals(testArrays[i], sorted);
        }
    }

    @Test
    void mergeSort_Strings()
    {
        //given
        String[] sorted = {"aa", "bb", "cc", "dd", "ee"};
        String[][] testArrays = {sorted.clone(),
                {"ee", "dd", "cc", "bb", "aa"},
                {"bb", "dd", "ee", "cc", "aa"}};

        for (int i = 0; i < testArrays.length; i++)
        {
            SortingMethods.mergeSort(testArrays[i], 0, testArrays[i].length);
            assertArrayEquals(testArrays[i], sorted);
        }
    }
}
