package com.susu.utils;


import javax.servlet.http.HttpServletRequest;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * <p>Description: Image generation and recognition</p>
 * <p>解析Ip地址</p>
 * @author sujay
 * @version 14:39 2022/2/18
 * @since JDK1.8 <br/>
 */
public class IpUtils {

    /**
     * Get request ip
     */
    public static String getIp(HttpServletRequest request) {
        String unknown = "unknown";
        String ip = null;
        try {
            ip = request.getHeader("x-forwarded-for");
            if (StringUtils.isEmpty(ip) || unknown.equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ip) || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ip) || unknown.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (StringUtils.isEmpty(ip) || unknown.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (StringUtils.isEmpty(ip) || unknown.equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } catch (Exception e) {
           throw new RuntimeException("Ip parsing failed");
        }

        return ip;
    }

    /**
     * Get localhost
     */
    public static String getIp() {

        try {
            if (!isLinux()) {
                return InetAddress.getLocalHost().getHostAddress();
            }

            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip;
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = allNetInterfaces.nextElement();
                if (!netInterface.isLoopback() && !netInterface.isVirtual() && netInterface.isUp()) {
                    Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        ip = addresses.nextElement();
                        if (ip instanceof Inet4Address) {
                            return ip.getHostAddress();
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Get ip address error, exception message:" + e.getMessage());
        }
        return "localhost";
    }

    public static String getOsName() {
        return System.getProperty("os.name");
    }

    public static boolean isWindows() {
        String osName = getOsName();
        return osName != null && osName.startsWith("Windows");
    }

    public static boolean isMacOs() {
        String osName = getOsName();
        return osName != null && osName.startsWith("Mac");
    }

    public static boolean isLinux() {
        String osName = getOsName();
        return (osName != null && osName.startsWith("Linux")) || (!isWindows() && !isMacOs());
    }
}
