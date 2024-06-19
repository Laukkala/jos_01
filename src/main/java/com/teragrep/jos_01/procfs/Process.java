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

import com.teragrep.jos_01.procfs.status.process.Stat;
import com.teragrep.jos_01.procfs.status.RowFile;
import com.teragrep.jos_01.procfs.status.CharacterDelimited;
import com.teragrep.jos_01.procfs.status.process.Statm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Process {

    private final long processId;
    private final File procDirectory;
    private final Logger LOGGER = LoggerFactory.getLogger(Process.class);
    private final LinuxOS os;

    public Process(String processId) {
        this(Long.parseLong(processId));
    }

    public Process(long processId) {
        this(processId, new File("/proc", Long.toString(processId)), new LinuxOS());
    }

    public Process(long processId, LinuxOS os) {
        this(processId, new File("/proc", Long.toString(processId)), os);
    }

    public Process(long processId, File procDirectory, LinuxOS os) {
        this.processId = processId;
        this.procDirectory = procDirectory;
        this.os = os;
    }

    public Stat stat() throws IOException {
        return new Stat(new CharacterDelimited(new RowFile(procDirectory, "stat"), " "));
    }

    public Statm statm() throws IOException {
        return new Statm(new RowFile(procDirectory, "statm"));
    }

    private ArrayList<String> procFileNames(ArrayList<String> nameList, File file) {
        if (!file.isDirectory()) {
            nameList.add(file.getPath().replace(procDirectory.getPath(), ""));
        }
        else {
            try {
                File[] subdirectories = file.listFiles();
                if (subdirectories == null) {
                    throw new IOException(
                            "Failed to get all file names! Either no permission to open file at " + file.getPath()
                                    + " or it is not a directory!"
                    );
                }
                for (File child : file.listFiles()) {
                    procFileNames(nameList, child);
                }
            }
            catch (IOException ioe) {
                return nameList;
            }

        }
        return nameList;
    }

    public ArrayList<Task> tasks() throws IOException {
        ArrayList<Task> tasks = new ArrayList<Task>();
        File processTaskDirectory = new File(procDirectory, "task");
        File[] childDirectories = processTaskDirectory.listFiles();
        if (childDirectories == null) {
            throw new IOException(
                    "Failed to access list of tasks within " + processTaskDirectory.getPath()
                            + " Either no permission or file is not a directory"
            );
        }
        for (File directory : childDirectories) {
            tasks.add(new Task(Long.parseLong(directory.getName()), this));
        }
        return tasks;
    }

    // Prints RSS in kB
    public float residentSetSize() throws IOException {
        Statm statm = statm();
        long pageCount = statm.resident();
        long pageSize = os.pageSize();
        return pageCount * pageSize;
    }

    public double cpuUsage() throws IOException {
        double cpuTicksPerSecond = os.cpuTicksPerSecond();

        double OSUpTime = os.uptime().uptimeSeconds();
        Stat status = stat();
        double utime = status.utime() / cpuTicksPerSecond;
        double stime = status.stime() / cpuTicksPerSecond;
        double starttime = status.starttime() / cpuTicksPerSecond;
        double cpuTime = utime + stime;

        return cpuTime / (OSUpTime - starttime);
    }

    public float cpuTime() throws IOException {
        Stat status = stat();
        long cpuTicksPerSecond = os.cpuTicksPerSecond();
        float utime = (float) status.utime() / cpuTicksPerSecond;
        float stime = (float) status.stime() / cpuTicksPerSecond;
        return utime + stime;
    }

    public long pid() {
        return processId;
    }

    // Only the OS kernel can write or delete files from /proc, so if the process ID directory exists, the process is alive.
    public boolean isAlive() {
        return procDirectory.exists();
    }
}
