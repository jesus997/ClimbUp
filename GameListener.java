package me.jesus997.ClimbUp;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GameListener implements Listener{
    static List<String> players = new ArrayList<String>();
    static CUMain plugin;
    public GameListener(CUMain plugin){
        GameListener.plugin = plugin; 
    }
    
    // No tieren las cosas al morir
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e){
        if(plugin.getManager().isInGame(e.getEntity())){
            e.getDrops().clear();
        }           
    }
    
    // Evitar que rompan bloques
    @EventHandler
    public void BlockBreakEvent(BlockBreakEvent e){
        if(plugin.getManager().isInGame(e.getPlayer())){
            e.setCancelled(true);
        }
    }
    
    // Evitar que pungan bloques
    @EventHandler
    public void BlockPlaceEvent(BlockPlaceEvent e){
        if(plugin.getManager().isInGame(e.getPlayer())){
            e.setCancelled(true);
        }
    }
    
    // Sacarlos de la arena al desconectarse del servidor
    @EventHandler
    public void onLeavePlayerServer(PlayerQuitEvent e){
        if(plugin.getManager().isInGame(e.getPlayer())){
        	plugin.getManager().removePlayer(e.getPlayer());
        }
    }
    
    // No permitir que usen comandos dentro de la arena
    @EventHandler
    public void onPlayerCommandBlock(PlayerCommandPreprocessEvent e){
        if(plugin.getManager().isInGame(e.getPlayer())){
            if(e.getPlayer().hasPermission("cu.editArena")){
                e.setCancelled(false);
            }else{
                if(!e.getMessage().contains("cu leave")){
                    e.getPlayer().sendMessage("No puedes usar comandos dentro de la arena");
                    e.setCancelled(true);
                }
            }
        }
    }
    
    // Create gamemode by ccrama
    @EventHandler
    public void onGameModeChange(PlayerGameModeChangeEvent e){
        if(plugin.getManager().isInGame(e.getPlayer())){
            Jugador j=plugin.getManager().getJugador(e.getPlayer());
            if(j==null){return;}
            Arena a = j.getArena();
            final Player p = e.getPlayer();
            if(e.getNewGameMode() == GameMode.ADVENTURE){


                  Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            p.setAllowFlight(true);
                            p.setFlying(true);     
                            p.teleport(p.getLocation().add(0,5,0));

                        }
                    }, 2L);
                for(Player pla: Bukkit.getOnlinePlayers()){
                    pla.hidePlayer(e.getPlayer());
                }

                e.getPlayer().sendMessage(ChatColor.RED + "Ahora eres un espectador!");


            } else { //will fire when player is changed to SURVIVAL or CREATIVE gamemodes
              for(Player pla:Bukkit.getOnlinePlayers()){ //reshows a player to all online players
                    pla.showPlayer(e.getPlayer());

                }
                e.getPlayer().setAllowFlight(false); //disallows flight
                e.getPlayer().setFlying(false); //makes them stop flying if they are in flight
                e.getPlayer().teleport(a.getLobby()); //teleport them to spawn if they are no longer spectator
            }
        }
    }
    
    // creación de la cupula del jugador y fuegos artificiales
        @EventHandler
     public void onPlayerMove(PlayerMoveEvent e){
            if(plugin.getManager().isInGame(e.getPlayer())){
                        final Player player = e.getPlayer();
               
                        final Jugador j=plugin.getManager().getJugador(e.getPlayer());
                        if(j==null){return;}
                        final Arena a=j.getArena();

                        Location locP = a.getMeta();

                        if(locP.distance(player.getLocation())<2){
                            Auxiliar.acristalar(player.getLocation());
                            Auxiliar.FuegosArtificiales(player.getLocation());

                            player.sendMessage("Has ganado la primera ronda!");
                            //player.teleport(a.getSpawn());       
                            
                            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
                                @Override
                                public void run(){
                                    for(Player p :Bukkit.getOnlinePlayers()){
                                            p.teleport(j.getSpawn());
                                    }
                                    Auxiliar.desAcristalar(player.getLocation());
                                    
                                }
                            }, 100L);
                        }
            }
    }
    
    public static void add(Player p){
        final String name = p.getName();
        players.add(name);
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
            @Override
            public void run(){
                players.remove(name);
            }
        }, 100L);
    }
}
