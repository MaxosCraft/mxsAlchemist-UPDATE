package ru.mxsserver.mxsalchemist.listeners;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import ru.mxsserver.mxsalchemist.api.events.SuccessPotionMixEvent;
import ru.mxsserver.mxsalchemist.api.events.UnsuccessfulPotionMixEvent;
import ru.mxsserver.mxsalchemist.configurations.Config;
import ru.mxsserver.mxsalchemist.configurations.Menu;
import ru.mxsserver.mxsalchemist.menus.Alchemist;
import ru.mxsserver.mxsalchemist.menus.Animation;
import ru.mxsserver.mxsalchemist.utils.ChatUtils;
import ru.mxsserver.mxsalchemist.utils.Eco;
import ru.mxsserver.mxsalchemist.utils.Storage;
import ru.mxsserver.mxsalchemist.utils.StorageUtils;

public class AlchemistClickListener implements Listener {
    @EventHandler
    public void onClickInventory(InventoryClickEvent e) {
        if (e.getView().getTitle().equals("§8" + Menu.getTitle())) {
            Player player = (Player)e.getWhoClicked();
            if (e.getClick().isKeyboardClick() && !e.getClick().isLeftClick() && !e.getClick().isRightClick() && !e.getClick().isShiftClick()) {
                e.setCancelled(true);
                return;
            }

            if (e.getClickedInventory() == null || e.getCurrentItem() == null) {
                return;
            }

            if (e.getClickedInventory() == player.getInventory() || e.getInventory() == player.getInventory()) {
                if (e.getCurrentItem().getType() == Material.POTION) {
                    PotionMeta meta = (PotionMeta)e.getCurrentItem().getItemMeta();
                    if (meta.getBasePotionData().getType().getEffectType() == null) {
                        if (Config.getConfig().getBoolean("unlimited-mix") && meta.hasCustomEffects()) {
                            for(String potion : Config.getConfig().getStringList("blocked-effects")) {
                                String[] split = potion.split(":");
                                for(PotionEffect effect : meta.getCustomEffects()) {
                                    if (effect.getType().getName().equalsIgnoreCase(split[0]) && effect.getAmplifier() >= Integer.parseInt(split[1]) - 1) {
                                        ChatUtils.sendMsg(player, Config.getConfig().getString("messages.potion-level"));
                                        e.setCancelled(true);
                                        return;
                                    }
                                }
                            }
                            return;
                        }
                        ChatUtils.sendMsg(player, Config.getConfig().getString("messages.null-potion"));
                        e.setCancelled(true);
                        return;
                    }
                } else if (e.getCurrentItem().getType() != Material.POTION && e.getCurrentItem().getType() != Material.AIR) {
                    ChatUtils.sendMsg(player, Config.getConfig().getString("messages.null-potion"));
                    e.setCancelled(true);
                    return;
                }
                return;
            }

            if (e.getSlot() == Menu.getMixedPotionSlot() && e.getInventory().getItem(Menu.getMixedPotionSlot()) != null) {
                e.setCancelled(true);
                int chance = (int)(Math.random() * 100.0);
                if (Eco.getEconomy().getBalance(player) < Menu.getMixPrice()) {
                    ChatUtils.sendMsg(player, Config.getConfig().getString("messages.no-money"));
                    return;
                }

                if (e.getInventory().getItem(Menu.getFirstPotionSlot()) == null || e.getInventory().getItem(Menu.getSecondPotionSlot()) == null) {
                    ChatUtils.sendMsg(player, Config.getConfig().getString("messages.no-potions"));
                    return;
                }

                ItemStack item1 = e.getInventory().getItem(Menu.getFirstPotionSlot());
                ItemStack item2 = e.getInventory().getItem(Menu.getSecondPotionSlot());
                PotionMeta meta1 = (PotionMeta)item1.getItemMeta();
                PotionMeta meta2 = (PotionMeta)item2.getItemMeta();
                if (meta1.getBasePotionData().getType() == PotionType.WATER || meta2.getBasePotionData().getType() == PotionType.WATER ||
                        meta1.getBasePotionData().getType() == PotionType.AWKWARD || meta2.getBasePotionData().getType() == PotionType.AWKWARD) {
                    ChatUtils.sendMsg(player, Config.getConfig().getString("messages.water-mix"));
                    return;
                }

                // Проверяем количество зелий в слотах
                int amount1 = item1.getAmount();
                int amount2 = item2.getAmount();
                int resultAmount = Math.min(amount1, amount2); // Берем минимальное количество зелий

                Eco.getEconomy().withdrawPlayer(player, Menu.getMixPrice());
                ItemStack item;
                if (chance <= Storage.chance.get(player.getUniqueId())) {
                    item = new ItemStack(Material.POTION);
                    PotionMeta meta = (PotionMeta)item.getItemMeta();
                    List<PotionType> pt = new ArrayList<>();
                    pt.add(PotionType.FIRE_RESISTANCE);
                    pt.add(PotionType.NIGHT_VISION);
                    pt.add(PotionType.INVISIBILITY);
                    pt.add(PotionType.LUCK);
                    List<PotionType> pt2 = new ArrayList<>();
                    pt2.add(PotionType.SLOW_FALLING);
                    pt2.add(PotionType.WEAKNESS);
                    List<PotionType> pt3 = new ArrayList<>();
                    pt3.add(PotionType.INSTANT_HEAL);
                    pt3.add(PotionType.INSTANT_DAMAGE);
                    List<PotionType> pt4 = new ArrayList<>();
                    pt4.add(PotionType.REGEN);
                    pt4.add(PotionType.POISON);

                    if (Config.getConfig().getBoolean("unlimited-mix") && meta1.hasCustomEffects()) {
                        for(PotionEffect effect : meta1.getCustomEffects()) {
                            meta.addCustomEffect(effect, true);
                        }
                    } else if (meta1.getBasePotionData().getType().getEffectType() != null) {
                        PotionData data = meta1.getBasePotionData();
                        PotionEffect potionEffect;
                        if (data.isUpgraded()) {
                            potionEffect = new PotionEffect(data.getType().getEffectType(), 1800, 1);
                            if (pt4.contains(data.getType())) {
                                potionEffect = new PotionEffect(data.getType().getEffectType(), 440, 1);
                            }
                        } else {
                            potionEffect = new PotionEffect(data.getType().getEffectType(), 3600, 0);
                            if (pt2.contains(data.getType())) {
                                potionEffect = new PotionEffect(data.getType().getEffectType(), 2400, 0);
                            }
                            if (pt4.contains(data.getType())) {
                                potionEffect = new PotionEffect(data.getType().getEffectType(), 900, 0);
                            }
                        }
                        if (data.isExtended()) {
                            potionEffect = new PotionEffect(data.getType().getEffectType(), 9600, 0);
                            if (pt2.contains(data.getType())) {
                                potionEffect = new PotionEffect(data.getType().getEffectType(), 4800, 0);
                            }
                            if (pt4.contains(data.getType())) {
                                potionEffect = new PotionEffect(data.getType().getEffectType(), 1800, 1);
                            }
                        }
                        if (pt3.contains(data.getType())) {
                            potionEffect = new PotionEffect(data.getType().getEffectType(), 0, data.isUpgraded() ? 1 : 0);
                        }
                        if (pt.contains(data.getType())) {
                            potionEffect = new PotionEffect(data.getType().getEffectType(), data.isExtended() ? 9600 : 3600, 0);
                        }
                        if (data.getType() == PotionType.TURTLE_MASTER) {
                            PotionEffect potionEffect1, potionEffect2;
                            if (data.isUpgraded()) {
                                potionEffect1 = new PotionEffect(PotionEffectType.SLOW, 400, 3);
                                potionEffect2 = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 400, 3);
                            } else if (data.isExtended()) {
                                potionEffect1 = new PotionEffect(PotionEffectType.SLOW, 800, 3);
                                potionEffect2 = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 800, 2);
                            } else {
                                potionEffect1 = new PotionEffect(PotionEffectType.SLOW, 400, 3);
                                potionEffect2 = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 400, 2);
                            }
                            meta.addCustomEffect(potionEffect1, true);
                            meta.addCustomEffect(potionEffect2, true);
                        } else {
                            meta.addCustomEffect(potionEffect, true);
                        }
                    }

                    if (Config.getConfig().getBoolean("unlimited-mix") && meta2.hasCustomEffects()) {
                        for(PotionEffect effect : meta2.getCustomEffects()) {
                            meta.addCustomEffect(effect, true);
                        }
                    } else if (meta2.getBasePotionData().getType().getEffectType() != null) {
                        PotionData data = meta2.getBasePotionData();
                        PotionEffect potionEffect;
                        if (data.isUpgraded()) {
                            potionEffect = new PotionEffect(data.getType().getEffectType(), 1800, 1);
                            if (pt4.contains(data.getType())) {
                                potionEffect = new PotionEffect(data.getType().getEffectType(), 440, 1);
                            }
                        } else {
                            potionEffect = new PotionEffect(data.getType().getEffectType(), 3600, 0);
                            if (pt2.contains(data.getType())) {
                                potionEffect = new PotionEffect(data.getType().getEffectType(), 2400, 0);
                            }
                            if (pt4.contains(data.getType())) {
                                potionEffect = new PotionEffect(data.getType().getEffectType(), 900, 0);
                            }
                        }
                        if (data.isExtended()) {
                            potionEffect = new PotionEffect(data.getType().getEffectType(), 9600, 0);
                            if (pt2.contains(data.getType())) {
                                potionEffect = new PotionEffect(data.getType().getEffectType(), 4800, 0);
                            }
                            if (pt4.contains(data.getType())) {
                                potionEffect = new PotionEffect(data.getType().getEffectType(), 1800, 1);
                            }
                        }
                        if (pt3.contains(data.getType())) {
                            potionEffect = new PotionEffect(data.getType().getEffectType(), 0, data.isUpgraded() ? 1 : 0);
                        }
                        if (pt.contains(data.getType())) {
                            potionEffect = new PotionEffect(data.getType().getEffectType(), data.isExtended() ? 9600 : 3600, 0);
                        }
                        if (data.getType() == PotionType.TURTLE_MASTER) {
                            PotionEffect potionEffect1, potionEffect2;
                            if (data.isUpgraded()) {
                                potionEffect1 = new PotionEffect(PotionEffectType.SLOW, 400, 3);
                                potionEffect2 = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 400, 3);
                            } else if (data.isExtended()) {
                                potionEffect1 = new PotionEffect(PotionEffectType.SLOW, 800, 3);
                                potionEffect2 = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 800, 2);
                            } else {
                                potionEffect1 = new PotionEffect(PotionEffectType.SLOW, 400, 3);
                                potionEffect2 = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 400, 2);
                            }
                            meta.addCustomEffect(potionEffect1, true);
                            meta.addCustomEffect(potionEffect2, true);
                        } else {
                            meta.addCustomEffect(potionEffect, true);
                        }
                    }

                    String rgb = Config.getConfig().getString("potion-color");
                    String[] split = rgb.split(";");
                    int r = Integer.parseInt(split[0]);
                    int g = Integer.parseInt(split[1]);
                    int b = Integer.parseInt(split[2]);
                    meta.setColor(Color.fromRGB(r, g, b));
                    meta.setDisplayName(ChatUtils.color(Config.getConfig().getString("potion-name")));
                    item.setItemMeta(meta);
                    item.setAmount(resultAmount); // Устанавливаем количество зелий
                    new SuccessPotionMixEvent(player, item, meta.getCustomEffects());
                } else {
                    item = new ItemStack(Material.BARRIER);
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName("§cНеудача :(");
                    item.setItemMeta(meta);
                    item.setAmount(resultAmount); // Устанавливаем количество барьеров
                    new UnsuccessfulPotionMixEvent(player);
                }

                // Уменьшаем количество зелий в слотах
                if (amount1 > resultAmount) {
                    item1.setAmount(amount1 - resultAmount);
                    e.getInventory().setItem(Menu.getFirstPotionSlot(), item1);
                } else {
                    e.getInventory().setItem(Menu.getFirstPotionSlot(), new ItemStack(Material.AIR));
                }
                if (amount2 > resultAmount) {
                    item2.setAmount(amount2 - resultAmount);
                    e.getInventory().setItem(Menu.getSecondPotionSlot(), item2);
                } else {
                    e.getInventory().setItem(Menu.getSecondPotionSlot(), new ItemStack(Material.AIR));
                }

                Storage.price.put(player.getUniqueId(), Menu.getChanceGiverPrice());
                Storage.chance.put(player.getUniqueId(), Menu.getStandardChance());
                new Animation(player, item).run();
            }

            for(String s : Menu.getDividerItems()) {
                String path = "divider-items." + s + ".";
                for(Integer i : Menu.getConfig().getIntegerList(path + "slots")) {
                    if (e.getSlot() == i) {
                        e.setCancelled(true);
                        return;
                    }
                }
            }

            for(Integer i : Menu.getExitSlots()) {
                if (e.getSlot() == i) {
                    e.setCancelled(true);
                    player.closeInventory();
                    return;
                }
            }

            if (Menu.isChanceGiverEnable()) {
                for(Integer i : Menu.getChanceGiverSlots()) {
                    if (e.getSlot() == i) {
                        e.setCancelled(true);
                        if (Storage.chance.get(player.getUniqueId()) >= Menu.getMaxChance()) {
                            ChatUtils.sendMsg(player, Config.getConfig().getString("messages.max-chance"));
                            return;
                        }
                        if (Eco.getEconomy().getBalance(player) < Storage.price.get(player.getUniqueId())) {
                            ChatUtils.sendMsg(player, Config.getConfig().getString("messages.no-money"));
                            return;
                        }
                        Eco.getEconomy().withdrawPlayer(player, Storage.price.get(player.getUniqueId()));
                        StorageUtils.addChance(player);
                        StorageUtils.addPrice(player);
                        Alchemist.update(player, e.getInventory());
                    }
                }
            }
        }
    }
}
