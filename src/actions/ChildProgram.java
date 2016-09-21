package actions;

import org.jgap.InvalidConfigurationException;
import org.jgap.gp.CommandGene;
import org.jgap.gp.IGPProgram;
import org.jgap.gp.impl.GPConfiguration;
import org.jgap.gp.impl.ProgramChromosome;

public class ChildProgram extends CommandGene {
	
	private static final long serialVersionUID = -7294290859058107682L;
	int arity;

	public ChildProgram(GPConfiguration a_conf, int a_arity) throws InvalidConfigurationException {
		super(a_conf, a_arity, VoidClass);
		arity = a_arity;
	}
	
	@Override
	public Class getChildType(IGPProgram a_ind, int a_chromNum) {
		return VoidClass;
	}
	
	@Override
	public boolean isValid(ProgramChromosome a_program) {
		return true;
	}
	
	@Override
	public Object execute_object(ProgramChromosome c, int n, Object[] args) {
		String str = "";
		for (int i = 0; i < arity; ++i) {
			Object abc = c.execute_object(n, i, args);
			str += abc.toString();
		}
		return " { " + str + " } "; 
	}
	
	@Override
	public String toString() {
		String str = "";
		for (int i = 1; i < arity + 1; ++i) {
			str += " &" + i + " ";
		}
		return "{"+str+"}";
	}

}

