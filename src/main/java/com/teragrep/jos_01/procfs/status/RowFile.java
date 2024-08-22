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
package com.teragrep.jos_01.procfs.status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.time.Instant;
import java.util.ArrayList;

public class RowFile extends File implements Text {

    private final BufferedReader reader;
    private final Instant timestamp;
    private final ArrayList<String> fileRows;
    private final Logger LOGGER = LoggerFactory.getLogger(RowFile.class);

    public RowFile(File procDirectory, String fileName) throws Exception {
        this(new File(procDirectory, fileName));
    }

    public RowFile(File procFile) throws Exception {
        this(procFile, new BufferedReader(new FileReader(procFile)), new ArrayList<String>());
    }

    public RowFile(File procFile, BufferedReader reader, ArrayList<String> fileRows) throws Exception {
        super(procFile.toURI());
        this.reader = reader;
        this.timestamp = Instant.now();
        this.fileRows = fileRows;
    }

    @Override
    public ArrayList<String> read() throws Exception {
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                fileRows.add(line);
            }
        }
        catch (Exception e) {
            throw new Exception("Failed to read from file!", e);
        }
        return fileRows;
    }

    @Override
    public Instant timestamp() {
        return timestamp;
    }
}
