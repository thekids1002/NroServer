package nro.task;

import nro.player.Player;
import nro.io.Message;
import nro.io.Session;

public class TaskService {
    private static TaskService instance;

    public static TaskService gI() {
        if (instance == null) {
            instance = new TaskService();
        }
        return instance;
    }

    public void loadCurrentTask(Session session, Task _task) {
        Message m = null;
        try {
//            Task _task = TaskManager.gI().getTasks().get(1);
            m = new Message(40);
            m.writer().writeShort(_task.taskId);
            m.writer().writeByte(_task.index);
//            m.writer().writeByte((byte)1);
            m.writer().writeUTF(_task.name);
            m.writer().writeUTF(_task.detail);
            m.writer().writeByte(_task.countSub);
            for(byte i = 0; i < _task.countSub; i++) {
                m.writer().writeUTF(_task.subNames[i]);
                m.writer().writeByte(_task.subtasks[i]);
                m.writer().writeShort(_task.mapTasks[i]);
                m.writer().writeUTF(_task.contentInfo[i]);
            }
            m.writer().writeShort((short)_task.count);
            for(byte i = 0; i < _task.counts.length; i++) {
                m.writer().writeShort(_task.counts[i]);
            }
            session.sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    //
    public void setupNextNewTask(Player p, byte idTask) {
        Task _task = new Task();
        _task.setupTask(TaskManager.gI().getTasks().get(idTask), p.gender);
        p.crrTask = _task;
        p.taskId = idTask; //ID TASK TIEP THEO

        loadCurrentTask(p.session, p.crrTask);
    }

    //UPDATE TASK
    public void updateCountTask(Player p) {
        short countNow = p.crrTask.counts[p.crrTask.index];
        if(countNow != (short)(-1) && p.crrTask.count < countNow) {
            p.crrTask.count++;
            if(p.crrTask.count == countNow) { //UPDATE TIEN DO NHIEM VU
                p.crrTask.index++;
//                if(p.crrTask.index == (byte)(p.crrTask.countSub - (byte)1)) {
//                    loadCurrentTask(p.session, p.crrTask);
//                    return;
//                }
                p.crrTask.count = (short)0;
                //SEND UPDATE TASK
                sendNextTaskChild(p);
//                if(p.taskId == (short)5 && p.crrTask.index == (byte)2) {
//                    p.crrTask.taskId = (short)100;
//                    loadCurrentTask(p.session, p.crrTask);
//                }
                return;
            }
            //SEND UPDATE COUNT TASK
            sendUpCountTask(p);
        } else if(countNow == (short)(-1) && p.crrTask.index < p.crrTask.countSub) { //UPDATE NEXT NHIEM VU MA KHONG TANG COUNT
            //SEND UPDATE TASK
            p.crrTask.index++;
            p.crrTask.count = (short)0;
            sendNextTaskChild(p);
            return;
        }
    }

    //SEND UPDATE COUNT TASK
    public void sendUpCountTask(Player p) {
        Message m = null;
        try {
            m = new Message(43);
            m.writer().writeShort(p.crrTask.count);
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
    //SEND NEXT TASK CON
    public void sendNextTaskChild(Player p) {
        Message m = null;
        try {
            m = new Message(41);
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
