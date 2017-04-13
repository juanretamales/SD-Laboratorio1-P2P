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
	private ExampleNode tempNodo;
	

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
		/**
		 * sendNode ID del Nodo que se debe enviar
		 */
		//Node sendNode = ((Linkable) currentNode.getProtocol(0)).getNeighbor(randDegree);
		Node sendNode = searchNode(randNode);
		if(myNode.getID()!=sendNode.getID())
		{
			int idNodo = message.getType();
			if(idNodo == myNode.getID())
			{
				System.out.println("Eureka!!");
			}
			else
			{
				
			}
			//guardo en la variable cache para despues verificar que no pasemos por el mismo
			LRUMap<Integer, Integer> cache=((ExampleNode) myNode).getCache();
			//cache.put((int) this.tempNodo.getID(), CommonState.r.nextInt((int)((Linkable) this.tempNodo.getProtocol(0)).degree()));
			
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
