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
            int half = start + size / 2; //computing half incorrectly

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
        while (r != end)
        {
            int comparison = arr[l].compareTo(arr[r]);
            if (comparison < 0)
            {
                ++l;
            }
            else if (comparison > 0)
            {
                swap(arr, l, r);
                ++l;
                if (r != end-1 && arr[r].compareTo(arr[r+1]) > 0)
                {
                    ++r;
                }
            }
            else //they're equal
            {

                if (l + 1 != r) //r and l aren't next to each other
                {
                    ++l;
                    swap(arr, l, r);
                    ++l;
                    ++r;
                } else
                {
                    l += 2;
                    r += 2;
                }
            }

            if (l == r) //they shouldn't ever point to the same index
            {
                ++r;
            }
        }
    }
}
