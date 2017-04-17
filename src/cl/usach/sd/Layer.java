package cl.usach.sd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
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
			LRUMap<Integer, Integer> cache=new LRUMap<Integer, Integer>(((ExampleNode) myNode).getCacheSize());
			while((tempNode.getID()!=((double) randNode))  & (recorrido.size()<maxJump))
			{
				
				cache = tempNode.getCache(); //buscando la ID del vecino a quien preguntar
				if(cache.get(randNode)!=null) //Revisando si la encontro en cache.
				{
					tempNode=(ExampleNode) ((Linkable) tempNode.getProtocol(0)).getNeighbor(cache.get(randNode, true)); //asignando a tempNote el vecino encontrado
					if(tempNode.getID()==((double) randNode))
					{
						System.out.println("EUREKA!!!!!!!!");
						break;
					}
					else
					{
						if((recorrido.size()+1)<=maxJump)//revisando si al recorrido le quedan saltos disponibles.
						{
							recorrido.add(((int) tempNode.getID())); //agregando el nuevo nodo al recorrido
						}
						else
						{
							System.out.println("Alcansando numero maximo de saltos.");
							break;
						}
					}
				}
				else //de no encontrarla
				{
					int neighbor = (int) ((Linkable) tempNode.getProtocol(0)).degree(); //Obtengo la cantidad de vecinos del nodo actual.
					for (int i = 0; i < neighbor; i++)  //Reviso si algun vecino tiene la ID que estoy buscando.
					{
						if(((double) randNode)==((Linkable) tempNode.getProtocol(0)).getNeighbor(i).getID()) //De tenerla sera maravillas y termina el While
						{
							tempNode=(ExampleNode) ((Linkable) tempNode.getProtocol(0)).getNeighbor(i);
							recorrido.add(((int) tempNode.getID()));//este ultimo es para cuando llegue al final sepa que tiene q ir a ese vecino y no recorrerlos
							break;
						}
					}
					if(tempNode.getID()!=((double) randNode)) //Ahora reviso si el nodo actual no es el encontrado para buscar un vecino aleatorio.
					{
						tempNode=(ExampleNode) ((Linkable) tempNode.getProtocol(0)).getNeighbor(CommonState.r.nextInt(neighbor)); //Actualizo el nodo actual de entre todos los vecinos.
						while(recorrido.contains((int) tempNode.getID())==true) //Compruebo que el nodo seleccionado no este en el recorrido, de lo contrario selecciono uno nuevo
						{
							tempNode=(ExampleNode) ((Linkable) tempNode.getProtocol(0)).getNeighbor(CommonState.r.nextInt(neighbor)); //Actualizo el nodo actual de entre todos los vecinos.
							//System.out.println("	Buscando en el vecino: "+tempNode.getID());
						}
						if((recorrido.size()+1)<=maxJump)//revisando si al recorrido le quedan saltos disponibles.
						{
							recorrido.add(((int) tempNode.getID())); //agregando el nuevo nodo al recorrido
						}
						else
						{
							System.out.println("Alcansando numero maximo de saltos.");
							break;
						}
					}
				}
			}
			if(myNode.getID()==tempNode.getID())
			{
				System.out.println("ENCONTRO DESTINO!!!!!!!");
				Node sendNode = (Node) tempNode;
				// Ya le encontro, falta actualizar cache de todos los nodos recorridos.
				tempNode=(ExampleNode) myNode;
				for( int i = 0 ; i < recorrido.size() ; i++ )
				{
					  tempNode.getCache().put(randNode, recorrido.get(i));//Agrego el cache al nodo.
					  tempNode=(ExampleNode) ((Linkable) tempNode.getProtocol(0)).getNeighbor(recorrido.get(i));//me cambio al nodo vecino para actualizar su cache.
				}
				
				
				sendmessage(myNode,sendNode, layerId, message);
				
			}
			else
			{
				System.out.println("NO SE ENCONTRO DESTINO. Recorrido:");
				for( int i = 0 ; i < recorrido.size() ; i++ )
				{
					System.out.println("	"+recorrido.get(i));
				}
				//Como no encontro el nodo en los saltos, igual realiza el envio del mensaje PERO la busca directamente en TODA la red.
				Node sendNode = searchNode(randNode);
				sendmessage(myNode,sendNode, layerId, message);
			}
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
