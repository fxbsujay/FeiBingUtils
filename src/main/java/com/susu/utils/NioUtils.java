package com.susu.utils;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.function.Consumer;

/**
 * <p>Description: NIO Network</p>
 * <p>NIO 网络模型</p>
 * @author sujay
 * @version 15:15 2023/12/07
 * @since JDK1.8 <br/>
 */
public class NioUtils {

    public static void main(String[] args) {
        Consumer<SocketChannel> handler = sc -> {
            ByteBuffer readBuffer = ByteBuffer.allocate(1024);
            try {
                int readBytes = sc.read(readBuffer);
                if (readBytes > 0) {
                    readBuffer.flip();
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    System.out.println(new String(bytes, StandardCharsets.UTF_8));
                } else if (readBytes < 0) {
                    sc.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        };

        NioServer server = createServer(8849, handler);
        NioUtils.NioClient client = NioUtils.createClient("127.0.0.1", 8849, handler);

        try {
            client.listen();
        } catch (IOException e) {
            client.close();
        }
        client.write(ByteBuffer.wrap(("你好。\n".getBytes())));
        client.write(ByteBuffer.wrap(("你好2。".getBytes())));

        try {
            server.listen();
        } catch (IOException e) {
            server.close();
        }
    }

    /**
     * 创建一个NIO服务端
     *
     * @param port      启动端口
     * @param handler   消息处理器
     */
    public static NioServer createServer(int port, Consumer<SocketChannel> handler) {
        return new NioServer(port, handler);
    }

    /**
     * 创建一个NIO客户端
     *
     * @param host      服务端IP地址
     * @param port      服务端端口
     * @param handler   消息处理器
     */
    public static NioClient createClient(String host, int port, Consumer<SocketChannel> handler) {
        return new NioClient(new InetSocketAddress(host, port), handler);
    }

    /**
     * 一个NIO抽象层
     * @param <T>
     */
    static abstract class NioSocketAbstract<T extends SelectableChannel> implements Closeable {

        /**
         * 多路复用器
         */
        protected Selector selector;

        /**
         * 连接对象
         */
        protected final T socket;

        /**
         * 消息处理器
         */
        protected Consumer<SocketChannel> handler;

        public NioSocketAbstract(Consumer<SocketChannel> handler) {
            try {
                this.socket = initSocket();
                this.socket.configureBlocking(false);
                this.selector = Selector.open();
                setHandler(handler);
            } catch (IOException e) {
                close();
                throw new RuntimeException(e);
            }
        }

        /**
         * 初始化连接
         */
        protected abstract T initSocket();

        /**
         * 处理消息
         */
        protected abstract void handle(SelectionKey key);

        /**
         * 监听消息
         */
        public void listen() throws IOException {
            label18:
            while(true) {
                if (this.selector.isOpen() && 0 != this.selector.select()) {
                    Iterator<SelectionKey> keyIter = this.selector.selectedKeys().iterator();
                    while(true) {
                        if (!keyIter.hasNext()) {
                            continue label18;
                        }
                        handle(keyIter.next());
                        keyIter.remove();
                    }
                }
                return;
            }
        }

        public void setHandler(Consumer<SocketChannel> handler) {
            this.handler = handler;
        }

        public Selector getSelector() {
            return selector;
        }

        @Override
        public void close() {
            if (selector != null) {
                try {
                    selector.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    static class NioServer extends NioSocketAbstract<ServerSocketChannel> {

        public NioServer(int port, Consumer<SocketChannel> handler) {
            super(handler);
            try {
                this.socket.register(this.selector, SelectionKey.OP_ACCEPT);
                this.socket.bind(new InetSocketAddress(port));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected ServerSocketChannel initSocket() {
            try {
                return ServerSocketChannel.open();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void handle(SelectionKey key) {
            if (key.isAcceptable()) {
                try {
                    SocketChannel channel = ((ServerSocketChannel) key.channel()).accept();
                    channel.configureBlocking(false);
                    channel.register(selector, SelectionKey.OP_READ);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            if (key.isReadable()) {
                SocketChannel socketChannel = (SocketChannel)key.channel();
                try {
                    handler.accept(socketChannel);
                } catch (Exception var4) {
                    key.cancel();
                    try {
                        socketChannel.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    static class NioClient extends NioSocketAbstract<SocketChannel> {

        public NioClient(InetSocketAddress address, Consumer<SocketChannel> handler) {
            super(handler);
            try {
                this.socket.register(this.selector, SelectionKey.OP_READ);
                this.socket.connect(address);
                while(true) {
                    if (this.socket.finishConnect()) break;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected SocketChannel initSocket() {
            try {
                return SocketChannel.open();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void listen() throws IOException {
            Thread thread = new Thread(() -> {
                try {
                    super.listen();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            thread.setDaemon(true);
            thread.start();
        }

        @Override
        protected void handle(SelectionKey key) {
            if (key.isReadable()) {
                SocketChannel socketChannel = (SocketChannel)key.channel();
                try {
                    this.handler.accept(socketChannel);
                } catch (Exception var4) {
                    key.cancel();
                    try {
                        socketChannel.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        public void write(ByteBuffer... data) {
            try {
                this.socket.write(data);
            } catch (IOException var3) {
                throw new RuntimeException(var3);
            }
        }
    }
}
