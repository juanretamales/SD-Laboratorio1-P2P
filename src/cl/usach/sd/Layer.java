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
		
		boolean debug=true;//poner a true si quieres que muestre los mensajes comunes en la consola
		
		boolean cacheNode=true;//poner false si quieres usar el cache que use solo la id y no el cache que guarda el nodo.
		
		Message message = (Message) event;
		debugeando("Nodo actual:"+myNode.getID(), debug);
		/**
		 * Random degree - busca un numero aleatorio entre la cantidad de nodos en la red.
		 */
		int randNode = CommonState.r.nextInt(Network.size());
		while(randNode==((int)myNode.getID()))
		{
			randNode = CommonState.r.nextInt(Network.size());
		}
		debugeando("Nodo a recibir el mensaje:"+randNode, debug);
		// Guardo en la variable recorrido para despues verificar que no pasemos por el mismo nodo. INT idNodo recorrido, INT idNODO a buscar
		// Nota: para todo lo relacionado con ArrayList consulte a esta pagina: https://jarroba.com/arraylist-en-java-ejemplos/
		ArrayList<Integer> recorrido = new ArrayList<Integer>();
		//Map<Integer, Integer> recorrido = new HashMap<Integer, Integer>();
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
			// Creo el LRUMap para despues asignarlo a cada nodo.
			// Nota: para todo lo relacionado con Map consulte a esta pagina: https://jarroba.com/map-en-java-con-ejemplos/
			LRUMap<Integer, Integer> cache=new LRUMap<Integer, Integer>(((ExampleNode) myNode).getCacheSize());
			while((tempNode.getID()!=((double) randNode))  & (recorrido.size()<maxJump))
			{
				if(cacheNode==true)
				{
					
					//Aqui se realiza el recorrido por cache
					if(((ExampleNode) myNode).getCacheNode(randNode)!=null)
					{
						tempNode=((ExampleNode) myNode).getCacheNode(randNode);
						debugeando("EUREKA!!!!!!!!", debug);
						sendmessage(myNode,tempNode, layerId, message);
						Message msg = new Message((String) "Mensaje enviado con exito");
						sendmessage(tempNode,myNode, layerId, msg);
						break;
					}
				}
				//Aqui se realiza el recorrido por salto
				cache = tempNode.getCache(); //buscando la ID del vecino a quien preguntar
				if(cache.containsKey(randNode)==true && cacheNode==false) //Revisando si la encontro en cache.
				{
					debugeando("	cache.get(randNode, true): "+cache.get(randNode, true), debug);
					int vecinos = (int) ((Linkable) tempNode.getProtocol(0)).degree(); //Obtengo la cantidad de vecinos del nodo actual.
					for (int j = 0; j < vecinos; j++)
					{
						if(((Linkable) tempNode.getProtocol(0)).getNeighbor(j).getID() == ((double) cache.get(randNode, true)))
						{
							tempNode=(ExampleNode) ((Linkable) tempNode.getProtocol(0)).getNeighbor(j);//asignando a tempNote el vecino encontrado
							break;
						}
					}
					if(tempNode.getID()==((double) randNode))
					{
						debugeando("EUREKA!!!!!!!!", debug);
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
							debugeando("Alcansando numero maximo de saltos.", debug);
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
							recorrido.add(((int) tempNode.getID())); 
							break;
						}
					}
					if(tempNode.getID()!=((double) randNode)) //Ahora reviso si el nodo actual no es el encontrado para buscar un vecino aleatorio.
					{
						tempNode=(ExampleNode) ((Linkable) tempNode.getProtocol(0)).getNeighbor(CommonState.r.nextInt(neighbor)); //Actualizo el nodo actual de entre todos los vecinos.
						while(recorrido.contains((int) tempNode.getID())) //Compruebo que el nodo seleccionado no este en el recorrido, de lo contrario selecciono uno nuevo
						{
							int rand = CommonState.r.nextInt(neighbor);
							if(((Linkable) tempNode.getProtocol(0)).getNeighbor(rand)!=null)
							{
								tempNode=(ExampleNode) ((Linkable) tempNode.getProtocol(0)).getNeighbor(rand); //Actualizo el nodo actual de entre todos los vecinos.
							}
						}
						if((recorrido.size()+1)<=maxJump)//revisando si al recorrido le quedan saltos disponibles.
						{
							recorrido.add(((int) tempNode.getID())); //agregando el nuevo nodo al recorrido
						}
						else
						{
							debugeando("Alcansando numero maximo de saltos.", debug);
							break;
						}
					}
				}
			}
			if((double) randNode==tempNode.getID())
			{
				debugeando("ENCONTRO DESTINO!!!!!!!", debug);
				if(cacheNode==true)
				{

					Map<Integer, ExampleNode> tempMapCache = tempNode.getCacheNode();

					tempMapCache.put(randNode, tempNode);
					tempNode.setCacheNode((LRUMap<Integer, ExampleNode>) tempMapCache);

					debugeando("	MENSAJE ENVIADO!", debug);
					sendmessage(myNode,tempNode, layerId, message);
					Message msg = new Message((String) "Mensaje enviado con exito");
					sendmessage(tempNode,myNode, layerId, msg);
				}
				else
				{
					//Como el nodo fue encontrado empezamos a asignar el cache a cada nodo.
					tempNode=(ExampleNode) myNode;
					for( int i = 0 ; i < recorrido.size() ; i++ )
					{
						Map<Integer, Integer> tempMap = tempNode.getCache();
						tempMap.put(randNode, recorrido.get(i));
						tempNode.setCache((LRUMap<Integer, Integer>) tempMap);
						int vecinos = (int) ((Linkable) tempNode.getProtocol(0)).degree(); //Obtengo la cantidad de vecinos del nodo actual.
						for (int j = 0; j < vecinos; j++)
						{
							int a = (int) ((Linkable) tempNode.getProtocol(0)).getNeighbor(j).getID();
							int b = recorrido.get(i);
							if(b == a)
							{
								tempNode=(ExampleNode) ((Linkable) tempNode.getProtocol(0)).getNeighbor(j);
								break;
							}
						}
					}

					
					debugeando("	MENSAJE ENVIADO!", debug);
					sendmessage(myNode,tempNode, layerId, message);
					Message msg = new Message((String) "Mensaje enviado con exito");
					sendmessage(tempNode,myNode, layerId, msg);
					
				}
				
			}
			else
			{
				debugeando("NO SE ENCONTRO DESTINO.", debug);
			}
		}
		else
		{
			debugeando("El nodo destino tiene que ser diferente al nodo origen.", debug);
		}
		getStats();
	}

	private void debugeando(String msg, boolean mostrar)
	{
		if(mostrar==true)
		{
			System.out.println(msg);
		}
	}
	
	private void getStats() {
		Observer.message.add(1);		
	}

	public void sendmessage(Node currentNode,Node sendNode, int layerId, Object message) {
		/**
		 * EnviÛ del dato a travÈs de la capa de transporte, la cual enviar·
		 * seg˙n el ID del emisor y el receptor
		 * 
		    Parameters:
		        src - sender node
		        dest - destination node - Se puede enviar por parametro o buscandolo con searchNode(sendNode)
		        msg - message to be sent
		        pid - protocol identifier
		 */
		((Transport) currentNode.getProtocol(transportId)).send(currentNode, sendNode, message, layerId);

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
