package com.github.teraprath.region.command;

import com.github.teraprath.region.api.Region;
import com.github.teraprath.region.manager.SelectionManager;
import com.github.teraprath.region.plugin.RegionPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class RegionCommand implements CommandExecutor, TabCompleter {

    private final RegionPlugin plugin;

    public RegionCommand(RegionPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 1) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                String subCommand = args[0];
                String arg = args[1];
                switch (subCommand) {
                    case "set":
                        if (arg.equals("pos1")) {
                            setPosition(player, 1);
                        } else if (arg.equals("pos2")) {
                            setPosition(player, 2);
                        } else {
                            showHelp(sender);
                        }
                        break;
                    case "save":
                        SelectionManager selection = plugin.getSelection(player.getUniqueId());
                        Region region = new Region(arg, selection.getPos1(), selection.getPos2());
                        plugin.getAPI().addRegion(region);
                        player.sendMessage("Region '" + arg + "' has been saved.");
                        break;
                    case "delete":
                        if (plugin.getAPI().getRegion(arg) != null) {
                            plugin.getAPI().deleteRegion(arg);
                            player.sendMessage("Region '" + arg + "' deleted.");
                        } else {
                            player.sendMessage("§cRegion '" + arg + "' does not exists!");
                        }
                        break;
                    default:
                        showHelp(sender);
                        break;
                }
            } else {
                sender.sendMessage("You have to run this command as player.");
            }
        } else if (args.length > 0 && args[0].equals("list")) {
            sender.sendMessage("");
            sender.sendMessage("Regions");
            plugin.getAPI().getRegions().forEach(region -> {
                sender.sendMessage("    - " + region.getName());
            });
            sender.sendMessage("");
        } else {
            showHelp(sender);
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        ArrayList<String> list = new ArrayList<>();
        String current = args[args.length - 1].toLowerCase();

        switch (args.length) {
            case 1:
                list.add("set");
                list.add("save");
                list.add("delete");
                list.add("list");
                break;
            case 2:
                switch (args[0]) {
                    case "set":
                        list.add("pos1");
                        list.add("pos2");
                        break;
                    case "save":
                        list.add("<name>");
                        plugin.getAPI().getRegions().forEach(region -> {
                            list.add(region.getName());
                        });
                        break;
                    case "delete":
                        plugin.getAPI().getRegions().forEach(region -> {
                            list.add(region.getName());
                        });
                        break;
                    default:
                        break;
                }
            default:
                break;
        }

        list.removeIf(s -> !s.toLowerCase().startsWith(current));
        return list;
    }

    private void showHelp(CommandSender sender) {
        sender.sendMessage("");
        sender.sendMessage("/region");
        sender.sendMessage("    set pos1|pos2 §7§o(Set region positions)");
        sender.sendMessage("    save <Name> §7§o(Save a selection as region)");
        sender.sendMessage("    delete <Name> §7§o(Remove a region)");
        sender.sendMessage("    list §7§o(Shows all regions)");
        sender.sendMessage("");
    }

    private void setPosition(Player player, int position) {
        if (position == 1) {
            plugin.getSelection(player.getUniqueId()).setPos1(player.getLocation());
        } else {
            plugin.getSelection(player.getUniqueId()).setPos2(player.getLocation());
        }
        player.sendMessage("Position " + position + " has been set.");
    }

}
