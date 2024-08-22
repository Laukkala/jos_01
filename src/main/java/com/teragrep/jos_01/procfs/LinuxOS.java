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

import com.teragrep.jos_01.procfs.status.*;
import com.teragrep.jos_01.procfs.status.os.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LinuxOS {

    private final File procDirectory;

    private final Logger LOGGER = LoggerFactory.getLogger(LinuxOS.class);

    private final SysconfInterface sysconf;

    public LinuxOS(SysconfInterface sysconf) {
        this("/proc", sysconf);
    }

    public LinuxOS(String procDirectoryPath) {
        this(procDirectoryPath, new Sysconf());
    }

    public LinuxOS() {
        this("/proc", new Sysconf());
    }

    public LinuxOS(String procDirectoryPath, SysconfInterface sysconf) {
        procDirectory = new File(procDirectoryPath);
        this.sysconf = sysconf;
    }

    public Stat stat() throws Exception {
        try {
            return new Stat(new RowFile(procDirectory, "stat"));
        }
        catch (Exception e) {
            throw new Exception("Failed to create a Stat object!", e);
        }
    }

    public Vmstat vmstat() throws Exception {
        try {
            return new Vmstat(new RowFile(procDirectory, "vmstat"));
        }
        catch (Exception e) {
            throw new Exception("Failed to create a Vmstat object!", e);
        }
    }

    public Meminfo meminfo() throws Exception {
        try {
            return new Meminfo(new RowFile(procDirectory, "meminfo"));
        }
        catch (Exception e) {
            throw new Exception("Failed to create a Meminfo object!", e);
        }
    }

    // Estimates page size in kB.
    public long pageSize() throws Exception {
        try {
            Vmstat vmstat = vmstat();
            Meminfo meminfo = meminfo();
            long mapped = meminfo.Mapped();
            long nr_mapped = vmstat.nr_mapped();
            return mapped / nr_mapped;
        }
        catch (Exception e) {
            throw new Exception("Failed to calculate page size! ", e);
        }
    }

    // Returns total RAM in kB
    public long totalRAM() throws Exception {
        try {

            Meminfo meminfo = meminfo();
            return meminfo.MemTotal();
        }
        catch (Exception e) {
            throw new Exception("Failed to calculate total system RAM!", e);
        }
    }

    public int cpuCount() throws Exception {
        try {
            Cpuinfo cpuinfo = cpuinfo();
            return cpuinfo.cpuCount();
        }
        catch (Exception e) {
            throw new Exception("Failed to calculate system CPU count!", e);
        }
    }

    public int cpuPhysicalCoreCount() throws Exception {
        try {
            Cpuinfo cpuinfo = cpuinfo();
            return cpuinfo.cpuPhysicalCoreCount();
        }
        catch (Exception e) {
            throw new Exception("Failed to calculate system phyical CPU core count!", e);
        }
    }

    public int cpuThreadCount() throws Exception {
        try {
            Cpuinfo cpuinfo = cpuinfo();
            return cpuinfo.cpuThreadCount();
        }
        catch (Exception e) {
            throw new Exception("Failed to calculate system CPU thread count!", e);
        }
    }

    public Cpuinfo cpuinfo() throws Exception {
        try {
            return new Cpuinfo(new RowFile(procDirectory, "cpuinfo"));
        }
        catch (Exception e) {
            throw new Exception("Failed to create a Cpuinfo object!", e);
        }
    }

    public Uptime uptime() throws Exception {
        try {
            return new Uptime(new CharacterDelimited(new RowFile(procDirectory, "uptime"), " "));
        }
        catch (Exception e) {
            throw new Exception("Failed to create a Uptime object!", e);
        }
    }

    public long cpuTicksPerSecond() throws Exception {
        try {
            long clkTck = sysconf.main();
            return clkTck;
        }
        catch (Exception e) {
            throw new Exception("Failed to get system CPU tick rate!", e);
        }
    }

    public File procDirectory() {
        return procDirectory;
    }

}
