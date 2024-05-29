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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Uptime implements Status {

    private final ArrayList<String> rows;
    private final LocalDateTime timestamp;
    private final Map<String, String> statistics;
    private enum fields {
        uptimeSeconds, combinedCpuCoreIdleTimeSeconds
    };

    public Uptime(ArrayList<String> rows) {
        this.rows = rows;
        statistics = new LinkedHashMap<String, String>();
        for (String row : rows) {
            Pattern pattern = Pattern.compile("(?<uptimeSeconds>\\d+.\\d+) (?<combinedCpuCoreIdleTimeSeconds>\\d+.\\d+)");
            Matcher matcher = pattern.matcher(row);
            if(matcher.find()){
                for(int i = 0; i < matcher.groupCount();i++){
                    statistics.put(fields.values()[i].name(),matcher.group(fields.values()[i].name()));
                }
            }
        }
        timestamp = LocalDateTime.now();
    }

    public Map<String, String> statistics() {
        return statistics;
    }

    public void printStatistics() {
        for (Map.Entry<String, String> statistic : statistics.entrySet()) {
            System.out.print(statistic.getKey() + ": ");
            System.out.println(statistic.getValue());
        }
    }

    public ArrayList<String> rows() {
        return this.rows;
    }

    public LocalDateTime timestamp() {
        return timestamp;
    }

    public void printTimestamp() {
        System.out.println(timestamp);
    }
}
