package nro.item;

import java.util.ArrayList;

import nro.player.Player;

public class Item {
    public int id;
    public ItemTemplate template;
    public String info;
    public String content;
    public int quantity;
    public boolean isExpires = false;
    public boolean isUpToUp;
    public int quantityTemp = 1;
    public short headTemp;
    public short bodyTemp;
    public short legTemp;
    public int idTemp;
    public int _starItem = 0;
    public int _levelItem =0;
    public ArrayList<ItemOption> itemOptions;
    public static ArrayList<Item> entrys = new ArrayList<Item>();
    public byte isDrop = -1;
    //TIME GIAP LUYEN TAP
    public long timeGLT = 0;
    public byte cUpTimeGLT = 0;
    public boolean maxTimeGLT = false;
    public boolean useNow = false;
    //TIME END CAI TRANG
    public long timeHSD = 0;

    public Item() {
        this.id = -1;
        this.quantity = 1;
        this.itemOptions = new ArrayList<ItemOption>();
    }
    public Item(Item _item) { //INIT ITEM TU TEMPLATE
        this.id = _item.id;
        this.template = _item.template;
        this.info = _item.info;
        this.content = _item.content;
        this.isExpires = _item.isExpires;
        this.isUpToUp = _item.isUpToUp;
        this.quantityTemp = _item.quantityTemp;
        this.headTemp = _item.headTemp;
        this.bodyTemp = _item.bodyTemp;
        this.legTemp = _item.legTemp;
        this.idTemp = _item.idTemp;
//        this.itemOptions = _item.itemOptions;
        this.itemOptions = new ArrayList<ItemOption>();
        for(ItemOption _option: _item.itemOptions) {
            this.itemOptions.add(new ItemOption(_option.id, _option.param));
        }

        this.entrys = _item.entrys;
        this.quantity = 1;
        this.timeHSD = _item.timeHSD;
    }
    public String getInfo() {
        String strInfo = "";
        for (ItemOption itemOption : itemOptions) {
            strInfo += itemOption.getOptionString();
        }
        return strInfo;
    }

    public String getInfoUpStar(Player _player) {
        String strInfo = this.template.name;
        boolean hasStar = false;
        int starItem = 0;
        for (ItemOption itemOption : itemOptions) {
            if(itemOption.id == 107 && itemOption.param < 8) {
                starItem = itemOption.param;
                this._starItem = itemOption.param;
                strInfo += "\b|0|" + (itemOption.param + 1) + " Sao Pha Lê";
                hasStar = true;
            } else if(itemOption.id != 102) {
                strInfo += "\b|0|" + itemOption.getOptionString();
            }
        }
        if(!hasStar) {
            strInfo += "\b|0|1 Sao Pha Lê";
        }
        if(starItem == 7) {
            strInfo += "\b|2|Tỉ lệ thành công 0.5%";
        } else {
            strInfo += "\b|2|Tỉ lệ thành công " + getPercentUpStar(starItem) + "%";
        }
        if(_player.vang >= (getGoldUpStar(starItem)*1000000L)) {
            strInfo += "\b|2|Cần " + getGoldUpStar(starItem) + " Tr vàng";
        } else {
            strInfo += "\b|7|Cần " + getGoldUpStar(starItem) + " Tr vàng";
        }
        return strInfo;
    }

    public String getInfoEpStar(Player _player, int _idEp) {
        int _idOption = mapItemEpStarToOption(_idEp);
        int _PerStar = mapItemEpParamPerStar(_idEp);
        String strInfo = this.template.name;
        boolean hasStar = false;
        for (ItemOption itemOption : itemOptions) {
            if(itemOption.id == _idOption && itemOption.param < 8*_PerStar) {
                ItemOption itOpNew = new ItemOption(_idOption, (itemOption.param + _PerStar));
                strInfo += "\b|0|" + itOpNew.getOptionString();
                hasStar = true;
            } else if(itemOption.id != 102 && itemOption.id != 107) {
                strInfo += "\b|0|" + itemOption.getOptionString();
            }
        }
        if(!hasStar) {
            ItemOption itOpNew2 = new ItemOption(_idOption, _PerStar);
            strInfo += "\b|0|" + itOpNew2.getOptionString();
        }
        if(_player.ngoc >= 10) {
            strInfo += "\b|2|Cần 10 ngọc";
        } else {
            strInfo += "\b|7|Cần 10 ngọc";
        }
        return strInfo;
    }

