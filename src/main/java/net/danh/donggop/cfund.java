package net.danh.donggop;


import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class cfund implements CommandExecutor {
    FundGui gui = new FundGui();

    Main plugin = (Main)Main.getPlugin(Main.class);

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player)sender;
        if (sender instanceof Player)
            if (sender.hasPermission("cfunding.use")) {
                this.gui.createGui(p);
                p.openInventory(this.gui.inv);
            } else {
                p.sendMessage(ChatColor.RED + "You don't have permission to use this command");
            }
        return true;
    }
}
