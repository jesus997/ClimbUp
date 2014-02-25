package me.jesus997.ClimbUp;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Queue;

import me.jesus997.ClimbUp.Constantes.Estado;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class Arena {
    private final int id;
    private Queue<Location>spawns = new LinkedList<>();
    private Location meta = null;
    private HashMap<String,Jugador> players = new HashMap<String,Jugador>();
    private int MaxPlayers = 8;
    private boolean inGame = false;
    private Location lobby;
    private Estado estado;
    private Control control;
    
    public Arena(int id, int maxPlayers){
        this.id = id;
        this.MaxPlayers = maxPlayers;
        estado = Estado.SinIniciar;
    }
    
    private void initControl(){
        control = new Control(this);
        int id= Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(CUMain.getInstance(), control, 100, 100);
        control.setId(id);
    }
    
    public Arena(Location loc, int id){
        this.setMeta(loc);
        this.id = id;
    }
    
    public int getId(){
        return this.id;
    }
    
    public int getMaxPlayers() {
        return this.MaxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.MaxPlayers = maxPlayers;
    }    
    
    public boolean isFull() { //Retorna si la arena esta llena o no
        if (players.size() >= MaxPlayers) {
            return true;
        } else {
            return false;
        }
    }
    
    public boolean isInGame() {
        return inGame;
    }
    
    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }
    
    public boolean funcional(){
        return (meta!=null&&spawns!=null&&spawns.size()>=MaxPlayers);
    }
    
    public void sendMessage(String message){
        Iterator<Entry<String, Jugador>> it=players.entrySet().iterator();
        while (it.hasNext()) {
			it.next().getValue().getPlayer().sendMessage(message);

		}
    }

	public void removeJugador(Jugador j) {
		if(players.remove(j.getPlayer().getName()) != null){ //estaba jugando
			spawns.add(j.getSpawn());				//se reupera su spawn
		}
	}

	public int  size() {
		return players.size();
	}

	public Jugador getJugador(String name) {
		return players.get(name);
	}

	public void addJugador(String name, Jugador j) {
		if(players.put(name, j) == null){ //no se habia introducido el jugador
										//y se le asocia un spawn
			j.setSpawn(spawns.poll());
		}
	}

	public Location getMeta() {
		return meta;
	}

	public void setMeta(Location meta) {
		this.meta = meta;
		if(estado==Estado.NoDisponible){
			estado=Estado.SinIniciar;
		}                
	}

	public void setLobby(Location l) {
		lobby=l;
		if(estado==Estado.NoDisponible){
			estado=Estado.SinIniciar;
		}                
	}
	public Location getLobby() {
		return lobby;
	}

	public void addSpawn(Location deserializeloc) {
		spawns.add(deserializeloc);
		if(estado==Estado.NoDisponible){
			estado=Estado.SinIniciar;
		}                
	}


	/*public Location getSpawn() {
		return spawns.get((int)Math.random()*spawns.size());
	}*/


	public int spawnSize() {
		return spawns!=null? spawns.size():0;
	}
        
	public void removeAllJugadores(){
		for(Jugador j:players.values()){
			removeJugador(j);
		}
	}

	public void finalizar() {
		estado=Estado.Finalizada;
	}


	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado e) {
		 estado=e;
	}

	public void iniciar() {
		if(funcional()){
	        initControl();
	        estado=Estado.Iniciando;
		}else{
			estado=Estado.NoDisponible;
		}
	}
}