    public String getInfoUpgradeItem(Player _player, int _idEp, byte _indexStone) {
        String strInfo = this.template.name + "\b|2|Hiện tại";
        int levelUp = 0;
        for (ItemOption itemOption : itemOptions) {
            if(itemOption.id == 72 && itemOption.param < 8) {
                levelUp = itemOption.param;
                this._levelItem = itemOption.param;
            }
            if(itemOption.id != 102 && itemOption.id != 107) {
                strInfo += "\b|0|" + itemOption.getOptionString();
            }
        }
        strInfo += "\b|2|Sau khi nâng cấp (+" + (levelUp + 1) + ")";
        if(getParamItemByID(mapStoneUpToOption(_idEp)) != 0) {
            for (ItemOption itemOption : itemOptions) {
                if(itemOption.id == mapStoneUpToOption(_idEp)) {
                    strInfo += "\b|1|" + mapPointTypeUpByIdStone(_idEp) + "+" + (int)Math.ceil(itemOption.param*1.1);
                } else if(itemOption.id != 102 && itemOption.id != 107) {
                    strInfo += "\b|1|" + itemOption.getOptionString();
                }
            }
        } else if(_idEp == 222) {
            for (ItemOption itemOption : itemOptions) {
                if(itemOption.id == 22) {
                    strInfo += "\b|1|" + mapPointTypeUpByIdStone(_idEp) + "+" + (int)Math.ceil(itemOption.param*1.1)*1000;
                } else if(itemOption.id != 102 && itemOption.id != 107) {
                    strInfo += "\b|1|" + itemOption.getOptionString();
                }
            }
        } else if(_idEp == 221) {
            for (ItemOption itemOption : itemOptions) {
                if(itemOption.id == 23) {
                    strInfo += "\b|1|" + mapPointTypeUpByIdStone(_idEp) + "+" + (int)Math.ceil(itemOption.param*1.1)*1000;
                } else if(itemOption.id != 102 && itemOption.id != 107) {
                    strInfo += "\b|1|" + itemOption.getOptionString();
                }
            }
        }
        
        strInfo += "\b|2|Tỉ lệ thành công: " + getPercentUpItem(levelUp) + "%";
        if(_player.ItemBag[_indexStone].quantity >= getCountStoneUpItem(levelUp)) {
            strInfo += "\b|2|Cần " + getCountStoneUpItem(levelUp) + " Đá " + mapTypeStone(_idEp);
        } else {
            strInfo += "\b|7|Cần " + getCountStoneUpItem(levelUp) + " Đá " + mapTypeStone(_idEp);
        }
        if(_player.vang >= getGoldUpItem(levelUp)) {
            strInfo += "\b|2|Cần " + getGoldUpItem(levelUp) + "k vàng";
        } else {
            strInfo += "\b|7|Cần " + getGoldUpItem(levelUp) + "k vàng";
        }
        return strInfo;
    }

    public int getPercentUpStar(int _star) {
        if (_star == 0) {
            return 50;
        }  else if(_star == 1) {
            return 20;
        } else if (_star == 2) {
            return 10;
        } else if (_star == 3) {
            return 5;
        } else if (_star == 4) {
            return 3;
        } else if (_star == 5) {
            return 2;
        } else if (_star == 6) {
            return 1;
        } else if (_star == 7) {
            return 1;
        }
        return 1;
    }

    public int getGoldUpStar(int _star) {
        if (_star == 0) {
            return 5;
        }  else if(_star == 1) {
            return 10;
        } else if (_star == 2) {
            return 20;
        } else if (_star == 3) {
            return 40;
        } else if (_star == 4) {
            return 60;
        } else if (_star == 5) {
            return 90;
        } else if (_star == 6) {
            return 120;
        } else if (_star == 7) {
            return 240;
        }
        return 240;
    }

