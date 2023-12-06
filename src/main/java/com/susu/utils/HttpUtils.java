package com.susu.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

/**
 * <p>Description: Http Request Tools</p>
 * <p>Http请求工具类</p>
 * @author sujay
 * @version 11:51 2023/12/06
 * @since JDK1.8 <br/>
 */
public class HttpUtils {

    public static void main(String[] args) throws IOException {
        System.out.println(new String(get("https://fengkeai-ui-pro.1cno.com/api/auth/getWebsiteConfigure/fengkeai-ui-pro"), StandardCharsets.UTF_8));
        File file = download("http://xuebin.xyz/rabbit.jpg", "G:\\file.jpg");

        Map<String, Object> data = new HashMap<>();
        data.put("file", new File[]{file, new File("G:\\file.jpg")});
        data.put("img", file);
        data.put("data", 1);
        System.out.println(new String(post("http://localhost:8849/auth/file", data), StandardCharsets.UTF_8));
    }

    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String PUT = "PUT";
    public static final String DELETE = "DELETE";

    public static final String CONTENT_FORM_URLENCODED = "application/x-www-form-urlencoded";
    public static final String CONTENT_JSON = "application/json";
    public static final String CONTENT_XML = "application/xml";
    public static final String CONTENT_MULTIPART = "multipart/form-data";
    public static final String CONTENT_STREAM = "application/octet-stream";

    public static byte[] get(String url){
       return new HttpRequest(url).method(GET).header().send().response();
    }

    public static byte[] post(String url, String body) {
        return new HttpRequest(url).method(POST).header().body(body).send().response();
    }

    public static byte[] put(String url, String body) {
        return new HttpRequest(url).method(PUT).header().body(body).send().response();
    }

    public static byte[] post(String url, Map<String, Object> formData) {
        return multipart(url, POST ,formData);
    }

    public static byte[] delete(String url) {
        return new HttpRequest(url).method(DELETE).header().send().response();
    }

    public static byte[] multipart(String url, String method, Map<String, Object> formData) {
        return new HttpRequest(url).method(method).header().send(formData).response();
    }

    /**
     * 下载文件
     *
     * @param url       下载连接
     * @param filename  下载到本地的文件路径
     */
    public static File download(String url, String filename) {
        byte[] bytes = get(url);
        File file = new File(filename);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }

    /**
     * 请求体
     */
    static class HttpRequest {

        private final HttpURLConnection conn;

        private String method = "GET";

        private String contentType;

        private byte[] body;

        private Map<String, String> header = new HashMap<>();

        public HttpRequest(String url) {
            try {
                conn = (HttpURLConnection) new URL(url).openConnection();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            conn.setUseCaches(false);
            conn.setConnectTimeout(3000);

            header.put("Charset", "UTF-8");
        }

        public HttpRequest method(String method) {

            if (method == null || method.isEmpty()) {
                return this;
            }

            this.method = method;
            return this;
        }

        public HttpRequest contentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public HttpRequest header(Map<String, String> header) {
            this.header = header;
            return this;
        }

        public HttpRequest header(String name, String value) {
            header.put(name, value);
            return this;
        }

        private HttpRequest header(){
            if (!header.isEmpty()) {
                for (String name : header.keySet()) {
                    conn.setRequestProperty(name, header.get(name));
                }
            }

            if (contentType != null) {
                conn.setRequestProperty("Content-Type", contentType);
            }

            try {
                conn.setRequestMethod(method);
            } catch (ProtocolException e) {
                throw new RuntimeException(e);
            }

            return this;
        }

        public HttpRequest body(byte[] body) {
            this.body = body;
            return this;
        }

        public HttpRequest body(String body) {

            if (body == null || body.isEmpty()) {
                return this;
            }

            if (contentType == null) {
                char firstChar = body.charAt(0);
                switch (firstChar) {
                    case '<':
                        contentType = CONTENT_XML;
                        break;
                    case '[':
                    case '{':
                        contentType = CONTENT_JSON;
                        break;
                    default:
                        contentType = CONTENT_FORM_URLENCODED;
                }
            }

            return body(body.getBytes(StandardCharsets.UTF_8));
        }

        public HttpRequest send(Map<String, Object> formData) {
            String boundary = Long.toHexString(System.currentTimeMillis());
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", CONTENT_MULTIPART + "; boundary=" + boundary);
            PrintWriter writer;
            OutputStream output;
            try {
                output = conn.getOutputStream();
                writer = new PrintWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8), true);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }

            if (formData != null && !formData.isEmpty()) {
                for (String name : formData.keySet()) {
                    Object value = formData.get(name);
                    if (value instanceof File) {
                        writerFile(writer, output, boundary, name, (File) value);
                    } else if (value instanceof File[]) {
                        File[] files = (File[]) value;
                        for (File file : files) {
                            writerFile(writer, output, boundary, name, file);
                        }
                    } else {
                        writer.append("--").append(boundary).append("\r\n");
                        writer.append("Content-Disposition: form-data; name=\"").append(name).append("\"").append("\r\n");
                        writer.append("Content-Type: " + CONTENT_JSON).append("\r\n\r\n");
                        writer.append(value.toString());
                        writer.append("\r\n").flush();
                    }
                }
            }
            writer.append("--").append(boundary).append("--").append("\r\n").flush();
            try {
                output.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            writer.close();
            return this;
        }

