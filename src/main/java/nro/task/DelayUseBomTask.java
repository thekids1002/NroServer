package nro.task;

import java.util.TimerTask;
import java.util.Timer;
import nro.io.Message;
import nro.player.Player;
import nro.player.Boss;
import nro.player.Detu;
import nro.player.PlayerManger;
import nro.map.Mob;
import nro.skill.Skill;
import java.util.ArrayList;
import nro.main.Util;
import nro.item.Item;
import nro.item.ItemSell;
import nro.item.ItemTemplate;
import nro.map.ItemMap;
import nro.main.Service;
import nro.main.Server;

public class DelayUseBomTask extends TimerTask {
    public ArrayList<Mob> mobs = new ArrayList();
    public ArrayList<Player> players = new ArrayList<>();
    public ArrayList<Boss> bosses = new ArrayList<>();
    public ArrayList<Detu> petss = new ArrayList<>();
    public ArrayList<ItemMap> itemsMap = new ArrayList<>();
    public Player player;
    public Skill skill;

    public DelayUseBomTask(ArrayList<Player> listPlayer, ArrayList<Mob> listMob, ArrayList<ItemMap> iItemMap, Player iplayer, Skill iskill, ArrayList<Boss> listBoss, ArrayList<Detu> listDetu) {
        this.players = listPlayer;
        this.mobs = listMob;
        this.player = iplayer;
        this.skill = iskill;
        this.itemsMap = iItemMap;
        this.bosses = listBoss;
        this.petss = listDetu;
    }

