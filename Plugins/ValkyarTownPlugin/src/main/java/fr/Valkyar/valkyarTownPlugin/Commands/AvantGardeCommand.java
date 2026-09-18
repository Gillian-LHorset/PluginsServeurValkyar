package fr.Valkyar.valkyarTownPlugin.Commands;

import fr.Valkyar.valkyarTownPlugin.ValkyarTownPlugin;
import org.bukkit.entity.Player;

public class AvantGardeCommand {

    int AvantGardePrice = 500;

    public AvantGardeCommand(ValkyarTownPlugin plugin, Player player, String[] args) {

        if (args[0].equalsIgnoreCase("spawn")) {

        } else if (args[0].equalsIgnoreCase("create")) {

        } else if (args[0].equalsIgnoreCase("destroy")) {

        } else if (args[0].equalsIgnoreCase("claim")) {

        } else if (args[0].equalsIgnoreCase("set-spawn")) {


        }
    }

    public boolean HasTownAAvantGarde() {
        return false;
    }
}
