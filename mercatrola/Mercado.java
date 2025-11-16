package mercatrola;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Mercado {
	public static void simulateMulticola(int nClientes,int nCajas) {
		BlockingQueue<Cliente> [] colas=new LinkedBlockingQueue[nCajas];
		
		for(int i=0;i<nCajas;i++) {
			colas[i]=new LinkedBlockingQueue<>();
		}
		
		AtomicInteger clientesRestantes=new AtomicInteger(nClientes);
		Thread[] cajas=new Thread[nCajas];
		for(int i=0;i<nCajas;i++) {
			cajas[i]=new Thread(new Caja(colas[i],clientesRestantes),"Caja "+i);
			cajas[i].start();
		}
		
		List<Cliente> listaClientes =new ArrayList<>();
		
		Thread[] clientes=new Thread[nClientes];
		for(int i=0;i<nClientes;i++) {
			BlockingQueue<Cliente> colaAsignada=colas[i%nCajas];
			
			Cliente c=new Cliente(i,colaAsignada);
			listaClientes.add(c);
			
			clientes[i]=new Thread(c,"Cliente "+i);
			clientes[i].start();
		}
		
		for (Thread thread : cajas) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		for (Thread thread : clientes) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println("\nMercatrola cierra sus puertas.\n");
		
		getStats(listaClientes);
	}
	
	private static void getStats(List<Cliente> listaClientes) {
		List<Long> tiemposEspera=new ArrayList<>();
		for(Cliente c: listaClientes) {
			tiemposEspera.add(c.gettWait());
		}
		
		long max=tiemposEspera.stream().mapToLong(v -> v).max().orElse(0);
		long min=tiemposEspera.stream().mapToLong(v -> v).min().orElse(0);
		double avg=tiemposEspera.stream().mapToLong(v -> v).average().orElse(0);
		
		System.out.println("RESULTADOS");
		System.out.println("Tiempo de espera máximo: "+max+" ms.");
		System.out.println("Tiempo de espera mínimo: "+min+" ms.");
		System.out.println("Tiempo de espera medio: "+avg+" ms.");
		
	}
	
	public static void simulateUnicola(int nClientes,int nCajas) {
		System.out.println("UNICOLA");
		BlockingQueue<Cliente> cola  = new LinkedBlockingQueue<>();
		
		AtomicInteger clientesRestantes=new AtomicInteger(nClientes);
		Thread[] cajas=new Thread[nCajas];
		for(int i=0;i<nCajas;i++) {
			cajas[i]=new Thread(new Caja(cola,clientesRestantes),"Caja "+i);
			cajas[i].start();
		}
		
		List<Cliente> listaClientes =new ArrayList<>();
		
		Thread[] clientes=new Thread[nClientes];
		for(int i=0;i<nClientes;i++) {
			
			Cliente c=new Cliente(i,cola);
			listaClientes.add(c);
			
			clientes[i]=new Thread(c,"Cliente "+i);
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
		
		System.out.println("\nMercatrola cierra sus puertas.\n");
		
		getStats(listaClientes);
	}
	
	public static void main(String[] args) {
		System.out.println("Mercatrola abre sus puertas.\n");
		simulateMulticola(500,10);
		simulateUnicola(500,10);
		
	}
}
