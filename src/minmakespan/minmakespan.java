package minmakespan;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import utils.BadOptionsException;
import static utils.CommandLineUtils.*;
import static minmakespan.MinmakespanDisplay.*;

public class minmakespan {

	public minmakespan() {
	}

	public static void main(String[] args) throws IOException {
		try {
			if(getOption(new String[] {"help", "h"}, args) != null) {
				displayHelp();
			} else if(getOption(new String[] {"file", "f", "fichier"}, args) != null) {
				fromFileDirect(args);
			} else if(getOption(new String[] {"random", "r"}, args) != null) {
				randomDirect(args);
			} else if(getOption(new String[] {"Im", "im"}, args) != null) {
				ImDirect(args);
			} else if(getOption(new String[] {"interactive", "i"}, args) != null) {
				interactiveDirect(args);
			} else {
				mainMenu();
			}
		} catch (BadOptionsException e) {
			displayHelp();
		}
	}
	public static void fromFileDirect(String[] args) throws BadOptionsException {
		try {
			String filename = getOption(new String[] {"file", "f", "fichier"}, args);
			ArrayList<Float> data = parse(readFileInList(filename).get(0));
			int m = data.get(0).intValue();
			int n = data.get(1).intValue();
			float[] fTasks = new float[n];
			
			for(int i = 0; i < n; i++) {
				fTasks[i] = data.get(i + 2);
			}
			
			System.out.println(displayResults(m, n, fTasks, doAlgos(m, n, fTasks)));
		} catch (IndexOutOfBoundsException | IOException e) {
			throw new BadOptionsException();
		}
	}
	public static void ImDirect(String[] args) throws BadOptionsException {
		try {
			Integer m = getIntOption(new String[] {"machines", "m"}, args);
			float[] numericalOptions = getNumericalOptions(args);
			int optIndex = numericalOptions.length -1;
			if(m == null) {
				m = (int) numericalOptions[optIndex];
				optIndex --;
			}
			
			ArrayList<Float> data = doIm(m);
			int n = data.get(1).intValue();
			float[] fTasks = new float[n];
			
			for(int i = 0; i < n; i++) {
				fTasks[i] = data.get(i + 2);
			}
			
			System.out.println(displayResults(m, n, fTasks, doAlgos(m, n, fTasks)));
		} catch (IndexOutOfBoundsException e) {
			throw new BadOptionsException();
		}
	}
	public static void interactiveDirect(String[] args) throws BadOptionsException {
		try {
			ArrayList<Float> data = getFromUser();
			int m = data.get(0).intValue();
			int n = data.get(1).intValue();
			float[] fTasks = new float[n];
			
			for(int i = 0; i < n; i++) {
				fTasks[i] = data.get(i + 2);
			}
			
			System.out.println(displayResults(m, n, fTasks, doAlgos(m, n, fTasks)));
		} catch (IndexOutOfBoundsException | IOException e) {
			throw new BadOptionsException();
		}
	}
	public static void randomDirect(String[] args) throws BadOptionsException {
		try {
			Integer m = getIntOption(new String[] {"machines", "m"}, args);
			Integer n = getIntOption(new String[] {"tasks", "taches", "t", "n"}, args);
			Integer k = getIntOption(new String[] {"iter", "iterations", "k", "i"}, args);
			Float min = getFloatOption(new String[] {"min"}, args);
			Float max = getFloatOption(new String[] {"max"}, args);
			
			float[] numericalOptions = getNumericalOptions(args);
			int optIndex = numericalOptions.length -1;
			if(max == null) {
				max = numericalOptions[optIndex];
				optIndex --;
			}
			if(min == null) {
				min = numericalOptions[optIndex];
				optIndex --;
			}
			if(k == null) {
				k = (int) numericalOptions[optIndex];
				optIndex --;
			}
			if(n == null) {
				n = (int) numericalOptions[optIndex];
				optIndex --;
			}
			if(m == null) {
				m = (int) numericalOptions[optIndex];
				optIndex --;
			}

			ArrayList< ArrayList< Float > > datas = doRandom(m, n, k, min, max);
			float[][] fTasks = new float[k][n];
			
			float[][][] algoResults = new float[k][3][n];
			for(int i = 0; i < k; i++) {
				for(int j = 0; j < n; j ++) {
					fTasks[i][j] = datas.get(i).get(j +2);
				}
				algoResults[i] = doAlgos(m, n, fTasks[i]);
			}

			System.out.println(displayResultsFromRandom(m, n, fTasks, algoResults));
		} catch (IndexOutOfBoundsException e) {
			throw new BadOptionsException();
		}
	}
	
	public static float[] LSA(int m, int n, float[] tasks) {
		float[] machines = new float[m];
		Arrays.fill(machines, 0);
		return LSA(m, n, tasks, machines);
	}
	public static float[] LSA(int m, int n, float[] tasks, float[] machines) {
		for(int i = 0; i < n; i++) {
			float minMachineCapacity = Float.MAX_VALUE;
			int nextMachine = 0;
			for(int j = 0; j < m; j++) {
				if(machines[j] < minMachineCapacity) {
					nextMachine = j;
					minMachineCapacity = machines[j];
				}
			}
			machines[nextMachine] += tasks[i];
		}
		return machines;
	}

