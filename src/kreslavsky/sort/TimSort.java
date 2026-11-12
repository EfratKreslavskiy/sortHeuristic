package kreslavsky.sort;

public class TimSort
{
    private static final int MIN_MERGE = 32;

    public static <T extends Comparable<T>> void timSort(T[] arr)
    {
        int size = arr.length;
        if (size < 2)
        {
            return;
        }

        TimSorter<T> sorter = new TimSorter<>(arr);
        sorter.sort();
    }

    private static int minRunLength(int size)
    {
        int remainder = 0;
        while (size >= MIN_MERGE)
        {
            remainder |= size & 1;
            size >>= 1;
        }
        return size + remainder;
    }

    private static final class TimSorter<T extends Comparable<T>>
    {
        private final T[] array;
        private final int minRun;
        private final T[] temporary;
        private final int[] runBase = new int[85];
        private final int[] runLength = new int[85];
        private int stackSize;

        private TimSorter(T[] array)
        {
            this.array = array;
            minRun = minRunLength(array.length);
            temporary = array.clone();
        }

        private void sort()
        {
            int low = 0;
            int high = array.length;

            while (low < high)
            {
                int length = countRunAndMakeAscending(low, high);
                if (length < minRun)
                {
                    int forcedLength = Math.min(minRun, high - low);
                    binarySort(low, low + forcedLength, low + length);
                    length = forcedLength;
                }

                pushRun(low, length);
                mergeCollapse();
                low += length;
            }

            mergeForceCollapse();
        }

        private int countRunAndMakeAscending(int low, int high)
        {
            int runHigh = low + 1;
            if (runHigh == high)
            {
                return 1;
            }

            if (array[runHigh].compareTo(array[low]) < 0)
            {
                while (runHigh < high
                        && array[runHigh].compareTo(array[runHigh - 1]) < 0)
                {
                    runHigh++;
                }
                reverseRange(low, runHigh);
            }
            else
            {
                while (runHigh < high
                        && array[runHigh].compareTo(array[runHigh - 1]) >= 0)
                {
                    runHigh++;
                }
            }
            return runHigh - low;
        }

        private void binarySort(int low, int high, int start)
        {
            if (start == low)
            {
                start++;
            }

            for (; start < high; start++)
            {
                T pivot = array[start];
                int left = low;
                int right = start;

                while (left < right)
                {
                    int middle = (left + right) >>> 1;
                    if (pivot.compareTo(array[middle]) < 0)
                    {
                        right = middle;
                    }
                    else
                    {
                        left = middle + 1;
                    }
                }

                int moved = start - left;
                if (moved == 1)
                {
                    array[left + 1] = array[left];
                }
                else if (moved > 1)
                {
                    System.arraycopy(array, left, array, left + 1, moved);
                }
                array[left] = pivot;
            }
        }

        private void reverseRange(int low, int high)
        {
            high--;
            while (low < high)
            {
                T value = array[low];
                array[low++] = array[high];
                array[high--] = value;
            }
        }

        private void pushRun(int base, int length)
        {
            runBase[stackSize] = base;
            runLength[stackSize] = length;
            stackSize++;
        }

        private void mergeCollapse()
        {
            while (stackSize > 1)
            {
                int index = stackSize - 2;
                if (index > 0
                        && runLength[index - 1] <= runLength[index] + runLength[index + 1])
                {
                    if (runLength[index - 1] < runLength[index + 1])
                    {
                        index--;
                    }
                    mergeAt(index);
                }
                else if (runLength[index] <= runLength[index + 1])
                {
                    mergeAt(index);
                }
                else
                {
                    break;
                }
            }
        }

        private void mergeForceCollapse()
        {
            while (stackSize > 1)
            {
                int index = stackSize - 2;
                if (index > 0 && runLength[index - 1] < runLength[index + 1])
                {
                    index--;
                }
                mergeAt(index);
            }
        }

        private void mergeAt(int index)
        {
            int firstBase = runBase[index];
            int firstLength = runLength[index];
            int secondBase = runBase[index + 1];
            int secondLength = runLength[index + 1];

            runLength[index] = firstLength + secondLength;
            if (index == stackSize - 3)
            {
                runBase[index + 1] = runBase[index + 2];
                runLength[index + 1] = runLength[index + 2];
            }
            stackSize--;

            int skipLeft = gallopRight(array[secondBase], firstBase, firstLength, 0);
            firstBase += skipLeft;
            firstLength -= skipLeft;
            if (firstLength == 0)
            {
                return;
            }

            secondLength = gallopLeft(
                    array[firstBase + firstLength - 1],
                    secondBase,
                    secondLength,
                    secondLength - 1);
            if (secondLength == 0)
            {
                return;
            }

            if (firstLength <= secondLength)
            {
                mergeLow(firstBase, firstLength, secondBase, secondLength);
            }
            else
            {
                mergeHigh(firstBase, firstLength, secondBase, secondLength);
            }
        }

