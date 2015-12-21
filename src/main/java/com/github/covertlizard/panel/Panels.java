package com.github.covertlizard.panel;

import org.apache.commons.lang3.Validate;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * Project: Panel
 * Created: 12/19/2015
 * Time: 22:47
 * Package: com.github.covertlizard.panel.temp
 * Description: Enables layouts to have listeners
 */
@SuppressWarnings("all")
public class Panels implements Listener
{
    public static final HashMap<Inventory, Layout> LAYOUTS = new HashMap<>();
    public static WeakReference<JavaPlugin> plugin;

    /**
     * Registers the Panel listener to the plugin instance
     * @param plugin the plugin instance
     */
    public static void register(JavaPlugin plugin)
    {
        Validate.isTrue(Panels.plugin == null, "You have already registered the Panel listener.");
        Panels.plugin = new WeakReference<>(plugin);
        plugin.getServer().getPluginManager().registerEvents(new Panels(), plugin);
    }

    @EventHandler
    private void onInventoryClickEvent(InventoryClickEvent event)
    {
        if(!Panels.LAYOUTS.containsKey(event.getClickedInventory())) return;
        switch(Panels.LAYOUTS.get(event.getClickedInventory()).getState())
        {
            case DYNAMIC:
                break;
            case STATIC:
                if(Panels.LAYOUTS.get(event.getClickedInventory()).contains(event.getSlot())) Panels.LAYOUTS.get(event.getClickedInventory()).component(event.getSlot()).eventAction(event);
                event.setCancelled(true);
                break;
            default:
                if(Panels.LAYOUTS.get(event.getClickedInventory()).contains(event.getSlot())) Panels.LAYOUTS.get(event.getClickedInventory()).component(event.getSlot()).eventAction(event);
        }
    }

    @EventHandler
    private void onInventoryDragEvent(InventoryDragEvent event)
    {
        if(!Panels.LAYOUTS.containsKey(event.getInventory())) return;
        if(!Panels.LAYOUTS.get(event.getInventory()).getState().equals(Layout.State.DYNAMIC)) event.setCancelled(true);
    }
}