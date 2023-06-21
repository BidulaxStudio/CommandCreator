package eu.bidulman.commandcreator;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

public class MessageEvent implements Listener {

    private final CommandCreator main;

    public MessageEvent(CommandCreator main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerChat(PlayerChatEvent event) {
        String message = event.getMessage();

        if (message.startsWith("#")) {
            event.setCancelled(true);

            message = message.replace("#", "").replace(" ", "");

            if (!main.useCommand(event.getPlayer(), message)) {
                main.sendConfigMessage(event.getPlayer(), "messages.notWorked");
            }

        }
    }

}
