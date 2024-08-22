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
package com.teragrep.jos_01.procfs.status.os;

import com.teragrep.jos_01.procfs.status.Matched;
import com.teragrep.jos_01.procfs.status.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;

// Vmstat does not always have predefined number of fields on all systems with different configurations.
// Different kernels have different configurations which influence what fields are shown.
// Fields are separated by rows.
// Fields include a name and a value, separated by a spacebar.
// All values are integers.
public class Vmstat implements Text {

    private final Logger LOGGER = LoggerFactory.getLogger(Vmstat.class);

    private final Instant timestamp;
    private final long nr_free_pages;
    private final long nr_inactive_anon;
    private final long nr_active_anon;
    private final long nr_inactive_file;
    private final long nr_active_file;
    private final long nr_unevictable;
    private final long nr_mlock;
    private final long nr_anon_pages;
    private final long nr_mapped;
    private final long nr_file_pages;
    private final long nr_dirty;
    private final long nr_writeback;
    private final long nr_slab_reclaimable;
    private final long nr_slab_unreclaimable;
    private final long nr_page_table_pages;
    private final long nr_kernel_stack;
    private final long nr_unstable;
    private final long nr_bounce;
    private final long nr_vmscan_write;
    private final long nr_vmscan_immediate_reclaim;
    private final long nr_writeback_temp;
    private final long nr_isolated_anon;
    private final long nr_isolated_file;
    private final long nr_shmem;
    private final long nr_dirtied;
    private final long nr_written;
    private final long numa_hit;
    private final long numa_miss;
    private final long numa_foreign;
    private final long numa_interleave;
    private final long numa_local;
    private final long numa_other;
    private final long nr_free_cma;
    private final long nr_dirty_threshold;
    private final long nr_dirty_background_threshold;
    private final long pgpgin;
    private final long pgpgout;
    private final long pswpin;
    private final long pswpout;
    private final long pgalloc_dma;
    private final long pgalloc_dma32;
    private final long pgalloc_normal;
    private final long pgalloc_movable;
    private final long pgfree;
    private final long pgactivate;
    private final long pgdeactivate;
    private final long pgfault;
    private final long pgmajfault;
    private final long pgscan_direct_throttle;
    private final long zone_reclaim_failed;
    private final long pginodesteal;
    private final long slabs_scanned;
    private final long kswapd_inodesteal;
    private final long kswapd_low_wmark_hit_quickly;
    private final long kswapd_high_wmark_hit_quickly;
    private final long pageoutrun;
    private final long pgrotated;
    private final long drop_pagecache;
    private final long drop_slab;
    private final long pgmigrate_success;
    private final long pgmigrate_fail;
    private final long compact_migrate_scanned;
    private final long compact_free_scanned;
    private final long compact_isolated;
    private final long compact_stall;
    private final long compact_fail;
    private final long compact_success;
    private final long htlb_buddy_alloc_success;
    private final long htlb_buddy_alloc_fail;
    private final long unevictable_pgs_culled;
    private final long unevictable_pgs_scanned;
    private final long unevictable_pgs_rescued;
    private final long unevictable_pgs_mlocked;
    private final long unevictable_pgs_munlocked;
    private final long unevictable_pgs_cleared;
    private final long unevictable_pgs_stranded;
    private final long thp_fault_alloc;
    private final long thp_fault_fallback;
    private final long thp_collapse_alloc;
    private final long thp_collapse_alloc_failed;
    private final long thp_zero_page_alloc;
    private final long thp_zero_page_alloc_failed;
    private final ArrayList<String> fields;

