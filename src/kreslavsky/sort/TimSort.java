package kreslavsky.sort;

public class TimSort
{
    private static final int MIN_MERGE = 32;

    public static <T extends Comparable<T>> void timSort(T[] arr)
    {
        if (arr.length < 2)
            return;
        int[] runStarts = new int[arr.length + 1];
        int runCount = 0;
        int minRun = minRunLength(arr.length);
        @SuppressWarnings("unchecked")
        T[] auxiliary = (T[]) java.lang.reflect.Array.newInstance(
                arr.getClass().getComponentType(), arr.length);

        int start = 0;
        while (start < arr.length)
        {
            runStarts[runCount++] = start;
            int end = findNaturalRunEnd(arr, start);
            boolean descending = end > start && arr[start].compareTo(arr[start + 1]) > 0;
            if (descending)
                reverseRange(arr, start, end);
            if (end - start + 1 < minRun)
                end = Math.min(start + minRun - 1, arr.length - 1);
            if (end > start)
                InsertionSort.insertionSort(arr, start, end);
            start = end + 1;
        }
        runStarts[runCount] = arr.length;

        while (runCount > 1)
        {
            int nextRunCount = 0;
            for (int i = 0; i < runCount - 1; i += 2)
            {
                int left = runStarts[i];
                int mid = runStarts[i + 1];
                int right = i + 2 < runCount ? runStarts[i + 2] : arr.length;
                MergeSort.mergeHalves(arr, auxiliary, left, mid, right);
                runStarts[nextRunCount++] = left;
            }
            if ((runCount & 1) != 0)
                runStarts[nextRunCount++] = runStarts[runCount - 1];
            runStarts[nextRunCount] = arr.length;
            runCount = nextRunCount;
        }
    }

    public static void timSort(int[] arr)
    {
        if (arr.length < 2)
            return;
        int[] runStarts = new int[arr.length + 1];
        int runCount = 0;
        int minRun = minRunLength(arr.length);
        int[] auxiliary = new int[arr.length];
        int start = 0;
        while (start < arr.length)
        {
            runStarts[runCount++] = start;
            int end = findNaturalRunEndInt(arr, start);
            if (start < end && arr[start] > arr[start + 1])
                reverseRangeInt(arr, start, end);
            if (end - start + 1 < minRun)
                end = Math.min(start + minRun - 1, arr.length - 1);
            insertionSortInt(arr, start, end);
            start = end + 1;
        }
        mergeRunsInt(arr, runStarts, runCount, auxiliary);
    }

    public static void timSort(double[] arr)
    {
        if (arr.length < 2)
            return;
        int[] runStarts = new int[arr.length + 1];
        int runCount = 0;
        int minRun = minRunLength(arr.length);
        double[] auxiliary = new double[arr.length];
        int start = 0;
        while (start < arr.length)
        {
            runStarts[runCount++] = start;
            int end = findNaturalRunEndDouble(arr, start);
            if (start < end && Double.compare(arr[start], arr[start + 1]) > 0)
                reverseRangeDouble(arr, start, end);
            if (end - start + 1 < minRun)
                end = Math.min(start + minRun - 1, arr.length - 1);
            insertionSortDouble(arr, start, end);
            start = end + 1;
        }
        mergeRunsDouble(arr, runStarts, runCount, auxiliary);
    }

    private static <T extends Comparable<T>> int findNaturalRunEnd(T[] arr, int start)
    {
        int end = start;
        if (end + 1 < arr.length)
        {
            boolean descending = arr[end].compareTo(arr[end + 1]) > 0;
            ++end;
            while (end < arr.length - 1)
            {
                int comparison = arr[end].compareTo(arr[end + 1]);
                if (descending ? comparison <= 0 : comparison > 0)
                    break;
                ++end;
            }
        }
        return end;
    }

    private static <T> void reverseRange(T[] arr, int start, int end)
    {
        while (start < end)
        {
            T temp = arr[start];
            arr[start++] = arr[end];
            arr[end--] = temp;
        }
    }

    private static int findNaturalRunEndInt(int[] arr, int start)
    {
        int end = start;
        if (end + 1 < arr.length)
        {
            boolean descending = arr[end] > arr[end + 1];
            ++end;
            while (end < arr.length - 1)
            {
                int comparison = Integer.compare(arr[end], arr[end + 1]);
                if (descending ? comparison <= 0 : comparison > 0)
                    break;
                ++end;
            }
        }
        return end;
    }

