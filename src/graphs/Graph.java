package graphs;

import java.util.*;

/**
 * Implements a graph. We use two maps: one map for adjacency properties
 * (adjancencyMap) and one map (dataMap) to keep track of the data associated
 * with a vertex.
 * 
 * @author cmsc132
 * 
 * @param <E>
 */
public class Graph<E> {
	/* You must use the following maps in your implementation */
	private HashMap<String, HashMap<String, Integer>> adjMap;
	private HashMap<String, E> dataMap;

	public Graph() {
		adjMap = new HashMap<String, HashMap<String, Integer>>();
		dataMap = new HashMap<String, E>();
	}

	public void doDepthFirstSearch(String startv, CallBack<E> printCallBack) {
		TreeSet<String> tested = new TreeSet<String>();
		String current = "";
		Stack<String> st = new Stack<String>();
		st.add(startv);

		while (!st.isEmpty()) {
			current = st.pop();
			if (!tested.contains(current)) {
				tested.add(current);
				printCallBack.processVertex(current, dataMap.get(current));
				for (String str : adjMap.get(current).keySet()) {
					if (!tested.contains(str)) {
						st.add(str);
					}
				}
			}
		}
	}

	public void doBreadthFirstSearch(String startv, CallBack<E> printCallBack) {
		TreeSet<String> tested = new TreeSet<String>();
		String current = "";
		LinkedList<String> que = new LinkedList<String>();
		que.add(startv);

		while (!que.isEmpty()) {
			current = que.poll();
			if (!tested.contains(current)) {
				tested.add(current);
				printCallBack.processVertex(current, dataMap.get(current));
				for (String h : adjMap.get(current).keySet()) {
					if (!tested.contains(h)) {
						que.add(h);
					}
				}
			}
		}
	}

	public void addVertex(String startv, E data) {
		dataMap.put(startv, data);
	}

	public void addDirectedEdge(String startv, String endv, int cost) {
		if (adjMap.containsKey(startv)) {
			adjMap.get(startv).put(endv, cost);
		} else {
			HashMap<String, Integer> newmap = new HashMap<String, Integer>();
			newmap.put(endv, cost);
			adjMap.put(startv, newmap);
		}

	}

	public Set<String> getVertices() {
		TreeSet<String> rString = new TreeSet<String>();
		for (String str : dataMap.keySet()) {
			rString.add(str);
		}
		return rString;
	}

	public int doDijkstras(String startv, String endv, ArrayList<String> shortestPath) {
		Set<String> checked = new TreeSet<>();
		PriorityQueue<Entry> pQue = new PriorityQueue<Entry>();
		HashMap<String, String> last = new HashMap<>();
		HashMap<String, Integer> costmap = new HashMap<>();
		Entry front = null;

		if (adjMap.size()==0) {
			return -1;
		}

		for (String h : getVertices()) {
			if (!h.equals(startv)) {
				costmap.put(h, Integer.MAX_VALUE);
			}
		}

		pQue.add(new Entry(startv, 0));
		costmap.put(startv, 0);
		last.put(startv, null);

		while (!checked.contains(endv) && !pQue.isEmpty()) {
			front = pQue.poll();
			checked.add(front.data);
			for (String a : getAdjacentVertices(front.data).keySet()) {
				if (!checked.contains(a)) {
					if (costmap.get(front.data) + getCost(front.data, a) < costmap.get(a)) {
						costmap.put(a, costmap.get(front.data) + getCost(front.data, a));
						last.put(a, front.data);
						pQue.add(new Entry(a, costmap.get(front.data) + getCost(last.get(a), a)));
					}
				}
			}
		}
		String vert = endv;
		while (last.get(vert) != null) {
			shortestPath.add(0, last.get(vert));
			vert = last.get(vert);
		}
		if (costmap.get(endv) == 2147483647) {
			shortestPath.add("None");
			return -1;
		} else {
			shortestPath.add(endv);
			return costmap.get(endv);
		}
	}

	public int getCost(String startv, String endv) {
		return adjMap.get(startv).get(endv);
	}

	public E getData(String vertex) {
		return dataMap.get(vertex);
	}

	public String toString() {
		String returnstring = "";
		Set<String> verts = this.getVertices();
		String[] temp = new String[0];
		temp = verts.toArray(temp);
		returnstring += "Vertices: [";
		for (int i = 0; i < temp.length; i++) {
			returnstring += temp[i];
			if (i != temp.length - 1) {
				returnstring += ", ";
			}
		}
		returnstring += "]" + "\nEdges:\n";
		for (String s : temp) {
			returnstring += "Vertex(" + s + ")--->";
			if (adjMap.get(s) == null) {
				returnstring += "{}\n";
			} else {
				returnstring += adjMap.get(s) + "\n";
			}
		}
		return returnstring;
	}

	public Map<String, Integer> getAdjacentVertices(String vertexName) {
		Map<String, Integer> rMap = new TreeMap<String, Integer>();
		if (adjMap.get(vertexName) == null) {
			return rMap;
		} else {
			return adjMap.get(vertexName);
		}
	}

	public class Entry implements Comparable<Entry> {
		String data;
		int cost;

		private Entry(String dataPortion, int cost) {
			data = dataPortion;
			this.cost = cost;
		}

		@Override
		public int compareTo(Graph<E>.Entry o) {
			if (this.cost == o.cost) {
				return 0;
			} else if (this.cost > o.cost) {
				return 1;
			} else {
				return -1;
			}
		}

	}
}