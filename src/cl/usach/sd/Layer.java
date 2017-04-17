package cl.usach.sd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections4.map.LRUMap;

import peersim.config.Configuration;
import peersim.config.FastConfig;
import peersim.core.CommonState;
import peersim.core.Network;
import peersim.core.Node;
import peersim.edsim.EDProtocol;
import peersim.transport.Transport;
import sun.misc.Cache;
import peersim.core.Linkable;

public class Layer implements Cloneable, EDProtocol {
	private static final String PAR_TRANSPORT = "transport";
	private static String prefix = null;
	private int transportId;
	private int layerId;
	
	

	/**
	 * M√©todo en el cual se va a procesar el mensaje que ha llegado al Nodo
	 * desde otro Nodo. Cabe destacar que el mensaje va a ser el evento descrito
	 * en la clase, a trav√©s de la simulaci√≥n de eventos discretos.
	 */
	@Override
	public void processEvent(Node myNode, int layerId, Object event) {
		Message message = (Message) event;
		System.out.println("Nodo actual:"+myNode.getID());
		/**
		 * Random degree - busca un numero aleatorio entre la cantidad de nodos en la red.
		 */
		int randNode = CommonState.r.nextInt(Network.size());
		while(randNode==((int)myNode.getID()))
		{
			randNode = CommonState.r.nextInt(Network.size());
		}
		System.out.println("Nodo a recibir el mensaje:"+randNode);
		//guardo en la variable recorrido para despues verificar que no pasemos por el mismo nodo. INT idNodo recorrido, INT idNODO a buscar
		ArrayList<Integer> recorrido = new ArrayList<Integer>();
		//cache.put((int) this.tempNodo.getID(), CommonState.r.nextInt((int)((Linkable) this.tempNodo.getProtocol(0)).degree()));
		/**
		 * maxJump Cantidad de saltos maximos para encontrar un nodo.
		 */
		int maxJump=4;
		/**
		 * sendNode ID del Nodo que se debe enviar
		 */
		//Node sendNode = ((Linkable) currentNode.getProtocol(0)).getNeighbor(randDegree);
		//Node sendNode = searchNode(randNode);
		// ((double) randNode)
		/**
		 * Buscando al nodo si no esta en cache
		 */
		//Verifico si el origen es distinto al destino.
		if(myNode.getID()!=((double) randNode))
		{
			ExampleNode tempNode = (ExampleNode) myNode;
			//Verifico si tengo la busqueda en el cache
			if(tempNode.getCache().containsKey(((double) randNode)))
			{
				//lo encontro en cache
				
				LRUMap<Integer, Integer> cache=new LRUMap<Integer, Integer>(((ExampleNode) myNode).getCacheSize());
				while((tempNode.getID()!=((double) randNode)))
				{
					cache = tempNode.getCache();
					//verificando si se encuentra dentro del cache
					if(cache.get(randNode)!=null)
					{
						//buscando la ID del vecino a quien preguntar
						//int i=cache.get(((int) sendNode.getID()));
						//asignando a tempNote el vecino encontrado
						tempNode=(ExampleNode) ((Linkable) tempNode.getProtocol(0)).getNeighbor(cache.get(randNode));
					}
					//si tempNode no es el nodo que estamos buscando sigue buscando en cache.
					//falta buscar si ya no lo tiene en cache.
				}
			}
			else
			{
				//si no lo tengo en cache.
				//cache.put(sendNode.getID(), 5);
				//Mientras que no encuentre al nodo o queden saltos (espacio) en la variable cache
				//int found = 0;
				while((tempNode.getID()!=((double) randNode)) & (recorrido.size()<maxJump))
				{
					//System.out.println("Destino diferente a inicio");
					//guardo la cantidad de vecinos que tiene un nodo.
					int neighbor = (int) ((Linkable) tempNode.getProtocol(0)).degree();
					for (int i = 0; i < neighbor; i++) 
					{
						//Reviso si algun vecino tiene la ID que estoy buscando.
						if(((double) randNode)==((Linkable) tempNode.getProtocol(0)).getNeighbor(i).getID())
							//if(tempNode.getID()==((Linkable) tempNode.getProtocol(0)).getNeighbor(i).getID())	
						{
							tempNode=(ExampleNode) ((Linkable) tempNode.getProtocol(0)).getNeighbor(i);
							break;
						}
					}
					
					if(tempNode.getID()==((double) randNode))
					{
						//actualizo cache
						System.out.println("EUREKA!!!!!!!!");
						break;
					}
					else
					{
						tempNode=(ExampleNode) ((Linkable) tempNode.getProtocol(0)).getNeighbor(CommonState.r.nextInt(neighbor));
						//selecciono un nuevo nodo entre los vecinos al azar para continuar, y si ya lo tomo, lo salta y toma al azar otro vecino
						while(recorrido.contains(((int) tempNode.getID()))==true)
						{
							
							/*tempId=(int) CommonState.r.nextInt(neighbor);
							if(recorrido.contains(tempId)==false)
							{
								
								tempNode=(ExampleNode) ((Linkable) tempNode.getProtocol(0)).getNeighbor(tempId);
								System.out.println("Buscando nodo("+tempId+") y encontrando:"+tempNode.getID());
								break;
							}*/
							tempNode=(ExampleNode) ((Linkable) tempNode.getProtocol(0)).getNeighbor(CommonState.r.nextInt(neighbor));
						}
						recorrido.add(((int) tempNode.getID()));
						
						//System.out.println("    Ir al vecino N∞"+ tempId);
						//cache.put(key, value);
					}
					//System.out.println("    tempNode.getID():"+ tempNode.getID());
				}
				if(recorrido.size()==maxJump)
				{
					System.out.println("NO SE ENCONTRO DESTINO");
				}
			}
			
			//de tenerla actualiza el cache, envia el mensaje y termina el ciclo for.
			/*
			 * Actualizar los cache de los nodos
			 */
			/*
			Iterator<Integer> it = cache.keySet().iterator();
			while(it.hasNext())
			{
				Integer key = it.next();
				//putCache;
			}
			sendmessage(myNode,((Linkable) tempNode.getProtocol(0)).getNeighbor(2), layerId, message);
			int idNodo = message.getType();
			if(idNodo == myNode.getID())
			{
				System.out.println("Eureka!!");
			}
			else
			{
				
			}
			*/
			sendmessage(myNode,tempNode, layerId, message);
		}
		else
		{
			//sendmessage(myNode,tempNode, layerId, message);
			System.out.println("El nodo destino tiene que ser diferente al nodo origen.");
		}
		getStats();
	}

