/**
 * Copyright (c) 2011-2020, hubin (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.excelsecu.iot.util;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SystemClock {

    private volatile long now;

    private SystemClock() {
        this.now = System.currentTimeMillis();
        scheduleTick();
    }

    private void scheduleTick() {
        new ScheduledThreadPoolExecutor(1, runnable -> {
            Thread thread = new Thread(runnable, "current-time-millis");
            thread.setDaemon(true);
            return thread;
        }).scheduleAtFixedRate(() -> {
            now = System.currentTimeMillis();
        }, 1, 1, TimeUnit.MILLISECONDS);
    }

    public static long now() {
        return SingletonHolder.INSTANCE.now;
    }

    public static String nowString() {
        return SingletonHolder.INSTANCE.now +"";
    }

    public static SystemClock getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final SystemClock INSTANCE = new SystemClock();
    }

}
