package com.unclecole.coleclaimwithdraw.Listeners;

import com.unclecole.coleclaimwithdraw.ColeClaimWithdraw;
import com.unclecole.coleclaimwithdraw.utils.PlaceHolder;
import com.unclecole.coleclaimwithdraw.utils.TL;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class RightClickListener implements Listener {

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        if(!event.getPlayer().getInventory().getItemInMainHand().getType().equals(ColeClaimWithdraw.getInstance().getNote().getType())) return;
        if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
            ItemMeta itemMeta = item.getItemMeta();
            PersistentDataContainer container = itemMeta.getPersistentDataContainer();
            if(container.has(ColeClaimWithdraw.getInstance().getNoteKey(), PersistentDataType.INTEGER)) {
                int foundValue = container.get(ColeClaimWithdraw.getInstance().getNoteKey(), PersistentDataType.INTEGER);

                int current = ColeClaimWithdraw.getInstance().getApi().dataStore.getPlayerData(event.getPlayer().getUniqueId()).getBonusClaimBlocks();
                ColeClaimWithdraw.getInstance().getApi().dataStore.getPlayerData(event.getPlayer().getUniqueId()).setBonusClaimBlocks(current + foundValue);
                item.setAmount(item.getAmount()-1);
                TL.SUCCESSFULLY_DEPOSITED_CLAIMS.send(event.getPlayer(), new PlaceHolder("%amount%", foundValue));
            }

        }
    }
}
