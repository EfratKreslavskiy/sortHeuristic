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

        for (int pos = maxLength - 1; pos >= 0; --pos) //least significant digit first
        {
            countingSortByDigit(arr, key, pos);
        }
    }

    private static <T> int digitOf(T item, Key<T> key, int pos)
    {
        //shift real digits up by 1 so 0 can mean "ran out of digits", which makes shorter keys sort first
        return (pos < key.length(item)) ? key.digitAt(item, pos) + 1 : 0;
    }

    private static <T> void countingSortByDigit(T[] arr, Key<T> key, int pos)
    {
        T[] output = arr.clone();
        int[] count = new int[key.radix() + 1];

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

        System.arraycopy(output, 0, arr, 0, arr.length);
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
