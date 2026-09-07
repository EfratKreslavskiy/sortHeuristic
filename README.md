# Optimal Sort Heuristic

An honors thesis project that investigates how the performance of sorting algorithms
changes with the data type, input length, and sorted state of an array. The project
uses those characteristics to build a heuristic that selects the sorting method most
likely to sort a given input efficiently.

Sorting is a fundamental operation in computer science. Organized data is easier to
search, maintain, and manipulate, but no single sorting algorithm performs best for
every possible input. A method that is effective for a small, nearly sorted array may
be inefficient for a large, randomly ordered dataset. This project studies those
tradeoffs and uses measured results to guide algorithm selection.

## Project Goals

- Implement and analyze several comparison-based and non-comparison sorting methods.
- Compare performance across different input lengths and data distributions.
- Evaluate integer, floating-point, character, word, and alphanumeric string data.
- Consider whether an input is random, sorted, reverse sorted, or partially sorted.
- Record execution times and use the results to develop a sorting heuristic.
- Select an appropriate sorting algorithm based on the characteristics of the input.

## Sorting Algorithms

The project evaluates the following methods:

### Selection Sort

Selection Sort is simple and uses very little extra memory, but it repeatedly scans
the unsorted portion of the array. Its performance is typically `O(n^2)` even when
the input is already sorted, so it is best for small arrays or when minimizing swaps
matters.

### Insertion Sort

Insertion Sort performs very well on small or nearly sorted arrays because it only
moves elements that are out of place. Its best-case performance is `O(n)`, but it is
typically `O(n^2)` for random or reverse-sorted input.

### Merge Sort

Merge Sort divides the array and combines sorted sections, giving it consistent
`O(n log n)` performance for best-, average-, and worst-case inputs. It handles large
datasets reliably, but requires extra memory while merging.

### Quick Sort

Quick Sort is often one of the fastest practical comparison-based algorithms because
it usually sorts in place with low memory overhead. Its average performance is
`O(n log n)`, although a poor pivot choice can produce `O(n^2)` performance.

### Radix Sort

Radix Sort can outperform comparison-based algorithms when sorting integers or
fixed-length strings with a limited number of digits. Its performance depends on the
input size and number of digits, so it is less suitable for arbitrary data types.

### Timsort

Timsort combines Insertion Sort and Merge Sort and is especially effective when data
already contains sorted runs. It can achieve `O(n)` performance on favorable inputs
and has `O(n log n)` worst-case performance, with extra memory required.

### Bucket Sort

Bucket Sort is typically very fast for uniformly distributed values within a known,
limited range and can approach `O(n)` performance. Its speed decreases when values
are unevenly distributed, and it requires extra memory for the buckets.

The heuristic will account for each algorithm's strengths and limitations. For
example, Insertion Sort may be preferable for a short, nearly sorted array, while
Merge Sort may be more appropriate when predictable worst-case performance matters.
Radix Sort and Bucket Sort can be especially effective for suitable numeric inputs,
but are less flexible than comparison-based algorithms.

## Benchmarking Plan

After the sorting methods are implemented, the project will benchmark them using
hundreds of generated arrays. Planned input sizes range from very small arrays to
arrays containing up to one million elements. The test data will include integers,
floating-point values, letters, words, and strings containing letters and numbers.

Each sorting method will process comparable inputs while execution times are recorded.
The results will then be analyzed to identify patterns based on:

- Data type
- Array length
- Degree of existing order
- Value range and digit length
- Distribution of the input values

These measurements will provide the evidence used to structure and refine the final
heuristic.

## Complexity and Evaluation

The project uses asymptotic complexity notation to compare how each algorithm scales
as the input size, `n`, increases. For example, `O(n)` describes linear growth and
`O(n^2)` describes quadratic growth. Actual execution times will also be measured,
since constant factors, memory usage, implementation details, and the characteristics
of a particular dataset can affect real-world performance.

## Advisors

- [Dr. Dmitry Kreslavskiy](https://www.linkedin.com/in/gt6989b/)
- [Dr. Andrew Katz](https://www.linkedin.com/in/andrew-katz-24611a8/)

## Links

- [JUnit](https://junit.org/)
