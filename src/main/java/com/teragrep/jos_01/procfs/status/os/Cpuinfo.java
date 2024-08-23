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
import java.util.*;

// Provides information about CPUs.
// There is 27 fields for each logical CPU. Every logical CPU is separated by an empty line, each field has a name and value is separated by whitespace and a colon character (:).
// Fields can be either strings, integers or floating point numbers. Some fields contain multiple values with variable counts. Some fields may have empty values.
public class Cpuinfo implements Text {

    private final Logger LOGGER = LoggerFactory.getLogger(Cpuinfo.class);
    private final Instant timestamp;
    private final ArrayList<Processor> processors;
    private final ArrayList<String> fields;

    public Cpuinfo(Text origin) throws Exception {
        fields = new NonEmptyLines(new Replaced(origin, "\\s+", " ")).read();
        this.processors = new ArrayList<Processor>();
        Map<String, String> processorFields = new HashMap();
        for (String field : fields) {
            if (field.startsWith("processor") && processorFields.size() != 0) {
                processors.add(new Processor(processorFields));
                processorFields = new HashMap<>();
            }
            ArrayList<String> keyValuePair = new Trimmed(new CharacterDelimited((new TimeaddedText(field)), ":"))
                    .read();
            String key;
            String value;
            key = keyValuePair.get(0);
            if (keyValuePair.size() == 1) {
                value = "";
            }
            else {
                value = keyValuePair.get(1);
            }

            processorFields.put(key, value);
        }
        processors.add(new Processor(processorFields));
        this.timestamp = origin.timestamp();
    }

    public ArrayList<Processor> processors() {
        return processors;
    }

    public int cpuPhysicalCoreCount() {
        Map<Integer, Integer> coreCounts = new HashMap<>();
        for (Processor processor : processors) {
            coreCounts.put(processor.physical_id, processor.cpu_cores);
        }
        int coreCount = 0;
        for (Map.Entry<Integer, Integer> physicalCPU : coreCounts.entrySet()) {
            coreCount = coreCount + physicalCPU.getValue();
        }
        return coreCount;
    }

    public int cpuCount() {
        ArrayList<Integer> physicalIds = new ArrayList<Integer>();
        for (Processor processor : processors) {
            physicalIds.add(processor.physical_id);
        }
        Map<Integer, Integer> distinct = new HashMap<>();
        for (int physicalId : physicalIds) {
            distinct.put(physicalId, distinct.getOrDefault(physicalId, 0) + 1);
        }
        return distinct.size();
    }

