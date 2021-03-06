package com.example.redis.demo;

import io.netty.util.HashedWheelTimer;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;
import java.util.function.BiFunction;

/**
 * @author ZhuYX
 * @date 2021/04/19
 */
public class StreamTest {

    @Test
    public void test() throws ExecutionException, InterruptedException {
        // SubmissionPublisher<String> sp = new SubmissionPublisher<>();
        CompletableFuture<String> cf = new CompletableFuture<>();
        var complete = cf.complete("cf complete.");
        System.out.println(complete);

        System.out.println("####### result : " + cf.get());
        System.out.println("####### result : " + cf.join());


    }
    @Test
    public void test1() throws InterruptedException {
        var async = CompletableFuture.runAsync(() -> {
            System.out.println("######### run async.");
        });
        System.out.println(async);

        var future = CompletableFuture.supplyAsync(() -> {
            System.out.println("######## run supply async.");
            return "supplyAsync";
        });
        System.out.println(future.join());

    }

    @Test
    public void test2() {

        CompletableFuture.runAsync(() -> {}).thenRun(() -> {});
        CompletableFuture.runAsync(() -> {}).thenAccept(System.out::println);
        CompletableFuture.runAsync(() -> {}).thenApply(resultA -> {
            System.out.println(resultA);
            return "resultB";
        });

        CompletableFuture.supplyAsync(() -> "resultA").thenRun(() -> {});
        CompletableFuture.supplyAsync(() -> "resultA").thenAccept(System.out::println);
        var stringCompletableFuture = CompletableFuture.supplyAsync(() -> "resultA").thenApply(resultA -> resultA + " resultB");
        System.out.println(stringCompletableFuture.join());
    }

    @Test
    public void test2_exceptionally() {
        var apply = CompletableFuture
                // .supplyAsync(() -> "resultA")
                .supplyAsync(() -> {
                    throw new RuntimeException("test exception.");
                })
                .exceptionally(throwable -> "throwable " + throwable.getMessage())
                .thenApply(resultA -> resultA + " resultB")
                .thenApply(resultB -> resultB + " resultC")
                .thenApply(resultC -> resultC + " resultD");
        System.out.println(apply.join());

    }

    @Test
    public void test2_handle_exception() {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "resultA")
                .thenApply(resultA -> resultA + " resultB")
                // ?????? C ????????????
                .thenApply(resultB -> {throw new RuntimeException();})
                // ???????????? C ?????????????????????
                .handle((re, throwable) -> {
                    if (throwable != null) {
                        return "errorResultC";
                    }
                    return re;
                })
                .thenApply(resultC -> resultC + " resultD");

        System.out.println(future.join());
    }

    @Test
    public void test3() throws ExecutionException, InterruptedException {

        ExecutorService executorService = Executors.newCachedThreadPool();

        Future<String> futureA = executorService.submit(() -> "resultA");
        Future<String> futureB = executorService.submit(() -> "resultB");

        System.out.println(futureA.get());
        System.out.println(futureB.get());

    }
    @Test
    public void test4() {

        CompletableFuture<String> cfA = CompletableFuture.supplyAsync(() -> "resultA");
        CompletableFuture<String> cfB = CompletableFuture.supplyAsync(() -> "resultB");

        var completableFuture = cfA.thenAcceptBoth(cfB, (resultA, resultB) -> {
            System.out.println(resultA + ", " + resultB);
        });
        System.out.println(completableFuture.join());

        var stringCompletableFuture = cfA.thenCombine(cfB, (resultA, resultB) -> {
            System.out.println(resultA + ", " + resultB);
            return "result A + B";
        });
        System.out.println(stringCompletableFuture.join());


        cfA.runAfterBoth(cfB, () -> System.out.println("runAfterBoth"));

        cfA.thenAcceptBoth(cfB, (a, b) -> System.out.println(a + b));
        var stringCompletableFuture1 = cfA.thenCombine(cfB, (a, b) -> {
            System.out.println(a + b);
            return "result A + B";
        });
        System.out.println(stringCompletableFuture1.join());


    }
    @Test
    public void test5() {
        CompletableFuture<String> cfA = CompletableFuture.supplyAsync(() -> "resultA");
        CompletableFuture<String> cfB = CompletableFuture.supplyAsync(() -> "123");
        CompletableFuture<String> cfC = CompletableFuture.supplyAsync(() -> "resultC");

        CompletableFuture<Void> future = CompletableFuture.allOf(cfA, cfB, cfC);
        // ??????????????? join() ?????????????????????????????????????????????
        future.join();

    }

    /**
     * ?????????????????? either ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
     * ???????????????????????????????????????????????????????????????????????? A ????????? B ??????????????????????????????????????????
     *
     */
    @Test
    public void test5_either() {
        var executorService = Executors.newFixedThreadPool(4);

        CompletableFuture<String> cfA = CompletableFuture.supplyAsync(() -> "resultA");
        CompletableFuture<String> cfB = CompletableFuture.supplyAsync(() -> "123");

        cfA.acceptEither(cfB, result -> {});
        cfA.acceptEitherAsync(cfB, result -> {});
        cfA.acceptEitherAsync(cfB, result -> {}, executorService);

        cfA.applyToEither(cfB, result -> {return result;});
        cfA.applyToEitherAsync(cfB, result -> {return result;});
        cfA.applyToEitherAsync(cfB, result -> {return result;}, executorService);

        cfA.runAfterEither(cfA, () -> {});
        cfA.runAfterEitherAsync(cfB, () -> {});
        cfA.runAfterEitherAsync(cfB, () -> {}, executorService);

    }
}
