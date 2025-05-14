package com.streams;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class ParallelStream {
    public static void main(final String[] args) {
        // A type of stream that enable parallel processing of elements
        // Allowing multiple threads to process parts of the Streams simultaneously
        // This can significantly improve performance for large data sets
        // workload is distributed across multiple threads

        long startTime = System.currentTimeMillis();
        System.out.println("Before Exceution Time : " + startTime);
        List<Integer> list = Stream.iterate(1, x -> x + 1).limit(20000).toList();
        List<Long> factorialsList = list.stream().map(x -> factorial(x)).toList();
        long endTime = System.currentTimeMillis();
        System.out.println("After Exceution Time : "+endTime);
        System.out.println("Time taken with sequential stream : " + (endTime - startTime) +" ms");

        System.out.println("-------------------------------------");
        startTime = System.currentTimeMillis();
        System.out.println("Before Exceution Time : " + startTime);
       // factorialsList = list.parallelStream().map(x -> factorial(x)).toList();
        factorialsList = list.parallelStream().map(x -> factorial(x)).sequential().toList();
        endTime = System.currentTimeMillis();
        System.out.println("After Exceution Time : "+endTime);
        System.out.println("Time taken with parallel stream : " + (endTime - startTime) +" ms");

        // Parallel streams are most effective for CPU-intensive or large datasets where tasks are independent.
        // They may add overhead for simple tasks or small datasets.

        // Cumulative Sum
        // [1,2,3,4,5] --> [1,3,6,10,15]

        List<Integer> list1 = Arrays.asList(1, 2, 3, 4, 5);

        AtomicInteger sum = new AtomicInteger(0);
        List<Integer> cumulative = list1.parallelStream().map(x -> sum.addAndGet(x)).sequential().toList();
        System.out.println(cumulative);

    }
    public static long factorial(int n){
        long result = 1;
        for(int i = 2 ; i<=n; i++){
            result *= i;
        }
        return result;
    }
}
