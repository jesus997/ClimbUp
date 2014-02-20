package me.jesus997.ClimbUp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.potion.PotionEffect;

public class ArenaManager {
    public Map<String, Location> locs = new HashMap<String, Location>();
    public static ArenaManager am = new ArenaManager();
    Map<String, ItemStack[]> inv = new HashMap<String, ItemStack[]>();
    Map<String, ItemStack[]> armor = new HashMap<String, ItemStack[]>();
    private Map<String, Float> exp = new HashMap<String, Float>();
    private Map<String, Integer> level = new HashMap<String, Integer>();
    private Map<String, GameMode> gm = new HashMap<String, GameMode>();
    List<String> pages = plugin.getConfig().getStringList("BookPages");
    
    List<Arena> arenas = new ArrayList<Arena>();
    List<Arena> respawns = new ArrayList<Arena>();
    List<Arena> metas = new ArrayList<Arena>();
    int arenaSize = 0;
    int respawnSize = 0;
    int metaSize = 0;
    
    static CUMain plugin;
    public ArenaManager(CUMain plugin){this.plugin = plugin;}
    
    public ArenaManager(){
        
    }
    
    public static ArenaManager getManager(){
        return am;
    }
    
    public Arena getArena(int i){
        for(Arena a : arenas){
            if(a.getId() == i){
                return a;
            }
        }
        return null;
    }
    
    public void createConfig(){
       plugin.getConfig().set("BookName", "NORMAS");
       plugin.getConfig().set("AuthorName", "killercreeper_55");
       plugin.getConfig().set("BookPages", pages);
       pages.add("                   --------------------------------------          CLIMB                         UP                      @@@@@@@@@@@@@@@@");
       pages.add("Bienvenido al minijuego Climb Up creado por Killercreeper55 y tatanpoker09                                     NORMAS:                       * Maximo 8 jugadores *El objetivo es subir la escalera en el centro de la zona.");
       pages.add("* 15 rondas.           *Las tres ultimas son las mas importantes: Triunfo de hierro, oro y diamante.              *En cada ronda tendras armas con encantamientos distintos.             *Una vez acabadas las 15 rondas, el juego se terminara.");
       pages.add("                                                                                       DISFRUTA! :)                                                                                     @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");  
       plugin.saveConfig();
    }
    
    public void addPlayer(Player p, int i){
        Arena a = getArena(i);
        if(a == null){
            p.sendMessage("Arena invalida!");
            return;
        }
        
        if(!a.isFull()){
            if(!a.isInGame()){
                a.getPlayers().add(p.getName());
                inv.put(p.getName(), p.getInventory().getContents());
                armor.put(p.getName(), p.getInventory().getArmorContents());

                p.getInventory().setArmorContents(null);
                p.getInventory().clear();

                gm.put(p.getName(), p.getGameMode());

                String p2 = p.getName();

                level.put(p2, p.getLevel());
                exp.put(p2, p.getExp());
                p.setLevel(0);
                p.setExp(0);                                                                                                                     

                ItemStack bookItem = new ItemStack(Material.WRITTEN_BOOK, 1);
                BookMeta metaData = (BookMeta) bookItem.getItemMeta();
                metaData.setTitle(ChatColor.DARK_GREEN + plugin.getConfig().getString("BookName"));
                metaData.setAuthor(ChatColor.DARK_GRAY + plugin.getConfig().getString("AuthorName"));
                for(String page : pages){
                    metaData.addPage(page.replaceAll("(&([a-f0-9]))", "\u00A7$2"));
                }
                bookItem.setItemMeta(metaData);
                Inventory in = p.getInventory();
                in.setItem(8, bookItem);

                locs.put(p.getName(), p.getLocation());
                p.teleport(a.spawn);
                p.setGameMode(GameMode.SURVIVAL);
                
                int playersLeft = a.getMaxPlayers() - a.getPlayers().size();
                a.sendMessage(ChatColor.BLUE + p.getName() + " ha entrado a la arena!, Necesitamos " + playersLeft + " para comenzar el juego!");
                
                if(playersLeft == 0){
                    startArena(i);
                }
            }else{
                p.sendMessage("Lo sentimos, la arena ya ha comenzado :(");
            }
        }else{
            p.sendMessage("Lo sentimos, la arena ya esta llena :(");
        }
    }
    
