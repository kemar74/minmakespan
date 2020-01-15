package minmakespan;

import static utils.CommandLineUtils.numericInput;
import static minmakespan.minmakespan.*;

import java.io.IOException;
import java.util.ArrayList;

public class MinmakespanDisplay {

	public static String displayResults(int m, int n, float[] tasks, float[][] results) {
		String output = "";

		float[] LSA = results[0];
		float[] LPT = results[1];
		float[] myAlgo = results[2];
		float maxLSA = max(LSA);
		float maxLPT = max(LPT);
		float maxMyAlgo = max(myAlgo);
		float borneMax = max(tasks);
		float borneMean = sum(tasks)/m;
		float ratioLSA = maxLSA / max(borneMax, borneMean);
		float ratioLPT = maxLPT / max(borneMax, borneMean);
		float ratioMyAlgo = maxMyAlgo / max(borneMax, borneMean);

		output += "Borne inferieure ``maximum'' = " + borneMax + "\n";
		output += "Borne inferieure ``moyenne'' = " + borneMean + "\n";
		
		output += "Resultat LSA = " + maxLSA + "\n";
		output += "ratio LSA = " + ratioLSA + "\n";
		
		output += "Resultat LPT = " + maxLPT + "\n";
		output += "ratio LPT = " + ratioLPT + "\n";
		
		output += "Resultat myAlgo = " + maxMyAlgo + "\n";
		output += "ratio MyAlgo = " + ratioMyAlgo + "\n";
		return output;
	}
	public static String displayResultsFromRandom(int m, int n, float[][] allTasks, float[][][] allResults) {
		String output = "";
		

		float meanRatioLSA = 0;
		float meanRatioLPT = 0;
		float meanRatioMyAlgo = 0;
		
		for(int i = 0; i < allResults.length; i++) {
			float[][] results = allResults[i];
			float[] tasks = allTasks[i];
			float[] LSA = results[0];
			float[] LPT = results[1];
			float[] myAlgo = results[2];
			float maxLSA = max(LSA);
			float maxLPT = max(LPT);
			float maxMyAlgo = max(myAlgo);
			float borneMax = max(tasks);
			float borneMean = sum(tasks)/m;
			meanRatioLSA += maxLSA / max(borneMax, borneMean);
			meanRatioLPT += maxLPT / max(borneMax, borneMean);
			meanRatioMyAlgo += maxMyAlgo / max(borneMax, borneMean);
		}
		meanRatioLSA /= allResults.length;
		meanRatioLPT /= allResults.length;
		meanRatioMyAlgo /= allResults.length;
		
		output += "ratio moyen LSA = " + meanRatioLSA + "\n";
		output += "ratio moyen LPT = " + meanRatioLPT + "\n";
		output += "ratio moyen MyAlgo = " + meanRatioMyAlgo + "\n";
		return output;
	}

	public static void displayHelp() {
		System.out.println(""
				+ "=======================================\n"
				+ "-- Aide a l'utilisation du programme --\n"
				+ "=======================================\n"
				+ "Objectif : Calculer une duree minimale pour l'execution de N taches sur M machines\n"
				+ "Auteurs : VENTE Maxime et HARTLEY Marc\n"
				+ "Usage: \n"
				+ "  Classique   : minmakespan <options>\n"
				+ "  Random      : minmakespan -random <-m=M> <-n=N> <-k=K> <-min=MIN> <-max=MAX>\n"
				+ "  Interactive : minmakespan -i\n"
				+ "  Fichier     : minmakespan <-fichier=CHEMIN>\n"
				+ "  Im-type     : minmakespan -Im <-m=M>\n"
				+ "  Aide        : minmakespan -h\n"
				+ "Options possible :\n"
				+ "  -h -help\t\t: Affiche cette page d'aide\n"
				+ "  -m -machines\t\t: nombre de machines a utiliser\n"
				+ "  -n -t -taches -tasks\t: nombre de taches\n"
				+ "  -k -iter -iterations\t: nombre d'iterations a executer\n"
				+ "  -min\t\t\t: duree minimale d'une tache\n"
				+ "  -max\t\t\t: duree maximale d'une tache\n"
				+ "  -random -r\t\t: utilisation du mode aleatoire\n"
				+ "  -f -fichier\t\t: chemin vers le fichier utilise pour appliquer les calculs\n"
				+ "  -i -interactive\t: mode interactif utilise\n"
				+ "  -Im\t\t\t: utilisation d'une instance type Im\n"
				+ "");
	}

	public static void mainMenu() throws IOException {
		System.out.println("============================");
		System.out.println("--      MIN-MAKESPAN      --");
		System.out.println("============================");
		System.out.println("");
		System.out.println("1. Donner l'instance depuis un fichier");
		System.out.println("2. Donner l'instance depuis le clavier");
		System.out.println("3. Generer une instance de type Im");
		System.out.println("4. Generer des instances aleatoires");
		System.out.println("5. Quitter");
		
		int choix = (int) numericInput("Faites votre choix", 5, 1);
		ArrayList<Float> data = new ArrayList<Float>();
		switch(choix) {
		case 1:
			try {
				data = getFromFile();
			} catch (IOException e) {
				System.out.println("Une erreur s'est produite avec ce fichier...");
				mainMenu();
			}
			break;
		case 2:
			data = getFromUser();
			break;
		case 3:
			data = getIm();
			break;
		case 4:
			break;
		case 5:
			return;
		}

		if(choix != 4) {
			int m = data.get(0).intValue();
			int n = data.get(1).intValue();
			float[] fTasks = new float[n];
			
			for(int i = 0; i < n; i++) {
				fTasks[i] = data.get(i + 2);
			}
			
			System.out.println(displayResults(m, n, fTasks, doAlgos(m, n, fTasks)));
		} else {
			ArrayList< ArrayList< Float > > datas = getRandom();
			int m = datas.get(0).get(0).intValue();
			int n = datas.get(0).get(1).intValue();
			int k = datas.size();
			float[][] fTasks = new float[k][n];
			
			float[][][] algoResults = new float[k][3][n];
			for(int i = 0; i < k; i++) {
				for(int j = 0; j < n; j ++) {
					fTasks[i][j] = datas.get(i).get(j +2);
				}
				algoResults[i] = doAlgos(m, n, fTasks[i]);
			}
			System.out.println(displayResultsFromRandom(m, n, fTasks, algoResults));
		}
		mainMenu();
	}
	
}
