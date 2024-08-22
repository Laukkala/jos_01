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

import java.time.Instant;
import java.util.*;

// Provides information about memory usage of the process.
// Fields are on a single row, separated by spacebars. Contains 7 fields.
// All fields are integers.
public class Statm implements Text {

    private final Logger LOGGER = LoggerFactory.getLogger(Statm.class);
    private final Instant timestamp;
    private final long size;
    private final long resident;
    private final long shared;
    private final long text;
    private final long lib;
    private final long data;
    private final long dt;
    private final ArrayList<String> fields;

    public Statm(Text origin) throws Exception {
        fields = new CharacterDelimited(origin, " ").read();
        size = Long.parseLong(fields.get(0));
        resident = Long.parseLong(fields.get(1));
        shared = Long.parseLong(fields.get(2));
        text = Long.parseLong(fields.get(3));
        lib = Long.parseLong(fields.get(4));
        data = Long.parseLong(fields.get(5));
        dt = Long.parseLong(fields.get(6));
        timestamp = origin.timestamp();

    }

    @Override
    public ArrayList<String> read() {
        return fields;
    }

    public void printStatistics() {
        LOGGER.info("{}\n{}\n{}\n{}\n{}\n{}\n{}\n", size, resident, shared, text, lib, data, dt);
    }

    public Instant timestamp() {
        return timestamp;
    }

    public void printTimestamp() {
        LOGGER.info(timestamp.toString());
    }

    public long size() {
        return size;
    }

    public long resident() {
        return resident;
    }

    public long shared() {
        return shared;
    }

    public long text() {
        return text;
    }

    public long lib() {
        return lib;
    }

    public long data() {
        return data;
    }

    public long dt() {
        return dt;
    }
}//
