package me.jesus997.ClimbUp;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class Arena {
    public int id = 0;
    public Location spawn = null;
    public Location meta = null;
    List<String> players = new ArrayList<String>();
    private int MaxPlayers = 8;
    private boolean inGame = false;
    
    public Arena(Location loc, int id, int maxPlayers){
        this.spawn = loc;
        this.id = id;
        this.MaxPlayers = maxPlayers;
    }
    
    public Arena(Location loc, int id){
        this.meta = loc;
        this.id = id;
    }
    
    public int getId(){
        return this.id;
    }
    
    public List<String> getPlayers(){
        return this.players;
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
    
    public void sendMessage(String message){
        for(String s : players){
            Bukkit.getPlayer(s).sendMessage(message);
        }
    }
}
