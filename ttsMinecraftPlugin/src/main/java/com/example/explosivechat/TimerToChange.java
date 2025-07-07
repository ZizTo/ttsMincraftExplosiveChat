package com.example.explosivechat;

import org.bukkit.scheduler.BukkitRunnable;

public class TimerToChange extends BukkitRunnable{
    private final ExplosiveChat plugin;

    public TimerToChange(ExplosiveChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        plugin.secondPlus();
    }
}
