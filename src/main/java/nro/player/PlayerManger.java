package nro.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;

import nro.io.Message;
import nro.io.Session;

public class PlayerManger {

    private static PlayerManger instance;

    private ArrayList<Player> players;
    private ArrayList<User> users;
    private Timer timer;
    public final ArrayList<Session> conns;
    public final HashMap<Integer, Session> conns_id;
    public final HashMap<Integer, Player> players_id = new HashMap<Integer, Player>();
    public final HashMap<String, Player> players_uname = new HashMap<String, Player>();
    public PlayerManger() {
        this.players = new ArrayList<>();
        this.timer = new Timer();
        this.users = new ArrayList<>();
        this.conns = new ArrayList<>();
        this.conns_id = new HashMap<>();
    }
    
    public void kick(Session s){
        //clear all message
        s.clearMessage();
        if (conns_id.containsKey(s.userId)) {
            this.conns_id.remove(s.userId);
        }
        if (conns.contains(s)) {
            this.conns.remove(s);
        }
        if (s.player != null) {
            if (players_id.containsKey(s.player.id)) {
                this.players_id.remove(s.player.id);
            }
            //
            if (players_uname.containsKey(s.player.name)) {
                this.players_uname.remove(s.player.name);
            }
            //
            s.disconnect();
        }
        s = null;
    }
    
    public static PlayerManger gI(){
        if (instance == null){
            instance = new PlayerManger();
        }
        return instance;
    }

    public Player getPlayerByUserID(int _userID) {
        for (Player player : players) {
            if (player.session.userId == _userID){
                return player;
            }
        }
        return null;
    }
    public Player getPlayerByDetuID(int _detuID) {
        for (Player player : players) {
            if (player.havePet == (byte)1 && player.detu.id == _detuID){
                return player;
            }
        }
        return null;
    }
    public Player getPlayerByName(String name) {
        for (Player player : players) {
            if (player.name.equals(name) && player.session != null){
                return player;
            }
        }
        return null;
    }
    public User getUserID(int _userID) {
        for (User player : users) {
            if (player.session.userId == _userID){
                return player;
            }
        }
        return null;
    }
    public ArrayList<User> getUsers() {
        return users;
    }
    public ArrayList<Player> getPlayers() {
        return players;
    }
    
    public int size(){
        return players.size();
    }
    public void SendMessageServer(Message m) {
        synchronized (conns) {
            for (int i = conns.size()-1; i >= 0; i--)
                if (conns.get(i).player != null)
                    conns.get(i).sendMessage(m);
        }
    }
    public void put(Session conn) {
        if (!conns_id.containsValue(conn)){
            conns_id.put(conn.userId, conn);
        } 
        if (!conns.contains(conn)){
            conns.add(conn);
        }
    }
    public void put(Player p) {
        if (!players_id.containsKey(p.id)){
            players_id.put(p.id, p);
        }
    }

}
