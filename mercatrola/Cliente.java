package mercatrola;

import java.util.concurrent.BlockingQueue;

public class Cliente implements Runnable{
	private int id;
	private final BlockingQueue<Cliente> cola;
	private long tArrival; 

	public Cliente(int id,BlockingQueue<Cliente> cola) {
		super();
		this.id=id;
		this.cola=cola;
	}


	@Override
	public void run() {
		this.tArrival=System.currentTimeMillis();
		System.out.printf("El cliente %s ha llegado al supermercado.%n",Thread.currentThread().getName());
		
		try {
			this.cola.put(this);
			System.out.printf("El %s se ha puesto en la cola.%n",Thread.currentThread().getName());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public long gettArrival() {
		return tArrival;
	}


	public int getId() {
		return id;
	}

	
}