    public int cpuThreadCount() {
        return processors.size();
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

    private static class Processor {

        private final int processor;
        private final String vendor_id;
        private final String cpu_family;
        private final String model;
        private final String model_name;
        private final int stepping;
        private final String microcode;
        private final float cpu_MHz;
        private final String cache_size;
        private final int physical_id;
        private final int siblings;
        private final int core_id;
        private final int cpu_cores;
        private final int apicid;
        private final int initial_apicid;
        private final String fpu;
        private final String fpu_exception;
        private final int cpuid_level;
        private final String wp;
        private final String[] flags;
        private final String[] vmx_flags;
        private final String[] bugs;
        private final float bogomips;
        private final String TLB_size;
        private final int clflush_size;
        private final int cache_alignment;
        private final String[] address_sizes;
        private final String power_management;

        Processor(Map<String, String> processorFields) {
            this.processor = Integer.parseInt(processorFields.getOrDefault("processor", ""));
            this.vendor_id = processorFields.getOrDefault("vendor_id", "");
            this.cpu_family = processorFields.getOrDefault("cpu family", "");
            this.model = processorFields.getOrDefault("model", "");
            this.model_name = processorFields.getOrDefault("model name", "");
            this.stepping = Integer.parseInt(processorFields.getOrDefault("stepping", ""));
            this.microcode = processorFields.getOrDefault("microcode", "");
            this.cpu_MHz = Float.parseFloat(processorFields.getOrDefault("cpu MHz", ""));
            this.cache_size = processorFields.getOrDefault("cache size", "");
            this.physical_id = Integer.parseInt(processorFields.getOrDefault("physical id", ""));
            this.siblings = Integer.parseInt(processorFields.getOrDefault("siblings", ""));
            this.core_id = Integer.parseInt(processorFields.getOrDefault("core id", ""));
            this.cpu_cores = Integer.parseInt(processorFields.getOrDefault("cpu cores", ""));
            this.apicid = Integer.parseInt(processorFields.getOrDefault("apicid", ""));
            this.initial_apicid = Integer.parseInt(processorFields.getOrDefault("initial apicid", ""));
            this.fpu = processorFields.getOrDefault("fpu", "");
            this.fpu_exception = processorFields.getOrDefault("fpu_exception", "");
            this.cpuid_level = Integer.parseInt(processorFields.getOrDefault("cpuid level", ""));
            this.wp = processorFields.getOrDefault("wp", "");
            this.flags = processorFields.getOrDefault("flags", "").split(" ");
            this.vmx_flags = processorFields.getOrDefault("vmx_flags", "").split(" ");
            this.bugs = processorFields.getOrDefault("bugs", "").split(" ");
            this.bogomips = Float.parseFloat(processorFields.getOrDefault("bogomips", ""));
            this.TLB_size = processorFields.getOrDefault("TLB size", "");
            this.clflush_size = Integer.parseInt(processorFields.getOrDefault("clflush size", ""));
            this.cache_alignment = Integer.parseInt(processorFields.getOrDefault("cache_alignment", ""));
            this.address_sizes = processorFields.getOrDefault("address sizes", "").split(",");
            this.power_management = processorFields.getOrDefault("power management", "");
        }

        Processor(
                String processor,
                String vendor_id,
                String cpu_family,
                String model,
                String model_name,
                String stepping,
                String microcode,
                String cpu_MHz,
                String cache_size,
                String physical_id,
                String siblings,
                String core_id,
                String cpu_cores,
                String apicid,
                String initial_apicid,
                String fpu,
                String fpu_exception,
                String cpuid_level,
                String wp,
                String flags,
                String vmx_flags,
                String bugs,
                String bogomips,
                String TLB_size,
                String clflush_size,
                String cache_alignment,
                String address_sizes,
                String power_management,
                Map<String, String> processorFields
        ) {
            this.processor = Integer.parseInt(processor);
            this.vendor_id = vendor_id;
            this.cpu_family = cpu_family;
            this.model = model;
            this.model_name = model_name;
            this.stepping = Integer.parseInt(stepping);
            this.microcode = microcode;
            this.cpu_MHz = Float.parseFloat(cpu_MHz);
            this.cache_size = cache_size;
            this.physical_id = Integer.parseInt(physical_id);
            this.siblings = Integer.parseInt(siblings);
            this.core_id = Integer.parseInt(core_id);
            this.cpu_cores = Integer.parseInt(cpu_cores);
            this.apicid = Integer.parseInt(apicid);
            this.initial_apicid = Integer.parseInt(initial_apicid);
            this.fpu = fpu;
            this.fpu_exception = fpu_exception;
            this.cpuid_level = Integer.parseInt(cpuid_level);
            this.wp = wp;
            this.flags = flags.split(" ");
            this.vmx_flags = vmx_flags.split(" ");
            this.bugs = bugs.split(" ");
            this.bogomips = Float.parseFloat(bogomips);
            this.TLB_size = TLB_size;
            this.clflush_size = Integer.parseInt(clflush_size);
            this.cache_alignment = Integer.parseInt(cache_alignment);
            this.address_sizes = address_sizes.split(", ");
            this.power_management = power_management;
        }

        public int processor() {
            return processor;
        }

        public String vendor_id() {
            return vendor_id;
        }

        public String cpu_family() {
            return cpu_family;
        }

        public String model() {
            return model;
        }

        public String model_name() {
            return model_name;
        }

        public int stepping() {
            return stepping;
        }

        public String microcode() {
            return microcode;
        }

        public float cpu_MHz() {
            return cpu_MHz;
        }

        public String cache_size() {
            return cache_size;
        }

        public int physical_id() {
            return physical_id;
        }

        public int siblings() {
            return siblings;
        }

        public int core_id() {
            return core_id;
        }

        public int cpu_cores() {
            return cpu_cores;
        }

        public int apicid() {
            return apicid;
        }

        public int initial_apicid() {
            return initial_apicid;
        }

        public String fpu() {
            return fpu;
        }

        public String fpu_exception() {
            return fpu_exception;
        }

        public int cpuid_level() {
            return cpuid_level;
        }

        public String wp() {
            return wp;
        }

        public String[] flags() {
            return flags;
        }

        public String[] vmx_flags() {
            return vmx_flags;
        }

        public String[] bugs() {
            return bugs;
        }

        public float bogomips() {
            return bogomips;
        }

        public int clflush_size() {
            return clflush_size;
        }

        public int cache_alignment() {
            return cache_alignment;
        }

        public String[] address_sizes() {
            return address_sizes;
        }

        public String power_management() {
            return power_management;
        }
    }

}
