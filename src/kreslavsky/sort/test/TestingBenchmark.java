package kreslavsky.sort.test;

import kreslavsky.sort.BucketSort;
import kreslavsky.sort.InsertionSort;
import kreslavsky.sort.MergeSort;
import kreslavsky.sort.QuickSort;
import kreslavsky.sort.RadixSort;
import kreslavsky.sort.SelectionSort;
import kreslavsky.sort.TimSort;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

public class TestingBenchmark {
    private static final int[] SIZES = createSizes();
    private static final String[] ORDERS = {
            "random",
            "already sorted",
            "reversed",
            "nearly sorted",
            "few runs"
    };
    private static final String[] DUPLICATES = {
            "none",
            "heavy",
            "all equal"
    };
    private static final String[] SIGNS = {
            "positive only",
            "mixed negative and positive"
    };
    private static final int MEASUREMENTS = 20;
    private static final int WARMUP_RUNS = 20;
    private static final long MAX_BUCKET_RANGE = 10_000_000L;
    private static final Path OUTPUT = Paths.get("results", "results.csv");

    public static void main(String[] args) throws IOException {
        Files.createDirectories(OUTPUT.getParent());
        try (BufferedWriter writer = Files.newBufferedWriter(
                OUTPUT,
                StandardCharsets.UTF_8)) {
            writer.write("Order,type, size, duplicates, sign, sort, runtime, built-in-sort runtime, runNumber");
            writer.newLine();
            Set<String> warmedTypes = new HashSet<String>();

            for (String type : typeNames()) {
                for (int size : SIZES) {
                    for (String order : ORDERS) {
                        for (String duplicates : DUPLICATES) {
                            if ("all equal".equals(duplicates) && !"random".equals(order)) {
                                continue;
                            }
                            for (String sign : signsFor(type, duplicates)) {
                                runCase(writer, type, size, order, duplicates, sign, warmedTypes);
                            }
                        }
                    }
                }
            }
        }
    }

    private static void runCase(
            BufferedWriter writer,
            String type,
            int size,
            String order,
            String duplicates,
            String sign,
            Set<String> warmedTypes) throws IOException {
        Object data = generate(type, size, order, duplicates, sign);
        List<SortSpec> sorts = applicableSorts(type);
        int rotation = Math.floorMod(caseHash(type, size, order, duplicates, sign), sorts.size());
        Collections.rotate(sorts, rotation);

        if (warmedTypes.add(type)) {
            warmUp(data, sorts);
        }
        Object builtInReference = cloneArray(data);
        java.util.Arrays.sort((Comparable[]) builtInReference);
        System.gc();
        for (int runNumber = 1; runNumber <= MEASUREMENTS; runNumber++) {
            long builtInRuntime;
            builtInRuntime = timedMeasurement(data, array ->
                    java.util.Arrays.sort((Comparable[]) array)).runtime;

            writeRow(
                    writer,
                    order,
                    type,
                    size,
                    duplicates,
                    sign,
                    "Arrays.sort",
                    Long.toString(builtInRuntime),
                    Long.toString(builtInRuntime),
                    runNumber);

            for (SortSpec sort : sorts) {
                String runtime;
                try {
                    Measurement measurement = timedMeasurement(data, sort::sort);
                    if (!isSortedLike(measurement.firstResult, builtInReference)) {
                        runtime = "WRONG:" + measurement.runtime;
                    } else {
                        runtime = Long.toString(measurement.runtime);
                    }
                } catch (Throwable throwable) {
                    runtime = "CRASH:" + throwable.getClass().getSimpleName();
                }

                String sortName = sort.name;
                if ("BucketSort".equals(sort.name)
                        && bucketUsesArraysSort((Integer[]) data)) {
                    sortName = "BucketSort (Arrays.sort fallback)";
                }
                writeRow(
                        writer,
                        order,
                        type,
                        size,
                        duplicates,
                        sign,
                        sortName,
                        runtime,
                        Long.toString(builtInRuntime),
                        runNumber);
            }
        }
    }