    private static int findNaturalRunEndDouble(double[] arr, int start)
    {
        int end = start;
        if (end + 1 < arr.length)
        {
            boolean descending = Double.compare(arr[end], arr[end + 1]) > 0;
            ++end;
            while (end < arr.length - 1)
            {
                int comparison = Double.compare(arr[end], arr[end + 1]);
                if (descending ? comparison <= 0 : comparison > 0)
                    break;
                ++end;
            }
        }
        return end;
    }

    private static void reverseRangeInt(int[] arr, int start, int end)
    {
        while (start < end)
        {
            int temp = arr[start];
            arr[start++] = arr[end];
            arr[end--] = temp;
        }
    }

    private static void reverseRangeDouble(double[] arr, int start, int end)
    {
        while (start < end)
        {
            double temp = arr[start];
            arr[start++] = arr[end];
            arr[end--] = temp;
        }
    }

    private static void insertionSortInt(int[] arr, int left, int right)
    {
        for (int i = left + 1; i <= right; ++i)
        {
            int item = arr[i];
            int ix = i - 1;
            while (ix >= left && arr[ix] > item)
            {
                arr[ix + 1] = arr[ix];
                --ix;
            }
            arr[ix + 1] = item;
        }
    }

    private static void insertionSortDouble(double[] arr, int left, int right)
    {
        for (int i = left + 1; i <= right; ++i)
        {
            double item = arr[i];
            int ix = i - 1;
            while (ix >= left && Double.compare(arr[ix], item) > 0)
            {
                arr[ix + 1] = arr[ix];
                --ix;
            }
            arr[ix + 1] = item;
        }
    }

    private static void mergeRunsInt(int[] arr, int[] runStarts, int runCount, int[] auxiliary)
    {
        runStarts[runCount] = arr.length;
        while (runCount > 1)
        {
            int nextRunCount = 0;
            for (int i = 0; i < runCount - 1; i += 2)
            {
                int left = runStarts[i];
                int mid = runStarts[i + 1];
                int right = i + 2 < runCount ? runStarts[i + 2] : arr.length;
                mergeHalvesInt(arr, auxiliary, left, mid, right);
                runStarts[nextRunCount++] = left;
            }
            if ((runCount & 1) != 0)
                runStarts[nextRunCount++] = runStarts[runCount - 1];
            runStarts[nextRunCount] = arr.length;
            runCount = nextRunCount;
        }
    }

    private static void mergeRunsDouble(double[] arr, int[] runStarts, int runCount, double[] auxiliary)
    {
        runStarts[runCount] = arr.length;
        while (runCount > 1)
        {
            int nextRunCount = 0;
            for (int i = 0; i < runCount - 1; i += 2)
            {
                int left = runStarts[i];
                int mid = runStarts[i + 1];
                int right = i + 2 < runCount ? runStarts[i + 2] : arr.length;
                mergeHalvesDouble(arr, auxiliary, left, mid, right);
                runStarts[nextRunCount++] = left;
            }
            if ((runCount & 1) != 0)
                runStarts[nextRunCount++] = runStarts[runCount - 1];
            runStarts[nextRunCount] = arr.length;
            runCount = nextRunCount;
        }
    }

    private static void mergeHalvesInt(int[] arr, int[] auxiliary, int left, int mid, int right)
    {
        if (arr[mid - 1] <= arr[mid])
            return;
        System.arraycopy(arr, left, auxiliary, 0, mid - left);
        int ix = 0;
        int leftEnd = mid - left;
        int rightIx = mid;
        int write = left;
        while (ix < leftEnd && rightIx < right)
            arr[write++] = auxiliary[ix] <= arr[rightIx] ? auxiliary[ix++] : arr[rightIx++];
        while (ix < leftEnd)
            arr[write++] = auxiliary[ix++];
    }

    private static void mergeHalvesDouble(double[] arr, double[] auxiliary, int left, int mid, int right)
    {
        if (Double.compare(arr[mid - 1], arr[mid]) <= 0)
            return;
        System.arraycopy(arr, left, auxiliary, 0, mid - left);
        int ix = 0;
        int leftEnd = mid - left;
        int rightIx = mid;
        int write = left;
        while (ix < leftEnd && rightIx < right)
        {
            if (Double.compare(auxiliary[ix], arr[rightIx]) <= 0)
                arr[write++] = auxiliary[ix++];
            else
                arr[write++] = arr[rightIx++];
        }
        while (ix < leftEnd)
            arr[write++] = auxiliary[ix++];
    }

    private static int minRunLength(int n)
    {
        int r = 0;
        while (n >= MIN_MERGE)
        {
            r |= n & 1;
            n >>= 1;
        }
        return n + r;
    }

}
