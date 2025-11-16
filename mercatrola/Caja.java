package mercatrola;

import java.util.concurrent.BlockingQueue;

public class Caja implements Runnable{
	private final BlockingQueue<Cliente> cola;

	public Caja(BlockingQueue<Cliente> cola) {
		super();
		this.cola = cola;
	}

	@Override
	public void run() {
		System.out.printf("%s preparada.%n",Thread.currentThread().getName());
		
		while(!this.cola.isEmpty()) {
			try {
				
				Cliente c=this.cola.take();
				
				long tStartService=System.currentTimeMillis();
				c.settServiceStart(tStartService);
				
				System.out.printf("Cliente %s atendido en %s.%n",c.getId(),Thread.currentThread().getName());
				Thread.sleep((int)(Math.random()*1000));
				
				long tEndService=System.currentTimeMillis();
				System.out.printf("Cliente %s ha terminado en %s. Tiempo de espera: %d ms.%n",
						c.getId(),Thread.currentThread().getName(),c.gettWait());
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}

	
}