    private static boolean bucketUsesArraysSort(Integer[] array) {
        int min = array[0];
        int max = array[0];
        for (int i = 1; i < array.length; i++) {
            min = Math.min(min, array[i]);
            max = Math.max(max, array[i]);
        }
        long range = (long) max - min + 1;
        return range > Integer.MAX_VALUE
                || range > MAX_BUCKET_RANGE
                || range > Math.max(1024L, (long) array.length * 64L);
    }

    private static void warmUp(Object data, List<SortSpec> sorts) {
        Object warmupData = resizeForWarmup(data);
        for (SortSpec sort : sorts) {
            for (int run = 0; run < WARMUP_RUNS; run++) {
                Object copy = cloneArray(warmupData);
                try {
                    sort.sort(copy);
                } catch (Throwable ignored) {
                    break;
                }
            }
        }
    }

    private static Measurement timedMeasurement(Object source, ArraySorter sorter) {
        int batchSize = sourceLength(source) <= 100
                ? 1000
                : sourceLength(source) <= 1000 ? 10 : 1;
        Object[] copies = new Object[batchSize];
        for (int i = 0; i < batchSize; i++) {
            copies[i] = cloneArray(source);
        }
        Object firstResult = null;
        long start = System.nanoTime();
        for (int i = 0; i < batchSize; i++) {
            Object copy = copies[i];
            sorter.sort(copy);
            if (firstResult == null) {
                firstResult = copy;
            }
        }
        return new Measurement((System.nanoTime() - start) / batchSize, firstResult);
    }

    private static void writeRow(
            BufferedWriter writer,
            String order,
            String type,
            int size,
            String duplicates,
            String sign,
            String sort,
            String runtime,
            String builtInRuntime,
            int runNumber) throws IOException {
        writer.write(csv(order));
        writer.write(',');
        writer.write(csv(type));
        writer.write(',');
        writer.write(Integer.toString(size));
        writer.write(',');
        writer.write(csv(duplicates));
        writer.write(',');
        writer.write(csv(sign));
        writer.write(',');
        writer.write(csv(sort));
        writer.write(',');
        writer.write(runtime);
        writer.write(',');
        writer.write(builtInRuntime);
        writer.write(',');
        writer.write(Integer.toString(runNumber));
        writer.newLine();
    }

