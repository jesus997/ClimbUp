package me.jesus997.ClimbUp;

import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CUMain extends JavaPlugin{
    @Override
    public void onEnable(){
        getServer().getPluginManager().registerEvents(new GameListener(this), this);
        if(!getDataFolder().exists()) 
            getDataFolder().mkdir();
        if(getConfig() == null)
            ArenaManager.getManager().createConfig();
            saveDefaultConfig();
        
        ArenaManager a = new ArenaManager(this);
        
        getLogger().info("ClimbUp activado correctemente!");
    }
    @Override
    public void onDisable(){
        saveConfig();
        getLogger().info("ClibUp desactivado correctamente!");
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(cmd.getName().equalsIgnoreCase("cu")){
            if(args[0].equalsIgnoreCase("create")){
                if(args[1].equalsIgnoreCase("lobby")){
                    Player p = (Player) sender;
                    int maxPlayer =  Integer.parseInt(args[2]);
                    ArenaManager.getManager().createLobby(p.getLocation(), maxPlayer);
                }else if(args[1].equalsIgnoreCase("meta")){
                    Player p = (Player) sender;
                    int id = Integer.parseInt(args[2]);
                    ArenaManager.getManager().createMeta(p.getLocation(), id);
                }else if(args[1].equalsIgnoreCase("respawn")){
                    Player p = (Player) sender;
                    int num = Integer.parseInt(args[2]);
                    ArenaManager.getManager().createRespawn(p.getLocation(), num);
                }
            }else if(args[0].equalsIgnoreCase("join")){
                Player p = (Player) sender;
                int id = Integer.parseInt(args[1]);
                ArenaManager.getManager().addPlayer(p, id);
            }else if(args[0].equalsIgnoreCase("leave")){
                Player p = (Player) sender;
                ArenaManager.getManager().removePlayer(p);
            }
        }
        return false;
    }
}
