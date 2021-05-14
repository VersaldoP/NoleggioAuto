package polito.it.noleggio.model;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.PriorityQueue;

import polito.it.noleggio.model.Event.EventType;

public class Simulator {
	//Eventi
	private PriorityQueue<Event> queue;
	
	//parametri di simulazione
	private int NC;//numeber of cars
	private Duration T_IN;//intervallo di tempo tra i clienti 
	private LocalTime oraApertura = LocalTime.of(8,0);
	private LocalTime oraChiusura = LocalTime.of(20,0);
	
	//stato del sistema 
	private int nAuto;
	// miaure in uscita
	private int nClienti;
	private int nClientiInsoddisfatti;
	public void setNumCars(int i) {
		this.NC=i;
		
	}
	public void setClientFrequency(Duration of) {
		this.T_IN=of;
		
	}
	public int getTotClients() {
		// TODO Auto-generated method stub
		return this.nClienti;
	}
	public int getDissatisfied() {
		// TODO Auto-generated method stub
		return this.nClientiInsoddisfatti;
	}
	public void run() {
		this.queue = new PriorityQueue<>();
		
		//stato iniziale
		this.nAuto= NC;
		this.nClienti = 0;
		this.nClientiInsoddisfatti=0;
		
		//Eventi inziali
		LocalTime ora = this.oraApertura;
		while(ora.isBefore(this.oraChiusura)) {
			this.queue.add(new Event(ora,EventType.NUOVO_CLIENTE));
			ora = ora.plus(this.T_IN);
		}
		
		//Ciclo di simulazione
		while(!this.queue.isEmpty()) {
			Event e = this.queue.poll();
			System.out.println(e);
			processEvent(e);
		}
		
	}
	private void processEvent(Event e) {
		switch(e.getType()) {
		case NUOVO_CLIENTE:
			if (this.nAuto>0) {
//				noleggia
				this.nAuto--;
				this.nClienti++;
				
				double num = Math.random()*3;//numeri casuli tra 0 e 3
				if (num<1.0) {
					this.queue.add(
							new Event(
									e.getTime().plus(Duration.of(1,ChronoUnit.HOURS)),
									EventType.RITORNO_AUTO
									)
							);
				} else if (num<2.0) {
					this.queue.add(
							new Event(
									e.getTime().plus(Duration.of(2,ChronoUnit.HOURS)),
									EventType.RITORNO_AUTO
									)
							);
				}else {
					this.queue.add(
							new Event(
									e.getTime().plus(Duration.of(3,ChronoUnit.HOURS)),
									EventType.RITORNO_AUTO
									)
							);
				}
				
			} else {
				//insoddisfatto
				this.nClientiInsoddisfatti++;
				
			}
			break;
		case RITORNO_AUTO:
			this.nAuto++;
			break;
		}
		
	}

}
