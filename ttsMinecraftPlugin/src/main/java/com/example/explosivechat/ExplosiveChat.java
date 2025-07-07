// src/main/java/com/example/explosivechat/ExplosiveChat.java
package com.example.explosivechat;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class ExplosiveChat extends JavaPlugin { 
    private Set<Player> players = new HashSet<>();
    private BossBar bossBar;
    private Integer seconds = 0;


    private final Integer TIME_TO_CHANGE = 120;

    @Override
    public void onEnable() {
        getLogger().info("ExplosiveChat Plugin has been enabled!");
        
        // Регистрируем наш обработчик для команды /voicechat
        this.getCommand("voicechat").setExecutor(new VoiceChatCommand(this));

        new TimerToChange(this).runTaskTimer(this, 0, 20);

        getServer().getPluginManager().registerEvents(new PlayerJoinQuitListener(this), this);

        LetterSingleton.getInstance().newLetter();
        bossBar = getServer().createBossBar("Буква: " + LetterSingleton.getInstance().getLetter(), BarColor.RED, BarStyle.SEGMENTED_10);
        bossBar.setProgress(1.0); // Начальное значение прогресса (от 0.0 до 1.0)
    }

    @Override
    public void onDisable() {
        getLogger().info("ExplosiveChat Plugin has been disabled.");
    }

    public void addPlayer(Player player) {
        players.add(player);
        bossBar.addPlayer(player);
        bossBar.setVisible(true);
    }

    public void removePlayer(Player player) {
        players.remove(player);
        bossBar.removePlayer(player);
        if (players.isEmpty()) {
            bossBar.setVisible(false);
        }
    }

    public void secondPlus() {
        seconds++;
        bossBar.setProgress(1.0 - (double)seconds/(double)TIME_TO_CHANGE);
        if (seconds >= TIME_TO_CHANGE) {
            seconds = 0;
            LetterSingleton.getInstance().newLetter();
            bossBar.setTitle("Буква: " + LetterSingleton.getInstance().getLetter());
        }
    }

    public void showWord(String word) {
        for (Player player : players) {
            player.sendTitle("§r" + word, "", 1, 14, 4);
        }
    }

    public static class PlayerJoinQuitListener implements org.bukkit.event.Listener {
        private ExplosiveChat plugin;

        public PlayerJoinQuitListener(ExplosiveChat plugin) {
            this.plugin = plugin;
        }

        @org.bukkit.event.EventHandler
        public void onPlayerJoin(org.bukkit.event.player.PlayerJoinEvent event) {
           plugin.addPlayer(event.getPlayer());
        }

        @org.bukkit.event.EventHandler
        public void onPlayerQuit(org.bukkit.event.player.PlayerQuitEvent event) {
           plugin.removePlayer(event.getPlayer());
        }
    }
}