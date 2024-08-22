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

import com.teragrep.jos_01.procfs.status.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.ArrayList;

// Provides information about memory usage, measured in pages.
// Some of these values are inaccurate because of a kernel-internal scalability optimization.

// Meminfo may contain different number of fields on different systems, depending on Linux version and configuration.
// This implementation includes fields listed in https://man7.org/linux/man-pages/man5/procfs.5.html that don't require a configuration setting to be set.
// Fields are separated by rows, and contain a name and a value separated by a colon character (:)
// Some values are appended by a metric, such as 'kB'.
// Values are integers.
public class Meminfo implements Text {

    private final Logger LOGGER = LoggerFactory.getLogger(Meminfo.class);
    private final Instant timestamp;
    private final ArrayList<String> fields;
    private final long MemTotal;
    private final long MemFree;
    private final long MemAvailable;
    private final long Buffers;
    private final long Cached;
    private final long SwapCached;
    private final long Active;
    private final long Inactive;
    private final long Activeanon;
    private final long Inactiveanon;
    private final long Activefile;
    private final long Inactivefile;
    private final long SwapTotal;
    private final long SwapFree;
    private final long Dirty;
    private final long Writeback;
    private final long AnonPages;
    private final long Mapped;
    private final long Shmem;
    private final long KReclaimable;
    private final long Slab;
    private final long SReclaimable;
    private final long SUnreclaim;
    private final long KernelStack;
    private final long PageTables;
    private final long NFS_Unstable;
    private final long Bounce;
    private final long WritebackTmp;
    private final long CommitLimit;
    private final long Committed_AS;
    private final long VmallocTotal;
    private final long VmallocUsed;
    private final long VmallocChunk;
    private final long DirectMap4k;

    public Meminfo(Text origin) throws Exception {
        fields = new Digits(
                new Trimmed(
                        new Replaced(
                                new CharacterDelimited(
                                        new Matched(
                                                origin,
                                                "MemTotal.*|MemFree.*|MemAvailable.*|Buffers.*|Cached.*|SwapCached.*|Active.*|Inactive.*|Activeanon.*|Inactiveanon.*|Activefile.*|Inactivefile.*|SwapTotal.*|SwapFree.*|Dirty.*|Writeback.*|AnonPages.*|Mapped.*|Shmem:.*|KReclaimable.*|Slab.*|SReclaimable.*|SUnreclaim.*|KernelStack.*|PageTables.*|NFS_Unstable.*|Bounce.*|WritebackTmp.*|CommitLimit.*|Committed_AS.*|VmallocTotal.*|VmallocUsed.*|VmallocChunk.*|DirectMap4k.*"
                                        ),
                                        ":"
                                ),
                                "kB",
                                ""
                        )
                )
        ).read();
        MemTotal = Long.parseLong(fields.get(0));
        MemFree = Long.parseLong(fields.get(1));
        MemAvailable = Long.parseLong(fields.get(2));
        Buffers = Long.parseLong(fields.get(3));
        Cached = Long.parseLong(fields.get(4));
        SwapCached = Long.parseLong(fields.get(5));
        Active = Long.parseLong(fields.get(6));
        Inactive = Long.parseLong(fields.get(7));
        Activeanon = Long.parseLong(fields.get(8));
        Inactiveanon = Long.parseLong(fields.get(9));
        Activefile = Long.parseLong(fields.get(10));
        Inactivefile = Long.parseLong(fields.get(11));
        SwapTotal = Long.parseLong(fields.get(12));
        SwapFree = Long.parseLong(fields.get(13));
        Dirty = Long.parseLong(fields.get(14));
        Writeback = Long.parseLong(fields.get(15));
        AnonPages = Long.parseLong(fields.get(16));
        Mapped = Long.parseLong(fields.get(17));
        Shmem = Long.parseLong(fields.get(18));
        KReclaimable = Long.parseLong(fields.get(19));
        Slab = Long.parseLong(fields.get(20));
        SReclaimable = Long.parseLong(fields.get(21));
        SUnreclaim = Long.parseLong(fields.get(22));
        KernelStack = Long.parseLong(fields.get(23));
        PageTables = Long.parseLong(fields.get(24));
        NFS_Unstable = Long.parseLong(fields.get(25));
        Bounce = Long.parseLong(fields.get(26));
        WritebackTmp = Long.parseLong(fields.get(27));
        CommitLimit = Long.parseLong(fields.get(28));
        Committed_AS = Long.parseLong(fields.get(29));
        VmallocTotal = Long.parseLong(fields.get(30));
        VmallocUsed = Long.parseLong(fields.get(31));
        VmallocChunk = Long.parseLong(fields.get(32));
        DirectMap4k = Long.parseLong(fields.get(33));
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

    public long MemTotal() {
        return MemTotal;
    }

    public long MemFree() {
        return MemFree;
    }

    public long MemAvailable() {
        return MemAvailable;
    }

    public long Buffers() {
        return Buffers;
    }

    public long Cached() {
        return Cached;
    }

    public long SwapCached() {
        return SwapCached;
    }

    public long Active() {
        return Active;
    }

    public long Inactive() {
        return Inactive;
    }

    public long Activeanon() {
        return Activeanon;
    }

    public long Inactiveanon() {
        return Inactiveanon;
    }

    public long Activefile() {
        return Activefile;
    }

    public long Inactivefile() {
        return Inactivefile;
    }

    public long SwapTotal() {
        return SwapTotal;
    }

    public long SwapFree() {
        return SwapFree;
    }

    public long Dirty() {
        return Dirty;
    }

    public long Writeback() {
        return Writeback;
    }

    public long AnonPages() {
        return AnonPages;
    }

    public long Mapped() {
        return Mapped;
    }

    public long Shmem() {
        return Shmem;
    }

    public long KReclaimable() {
        return KReclaimable;
    }

    public long Slab() {
        return Slab;
    }

    public long SReclaimable() {
        return SReclaimable;
    }

    public long SUnreclaim() {
        return SUnreclaim;
    }

    public long KernelStack() {
        return KernelStack;
    }

    public long PageTables() {
        return PageTables;
    }

    public long NFS_Unstable() {
        return NFS_Unstable;
    }

    public long Bounce() {
        return Bounce;
    }

    public long WritebackTmp() {
        return WritebackTmp;
    }

    public long CommitLimit() {
        return CommitLimit;
    }

    public long Committed_AS() {
        return Committed_AS;
    }

    public long VmallocTotal() {
        return VmallocTotal;
    }

    public long VmallocUsed() {
        return VmallocUsed;
    }

    public long VmallocChunk() {
        return VmallocChunk;
    }

    public long DirectMap4k() {
        return DirectMap4k;
    }

}
