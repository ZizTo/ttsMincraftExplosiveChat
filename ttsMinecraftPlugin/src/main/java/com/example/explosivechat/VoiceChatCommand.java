package com.example.explosivechat;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VoiceChatCommand implements CommandExecutor {

    // --- НАСТРОЙКИ ПЛАГИНА ---
    private final String FORBIDDEN_LETTER = "с";
    private final float EXPLOSION_POWER = 4.0f;
    private final boolean SET_FIRE = false;
    private final boolean BREAK_BLOCKS = true;
    // --- КОНЕЦ НАСТРОЕК ---

    private final ExplosiveChat plugin;

    public VoiceChatCommand(ExplosiveChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Теперь команда может приходить от консоли (через RCON), поэтому мы не проверяем, является ли sender игроком.

        // Проверяем, что у нас достаточно аргументов (хотя бы ник и одно слово)
        if (args.length < 2) {
            sender.sendMessage("Ошибка: Недостаточно аргументов. Использование: /voicechat <игрок> <сообщение>");
            return true;
        }

        // Первый аргумент - это имя игрока.
        String playerName = args[0];
        // Ищем игрока на сервере. Используем getPlayerExact для точного совпадения имени.
        Player player = Bukkit.getPlayerExact(playerName);

        // Проверяем, онлайн ли игрок.
        if (player == null) {
            sender.sendMessage("Ошибка: Игрок с ником '" + playerName + "' не найден на сервере.");
            return true;
        }

        // Все остальные аргументы - это части сообщения. Собираем их в одну строку.
        // Arrays.copyOfRange(args, 1, args.length) создает новый массив из всех элементов, начиная с индекса 1.
        String messageText = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        String messageLowerCase = messageText.toLowerCase();
        ;
        // Проверяем на наличие запретной буквы
        int index = messageLowerCase.indexOf(LetterSingleton.getInstance().getLetter());
        if (index != -1) {
            plugin.getLogger().info("Игрок " + player.getName() + " сказал запретную букву! БУМ!");
            
            int startIndex = messageLowerCase.lastIndexOf(' ', index);
            if (startIndex == -1) {
                startIndex = 0;
            } else {
                startIndex++; // Увеличиваем на 1, чтобы не включать пробел
            }

            int endIndex = messageLowerCase.indexOf(' ', index);
            if (endIndex == -1) {
                endIndex = messageLowerCase.length();
            }

            // Извлекаем слово
            String word = messageLowerCase.substring(startIndex, endIndex);
            plugin.showWord(word);

            Location loc = player.getLocation();
            // Важно: создаем взрыв в основном потоке сервера с помощью планировщика.
            Bukkit.getScheduler().runTask(plugin, () -> {
                player.getWorld().createExplosion(loc, EXPLOSION_POWER, SET_FIRE, BREAK_BLOCKS);
            });
        }

        // Отправляем сообщение в чат от имени игрока
        String chatFormat = String.format("<%s> %s", player.getName(), messageText);
        Bukkit.broadcastMessage(chatFormat);

        return true; // Команда выполнена успешно
    }
}