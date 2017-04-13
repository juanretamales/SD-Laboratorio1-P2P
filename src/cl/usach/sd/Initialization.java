package cl.usach.sd;

import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Control;
import peersim.core.Network;

public class Initialization implements Control {
	String prefix;

	int idLayer;
	int idTransport;

	int argExample;

	public Initialization(String prefix) {
		this.prefix = prefix;
		/**
		 * Para obtener valores que deseamos como argumento del archivo de
		 * configuración, podemos colocar el prefijo de la inicialización en
		 * este caso "init.1statebuilder" y luego la variable de entrada
		 */
		// Configuration.getPid retornar al número de la capa
		// que corresponden esa parte del protocolo
		this.idLayer = Configuration.getPid(prefix + ".protocol");
		this.idTransport = Configuration.getPid(prefix + ".transport");
		// Configuration.getInt retorna el número del argumento
		// que se encuentra en el archivo de configuración.
		// También hay Configuration.getBoolean, .getString, etc...
		this.argExample = Configuration.getInt(prefix + ".argExample");

		System.out.println("Arg: " + argExample);
	}

	/**
	 * Ejecución de la inicialización en el momento de crear el overlay en el
	 * sistema
	 */
	@Override
	public boolean execute() {
		/**
		 * Tira un número random el cual corresponderá a un Nodo de la red
		 */
		int idNode = CommonState.r.nextInt(Network.size());

		/**
		 * Asignar un valor al atributo del peer (o nodo) de la red
		 */
		int rand = CommonState.r.nextInt(100);
		((ExampleNode) Network.get(idNode)).setCount(rand);

		return true;
	}

}
