package nro.task;

import java.util.TimerTask;
import java.util.Timer;

import nro.main.Service;
import nro.player.Player;
import nro.player.Detu;
import nro.skill.Skill;
import nro.map.Mob;

public class PetProtectTask extends TimerTask {
    public Player player;
    public Detu pet;
    public Skill skill;
    public Timer timer;

    public PetProtectTask(Player p, Detu detu, Skill s, Timer time) {
        this.player = p;
        this.pet = detu;
        this.skill = s;
        this.timer = time;
    }
    
    @Override
    public void run() {
//        System.out.println("de tu bao ve:");
        if(pet.isdie) {
            timer.cancel();

            // creating timertask, timer
            Timer timerHs = new Timer();
            TimerTask tt = new TimerTask() {
                public void run()
                {
                    if(!pet.isdie) {
                        timerHs.cancel();
                    }
                    Service.gI().petLiveFromDead(player);
                    player.zone.PetAttack(player, pet, (byte)1);
                };
            };
            timerHs.schedule(tt, 60000);
        } else if(player.statusPet != 1) {
            timer.cancel();
        } else {
            Mob _mobAttack = player.zone.getMobPetNearest(pet);
            int damage = 0;
            if((Math.abs(pet.x - _mobAttack.pointX) <= skill.dx) && (Math.abs(pet.y - _mobAttack.pointY) <= skill.dy)) {
                //send detu den vi tri mob
                pet.x = _mobAttack.pointX;
                pet.y = _mobAttack.pointY;
                player.zone.detuMove(pet);
                //send de tu attack
                try {
                    player.zone.PetSendAttack(player, pet, _mobAttack, skill); 
                } catch (Exception e) {}

            }
        }
        
    }
}
