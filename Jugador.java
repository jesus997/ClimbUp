package me.jesus997.ClimbUp;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Jugador {
	private Player player;
	private Arena arena;
	private ItemStack[] inventory,armor;
	private int gamemode;
	private float exp;
	private Location home,spawn;
	private int puntuacion;
	private int level;

	@SuppressWarnings("deprecation")
	public Jugador(Player pl,Arena a){
		player=pl;
		arena=a;
		gamemode=pl.getGameMode().getValue();
		exp=pl.getExp();  
		level=pl.getLevel();
		home=pl.getLocation();
		inventory=pl.getInventory().getContents();
		armor=pl.getInventory().getArmorContents();
	}
	public Player getPlayer() {
		return player;
	}

	public Arena getArena() {
		return arena;
	}

	public ItemStack[] getInventory() {
		return inventory;
	}

	public ItemStack[] getArmor() {
		return armor;
	}

	public int getGamemode() {
		return gamemode;
	}
	public float getExp() {
		return exp;
	}
	public Location gethome() {
		return home;
	}
	public int getPuntuacion() {
		return puntuacion;
	}
	public void setPuntuacion(int puntuacion) {
		this.puntuacion = puntuacion;
	}
	public int getLevel() {
		return level;
	}
        
        public void setSpawn(Location poll){
            spawn = poll;
        }
        
        public Location getSpawn(){
            return spawn;
        }

}