        private int gallopRight(T value, int base, int length, int hint)
        {
            int offset = 1;
            int lastOffset = 0;
            if (value.compareTo(array[base + hint]) > 0)
            {
                int maxOffset = length - hint;
                while (offset < maxOffset
                        && value.compareTo(array[base + hint + offset]) > 0)
                {
                    lastOffset = offset;
                    offset = (offset << 1) + 1;
                    if (offset <= 0)
                    {
                        offset = maxOffset;
                    }
                }
                if (offset > maxOffset)
                {
                    offset = maxOffset;
                }
                lastOffset += hint;
                offset += hint;
            }
            else
            {
                int maxOffset = hint + 1;
                while (offset < maxOffset
                        && value.compareTo(array[base + hint - offset]) <= 0)
                {
                    lastOffset = offset;
                    offset = (offset << 1) + 1;
                    if (offset <= 0)
                    {
                        offset = maxOffset;
                    }
                }
                if (offset > maxOffset)
                {
                    offset = maxOffset;
                }
                int temporary = lastOffset;
                lastOffset = hint - offset;
                offset = hint - temporary;
            }

            lastOffset++;
            while (lastOffset < offset)
            {
                int middle = lastOffset + ((offset - lastOffset) >>> 1);
                if (value.compareTo(array[base + middle]) > 0)
                {
                    lastOffset = middle + 1;
                }
                else
                {
                    offset = middle;
                }
            }
            return offset;
        }

        private int gallopLeft(T value, int base, int length, int hint)
        {
            int lastOffset = 0;
            int offset = 1;
            if (value.compareTo(array[base + hint]) < 0)
            {
                int maxOffset = hint + 1;
                while (offset < maxOffset
                        && value.compareTo(array[base + hint - offset]) < 0)
                {
                    lastOffset = offset;
                    offset = (offset << 1) + 1;
                    if (offset <= 0)
                    {
                        offset = maxOffset;
                    }
                }
                if (offset > maxOffset)
                {
                    offset = maxOffset;
                }
                int temporary = lastOffset;
                lastOffset = hint - offset;
                offset = hint - temporary;
            }
            else
            {
                int maxOffset = length - hint;
                while (offset < maxOffset
                        && value.compareTo(array[base + hint + offset]) >= 0)
                {
                    lastOffset = offset;
                    offset = (offset << 1) + 1;
                    if (offset <= 0)
                    {
                        offset = maxOffset;
                    }
                }
                if (offset > maxOffset)
                {
                    offset = maxOffset;
                }
                lastOffset += hint;
                offset += hint;
            }

            lastOffset++;
            while (lastOffset < offset)
            {
                int middle = lastOffset + ((offset - lastOffset) >>> 1);
                if (value.compareTo(array[base + middle]) < 0)
                {
                    offset = middle;
                }
                else
                {
                    lastOffset = middle + 1;
                }
            }
            return offset;
        }

        private void mergeLow(int firstBase, int firstLength, int secondBase, int secondLength)
        {
            System.arraycopy(array, firstBase, temporary, 0, firstLength);
            int first = 0;
            int second = secondBase;
            int destination = firstBase;

            while (first < firstLength && second < secondBase + secondLength)
            {
                if (temporary[first].compareTo(array[second]) <= 0)
                {
                    array[destination++] = temporary[first++];
                }
                else
                {
                    array[destination++] = array[second++];
                }
            }

            if (first < firstLength)
            {
                System.arraycopy(temporary, first, array, destination, firstLength - first);
            }
        }

        private void mergeHigh(int firstBase, int firstLength, int secondBase, int secondLength)
        {
            System.arraycopy(array, secondBase, temporary, 0, secondLength);
            int first = firstBase + firstLength - 1;
            int second = secondLength - 1;
            int destination = secondBase + secondLength - 1;

            while (first >= firstBase && second >= 0)
            {
                if (array[first].compareTo(temporary[second]) > 0)
                {
                    array[destination--] = array[first--];
                }
                else
                {
                    array[destination--] = temporary[second--];
                }
            }

            if (second >= 0)
            {
                System.arraycopy(temporary, 0, array, firstBase, second + 1);
            }
        }
    }
}
