/*
 * Java Operating System Statistics JOS-01
 * Copyright (C) 2021-2024 Suomen Kanuuna Oy
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 *
 * Additional permission under GNU Affero General Public License version 3
 * section 7
 *
 * If you modify this Program, or any covered work, by linking or combining it
 * with other code, such other code is not for that reason alone subject to any
 * of the requirements of the GNU Affero GPL version 3 as long as this Program
 * is the same Program as licensed from Suomen Kanuuna Oy without any additional
 * modifications.
 *
 * Supplemented terms under GNU Affero General Public License version 3
 * section 7
 *
 * Origin of the software must be attributed to Suomen Kanuuna Oy. Any modified
 * versions must be marked as "Modified version of" The Program.
 *
 * Names of the licensors and authors may not be used for publicity purposes.
 *
 * No rights are granted for use of trade names, trademarks, or service marks
 * which are in The Program if any.
 *
 * Licensee must indemnify licensors and authors for any liability that these
 * contractual assumptions impose on licensors and authors.
 *
 * To the extent this program is licensed as part of the Commercial versions of
 * Teragrep, the applicable Commercial License may apply to this file if you as
 * a licensee so wish it.
 */
package com.teragrep.jos_01.procfs;

import com.teragrep.jos_01.procfs.status.process.Stat;
import com.teragrep.jos_01.procfs.status.process.Statm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Iterator;

public class ProcessTest {

    private final Logger LOGGER = LoggerFactory.getLogger(ProcessTest.class);
    Process systemd;
    Process kthreadd;

    public static class TestThread implements Runnable {

        private final ProcessBuilder processBuilder;
        private java.lang.Process process;

        public TestThread(ProcessBuilder processBuilder) {
            this.processBuilder = processBuilder;
        }

