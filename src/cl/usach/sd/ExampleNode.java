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
	private LRUMap<Integer, ExampleNode> cacheNode;
	private int cacheSize;
	//para prueba
	//private list<String> datos;
	
	public ExampleNode(String prefix) {
		super(prefix);
		this.setCount(0);
		this.cacheSize=100;
		this.cache=new LRUMap<Integer, Integer> (this.cacheSize);
		this.cacheNode=new LRUMap<Integer, ExampleNode> (this.cacheSize);
	}
	public ExampleNode(String prefix, Integer cacheSize) {
		super(prefix);
		this.setCount(0);
		this.cacheSize=cacheSize;
		this.cache=new LRUMap<Integer, Integer> (this.cacheSize);
		this.cacheNode=new LRUMap<Integer, ExampleNode> (this.cacheSize);
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
	
	public void putCache(int idNode, int idNeigbor)
	{
		this.cache.put(idNode, idNeigbor);
	}
	public LRUMap<Integer, Integer> getCache(int idNode)
	{
		return this.cache;
	}
	public int getOneCache(int idNode)
	{
		return this.cache.get(idNode, true);
	}
	public void insertCache(int idNode, int idNeigbor)
	{
		if(this.cache.isFull())
		{
			this.cache.remove(this.cache.lastKey());
			this.cache.put(idNode, idNeigbor);
		}
		else
		{
			this.cache.put(idNode, idNeigbor);
		}
	}
	public int getCacheSize() {
		return cacheSize;
	}

	public void setCacheSize(int cacheSize) {
		this.cacheSize = cacheSize;
	}
	public LRUMap<Integer, ExampleNode> getCacheNode() {
		return cacheNode;
	}
	public void setCacheNode(LRUMap<Integer, ExampleNode> cacheNode) {
		this.cacheNode = cacheNode;
	}
	public ExampleNode getCacheNode(double idNode)
	{
		if(this.cacheNode.containsKey(idNode))
		{
			return this.cacheNode.get(idNode, true);
		}
		return null;
	}
	
}