	public static float[] LPT(int m, int n, float[] tasks) {
		float[] machines = new float[m];
		Arrays.fill(machines, 0);
		return LPT(m, n, tasks, machines);
	}
	public static float[] LPT(int m, int n, float[] tasks, float[] machines) {
		float[] sortedTasks = tasks;
		for(int i = 0; i < tasks.length; i++) {
			for(int j = i+1; j < tasks.length; j++) {
				if(sortedTasks[i] < sortedTasks[j]) {
					float tmp = sortedTasks[j];
					sortedTasks[j] = sortedTasks[i];
					sortedTasks[i] = tmp;
				}
			}
		}
		return LSA(m, n, sortedTasks, machines);
	}
	
	public static float[] myAlgo(int m, int n, float[] tasks) {
		boolean[] used = new boolean[n];
		int remainingTasks = n;
		Arrays.fill(used, false);
		float mean = mean(tasks);
		
		float[] machines = new float[m];
		Arrays.fill(machines, 0);
		
		for(int iM = 0; iM < m; iM++) {
			for(int iT = 0; iT < n; iT++) {
				if(!used[iT] && machines[iM] + tasks[iT] < mean) {
					machines[iM] += tasks[iT];
					used[iT] = true;
					remainingTasks --;
				}
			}
		}
		float[] lastTasks = new float[remainingTasks];
		int index = 0;
		for(int i = 0; i < tasks.length; i++) {
			if(!used[i]) {
				lastTasks[index] = tasks[i];
				index ++;
			}
		}
		
		return LPT(m, remainingTasks, lastTasks, machines);
	}
	
	public static float[][] doAlgos(int m, int n, float[] tasks) {
		float[][] algos = new float[3][m]; 
		algos[0] = LSA(m, n, tasks);
		algos[1] = LPT(m, n, tasks);
		algos[2] = myAlgo(m, n, tasks);
		
		return algos;
	}

	public static ArrayList<Float> getFromFile() throws IOException {
		Scanner input = new Scanner(System.in);
		System.out.print("Nom du fichier : ");
		String filename = input.nextLine();
//		input.close();
		return parse(readFileInList(filename).get(0));
	}
	public static ArrayList<Float> getFromUser() throws IOException {
		Scanner input = new Scanner(System.in);
		System.out.print("Contenu de l'instance (sous la forme <nombre de machines : nombre de taches : tache1 : tache 2 : ... : tache N>) : \n");
		String line = input.nextLine();
//		input.close();
		return parse(line);
	} 
	public static ArrayList<Float> getIm() {
		int m = (int) numericInput("Nombre de machines :");
		return doIm(m);
	}
	public static ArrayList< ArrayList<Float> > getRandom() {
		int m = (int) numericInput("Nombre de machines :");
		int n = (int) numericInput("Nombre de taches :");
		int k = (int) numericInput("Nombre d'iterations :");
		float min = numericInput("Duree minimale d'une tache :");
		float max = numericInput("Duree maximale d'une tache :");
		return doRandom(m, n, k, min, max);
	}
	public static ArrayList<Float> doIm(int m) {
		int n = (int) 2*m + 1;

		ArrayList<Float> data = new ArrayList<Float>();
		data.add((float) m);
		data.add((float) n);
		
		float taskTime = m;
		for(int i = 0; i < 3; i++)
			data.add(taskTime);
		for(int i = 0; i < n-3; i++) {
			if(i % 2 == 0)
				taskTime ++;
			data.add(taskTime);
		}
		return data;
	}
	public static ArrayList< ArrayList<Float> > doRandom(int m, int n, int k, float min, float max) {
		ArrayList< ArrayList<Float> > datas = new ArrayList< ArrayList<Float> >(k);
		for(int instance = 0; instance < k; instance ++) {
			ArrayList<Float> data = new ArrayList<Float>();
			data.add((float) m);
			data.add((float) n);
			
			for(int i = 0; i < n; i++) {
				float taskTime = (float) (Math.random() * (max - min) + min);
				data.add(taskTime);
			}
			datas.add(data);
		}
		return datas;
	}

	public static float max(float a, float b) {
		return Math.max(a, b);
	}
	public static float sum(float[] array) {
		float sum = 0;
		for(float i : array)
			sum += i;
		return sum;
	}
	public static float max(float[] array) {
		float max = 0;
		for(float i : array)
			max = Math.max(max, i);
		return max;
	}
	public static float mean(float[] array) {
		return sum(array) / array.length;
	}
	
	public static ArrayList<Float> parse(String data) {
		ArrayList<Float> returns = new ArrayList<Float>();
		String[] datas = data.split(":");
		int m = Integer.parseInt(datas[0]);
		int n = Integer.parseInt(datas[1]);
		
		if(datas.length != n+2) {
			System.out.println("Le format de la chaine est incorrect : " + n + " taches demandees mais seulement " + (datas.length -2) + " recues...");
			return null;
		}
		returns.add((float) m);
		returns.add((float) n);
		for(int i = 0; i < n; i++) {
			returns.add(Float.parseFloat(datas[i + 2]));
		}
		return returns;
	}
}