	private void getStats() {
		Observer.message.add(1);		
	}

	public void sendmessage(Node currentNode,Node sendNode, int layerId, Object message) {
		
		/*
		System.out.println("CurrentNode: " + currentNode.getID() + " | Degree: " + ((Linkable) currentNode.getProtocol(0)).degree());
		
		for (int i = 0; i < ((Linkable) currentNode.getProtocol(0)).degree(); i++) {
			System.out.println("	NeighborNode: " + ((Linkable) currentNode.getProtocol(0)).getNeighbor(i).getIndex());
		}*/
		/**
		 * EnviÛ del dato a travÈs de la capa de transporte, la cual enviar·
		 * seg˙n el ID del emisor y el receptor
		 * 
		    Parameters:
		        src - sender node
		        dest - destination node
		        msg - message to be sent
		        pid - protocol identifier
		 */
		((Transport) currentNode.getProtocol(transportId)).send(currentNode, sendNode, message, layerId);
		// Otra forma de hacerlo
		// ((Transport)
		// currentNode.getProtocol(FastConfig.getTransport(layerId))).send(currentNode,
		// searchNode(sendNode), message, layerId);

	}

	/**
	 * Constructor por defecto de la capa Layer del protocolo construido
	 * 
	 * @param prefix
	 */
	public Layer(String prefix) {
		/**
		 * Inicializaci√≥n del Nodo
		 */
		Layer.prefix = prefix;
		transportId = Configuration.getPid(prefix + "." + PAR_TRANSPORT);
		/**
		 * Siguiente capa del protocolo
		 */
		layerId = transportId + 1;
	}

	private Node searchNode(int id) {
		return Network.get(id);
	}

	/**
	 * Definir Clone() para la replicacion de protocolo en nodos
	 */
	public Object clone() {
		Layer dolly = new Layer(Layer.prefix);
		return dolly;
	}
}
