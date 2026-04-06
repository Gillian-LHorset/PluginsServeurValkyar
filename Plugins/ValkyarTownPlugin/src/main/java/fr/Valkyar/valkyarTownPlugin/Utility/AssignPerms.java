package fr.Valkyar.valkyarTownPlugin.Utility;

import fr.Valkyar.databaseCommunicationAPI.db.Database;
import fr.Valkyar.valkyarTownPlugin.ValkyarTownPlugin;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class AssignPerms {
    public static void assignPerms(Player p, Database db, ValkyarTownPlugin _plugin) {
        try {

            Map<String, Boolean> permissionsMap = db.getAllPermissionOfAPlayer(p);

            System.out.println(permissionsMap);

            if (permissionsMap == null) {
                db.registerPermissionPlayer(p);
                permissionsMap = db.getAllPermissionOfAPlayer(p);
            }

            for (PermissionAttachmentInfo perm : new ArrayList<>(p.getEffectivePermissions())) {

                PermissionAttachment oldAttachment = perm.getAttachment();

                if (oldAttachment != null && oldAttachment.toString().startsWith("town.") && _plugin.equals(oldAttachment.getPlugin())) {

                    try {
                        p.removeAttachment(oldAttachment);
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }

            if (permissionsMap != null) {
                PermissionAttachment attachment = p.addAttachment(_plugin);

                for (Map.Entry<String, Boolean> entry : permissionsMap.entrySet()) {
                    attachment.setPermission(("town." + entry.getKey().toString()), entry.getValue());
                    System.out.println(entry.getKey() + ": " + entry.getValue());
                }
            }

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

}
