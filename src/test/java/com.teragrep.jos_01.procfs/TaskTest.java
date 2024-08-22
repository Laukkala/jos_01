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

public class TaskTest {

    private final Logger LOGGER = LoggerFactory.getLogger(TaskTest.class);

    @Test
    public void statTest() {
        Assertions.assertDoesNotThrow(() -> {
            Process process = new Process(1);
            Task task = process.tasks().get(0);
            Stat stat = task.stat();

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
            Task task = process.tasks().get(0);
            Statm statm = task.statm();
            Assertions.assertNotNull(statm.size());
            Assertions.assertNotNull(statm.resident());
            Assertions.assertNotNull(statm.shared());
            Assertions.assertNotNull(statm.text());
            Assertions.assertNotNull(statm.lib());
            Assertions.assertNotNull(statm.data());
            Assertions.assertNotNull(statm.dt());
        });
    }

    @Test
    public void timestampTest() {
        Assertions.assertDoesNotThrow(() -> {
            Process systemd = new Process(1);
            Task task = systemd.tasks().get(0);
            Stat stat = task.stat();
            Stat stat2 = task.stat();
            Statm statm = task.statm();

            // Timestamps should always have a value
            Assertions.assertNotNull(stat.timestamp());
            Assertions.assertNotNull(statm.timestamp());

            // Timestamps should be different if called at different times, even when calling the same method again
            Assertions.assertFalse(stat.timestamp().equals(stat2.timestamp()));
            Assertions.assertFalse(stat.timestamp().equals(statm.timestamp()));
        });
    }
}
