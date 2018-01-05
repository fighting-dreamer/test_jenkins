/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.sample;

import com.swiggy.dp.JsonSerializer;
import com.swiggy.dp.checkout.BasicData;
import com.swiggy.dp.checkout.UpdateCartV2;
import com.swiggy.dp.checkout.Item;
import org.mockito.Mock;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

import static org.mockito.Mockito.mock;

public class MyBenchmark {
    public static DiskBackedQueueBehaviour dq = new DiskBackedQueueBehaviour(new File("/tmp/test.txt"));
    public static BlockingQueue bq = new LinkedBlockingQueue();

    public static MyState mystate = new MyState();
    public static long cnt = 0;
    @State(Scope.Thread)
    public static class MyState{
        public static List<BasicData> evList = new ArrayList<>();
        public MyState(){
            for (int i = 0; i < 1000; i++)
                evList.add(new BasicData("user_agent", "device_id", "app_version", "version_code", "sid", "request_id", "swid"));
        }
    }

    @Benchmark
    public void testAddingDiskBackedEvents() {
        if (cnt > 1000000000)
            try {
                MyBenchmark.dq.events.clear();
            } catch (IOException e) {
                e.printStackTrace();
            }
        MyBenchmark.dq.add(mystate.evList);
        cnt += 1;
    }

    @Benchmark
    public void testAddingBlockingQueueEvents() {
        if (cnt > 1000000000)
            MyBenchmark.bq.clear();
        MyBenchmark.bq.add(mystate.evList);
        cnt += 1;
    }



//    @Benchmark
//    public void testConcurrentAddingEvents() {
//        if (cnt > 1000000000)
//            try {
//                MyBenchmark.dq.events.clear();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        ExecutorService excutor = Executors.newFixedThreadPool(10);//creating a pool of 10 threads
//
//        MyBenchmark.dq.add(mystate.evList);
//        cnt += 1;
//    }

//    @Benchmark
//    public void testRetrivalOfEvents(){
//        for (int i = 0; i < 1000; i++)
//            dq.add(new Event(1l, "Example"));
//
//        List<Event> evList = new ArrayList<>();
//        int batchSize = 10;
//        for (int i = 0; i < 10; i++) {
//            new Thread() {
//                public void run() {
//                    if (dq.size() != 0)
//                        dq.drainTo(evList, batchSize);
//                }
//            }.start();
//        }
//    }


//
//    @Benchmark
//    public List testRetrivalOfEvents_2(){
//        List<Object> evList = new ArrayList<>();
//        int batchSize = 10;
//        for (int i = 0; i < 100; i++) {
//            new Thread() {
//                public void run() {
//                    if (MyBenchmark.dq.size() != 0)
//                        MyBenchmark.dq.drainTo(evList, batchSize);
//                }
//            }.start();
//        }
//
//        return evList;
//    }
//    public static void main(String[] args) {
////        MyBenchmark.dq.add(mock(BasicData.class));
////        List<BasicData> blist = new ArrayList<>();
////        for (int i = 0; i < 3; i++){
////            blist.add(new BasicData("user_agent", "device_id", "app_version", "version_code", "sid", "request_id", "swid"));
////       }
////        MyBenchmark.dq.add(new BasicData("user_agent", "device_id", "app_version", "version_code", "sid", "request_id", "swid"));
////            MyBenchmark.dq.add(blist);
//
//        MyBenchmark myBenchmark = new MyBenchmark();
//        myBenchmark.testConcurrentAddingEvents();
//    }

}