    public int mapItemEpStarToOption(int _idItem) {
        if (_idItem == 14) { return 108; } //nr1s
        else if (_idItem == 15) { return 94; } //nr2s done
        else if (_idItem == 16) { return 50; } //nr3s done
        else if (_idItem == 17) { return 81; } //nr4s
        else if (_idItem == 18) { return 80; } //nr5s
        else if (_idItem == 19) { return 103; } //nr6s done
        else if (_idItem == 20) { return 77; } //nr7s done
        else if (_idItem == 441) { return 95; } //spl hut mau done
        else if (_idItem == 442) { return 96; } //spl hut ki done
        else if (_idItem == 443) { return 97; } //spl phanst
        else if (_idItem == 444) { return 98; } //spl xuyen giap chuong
        else if (_idItem == 445) { return 99; } //spl xuyen giap cchien
        else if (_idItem == 446) { return 100; } //spl vang
        else if (_idItem == 447) { return 101; } //spl tnsm done
        return 0;
    }

    public int mapItemEpParamPerStar(int _idItem) {
        if (_idItem == 14) { return 2; } //nr1s
        else if (_idItem == 15) { return 2; } //nr2s
        else if (_idItem == 16) { return 3; } //nr3s
        else if (_idItem == 17) { return 5; } //nr4s
        else if (_idItem == 18) { return 5; } //nr5s
        else if (_idItem == 19) { return 5; } //nr6s
        else if (_idItem == 20) { return 5; } //nr7s
        else if (_idItem == 441) { return 5; } //spl hut mau
        else if (_idItem == 442) { return 5; } //spl hut ki
        else if (_idItem == 443) { return 5; } //spl phanst
        else if (_idItem == 444) { return 5; } //spl xuyen giap chuong
        else if (_idItem == 445) { return 5; } //spl xuyen giap cchien
        else if (_idItem == 446) { return 5; } //spl vang
        else if (_idItem == 447) { return 5; } //spl tnsm
        return 0;
    }

    public int mapStoneUpToOption(int _idItem) { //map id da dap do sang option dap do
        if (_idItem == 220) { return 14; } //chimang+
        else if (_idItem == 221) { return 7; } //ki+
        else if (_idItem == 222) { return 6; } //hp+
        else if (_idItem == 223) { return 47; } //giap+
        else if (_idItem == 224) { return 0; } //tancong+
        return 0;
    }

    public String mapPointTypeUpByIdStone(int _idItem) { //
        if (_idItem == 220) { return "Chí mạng"; } //chimang+
        else if (_idItem == 221) { return "KI"; } //ki+
        else if (_idItem == 222) { return "HP"; } //hp+
        else if (_idItem == 223) { return "Giáp"; } //giap+
        else if (_idItem == 224) { return "Tấn công"; } //tancong+
        return "";
    }

    public String mapTypeStone(int _idItem) { //
        if (_idItem == 220) { return "Lục bảo"; } //chimang+
        else if (_idItem == 221) { return "Saphia"; } //ki+
        else if (_idItem == 222) { return "Ruby"; } //hp+
        else if (_idItem == 223) { return "Titan"; } //giap+
        else if (_idItem == 224) { return "Thạch anh tím"; } //tancong+
        return "";
    }

    public int getPercentUpItem(int _levelNow) {
        if (_levelNow == 0) {
            return 80;
        }  else if(_levelNow == 1) {
            return 50;
        } else if (_levelNow == 2) {
            return 20;
        } else if (_levelNow == 3) {
            return 10;
        } else if (_levelNow == 4) {
            return 5;
        } else if (_levelNow == 5) {
            return 3;
        } else if (_levelNow == 6) {
            return 2;
        } else if (_levelNow == 7) {
            return 1;
        }
        return 1;
    }

    public int getGoldUpItem(int _levelNow) {
        if (_levelNow == 0) {
            return 80;
        }  else if(_levelNow == 1) {
            return 160;
        } else if (_levelNow == 2) {
            return 240;
        } else if (_levelNow == 3) {
            return 320;
        } else if (_levelNow == 4) {
            return 400;
        } else if (_levelNow == 5) {
            return 480;
        } else if (_levelNow == 6) {
            return 560;
        } else if (_levelNow == 7) {
            return 1120;
        }
        return 1120;
    }

