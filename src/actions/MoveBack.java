package actions;

import org.jgap.InvalidConfigurationException;
import org.jgap.gp.CommandGene;
import org.jgap.gp.IGPProgram;
import org.jgap.gp.impl.GPConfiguration;
import org.jgap.gp.impl.ProgramChromosome;

public class MoveBack extends CommandGene {

	public MoveBack(GPConfiguration a_conf) throws InvalidConfigurationException {
		super(a_conf, 1, VoidClass);
	}
	
	@Override
	public Class getChildType(IGPProgram a_ind, int a_chromNum) {
		return DoubleClass;
	}

	@Override
	public Object execute_object(ProgramChromosome c, int n, Object[] args) {
		return "back(" + c.execute_object(n, 0, args) + ");";
	}
	
	@Override
	public String toString() {
		return "back(&1);";
	}
}

