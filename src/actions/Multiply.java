package actions;

import org.jgap.InvalidConfigurationException;
import org.jgap.gp.CommandGene;
import org.jgap.gp.impl.GPConfiguration;
import org.jgap.gp.impl.ProgramChromosome;

public class Multiply extends CommandGene {

	public Multiply(GPConfiguration config) throws InvalidConfigurationException {
		super(config, 2, DoubleClass);
	}
	
	@Override
	public Object execute_object(ProgramChromosome c, int n, Object[] args) {
		return c.execute_object(n, 0, args) + "*" + c.execute_object(n, 1, args);
	}
	
	@Override
	public String toString() {
		return "(&1 * &2)";
	}
}