    public int getCountStoneUpItem(int _levelNow) {
        if (_levelNow == 0) {
            return 7;
        }  else if(_levelNow == 1) {
            return 9;
        } else if (_levelNow == 2) {
            return 11;
        } else if (_levelNow == 3) {
            return 13;
        } else if (_levelNow == 4) {
            return 15;
        } else if (_levelNow == 5) {
            return 17;
        } else if (_levelNow == 6) {
            return 20;
        } else if (_levelNow == 7) {
            return 25;
        }
        return 25;
    }

    // lay id template cuar skill vaf point cua skill dua tren id cua sach vo cong
    public String getIDTemplateAndLevelSkill(int _idBook) {
        switch(_idBook) {
            case 66: //dam dragon
                return "0,1,0";
            case 67:
                return "0,2,0";
            case 68:
                return "0,3,0";
            case 69:
                return "0,4,0";
            case 70:
                return "0,5,0";
            case 71:
                return "0,6,0";
            case 72:
                return "0,7,0";
            case 94: //kamejoko
                return "1,1,0";
            case 95:
                return "1,2,0";
            case 96:
                return "1,3,0";
            case 97:
                return "1,4,0";
            case 98:
                return "1,5,0";
            case 99:
                return "1,6,0";
            case 100:
                return "1,7,0";
            case 115: //tdhs
                return "6,1,0";
            case 116:
                return "6,2,0";
            case 117:
                return "6,3,0";
            case 118:
                return "6,4,0";
            case 119:
                return "6,5,0";
            case 120:
                return "6,6,0";
            case 121:
                return "6,7,0";
            case 300: //kaioken
                return "9,1,10";
            case 301:
                return "9,2,10";
            case 302:
                return "9,3,10";
            case 303:
                return "9,4,10";
            case 304:
                return "9,5,10";
            case 305:
                return "9,6,10";
            case 306:
                return "9,7,10";
            case 307: //kenhkhi
                return "10,1,10";
            case 308:
                return "10,2,10";
            case 309:
                return "10,3,10";
            case 310:
                return "10,4,10";
            case 311:
                return "10,5,10";
            case 312:
                return "10,6,10";
            case 313:
                return "10,7,10";
            case 488: //dich chuyen tuc thoi
                return "20,1,150";
            case 489:
                return "20,2,150";
            case 490:
                return "20,3,150";
            case 491:
                return "20,4,150";
            case 492:
                return "20,5,150";
            case 493:
                return "20,6,150";
            case 494:
                return "20,7,150";
            case 495: //thoi mien
                return "22,1,150";
            case 496:
                return "22,2,150";
            case 497:
                return "22,3,150";
            case 498:
                return "22,4,150";
            case 499:
                return "22,5,150";
            case 500:
                return "22,6,150";
            case 501:
                return "22,7,150";
            case 434: //khien nang luong
                return "19,1,150";
            case 435:
                return "19,2,150";
            case 436:
                return "19,3,150";
            case 437:
                return "19,4,150";
            case 438:
                return "19,5,150";
            case 439:
                return "19,6,150";
            case 440:
                return "19,7,150";
            case 79: //dam demon
                return "2,1,0";
            case 80:
                return "2,2,0";
            case 81:
                return "2,3,0";
            case 82:
                return "2,4,0";
            case 83:
                return "2,5,0";
            case 84:
                return "2,6,0";
            case 86:
                return "2,7,0";
            case 101: //masenko
                return "3,1,0";
            case 102:
                return "3,2,0";
            case 103:
                return "3,3,0";
            case 104:
                return "3,4,0";
            case 105:
                return "3,5,0";
            case 106:
                return "3,6,0";
            case 107:
                return "3,7,0";
            case 122: //tri thuong
                return "7,1,0";
            case 123:
                return "7,2,0";
            case 124:
                return "7,3,0";
            case 125:
                return "7,4,0";
            case 126:
                return "7,5,0";
            case 127:
                return "7,6,0";
            case 128:
                return "7,7,0";
            case 328: //laze
                return "11,1,10";
            case 329:
                return "11,2,10";
            case 330:
                return "11,3,10";
            case 331:
                return "11,4,10";
            case 332:
                return "11,5,10";
            case 333:
                return "11,6,10";
            case 334:
                return "11,7,10";
            case 335: //de trung
                return "12,1,10";
            case 336:
                return "12,2,10";
            case 337:
                return "12,3,10";
            case 338:
                return "12,4,10";
            case 339:
                return "12,5,10";
            case 340:
                return "12,6,10";
            case 341:
                return "12,7,10";
            case 481: //lien hoan
                return "17,1,150";
            case 482:
                return "17,2,150";
            case 483:
                return "17,3,150";
            case 484:
                return "17,4,150";
            case 485:
                return "17,5,150";
            case 486:
                return "17,6,150";
            case 487:
                return "17,7,150";
            case 474: //socola
                return "18,1,150";
            case 475:
                return "18,2,150";
            case 476:
                return "18,3,150";
            case 477:
                return "18,4,150";
            case 478:
                return "18,5,150";
            case 479:
                return "18,6,150";
            case 480:
                return "18,7,150";
            case 87: //dam galick
                return "4,1,0";
            case 88:
                return "4,2,0";
            case 89:
                return "4,3,0";
            case 90:
                return "4,4,0";
            case 91:
                return "4,5,0";
            case 92:
                return "4,6,0";
            case 93:
                return "4,7,0";
            case 108: //atomic
                return "5,1,0";
            case 109:
                return "5,2,0";
            case 110:
                return "5,3,0";
            case 111:
                return "5,4,0";
            case 112:
                return "5,5,0";
            case 113:
                return "5,6,0";
            case 114:
                return "5,7,0";
            case 129: //ttnl
                return "8,1,0";
            case 130:
                return "8,2,0";
            case 131:
                return "8,3,0";
            case 132:
                return "8,4,0";
            case 133:
                return "8,5,0";
            case 134:
                return "8,6,0";
            case 135:
                return "8,7,0";
            case 314: //bien khi
                return "13,1,10";
            case 315:
                return "13,2,10";
            case 316:
                return "13,3,10";
            case 317:
                return "13,4,10";
            case 318:
                return "13,5,10";
            case 319:
                return "13,6,10";
            case 320:
                return "13,7,10";
            case 321: //boom
                return "14,1,10";
            case 322:
                return "14,2,10";
            case 323:
                return "14,3,10";
            case 324:
                return "14,4,10";
            case 325:
                return "14,5,10";
            case 326:
                return "14,6,10";
            case 327:
                return "14,7,10";
            case 509: //saooooo
                return "21,1,150";
            case 510:
                return "21,2,150";
            case 511:
                return "21,3,150";
            case 512:
                return "21,4,150";
            case 513:
                return "21,5,150";
            case 514:
                return "21,6,150";
            case 515:
                return "21,7,150";
            case 502: //troi
                return "23,1,150";
            case 503:
                return "23,2,150";
            case 504:
                return "23,3,150";
            case 505:
                return "23,4,150";
            case 506:
                return "23,5,150";
            case 507:
                return "23,6,150";
            case 508:
                return "23,7,150";
        }
        return "";
    }

