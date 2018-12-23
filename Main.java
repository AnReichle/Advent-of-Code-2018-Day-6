/*
 * Disclaimer:
 * Despite the program working for auxiliary data
 * and the example, it did not give the right answer
 * for the puzzle input. For demonstration of the principle
 * and in hope someone finds the mistake, I published it
 * anyways.
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

public class Main {
	public static void main(String[] args) throws Exception {
		ArrayList<Integer> x = new ArrayList<Integer>();
		ArrayList<Integer> y = new ArrayList<Integer>();
		String path = "src\\input.txt";
		try(BufferedReader br = new BufferedReader(new FileReader(path))){
			String str;
			while((str = br.readLine()) != null) {
				// shift indexes by 1 in order to avoid negative coordinates in later code
				//, only relative distances matter;
				x.add(Integer.parseInt(str.split(", ")[0])+1);
				y.add(Integer.parseInt(str.split(", ")[1])+1);
			}
		}	
		int maxY = y.stream().max(Integer::compare).get();
		int maxX = x.stream().max(Integer::compare).get();
		int nearestNeigh, closestDist, aux;
		// points with i == 0, maxX+2 xor j == 0, maxY+2 are on the frame,
		// there are not points on the frame;
		int[][] grid = new int[maxX+3][maxY+3];
		for(int i = 0; i<grid.length; i++) {
			for(int j = 0; j<grid[0].length; j++) {
				nearestNeigh = 0;
				closestDist = manhDist(x.get(0),i,y.get(0),j);
				for(int k = 1; k<x.size(); k++) {
					if(closestDist > (aux = manhDist(x.get(k),i,y.get(k),j))) {
						closestDist = aux;
						nearestNeigh = k;
						continue;
					}
					if(closestDist == aux) {
						// -1 stands for no unique nearest neighbor
						nearestNeigh = -1;
						continue;
					}
				}
				grid[i][j] = nearestNeigh;
			}
		}
		HashSet<Integer> infinteAreas = new HashSet<Integer>();
		for(int i = 0; i<=maxY+2 ; i++) {
			infinteAreas.add(grid[0][i]);
			infinteAreas.add(grid[maxX+2][i]);
		}
		for(int i = 0; i<=maxX+2 ; i++) {
			infinteAreas.add(grid[i][0]);
			infinteAreas.add(grid[i][maxY+2]);
		}
		HashMap<Integer, Integer> areas = new HashMap<Integer, Integer>();
		for(int i = 0; i<grid.length; i++) {
			for(int j = 0; j<grid[0].length; j++) {
				if((aux = grid[i][j]) == -1) continue;
				if(!areas.containsKey(aux = grid[i][j])) areas.put(aux, 1);
				else areas.put(aux, areas.get(aux)+1);
			}
		}
		Entry<Integer, Integer> max = areas.entrySet().stream()
						.filter(e -> !infinteAreas.contains(e.getKey()))
						.max((e1,e2) -> e1.getKey().compareTo(e2.getKey()))
						.get();
		System.out.println("answer for (a): " + max.getValue());
	}
	static int manhDist(int x1, int x2, int y1, int y2) {
		return Math.abs(x1-x2) + Math.abs(y1-y2);
	}
}
