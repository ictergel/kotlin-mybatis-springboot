package com.demo.base.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Author: lfh
 * Version: 1.0
 * Date: 2016/12/2
 * Description: IPIP库查询工具类（区县版）
 * Function List:
 */
public class IPDistrictExt {
    public static boolean enableFileWatch = false;

    private static int offset;
    private static int[] index = new int[65536];
    private static ByteBuffer dataBuffer;
    private static ByteBuffer indexBuffer;
    private static Long lastModifyTime = 0L;
    private static File ipFile;
    private static ReentrantLock lock = new ReentrantLock();

    public static void load(String filename) {
        ipFile = new File(filename);
        load();
        if (enableFileWatch) {
            watch();
        }
    }

    public static void load(String filename, boolean strict) throws Exception {
        ipFile = new File(filename);
        if (strict) {
            int contentLength = Long.valueOf(ipFile.length()).intValue();
            if (contentLength < 512 * 1024) {
                throw new Exception("ip data file error.");
            }
        }
        load();
        if (enableFileWatch) {
            watch();
        }
    }

    /**
     * 查询IP信息（经纬度定位区县）
     *
     * @param ip
     * @return arrs[0]：国家
     * arrs[1]：省份
     * arrs[2]：地市
     * arrs[3]：区县
     * arrs[4]：中国行政区划代码
     * arrs[5]：
     * arrs[6]：经度
     * arrs[7]：维度
     * @throws Exception
     */
    public static String[] find(String ip) throws Exception {
        String[] ips = ip.split("\\.");
        int prefix_value = (Integer.valueOf(ips[0]) * 256 + Integer.valueOf(ips[1]));
        long ip2long_value = ip2long(ip);
        int start = index[prefix_value];
        int max_comp_len = offset - 262144 - 4;
        long tmpInt;
        long index_offset = -1;
        int index_length = -1;
        byte b = 0;
        try {
            for (start = start * 13 + 262144; start < max_comp_len; start += 13) {
                tmpInt = int2long(indexBuffer.getInt(start));
                if (tmpInt >= ip2long_value) {
                    index_offset = bytesToLong(b, indexBuffer.get(start + 10), indexBuffer.get(start + 9), indexBuffer.get(start + 8));
                    index_length = (0xFF & indexBuffer.get(start + 11) << 8) + (0xFF & indexBuffer.get(start + 12));
                    break;
                }
            }
        } catch (Exception e) {
            throw new Exception("IP Data Not Found.");
        }

        if (index_offset == -1 && index_length == -1) {
            throw new Exception("IP Data Not Found.");
        }

        byte[] areaBytes;

        lock.lock();
        try {
            dataBuffer.position(offset + (int) index_offset - 262144);
            areaBytes = new byte[index_length];
            dataBuffer.get(areaBytes, 0, index_length);
        } catch (Exception e) {
            throw new Exception("The location of the Ip data buffer was not found.");
        } finally {
            lock.unlock();
        }

        return new String(areaBytes, Charset.forName("UTF-8")).split("\\t", -1);
    }

    private static void watch() {
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                long time = ipFile.lastModified();
                if (time > lastModifyTime) {
                    load();
                }
            }
        }, 1000L, 5000L, TimeUnit.MILLISECONDS);
    }

    private static void load() {
        lastModifyTime = ipFile.lastModified();
        lock.lock();
        try {
            dataBuffer = ByteBuffer.wrap(getBytesByFile(ipFile));
            dataBuffer.position(0);
            offset = dataBuffer.getInt(); // indexLength
            byte[] indexBytes = new byte[offset];
            dataBuffer.get(indexBytes, 0, offset - 4);
            indexBuffer = ByteBuffer.wrap(indexBytes);
            indexBuffer.order(ByteOrder.LITTLE_ENDIAN);

            for (int i = 0; i < 256; i++) {
                for (int j = 0; j < 256; j++) {
                    index[i * 256 + j] = indexBuffer.getInt();
                }
            }
            indexBuffer.order(ByteOrder.BIG_ENDIAN);
        } finally {
            lock.unlock();
        }
    }

    private static byte[] getBytesByFile(File file) {
        FileInputStream fin = null;
        byte[] bs = new byte[new Long(file.length()).intValue()];
        try {
            fin = new FileInputStream(file);
            int readBytesLength = 0;
            int i;
            while ((i = fin.available()) > 0) {
                fin.read(bs, readBytesLength, i);
                readBytesLength += i;
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (fin != null) {
                    fin.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return bs;
    }

    private static long bytesToLong(byte a, byte b, byte c, byte d) {
        return int2long((((a & 0xff) << 24) | ((b & 0xff) << 16) | ((c & 0xff) << 8) | (d & 0xff)));
    }

    private static int str2Ip(String ip) {
        String[] ss = ip.split("\\.");
        int a, b, c, d;
        a = Integer.parseInt(ss[0]);
        b = Integer.parseInt(ss[1]);
        c = Integer.parseInt(ss[2]);
        d = Integer.parseInt(ss[3]);
        return (a << 24) | (b << 16) | (c << 8) | d;
    }

    private static long ip2long(String ip) {
        return int2long(str2Ip(ip));
    }

    private static long int2long(int i) {
        long l = i & 0x7fffffffL;
        if (i < 0) {
            l |= 0x080000000L;
        }
        return l;
    }

    public void afterPropertiesSet() throws Exception {
        if (ipFile == null) {
            load(URLDecoder.decode(IPDistrictExt.class.getResource("/config/quxian.datx").getFile(),"utf-8"));
        }
    }

    public static void main(String[] args) {
        try {
            load(URLDecoder.decode(IPDistrictExt.class.getResource("/config/quxian.datx").getFile(),"utf-8"));
            System.out.println(Arrays.toString(IPDistrictExt.find("8.8.8.8")));
            System.out.println(Arrays.toString(IPDistrictExt.find("118.28.8.8")));
            Long st = System.nanoTime();
            for (int i = 0; i < 10000; i++) {
                find("222.180.195.194");
            }
            Long et = System.nanoTime();
            System.out.println("查询耗时：" + (et - st) / 1000 / 1000);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
