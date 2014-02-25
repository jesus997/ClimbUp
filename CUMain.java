package me.jesus997.ClimbUp;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CUMain extends JavaPlugin{
	private ArenaManager a;
        private static CUMain instance;
    @Override
    public void onEnable(){
        instance = this;
        getServer().getPluginManager().registerEvents(new GameListener(this), this);
        if(!getDataFolder().exists()) 
            getDataFolder().mkdir();
        a = new ArenaManager(this);

        //if(getConfig() == null)
            a.createConfig();
            //saveDefaultConfig();
        
        
        getLogger().info("ClimbUp activado correctemente!");
    }
    @Override
    public void onDisable(){
        saveConfig();
        getLogger().info("ClibUp desactivado correctamente!");
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
    	if(sender.getName().equalsIgnoreCase("CONSOLE")){return true;}
    	if(cmd.getName().equalsIgnoreCase("cu")){
        	if(args.length<2){
        		return true;
        	}
            if(args[0].equalsIgnoreCase("create")){
            	if(args.length<3){
            		return true;
            	}
                if(args[1].equalsIgnoreCase("lobby")){
                    if(args.length < 4 ){
                        return true;
                    }
                    Player p = (Player) sender;
                    int id =  Auxiliar.getNatural(args[2], -1);
                    int maxP = Integer.parseInt(args[3]);
                    a.createLobby(p.getLocation(), id, maxP);
                    p.sendMessage("Arena creada con id: " + id + ", para " + maxP + " jugadores.");
                }else if(args[1].equalsIgnoreCase("meta")){
                    Player p = (Player) sender;
                    int id = Auxiliar.getNatural(args[2], -1);
                    a.createMeta(p.getLocation(), id);
                    p.sendMessage("Meta creada para la arena " + id + "."); 
                }else if(args[1].equalsIgnoreCase("respawn")){
                    Player p = (Player) sender;
                    int id = Auxiliar.getNatural(args[2], -1);
                    a.createRespawn(p.getLocation(), id, p);
                }
            }else if(args[0].equalsIgnoreCase("join")){
                Player p = (Player) sender;
                int id = Auxiliar.getNatural(args[2], -1);
                a.addPlayer(p, id);
            }else if(args[0].equalsIgnoreCase("leave")){
                Player p = (Player) sender;
                if(p.getGameMode() == p.getGameMode().ADVENTURE){
                    p.setGameMode(GameMode.SURVIVAL);
                }
                a.removePlayer(p);
            }else if(args[0].equalsIgnoreCase("spectate")){
                Player p = (Player) sender;
                p.setGameMode(GameMode.ADVENTURE);
            }else if(args[0].equalsIgnoreCase("start")){
                int id = Auxiliar.getNatural(args[2], -1);
                a.startArena(id);
            }
        }
        return false;
    }
    
    public ArenaManager getManager(){return a;}
    public static CUMain getInstance(){
        return instance;
    }
}
