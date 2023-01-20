package nro.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import nro.main.Controller;
import nro.player.Player;
import nro.player.PlayerManger;

public class Session {

    public class Sender {
        public ArrayList<Message> sendingMessage;

        public Sender() {
            sendingMessage = new ArrayList<>();
        }

        public void AddMessage(Message message) {
            sendingMessage.add(message);
        }

        public void clearMessage() {
            sendingMessage.clear();
        }

        public void run() {
            while (connected) {
                try {
                    if (sendKeyComplete) {
                        while (sendingMessage.size() > 0) {
                            Message m = sendingMessage.get(0);
                            if (m != null) {
                                doSendMessage(m);
                                sendingMessage.remove(0);
                            }
                        }
                    }
                    try {
                        Thread.sleep(5);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class MessageCollector {
        public Session session;
        public MessageCollector(Session session) {
            this.session = session;
        }
        public void run() {
            try {
                while (connected) {
                    Message message = readMessage();
                    if (message == null) {
                        break;
                    }
                    try {
                        controller.onMessage(session, message);
                        message.cleanup();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(5);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!connected) {
//                System.out.println("disconnect...");
            } else {
//                System.out.println("Session " + socket.getPort() + " disconnect...");
                //disconnect();
//                PlayerManger.gI().kick(this);
                PlayerManger.gI().kick(session);
            }
        }

        private Message readMessage() {
            try {
                byte cmd = dis.readByte();
                if (sendKeyComplete) {
                    cmd = readKey(cmd);
                }
                int size;
                if (sendKeyComplete) {
                    byte b1 = dis.readByte();
                    byte b2 = dis.readByte();
                    size = (readKey(b1) & 255) << 8 | readKey(b2) & 255;
                } else {
                    size = dis.readUnsignedShort();
                }
                byte[] data = new byte[size];
                int len = 0;
                int byteRead = 0;
                while (len != -1 && byteRead < size) {
                    len = dis.read(data, byteRead, size - byteRead);
                    if (len > 0) {
                        byteRead += len;
                    }
                }
                if (sendKeyComplete) {
                    for (int i = 0; i < data.length; i++) {
                        data[i] = readKey(data[i]);
                    }
                }
                return new Message(cmd, data);
            } catch (IOException e) {
//                throw new RuntimeException(e);
            }
            return null;
        }
    }

    private boolean sendKeyComplete, connected;
    private final byte[] keys = "pirate".getBytes();
    private final static String KEY = "pirate";
    private byte curR, curW;
    private Socket socket;
    private final Sender sender = new Sender();
    public static Thread collectorThread;
    public static Thread sendThread;
    public byte zoomLevel;
    private final Controller controller;
    private DataInputStream dis;
    private DataOutputStream dos;

    public Player player = null;
    public int userId;
    public String nhanvat = null;
    public String taikhoan;
    public String matkhau;

    public Session(Socket socket) {
        try {
            this.socket = socket;
            this.controller = new Controller();
            this.dis = new DataInputStream(socket.getInputStream());
            this.dos = new DataOutputStream(socket.getOutputStream());
            this.connected = true;
            //create sendThread
            sendThread = new Thread(sender::run);

            //start run collectorThread
            MessageCollector msgCollector = new MessageCollector(this);
            collectorThread = new Thread(msgCollector::run);
            collectorThread.start();
//            System.out.println("Session " + socket.getPort() + " connect...");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private byte readKey(byte b) {
        byte[] bytes = KEY.getBytes();
        byte curR = this.curR;
        this.curR = (byte)(curR + 1);
        byte i = (byte)(bytes[curR] & 255 ^ b & 255);
        if (this.curR >= KEY.getBytes().length) {
            this.curR %= (byte)KEY.getBytes().length;
        }
        return i;
    }

    private byte writeKey(byte b) {
        byte[] bytes = KEY.getBytes();
        byte curW = this.curW;
        this.curW = (byte)(curW + 1);
        byte i = (byte)(bytes[curW] & 255 ^ b & 255);
        if (this.curW >= KEY.getBytes().length) {
            this.curW %= (byte)KEY.getBytes().length;
        }
        return i;
    }

    public void sendSessionKey() {
        Message msg = new Message(-27);
        try {
            msg.writer().writeByte(keys.length);
            msg.writer().writeByte(keys[0]);
            for (int i = 1; i < keys.length; i++) {
                msg.writer().writeByte(keys[i] ^ keys[i - 1]);
            }
            doSendMessage(msg);
            msg.cleanup();
            sendKeyComplete = true;
            if(!sendThread.isAlive()) {
                sendThread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(Message message)
    {
        sender.AddMessage(message);
    }
    public void doSendMessage(Message msg) {
        byte[] data = msg.getData();
        try {
            if (sendKeyComplete) {
                byte b = writeKey(msg.command);
                dos.writeByte(b);
            } else {
                dos.writeByte(msg.command);
            }
            if (data != null) {
                int size = data.length;
                if (msg.command == -32 || msg.command == -66 || msg.command == -74 || msg.command == 11 || msg.command == -67 || msg.command == -87 || msg.command == 66) {
                    byte b = writeKey((byte) (size));
                    dos.writeByte(b - 128);
                    byte b2 = writeKey((byte) (size >> 8));
                    dos.writeByte(b2 - 128);
                    byte b3 = writeKey((byte) (size >> 16));
                    dos.writeByte(b3 - 128);
                } else if (sendKeyComplete) {
                    int byte1 = writeKey((byte) (size >> 8));
                    dos.writeByte(byte1);
                    int byte2 = writeKey((byte) (size & 255));
                    dos.writeByte(byte2);
                } else {
                    dos.writeShort(size);
                }
                if (sendKeyComplete) {
                    for (int i = 0; i < data.length; i++) {
                        data[i] = writeKey(data[i]);
                    }
                }
                dos.write(data);
            } else {
                dos.writeShort(0);
            }
            dos.flush();
            msg.cleanup();
        } catch (Exception e) {
            //EXCEPTION SOCKET : ERROR WRITER
//            e.printStackTrace();
        } finally {
            msg.cleanup();
        }
    }

    public void disconnect() {
        controller.logout(this);
        curR = 0;
        curW = 0;
        try {
            connected = false;
            if (socket != null) {
                socket.close();
                socket = null;
            }
            if (dos != null) {
                dos.close();
                dos = null;
            }
            if (dis != null) {
                dis.close();
                dis = null;
            }
            sendThread = null;
            collectorThread = null;
            System.gc();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearMessage() {
        sender.clearMessage();
    }

    public void setClientType(Message msg) {
        try {
            msg.reader().readByte();//client_type
            zoomLevel = msg.reader().readByte();//zoom_level
            msg.reader().readBoolean();//is_gprs
            msg.reader().readInt();//width
            msg.reader().readInt();//height
            msg.reader().readBoolean();//is_qwerty
            msg.reader().readBoolean();//is_touch
            msg.reader().readUTF();//
            msg.cleanup();
        } catch (Exception e) {
        }

    }
}