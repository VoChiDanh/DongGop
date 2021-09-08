package net.danh.donggop;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class FundGui {
    Main plugin = (Main)Main.getPlugin(Main.class);

    FileConfiguration config;

    FileConfiguration fStore;

    Inventory inv;

    List<String> desc = new ArrayList<>();

    ItemMeta cItemMeta;

    ItemStack cItem;

    ItemStack pane;

    int slot;

    int price;

    String section;

    String command;

    String item;

    String itemName;

    public void createGui(Player p) {
        this.config = this.plugin.getConfig();
        this.fStore = Main.fStore;
        this.inv = Bukkit.createInventory((InventoryHolder)p, this.config.getInt("slots"), ChatColor.translateAlternateColorCodes('&', this.config.getString("invtitle")));
        for (String cmd : this.plugin.getConfig().getConfigurationSection("guisettings").getKeys(false)) {
            this.slot = this.config.getInt("guisettings." + cmd + ".slot") - 1;
            this.cItem = new ItemStack(Material.valueOf(this.config.getString("guisettings." + cmd + ".item").toUpperCase()));
            this.cItemMeta = this.cItem.getItemMeta();
            this.item = this.config.getString("guisettings." + cmd + ".item").toUpperCase();
            this.cItemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', this.config.getString("guisettings." + cmd + ".name")));
            this.itemName = this.config.getString("guisettings." + cmd + ".name");
            this.desc.add("§bCost: §a$" + this.config.getInt("guisettings." + cmd + ".price"));
            this.desc.add("§bGoal: §a$" + this.config.getInt("guisettings." + cmd + ".goal"));
            this.desc.add("§bTotal Raised: §a$" + this.fStore.getInt(this.itemName + ".totalfunds"));
            this.cItemMeta.setLore(this.desc);
            this.cItem.setItemMeta(this.cItemMeta);
            this.desc.clear();
            this.inv.setItem(this.slot, this.cItem);
        }
    }
}
