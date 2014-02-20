package me.jesus997.ClimbUp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.meta.FireworkMeta;

public class GameListener implements Listener{
    static List<String> players = new ArrayList<String>();
    static CUMain plugin;
    public GameListener(CUMain plugin){
        GameListener.plugin = plugin;
    }
    
    // No tieren las cosas al morir
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e){
        if(ArenaManager.getManager().isInGame(e.getEntity())){
            e.getDrops().clear();
        }           
    }
    
    // Evitar que rompan bloques
    @EventHandler
    public void BlockBreakEvent(BlockBreakEvent e){
        if(ArenaManager.getManager().isInGame(e.getPlayer())){
            e.setCancelled(true);
        }
    }
    
    // Evitar que pungan bloques
    @EventHandler
    public void BlockPlaceEvent(BlockPlaceEvent e){
        if(ArenaManager.getManager().isInGame(e.getPlayer())){
            e.setCancelled(true);
        }
    }
    
    // Sacarlos de la arena al desconectarse del servidor
    @EventHandler
    public void onLeavePlayerServer(PlayerQuitEvent e){
        if(ArenaManager.getManager().isInGame(e.getPlayer())){
            ArenaManager.getManager().removePlayer(e.getPlayer());
        }
    }
    
    // No permitir que usen comandos dentro de la arena
    @EventHandler
    public void onPlayerCommandBlock(PlayerCommandPreprocessEvent e){
        if(ArenaManager.getManager().isInGame(e.getPlayer())){
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
    
    // creación de la cupula del jugador y fuegos artificiales
        @EventHandler
     public void onPlayerMove(PlayerMoveEvent e){
            if(ArenaManager.getManager().isInGame(e.getPlayer())){
                        Player player = e.getPlayer();
                        Location loc = player.getLocation();
                        double locX = Math.floor(loc.getBlockX());
                        double locY = loc.getBlockY();
                        double locZ = Math.floor(loc.getBlockZ());
                        World mundo = Bukkit.getWorld(loc.getWorld().getName());
                        Arena a = null;
                        for(Arena arena : ArenaManager.getManager().arenas){
                            if(arena.getPlayers().contains(e.getPlayer().getName())){
                                a = arena;
                            }
                        }
                        int id = a.getId();
                        String s = plugin.getConfig().getString("Arenas." + id + "meta");
                        Location locP = ArenaManager.getManager().deserializeloc(s);
                        //double X = getConfig().getDouble("arena1.meta.x");
                        //double Y = getConfig().getDouble("arena1.meta.y");
                        //double Z = getConfig().getDouble("arena1.meta.z");
                        Location locp2 = new Location(mundo,locX, locY, locZ);
                        if(locP == locp2){
                        //if(locX == X && locY == Y && locZ == Z ){
                            final World world = player.getWorld();
                            final int x = (int)locP.getBlockX();
                            final int y = (int)locP.getBlockY();
                            final int z = (int)locP.getBlockZ();


                            world.getBlockAt(x,y+2,z).setType(Material.GLASS); // Bloque encima del jugador
                            world.getBlockAt(x-1,y+2,z).setType(Material.GLASS); // Bloque a la derecha del bloque de arriba
                            world.getBlockAt(x+1,y+2,z).setType(Material.GLASS); // Bloque a la derecha del bloque de arriba
                            world.getBlockAt(x,y+2,z+1).setType(Material.GLASS); // Bloque que esta delante del bloque de arriba
                            world.getBlockAt(x,y+2,z-1).setType(Material.GLASS); // Bloque que esta detras del bloque de arriba

                            world.getBlockAt(x+1,y-1,z).setType(Material.GLASS); // Bloque al frente del de abajo
                            world.getBlockAt(x-1,y-1,z).setType(Material.GLASS); // Bloque tracero al de abajo
                            world.getBlockAt(x,y-1,z+1).setType(Material.GLASS); // Bloque a la derecha de abajo
                            world.getBlockAt(x,y-1,z-1).setType(Material.GLASS); // Bloque a la izquierda de abajo

                            world.getBlockAt(x+2,y,z).setType(Material.GLASS);  // Bloque 1 columna +x
                            world.getBlockAt(x+2,y+1,z).setType(Material.GLASS);  // Bloque 2 columna +x

                            world.getBlockAt(x-2,y,z).setType(Material.GLASS);  // Bloque 1 columna -x
                            world.getBlockAt(x-2,y+1,z).setType(Material.GLASS);  // Bloque 2 columna -x   

                            world.getBlockAt(x,y,z+2).setType(Material.GLASS);  // Bloque 1 columna +z
                            world.getBlockAt(x,y+1,z+2).setType(Material.GLASS);  // Bloque 2 columna +z

                            world.getBlockAt(x,y,z-2).setType(Material.GLASS);  // Bloque 1 columna -z
                            world.getBlockAt(x,y+1,z-2).setType(Material.GLASS);  // Bloque 2 columna -z  

                            world.getBlockAt(x+1,y,z+1).setType(Material.GLASS);  // Bloque 1 columna +x
                            world.getBlockAt(x+1,y+1,z+1).setType(Material.GLASS);  // Bloque 2 columna +x

                            world.getBlockAt(x-1,y,z-1).setType(Material.GLASS);  // Bloque 1 columna -x
                            world.getBlockAt(x-1,y+1,z-1).setType(Material.GLASS);  // Bloque 2 columna -x   

                            world.getBlockAt(x+1,y,z-1).setType(Material.GLASS);  // Bloque 1 columna +z
                            world.getBlockAt(x+1,y+1,z-1).setType(Material.GLASS);  // Bloque 2 columna +z

                            world.getBlockAt(x-1,y,z+1).setType(Material.GLASS);  // Bloque 1 columna -z
                            world.getBlockAt(x-1,y+1,z+1).setType(Material.GLASS);  // Bloque 2 columna -z    

                            // Fuego artificial

                             //Spawn the Firework, get the FireworkMeta.
                            Firework fw = (Firework) player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
                            FireworkMeta fwm = fw.getFireworkMeta();

                            //Our random generator
                            Random r = new Random();   

                            //Get the type
                            int rt = r.nextInt(5) + 1;
                            Type type = Type.BALL;       
                            if (rt == 1) type = Type.BALL;
                            if (rt == 2) type = Type.BALL_LARGE;
                            if (rt == 3) type = Type.BURST;
                            if (rt == 4) type = Type.CREEPER;
                            if (rt == 5) type = Type.STAR;

                            //Get our random colours   
                            int r1i = r.nextInt(17) + 1;
                            int r2i = r.nextInt(17) + 1;
                            Color c1 = getColor(r1i);
                            Color c2 = getColor(r2i);

                            //Create our effect with this
                            FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(c1).withFade(c2).with(type).trail(r.nextBoolean()).build();

                            //Then apply the effect to the meta
                            fwm.addEffect(effect);

                            //Generate some random power and set it
                            int rp = r.nextInt(1) + 1;
                            fwm.setPower(rp);

                            //Then apply this to our rocket
                            fw.setFireworkMeta(fwm);


                            player.sendMessage("Has ganado la primera ronda!");
                            player.teleport(a.spawn);
                        }
            }
    }
    
     private Color getColor(int i) {
                Color c = null;
                if(i==1){
                c=Color.AQUA;
                }
                if(i==2){
                c=Color.BLACK;
                }
                if(i==3){
                c=Color.BLUE;
                }
                if(i==4){
                c=Color.FUCHSIA;
                }
                if(i==5){
                c=Color.GRAY;
                }
                if(i==6){
                c=Color.GREEN;
                }
                if(i==7){
                c=Color.LIME;
                }
                if(i==8){
                c=Color.MAROON;
                }
                if(i==9){
                c=Color.NAVY;
                }
                if(i==10){
                c=Color.OLIVE;
                }
                if(i==11){
                c=Color.ORANGE;
                }
                if(i==12){
                c=Color.PURPLE;
                }
                if(i==13){
                c=Color.RED;
                }
                if(i==14){
                c=Color.SILVER;
                }
                if(i==15){
                c=Color.TEAL;
                }
                if(i==16){
                c=Color.WHITE;
                }
                if(i==17){
                c=Color.YELLOW;
                }

                return c;
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
