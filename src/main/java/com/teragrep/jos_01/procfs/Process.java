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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.teragrep.jos_01.procfs.status.ProcessStat;
import com.teragrep.jos_01.procfs.status.Statm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Process {

    private final long processId;
    private final File procDirectory;
    private final Logger LOGGER = LoggerFactory.getLogger(Process.class);

    public Process(String processId) {
        this(Integer.parseInt(processId));
    }

    public Process(long processId) {
        this(processId, "/proc");
    }

    public Process(long processId, String procDirectoryPath) {
        this.processId = processId;
        this.procDirectory = new File(procDirectoryPath, Long.toString(processId));
    }

    // Creates a Status object based on the chosen file name in /proc for this process. Overloaded to accept different kinds of Status objects
    private ArrayList<String> reportStatistics(String procFileName) throws IOException {
        ArrayList<String> rows = readProcFile(procFileName);
        return rows;
    }

    private ArrayList<String> readProcFile(String procFileName) throws IOException {
        RowFile procFile = new RowFile(procDirectory, procFileName);
        ArrayList<String> rows = procFile.readFile();
        return rows;
    }

    public ProcessStat stat() throws IOException {
        ArrayList<String> rows = readProcFile("stat");
        return new ProcessStat(rows);
    }

    public Statm statm() throws IOException {
        ArrayList<String> rows = readProcFile("statm");
        return new Statm(rows);
    }

    public ArrayList<String> availableProcFiles() {
        ArrayList<String> fileNames = procFileNames(new ArrayList<String>(), procDirectory);
        return fileNames;
    }

    private ArrayList<String> procFileNames(ArrayList<String> nameList, File file) {
        if (!file.isDirectory()) {
            nameList.add(file.getPath().replace(procDirectory.getPath(), ""));
        }
        else {
            File[] subdirectories = file.listFiles();
            if (subdirectories == null) {
                LOGGER
                        .info(
                                "Failed to get all file names! Either no permission to open file at {} or it is not a directory!",
                                file.getPath()
                        );
                return nameList;
            }
            for (File child : file.listFiles()) {
                procFileNames(nameList, child);
            }
        }
        return nameList;
    }

    public ArrayList<Task> tasks() {
        ArrayList<Task> tasks = new ArrayList<Task>();
        for (File taskDirectory : new File(procDirectory, "task").listFiles()) {
            tasks.add(new Task(Long.parseLong(taskDirectory.getName()), this));
        }
        return tasks;
    }

    // Prints RSS in kB
    public float residentSetSize() throws IOException {
        Statm statm = statm();
        String rssPages = statm.statistics().get("resident");
        int pageCount = Integer.parseInt(rssPages);
        float pageSize = new LinuxOS().pageSize();
        return pageCount * pageSize;
    }

    public float cpuUsage() throws IOException {
        return cpuUsage(new Sysconf());
    }

    public float cpuUsage(SysconfInterface sysconf) throws IOException {
        LinuxOS os = new LinuxOS();
        int cpuCount = os.cpuCount();
        long cpuTicksPerSecond = os.cpuTicksPerSecond(sysconf);

        float OSUpTime = Float.parseFloat(os.uptime().statistics().get("uptimeSeconds"));
        ProcessStat status = stat();

        float utime = Float.parseFloat(status.statistics().get("utime")) / cpuTicksPerSecond;
        float stime = Float.parseFloat(status.statistics().get("stime")) / cpuTicksPerSecond;
        float starttime = Float.parseFloat(status.statistics().get("starttime")) / cpuTicksPerSecond;
        float cpuTime = utime + stime;

        float cpuUsage = cpuTime / (OSUpTime - starttime);
        return cpuUsage;
    }

    public float cpuTime() throws IOException {
        return cpuTime(new Sysconf());
    }

    public float cpuTime(SysconfInterface sysconf) throws IOException {
        LinuxOS os = new LinuxOS();
        ProcessStat status = stat();
        long cpuTicksPerSecond = os.cpuTicksPerSecond(sysconf);
        float utime = Float.parseFloat(status.statistics().get("utime")) / cpuTicksPerSecond;
        float stime = Float.parseFloat(status.statistics().get("stime")) / cpuTicksPerSecond;

        float cpuTime = utime + stime;
        return cpuTime;
    }

    public long pid() {
        return processId;
    }

    // Only the OS kernel can write or delete files from /proc, so if the process ID directory exists, the process is alive.
    public boolean isAlive() {
        return procDirectory.exists();
    }
}