    public int getParamItemByID(int id)
    {
        for (ItemOption itemOption : itemOptions) {
            if(itemOption.id == id){
                return itemOption.param;
            }
        }
        return 0;
    }

    public void setNewParam(int id, int paramN) {
        for(byte i = 0; i < itemOptions.size(); i++) {
            if(itemOptions.get(i).id == id) {
                itemOptions.get(i).param = paramN;
                break;
            }
        }
    }

    //CHECK ITEM GIAO DICH DUOC HAY KHONG
    public boolean chkItemCanGD() {
        for (ItemOption itemOption : itemOptions) {
            if(itemOption.id == 30){
                return false;
            }
        }
        return true;
    }
    
    public String getContent() {
        return "Yêu cầu sức mạnh " + this.template.strRequire + " trở lên";
    }

    public boolean isItemBackground() {
        return this.template.id == 740 || this.template.id == 741 || this.template.id == 745 || this.template.id == 805 || this.template.id == 814 ||
                this.template.id == 815 || this.template.id == 816 || this.template.id == 817 || this.template.id == 822 || this.template.id == 823 ||
                this.template.id == 852 || this.template.id == 865 || this.template.id == 966 || this.template.id == 982 || this.template.id == 983 ||
                this.template.id == 994 || this.template.id == 1030 || this.template.id == 1031;
    }
}
