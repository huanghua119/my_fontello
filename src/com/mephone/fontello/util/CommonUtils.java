package com.mephone.fontello.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;

import com.mephone.fontello.config.MyLog;

public class CommonUtils {

    public static final void saveObject(String path, Object saveObject) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        File f = new File(path);
        try {
            fos = new FileOutputStream(f);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(saveObject);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static final Object restoreObject(String path) {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        Object object = null;
        File f = new File(path);
        if (!f.exists()) {
            return null;
        }
        try {
            fis = new FileInputStream(f);
            ois = new ObjectInputStream(fis);
            object = ois.readObject();
            return object;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return object;
    }

    public static String getLocalMac() {
        String macAddress = "";
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            if (NetworkInterface.getByInetAddress(inetAddress) == null) {
                return macAddress;
            }
            byte[] mac = NetworkInterface.getByInetAddress(inetAddress)
                    .getHardwareAddress();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                if (i != 0) {
                    sb.append("-");
                }
                String s = Integer.toHexString(mac[i] & 0xFF);
                sb.append(s.length() == 1 ? 0 + s : s);
            }
            macAddress = sb.toString().trim().toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return macAddress;
    }

    /**
     * 计算最小值
     * @param index
     * @return
     */
    public static int calcMin(int... index) {
        int temp = 10000;
        for (int i : index) {
            if (i == -1) {
                continue;
            }
            temp = Math.min(temp, i);
        }
        return temp;
    }

    public static void printMemoryInfo() {
        Runtime currRuntime = Runtime.getRuntime();
        int nFreeMemory = (int) (currRuntime.freeMemory() / 1024 / 1024);
        int nTotalMemory = (int) (currRuntime.totalMemory() / 1024 / 1024);
        MyLog.i("nFreeMemory:" + nFreeMemory + " nTotalMemory:" + nTotalMemory);
    }
}
