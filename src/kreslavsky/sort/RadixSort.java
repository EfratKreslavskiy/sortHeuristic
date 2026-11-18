package kreslavsky.sort;

import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

public class RadixSort
{
    public interface Key<T>
    {
        int radix();                  //how many different digit values there are (digits go from 0 to radix - 1)
        int length(T item);           //how many digits this item's key has
        int digitAt(T item, int pos); //the digit at pos, where pos 0 is the most significant
    }

    public static <T> void radixSort(T[] arr, Key<T> key)
    {
        if (arr.length <= 1)
            return;

        int maxLength = 0;
        for (T item : arr)
            maxLength = Math.max(maxLength, key.length(item));

        @SuppressWarnings("unchecked")
        T[] buffer = arr.clone();
        int[] count = new int[key.radix() + 1];
        T[] source = arr;
        T[] destination = buffer;

        for (int pos = maxLength - 1; pos >= 0; --pos) //least significant digit first
        {
            countingSortByDigit(source, destination, key, pos, count);
            T[] swap = source;
            source = destination;
            destination = swap;
        }

        if (source != arr)
            System.arraycopy(source, 0, arr, 0, arr.length);
    }

    public static void radixSort(int[] arr)
    {
        if (arr.length <= 1)
            return;
        int[] buffer = new int[arr.length];
        int[] count = new int[257];
        int[] source = arr;
        int[] destination = buffer;
        for (int byteIndex = 0; byteIndex < 4; ++byteIndex)
        {
            java.util.Arrays.fill(count, 0);
            int shift = byteIndex * 8;
            for (int value : source)
                ++count[((value ^ Integer.MIN_VALUE) >>> shift) & 0xFF];
            for (int i = 1; i < count.length; ++i)
                count[i] += count[i - 1];
            for (int i = source.length - 1; i >= 0; --i)
            {
                int digit = ((source[i] ^ Integer.MIN_VALUE) >>> shift) & 0xFF;
                destination[--count[digit]] = source[i];
            }
            int[] swap = source;
            source = destination;
            destination = swap;
        }
        if (source != arr)
            System.arraycopy(source, 0, arr, 0, arr.length);
    }

    public static void radixSort(double[] arr)
    {
        if (arr.length <= 1)
            return;
        long[] source = new long[arr.length];
        long[] destination = new long[arr.length];
        int[] count = new int[257];
        for (int i = 0; i < arr.length; ++i)
        {
            long bits = Double.doubleToLongBits(arr[i]);
            source[i] = (bits < 0) ? ~bits : bits ^ Long.MIN_VALUE;
        }
        for (int byteIndex = 0; byteIndex < 8; ++byteIndex)
        {
            java.util.Arrays.fill(count, 0);
            int shift = byteIndex * 8;
            for (long value : source)
                ++count[(int)((value >>> shift) & 0xFF)];
            for (int i = 1; i < count.length; ++i)
                count[i] += count[i - 1];
            for (int i = source.length - 1; i >= 0; --i)
            {
                int digit = (int)((source[i] >>> shift) & 0xFF);
                destination[--count[digit]] = source[i];
            }
            long[] swap = source;
            source = destination;
            destination = swap;
        }
        for (int i = 0; i < arr.length; ++i)
        {
            long fixed = source[i];
            arr[i] = Double.longBitsToDouble((fixed & Long.MIN_VALUE) != 0
                    ? fixed ^ Long.MIN_VALUE : ~fixed);
        }
    }

    private static <T> int digitOf(T item, Key<T> key, int pos)
    {
        //shift real digits up by 1 so 0 can mean "ran out of digits", which makes shorter keys sort first
        return (pos < key.length(item)) ? key.digitAt(item, pos) + 1 : 0;
    }

    private static <T> void countingSortByDigit(T[] arr, T[] output, Key<T> key, int pos, int[] count)
    {
        java.util.Arrays.fill(count, 0);

        for (T item : arr)
            ++count[digitOf(item, key, pos)];

        for (int d = 1; d < count.length; ++d)
            count[d] += count[d - 1];

        for (int i = arr.length - 1; i >= 0; --i) //backwards keeps each pass stable
        {
            int digit = digitOf(arr[i], key, pos);
            --count[digit];
            output[count[digit]] = arr[i];
        }
    }

    public static <T> Key<T> byInt(ToIntFunction<T> getter)
    {
        return new Key<T>()
        {
            public int radix()
            {
                return 256;
            }

            public int length(T item)
            {
                return 4; //4 bytes
            }

            public int digitAt(T item, int pos)
            {
                //flipping the sign bit makes negatives sort before positives
                return ((getter.applyAsInt(item) ^ Integer.MIN_VALUE) >>> (24 - 8 * pos)) & 0xFF;
            }
        };
    }

    public static <T> Key<T> byLong(ToLongFunction<T> getter)
    {
        return new Key<T>()
        {
            public int radix()
            {
                return 256;
            }

            public int length(T item)
            {
                return 8;
            }

            public int digitAt(T item, int pos)
            {
                return (int)(((getter.applyAsLong(item) ^ Long.MIN_VALUE) >>> (56 - 8 * pos)) & 0xFF);
            }
        };
    }

    public static <T> Key<T> byDouble(ToDoubleFunction<T> getter)
    {
        return new Key<T>()
        {
            public int radix()
            {
                return 256;
            }

            public int length(T item)
            {
                return 8;
            }

            public int digitAt(T item, int pos)
            {
                long bits = Double.doubleToLongBits(getter.applyAsDouble(item));
                long fixed = (bits < 0) ? ~bits : bits ^ Long.MIN_VALUE; //same bit fix as the double version
                return (int)((fixed >>> (56 - 8 * pos)) & 0xFF);
            }
        };
    }

    public static <T> Key<T> byString(Function<T, String> getter)
    {
        return new Key<T>()
        {
            public int radix()
            {
                return 65536; //a Java char has 65536 possible values
            }

            public int length(T item)
            {
                return getter.apply(item).length();
            }

            public int digitAt(T item, int pos)
            {
                return getter.apply(item).charAt(pos);
            }
        };
    }

    public static final Key<Integer> INTEGER = byInt(x -> x);
    public static final Key<Long> LONG = byLong(x -> x);
    public static final Key<Double> DOUBLE = byDouble(x -> x);
    public static final Key<String> STRING = byString(x -> x);
}
