package br.uece.ctmc.bdp;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.StringUtils;

public class Main {
	
	private final NumberFormat numberFormatter;
	
	private final BirthDeathProcess birthDeathProcess;
	private final BirthDeathProcessWalkSimulator simulator;

	private final List<Integer> takenPath;
	
	public Main(int states, int stages, double lambda, double mu) {
		this.numberFormatter = new DecimalFormat("##.###");
		
		this.birthDeathProcess = BirthDeathProcessBuilder.buildHomogeneousBirthDeathProcess(states, lambda, mu);
		this.simulator = new BirthDeathProcessWalkSimulator(birthDeathProcess);
		
		this.takenPath = this.simulator.walk(stages);
	}
	
	public void run() {
		System.out.println("Q:");
		printMatrix(birthDeathProcess.getRateMatrix());
		
		System.out.println();
		
		System.out.println("P:");
		printMatrix(birthDeathProcess.getDiscreteMarkovianMatrix());
		
		System.out.println();
		
		System.out.println("D:");
		printArray(birthDeathProcess.getStationaryDistribution());
		
		System.out.println();
		
		System.out.println("Path:");
		System.out.println(takenPath);
	}
	
	public void printMatrix(double[][] values) {
		for (double[] valuesArray : values) {
			printArray(valuesArray);
		}
	}
	
	public void printArray(double[] values) {
		for (double value : values) {
			String formattedValue = numberFormatter.format(value);
			System.out.print(StringUtils.rightPad(formattedValue, 7));
		}
		
		System.out.println();
	}
	
	public static void main(String[] args) {
		Options options = new Options();
		options.addOption("s", true, "states count");
		options.addOption("n", true, "stages to simulate");
		options.addOption("l", true, "lambda");
		options.addOption("m", true, "mu");
		
		try {
			parse(new DefaultParser().parse(options, args));
		}
		catch (ParseException e) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(" ", options);
		}
	}
	
	public static void parse(CommandLine commandLine) throws ParseException, MissingArgumentException  {
		int states, stages;
		double lambda, mu;
		
		if (commandLine.hasOption("s")) {
			states = Integer.parseInt(commandLine.getOptionValue("s"));
		}
		else {
			states = 3;
		}
		
		if (commandLine.hasOption("n")) {
			stages = Integer.parseInt(commandLine.getOptionValue("n"));
		} else {
			throw new MissingArgumentException("The parameter 'n' is unset");
		}

		if (commandLine.hasOption("l")) {
			lambda = Double.parseDouble(commandLine.getOptionValue("l"));
		} else {
			throw new MissingArgumentException("The parameter 'l' is unset");
		}

		if (commandLine.hasOption("m")) {
			mu = Double.parseDouble(commandLine.getOptionValue("m"));
		} else {
			throw new MissingArgumentException("The parameter 'm' is unset");
		}
		
		new Main(states, stages, lambda, mu).run();
	}

}
