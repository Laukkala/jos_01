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

// Provides general information about the Operating System's status.
// Stat has 7 standard fields and one field for each logical CPU as well as one field for aggregated CPU information.
// Fields are separated by rows.
// Fields can include multiple values, first value is a string indicating the name of the field.
// All values are integers, but may be large.
public class Stat implements Text {

    private final Logger LOGGER = LoggerFactory.getLogger(Stat.class);
    private final Instant timestamp;
    private final ArrayList<String> fields;
    private final ArrayList<ArrayList<Long>> cpus;
    private final ArrayList<Long> intr;
    private final ArrayList<Long> softirq;
    private final long ctxt;
    private final long btime;
    private final long processes;
    private final long procs_running;
    private final long procs_blocked;

    public Stat(Text origin) throws Exception {
        timestamp = origin.timestamp();
        fields = new TimeaddedText(new Replaced(origin, " +", " ")).read();
        cpus = new ArrayList<ArrayList<Long>>();
        intr = new ArrayList<Long>();
        softirq = new ArrayList<Long>();
        for (String field : fields) {
            if (field.startsWith("cpu")) {
                ArrayList<String> cpuValues = new Digits(new CharacterDelimited(new TimeaddedText(field), " ")).read();
                ArrayList<Long> cpuLongs = new ArrayList<Long>();
                for (String value : cpuValues) {
                    cpuLongs.add(Long.parseLong(value));
                }
                cpus.add(cpuLongs);
            }
            if (field.startsWith("intr")) {
                ArrayList<String> intrValues = new Digits(new CharacterDelimited(new TimeaddedText(field), " ")).read();
                for (String value : intrValues) {
                    intr.add(Long.parseLong(value));
                }
            }
            if (field.startsWith("softirq")) {
                ArrayList<String> softIrqValues = new Digits(new CharacterDelimited(new TimeaddedText(field), " "))
                        .read();
                ArrayList<Long> softIrqLongs = new ArrayList<Long>();
                for (String value : softIrqValues) {
                    softirq.add(Long.parseLong(value));
                }
            }
        }

        ctxt = Long.parseLong(fields.get(cpus.size() + 2).split(" ")[1]);
        btime = Long.parseLong(fields.get(cpus.size() + 3).split(" ")[1]);
        processes = Long.parseLong(fields.get(cpus.size() + 4).split(" ")[1]);
        procs_running = Long.parseLong(fields.get(cpus.size() + 5).split(" ")[1]);
        procs_blocked = Long.parseLong(fields.get(cpus.size() + 6).split(" ")[1]);
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

    public ArrayList<ArrayList<Long>> cpus() {
        return cpus;
    }

    public ArrayList<Long> intr() {
        return intr;
    }

    public ArrayList<Long> softirq() {
        return softirq;
    }

    public long ctxt() {
        return ctxt;
    }

    public long btime() {
        return btime;
    }

    public long processes() {
        return processes;
    }

    public long procs_running() {
        return procs_running;
    }

    public long procs_blocked() {
        return procs_blocked;
    }

}
