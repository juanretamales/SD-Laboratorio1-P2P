package cl.usach.sd;

import org.apache.commons.collections4.map.LRUMap;

import peersim.core.GeneralNode;

public class ExampleNode extends GeneralNode {
	private int count;
	/*
	 * Primer int es el nodo buscado
	 * el segundo es en cual vecino encontro la solucion.
	 */
	private LRUMap<Integer, Integer> cache;
	private int cacheSize;
	//para prueba
	//private list<String> datos;
	
	public ExampleNode(String prefix) {
		super(prefix);
		this.setCount(0);
		this.cacheSize=100;
		this.cache=new LRUMap<Integer, Integer> (this.cacheSize);
	}
	public ExampleNode(String prefix, Integer cacheSize) {
		super(prefix);
		this.setCount(0);
		this.cacheSize=cacheSize;
		this.cache=new LRUMap<Integer, Integer> (this.cacheSize);
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public LRUMap<Integer, Integer> getCache() {
		return this.cache;
	}

	public void setCache(LRUMap<Integer, Integer> cache) {
		this.cache = cache;
	}

	public int getCacheSize() {
		return cacheSize;
	}

	public void setCacheSize(int cacheSize) {
		this.cacheSize = cacheSize;
	}
	
}
