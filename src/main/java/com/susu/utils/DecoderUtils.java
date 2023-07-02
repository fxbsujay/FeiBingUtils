package com.susu.utils;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * <p>Description: String decoder</p>
 * <p>字符串解码器</p>
 * @author sujay
 * @version 22:11 2022/1/24
 * @since JDK1.8 <br/>
 */
public class DecoderUtils  {

    public static final Charset CHARSET_ISO_8859_1 = StandardCharsets.ISO_8859_1;
    public static final Charset CHARSET_UTF_8 = StandardCharsets.UTF_8;
    public static final Charset CHARSET_GBK = Charset.forName("GBK");

    public static String decodeForPath(String str) {
        return decode(str, CHARSET_UTF_8, false);
    }

    public static String decode(String str) {
        return decode(str, CHARSET_UTF_8, true);
    }

    /**
     * <p>Description: string decode</p>
     *
     * @param str               需要解码内容
     * @param charset           编码格式，null表示不解码
     * @param isPlusToSpace     是否+转换为空格
     * @return                  编码后的字符
     */
    public static String decode(String str, Charset charset, boolean isPlusToSpace) {
        if (null != str && null != charset) {
            int length = str.length();
            if (0 == length) {
                return "";
            } else {
                StringBuilder result = new StringBuilder(length / 3);
                int begin = 0;
                for(int i = 0; i < length; ++i) {
                    char c = str.charAt(i);
                    if ('%' != c && !((c >= '0' && c <= '9') || c >= 'a' && c <= 'f' || c >= 'A' && c <= 'F')) {
                        if (i > begin) {
                            result.append(decodeSub(str, begin, i, charset, isPlusToSpace));
                        }

                        if ('+' == c && isPlusToSpace) {
                            c = ' ';
                        }

                        result.append(c);
                        begin = i + 1;
                    }
                }

                if (begin < length) {
                    result.append(decodeSub(str, begin, length, charset, isPlusToSpace));
                }

                return result.toString();
            }
        } else {
            return str;
        }
    }

    public static byte[] decode(byte[] bytes, boolean isPlusToSpace) {
        if (bytes == null) {
            return null;
        } else {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream(bytes.length);

            for(int i = 0; i < bytes.length; ++i) {
                int b = bytes[i];
                if (b == 43) {
                    buffer.write(isPlusToSpace ? 32 : b);
                } else if (b == 37) {
                    if (i + 1 < bytes.length) {

                        int u =  Character.digit(bytes[i + 1], 16);
                        if (u >= 0 && i + 2 < bytes.length) {
                            int l =  Character.digit(bytes[i + 2], 16);
                            if (l >= 0) {
                                buffer.write((char)((u << 4) + l));
                                i += 2;
                                continue;
                            }
                        }
                    }

                    buffer.write(b);
                } else {
                    buffer.write(b);
                }
            }

            return buffer.toByteArray();
        }
    }

    private static String decodeSub(String str, int begin, int end, Charset charset, boolean isPlusToSpace) {
        return new String(decode(str.substring(begin, end).getBytes(StandardCharsets.ISO_8859_1), isPlusToSpace), charset);
    }

}
