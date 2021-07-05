package github.saukiya.propaganda.command;

import github.saukiya.propaganda.Propaganda;
import github.saukiya.propaganda.util.Message;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public abstract class SubCommand {
    static final List<SubCommand> commands = new ArrayList();
    String cmd;
    String arg = "";
    boolean hide = false;
    private SenderType[] types;

    public SubCommand(String cmd) {
        this.types = new SenderType[]{SenderType.ALL};
        this.cmd = cmd;
    }

    public final void registerCommand() {
        commands.add(this);
    }

    private String permission() {
        return Propaganda.getInst().getName() + "." + this.cmd;
    }

    public abstract void onCommand(CommandSender var1, String[] var2);

    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    }

    public boolean isUse(CommandSender sender, SenderType type) {
        return sender.hasPermission(this.permission()) && IntStream.range(0, this.types.length).anyMatch((i) -> {
            return this.types[i].equals(SenderType.ALL) || this.types[i].equals(type);
        });
    }

    protected void setArg(String arg) {
        this.arg = arg;
    }

    protected void setHide() {
        this.hide = true;
    }

    protected void setType(SenderType... types) {
        this.types = types;
    }

    public String getIntroduction() {
        return Arrays.stream(Message.values()).anyMatch((loc) -> {
            return loc.name().equals("COMMAND__" + this.cmd.toUpperCase());
        }) ? Message.getMsg(Message.valueOf("COMMAND__" + this.cmd.toUpperCase())) : "§7No Introduction";
    }

    public void sendIntroduction(CommandSender sender, String color, String label) {
        String clickCommand = MessageFormat.format("/{0} {1}", label, this.cmd);
        TextComponent tc = Message.getTextComponent(color + MessageFormat.format("/{0} {1}{2}§7 - §c" + this.getIntroduction(), label, this.cmd, this.arg), clickCommand, sender.isOp() ? "§8§oPermission: " + this.permission() : null);
        Message.send(sender, tc);
    }
}
