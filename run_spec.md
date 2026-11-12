**Instructions for Generating Test Cases for Sorting Methods in Project:**

**Goal: For every combination of data type, size, order and duplicate level, time each sort, compare it to Java's Arrays.sort, and say which sort is fastest for each case.**

| **Dimension** | **Values** |
| --- | --- |
| **Type** | **Integer, Double, String, abstract object with some ordering properties** |
| **Size** | - **3, 4, 5, 6, 7, 8, 9, 10**<br>- **20, 30, 40, … 100**<br>- **200, 300, 400, … 1,000**<br>- **2,000, 3,000, 4,000, … 10,000** |
| **Order** | **random, already sorted, reversed, nearly sorted (sorted, then about 1% of elements swapped at random), few runs (about 10 sorted chunks stuck together)** |
| **Duplicates** | **none (all distinct), heavy (few distinct values), all equal** |
| **Sign (numbers only)** | **positive only, mixed negative and positive** |

**With "all equal", run only order = random, because the order is meaningless.**

#### **Data generation**

- **Use a fixed seed (e.g. 42) for each combination so that every sort gets identical data.**
- **Generate the array once per combination. Before each timed run, give each sort its own copy with .clone().**
- **Generation and cloning must not be timed.**

#### **Timing rules**

- **Time only the sort call, with System.nanoTime().**
- **Warm-up: before measuring, run each sort a few times on throwaway arrays (e.g. 20 sorts of 10,000 elements). This lets the Java JIT compiler optimise first, so the first sort tested isn't unfairly slow.**
- **Tiny arrays (size ≤ 100): one sort takes too little time to measure. Time a loop of 1,000 fresh copies and divide by 1,000.**
- **Repetitions: 30 runs for sizes up to 10,000; 10 runs at 100,000; 3 runs at 1,000,000. Run the selection and insertion sorts only once at 1,000,000. Record the average and also the min and median, which are less affected by random pauses.**
- **Call System.gc() before each batch of timed runs, outside the timing.**
- **Rotate the order in which sorts run between combinations so no sort always goes first.**
- **Wrap each sort in try/catch. If it crashes or runs out of memory, record CRASH and continue.**

#### **Catching crashes and wrong results (cheap)**

**Don't run a separate set of correctness tests. Instead, check the sorts on the data you're already timing:**

1. **For each combination, run Arrays.sort first on a clone and keep that sorted array as the reference.**
2. **Run every other sort inside try { … } catch (Throwable t) { … }. Throwable also catches StackOverflowError and OutOfMemoryError, not just exceptions.**
3. **After a sort's first timed run, compare its output to the reference with Arrays.equals. This is one linear pass over an array that's already in memory, so it costs almost nothing, and unlike a spot check it is exact.**
4. **Set the sort's status to OK, WRONG (output differs from the reference), or CRASH:<ExceptionName>.**
5. **A sort that is WRONG or CRASH for a case is not timed further for that case, and it is skipped for the larger sizes of the same type/order/duplicates/sign case, so a broken sort doesn't waste time. It is excluded from the best-sort table but noted at the bottom that it was skipped/crashed, etc.**

#### **Timing rules**

- **Time only the sort call, with System.nanoTime().**
- **Warm-up: before the real run, run each sort about 20 times on throwaway arrays of 1,000 elements so Java's JIT compiler has optimised it. Don't call System.gc(); with arrays this small it only slows things down.**
- **A tiny sort is too fast to time on its own, so each measurement is a batch of fresh clones, divided by the batch size:**

| **Size** | **Clones per measurement** |
| --- | --- |
| **≤ 100** | **1000** |
| **101–1000** | **10** |
| **> 1000** | **1** |

- **Take 20 measurements per sort per case. Record the average, minimum and median of the 20 (all in nanoseconds per single sort).**
- **Rotate the order the sorts run in from one combination to the next so no sort is always first.**

#### **Output files**

**Create a results/ folder and write one CSV per size and type, named size.type.csv, e.g. 3.Integer.csv, 500.String.csv, 10000.Double.csv. That is 35 × 3 = 105 files.**

**Each file has this header and one row per case per sort:**

**order, duplicates, sign, sort, avg_ns, min_ns, median_ns, status, ratio_vs_arrays_sort**

- **Sort the rows by order, then duplicates, then sign, then sort name, and put the Arrays.sort baseline first in each group.**
- **ratio_vs_arrays_sort = this sort's average ÷ Arrays.sort's average. 1.00 is the same speed, 2.00 is twice as slow, and under 1.00 is faster.**
- **For String there is no sign, so leave that column empty.**
- **Write each file when its size/type is finished. On a rerun, skip any file that already exists, so the run can resume.**

#### **Best sort by case**

**When everything is done, write results/best_by_case.csv:**

**size, order, duplicates, Integer, Double, String**

- **One row per size, order and duplicates combination. Each type cell contains the fastest valid sort - Arrays.sort is not included, but is represented by an added last column with boolean value of is faster than Arrays.sort or not.**
- **Where a type has several sign variants (mixed or positive, plus special values and long prefix), average the avg_ns of all variants for that case first, then pick the winner.**
- **If every one of your sorts is invalid for a case, put none.**

#### **Console output (kept short)**

**Print only:**

- **One progress line per finished file: [12/105] 300.Integer done**
- **One line the first time a sort is marked WRONG or CRASH: ! MergeSort WRONG at 300.Integer**
- **A final line: Done. Results in results/**
