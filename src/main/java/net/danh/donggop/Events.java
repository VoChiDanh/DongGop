package net.danh.donggop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.black_ixx.playerpoints.PlayerPoints;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;

public class Events implements Listener {
    Main plugin = (Main)Main.getPlugin(Main.class);

    FileConfiguration config = this.plugin.getConfig();

    FileConfiguration fStore = Main.fStore;

    int price;

    int goal;

    int rInt;

    Random rNum;

    String itemName;

    String item;

    String rName;

    String msg;

    List<String> commands;

    ItemStack currentItem;

    Player lastplayer;

    Player p;

    ArrayList<String> playerList = new ArrayList<>();

    @EventHandler
    public void onInventClick(InventoryClickEvent e) {
        this.p = (Player)e.getWhoClicked();
        this.currentItem = e.getCurrentItem();
        if (e.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&', this.config.getString("invtitle")))) {
            if (e.getClickedInventory().getItem(e.getSlot()) == null)
                return;
            for (String citem : this.config.getConfigurationSection("guisettings").getKeys(false)) {
                this.itemName = this.config.getString("guisettings." + citem + ".name");
                this.item = this.config.getString("guisettings." + citem + ".item").toUpperCase();
                this.lastplayer = (Player)e.getWhoClicked();
                if (this.currentItem.getType().equals(Material.valueOf(this.item))) {
                    this.fStore = Main.fStore;
                    this.config = this.plugin.getConfig();
                    this.price = this.config.getInt("guisettings." + citem + ".price ");
                    this.goal = this.config.getInt("guisettings." + citem + ".goal");
                    this.commands = this.config.getStringList("guisettings." + citem + ".commands");
                    if (!this.fStore.contains(String.valueOf(this.itemName) + ".players." + this.lastplayer.getUniqueId()))
                        this.fStore.set(String.valueOf(this.itemName) + ".players." + this.lastplayer.getUniqueId() + ".uses", Integer.valueOf(0));
                    if (this.config.getInt("guisettings." + citem + ".uselimit") != this.fStore.getInt(String.valueOf(this.itemName) + ".players." + this.lastplayer.getUniqueId() + ".uses")) {
                        if (Main.econ.has((OfflinePlayer) e.getWhoClicked(), this.config.getInt("guisettings." + citem + ".price "))) {
                            if (!this.fStore.contains(this.itemName))
                                this.fStore.set(String.valueOf(this.itemName) + ".totalfunds", Integer.valueOf(0));
                            this.fStore.set(String.valueOf(this.itemName) + ".totalfunds", Integer.valueOf(this.fStore.getInt(String.valueOf(this.itemName) + ".totalfunds") + this.price ));
                            this.fStore.set(String.valueOf(this.itemName) + ".players." + this.lastplayer.getUniqueId() + ".uses", Integer.valueOf(this.fStore.getInt(String.valueOf(this.itemName) + ".players." + this.lastplayer.getUniqueId() + ".uses") + 1));
                            Main.econ.withdrawPlayer((OfflinePlayer) e.getWhoClicked(), this.price );
                            e.setCancelled(true);
                            try {
                                this.fStore.save(Main.storage);
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            if (this.goal <= this.fStore.getInt(String.valueOf(this.itemName) + ".totalfunds")) {
                                for (String cmd : this.commands) {
                                    if (cmd.contains("@a")) {
                                        for (Player pl : Bukkit.getOnlinePlayers())
                                            Bukkit.dispatchCommand((CommandSender)Bukkit.getServer().getConsoleSender(), cmd.replaceFirst("@a", pl.getName()));
                                        continue;
                                    }
                                    if (cmd.contains("@l")) {
                                        this.lastplayer = (Player)e.getWhoClicked();
                                        Bukkit.dispatchCommand((CommandSender)Bukkit.getServer().getConsoleSender(), cmd.replaceFirst("@l", this.lastplayer.getName()));
                                        continue;
                                    }
                                    if (cmd.contains("@r")) {
                                        for (Player pl : Bukkit.getOnlinePlayers())
                                            this.playerList.add(pl.getName());
                                        Player rPlayer = (Player)Bukkit.getOnlinePlayers().toArray()[(new Random()).nextInt(Bukkit.getOnlinePlayers().size())];
                                        this.rName = rPlayer.getName();
                                        Bukkit.dispatchCommand((CommandSender)Bukkit.getServer().getConsoleSender(), cmd.replaceFirst("@r", this.rName));
                                        this.playerList.clear();
                                        continue;
                                    }
                                    Bukkit.dispatchCommand((CommandSender)Bukkit.getServer().getConsoleSender(), cmd);
                                }
                                this.msg = this.config.getString("guisettings." + citem + ".message");
                                if (this.msg.contains("@l")) {
                                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', this.msg.replaceFirst("@l", this.lastplayer.getName())));
                                } else if (this.msg.contains("@r")) {
                                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', this.msg.replaceFirst("@r", this.rName)));
                                } else {
                                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', this.msg));
                                }
                                this.fStore.set(String.valueOf(this.itemName) + ".totalfunds", Integer.valueOf(0));
                                for (String k : this.fStore.getConfigurationSection(String.valueOf(this.itemName) + ".players").getKeys(false))
                                    this.fStore.set(String.valueOf(this.itemName) + ".players." + k + ".uses", Integer.valueOf(0));
                                try {
                                    this.fStore.save(Main.storage);
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                            }
                            continue;
                        }
                        this.p.closeInventory();
                        this.p.sendMessage(ChatColor.RED + "Non-sufficient funds.");
                        continue;
                    }
                    this.p.closeInventory();
                    this.p.sendMessage(ChatColor.RED + "You cannot fund on this until funding has been reached");
                }
            }
            if (e.getCurrentItem().hasItemMeta())
                e.setCancelled(true);
        } else {
            return;
        }
    }
}
