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
import com.teragrep.jos_01.procfs.status.RowFile;
import com.teragrep.jos_01.procfs.status.CharacterDelimited;
import com.teragrep.jos_01.procfs.status.process.Statm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class Task {

    private final long taskId;
    private final long processId;
    private final File procDirectory;
    private final Logger LOGGER = LoggerFactory.getLogger(Task.class);

    public Task(String taskId, Process parentProcess) throws Exception {
        this(Long.parseLong(taskId), parentProcess);
    }

    public Task(long taskId, Process parentProcess) throws Exception {
        this.taskId = taskId;
        this.processId = parentProcess.pid();
        this.procDirectory = new File("/proc/" + processId + "/task/", Long.toString(processId));
    }

    public Stat stat() throws Exception {
        return new Stat(new CharacterDelimited(new RowFile(procDirectory, "stat"), " "));
    }

    public Statm statm() throws Exception {
        return new Statm(new RowFile(procDirectory, "statm"));
    }

    public long tid() {
        return taskId;
    }

    public long pid() {
        return processId;
    }

    // Only the OS kernel can write or delete files from /proc, so if the process ID directory exists, the process is alive.
    public boolean isAlive() {
        return (procDirectory.exists());
    }
}