    public Vmstat(Text origin) throws Exception {
        fields = new Matched(
                origin,
                "nr_free_pages.*|nr_inactive_anon.*|nr_active_anon.*|nr_inactive_file.*|nr_active_file.*|nr_unevictable.*|nr_mlock.*|nr_anon_pages.*|nr_mapped.*|nr_file_pages.*|nr_dirty.*|nr_writeback.*|nr_slab_reclaimable.*|nr_slab_unreclaimable.*|nr_page_table_pages.*|nr_kernel_stack.*|nr_unstable.*|nr_bounce.*|nr_vmscan_write.*|nr_vmscan_immediate_reclaim.*|nr_writeback_temp.*|nr_isolated_anon.*|nr_isolated_file.*|nr_shmem.*|nr_dirtied.*|nr_written.*|numa_hit.*|numa_miss.*|numa_foreign.*|numa_interleave.*|numa_local.*|numa_other.*|nr_free_cma.*|nr_dirty_threshold.*|nr_dirty_background_threshold.*|pgpgin.*|pgpgout.*|pswpin.*|pswpout.*|pgalloc_dma.*|pgalloc_dma32.*|pgalloc_normal.*|pgalloc_movable.*|pgfree.*|pgactivate.*|pgdeactivate.*|pgfault.*|pgmajfault.*|pgscan_direct_throttle.*|zone_reclaim_failed.*|pginodesteal.*|slabs_scanned.*|kswapd_inodesteal.*|kswapd_low_wmark_hit_quickly.*|kswapd_high_wmark_hit_quickly.*|pageoutrun.*|pgrotated.*|drop_pagecache.*|drop_slab.*|pgmigrate_success.*|pgmigrate_fail.*|compact_migrate_scanned.*|compact_free_scanned.*|compact_isolated.*|compact_stall.*|compact_fail.*|compact_success.*|htlb_buddy_alloc_success.*|htlb_buddy_alloc_fail.*|unevictable_pgs_culled.*|unevictable_pgs_scanned.*|unevictable_pgs_rescued.*|unevictable_pgs_mlocked.*|unevictable_pgs_munlocked.*|unevictable_pgs_cleared.*|unevictable_pgs_stranded.*|thp_fault_alloc.*|thp_fault_fallback.*|thp_collapse_alloc.*|thp_collapse_alloc_failed.*|thp_zero_page_alloc.*|thp_zero_page_alloc_failed.*"
        ).read();
        LinkedHashMap<String, String> keyValuePairs = new LinkedHashMap<>();
        for (String field : fields) {
            String[] pair = field.split(" ");
            keyValuePairs.put(pair[0], pair[1]);
        }
        nr_free_pages = Long.parseLong(keyValuePairs.get("nr_free_pages"));
        nr_inactive_anon = Long.parseLong(keyValuePairs.get("nr_inactive_anon"));
        nr_active_anon = Long.parseLong(keyValuePairs.get("nr_active_anon"));
        nr_inactive_file = Long.parseLong(keyValuePairs.get("nr_inactive_file"));
        nr_active_file = Long.parseLong(keyValuePairs.get("nr_active_file"));
        nr_unevictable = Long.parseLong(keyValuePairs.get("nr_unevictable"));
        nr_mlock = Long.parseLong(keyValuePairs.get("nr_mlock"));
        nr_anon_pages = Long.parseLong(keyValuePairs.get("nr_anon_pages"));
        nr_mapped = Long.parseLong(keyValuePairs.get("nr_mapped"));
        nr_file_pages = Long.parseLong(keyValuePairs.get("nr_file_pages"));
        nr_dirty = Long.parseLong(keyValuePairs.get("nr_dirty"));
        nr_writeback = Long.parseLong(keyValuePairs.get("nr_writeback"));
        nr_slab_reclaimable = Long.parseLong(keyValuePairs.get("nr_slab_reclaimable"));
        nr_slab_unreclaimable = Long.parseLong(keyValuePairs.get("nr_slab_unreclaimable"));
        nr_page_table_pages = Long.parseLong(keyValuePairs.get("nr_page_table_pages"));
        nr_kernel_stack = Long.parseLong(keyValuePairs.get("nr_kernel_stack"));
        nr_unstable = Long.parseLong(keyValuePairs.get("nr_unstable"));
        nr_bounce = Long.parseLong(keyValuePairs.get("nr_bounce"));
        nr_vmscan_write = Long.parseLong(keyValuePairs.get("nr_vmscan_write"));
        nr_vmscan_immediate_reclaim = Long.parseLong(keyValuePairs.get("nr_vmscan_immediate_reclaim"));
        nr_writeback_temp = Long.parseLong(keyValuePairs.get("nr_writeback_temp"));
        nr_isolated_anon = Long.parseLong(keyValuePairs.get("nr_isolated_anon"));
        nr_isolated_file = Long.parseLong(keyValuePairs.get("nr_isolated_file"));
        nr_shmem = Long.parseLong(keyValuePairs.get("nr_shmem"));
        nr_dirtied = Long.parseLong(keyValuePairs.get("nr_dirtied"));
        nr_written = Long.parseLong(keyValuePairs.get("nr_written"));
        numa_hit = Long.parseLong(keyValuePairs.get("numa_hit"));
        numa_miss = Long.parseLong(keyValuePairs.get("numa_miss"));
        numa_foreign = Long.parseLong(keyValuePairs.get("numa_foreign"));
        numa_interleave = Long.parseLong(keyValuePairs.get("numa_interleave"));
        numa_local = Long.parseLong(keyValuePairs.get("numa_local"));
        numa_other = Long.parseLong(keyValuePairs.get("numa_other"));
        nr_free_cma = Long.parseLong(keyValuePairs.get("nr_free_cma"));
        nr_dirty_threshold = Long.parseLong(keyValuePairs.get("nr_dirty_threshold"));
        nr_dirty_background_threshold = Long.parseLong(keyValuePairs.get("nr_dirty_background_threshold"));
        pgpgin = Long.parseLong(keyValuePairs.get("pgpgin"));
        pgpgout = Long.parseLong(keyValuePairs.get("pgpgout"));
        pswpin = Long.parseLong(keyValuePairs.get("pswpin"));
        pswpout = Long.parseLong(keyValuePairs.get("pswpout"));
        pgalloc_dma = Long.parseLong(keyValuePairs.get("pgalloc_dma"));
        pgalloc_dma32 = Long.parseLong(keyValuePairs.get("pgalloc_dma32"));
        pgalloc_normal = Long.parseLong(keyValuePairs.get("pgalloc_normal"));
        pgalloc_movable = Long.parseLong(keyValuePairs.get("pgalloc_movable"));
        pgfree = Long.parseLong(keyValuePairs.get("pgfree"));
        pgactivate = Long.parseLong(keyValuePairs.get("pgactivate"));
        pgdeactivate = Long.parseLong(keyValuePairs.get("pgdeactivate"));
        pgfault = Long.parseLong(keyValuePairs.get("pgfault"));
        pgmajfault = Long.parseLong(keyValuePairs.get("pgmajfault"));
        pgscan_direct_throttle = Long.parseLong(keyValuePairs.get("pgscan_direct_throttle"));
        zone_reclaim_failed = Long.parseLong(keyValuePairs.get("zone_reclaim_failed"));
        pginodesteal = Long.parseLong(keyValuePairs.get("pginodesteal"));
        slabs_scanned = Long.parseLong(keyValuePairs.get("slabs_scanned"));
        kswapd_inodesteal = Long.parseLong(keyValuePairs.get("kswapd_inodesteal"));
        kswapd_low_wmark_hit_quickly = Long.parseLong(keyValuePairs.get("kswapd_low_wmark_hit_quickly"));
        kswapd_high_wmark_hit_quickly = Long.parseLong(keyValuePairs.get("kswapd_high_wmark_hit_quickly"));
        pageoutrun = Long.parseLong(keyValuePairs.get("pageoutrun"));
        pgrotated = Long.parseLong(keyValuePairs.get("pgrotated"));
        drop_pagecache = Long.parseLong(keyValuePairs.get("drop_pagecache"));
        drop_slab = Long.parseLong(keyValuePairs.get("drop_slab"));
        pgmigrate_success = Long.parseLong(keyValuePairs.get("pgmigrate_success"));
        pgmigrate_fail = Long.parseLong(keyValuePairs.get("pgmigrate_fail"));
        compact_migrate_scanned = Long.parseLong(keyValuePairs.get("compact_migrate_scanned"));
        compact_free_scanned = Long.parseLong(keyValuePairs.get("compact_free_scanned"));
        compact_isolated = Long.parseLong(keyValuePairs.get("compact_isolated"));
        compact_stall = Long.parseLong(keyValuePairs.get("compact_stall"));
        compact_fail = Long.parseLong(keyValuePairs.get("compact_fail"));
        compact_success = Long.parseLong(keyValuePairs.get("compact_success"));
        htlb_buddy_alloc_success = Long.parseLong(keyValuePairs.get("htlb_buddy_alloc_success"));
        htlb_buddy_alloc_fail = Long.parseLong(keyValuePairs.get("htlb_buddy_alloc_fail"));
        unevictable_pgs_culled = Long.parseLong(keyValuePairs.get("unevictable_pgs_culled"));
        unevictable_pgs_scanned = Long.parseLong(keyValuePairs.get("unevictable_pgs_scanned"));
        unevictable_pgs_rescued = Long.parseLong(keyValuePairs.get("unevictable_pgs_rescued"));
        unevictable_pgs_mlocked = Long.parseLong(keyValuePairs.get("unevictable_pgs_mlocked"));
        unevictable_pgs_munlocked = Long.parseLong(keyValuePairs.get("unevictable_pgs_munlocked"));
        unevictable_pgs_cleared = Long.parseLong(keyValuePairs.get("unevictable_pgs_cleared"));
        unevictable_pgs_stranded = Long.parseLong(keyValuePairs.get("unevictable_pgs_stranded"));
        thp_fault_alloc = Long.parseLong(keyValuePairs.get("thp_fault_alloc"));
        thp_fault_fallback = Long.parseLong(keyValuePairs.get("thp_fault_fallback"));
        thp_collapse_alloc = Long.parseLong(keyValuePairs.get("thp_collapse_alloc"));
        thp_collapse_alloc_failed = Long.parseLong(keyValuePairs.get("thp_collapse_alloc_failed"));
        thp_zero_page_alloc = Long.parseLong(keyValuePairs.get("thp_zero_page_alloc"));
        thp_zero_page_alloc_failed = Long.parseLong(keyValuePairs.get("thp_zero_page_alloc_failed"));
        timestamp = origin.timestamp();
    }

