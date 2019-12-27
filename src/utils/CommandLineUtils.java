package utils;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.ArrayUtils;

public class CommandLineUtils {

	public static float numericInput() {
		return numericInput("");
	}
	public static float numericInput(String prompt) {
		return numericInput(prompt, Integer.MAX_VALUE);
	}
	public static float numericInput(String prompt, int maxValue) {
		return numericInput(prompt, maxValue, Integer.MIN_VALUE);
	} 
	public static float numericInput(String prompt, int maxValue, int minValue) {
		try {
			Scanner input = new Scanner(System.in);
			if(prompt != "")
				System.out.println(prompt);
			System.out.print("> ");
			float number = input.nextFloat();
			boolean ok = false;
			if(number > maxValue) {
				System.out.println("Merci de donner une réponse inférieure à " + maxValue);
			}
			else if(number < minValue) {
				System.out.println("Merci de donner une réponse supérieure à " + minValue);
			}
			else {
				ok = true;
			}
//			input.close();
			if(ok) 
				return number;
			return numericInput("", maxValue, minValue);
		} catch (InputMismatchException e) {
			System.out.println("Merci de donner une réponse numérique");
			return numericInput("", maxValue, minValue);
		}
	}

	public static Integer getIntOption(String[] names, String[] args) {
		return (getOption(names, args) != null ? Integer.parseInt(getOption(names, args)) : null);
	}
	public static Integer getIntOption(String name, String[] args) {
		return (getOption(name, args) != null ? Integer.parseInt(getOption(name, args)) : null);
	}
	public static Float getFloatOption(String[] names, String[] args) {
		return (getOption(names, args) != null ? Float.parseFloat(getOption(names, args)) : null);
	}
	public static Float getFloatOption(String name, String[] args) {
		return (getOption(name, args) != null ? Float.parseFloat(getOption(name, args)) : null);
	}
	public static String getOption(String[] names, String[] args) {
		for(String name : names) {
			String result = getOption(name, args);
			if(result != null)
				return result;
		}
		return null;
	}
	public static String getOption(String name, String[] args) {
		for(String opt : args) {
			if(opt.length() < name.length()) continue;
			if(opt.equals(name) || opt.equals("-" + name)) return "1";
			if(opt.length() > name.length()+1 && opt.substring(0, name.length()).equals(name + "=")) return opt.substring(opt.indexOf("=")+1);
			if(opt.length() > name.length()+2 && opt.substring(0, name.length()+2).equals("-" + name + "=")) return opt.substring(opt.indexOf("=")+1);
		}
		return null;
	}
	public static float[] getNumericalOptions(String[] args) {
		ArrayList<Float> options = new ArrayList<Float>();
		for(String opt : args) {
			try {
				options.add(Float.parseFloat(opt));
			} catch (NumberFormatException e) {
				
			}
		}
		float[] floatArray = ArrayUtils.toPrimitive(options.toArray(new Float[0]), 0.0F);
		return floatArray;
	}

	public static List<String> readFileInList(String fileName) throws IOException 
	{ 
	    List<String> lines = Collections.emptyList(); 
	    lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8); 
	    return lines; 
	} 
	
}
