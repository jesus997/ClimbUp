package me.jesus997.ClimbUp;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class Control extends BukkitRunnable {
	private Arena arena;
	private int id;

	public Control(Arena a) {
		arena=a;
	}

	@Override
	public void run() {
		switch (arena.getEstado()) {
		case NoDisponible:

			break;
		case SinIniciar:

			break;
		case Regenerando:

			break;
		case EsperandoJugadores:

			break;
		case Iniciando:

			break;
		case EnCurso:

			break;
		case Finalizada:
			finalizar();
			break;
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	public void finalizar(){
		Bukkit.getServer().getScheduler().cancelTask(id);
	}
}