    @Override
    public ArrayList<String> read() {
        return fields;
    }

    public Instant timestamp() {
        return timestamp;
    }

    public void printTimestamp() {
        LOGGER.info(timestamp.toString());
    }

    public long nr_free_pages() {
        return nr_free_pages;
    }

    public long nr_inactive_anon() {
        return nr_inactive_anon;
    }

    public long nr_active_anon() {
        return nr_active_anon;
    }

    public long nr_inactive_file() {
        return nr_inactive_file;
    }

    public long nr_active_file() {
        return nr_active_file;
    }

    public long nr_unevictable() {
        return nr_unevictable;
    }

    public long nr_mlock() {
        return nr_mlock;
    }

    public long nr_anon_pages() {
        return nr_anon_pages;
    }

    public long nr_mapped() {
        return nr_mapped;
    }

    public long nr_file_pages() {
        return nr_file_pages;
    }

    public long nr_dirty() {
        return nr_dirty;
    }

    public long nr_writeback() {
        return nr_writeback;
    }

    public long nr_slab_reclaimable() {
        return nr_slab_reclaimable;
    }

    public long nr_slab_unreclaimable() {
        return nr_slab_unreclaimable;
    }

    public long nr_page_table_pages() {
        return nr_page_table_pages;
    }

    public long nr_kernel_stack() {
        return nr_kernel_stack;
    }

