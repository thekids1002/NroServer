package nro.player;

import nro.main.Util;
import nro.io.Message;
import nro.main.Service;

public class FriendService {
    private static FriendService instance;

    public static FriendService gI() {
        if (instance == null) {
            instance = new FriendService();
        }
        return instance;
    }

    //get list friend of player
    public void listFriend(Player p, byte type) {
        if(p != null && p.session != null) {
            Message m = null;
            try {
                m = new Message(-80);
                m.writer().writeByte(type);
                m.writer().writeByte(p.friends.size()); //COUNT FRIEND
                if(p.friends.size() > 0) {
                    for(Friend f: p.friends) {
                        m.writer().writeInt(f.id);
                        m.writer().writeShort(f.head);
                        //head icon ID
                        m.writer().writeShort(f.headICON);
                        m.writer().writeShort(f.body);
                        m.writer().writeShort(f.leg);
                        m.writer().writeByte(f.bag);
                        m.writer().writeUTF(f.name);
                        m.writer().writeBoolean(f.isOnline);
                        m.writer().writeUTF(f.strPower);
                    }
                }
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

    public void addFriend(Player p, int idFriend, byte type) {
        if(p.friends.size() >= 10) {
            p.sendAddchatYellow("Danh sách bạn bè chỉ được tối đa 10 người");
            return;
        }
        Player newFriend = PlayerManger.gI().getPlayerByUserID(idFriend);
        if(p.isHasFriend(idFriend)) {
            p.sendAddchatYellow("Không thể kết bạn với người đã trong danh sách bạn bè");
            return;
        }
        if(newFriend != null) {
            String bodyLeg = Service.gI().writePartBodyLeg(newFriend);
            String[] arrOfStr = bodyLeg.split(",", 2);
            byte bag = (byte)(-1);
            if(p.clan != null) {
                bag = p.clan.imgID;
            }
            Friend nFriend = new Friend.Builder(newFriend.id)
                .withHead(newFriend.PartHead())
                .withHeadICON(newFriend.iconIDHead())
                .withBody(Short.parseShort(arrOfStr[0]))
                .withLeg(Short.parseShort(arrOfStr[1]))
                .withBag(bag)
                .withName(newFriend.name)
                .isOnline(true)
                .withPower(Util.powerToString(newFriend.power))
                .build();
            p.friends.add(nFriend);
            p.sendAddchatYellow("Đã thêm bạn với " + newFriend.name + " thành công");
        } else {
            p.sendAddchatYellow("Người chơi hiện tại không online");
        }
    }

    public void deleteFriend(Player p, int idFriend, byte type) {
        if(p != null && p.session != null) {
            for(byte i = 0; i < p.friends.size(); i++) {
                if(p.friends.get(i).id == idFriend) {
                    p.friends.remove(i);

                    Message m = null;
                    try {
                        m = new Message(-80);
                        m.writer().writeByte(type);
                        m.writer().writeInt(idFriend); //ID
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
                    break;
                }
            }
        }
    }
}
