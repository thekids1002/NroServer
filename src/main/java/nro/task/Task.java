package nro.task;

public class Task {

    public short taskId;
    public byte index = 0;
    public String name;
    public String detail;

    public int max;

    public byte countSub;
    public String[] subNames;
    public String[] contentInfo;
    public byte[] subtasks;
    public short[] mapTasks;

    public short count = 0;
    public short[] counts;
    public long bonus = 0;
    //ID QUAI THEO TUNG TASK NHIEM VU, ID BOSS LUON
    public int[] modTask;

    //SET UP NEW TEST BY TASK TEMPLATE
    public void setupTask(Task _task, byte gender) {
        this.taskId = _task.taskId;
        this.index = _task.index;
        this.name = _task.name;
        this.detail = TaskManager.gI().detailTASK[gender][this.taskId];
        this.bonus = _task.bonus;
        this.countSub = _task.countSub;
        this.subNames = new String[this.countSub];
        this.contentInfo = new String[this.countSub];
        this.subtasks = new byte[this.countSub];
        this.mapTasks = new short[this.countSub];
        this.counts = new short[this.countSub];
        for(byte i = 0; i < this.countSub; i++) {
            if(gender == (byte)0) {
                this.subNames[i] = TaskManager.gI().subnameTASK0[this.taskId][i];
                this.contentInfo[i] = TaskManager.gI().contentInfoTASK0[this.taskId][i];
                this.subtasks[i] = TaskManager.gI().npcTASK0[this.taskId][i];
                this.mapTasks[i] = TaskManager.gI().mapTASK0[this.taskId][i];
            } else if(gender == (byte)1) {
                this.subNames[i] = TaskManager.gI().subnameTASK1[this.taskId][i];
                this.contentInfo[i] = TaskManager.gI().contentInfoTASK1[this.taskId][i];
                this.subtasks[i] = TaskManager.gI().npcTASK1[this.taskId][i];
                this.mapTasks[i] = TaskManager.gI().mapTASK1[this.taskId][i];
            } else {
                this.subNames[i] = TaskManager.gI().subnameTASK2[this.taskId][i];
                this.contentInfo[i] = TaskManager.gI().contentInfoTASK2[this.taskId][i];
                this.subtasks[i] = TaskManager.gI().npcTASK2[this.taskId][i];
                this.mapTasks[i] = TaskManager.gI().mapTASK2[this.taskId][i];
            }
            this.counts[i] = _task.counts[i];
        }
    }
}
