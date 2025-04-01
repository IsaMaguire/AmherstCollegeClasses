# MultithreadedPrimeFinding

Assignment: Using multithreading techniques and the sieve of eratosthenes, write a program that finds all prime numbers between 0 and Integer.MAX_VALUE as fast as possible (competitive)

Files Given: final-primes (folder)

Written Code: ParellelPrimes, PrimeChunkTask (Improvements discussed in report)

How to Test: Run PrimeTester. This outputs whether or not the program was successful. If yes, show the time it took to run. If not, show the first incorrect result.

In order to run this program on the HPC cluster, use the command

```
sbatch run-tests.sh
```

When the tests are complete, the output will be printed to a file called `primeTest.out`. You can view the contents of this file by issuing the command

```
cat primeTest.out
```
