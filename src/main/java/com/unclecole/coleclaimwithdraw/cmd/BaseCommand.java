package com.unclecole.coleclaimwithdraw.cmd;

import com.unclecole.coleclaimwithdraw.cmd.subs.ClaimWithdrawCmd;
import com.unclecole.coleclaimwithdraw.utils.TL;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.*;

public class BaseCommand implements CommandExecutor {
    private Map<Class, AbstractCommand> subcommands;

    public BaseCommand() {
        this.subcommands = new HashMap<>();

        addCommand(new ClaimWithdrawCmd());
    }

    public boolean onCommand(CommandSender s, Command cmd, String string, String[] args) {
        try {
            //if (args.length == 0) {
                AbstractCommand helpCommand = this.subcommands.get(Class.forName("com.unclecole.coleclaimwithdraw.cmd.subs.ClaimWithdrawCmd"));

                if (s.hasPermission(helpCommand.getPermission())) {
                    (this.subcommands.get(Class.forName("com.unclecole.coleclaimwithdraw.cmd.subs.ClaimWithdrawCmd"))).execute(s, args);
                    return false;
                }

                TL.NO_PERMISSION.send(s);

                return false;
            //}

            /*for (AbstractCommand abstractCommand : this.subcommands.values()) {

                if (args[0].equalsIgnoreCase(abstractCommand.getLabel()) || abstractCommand.getAlias().contains(args[0])) {
                    if (!(s instanceof Player) && abstractCommand.isPlayerRequired()) {
                        TL.PLAYER_ONLY.send(s);
                        return false;
                    }

                    if (s.hasPermission(abstractCommand.getPermission()) || s.isOp()) return abstractCommand.execute(s, Arrays.copyOfRange(args, 1, args.length));

                    TL.NO_PERMISSION.send(s);
                    return false;
                }
            }*/
            //return false;
        } catch (ClassNotFoundException | IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private void addCommand(AbstractCommand cmd) {
        this.subcommands.put(cmd.getClass(), cmd);
    }

    public Collection<AbstractCommand> getCommands() {
        return Collections.unmodifiableCollection(this.subcommands.values());
    }
}