        public HttpRequest send() {
            try {
                if (body != null) {
                    conn.setDoOutput(true);
                    conn.setRequestProperty("Content-Length", String.valueOf(this.body.length));
                    conn.getOutputStream().write(body);
                }
                conn.connect();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            return this;
        }

        public byte[] response() {
            ByteArrayOutputStream out = null;
            try {
                int responseCode = conn.getResponseCode();

                if (responseCode == 200) {
                    out = new ByteArrayOutputStream();
                    InputStream in = conn.getInputStream();
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = in.read(buffer)) != -1) {
                        out.write(buffer, 0, len);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (conn != null) {
                    conn.disconnect();
                }
            }

            return out != null ? out.toByteArray() : new byte[0];
        }

        private void writerFile(PrintWriter writer, OutputStream output, String boundary, String name, File file) {
            writer.append("--").append(boundary).append("\r\n");
            writer.append("Content-Disposition: form-data; name=\"").append(name).append("\"; filename=\"").append(file.getName()).append("\"").append("\r\n");
            writer.append("Content-Type:").append(ContentTypeEnum.getContentType(file)).append("\r\n");
            writer.append("\r\n").flush();
            DataInputStream inputStream = null;
            try {
                inputStream = new DataInputStream(Files.newInputStream(file.toPath()));
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            writer.append("\r\n").flush();
        }
    }

    enum ContentTypeEnum {
        AAC("acc", "audio/aac"),
        ABW("abw", "application/x-abiword"),
        ARC("arc", "application/x-freearc"),
        AVI("avi", "video/x-msvideo"),
        AZW("azw", "application/vnd.amazon.ebook"),
        BIN("bin", "application/octet-stream"),
        BMP("bmp", "image/bmp"),
        BZ("bz", "application/x-bzip"),
        BZ2("bz2", "application/x-bzip2"),
        CSH("csh", "application/x-csh"),
        CSS("css", "text/css"),
        CSV("csv", "text/csv"),
        DOC("doc", "application/msword"),
        DOCX("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
        EOT("eot", "application/vnd.ms-fontobject"),
        EPUB("epub", "application/epub+zip"),
        GZ("gz", "application/gzip"),
        GIF("gif", "image/gif"),
        HTM("htm", "text/html"),
        HTML("html", "text/html"),
        ICO("ico", "image/vnd.microsoft.icon"),
        ICS("ics", "text/calendar"),
        JAR("jar", "application/java-archive"),
        JPEG("jpeg", "image/jpeg"),
        JPG("jpg", "image/jpeg"),
        JS("js", "text/javascript"),
        JSON("json", "application/json"),
        JSONLD("jsonld", "application/ld+json"),
        MID("mid", "audio/midi"),
        MIDI("midi", "audio/midi"),
        MJS("mjs", "text/javascript"),
        MP3("mp3", "audio/mpeg"),
        MPEG("mpeg", "video/mpeg"),
        MPKG("mpkg", "application/vnd.apple.installer+xml"),
        ODP("odp", "application/vnd.oasis.opendocument.presentation"),
        ODS("ods", "application/vnd.oasis.opendocument.spreadsheet"),
        ODT("odt", "application/vnd.oasis.opendocument.text"),
        OGA("oga", "audio/ogg"),
        OGV("ogv", "video/ogg"),
        OGX("ogx", "application/ogg"),
        OPUS("opus", "audio/opus"),
        OTF("otf", "font/otf"),
        PNG("png", "image/png"),
        PDF("pdf", "application/pdf"),
        PHP("php", "application/x-httpd-php"),
        PPT("ppt", "application/vnd.ms-powerpoint"),
        PPTX("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"),
        RAR("rar", "application/vnd.rar"),
        RTF("rtf", "application/rtf"),
        SH("sh", "application/x-sh"),
        SVG("svg", "image/svg+xml"),
        SWF("swf", "application/x-shockwave-flash"),
        TAR("tar", "application/x-tar"),
        TIF("tif", "image/tiff"),
        TIFF("tiff", "image/tiff"),
        TS("ts", "video/mp2t"),
        TTF("ttf", "font/ttf"),
        TXT("txt", "text/plain"),
        VSD("vsd", "application/vnd.visio"),
        WAV("wav", "audio/wav"),
        WEBA("weba", "audio/webm"),
        WEBM("webm", "video/webm"),
        WEBP("webp", "image/webp"),
        WOFF("woff", "font/woff"),
        WOFF2("woff2", "font/woff2"),
        XHTML("xhtml", "application/xhtml+xml"),
        XLS("xls",  "application/vnd.ms-excel"),
        XLSX("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
        XML("xml", "application/xml"),
        XUL("xul", "application/vnd.mozilla.xul+xml"),
        ZIP("zip", "application/zip"),
        MIME_3GP("3gp", "video/3gpp"),
        MIME_3GP_WITHOUT_VIDEO("3gp", "audio/3gpp2"),
        MIME_3G2("3g2", "video/3gpp2"),
        MIME_3G2_WITHOUT_VIDEO("3g2", "audio/3gpp2"),
        MIME_7Z("7z", "application/x-7z-compressed");

        private final String extension;

        private final String type;

        ContentTypeEnum(String extension, String type) {
            this.extension = extension;
            this.type = type;
        }

        public static String getContentType(File file) {
            String name = file.getName();
            String[] split = name.split("\\.");
            String extension = split[split.length - 1];
            for (ContentTypeEnum content : ContentTypeEnum.values()) {
                if (extension.equals(content.getExtension())) {
                    return content.getType();
                }
            }
            return CONTENT_STREAM;
        }

        public String getExtension() {
            return extension;
        }

        public String getType() {
            return type;
        }
    }
}
