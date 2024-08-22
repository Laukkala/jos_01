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

import com.teragrep.jos_01.procfs.status.os.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LinuxOSTest {

    // Uptime should have 2 fields, and the values should be positive floats.
    @Test
    public void upTimeTest() {
        Assertions.assertDoesNotThrow(() -> {
            LinuxOS os = new LinuxOS();
            Uptime uptime = os.uptime();
            Assertions.assertEquals(2, uptime.read().size());
            Assertions.assertTrue(uptime.uptimeSeconds() > 0);
            Assertions.assertTrue(uptime.combinedCpuCoreIdleTimeSeconds() > 0);
        });
    }

    // Stat should have 7 fields in addition to cpu lines, and all expected keys should be present.
    // The number of cpu lines is one aggregate line + one line for each logical core. The number of logical cores should be the same as reported in /proc/cpuinfo
    @Test
    public void statTest() {
        Assertions.assertDoesNotThrow(() -> {
            LinuxOS os = new LinuxOS();
            Stat stat = os.stat();

            int numberOfCpus = stat.cpus().size();
            Assertions.assertEquals(7 + numberOfCpus, stat.read().size());

            Assertions.assertTrue(stat.ctxt() >= 0);
            Assertions.assertTrue(stat.intr().size() >= 1);
            Assertions.assertTrue(stat.processes() >= 0);
            Assertions.assertTrue(stat.procs_blocked() >= 0);
            Assertions.assertTrue(stat.procs_running() >= 0);
            Assertions.assertTrue(stat.softirq().size() >= 1);
        });

    }

    // Check that vmStat instantiates every required keys
    // vmstat might have varying number of keys
    @Test
    public void vmStatTest() {
        Assertions.assertDoesNotThrow(() -> {
            LinuxOS os = new LinuxOS();
            Vmstat vmstat = os.vmstat();
            Assertions.assertTrue(vmstat.nr_free_pages() >= 0);
            Assertions.assertTrue(vmstat.nr_inactive_anon() >= 0);
            Assertions.assertTrue(vmstat.nr_active_anon() >= 0);
            Assertions.assertTrue(vmstat.nr_inactive_file() >= 0);
            Assertions.assertTrue(vmstat.nr_active_file() >= 0);
            Assertions.assertTrue(vmstat.nr_unevictable() >= 0);
            Assertions.assertTrue(vmstat.nr_mlock() >= 0);
            Assertions.assertTrue(vmstat.nr_anon_pages() >= 0);
            Assertions.assertTrue(vmstat.nr_mapped() >= 0);
            Assertions.assertTrue(vmstat.nr_file_pages() >= 0);
            Assertions.assertTrue(vmstat.nr_dirty() >= 0);
            Assertions.assertTrue(vmstat.nr_writeback() >= 0);
            Assertions.assertTrue(vmstat.nr_slab_reclaimable() >= 0);
            Assertions.assertTrue(vmstat.nr_slab_unreclaimable() >= 0);
            Assertions.assertTrue(vmstat.nr_page_table_pages() >= 0);
            Assertions.assertTrue(vmstat.nr_kernel_stack() >= 0);
            Assertions.assertTrue(vmstat.nr_unstable() >= 0);
            Assertions.assertTrue(vmstat.nr_bounce() >= 0);
            Assertions.assertTrue(vmstat.nr_vmscan_write() >= 0);
            Assertions.assertTrue(vmstat.nr_vmscan_immediate_reclaim() >= 0);
            Assertions.assertTrue(vmstat.nr_writeback_temp() >= 0);
            Assertions.assertTrue(vmstat.nr_isolated_anon() >= 0);
            Assertions.assertTrue(vmstat.nr_isolated_file() >= 0);
            Assertions.assertTrue(vmstat.nr_shmem() >= 0);
            Assertions.assertTrue(vmstat.nr_dirtied() >= 0);
            Assertions.assertTrue(vmstat.nr_written() >= 0);
            Assertions.assertTrue(vmstat.numa_hit() >= 0);
            Assertions.assertTrue(vmstat.numa_miss() >= 0);
            Assertions.assertTrue(vmstat.numa_foreign() >= 0);
            Assertions.assertTrue(vmstat.numa_interleave() >= 0);
            Assertions.assertTrue(vmstat.numa_local() >= 0);
            Assertions.assertTrue(vmstat.numa_other() >= 0);
            Assertions.assertTrue(vmstat.nr_free_cma() >= 0);
            Assertions.assertTrue(vmstat.nr_dirty_threshold() >= 0);
            Assertions.assertTrue(vmstat.nr_dirty_background_threshold() >= 0);
            Assertions.assertTrue(vmstat.pgpgin() >= 0);
            Assertions.assertTrue(vmstat.pgpgout() >= 0);
            Assertions.assertTrue(vmstat.pswpin() >= 0);
            Assertions.assertTrue(vmstat.pswpout() >= 0);
            Assertions.assertTrue(vmstat.pgalloc_dma() >= 0);
            Assertions.assertTrue(vmstat.pgalloc_dma32() >= 0);
            Assertions.assertTrue(vmstat.pgalloc_normal() >= 0);
            Assertions.assertTrue(vmstat.pgalloc_movable() >= 0);
            Assertions.assertTrue(vmstat.pgfree() >= 0);
            Assertions.assertTrue(vmstat.pgactivate() >= 0);
            Assertions.assertTrue(vmstat.pgdeactivate() >= 0);
            Assertions.assertTrue(vmstat.pgfault() >= 0);
            Assertions.assertTrue(vmstat.pgmajfault() >= 0);
            Assertions.assertTrue(vmstat.pgscan_direct_throttle() >= 0);
            Assertions.assertTrue(vmstat.zone_reclaim_failed() >= 0);
            Assertions.assertTrue(vmstat.pginodesteal() >= 0);
            Assertions.assertTrue(vmstat.slabs_scanned() >= 0);
            Assertions.assertTrue(vmstat.kswapd_inodesteal() >= 0);
            Assertions.assertTrue(vmstat.kswapd_low_wmark_hit_quickly() >= 0);
            Assertions.assertTrue(vmstat.kswapd_high_wmark_hit_quickly() >= 0);
            Assertions.assertTrue(vmstat.pageoutrun() >= 0);
            Assertions.assertTrue(vmstat.pgrotated() >= 0);
            Assertions.assertTrue(vmstat.drop_pagecache() >= 0);
            Assertions.assertTrue(vmstat.drop_slab() >= 0);
            Assertions.assertTrue(vmstat.pgmigrate_success() >= 0);
            Assertions.assertTrue(vmstat.pgmigrate_fail() >= 0);
            Assertions.assertTrue(vmstat.compact_migrate_scanned() >= 0);
            Assertions.assertTrue(vmstat.compact_free_scanned() >= 0);
            Assertions.assertTrue(vmstat.compact_isolated() >= 0);
            Assertions.assertTrue(vmstat.compact_stall() >= 0);
            Assertions.assertTrue(vmstat.compact_fail() >= 0);
            Assertions.assertTrue(vmstat.compact_success() >= 0);
            Assertions.assertTrue(vmstat.htlb_buddy_alloc_success() >= 0);
            Assertions.assertTrue(vmstat.htlb_buddy_alloc_fail() >= 0);
            Assertions.assertTrue(vmstat.unevictable_pgs_culled() >= 0);
            Assertions.assertTrue(vmstat.unevictable_pgs_scanned() >= 0);
            Assertions.assertTrue(vmstat.unevictable_pgs_rescued() >= 0);
            Assertions.assertTrue(vmstat.unevictable_pgs_mlocked() >= 0);
            Assertions.assertTrue(vmstat.unevictable_pgs_munlocked() >= 0);
            Assertions.assertTrue(vmstat.unevictable_pgs_cleared() >= 0);
            Assertions.assertTrue(vmstat.unevictable_pgs_stranded() >= 0);
            Assertions.assertTrue(vmstat.thp_fault_alloc() >= 0);
            Assertions.assertTrue(vmstat.thp_fault_fallback() >= 0);
            Assertions.assertTrue(vmstat.thp_collapse_alloc() >= 0);
            Assertions.assertTrue(vmstat.thp_collapse_alloc_failed() >= 0);
            Assertions.assertTrue(vmstat.thp_zero_page_alloc() >= 0);
            Assertions.assertTrue(vmstat.thp_zero_page_alloc_failed() >= 0);
        });
    }

    // CPUInfo should have 27 fields for every logical cpu (cpuThread) listed.
    @Test
    public void cpuInfoTest() {
        Assertions.assertDoesNotThrow(() -> {
            LinuxOS os = new LinuxOS();
            Assertions.assertEquals(os.cpuThreadCount() * 27, os.cpuinfo().read().size());
        });
    }

    // MemInfo should have exactly 34 fields, and object should be initialized with non-negative integer values.
    @Test
    public void memInfoTest() {
        Assertions.assertDoesNotThrow(() -> {
            LinuxOS os = new LinuxOS();
            Meminfo meminfo = os.meminfo();
            Assertions.assertEquals(34, meminfo.read().size());
            Assertions.assertTrue(meminfo.MemTotal() >= 0);
            Assertions.assertTrue(meminfo.MemFree() >= 0);
            Assertions.assertTrue(meminfo.MemAvailable() >= 0);
            Assertions.assertTrue(meminfo.Buffers() >= 0);
            Assertions.assertTrue(meminfo.Cached() >= 0);
            Assertions.assertTrue(meminfo.SwapCached() >= 0);
            Assertions.assertTrue(meminfo.Active() >= 0);
            Assertions.assertTrue(meminfo.Inactive() >= 0);
            Assertions.assertTrue(meminfo.Activeanon() >= 0);
            Assertions.assertTrue(meminfo.Inactiveanon() >= 0);
            Assertions.assertTrue(meminfo.Activefile() >= 0);
            Assertions.assertTrue(meminfo.Inactivefile() >= 0);
            Assertions.assertTrue(meminfo.SwapTotal() >= 0);
            Assertions.assertTrue(meminfo.SwapFree() >= 0);
            Assertions.assertTrue(meminfo.Dirty() >= 0);
            Assertions.assertTrue(meminfo.Writeback() >= 0);
            Assertions.assertTrue(meminfo.AnonPages() >= 0);
            Assertions.assertTrue(meminfo.Mapped() >= 0);
            Assertions.assertTrue(meminfo.Shmem() >= 0);
            Assertions.assertTrue(meminfo.KReclaimable() >= 0);
            Assertions.assertTrue(meminfo.Slab() >= 0);
            Assertions.assertTrue(meminfo.SReclaimable() >= 0);
            Assertions.assertTrue(meminfo.SUnreclaim() >= 0);
            Assertions.assertTrue(meminfo.KernelStack() >= 0);
            Assertions.assertTrue(meminfo.PageTables() >= 0);
            Assertions.assertTrue(meminfo.NFS_Unstable() >= 0);
            Assertions.assertTrue(meminfo.Bounce() >= 0);
            Assertions.assertTrue(meminfo.WritebackTmp() >= 0);
            Assertions.assertTrue(meminfo.CommitLimit() >= 0);
            Assertions.assertTrue(meminfo.Committed_AS() >= 0);
            Assertions.assertTrue(meminfo.VmallocTotal() >= 0);
            Assertions.assertTrue(meminfo.VmallocUsed() >= 0);
            Assertions.assertTrue(meminfo.VmallocChunk() >= 0);
            Assertions.assertTrue(meminfo.DirectMap4k() >= 0);
        });
    }

    // Check that cpuCount and physicalCoreCount returns a positive integer
    // PageSize and totalRAM should return a positive float
    // TotalRAM should never exceed memory allocated to JVM
    // CpuThreadCount matches Runtime.availableProcessors()
    // CpuThreadCount should either match or be greater than the number of physical cores
    // CpuThreadCount and number of physical cores should be greater or equal to the number of CPUs installed
    @Test
    public void highLevelMethodsTest() {
        Assertions.assertDoesNotThrow(() -> {
            LinuxOS os = new LinuxOS();
            Assertions.assertEquals(true, Integer.signum(os.cpuCount()) == 1);
            Assertions.assertEquals(Runtime.getRuntime().availableProcessors(), os.cpuThreadCount());
            Assertions.assertEquals(true, Integer.signum(os.cpuPhysicalCoreCount()) == 1);
            Assertions.assertTrue(os.cpuThreadCount() >= os.cpuPhysicalCoreCount());
            Assertions.assertTrue(os.cpuThreadCount() >= os.cpuCount() && os.cpuPhysicalCoreCount() >= os.cpuCount());
            Assertions.assertEquals(true, Integer.signum(os.cpuCount()) == 1);
            Assertions.assertTrue(os.pageSize() > 0);
            Assertions.assertTrue(os.totalRAM() > Runtime.getRuntime().maxMemory() / 1000);
        });
    }
}
