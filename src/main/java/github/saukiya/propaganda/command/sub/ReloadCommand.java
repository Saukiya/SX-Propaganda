package github.saukiya.propaganda.command.sub;

import github.saukiya.propaganda.Propaganda;
import github.saukiya.propaganda.command.SubCommand;
import github.saukiya.propaganda.util.Config;
import github.saukiya.propaganda.util.Message;
import org.bukkit.command.CommandSender;

public class ReloadCommand extends SubCommand {
    public ReloadCommand() {
        super("reload");
    }

    public void onCommand(CommandSender sender, String[] args) {
        Config.loadConfig();
        Message.loadMessage();
        Propaganda.getDataManager().loadData();
        sender.sendMessage(Message.getMsg(Message.ADMIN__PLUGIN_RELOAD));
    }
}
