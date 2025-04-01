#MultithreadedSorting

Assignment: Using multithreading techniques, sort a large array of doubles (length 10,000,000) as fast as possible (competitive)

Files Given: final-sorting (folder)

Written Code: Sorting.parallelSort, QSTask (Improvements discussed in report)

How to Test: Run SortingTester. This outputs whether or not the program was successful. If yes, show the time it took to run. If not, show the first incorrect result.

In order to run this program on the HPC cluster, use the command

```
sbatch run-tests.sh
```

When the tests are complete, the output will be printed to a file called `sortingTest.out`. You can view the contents of this file by issuing the command

```
cat sortingTest.out
```
