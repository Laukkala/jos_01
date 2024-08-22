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
package com.teragrep.jos_01.procfs.status.process;

import com.teragrep.jos_01.procfs.status.CharacterDelimited;
import com.teragrep.jos_01.procfs.status.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.time.Instant;
import java.util.ArrayList;

// Provides general information about a Process' status.
// Stat should always have 52 fields in a single row, delimited by a spacebar.
// Fields can be either strings or integers.
public class Stat implements Text {

    private final Logger LOGGER = LoggerFactory.getLogger(Stat.class);
    private final Instant timestamp;
    private final ArrayList<String> fields;
    private final long pid;
    private final String comm;
    private final String state;
    private final long ppid;
    private final long pgrp;
    private final long session;
    private final long tty_nr;
    private final long tpgid;
    private final long flags;
    private final long minflt;
    private final long cminflt;
    private final long majflt;
    private final long cmajflt;
    private final long utime;
    private final long stime;
    private final long cutime;
    private final long cstime;
    private final long priority;
    private final long nice;
    private final long num_threads;
    private final long itrealvalue;
    private final long starttime;
    private final long vsize;
    private final long rss;
    private final BigInteger rsslim;
    private final long startcode;
    private final long endcode;
    private final long startstack;
    private final long kstkesp;
    private final long kstkeip;
    private final long signal;
    private final long blocked;
    private final long sigignore;
    private final long sigcatch;
    private final long wchan;
    private final long nswap;
    private final long cnswap;
    private final long exit_signal;
    private final long processor;
    private final long rt_priority;
    private final long policy;
    private final long delayacct_blkio_ticks;
    private final long guest_time;
    private final long cguest_time;
    private final long start_data;
    private final long end_data;
    private final long start_brk;
    private final long arg_start;
    private final long arg_end;
    private final long env_start;
    private final long env_end;
    private final long exit_code;

    public Stat(Text origin) throws Exception {
        fields = new CharacterDelimited(origin, " ").read();
        pid = Long.parseLong(fields.get(0));
        comm = fields.get(1);
        state = fields.get(2);
        ppid = Long.parseLong(fields.get(3));
        pgrp = Long.parseLong(fields.get(4));
        session = Long.parseLong(fields.get(5));
        tty_nr = Long.parseLong(fields.get(6));
        tpgid = Long.parseLong(fields.get(7));
        flags = Long.parseLong(fields.get(8));
        minflt = Long.parseLong(fields.get(9));
        cminflt = Long.parseLong(fields.get(10));
        majflt = Long.parseLong(fields.get(11));
        cmajflt = Long.parseLong(fields.get(12));
        utime = Long.parseLong(fields.get(13));
        stime = Long.parseLong(fields.get(14));
        cutime = Long.parseLong(fields.get(15));
        cstime = Long.parseLong(fields.get(16));
        priority = Long.parseLong(fields.get(17));
        nice = Long.parseLong(fields.get(18));
        num_threads = Long.parseLong(fields.get(19));
        itrealvalue = Long.parseLong(fields.get(20));
        starttime = Long.parseLong(fields.get(21));
        vsize = Long.parseLong(fields.get(22));
        rss = Long.parseLong(fields.get(23));
        rsslim = new BigInteger(fields.get(24));
        startcode = Long.parseLong(fields.get(25));
        endcode = Long.parseLong(fields.get(26));
        startstack = Long.parseLong(fields.get(27));
        kstkesp = Long.parseLong(fields.get(28));
        kstkeip = Long.parseLong(fields.get(29));
        signal = Long.parseLong(fields.get(30));
        blocked = Long.parseLong(fields.get(31));
        sigignore = Long.parseLong(fields.get(32));
        sigcatch = Long.parseLong(fields.get(33));
        wchan = Long.parseLong(fields.get(34));
        nswap = Long.parseLong(fields.get(35));
        cnswap = Long.parseLong(fields.get(36));
        exit_signal = Long.parseLong(fields.get(37));
        processor = Long.parseLong(fields.get(38));
        rt_priority = Long.parseLong(fields.get(39));
        policy = Long.parseLong(fields.get(40));
        delayacct_blkio_ticks = Long.parseLong(fields.get(41));
        guest_time = Long.parseLong(fields.get(42));
        cguest_time = Long.parseLong(fields.get(43));
        start_data = Long.parseLong(fields.get(44));
        end_data = Long.parseLong(fields.get(45));
        start_brk = Long.parseLong(fields.get(46));
        arg_start = Long.parseLong(fields.get(47));
        arg_end = Long.parseLong(fields.get(48));
        env_start = Long.parseLong(fields.get(49));
        env_end = Long.parseLong(fields.get(50));
        exit_code = Long.parseLong(fields.get(51));
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

    public long pid() {
        return pid;
    }

    public String comm() {
        return comm;
    }

    public String state() {
        return state;
    }

    public long ppid() {
        return ppid;
    }

    public long pgrp() {
        return pgrp;
    }

    public long session() {
        return session;
    }

    public long tty_nr() {
        return tty_nr;
    }

    public long tpgid() {
        return tpgid;
    }

    public long flags() {
        return flags;
    }

    public long minflt() {
        return minflt;
    }

    public long cminflt() {
        return cminflt;
    }

    public long majflt() {
        return majflt;
    }

    public long cmajflt() {
        return cmajflt;
    }

    public long utime() {
        return utime;
    }

    public long stime() {
        return stime;
    }

    public long cutime() {
        return cutime;
    }

    public long cstime() {
        return cstime;
    }

    public long priority() {
        return priority;
    }

    public long nice() {
        return nice;
    }

    public long num_threads() {
        return num_threads;
    }

    public long itrealvalue() {
        return itrealvalue;
    }

    public long starttime() {
        return starttime;
    }

    public long vsize() {
        return vsize;
    }

    public long rss() {
        return rss;
    }

    public BigInteger rsslim() {
        return rsslim;
    }

    public long startcode() {
        return startcode;
    }

    public long endcode() {
        return endcode;
    }

    public long startstack() {
        return startstack;
    }

    public long kstkesp() {
        return kstkesp;
    }

    public long kstkeip() {
        return kstkeip;
    }

    public long signal() {
        return signal;
    }

    public long blocked() {
        return blocked;
    }

    public long sigignore() {
        return sigignore;
    }

    public long sigcatch() {
        return sigcatch;
    }

    public long wchan() {
        return wchan;
    }

    public long nswap() {
        return nswap;
    }

    public long cnswap() {
        return cnswap;
    }

    public long exit_signal() {
        return exit_signal;
    }

    public long processor() {
        return processor;
    }

    public long rt_priority() {
        return rt_priority;
    }

    public long policy() {
        return policy;
    }

    public long delayacct_blkio_ticks() {
        return delayacct_blkio_ticks;
    }

    public long guest_time() {
        return guest_time;
    }

    public long cguest_time() {
        return cguest_time;
    }

    public long start_data() {
        return start_data;
    }

    public long end_data() {
        return end_data;
    }

    public long start_brk() {
        return start_brk;
    }

    public long arg_start() {
        return arg_start;
    }

    public long arg_end() {
        return arg_end;
    }

    public long env_start() {
        return env_start;
    }

    public long env_end() {
        return env_end;
    }

    public long exit_code() {
        return exit_code;
    }
}
