package me.jesus997.ClimbUp;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import me.jesus997.ClimbUp.Constantes.Estado;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.potion.PotionEffect;

public class ArenaManager {

	private List<String> pages;

	private HashMap<Integer, Arena> arenas = new HashMap<Integer, Arena>();
        //List<Location> locs = new ArrayList<Location>();
	private int MAXPLAYER = 1;

	private CUMain plugin;
        
        private boolean INICIO_AUTOMATICO=true;

	public ArenaManager(CUMain plugin) {
		this.plugin = plugin;
		pages = plugin.getConfig().getStringList("BookPages");
	}

	public ArenaManager() {

	}

	/*
	 * public static ArenaManager getManager(){ return am; }
	 */
        
        public void cerrarArenas(){
            for(Arena a : arenas.values()){
                cerrarArena(a);
            }
        }
        
        public void cerrarArena(Arena a){
            a.finalizar();
        }

	public Arena getArena(int i) {
		return arenas.get(i);
	}

	public void createConfig() {
		plugin.getConfig().set("BookName", "NORMAS");
		plugin.getConfig().set("AuthorName", "killercreeper_55");
		plugin.getConfig().set("BookPages", pages);
		pages.add("                   --------------------------------------          CLIMB                         UP                      @@@@@@@@@@@@@@@@");
		pages.add("Bienvenido al minijuego Climb Up creado por Killercreeper55 y tatanpoker09                                     NORMAS:                       * Maximo 8 jugadores *El objetivo es subir la escalera en el centro de la zona.");
		pages.add("* 15 rondas.           *Las tres ultimas son las mas importantes: Triunfo de hierro, oro y diamante.              *En cada ronda tendras armas con encantamientos distintos.             *Una vez acabadas las 15 rondas, el juego se terminara.");
		pages.add("                                                                                       DISFRUTA! :)                                                                                     @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		plugin.saveConfig();
	}

	public void addPlayer(Player p, int i) {
		Arena a = getArena(i);
		if (a == null) {
			p.sendMessage("Arena invalida!");
			return;
		}
		if(!a.funcional()){
			p.sendMessage("Arena no funcional!");
			return;
		}
		if (!a.isFull()) {
			if (!a.isInGame()) {
                                if(a.getEstado()!=Estado.EsperandoJugadores){
                                    Jugador j = new Jugador(p, a);

                                    a.addJugador(p.getName(), j);

                                    p.getInventory().setArmorContents(null);
                                    p.getInventory().clear();

                                    p.setLevel(0);
                                    p.setExp(0);

                                    ItemStack bookItem = new ItemStack(Material.WRITTEN_BOOK, 1);
                                    BookMeta metaData = (BookMeta) bookItem.getItemMeta();
                                    metaData.setTitle(ChatColor.DARK_GREEN
                                                    + plugin.getConfig().getString("BookName"));
                                    metaData.setAuthor(ChatColor.DARK_GRAY
                                                    + plugin.getConfig().getString("AuthorName"));
                                    for (String page : pages) {
                                            metaData.addPage(page.replaceAll("(&([a-f0-9]))",
                                                            "\u00A7$2"));
                                    }
                                    bookItem.setItemMeta(metaData);
                                    Inventory in = p.getInventory();
                                    in.setItem(8, bookItem);

                                    p.teleport(j.gethome());
                                    p.setGameMode(GameMode.SURVIVAL);

                                    int playersLeft = a.getMaxPlayers() - a.size();
                                    a.sendMessage(ChatColor.BLUE + p.getName()
                                                    + " ha entrado a la arena!, Necesitamos " + playersLeft
                                                    + " jugadores para comenzar el juego!");

                                    if (playersLeft == 0) {
                                            startArena(i);
                                    }                                    
                                }
			} else {
				p.sendMessage("Lo sentimos, la arena ya ha comenzado :(");
			}
		} else {
			p.sendMessage("Lo sentimos, la arena ya esta llena :(");
		}
	}

	public Jugador getJugador(Player p) {
		Jugador j = null;

		for (Arena arena : arenas.values()) {
			if ((j = arena.getJugador(p.getName())) != null) {
				return j;
			}
		}
		return j;
	}

	@SuppressWarnings("deprecation")
	public void removePlayer(Player p) {
		Jugador j = getJugador(p);
		if (j == null) {
			p.sendMessage("No estas en ninguna arena!");
			return;
		}
		Arena a = j.getArena();
		p.setGameMode(GameMode.getByValue(j.getGamemode()));
		for (PotionEffect effect : p.getActivePotionEffects()) {
			p.removePotionEffect(effect.getType());
		}

		p.getInventory().clear();
		p.getInventory().setArmorContents(null);

		p.getInventory().setContents(j.getInventory());
		p.getInventory().setArmorContents(j.getArmor());

		p.setExp(j.getExp());
		p.setLevel(j.getLevel());

		p.teleport(j.gethome());

		p.setFireTicks(0);
                
                a.removeJugador(j);

		a.sendMessage(ChatColor.BLUE + p.getName()
				+ " ha salido de la arena!, aún quedan " + a.size()
				+ " jugadores dentro del juego!");

		p.sendMessage("Has salido de la arena!");

	}

	public void startArena(int arenaId) {
		if (getArena(arenaId) != null) {
			Arena a = getArena(arenaId);
			a.sendMessage(ChatColor.GOLD + "La arena ha comenzado!");
			a.setEstado(Estado.EnCurso);

		}
	}
        
	private Arena createArena(int id){
		int maxPlayers = plugin.getConfig().getInt(
				"Arenas." + id + "MaxPlayers");
		if (maxPlayers == 0) {
			maxPlayers = MAXPLAYER;
		}
		plugin.getConfig().set("Arenas." + id + ".MaxPlayers", maxPlayers);
		Arena a=new Arena(id, maxPlayers);
		arenas.put(id, a);
		return a;
	}
	public Arena createMeta(Location l, int id) {

		Arena a = arenas.get(id);
		if (a == null) {
			a=createArena(id);
		}
		a.setMeta(l);

		plugin.getConfig().set("Arenas." + id + ".meta", serializeLoc(l));
		plugin.saveConfig();
		return a;
	}

	public Arena createLobby(Location l, int id, int MaxPlayer) {

		Arena a = arenas.get(id);
		if (a == null) {
			a=createArena(id);
		}
		a.setLobby(l);
                a.setMaxPlayers(MaxPlayer);
                MAXPLAYER = a.getMaxPlayers();

		plugin.getConfig().set("Arenas." + id + ".lobby", serializeLoc(l));
		plugin.saveConfig();
		return a;
	}

	public Arena createRespawn(Location l, int id, Player p) {

		Arena a = arenas.get(id);
		if (a == null) {
			a=createArena(id);
		}
		a.addSpawn(l);

		plugin.getConfig().set("Arenas." + id + ".respawns." + a.spawnSize(),
				serializeLoc(l));
		plugin.saveConfig(); 
                p.sendMessage("Respawn " + a.spawnSize() + " creado para la arena " + id);
		return a;
	}

	public Arena reloadArena(int i, ConfigurationSection section) {

		Arena a =createArena(i);

		List<String> spawns = section.getStringList("respawns");
		if (spawns != null) {
			Iterator<String> it = spawns.iterator();
			while (it.hasNext()) {
				a.addSpawn(deserializeloc(it.next()));
			}
		}
		arenas.put(i, a);
		return a;
	}

	public void removeArena(int i, Player p) {
		Arena a = getArena(i);
		if (a == null) {
			return;
		}
		arenas.remove(a);

		plugin.getConfig().set("Arenas." + i, null);
		plugin.saveConfig();
		plugin.reloadConfig();
		p.sendMessage("Arena eliminada!");
	}

	public boolean isInGame(Player p) {
		for (Arena a : arenas.values()) {
			if (a.getJugador(p.getName()) != null) {
				return true;
			}
		}
		return false;
	}

	public void loadGame() {
		ConfigurationSection section = plugin.getConfig();
		Iterator<String> it = section.getKeys(false).iterator();
		while (it.hasNext()) {
			String next = it.next();
			if (plugin.getConfig().isConfigurationSection(next)) {
				int id = Auxiliar.getNatural(next, -1);
				if (id > -1) {
					Arena a = reloadArena(id,
					section.getConfigurationSection(next));
					arenas.put(id, a);
                                        if(INICIO_AUTOMATICO){a.iniciar();}
				}
			}
		}

	}

	public String serializeLoc(Location l) {
		return l.getWorld().getName() + "," + l.getBlockX() + ","
				+ l.getBlockY() + "," + l.getBlockZ();
	}

	public Location deserializeloc(String s) {
		String[] st = s.split(",");
		return new Location(Bukkit.getWorld(st[0]), Integer.parseInt(st[1]),
				Integer.parseInt(st[2]), Integer.parseInt(st[3]));
	}

}