        @Override
        public void run() {
            try {
                this.process = processBuilder.start();
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private ArrayList<Integer> getPidByCommand(String command) {
        Assertions.assertDoesNotThrow(() -> {
            File binDirectory = new File("/bin");
            File[] installedPrograms = binDirectory.listFiles();
            boolean psInstalled = false;
            for (File program : installedPrograms) {
                if (program.getName().equals("ps")) {
                    psInstalled = true;
                    break;
                }
            }
            Assertions.assertTrue(psInstalled);
        });

        ArrayList<Integer> pids = new ArrayList<Integer>();
        Assertions.assertDoesNotThrow(() -> {

            java.lang.Process ps = Runtime.getRuntime().exec("ps");
            BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
            String line;
            int pid = -1;
            while ((line = br.readLine()) != null) {
                if (line.contains(" " + command)) {
                    pid = Integer.parseInt(line.trim().replaceAll("(\\d+).+", "$1"));
                    pids.add(pid);
                }
            }
        });
        return pids;
    }

    // Should be able to change default proc directory.
    @Test
    public void procDirectoryTest() {
        Assertions.assertDoesNotThrow(() -> {
            Process process = new Process(1);
            Assertions.assertTrue(process.isAlive());
        });
        Assertions.assertThrows(Exception.class, () -> {
            LinuxOS os = new LinuxOS("ThisProcDirectoryDoesntExist");
            Process process = new Process(1, os);
            process.stat().read();
        });
    }

    // JVM process should have 52 fields in its Stat file, and it should have (systemd) as comm value and 1 as pid value in the file. None of the fields should be null
    @Test
    public void readJvmFileTest() {
        Assertions.assertDoesNotThrow(() -> {

            int jvmPid = Integer.parseInt(ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);
            Process jvm = new Process(jvmPid);
            Stat stat = jvm.stat();
            Assertions.assertEquals(jvmPid, stat.pid());
            Assertions.assertEquals("(java)", stat.comm());
            Iterator iterator = stat.read().iterator();
            int i = 0;
            while (iterator.hasNext()) {
                Assertions.assertNotNull(iterator.next());
                i++;
            }
            Assertions.assertEquals(52, i);

        });
    }

    // Process object should be able to be instantiated with an integer and a String
    @Test
    public void constructorTest() {
        Assertions.assertDoesNotThrow(() -> {
            Process process = new Process(1);
            Process processString = new Process("1");
            Assertions.assertTrue(process.isAlive());
            Assertions.assertTrue(processString.isAlive());
        });
    }

    // If a process with the designated ID is not found, the returned Process object should be a stub, and isAlive should return false.
    @Test
    public void noSuchProcessTest() {
        Assertions.assertDoesNotThrow(() -> {
            Process process = new Process(-1);
            Assertions.assertFalse(process.isAlive());
        });
    }

    // After starting a sleeping process in a new thread, isAlive should return true. After killing the process, it should return false.
    @Test
    public void isAliveTest() {
        Assertions.assertDoesNotThrow(() -> {
            ProcessBuilder pb = new ProcessBuilder();
            pb.command("sleep", "2");

            TestThread processThread = new TestThread(pb);
            processThread.run();

            ArrayList<Integer> pids = getPidByCommand("sleep");
            Assertions.assertEquals(1, pids.size());

            Process sleepingProcess = new Process(pids.get(0));

            Assertions.assertTrue(sleepingProcess.isAlive());

            Thread.sleep(2000);
            Assertions.assertFalse(sleepingProcess.isAlive());

        });
    }

    // The number of tasks should be equal to the number of threads in /proc/pid/stat. Test for process id 1
    @Test
    public void correctNumberOfTasksTest() {
        Assertions.assertDoesNotThrow(() -> {
            Process process = new Process(1);
            long statTaskcount = process.stat().num_threads();
            int taskCount = process.tasks().size();
            Assertions.assertEquals(statTaskcount, Long.parseLong(String.valueOf(taskCount)));
        });
    }

    // The number of tasks should be equal to the number of threads in /proc/pid/stat. Test for the current JVM
    @Test
    public void correctNumberOfTasksInJvmTest() {
        Assertions.assertDoesNotThrow(() -> {
            Process jvm = new Process(ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);
            long jvmStatTaskcount = jvm.stat().num_threads();
            int jvmTaskCount = jvm.tasks().size();
            Assertions.assertEquals(jvmStatTaskcount, Long.parseLong(String.valueOf(jvmTaskCount)));
        });
    }

    // Stat status object should contain all of the listed fields.
    @Test
    public void statTest() {
        Assertions.assertDoesNotThrow(() -> {
            Process process = new Process(1);
            Stat stat = process.stat();

            Assertions.assertNotNull(stat.pid());
            Assertions.assertNotNull(stat.comm());
            Assertions.assertNotNull(stat.state());
            Assertions.assertNotNull(stat.ppid());
            Assertions.assertNotNull(stat.pgrp());
            Assertions.assertNotNull(stat.session());
            Assertions.assertNotNull(stat.tty_nr());
            Assertions.assertNotNull(stat.tpgid());
            Assertions.assertNotNull(stat.flags());
            Assertions.assertNotNull(stat.minflt());
            Assertions.assertNotNull(stat.cminflt());
            Assertions.assertNotNull(stat.majflt());
            Assertions.assertNotNull(stat.cmajflt());
            Assertions.assertNotNull(stat.utime());
            Assertions.assertNotNull(stat.stime());
            Assertions.assertNotNull(stat.cutime());
            Assertions.assertNotNull(stat.cstime());
            Assertions.assertNotNull(stat.priority());
            Assertions.assertNotNull(stat.nice());
            Assertions.assertNotNull(stat.num_threads());
            Assertions.assertNotNull(stat.itrealvalue());
            Assertions.assertNotNull(stat.starttime());
            Assertions.assertNotNull(stat.vsize());
            Assertions.assertNotNull(stat.rss());
            Assertions.assertNotNull(stat.rsslim());
            Assertions.assertNotNull(stat.startcode());
            Assertions.assertNotNull(stat.endcode());
            Assertions.assertNotNull(stat.startstack());
            Assertions.assertNotNull(stat.kstkesp());
            Assertions.assertNotNull(stat.kstkeip());
            Assertions.assertNotNull(stat.signal());
            Assertions.assertNotNull(stat.blocked());
            Assertions.assertNotNull(stat.sigignore());
            Assertions.assertNotNull(stat.sigcatch());
            Assertions.assertNotNull(stat.wchan());
            Assertions.assertNotNull(stat.nswap());
            Assertions.assertNotNull(stat.cnswap());
            Assertions.assertNotNull(stat.exit_signal());
            Assertions.assertNotNull(stat.processor());
            Assertions.assertNotNull(stat.rt_priority());
            Assertions.assertNotNull(stat.policy());
            Assertions.assertNotNull(stat.delayacct_blkio_ticks());
            Assertions.assertNotNull(stat.guest_time());
            Assertions.assertNotNull(stat.cguest_time());
            Assertions.assertNotNull(stat.start_data());
            Assertions.assertNotNull(stat.end_data());
            Assertions.assertNotNull(stat.start_brk());
            Assertions.assertNotNull(stat.arg_start());
            Assertions.assertNotNull(stat.arg_end());
            Assertions.assertNotNull(stat.env_start());
            Assertions.assertNotNull(stat.env_end());
            Assertions.assertNotNull(stat.exit_code());

        });
    }

    // Statm status object should contain all of the listed fields.
    @Test
    public void statmTest() {
        Assertions.assertDoesNotThrow(() -> {
            Process process = new Process(1);
            Statm statm = process.statm();
            Assertions.assertNotNull(statm.size());
            Assertions.assertNotNull(statm.resident());
            Assertions.assertNotNull(statm.shared());
            Assertions.assertNotNull(statm.text());
            Assertions.assertNotNull(statm.lib());
            Assertions.assertNotNull(statm.data());
            Assertions.assertNotNull(statm.dt());
        });
    }

    // Timestamps should be available and should be different if the proc method is called later
    @Test
    public void timestampTest() {
        Assertions.assertDoesNotThrow(() -> {
            systemd = new Process(1);
            Stat stat = systemd.stat();
            Stat stat2 = systemd.stat();
            Statm statm = systemd.statm();

            // Timestamps should always have a value
            Assertions.assertNotNull(stat.timestamp());
            Assertions.assertNotNull(statm.timestamp());

            // Timestamps should be different if called at different times, even when calling the same method again
            Assertions.assertFalse(stat.timestamp().equals(stat2.timestamp()));
            Assertions.assertFalse(stat.timestamp().equals(statm.timestamp()));

        });
    }

    // Get the JVM process this test is running in, and make two delayed calls to cpuTime to make sure cpuTime actually increments as process is in use
    // Uses SysconfInterface.Fake() to get a hardcoded CPU tick rate (100) without actually having to compile Native C code.
    @Test
    public void CpuTimeTest() {
        Assertions.assertDoesNotThrow(() -> {
            long pid = Long.parseLong(ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);
            Process jvm = new Process(pid, new LinuxOS(new FakeSysconf()));
            float cpuTime1 = jvm.cpuTime();
            ArrayList<Double> workArray = new ArrayList<Double>();
            for (int i = 0; i < 300000; i++) { // Needs to do a lot of work to have different CPUtimes since the times are in seconds, not ticks
                workArray.add(Math.random());
            }
            float cpuTime2 = jvm.cpuTime();
            Assertions.assertNotEquals(cpuTime2, cpuTime1);

        });
    }

    // As resident set size for the whole JVM fluctuates a lot we need to create a real chonker of an object to be sure that RSS increases when creating objects
    @Test
    public void residentSetSizeTest() {
        Assertions.assertDoesNotThrow(() -> {
            Process jvm = new Process(ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);
            float rssBefore = jvm.residentSetSize();
            float freeMemoryBefore = Runtime.getRuntime().freeMemory();
            ArrayList<Integer> numbers = new ArrayList<Integer>();
            for (int i = 0; i < 10000000; i++) {
                numbers.add((int) Math.floor(Math.random() * 100));
            }
            float rssAfter = jvm.residentSetSize();
            float freeMemoryAfter = Runtime.getRuntime().freeMemory();
            Assertions.assertEquals(freeMemoryBefore, freeMemoryAfter + (rssAfter - rssBefore) * 1000, 20000000); // Based on testing JVM seems to allocate 15-20 megabytes of memory outside of the numbers object.
            numbers.clear();
        });
    }

    // As resident set size for the whole JVM fluctuates a lot we need to create a real chonker of an object to be sure that RSS increases when creating objects
    @Test
    public void memoryPercentageTest() {
        Assertions.assertDoesNotThrow(() -> {
            LinuxOS os = new LinuxOS(new FakeSysconf());
            Process jvm = new Process(
                    Integer.parseInt(ManagementFactory.getRuntimeMXBean().getName().split("@")[0]),
                    os
            );
            float memoryPercentage = jvm.memoryPercentage();
            float rss = jvm.residentSetSize();
            float totalMemory = os.totalRAM();
            Assertions.assertTrue(memoryPercentage >= 0 && memoryPercentage <= 100);
            Assertions.assertEquals(memoryPercentage, rss / totalMemory, 0.0001);
        });
    }

    // CpuUsage of JVM should increase over time
    @Test
    public void cpuUsageTest() {
        Assertions.assertDoesNotThrow(() -> {
            long pid = Long.parseLong(ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);
            Process jvm = new Process(pid, new LinuxOS(new FakeSysconf()));
            double cpuUsage1 = jvm.cpuUsage();
            ArrayList<String> workArray = new ArrayList<String>();
            for (int i = 0; i < 3000000; i++) {
                workArray.add("JVM doing important work for tests");
            }
            double cpuUsage2 = jvm.cpuUsage();
            Assertions.assertNotEquals(cpuUsage1, cpuUsage2);
        });
    }
}
