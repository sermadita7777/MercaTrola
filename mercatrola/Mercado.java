package mercatrola;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Mercado {
	public static void simulate(int nClientes,int nCajas) {
		System.out.println("Mercatrola abre sus puertas.");
		
		BlockingQueue<Cliente> [] colas=new LinkedBlockingQueue[nCajas];
		
		for(int i=0;i<nCajas;i++) {
			colas[i]=new LinkedBlockingQueue<>();
		}
		
		Thread[] cajas=new Thread[nCajas];
		for(int i=0;i<nCajas;i++) {
			cajas[i]=new Thread(new Caja(colas[i]),"Caja "+i);
			cajas[i].start();
		}
		
		Thread[] clientes=new Thread[nClientes];
		for(int i=0;i<nClientes;i++) {
			BlockingQueue<Cliente> colaAsignada=colas[i%nCajas];
			
			clientes[i]=new Thread(new Cliente(i,colaAsignada),"Cliente "+i);
			clientes[i].start();
		}
		
		for (Thread thread : clientes) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		for (Thread thread : cajas) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println("Mercatrola cierra sus puertas.");
	}
	
	public static void main(String[] args) {
		simulate(5,2);
	}
}
