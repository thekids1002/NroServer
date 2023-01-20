package nro.clan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import nro.main.DBService;
import nro.main.DataSource;

public class ClanDAO {

    synchronized public static void create(Clan clan) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DataSource.getConnection();
            ps = conn.prepareStatement("INSERT INTO clan(id, name, leader_id, img_id, tcreate, topen) VALUES(?,?,?,?,?,?)");
            ps.setInt(1, clan.id);
            ps.setString(2, clan.name);
            ps.setInt(3, clan.leaderID);
            ps.setInt(4, (int)clan.imgID);
//            ps.setString(5, Long.toString(System.currentTimeMillis()));
//            ps.setString(6, Long.toString(System.currentTimeMillis() + (long)86400000));
            ps.setString(5, Long.toString(clan.tcreate));
            ps.setString(6, Long.toString(clan.topen));
            ps.executeUpdate();
            conn.commit();
            ps.close();
            ps = null;

            ps = conn.prepareStatement("INSERT INTO clan_member(clan_id, player_id, name, head, headicon, body, leg, role, power_point) VALUES(?,?,?,?,?,?,?,?,?)");
            ps.setInt(1, clan.id);
            ps.setInt(2, clan.leaderID);
            ps.setString(3, clan.leaderName);
            ps.setInt(4, (int)(clan.members.get(0).head));
            ps.setInt(5, (int)(clan.members.get(0).headICON));
            ps.setInt(6, (int)(clan.members.get(0).body));
            ps.setInt(7, (int)(clan.members.get(0).leg));
            ps.setInt(8, 0); //role
            ps.setLong(9, clan.members.get(0).powerPoint);
            ps.executeUpdate();
            conn.commit();
            conn.close();
        } catch (Exception e) {
            System.out.println("error: " + e);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                    ps = null;
                }
                if (conn != null) {
                    conn.close();
                    conn = null;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void update(Clan clan) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DataSource.getConnection();
            ps = conn.prepareStatement("UPDATE clan SET leader_id=?,img_id=? WHERE id=? LIMIT 1");
            ps.setInt(1, clan.leaderID);
            ps.setInt(2, (int)clan.imgID);
            ps.setInt(3, clan.id);
            ps.executeUpdate();
            conn.commit();
            ps.close();
            conn.close();
        } catch (Exception e) {
            System.out.println("error: " + e);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                    ps = null;
                }
                if (conn != null) {
                    conn.close();
                    conn = null;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static ArrayList<Clan> load() {
        ArrayList<Clan> clans = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DataSource.getConnection();
            ps = conn.prepareStatement("SELECT * FROM clan");
            rs = ps.executeQuery();
            while (rs.next()) {
                Clan clan = new Clan();
                clan.id = rs.getInt("id");
                clan.name = rs.getString("name");// name
                clan.slogan = rs.getString("slogan");// slogan
                clan.imgID = rs.getByte("img_id");// img id
                clan.level = rs.getByte("level");// level
                clan.powerPoint = rs.getLong("power_point");// power point
                clan.leaderID = rs.getInt("leader_id");// leader id
                clan.clanPoint = rs.getInt("clan_point");// clan point

                clan.maxMember = rs.getByte("max_member");// max mem
                clan.time = rs.getTimestamp("create_time").getTime();// time
                clan.tcreate = Long.parseLong(rs.getString("tcreate"));
                clan.topen = Long.parseLong(rs.getString("topen"));
                clan.members = getMembers(clan.id, clan);
                clan.currMember = (byte)clan.members.size();
                clans.add(clan);
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                    ps = null;
                }
                if (conn != null) {
                    conn.close();
                    conn = null;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return clans;
    }

    public static ArrayList<Member> getMembers(int clanId, Clan clan) {
        ArrayList<Member> members = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DataSource.getConnection();
            ps = conn.prepareStatement("SELECT * FROM clan_member WHERE clan_id=?");
            ps.setInt(1, clanId);
            rs = ps.executeQuery();
            while (rs.next()) {
                Member member = new Member();
                member.id = rs.getInt("player_id");
                member.name = rs.getString("name");
                member.powerPoint = rs.getLong("power_point");
                member.role = rs.getByte("role");
                if(member.role == 0) {
                    clan.leaderName = rs.getString("name");
                }
                member.head = rs.getShort("head");
                member.headICON = rs.getShort("headicon");
                member.body = rs.getShort("body");
                member.leg = rs.getShort("leg");
                member.donate = rs.getInt("donate");
                member.receiveDonate = rs.getInt("receive_donate");
                member.clanPoint = rs.getInt("clan_point");
                member.currPoint = rs.getInt("curr_point");
                member.joinTime = (int) rs.getTimestamp("join_time").getTime() / 1000;
                members.add(member);
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println("error getMembers: " + e.toString());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                    ps = null;
                }
                if (conn != null) {
                    conn.close();
                    conn = null;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return members;
    }

    public static void updateSlogan(int clanId, String slogan) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DataSource.getConnection();
            ps = conn.prepareStatement("UPDATE clan SET slogan=? WHERE id=? LIMIT 1");
            ps.setString(1, slogan);
            ps.setInt(2, clanId);
            ps.executeUpdate();
            conn.commit();
            conn.close();
            ClanManager.gI().getClanById(clanId).slogan = slogan;
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    synchronized public static void updateTimeOpenDT(int clanId) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DataSource.getConnection();
            ps = conn.prepareStatement("UPDATE clan SET topen=? WHERE id=? LIMIT 1");
            ps.setString(1, Long.toString(System.currentTimeMillis()));
            ps.setInt(2, clanId);
            ps.executeUpdate();
            conn.commit();
            ps.close();
            conn.close();
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void changeRole(int playerId, int role, int clanId) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DataSource.getConnection();
            ps = conn.prepareStatement("UPDATE clan_member SET role=?,update_time=? WHERE player_id=? LIMIT 1");
            ps.setInt(1, role);
            ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            ps.setInt(3, playerId);
            ps.executeUpdate();
            conn.commit();
            ps.close();
            ps = null;

            ps = conn.prepareStatement("UPDATE player SET role_pt=? WHERE account_id=? LIMIT 1");
            ps.setInt(1, role);
            ps.setInt(2, playerId);
            ps.executeUpdate();
            conn.commit();
            ps.close();
            if(role == 0) {
                ps = null;
                ps = conn.prepareStatement("UPDATE clan SET leader_id=? WHERE id=? LIMIT 1");
                ps.setInt(1, playerId);
                ps.setInt(2, clanId);
                ps.executeUpdate();
                conn.commit();
            }
            conn.close();
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    synchronized public static void distory(int _guildID) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DataSource.getConnection();
            ps = conn.prepareStatement("DELETE FROM clan_member WHERE clan_id=?");
            ps.setInt(1, _guildID);
            ps.executeUpdate();
            conn.commit();
            ps.close();
            ps = null;
            ps = conn.prepareStatement("DELETE FROM clan WHERE id=? LIMIT 1");
            ps.setInt(1, _guildID);
            ps.executeUpdate();
            conn.commit();
            ps.close();

            ps = null;
            ps = conn.prepareStatement("UPDATE player SET clan_id=-1,role_pt=2 WHERE clan_id=?");
            ps.setInt(1, _guildID);
            ps.executeUpdate();
            conn.commit();
            conn.close();
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    synchronized public static boolean add(int clanId, Member member) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DataSource.getConnection();
            ps = conn.prepareStatement("INSERT INTO clan_member(clan_id, player_id, name, head, headicon, body, leg, role, power_point) VALUES(?,?,?,?,?,?,?,?,?)");
            ps.setInt(1, clanId);
            ps.setInt(2, member.id);
            ps.setString(3, member.name);
            ps.setInt(4, (int)member.head);
            ps.setInt(5, (int)member.headICON);
            ps.setInt(6, (int)member.body);
            ps.setInt(7, (int)member.leg);
            ps.setInt(8, (int)2);
            ps.setLong(9, member.powerPoint);
            ps.executeUpdate();
            conn.commit();
            conn.close();
            return true;
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    synchronized public static void removeClanMember(int _userID) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DataSource.getConnection();
            ps = conn.prepareStatement("DELETE FROM clan_member WHERE player_id=? LIMIT 1");
            ps.setInt(1, _userID);
            ps.executeUpdate();
            conn.commit();
            ps.close();
            ps = null;
            ps = conn.prepareStatement("UPDATE player SET role_pt=2,clan_id=-1 WHERE account_id=? LIMIT 1");
            ps.setInt(1, _userID);
            ps.executeUpdate();
            conn.commit();
            conn.close();
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static ArrayList<Clan> searchClan(String text) {
        ArrayList<Clan> clans = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DataSource.getConnection();
            ps = conn.prepareStatement("select * from clan where name like ? limit 10");
            ps.setString(1, text + "%");
            rs = ps.executeQuery();
            if (DBService.resultSize(rs) != 0) {
                while (rs.next()) {
                    Clan clan = new Clan();
                    clan.id = rs.getByte("id");// id
                    clan.name = rs.getString("name");// name
                    clan.slogan = rs.getString("slogan");// slogan
                    clan.imgID = rs.getByte("img_id");// img id
                    clan.powerPoint = rs.getLong("power_point");// power point
                    clan.leaderID = rs.getInt("leader_id");// leader id
                    clan.currMember = 1;// curr mem
                    clan.maxMember = 10;// max mem
                    clan.time = (int) System.currentTimeMillis();// time
                    clans.add(clan);
                }
            }
            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            System.out.println("error: " + e);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                    ps = null;
                }
                if (conn != null) {
                    conn.close();
                    conn = null;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return clans;
    }

    public static void updateAllDataClan(Clan clan) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DataSource.getConnection();
            ps = conn.prepareStatement("UPDATE clan SET slogan=?,leader_id=?,img_id=?,topen=? WHERE id=? LIMIT 1");
            ps.setString(1, clan.slogan);
            ps.setInt(2, clan.leaderID);
            ps.setInt(3, (int)clan.imgID);
            ps.setString(4, Long.toString(clan.topen));
            ps.setInt(5, clan.id);
            ps.executeUpdate();
            conn.commit();
            ps.close();

            ps = null;
            ps = conn.prepareStatement("DELETE FROM clan_member WHERE clan_id=?");
            ps.setInt(1, clan.id);
            ps.executeUpdate();
            conn.commit();
            ps.close();
            conn.close();
        } catch (Exception e) {
            System.out.println("error: " + e);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                    ps = null;
                }
                if (conn != null) {
                    conn.close();
                    conn = null;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
