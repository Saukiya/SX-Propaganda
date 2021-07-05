package github.saukiya.propaganda.command;

import github.saukiya.propaganda.Propaganda;
import github.saukiya.propaganda.command.sub.DebugCommand;
import github.saukiya.propaganda.command.sub.ReloadCommand;
import github.saukiya.propaganda.util.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class MainCommand implements CommandExecutor, TabCompleter {
    public MainCommand() {
        new DebugCommand().registerCommand();
        new ReloadCommand().registerCommand();
    }

    public void setup(String command) {
        Propaganda.getInst().getCommand(command).setExecutor(this);
        Propaganda.getInst().getCommand(command).setTabCompleter(this);
        SubCommand.commands.stream().filter((subCommand) -> subCommand instanceof Listener).forEach((subCommand) -> {
            Bukkit.getPluginManager().registerEvents((Listener) subCommand, Propaganda.getInst());
        });
        Propaganda.getInst().getLogger().info("Load " + SubCommand.commands.size() + " Commands");
    }

    private SenderType getType(CommandSender sender) {
        return sender instanceof Player ? SenderType.PLAYER : SenderType.CONSOLE;
    }

    public boolean onCommand(CommandSender sender, Command arg1, String label, String[] args) {
        SenderType type = this.getType(sender);
        if (args.length == 0) {
            sender.sendMessage("§0-§8 --§7 ---§c ----§4 -----§b " + Propaganda.getInst().getName() + "§4 -----§c ----§7 ---§8 --§0 - §0Author Saukiya");
            String color = "§7";
            for (SubCommand sub : SubCommand.commands) {
                if (sub.isUse(sender, type) && !sub.hide) {
                    color = color.length() > 0 ? "" : "§7";
                    sub.sendIntroduction(sender, color, label);
                }
            }
        } else {
            for (SubCommand sub : SubCommand.commands) {
                if (sub.cmd.equalsIgnoreCase(args[0])) {
                    if (!sub.isUse(sender, type)) {
                        sender.sendMessage(Message.getMsg(Message.ADMIN__NO_PERMISSION_CMD));
                    } else {
                        sub.onCommand(sender, args);
                    }
                    return true;
                }
            }
            sender.sendMessage(Message.getMsg(Message.ADMIN__NO_CMD, args[0]));
        }
       return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        SenderType type = this.getType(sender);
        return args.length == 1 ? SubCommand.commands.stream().filter((sub) -> sub.isUse(sender, type) && !sub.hide && sub.cmd.contains(args[0])).map((sub) -> sub.cmd).collect(Collectors.toList()) : SubCommand.commands.stream().filter((sub) -> sub.cmd.equalsIgnoreCase(args[0])).findFirst().filter((sub) -> sub.isUse(sender, type)).map((sub) -> sub.onTabComplete(sender, args)).orElse(null);
    }
}