    public long nr_unstable() {
        return nr_unstable;
    }

    public long nr_bounce() {
        return nr_bounce;
    }

    public long nr_vmscan_write() {
        return nr_vmscan_write;
    }

    public long nr_vmscan_immediate_reclaim() {
        return nr_vmscan_immediate_reclaim;
    }

    public long nr_writeback_temp() {
        return nr_writeback_temp;
    }

    public long nr_isolated_anon() {
        return nr_isolated_anon;
    }

    public long nr_isolated_file() {
        return nr_isolated_file;
    }

    public long nr_shmem() {
        return nr_shmem;
    }

    public long nr_dirtied() {
        return nr_dirtied;
    }

    public long nr_written() {
        return nr_written;
    }

    public long numa_hit() {
        return numa_hit;
    }

    public long numa_miss() {
        return numa_miss;
    }

    public long numa_foreign() {
        return numa_foreign;
    }

    public long numa_interleave() {
        return numa_interleave;
    }

    public long numa_local() {
        return numa_local;
    }

    public long numa_other() {
        return numa_other;
    }

    public long nr_free_cma() {
        return nr_free_cma;
    }

    public long nr_dirty_threshold() {
        return nr_dirty_threshold;
    }

    public long nr_dirty_background_threshold() {
        return nr_dirty_background_threshold;
    }

