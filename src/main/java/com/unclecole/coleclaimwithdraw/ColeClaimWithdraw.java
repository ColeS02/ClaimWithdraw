package com.unclecole.coleclaimwithdraw;

import com.unclecole.coleclaimwithdraw.Listeners.RightClickListener;
import com.unclecole.coleclaimwithdraw.cmd.BaseCommand;
import com.unclecole.coleclaimwithdraw.utils.ConfigFile;
import com.unclecole.coleclaimwithdraw.utils.TL;
import lombok.Getter;
import lombok.Setter;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.plugin.java.JavaPlugin;
import redempt.redlib.itemutils.ItemBuilder;

import java.util.List;
import java.util.Objects;

public final class ColeClaimWithdraw extends JavaPlugin {

    @Getter private static ColeClaimWithdraw instance;
    @Getter private ItemBuilder note;
    @Getter private String noteName;
    @Getter private List<String> noteLore;
    @Getter NamespacedKey noteKey;

    @Getter GriefPrevention api;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        api = GriefPrevention.instance;
        loadConfig();
        Objects.requireNonNull(getCommand("claimwithdraw")).setExecutor(new BaseCommand());

        saveDefaultConfig();
        TL.loadMessages(new ConfigFile("messages.yml", this));

        noteKey = new NamespacedKey(this, "cole-note-key");
        Bukkit.getPluginManager().registerEvents(new RightClickListener(), this);


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void loadConfig() {
        note = new ItemBuilder(Material.getMaterial(getConfig().getString("Note.Material")), 1);
        note.addItemFlags(ItemFlag.HIDE_DYE,ItemFlag.HIDE_PLACED_ON,ItemFlag.HIDE_UNBREAKABLE,ItemFlag.HIDE_ENCHANTS,ItemFlag.HIDE_POTION_EFFECTS,ItemFlag.HIDE_ATTRIBUTES);
        if(getConfig().getInt("Note.CustomModelData") < 0) {
            note.setCustomModelData(getConfig().getInt("Note.CustomModelData"));
        }
        if(getConfig().getInt("Note.Data") < 0) {
            note.setDurability(getConfig().getInt("Note.Data"));
        }
        if(getConfig().getBoolean("Note.Enchanted")) {
            note.addEnchant(Enchantment.DIG_SPEED,1);
        }
        noteName = getConfig().getString("Note.Name");
        noteLore = getConfig().getStringList("Note.Lore");
    }


}
