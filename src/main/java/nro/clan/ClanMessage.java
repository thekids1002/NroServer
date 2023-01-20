package nro.clan;

import nro.player.Player;

public class ClanMessage {
    public int id;
    public byte type;
    public int playerId;
    public String playerName;
    public byte role;
    public int time;
    public String text;
    public byte color;
    public byte recieve = 0;
    
    public ClanMessage(Player player, int idMess, byte typeMess, String textMess, byte colorMess) {
        this.id = idMess;
        this.type = typeMess;
        this.playerId = player.id;
        this.playerName = player.name;
        this.role = player.rolePT;
        this.time = (int)(System.currentTimeMillis() / 1000);
        this.text = textMess;
        this.color = colorMess;
    }
}