    @Override
    public void run() {
        if(this.player.isdie) {
            this.cancel();
        } else {
            Message m = null;
            int dameBoom = (int)(player.hp * Util.getPercentDouble((int)skill.damage));
            
            player.mp -= (int)(player.mp*0.5);

            try {
                m = new Message(-45);
                m.writer().writeByte(7);
                m.writer().writeInt(player.id); // id player use    
                m.writer().writeShort(skill.skillId); // b91 gui cho co
                m.writer().writeShort(0); //    seconds
                m.writer().flush();
                for(Player p: players) {
                    p.session.sendMessage(m);
                }
    //            m = new Message(-45);
    //            m.writer().writeByte(7);
    //            m.writer().writeInt(player.id); // id player use    
    //            m.writer().writeShort(3); // b91 gui cho co
    //            m.writer().writeShort(3); //    seconds
    //            m.writer().flush();
    //            for(Player p: players) {
    //                p.session.sendMessage(m);
    //            }

                Timer timerDie = new Timer();
                TimerTask tt = new TimerTask() {
                    public void run()
                    {
                        player.hp = 0;
                        player.isdie = true;

                        for(Player _pll: players) {
                            if(_pll.id == player.id) {
                                if(player.isMonkey) {
                                    Service.gI().loadCaiTrangTemp(player);
                                    player.isMonkey = false;
                                    //NOI TAI TANG DAME KHI HOA KHI
                                    if(player.upDameAfterKhi && player.noiTai.id != 0 && player.noiTai.idSkill == (byte)13) {
                                        player.upDameAfterKhi = false;
                                    }
                                    //NOI TAI TANG DAME KHI HOA KHI
                                    Service.gI().loadPoint(player.session, player);
                                }
                                player.zone.sendDieToMe(player);
                            } else {
                                _pll.sendDefaultTransformToPlayer(player);
                                player.zone.sendDieToAnotherPlayer(_pll, player);
                            }
                        }
                        //NEU DEO NGOC RONG SAO DEN THI ROT RA DAT
                        Service.gI().dropDragonBall(player);
                        
    //                    if(player.isMonkey) {
    //                        if(player.ItemBody[5] != null || player.NhapThe == 1){
    //                            Service.gI().LoadCaiTrang(player, 1, player.PartHead(), player.PartHead() + 1, player.PartHead() + 2);
    //                        }
    //                        else{
    //                            Service.gI().LoadCaiTrang(player, 1, player.PartHead(), player.PartBody(), player.Leg());
    //                        }
    //                        player.isMonkey = false;
    //                        Service.gI().loadPoint(player.session, player);
    //
    //                        for (Player p : players) {
    //                            if(p.id != player.id) {
    //                                p.sendDefaultTransformToPlayer(player);
    //                            }
    //                        }
    //                    }
    //
    //
    //                    player.zone.sendDieToMe(player);
    //                    for(Player plsend: players) {
    //                        if(plsend.id != player.id) {
    //                            player.zone.sendDieToAnotherPlayer(plsend, player);
    //                        }
    //                    }
                    };
                };
                timerDie.schedule(tt, 500);


                for(Mob mob: mobs) {
                    if (Math.abs(player.x - mob.pointX) < skill.dx && Math.abs(player.y - mob.pointY) < skill.dy && !mob.isDie) {
                            mob.updateHP(-dameBoom);

                            if(mob.isDie) {
                                ArrayList<ItemMap> itemDrop = new ArrayList<>();
                                //CHECK INIT BOSS MAP KHI GAS
                                Service.gI().initLychee(player);
                                if(mob.template.tempId != 0) {
                                    int idItemNotSell[] = {17,188,189,190,18,19,20, 441,442,443,444,445,446,447, 17,188,189,190, 225, 17,188,189,190,18,19,20,76,188,189,190,18,19,20};
                                    int percentDrop = Util.nextInt(0, 10);
                                    if(percentDrop < 3) {
                                        int id = Util.nextInt(0, 33);
                    //                    Item itemMap = ItemSell.getItem(id);
                                        Item itemMap = ItemSell.getItemNotSell(idItemNotSell[id]);
                                        ItemMap item = new ItemMap();
                                        item.playerId = player.id;
                                        item.x = mob.pointX;
                                        item.y = mob.pointY;
//                                        item.itemMapID = idItemNotSell[id];
//                                        item.itemTemplateID = (short) item.itemMapID;
                                        item.itemMapID = itemsMap.size();
                                        item.itemTemplateID = (short)idItemNotSell[id];
                                        itemMap.template = ItemTemplate.ItemTemplateID(idItemNotSell[id]);
                                        item.item = itemMap;
                                        itemDrop.add(item);
                                        itemsMap.addAll(itemDrop);
                                        //ITEM DROP RA MAP
                                        m = new Message(-12);
                                        m.writer().writeByte(mob.tempId);
                                        m.writer().writeInt(mob.hp);
                                        m.writer().writeBoolean(false);
        //                                m.writer().writeByte(itemDrop.size());
                                        m.writer().writeByte(1);
                                        m.writer().writeShort(item.itemMapID);
                                        m.writer().writeShort(item.item.template.id);
                                        m.writer().writeShort(mob.pointX);
                                        m.writer().writeShort(mob.pointY);
                                        m.writer().writeInt(player.id);
                                        m.writer().flush();
                                        for(Player pll: players) {
                                            pll.session.sendMessage(m);
                                        }
                                        m.cleanup();
                                    } else {
                                        //ITEM DROP RA MAP
                                        m = new Message(-12);
                                        m.writer().writeByte(mob.tempId);
                                        m.writer().writeInt(mob.hp);
                                        m.writer().writeBoolean(false);
                                        m.writer().writeByte(0); //so luong item
                                        m.writer().flush();
                                        for(Player pll: players) {
                                            pll.session.sendMessage(m);
                                        }
                                        m.cleanup();
                                    }
                                    //check TRUNG MABU
                                    if(mob.template.tempId == 70 && mob.typeHiru == (byte)2) {
                                        int rdMabu = Util.nextInt(0, 5);
                                        if(rdMabu < 1) {
                                            player.hasTrungMabu = true;
                                            player.sendAddchatYellow("Bạn vừa nhận được đệ tử Mabư, quay về nhà gặp Ông Già để thao tác");
                                        }
                                    }
                                } else {
                                    //ITEM DROP RA MAP
                                    m = new Message(-12);
                                    m.writer().writeByte(mob.tempId);
                                    m.writer().writeInt(mob.hp);
                                    m.writer().writeBoolean(false);
                                    m.writer().writeByte(0); //so luong item
                                    m.writer().flush();
                                    for(Player pll: players) {
                                        pll.session.sendMessage(m);
                                    }
                                    m.cleanup();
                                }
                            }
                            else {
    //                             try {
                                    m = new Message(-9);
                                    m.writer().writeByte(mob.tempId);
                                    m.writer().writeInt(mob.hp);
                                    m.writer().writeInt(dameBoom);
                                    m.writer().writeBoolean(false);//flag
                                    //eff boss
                                    //5 khói
                                    m.writer().writeByte(-1);
                                    m.writer().flush();
                                    for(Player pll: players) {
                                        pll.session.sendMessage(m);
                                    }
                                    m.cleanup();
    //                            } catch (Exception e) {
    //                                e.printStackTrace();
    //                            }
                            }

                    }
                }
                if(bosses.size() > 0) {
                    for(int i = 0; i < bosses.size(); i++) {
                        if (Math.abs(player.x - bosses.get(i).x) < skill.dx && Math.abs(player.y - bosses.get(i).y) < skill.dy && !bosses.get(i).isdie && Service.gI().checkCanAttackBoss(bosses.get(i))) {
                            if(player.isMonkey) {
                                dameBoom = (int)(dameBoom/3);
                            } else {
                                dameBoom = (int)(dameBoom/2);
                            }
                            bosses.get(i).hp -= dameBoom;
                            if(bosses.get(i).hp <= 0) {
                                bosses.get(i).isdie = true;
                                bosses.get(i).isTTNL = false;
                                bosses.get(i).hp = 0;
                            }
                            if (bosses.get(i).isdie && bosses.get(i).typePk == 5) {
                                //SET LAI TYPE PK CUA BOSS KHI BOSS CHET
                                bosses.get(i).typePk = 1;
                                //send dame
                                player.zone.dameChar(bosses.get(i).id, bosses.get(i).hp, dameBoom, false);
                                //REMOVE ALL KHONG CHE KHI BOSS CHET
                                bosses.get(i).removePlayerKhongChe();

                                if(bosses.get(i)._typeBoss != 1 && bosses.get(i)._typeBoss != 2) { //Broly khong roi do
                                    ArrayList<ItemMap> itemDrops = new ArrayList<>();
                                    if(bosses.get(i)._typeBoss == 3 || bosses.get(i)._typeBoss == 5 || (bosses.get(i)._typeBoss >= (byte)7 && bosses.get(i)._typeBoss <= (byte)30 && bosses.get(i)._typeBoss != (byte)29)) {
                                        if(bosses.get(i)._typeBoss == (byte)7) {
                                            Server.gI().mapKUKU = 0;
                                            Server.gI().khuKUKU = 0;
                                        } else if(bosses.get(i)._typeBoss == (byte)8) {
                                            Server.gI().mapMDD = 0;
                                            Server.gI().khuMDD = 0;
                                        } else if(bosses.get(i)._typeBoss == (byte)9) {
                                            Server.gI().mapRAMBO = 0;
                                            Server.gI().khuRAMBO = 0;
                                        } else if(bosses.get(i)._typeBoss == (byte)14) {
                                            Server.gI().mapTDST = 0;
                                            Server.gI().khuTDST = 0;
                                        }
                                        Service.gI().sendThongBaoServer(player.name + " vừa tiêu diệt " + bosses.get(i).name + " mọi người đều ngưỡng mộ");
                                        int perCapHong = Util.nextInt(0,10);
                                        if(perCapHong < 1) {
                                            ItemSell CapHong = ItemSell.getItemSell(722, (byte)1);
                                            ItemMap itemROI = new ItemMap();
                                            itemROI.playerId = player.id;
                                            itemROI.x = bosses.get(i).x;
                                            itemROI.y = bosses.get(i).y;
//                                            itemROI.itemMapID = 722;
//                                            itemROI.itemTemplateID = (short)itemROI.itemMapID;
                                            itemROI.itemMapID = itemsMap.size();
                                            itemROI.itemTemplateID = (short)722;
                //                                itemGOD.item.template = ItemTemplate.ItemTemplateID(_ITEMMAPID);
                                            //BUILD NEW ITEM + CHI SO CHO DO KICH HOAT
                                            Item _ITEMCapHong = new Item(CapHong.item);
                                            itemROI.item = _ITEMCapHong;
                                            itemDrops.add(itemROI);
                                            itemsMap.addAll(itemDrops);

                                            //đồng bộ boss chet, cho boss bien mat. add item map //68
                                            try {
                                                m = new Message(68);
                                                m.writer().writeShort(itemROI.itemMapID);
                                                m.writer().writeShort(itemROI.item.template.id);
                                                m.writer().writeShort(bosses.get(i).x);
                                                m.writer().writeShort(bosses.get(i).y);
                                                m.writer().writeInt(player.id);
                                                m.writer().flush();
                                                for(Player _pl: players) {
                                                    _pl.session.sendMessage(m);
                                                }
                                                m.cleanup();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } else if(bosses.get(i)._typeBoss == 4 || bosses.get(i)._typeBoss == 6) {
                                        Service.gI().sendThongBaoServer(player.name + " vừa tiêu diệt " + bosses.get(i).name + " mọi người đều ngưỡng mộ");
                                        ItemMap item = player.zone.dropItemGOD(player, bosses.get(i).x, bosses.get(i).y);
                                        if(item != null) {
                                            player.zone.addItemToMap(item, player.id, bosses.get(i).x, bosses.get(i).y);
                                        }
                                        //ADD ITEM TO MAP
//                                        if(item != null) {
//                                            try {
//                                                itemDrops.add(item);
//                                                itemsMap.addAll(itemDrops);
//                                                m = new Message(68);
//                                                m.writer().writeShort(item.itemMapID);
//                                                m.writer().writeShort(item.item.template.id);
//                                                m.writer().writeShort(bosses.get(i).x);
//                                                m.writer().writeShort(bosses.get(i).y);
//                                                m.writer().writeInt(player.id);
//                                                m.writer().flush();
//                                                for(Player _pl: players) {
//                                                    _pl.session.sendMessage(m);
//                                                }
//                                                m.cleanup();
//                                            } catch (Exception e) {
//                                                e.printStackTrace();
//                                            }
//                                        }
                                        if(Util.nextInt(0, 10) < 5) {
                                            ItemMap itemM = player.zone.newItemMAP(979, player.id, bosses.get(i).x, bosses.get(i).y);
                                            if(itemM != null) {
                                                player.zone.addItemToMap(itemM, player.id, bosses.get(i).x, bosses.get(i).y);
                                            }
                                        }
                                    } else if(bosses.get(i)._typeBoss >= (byte)31 && bosses.get(i)._typeBoss <= (byte)35) {
                                        int itemDT = 17;
                                        if(Util.nextInt(0, 4) == 0) {
                                            itemDT = 16;
                                        }
                                        ItemMap itemM = player.zone.newItemMAP(itemDT, player.id, bosses.get(i).x, bosses.get(i).y);
                                        if(itemM != null) {
                                            player.zone.addItemToMap(itemM, player.id, bosses.get(i).x, bosses.get(i).y);
                                        }
                                    } else if(bosses.get(i)._typeBoss >= (byte)26 && bosses.get(i)._typeBoss <= (byte)28) {//cell rot 3s
                                        ItemMap itemM = player.zone.newItemMAP(16, player.id, bosses.get(i).x, bosses.get(i).y);
                                        if(itemM != null) {
                                            player.zone.addItemToMap(itemM, player.id, bosses.get(i).x, bosses.get(i).y);
                                        }
                                    } else if(bosses.get(i)._typeBoss == (byte)30) {//sieu bo hung rot 2s
                                        ItemMap itemM = player.zone.newItemMAP(15, player.id, bosses.get(i).x, bosses.get(i).y);
                                        if(itemM != null) {
                                            player.zone.addItemToMap(itemM, player.id, bosses.get(i).x, bosses.get(i).y);
                                        }
                                    } else if(bosses.get(i)._typeBoss == (byte)48) {//zamasu rot da bao ve
                                        if(Util.nextInt(0, 10) <= 7) {
                                            ItemMap itemM = player.zone.newItemMAP(987, player.id, bosses.get(i).x, bosses.get(i).y);
                                            if(itemM != null) {
                                                player.zone.addItemToMap(itemM, player.id, bosses.get(i).x, bosses.get(i).y);
                                            }
                                        }
                                    } else if(bosses.get(i)._typeBoss == (byte)49 || bosses.get(i)._typeBoss == (byte)50) {//billwhis rot ruong
                                        ItemMap itemM = player.zone.newItemMAP(1055, player.id, bosses.get(i).x, bosses.get(i).y);
                                        if(itemM != null) {
                                            player.zone.addItemToMap(itemM, player.id, bosses.get(i).x, bosses.get(i).y);
                                        }
                                    }
//                                    else if(bosses.get(i)._typeBoss >= (byte)44 || bosses.get(i)._typeBoss <= (byte)47) {
//                                        ItemMap itemM = player.zone.cNewItemMap(590, player.id, bosses.get(i).x, bosses.get(i).y);
//                                        player.zone.addItemToMap(itemM, player.id, bosses.get(i).x, bosses.get(i).y);
//                                    }
                                }
                                if(bosses.get(i)._typeBoss == 2 && player.havePet == 0) { //boss die la super broly
                                    player.detu = bosses.get(i).detu;
                                    player.zone.leaveDEEEEE(bosses.get(i).detu);
                                    player.havePet = 1;
                                    player.isNewPet = true;
                                    player.detu.id = (-100000 - player.id);
                                    player.statusPet = 0;
                                    player.petfucus = 1;
                                    player.zone.pets.add(player.detu);
                                    for(Player _plz: players) {
                                        player.zone.loadInfoDeTu(_plz.session, player.detu);
                                    }
                                } else {
                                    player.zone.leaveDEEEEE(bosses.get(i).detu);
                                }
                                //boss chet
                                if(bosses.get(i)._typeBoss < (byte)44 || bosses.get(i)._typeBoss > 47) {
                                    for(Player _pp: players) {
                                        player.zone.sendDieToAnotherPlayer(_pp, bosses.get(i));
                                    }
                                }
                                //CHECK NHIEM VU SAN BOSS
                                if(player.taskId == (short)21 || player.taskId == (short)22 || player.taskId == (short)23 || player.taskId == (short)25 || player.taskId == (short)26 || player.taskId == (short)27 ||
                                    player.taskId == (short)28 || player.taskId == (short)29 || player.taskId == (short)30 || player.taskId == (byte)32) {
                                    int idBoss = TaskManager.gI().mobTASK0[player.taskId][player.crrTask.index];
                                    if((bosses.get(i)._typeBoss == (byte)(idBoss / 100)) || ((bosses.get(i)._typeBoss == (byte)41) && player.taskId == (byte)32 && player.crrTask.index == (byte)7)) {
                                        TaskService.gI().updateCountTask(player);
                                    }
                                } else if(player.taskId == (short)19 && player.crrTask.index == (byte)1 && bosses.get(i)._typeBoss == (byte)31) {
                                    TaskService.gI().updateCountTask(player);
                                    if(player.clan != null) {
                                        for(byte mk = 0; mk < player.clan.members.size(); mk++) {
                                            Player member = PlayerManger.gI().getPlayerByUserID(player.clan.members.get(mk).id);
                                            if(member != null && member.session != null && member.map.id == 59) {
                                                if(member.taskId == (short)19 && member.crrTask.index == (byte)1) {
                                                    TaskService.gI().updateCountTask(member);
                                                }
                                            }
                                        }
                                    }
                                }
                                //END NHIEM VU SAN BOSS
                                //CHECK NHIEM VU NHAN THOI KHONG
                                if(player.taskId == (short)31 && player.crrTask.index == (byte)0 && bosses.get(i)._typeBoss == (byte)6) {
                                    int perNhan = Util.nextInt(0, 5);
                                    if(perNhan == 0) {
                                        Item itemNhan =  ItemSell.getItemNotSell(992);
                                        Item _itemNhan = new Item(itemNhan);
                                        if(player.addItemToBag(_itemNhan)) {
                                            player.sendAddchatYellow("Bạn vừa nhận được nhẫn thời không sai lệch");
                                            Service.gI().updateItemBag(player);
                                            TaskService.gI().updateCountTask(player);
                                        }
                                    }
                                }
                                //END CHECK NHIEM VU NHAN THOI KHONG
                                //CHECK SP MABU
                                    if(bosses.get(i)._typeBoss >= (byte)36 && bosses.get(i)._typeBoss <= (byte)39) {
                                        player.pointMabu = (byte)10;
                                        Service.gI().setPowerPoint(player, "TL", (short)10, (short)10, (short)10);
                                    }
                                //END CHECK SP MABU
                                Boss _bossDIE = bosses.get(i);
                                if(_bossDIE._typeBoss >= (byte)44 && _bossDIE._typeBoss <= 47) {
//                                    player.zone.leaveBoss(_bossDIE);
                                    player.zone.leaveBossYardrat(_bossDIE);
                                } else {
                                    Timer timerBossLeave = new Timer();
                                    TimerTask bossLeave = new TimerTask() {
                                        public void run()
                                        {
                                            player.zone.leaveBoss(_bossDIE); //xoa boss
                                        };
                                    };
                                    timerBossLeave.schedule(bossLeave, 5000);
                                }
//                                return;
                            } else {
                                player.zone.dameChar(bosses.get(i).id, bosses.get(i).hp, dameBoom, false);
                            }
                        }
                    }
                }

                for(Player _p: players) {
                    if(Service.gI().checkCanAttackChar(player, _p) && !_p.isdie) {
//                    if(((_p.cPk != 0 && _p.cPk != player.cPk && player.cPk != 0) || (_p.cPk == 8 && player.cPk != 0) || (_p.cPk != 0 && player.cPk == 8)) && _p.id != player.id && !_p.isdie) {
                        if (Math.abs(player.x - _p.x) < skill.dx && Math.abs(player.y - _p.y) < skill.dy) {

                            _p.hp -= dameBoom;
                            if(_p.hp <= 0) {
                                _p.isdie = true;
                                _p.isTTNL = false;
                                _p.hp = 0;
                            }
                            if (_p.isdie) {
                                //NEU DEO NGOC RONG SAO DEN THI ROT RA DAT
                                Service.gI().dropDragonBall(_p);
                                
                                //CHECK NEU DANG CON DE TRUNG THI REMOVE DE TRUNG
                                if(_p.chimFollow == (byte)1 && _p.dameChim > 0) {
                                    _p.zone.useDeTrung(_p, (byte)7);
                                    _p.chimFollow = (byte)0;
                                    _p.dameChim = 0;
                                    _p.timerDeTrung.cancel();
                                    _p.timerDeTrung = null;
                                }
                                for(Player _pll: players) {
                                    if(_pll.id == _p.id) {
                                        if(_p.isMonkey) {
                                            Service.gI().loadCaiTrangTemp(_p);
                                            _p.isMonkey = false;
                                            //NOI TAI TANG DAME KHI HOA KHI
                                            if(_p.upDameAfterKhi && _p.noiTai.id != 0 && _p.noiTai.idSkill == (byte)13) {
                                                _p.upDameAfterKhi = false;
                                            }
                                            //NOI TAI TANG DAME KHI HOA KHI
                                            Service.gI().loadPoint(_p.session, _p);
                                        }
                                        player.zone.sendDieToMe(_p);
                                    } else {
                                        _pll.sendDefaultTransformToPlayer(_p);
                                        player.zone.sendDieToAnotherPlayer(_pll, _p);
                                    }
                                }
                            } else {
                                player.zone.dameChar(_p.id, _p.hp, dameBoom, false);
                            }
                            //ve dap kenh khi cho cac char khac
    //                        player.zone.attachedChar(player.id, _p.id, templateSkillUse.skillId);
                        }
                    }
                }

                for(Detu _pet: petss) {
                    if(((_pet.cPk != 0 && _pet.cPk != player.cPk && player.cPk != 0) || (_pet.cPk == 8 && player.cPk != 0) || (_pet.cPk != 0 && player.cPk == 8)) && _pet.id != player.id && !_pet.isdie) {
                        if (Math.abs(player.x - _pet.x) < skill.dx && Math.abs(player.y - _pet.y) < skill.dy) {

                            _pet.hp -= dameBoom;
                            if(_pet.hp <= 0) {
                                _pet.isdie = true;
                                _pet.isTTNL = false;
                                _pet.hp = 0;
                            }
                            if (_pet.isdie) {
                                //SEND TASK HOI SINH DE TU NEU DANH CHET
                                Timer hoiSinhDetu = new Timer();
                                TimerTask hsDetu = new TimerTask() {
                                    public void run()
                                    {
                                        if(_pet.isdie) {
                                            player.timerHSDe = null;
                                            hoiSinhDetu.cancel();
                                            Player suPhu = PlayerManger.gI().getPlayerByDetuID(_pet.id);
                                            _pet.x = suPhu.x;
                                            _pet.y = suPhu.y;
                                            Service.gI().petLiveFromDead(suPhu);
                                            if(suPhu.statusPet == (byte)1 || suPhu.statusPet == (byte)2) {
                                                suPhu.zone.PetAttack(suPhu, _pet, suPhu.statusPet);
                                            }
                                        } else {
                                            hoiSinhDetu.cancel();
                                        }
                                    };
                                };
                                hoiSinhDetu.schedule(hsDetu, 60000);
                                player.timerHSDe = hoiSinhDetu;
                                for(Player _pll: players) {

                                    _pll.sendDefaultTransformToPlayer(_pet);
                                    player.zone.sendDieToAnotherPlayer(_pll, _pet);
                                }
                            } else {
                                player.zone.dameChar(_pet.id, _pet.hp, dameBoom, false);
                            }
                            //ve dap kenh khi cho cac char khac
    //                        player.zone.attachedChar(player.id, _p.id, templateSkillUse.skillId);
                        }
                    }
                }

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
}
