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
package src.test.java.procfs;

import org.junit.Assert;
import org.junit.Test;
import src.main.java.procfs.Process;

import java.util.ArrayList;
import java.util.Map;

public class ProcessTest {

    @Test
    public void readFileTest() {
        Process systemd = new Process(1);
        Map<String, String> systemdStats = systemd.stat().statistics();
        Assert.assertEquals("1", systemdStats.get("pid"));
        Assert.assertEquals("(systemd)", systemdStats.get("comm"));
        Assert.assertEquals(52, systemdStats.size());
        for (Map.Entry<String, String> entry : systemdStats.entrySet()) {
            Assert.assertNotNull(entry.getValue());
            Assert.assertNotNull(entry.getKey());
        }

        Process kthreadd = new Process(2);
        Map<String, String> kthreaddStats = kthreadd.stat().statistics();
        Assert.assertEquals("2", kthreaddStats.get("pid"));
        Assert.assertEquals("(kthreadd)", kthreaddStats.get("comm"));
        Assert.assertEquals(52, kthreaddStats.size());
        for (Map.Entry<String, String> entry : kthreaddStats.entrySet()) {
            Assert.assertNotNull(entry.getValue());
            Assert.assertNotNull(entry.getKey());
        }
    }

    @Test
    public void constructorTest() {
        Process process = new Process(1);
        Process processString = new Process("1");
    }

    @Test
    public void noSuchProcessTest() {
        Process process = new Process(-1);
        Assert.assertFalse(process.isAlive());
    }

    @Test
    public void procFileNamesTest() {
        Process process = new Process(1);
        ArrayList<String> fileNames = process.availableProcFiles();
        Assert.assertEquals(243, fileNames.size());
    }

    @Test
    public void correctNumberOfTasksTest() {
        Process process = new Process(1);
        String statTaskcount = process.stat().statistics().get("num_threads");
        int taskCount = process.tasks().size();
        Assert.assertEquals(statTaskcount, String.valueOf(taskCount));
    }

    @Test
    public void procTest() {
        Process process = new Process(1);
        ArrayList<String> rows = process.proc("status").rows();
        Assert.assertEquals(59, rows.size());

        rows = process.proc("comm").rows();
        Assert.assertEquals(1, rows.size());
        Assert.assertEquals("systemd", rows.get(0));
    }
}