    public long pgpgin() {
        return pgpgin;
    }

    public long pgpgout() {
        return pgpgout;
    }

    public long pswpin() {
        return pswpin;
    }

    public long pswpout() {
        return pswpout;
    }

    public long pgalloc_dma() {
        return pgalloc_dma;
    }

    public long pgalloc_dma32() {
        return pgalloc_dma32;
    }

    public long pgalloc_normal() {
        return pgalloc_normal;
    }

    public long pgalloc_movable() {
        return pgalloc_movable;
    }

    public long pgfree() {
        return pgfree;
    }

    public long pgactivate() {
        return pgactivate;
    }

    public long pgdeactivate() {
        return pgdeactivate;
    }

    public long pgfault() {
        return pgfault;
    }

    public long pgmajfault() {
        return pgmajfault;
    }

    public long pgscan_direct_throttle() {
        return pgscan_direct_throttle;
    }

    public long zone_reclaim_failed() {
        return zone_reclaim_failed;
    }

    public long pginodesteal() {
        return pginodesteal;
    }

    public long slabs_scanned() {
        return slabs_scanned;
    }

    public long kswapd_inodesteal() {
        return kswapd_inodesteal;
    }

    public long kswapd_low_wmark_hit_quickly() {
        return kswapd_low_wmark_hit_quickly;
    }

    public long kswapd_high_wmark_hit_quickly() {
        return kswapd_high_wmark_hit_quickly;
    }

    public long pageoutrun() {
        return pageoutrun;
    }

    public long pgrotated() {
        return pgrotated;
    }

    public long drop_pagecache() {
        return drop_pagecache;
    }

    public long drop_slab() {
        return drop_slab;
    }

    public long pgmigrate_success() {
        return pgmigrate_success;
    }

    public long pgmigrate_fail() {
        return pgmigrate_fail;
    }

    public long compact_migrate_scanned() {
        return compact_migrate_scanned;
    }

    public long compact_free_scanned() {
        return compact_free_scanned;
    }

    public long compact_isolated() {
        return compact_isolated;
    }

    public long compact_stall() {
        return compact_stall;
    }

    public long compact_fail() {
        return compact_fail;
    }

    public long compact_success() {
        return compact_success;
    }

    public long htlb_buddy_alloc_success() {
        return htlb_buddy_alloc_success;
    }

    public long htlb_buddy_alloc_fail() {
        return htlb_buddy_alloc_fail;
    }

    public long unevictable_pgs_culled() {
        return unevictable_pgs_culled;
    }

    public long unevictable_pgs_scanned() {
        return unevictable_pgs_scanned;
    }

    public long unevictable_pgs_rescued() {
        return unevictable_pgs_rescued;
    }

    public long unevictable_pgs_mlocked() {
        return unevictable_pgs_mlocked;
    }

    public long unevictable_pgs_munlocked() {
        return unevictable_pgs_munlocked;
    }

    public long unevictable_pgs_cleared() {
        return unevictable_pgs_cleared;
    }

    public long unevictable_pgs_stranded() {
        return unevictable_pgs_stranded;
    }

    public long thp_fault_alloc() {
        return thp_fault_alloc;
    }

    public long thp_fault_fallback() {
        return thp_fault_fallback;
    }

    public long thp_collapse_alloc() {
        return thp_collapse_alloc;
    }

    public long thp_collapse_alloc_failed() {
        return thp_collapse_alloc_failed;
    }

    public long thp_zero_page_alloc() {
        return thp_zero_page_alloc;
    }

    public long thp_zero_page_alloc_failed() {
        return thp_zero_page_alloc_failed;
    }

}