    private static String csv(String value) {
        if (value == null || value.isEmpty()) {
            return "";
        }
        if (value.indexOf(',') >= 0 || value.indexOf('"') >= 0) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    private static Object generate(
            String type,
            int size,
            String order,
            String duplicates,
            String sign) {
        Random random = new Random(42L);
        int[] values = generateValues(size, order, duplicates, sign, random);
        if ("Integer".equals(type)) {
            Integer[] result = new Integer[size];
            for (int i = 0; i < size; i++) {
                result[i] = values[i];
            }
            return result;
        }
        if ("Double".equals(type)) {
            Double[] result = new Double[size];
            for (int i = 0; i < size; i++) {
                result[i] = (double) values[i];
            }
            return result;
        }
        if ("String".equals(type)) {
            String[] result = new String[size];
            for (int i = 0; i < size; i++) {
                result[i] = String.format(Locale.ROOT, "%010d", values[i]);
            }
            return result;
        }
        OrderedObject[] result = new OrderedObject[size];
        for (int i = 0; i < size; i++) {
            result[i] = new OrderedObject(values[i]);
        }
        return result;
    }

    private static int[] generateValues(
            int size,
            String order,
            String duplicates,
            String sign,
            Random random) {
        int[] values = new int[size];
        boolean mixed = "mixed negative and positive".equals(sign);
        boolean allEqual = "all equal".equals(duplicates);
        int distinctValues = Math.max(1, Math.min(10, size));

        for (int i = 0; i < size; i++) {
            if (allEqual) {
                values[i] = mixed ? -7 : 7;
            } else if ("none".equals(duplicates)) {
                int value = i + 1;
                values[i] = mixed && (i & 1) == 0 ? -value : value;
            } else {
                int value = random.nextInt(distinctValues);
                values[i] = mixed && random.nextBoolean() ? -value : value;
            }
        }

        shuffle(values, random);
        if ("already sorted".equals(order)) {
            Arrays.sort(values);
        } else if ("reversed".equals(order)) {
            Arrays.sort(values);
            reverse(values);
        } else if ("nearly sorted".equals(order)) {
            Arrays.sort(values);
            int swaps = Math.max(1, size / 100);
            for (int i = 0; i < swaps; i++) {
                swap(values, random.nextInt(size), random.nextInt(size));
            }
        } else if ("few runs".equals(order)) {
            createRuns(values, random);
        }
        return values;
    }

    private static void createRuns(int[] values, Random random) {
        Arrays.sort(values);
        int runCount = Math.min(10, Math.max(1, values.length));
        int[] boundaries = new int[runCount + 1];
        boundaries[0] = 0;
        boundaries[runCount] = values.length;
        for (int i = 1; i < runCount; i++) {
            boundaries[i] = i * values.length / runCount;
        }
        List<int[]> runs = new ArrayList<int[]>();
        for (int i = 0; i < runCount; i++) {
            runs.add(Arrays.copyOfRange(values, boundaries[i], boundaries[i + 1]));
        }
        Collections.shuffle(runs, random);
        int index = 0;
        for (int[] run : runs) {
            System.arraycopy(run, 0, values, index, run.length);
            index += run.length;
        }
    }

    private static void shuffle(int[] values, Random random) {
        for (int i = values.length - 1; i > 0; i--) {
            swap(values, i, random.nextInt(i + 1));
        }
    }

    private static void reverse(int[] values) {
        for (int left = 0, right = values.length - 1; left < right; left++, right--) {
            swap(values, left, right);
        }
    }

    private static void swap(int[] values, int first, int second) {
        int temporary = values[first];
        values[first] = values[second];
        values[second] = temporary;
    }

    private static boolean isSortedLike(Object actual, Object expected) {
        if (actual instanceof Integer[]) {
            return Arrays.equals((Integer[]) actual, (Integer[]) expected);
        }
        if (actual instanceof Double[]) {
            return Arrays.equals((Double[]) actual, (Double[]) expected);
        }
        if (actual instanceof String[]) {
            return Arrays.equals((String[]) actual, (String[]) expected);
        }
        return Arrays.equals((OrderedObject[]) actual, (OrderedObject[]) expected);
    }

    private static Object cloneArray(Object array) {
        if (array instanceof Integer[]) {
            return ((Integer[]) array).clone();
        }
        if (array instanceof Double[]) {
            return ((Double[]) array).clone();
        }
        if (array instanceof String[]) {
            return ((String[]) array).clone();
        }
        return ((OrderedObject[]) array).clone();
    }

    private static int sourceLength(Object array) {
        if (array instanceof Integer[]) {
            return ((Integer[]) array).length;
        }
        if (array instanceof Double[]) {
            return ((Double[]) array).length;
        }
        if (array instanceof String[]) {
            return ((String[]) array).length;
        }
        return ((OrderedObject[]) array).length;
    }

    private static Object resizeForWarmup(Object array) {
        if (array instanceof Integer[]) {
            Integer[] source = (Integer[]) array;
            Integer[] warmup = new Integer[1000];
            fillRepeated(warmup, source);
            return warmup;
        }
        if (array instanceof Double[]) {
            Double[] source = (Double[]) array;
            Double[] warmup = new Double[1000];
            fillRepeated(warmup, source);
            return warmup;
        }
        if (array instanceof String[]) {
            String[] source = (String[]) array;
            String[] warmup = new String[1000];
            fillRepeated(warmup, source);
            return warmup;
        }
        OrderedObject[] source = (OrderedObject[]) array;
        OrderedObject[] warmup = new OrderedObject[1000];
        fillRepeated(warmup, source);
        return warmup;
    }

    private static <T> void fillRepeated(T[] target, T[] source) {
        for (int i = 0; i < target.length; i++) {
            target[i] = source[i % source.length];
        }
    }

    private static List<SortSpec> applicableSorts(String type) {
        List<SortSpec> sorts = new ArrayList<SortSpec>();
        sorts.add(new SortSpec("InsertionSort", array -> InsertionSort.insertionSort((Comparable[]) array)));
        sorts.add(new SortSpec("MergeSort", array -> MergeSort.mergeSort((Comparable[]) array, 0, ((Comparable[]) array).length)));
        sorts.add(new SortSpec("QuickSort", array -> QuickSort.quickSort((Comparable[]) array, 0, ((Comparable[]) array).length)));
        sorts.add(new SortSpec("SelectionSort", array -> SelectionSort.selectionSort((Comparable[]) array)));
        sorts.add(new SortSpec("TimSort", array -> TimSort.timSort((Comparable[]) array)));
        if ("Integer".equals(type)) {
            sorts.add(new SortSpec("BucketSort", array -> BucketSort.bucketSort((Integer[]) array)));
        }
        if ("Integer".equals(type)) {
            sorts.add(new SortSpec("RadixSort", array -> RadixSort.radixSort((Integer[]) array, RadixSort.INTEGER)));
        } else if ("Double".equals(type)) {
            sorts.add(new SortSpec("RadixSort", array -> RadixSort.radixSort((Double[]) array, RadixSort.DOUBLE)));
        } else if ("String".equals(type)) {
            sorts.add(new SortSpec("RadixSort", array -> RadixSort.radixSort((String[]) array, RadixSort.STRING)));
        } else {
            sorts.add(new SortSpec("RadixSort", array -> RadixSort.radixSort((OrderedObject[]) array,
                    RadixSort.byInt(object -> object.key))));
        }
        return sorts;
    }

    private static String[] typeNames() {
        return new String[]{"Integer", "Double", "String", "abstract object with some ordering properties"};
    }

    private static String[] signsFor(String type, String duplicates) {
        if ("String".equals(type) || "abstract object with some ordering properties".equals(type)) {
            return new String[]{""};
        }
        if ("all equal".equals(duplicates)) {
            return new String[]{"positive only", "mixed negative and positive"};
        }
        return SIGNS.clone();
    }

    private static int[] createSizes() {
        int[] sizes = new int[35];
        int index = 0;
        for (int size = 3; size <= 10; size++) {
            sizes[index++] = size;
        }
        for (int size = 20; size <= 100; size += 10) {
            sizes[index++] = size;
        }
        for (int size = 200; size <= 1000; size += 100) {
            sizes[index++] = size;
        }
        for (int size = 2000; size <= 10000; size += 1000) {
            sizes[index++] = size;
        }
        return sizes;
    }

    private static int caseHash(
            String type,
            int size,
            String order,
            String duplicates,
            String sign) {
        int hash = type.hashCode();
        hash = 31 * hash + size;
        hash = 31 * hash + order.hashCode();
        hash = 31 * hash + duplicates.hashCode();
        return 31 * hash + sign.hashCode();
    }

    private interface ArraySorter {
        void sort(Object array);
    }

    private static final class Measurement {
        private final long runtime;
        private final Object firstResult;

        private Measurement(long runtime, Object firstResult) {
            this.runtime = runtime;
            this.firstResult = firstResult;
        }
    }

    private static final class SortSpec {
        private final String name;
        private final ArraySorter sorter;

        private SortSpec(String name, ArraySorter sorter) {
            this.name = name;
            this.sorter = sorter;
        }

        private void sort(Object array) {
            sorter.sort(array);
        }
    }

    private static final class OrderedObject implements Comparable<OrderedObject> {
        private final int key;

        private OrderedObject(int key) {
            this.key = key;
        }

        @Override
        public int compareTo(OrderedObject other) {
            return Integer.compare(key, other.key);
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof OrderedObject && key == ((OrderedObject) other).key;
        }

        @Override
        public int hashCode() {
            return Integer.hashCode(key);
        }
    }
}
