package iaa2;

import java.io.*;
import java.util.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class race{

 public static void stop(ExecutorService executor) {  // common function defined by Benjamin
        try {
            executor.shutdown();
            executor.awaitTermination(60, TimeUnit.SECONDS);
        }
        catch (InterruptedException e) {
            System.err.println("termination interrupted");
        }
        finally {
            if (!executor.isTerminated()) {
                System.err.println("killing non-finished tasks");
            }
            executor.shutdownNow();
        }
    }

int binaryLock = 0;
int turn = 0;
int[] interested = new int[2];

int count = 0;  // shared memory

void increment() {  // three steps: read count, add one to value, store at count
    count = count + 1;
}

void incrementWithBinaryLock() {  // binary lock Tanenbaum 2.3, p.123
    while (binaryLock == 1);
    binaryLock = 1;
    count = count + 1;
    binaryLock = 0;
}

void incrementStrictAlternation() {  // strict alternation Tanenbaum p.123
    int id = (int)(Thread.currentThread().getId()) & 1;
    while (turn != id);
    count = count + 1;
    turn = 1 - id;
}

void incrementPeterson() {  // Peterson's solution Tanenbaum p.124
    int id = (int)(Thread.currentThread().getId()) & 1;
    int other = 1 - id;
    interested[id] = 1;
    turn = id;
    while (turn == id && interested[other] == 1);
    count = count + 1;
    interested[id] = 0;
}

synchronized void incrementSync() {  // Tanenbaum p.143
    count = count + 1;
}

ReentrantLock lock = new ReentrantLock();

void incrementWithReentrancelock() {  // Java's ReentrantLock
    lock.lock();
    try {
        count++;
    } finally {
        lock.unlock();
    }
}

void race1(){  // simple race condition

ExecutorService executor = Executors.newFixedThreadPool(2);

IntStream.range(0, 10000)
    .forEach(i -> executor.submit(this::increment));

stop(executor);

System.out.println(count); 
}

void race2(){  // would a binary lock work?

ExecutorService executor = Executors.newFixedThreadPool(2);

IntStream.range(0, 10000)
    .forEach(i -> executor.submit(this::incrementWithBinaryLock));

stop(executor);

System.out.println(count); 
}

void race3(){  // strict alternation

ExecutorService executor = Executors.newFixedThreadPool(2);

IntStream.range(0, 400)  // would this work for more than 500 increments?
    .forEach(i -> executor.submit(this::incrementStrictAlternation));

stop(executor);

System.out.println(count); 
}

void race4(){  // Peterson's method, problem solved?

ExecutorService executor = Executors.newFixedThreadPool(2);

IntStream.range(0, 10000)
    .forEach(i -> executor.submit(this::incrementPeterson));

stop(executor);

System.out.println(count); 
}

void race5(){  // monitor provided in Java

ExecutorService executor = Executors.newFixedThreadPool(2);

IntStream.range(0, 10000)
    .forEach(i -> executor.submit(this::incrementSync));

stop(executor);

System.out.println(count); 
}

void race6(){  // Java's ReentrantLock

ExecutorService executor = Executors.newFixedThreadPool(2);

IntStream.range(0, 10000)
    .forEach(i -> executor.submit(this::incrementWithReentrancelock));

stop(executor);

System.out.println(count); 
}


public static void main(String[] args){
 Race race = new Race();
 System.out.printf("Race 1 \n");
 race.race1();
 race.race1();
 race.race1();
 race.race1();
 race.race1();
 System.out.printf("Race 2 \n");
Race race_2 = new Race();
 race_2.race2();
 race_2.race2();
 race_2.race2();
 race_2.race2();
 race_2.race2();
 System.out.printf("Race 3 \n");
 Race race_3 = new Race();
 race_3.race3();
 race_3.race3();
 race_3.race3();
 race_3.race3();
 race_3.race3();
 System.out.printf("Race 4 \n");
Race race_4 = new Race();
 race_4.race4();
 race_4.race4();
 race_4.race4();
 race_4.race4();
 race_4.race4();
 System.out.printf("Race 5 \n");
 Race race_5 = new Race();
 race_5.race5();
 race_5.race5();
 race_5.race5();
 race_5.race5();
 race_5.race5();
 System.out.printf("Race 6 \n");
 Race race_6 = new Race();
 race_6.race6();
 race_6.race6();
 race_6.race6();
 race_6.race6();
 race_6.race6();
}
}