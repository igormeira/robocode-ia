package main;

import org.apache.log4j.BasicConfigurator;

import org.jgap.InvalidConfigurationException;
import org.jgap.gp.CommandGene;
import org.jgap.gp.GPProblem;
import org.jgap.gp.impl.DefaultGPFitnessEvaluator;
import org.jgap.gp.impl.GPConfiguration;
import org.jgap.gp.impl.GPGenotype;
import org.jgap.gp.terminal.Terminal;

import actions.Add;
import actions.ChildProgram;
import actions.MoveBack;
import actions.MoveFoward;
import actions.Multiply;
import actions.Shoot;

import util.Fitness;

public class MainClass extends GPProblem {
	
    public MainClass() throws InvalidConfigurationException {
        super(new GPConfiguration());

        GPConfiguration config = getGPConfiguration();

        config.setGPFitnessEvaluator(new DefaultGPFitnessEvaluator());
        config.setMaxInitDepth(6);
        config.setPopulationSize(10);
        config.setMaxCrossoverDepth(6);
        config.setFitnessFunction(new Fitness());
    }

    @Override
    public GPGenotype create() throws InvalidConfigurationException {
        GPConfiguration config = getGPConfiguration();

        Class[] types = {
        		CommandGene.VoidClass,
        		CommandGene.VoidClass,
        		CommandGene.VoidClass,
        		CommandGene.VoidClass,
		};

        Class[][] argTypes = {
        		{},
        		{},
        		{},
        		{},
		};

        CommandGene[][] nodeSets = {
            {
            	new ChildProgram(config, 2),
            	new ChildProgram(config, 3),
            	new ChildProgram(config, 4),
            	new ChildProgram(config, 5),

            	new Terminal(config, CommandGene.DoubleClass, 0d, 1d),
            	new Terminal(config, CommandGene.DoubleClass, 1d, 2d),
            	
            	// dimensions of the screen, mostly for ahead and back methods 
            	//new Terminal(config, CommandGene.DoubleClass, 0d, 800d),
            	//new Terminal(config, CommandGene.DoubleClass, 0d, 600d),
            	
            	new Add(config),
                new Multiply(config),
                new Shoot(config),
                new MoveFoward(config),
                new MoveBack(config),
            },
            {
            	new Terminal(config, CommandGene.DoubleClass, 0d, 1d),
            	new Terminal(config, CommandGene.DoubleClass, 1d, 2d),
            	
            	new Add(config),
                new Multiply(config),
                new Shoot(config),
                new MoveFoward(config),
                new MoveBack(config),
            },
            {
            	new Terminal(config, CommandGene.DoubleClass, 0d, 1d),
            	new Terminal(config, CommandGene.DoubleClass, 1d, 2d),
            	
            	new Add(config),
                new Multiply(config),
                new Shoot(config),
                new MoveFoward(config),
                new MoveBack(config),
            },
            {
            	new Terminal(config, CommandGene.DoubleClass, 0d, 1d),
            	new Terminal(config, CommandGene.DoubleClass, 1d, 2d),
            	
            	new Add(config),
                new Multiply(config),
                new Shoot(config),
                new MoveFoward(config),
                new MoveBack(config),
            }
        };

        GPGenotype result = GPGenotype.randomInitialGenotype(config, types, argTypes,
                nodeSets, 20, true);

        return result;
    }

    public static void main(String[] args) throws Exception {
    	GPProblem problem = new MainClass();

        BasicConfigurator.configure();
        
        GPGenotype gp = problem.create();
        gp.setVerboseOutput(false);
        gp.evolve(20);

        gp.outputSolution(gp.getAllTimeBest());
        Fitness.createRobotFromChromossome(gp.getAllTimeBest());
    }
}
