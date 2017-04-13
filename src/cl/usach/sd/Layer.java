package cl.usach.sd;

import org.apache.commons.collections4.map.LRUMap;

import peersim.config.Configuration;
import peersim.config.FastConfig;
import peersim.core.CommonState;
import peersim.core.Network;
import peersim.core.Node;
import peersim.edsim.EDProtocol;
import peersim.transport.Transport;
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
		
		/**
		 * Random degree - busca un numero aleatorio entre la cantidad de nodos en la red.
		 */
		int randNode = CommonState.r.nextInt(Network.size());
		while(randNode==((int)myNode.getID()))
		{
			randNode = CommonState.r.nextInt(Network.size());
		}
		System.out.println("Nodo a recibir el mensaje:"+randNode);
		//guardo en la variable cache para despues verificar que no pasemos por el mismo
		LRUMap<Integer, Integer> cache=((ExampleNode) myNode).getCache();
		//cache.put((int) this.tempNodo.getID(), CommonState.r.nextInt((int)((Linkable) this.tempNodo.getProtocol(0)).degree()));
		/**
		 * maxJump Cantidad de saltos maximos para encontrar un nodo.
		 */
		int maxJump=4;
		/**
		 * sendNode ID del Nodo que se debe enviar
		 */
		//Node sendNode = ((Linkable) currentNode.getProtocol(0)).getNeighbor(randDegree);
		Node sendNode = searchNode(randNode);
		/**
		 * Buscando al nodo si no esta en cache
		 */
		//Verifico si el origen es distinto al destino.
		if(myNode.getID()!=sendNode.getID())
		{
			ExampleNode tempNode = (ExampleNode) myNode;
			//cache.put(sendNode.getID(), 5);
			//Mientras que no encuentre al nodo o queden saltos (espacio) en la variable cache
			while((tempNode.getID()!=sendNode.getID()) || (cache.size()<maxJump))
			{
				
				for (int i = 0; i < ((Linkable) myNode.getProtocol(0)).degree(); i++) 
				{
					//Reviso si algun vecino tiene la ID que estoy buscando.
					if(myNode.getID()==((Linkable) myNode.getProtocol(0)).getNeighbor(i).getID())
					{
						//de tenerla actualiza el cache, envia el mensaje y termina el ciclo for.
						/*
						 * Actualizar los cache de los nodos
						 */
						sendmessage(myNode,((Linkable) tempNode.getProtocol(0)).getNeighbor(i), layerId, message);
						break;
					}
				}
				
			}
			
			int idNodo = message.getType();
			if(idNodo == myNode.getID())
			{
				System.out.println("Eureka!!");
			}
			else
			{
				
			}
			
			sendmessage(myNode,sendNode, layerId, message);
		}
		else
		{
			sendmessage(myNode,sendNode, layerId, message);
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
