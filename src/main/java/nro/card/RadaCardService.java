package nro.card;

import nro.io.Message;
import nro.main.Service;

import nro.item.ItemOption;
import nro.player.Player;

public class RadaCardService {
    private static RadaCardService instance;

    public static RadaCardService gI() {
        if (instance == null) {
            instance = new RadaCardService();
        }
        return instance;
    }

    public void readRadaCard(Player p) {
        Message m = null;
        try {
            m = new Message(127);
            m.writer().writeByte((byte)0);
            m.writer().writeShort(p.cards.size());
            for(RadaCard card: p.cards) {
                m.writer().writeShort(card.id);
                m.writer().writeShort(card.template.idIcon);
                m.writer().writeByte(card.template.rank);
                m.writer().writeByte(card.amount);
                m.writer().writeByte(card.template.max_amount);
                m.writer().writeByte(card.template.is_cardmob);
                if(card.template.is_cardmob == (byte)0) {
                    m.writer().writeShort(card.template.temp_mob);
                } else {
                    m.writer().writeShort(card.template.head);
                    m.writer().writeShort(card.template.body);
                    m.writer().writeShort(card.template.leg);
                    m.writer().writeShort(card.template.bag);
                }
                m.writer().writeUTF(card.template.name);
                m.writer().writeUTF(card.template.info);
                m.writer().writeByte(card.level);
                m.writer().writeByte(card.set_use);
                m.writer().writeByte(card.template.itemOptions.size());
                for(ItemOption option: card.template.itemOptions) {
                    m.writer().writeByte((byte)(option.id));
                    m.writer().writeShort(option.param);
                    m.writer().writeByte(option.active_card);
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

    public void setUseCard(Player p, short id) {
        RadaCard card = p.cardByIdCard(id);
        if(card.level >= (byte)1) {
            if(card.set_use == (byte)0) {
                if(p.cCardUse < (byte)5) {
                    card.set_use = (byte)1;
                    p.cCardUse = (byte)(p.cCardUse + 1) > (byte)5 ? (byte)5 : (byte)(p.cCardUse + 1);
                    sendSetUseCard(p, card.id, card.set_use);
                    updateBuffRada(p, card);
                    Service.gI().loadPoint(p.session, p);
                    if(card.id == (short)956) {
                        sendAuraCard(p, (short)0);
                        p.idAura = (short)0;
                    }
                } else {
                    p.sendAddchatYellow("Trang bị tối đa 5 thẻ");
                }
            } else if(card.set_use == (byte)1) {
                card.set_use = (byte)0;
                p.cCardUse = (byte)(p.cCardUse - 1) < (byte)0 ? (byte)0 : (byte)(p.cCardUse - 1);
                sendSetUseCard(p, card.id, card.set_use);
                updateBuffRada(p, card);
                Service.gI().loadPoint(p.session, p);
                if(card.id == (short)956) {
                    sendAuraCard(p, (short)1);
                    p.idAura = (short)(-1);
                }
            }
        }
    }

    public void setCardLogout(Player p, short id) {
        RadaCard card = p.cardByIdCard(id);
        if(card.level >= (byte)1) {
            card.set_use = (byte)0;
            p.cCardUse = (byte)(p.cCardUse - 1) < (byte)0 ? (byte)0 : (byte)(p.cCardUse - 1);
            sendSetUseCard(p, card.id, card.set_use);
            updateBuffRada(p, card);
            Service.gI().loadPoint(p.session, p);
            if(card.id == (short)956) {
                sendAuraCard(p, (short)1);
                p.idAura = (short)(-1);
            }
        }
    }
    public void updateBuffRada(Player p, RadaCard card) {
        if(card.set_use == (byte)0) {
            for(byte i = 0; i < card.template.itemOptions.size(); i++) {
                if(i <= card.level) {
                    if(card.template.itemOptions.get(i).id == 6) {
                        p.hpRada = (p.hpRada - card.template.itemOptions.get(i).param) < 0 ? 0 : (p.hpRada - card.template.itemOptions.get(i).param);
                    } else if(card.template.itemOptions.get(i).id == 7) {
                        p.kiRada = (p.kiRada - card.template.itemOptions.get(i).param) < 0 ? 0 : (p.kiRada - card.template.itemOptions.get(i).param);
                    } else if(card.template.itemOptions.get(i).id == 0) {
                        p.damRada = (p.damRada - card.template.itemOptions.get(i).param) < 0 ? 0 : (p.damRada - card.template.itemOptions.get(i).param);
                    } else if(card.template.itemOptions.get(i).id == 47) {
                        p.defRada = (p.defRada - card.template.itemOptions.get(i).param) < 0 ? 0 : (p.defRada - card.template.itemOptions.get(i).param);
                    } else if(card.template.itemOptions.get(i).id == 50) {
                        p.sdRada = (p.sdRada - card.template.itemOptions.get(i).param) < 0 ? 0 : (p.sdRada - card.template.itemOptions.get(i).param);
                    }
                }
            }
        } else if(card.set_use == (byte)1) {
            for(byte i = 0; i < card.template.itemOptions.size(); i++) {
                if(i <= card.level) {
                    if(card.template.itemOptions.get(i).id == 6) {
                        p.hpRada += card.template.itemOptions.get(i).param;
                    } else if(card.template.itemOptions.get(i).id == 7) {
                        p.kiRada += card.template.itemOptions.get(i).param;
                    } else if(card.template.itemOptions.get(i).id == 0) {
                        p.damRada += card.template.itemOptions.get(i).param;
                    } else if(card.template.itemOptions.get(i).id == 47) {
                        p.defRada += card.template.itemOptions.get(i).param;
                    } else if(card.template.itemOptions.get(i).id == 50) {
                        p.sdRada += card.template.itemOptions.get(i).param;
                    }
                }
            }
        }
    }
    public void sendSetUseCard(Player p, short id, byte use) {
        Message m = null;
        try {
            m = new Message(127);
            m.writer().writeByte((byte)1);
            m.writer().writeShort(id);
            m.writer().writeByte(use);
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
    public void sendAuraCard(Player p, short idAura) {
        Message m = null;
        try {
            m = new Message(127);
            m.writer().writeByte((byte)4);
            m.writer().writeInt(p.id);
            m.writer().writeShort(idAura);
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
    
    public void setAmountCard(Player p, short id) {
        RadaCard card = p.cardByIdCard(id);
        if(card != null) {
            card.amount++;
            if(card.amount > card.template.max_amount && card.level < (byte)2) {
                card.amount = (byte)0;
                card.level = (byte)(card.level + 1) > (byte)2 ? (byte)2 : (byte)(card.level + 1);
                sendLevelCard(p, card);
                updateBuffRada(p, card);
                Service.gI().loadPoint(p.session, p);
            }
            sendAmountCard(p, card);
        }
    }
    public void sendAmountCard(Player p, RadaCard card) {
        Message m = null;
        try {
            m = new Message(127);
            m.writer().writeByte((byte)3);
            m.writer().writeShort(card.id);
            m.writer().writeByte(card.amount);
            m.writer().writeByte(card.template.max_amount);
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
    public void sendLevelCard(Player p, RadaCard card) {
        Message m = null;
        try {
            m = new Message(127);
            m.writer().writeByte((byte)2);
            m.writer().writeShort(card.id);
            m.writer().writeByte(card.level);
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
