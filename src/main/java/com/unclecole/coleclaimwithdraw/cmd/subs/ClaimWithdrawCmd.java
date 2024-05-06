package com.unclecole.coleclaimwithdraw.cmd.subs;

import com.unclecole.coleclaimwithdraw.ColeClaimWithdraw;
import com.unclecole.coleclaimwithdraw.cmd.AbstractCommand;
import com.unclecole.coleclaimwithdraw.utils.C;
import com.unclecole.coleclaimwithdraw.utils.PlaceHolder;
import com.unclecole.coleclaimwithdraw.utils.TL;
import jdk.dynalink.NamedOperation;
import me.ryanhamshire.GriefPrevention.DataStore;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import me.ryanhamshire.GriefPrevention.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import redempt.redlib.itemutils.ItemBuilder;

public class ClaimWithdrawCmd extends AbstractCommand {

    ItemBuilder note;

    public ClaimWithdrawCmd() {
        super("claimwithdraw", false);
        note = ColeClaimWithdraw.getInstance().getNote();
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {

        Player player = (Player) sender;

        if(args.length < 1) {
            TL.INVALID_COMMAND_USAGE.send(player, new PlaceHolder("<command>", "/claimwithdraw <amount>"));
            return false;
        }

        PlayerData data = ColeClaimWithdraw.getInstance().getApi().dataStore.getPlayerData(((Player) sender).getUniqueId());


        if(!isInteger(args[0])) {
            TL.INVALID_COMMAND_USAGE.send(player, new PlaceHolder("<command>", "/claimwithdraw <amount>"));
            return false;
        }
        int amount = Integer.parseInt(args[0]);

        if(amount > data.getRemainingClaimBlocks()) {
            TL.NOT_ENOUGH_CLAIMS.send(player, new PlaceHolder("%total%", data.getRemainingClaimBlocks() - GriefPrevention.instance.dataStore.getGroupBonusBlocks(player.getUniqueId())));
            return false;
        }

        if(player.getInventory().firstEmpty() == -1) {
            TL.INVENTORY_FULL.send(player);
            return false;

        }

        note.setName(C.color(ColeClaimWithdraw.getInstance().getNoteName().replaceAll("%amount%", String.valueOf(amount))));

        ItemMeta noteMeta = note.getItemMeta();

        noteMeta.setLore(C.color(ColeClaimWithdraw.getInstance().getNoteLore(), new PlaceHolder("%amount%", amount), new PlaceHolder("%player%", player.getName())));

        noteMeta.getPersistentDataContainer().set(ColeClaimWithdraw.getInstance().getNoteKey(), PersistentDataType.INTEGER, amount);
        TL.SUCCESSFULLY_WITHDRAWED_CLAIMS.send(player, new PlaceHolder("%amount%", amount));

        if(data.getBonusClaimBlocks() < amount) {
            amount = amount - data.getBonusClaimBlocks();
            data.setBonusClaimBlocks(0);
            data.setAccruedClaimBlocks(data.getAccruedClaimBlocks() - amount);
        } else {
            data.setBonusClaimBlocks(data.getBonusClaimBlocks() - amount);
        }

        note.setItemMeta(noteMeta);
        player.getInventory().addItem(note);
        return false;
    }

    public boolean isInteger(String string) {
        try {
            Integer.valueOf(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public String getDescription() {
        return "Withdraw Command";
    }

    @Override
    public String getPermission() {
        return "claimwithdraw.withdraw";
    }
}