    public void removePlayer(Player p){
        Arena a = null;
        for(Arena arena : arenas){
            if(arena.getPlayers().contains(p.getName())){
                a = arena;
            }
        }
        if(a == null || !a.getPlayers().contains(p.getName())){
            p.sendMessage("No estas en ninguna arena!");
            return;
        }
        
        a.getPlayers().remove(p.getName());
        p.setGameMode(gm.get(p.getName()));
        for(PotionEffect effect : p.getActivePotionEffects()){
            p.removePotionEffect(effect.getType());
        }
        
        p.getInventory().clear();
        p.getInventory().setArmorContents(null);
         
        p.getInventory().setContents(inv.get(p.getName()));
        p.getInventory().setArmorContents(armor.get(p.getName()));
        
        p.setExp(0);
        p.setLevel(0);
        String p2 = p.getName();
        p.setExp(exp.get(p2));
        p.setLevel(level.get(p2));
        
        inv.remove(p.getName());
        armor.remove(p.getName());
        p.teleport(locs.get(p.getName()));
        locs.remove(p.getName());
        
        p.setFireTicks(0);
        
        a.sendMessage(ChatColor.BLUE + p.getName() + " ha salido de la arena!, aún quedan " + a.getPlayers().size() + " dentro del juego!");
        
        p.sendMessage("Has salido de la arena!");
    }
    
    public void startArena(int arenaId){
        if(getArena(arenaId) != null){
            Arena a = getArena(arenaId);
            a.sendMessage(ChatColor.GOLD + "La arena ha comenzado!");
            
            a.setInGame(true);
            
            for(String s : a.getPlayers()){
               this.removePlayer(Bukkit.getPlayer(s)); 
            }
        }
    }
    
    public Arena createMeta(Location l, int id){
        int i = metaSize +1;
        metaSize++;
        
        int maxPlayers = plugin.getConfig().getInt("Arenas." + id + "MaxPlayers");
        
        Arena a = new Arena(l, i);
        metas.add(a);
        
        plugin.getConfig().set("Arenas." + id + ".meta", serializeLoc(l));
        plugin.saveConfig();
        return a;
    }
    
    public Arena createLobby(Location l, int maxPlayers){
        int num = arenaSize +1;
        arenaSize++;
        
        Arena a = new Arena(l, num, maxPlayers);
        arenas.add(a);
        
        plugin.getConfig().set("Arenas." + num + ".lobby", serializeLoc(l));
        plugin.getConfig().set("Arenas." + num + ".MaxPlayers", maxPlayers);
        List<Integer> list = plugin.getConfig().getIntegerList("Arenas.Arenas");
        list.add(num);
        plugin.getConfig().set("Arenas.Arenas", list);
        plugin.saveConfig();
        return a;
    }
    
    public Arena createRespawn(Location l, int num){
        int i = respawnSize +1;
        respawnSize++;
        
        int maxPlayers = plugin.getConfig().getInt("Arenas." + num + "MaxPlayers");
        
        Arena a = new Arena(l, i, maxPlayers);
        respawns.add(a);
        
        plugin.getConfig().set("Arenas." + num + ".respawns." + i, serializeLoc(l));
        plugin.saveConfig();
        return a;
    }
    
    public Arena reloadArena(Location l){
        int num = arenaSize +1;
        arenaSize++;
        
        int maxPlayers = plugin.getConfig().getInt("Arenas." + num + "MaxPlayers");
        
        Arena a = new Arena(l, num, maxPlayers);
        arenas.add(a);
        return a;
    }
    
    public void removeArena(int i, Player p){
        Arena a = getArena(i);
        if(a == null){
            return;
        }
        arenas.remove(a);
        
        plugin.getConfig().set("Arenas." + i, null);
        List<Integer> list = plugin.getConfig().getIntegerList("Arenas.Arenas");
        list.remove(i);
        plugin.getConfig().set("Arenas.Arenas", list);
        plugin.saveConfig();
        plugin.reloadConfig();
        p.sendMessage("Arena eliminada!");
    }
    
    public boolean isInGame(Player p){
        for(Arena a : arenas){
            if(a.getPlayers().contains(p.getName())){
                return true;
            }
        }
        return false;
    }
    
    public void loadGame(){
        arenaSize = 0;
        if(plugin.getConfig().getIntegerList("Arenas.Arenas").isEmpty()){
            return;
        }
        for(int i : plugin.getConfig().getIntegerList("Arenas.Arenas")){
            Arena a = reloadArena(deserializeloc(plugin.getConfig().getString("Arenas." + i + "lobby")));
            a.id = i;
        }
    }
    
    public String serializeLoc(Location l){
        return l.getWorld().getName()+","+l.getBlockX()+","+l.getBlockY()+","+l.getBlockZ();
    }
    
    public Location deserializeloc(String s){
        String[] st = s.split(",");
        return new Location(Bukkit.getWorld(st[0]), Integer.parseInt(st[1]), Integer.parseInt(st[2]), Integer.parseInt(st[3]));
    }
    
}
