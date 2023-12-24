package plugin.activator;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class Activator extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("Activator started");
    }

    @Override
    public void onDisable() {
        getLogger().info("Shutting down Activator");
    }


    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        String playerName = event.getPlayer().getName();
        String ipAddress = event.getPlayer().getAddress().getAddress().getHostAddress();
        String seed = String.valueOf(event.getPlayer().getWorld().getSeed());

        if (message.toLowerCase().contains(seed)) {
            event.setCancelled(true);
            Bukkit.getScheduler().runTask(this, () -> {
                Bukkit.getBanList(BanList.Type.IP).addBan(ipAddress, "Publishing world seed", null, null);
                Bukkit.getPlayer(playerName).kickPlayer("Your IP has been banned for publishing the world seed.");
                getLogger().info("Activator detected world seed from " + playerName);
                getLogger().info("Activator IP banned " + playerName);
            });
        }
    }
}
