/*
 * MurderMystery - Find the murderer, kill him and survive!
 * Copyright (C) 2019  Plajer's Lair - maintained by Tigerpanzer_02, Plajer and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package pl.plajer.murdermystery.commands.arguments.admin.arena;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import pl.plajer.murdermystery.ConfigPreferences;
import pl.plajer.murdermystery.arena.Arena;
import pl.plajer.murdermystery.arena.ArenaManager;
import pl.plajer.murdermystery.arena.ArenaRegistry;
import pl.plajer.murdermystery.commands.arguments.ArgumentsRegistry;
import pl.plajer.murdermystery.commands.arguments.data.CommandArgument;
import pl.plajer.murdermystery.commands.arguments.data.LabelData;
import pl.plajer.murdermystery.commands.arguments.data.LabeledCommandArgument;
import pl.plajer.murdermystery.handlers.ChatManager;
import pl.plajer.murdermystery.handlers.language.LanguageManager;
import pl.plajer.murdermystery.utils.Debugger;
import pl.plajerlair.commonsbox.minecraft.serialization.InventorySerializer;

/**
 * @author Plajer
 * <p>
 * Created at 18.05.2019
 */
public class ReloadArgument {

  private Set<CommandSender> confirmations = new HashSet<>();

  public ReloadArgument(ArgumentsRegistry registry) {
    registry.mapArgument("murdermysteryadmin", new LabeledCommandArgument("reload", "murdermystery.admin.reload", CommandArgument.ExecutorType.BOTH,
      new LabelData("/mma reload", "/mma reload", "&7Reload all game arenas and configurations\n&7&lArenas will be stopped!\n&6Permission: &7murdermystery.admin.reload")) {
      @Override
      public void execute(CommandSender sender, String[] args) {
        if (!confirmations.contains(sender)) {
          confirmations.add(sender);
          Bukkit.getScheduler().runTaskLater(registry.getPlugin(), () -> confirmations.remove(sender), 20 * 10);
          sender.sendMessage(ChatManager.PLUGIN_PREFIX + ChatManager.colorRawMessage("&cAre you sure you want to do this action? Type the command again &6within 10 seconds &cto confirm!"));
          return;
        }
        confirmations.remove(sender);
        Debugger.debug(Level.INFO, "Initiated plugin reload by {0}", sender.getName());
        long start = System.currentTimeMillis();

        registry.getPlugin().reloadConfig();
        LanguageManager.reloadConfig();

        for (Arena arena : ArenaRegistry.getArenas()) {
          Debugger.debug(Level.INFO, "[Reloader] Stopping {0} instance.");
          long stopTime = System.currentTimeMillis();
          for (Player player : arena.getPlayers()) {
            arena.doBarAction(Arena.BarAction.REMOVE, player);
            arena.teleportToEndLocation(player);
            if (registry.getPlugin().getConfigPreferences().getOption(ConfigPreferences.Option.INVENTORY_MANAGER_ENABLED)) {
              InventorySerializer.loadInventory(registry.getPlugin(), player);
            } else {
              player.getInventory().clear();
              player.getInventory().setArmorContents(null);
              for (PotionEffect pe : player.getActivePotionEffects()) {
                player.removePotionEffect(pe.getType());
              }
            }
          }
          ArenaManager.stopGame(true, arena);
          Debugger.debug(Level.INFO, "[Reloader] Instance {0} stopped took {1}ms", arena.getId(), System.currentTimeMillis() - stopTime);
        }
        ArenaRegistry.registerArenas();
        sender.sendMessage(ChatManager.PLUGIN_PREFIX + ChatManager.colorMessage("Commands.Admin-Commands.Success-Reload"));
        Debugger.debug(Level.INFO, "[Reloader] Finished reloading took {0}ms", System.currentTimeMillis() - start);
      }
    });
  }

}
