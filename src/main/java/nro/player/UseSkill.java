package nro.player;

import java.io.IOException;
import nro.io.Message;

public class UseSkill {
    public static void useSkill(Player p, Message m) throws IOException {
        byte tempID1 = m.reader().readByte();
        byte tempID2 = m.reader().readByte();
        byte tempID3 = m.reader().readByte();
        byte tempID4 = m.reader().readByte();
        byte tempID5 = m.reader().readByte();
        byte tempID6 = m.reader().readByte();
        byte tempID7 = m.reader().readByte();
        byte tempID8 = m.reader().readByte();
        byte tempID9 = m.reader().readByte();
        byte tempID10 = m.reader().readByte();
        m.cleanup();

        try {
            m = new Message(-113);
            m.writer().writeByte(tempID1);
            m.writer().writeByte(tempID2);
            m.writer().writeByte(tempID3);
            m.writer().writeByte(tempID4);
            m.writer().writeByte(tempID5);
            m.writer().writeByte(tempID6);
            m.writer().writeByte(tempID7);
            m.writer().writeByte(tempID8);
            m.writer().writeByte(tempID9);
            m.writer().writeByte(tempID10);
            m.writer().flush();
            p.session.sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }
}
