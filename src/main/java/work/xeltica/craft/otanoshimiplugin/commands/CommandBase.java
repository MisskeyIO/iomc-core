package work.xeltica.craft.otanoshimiplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public abstract class CommandBase {
    public abstract boolean execute(CommandSender sender, Command command, String label, String[] args);
}
