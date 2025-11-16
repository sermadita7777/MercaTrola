package mercatrola;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Caja implements Runnable {
	private final BlockingQueue<Cliente> cola;
	private final AtomicInteger clientesRestantes;

	public Caja(BlockingQueue<Cliente> cola, AtomicInteger clientesRestantes) {
		super();
		this.cola = cola;
		this.clientesRestantes = clientesRestantes;
	}

	@Override
	public void run() {
		System.out.printf("%s preparada.%n", Thread.currentThread().getName());

		try {
			while (true) {
				Cliente c = this.cola.poll(500,TimeUnit.MILLISECONDS);
				
				if(c!=null) {
					long tStartService = System.currentTimeMillis();
					c.settServiceStart(tStartService);

					System.out.printf("Cliente %s atendido en %s.%n", c.getId(), Thread.currentThread().getName());
					Thread.sleep((int) (Math.random() * 1000));

					System.out.printf("Cliente %s ha terminado en %s. Tiempo de espera: %d ms.%n", c.getId(),
							Thread.currentThread().getName(), c.gettWait());

					this.clientesRestantes.decrementAndGet();
				} else {
					if(this.clientesRestantes.get() == 0) {
						System.out.printf("%s no tiene más clientes y cierra.%n", Thread.currentThread().getName());
						break;
					}
				}

			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
