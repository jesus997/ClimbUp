package me.jesus997.ClimbUp;

import java.util.Random;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

public class Auxiliar {
	public static int getNatural(String s, int defaul) {
		try {
			return Integer.valueOf(s);
		} catch (Exception e) {
			return defaul;
		}
	}

	public static void acristalar(Location l){
		int x=l.getBlockX(),y=l.getBlockY(),z=l.getBlockZ();
		World world=l.getWorld();

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
	}
        
        public static void desAcristalar(Location l){
                int x=l.getBlockX(),y=l.getBlockY(),z=l.getBlockZ();
                World world=l.getWorld();
                world.getBlockAt(x,y+2,z).setType(Material.AIR); // Bloque encima del jugador
                world.getBlockAt(x-1,y+2,z).setType(Material.AIR); // Bloque a la derecha del bloque de arriba
                world.getBlockAt(x+1,y+2,z).setType(Material.AIR); // Bloque a la derecha del bloque de arriba
                world.getBlockAt(x,y+2,z+1).setType(Material.AIR); // Bloque que esta delante del bloque de arriba
                world.getBlockAt(x,y+2,z-1).setType(Material.AIR); // Bloque que esta detras del bloque de arriba

                world.getBlockAt(x+1,y-1,z).setType(Material.AIR); // Bloque al frente del de abajo
                world.getBlockAt(x-1,y-1,z).setType(Material.AIR); // Bloque tracero al de abajo
                world.getBlockAt(x,y-1,z+1).setType(Material.AIR); // Bloque a la derecha de abajo
                world.getBlockAt(x,y-1,z-1).setType(Material.AIR); // Bloque a la izquierda de abajo

                world.getBlockAt(x+2,y,z).setType(Material.AIR);  // Bloque 1 columna +x
                world.getBlockAt(x+2,y+1,z).setType(Material.AIR);  // Bloque 2 columna +x

                world.getBlockAt(x-2,y,z).setType(Material.AIR);  // Bloque 1 columna -x
                world.getBlockAt(x-2,y+1,z).setType(Material.AIR);  // Bloque 2 columna -x   

                world.getBlockAt(x,y,z+2).setType(Material.AIR);  // Bloque 1 columna +z
                world.getBlockAt(x,y+1,z+2).setType(Material.AIR);  // Bloque 2 columna +z

                world.getBlockAt(x,y,z-2).setType(Material.AIR);  // Bloque 1 columna -z
                world.getBlockAt(x,y+1,z-2).setType(Material.AIR);  // Bloque 2 columna -z  

                world.getBlockAt(x+1,y,z+1).setType(Material.AIR);  // Bloque 1 columna +x
                world.getBlockAt(x+1,y+1,z+1).setType(Material.AIR);  // Bloque 2 columna +x

                world.getBlockAt(x-1,y,z-1).setType(Material.AIR);  // Bloque 1 columna -x
                world.getBlockAt(x-1,y+1,z-1).setType(Material.AIR);  // Bloque 2 columna -x   

                world.getBlockAt(x+1,y,z-1).setType(Material.AIR);  // Bloque 1 columna +z
                world.getBlockAt(x+1,y+1,z-1).setType(Material.AIR);  // Bloque 2 columna +z

                world.getBlockAt(x-1,y,z+1).setType(Material.AIR);  // Bloque 1 columna -z
                world.getBlockAt(x-1,y+1,z+1).setType(Material.AIR);  // Bloque 2 columna -z 
        }

	public static void FuegosArtificiales(Location l) {
            // Fuego artificial

            //Spawn the Firework, get the FireworkMeta.
            Firework fw = (Firework) l.getWorld().spawnEntity(l, EntityType.FIREWORK);
            FireworkMeta fwm = fw.getFireworkMeta();

            //Our random generator
            Random r = new Random();

            //Create our effect with this
            FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(Color.BLACK).withFade(Color.BLACK).with(Type.BALL).trail(r.nextBoolean()).build();

            //Then apply the effect to the meta
            fwm.addEffect(effect);

            //Generate some random power and set it
            int rp = r.nextInt(1) + 1;
            fwm.setPower(rp);

            //Then apply this to our rocket
            fw.setFireworkMeta(fwm);	
	}

}
