package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;

import javax.tools.*;

import org.jgap.gp.*;

import robocode.*;
import robocode.control.*;
import robocode.control.events.*;

public class Fitness extends GPFitnessFunction {

	private static final long serialVersionUID = 5616535457545157300L;
	protected double fitness;
	
	static final String ROBOCODE_DIR = "C:/robocode";
	static final String ROBOTS_DIR = ROBOCODE_DIR + "/robots";
	static final String ROBOT_FILE = ROBOTS_DIR + "/ufcg/Detonator.java";
	static final String ROBOT_NAME = "ufcg.Detonator";
	static final String ROBOT_NAME_COMPOSED = "ufcg.Detonator*";

	@Override
	protected double evaluate(IGPProgram a_subject) {
		final RobocodeEngine engine;
		final BattlefieldSpecification battlefield;
		createRobotFromChromossome(a_subject);

		engine = new RobocodeEngine(new File(ROBOCODE_DIR));
		engine.setVisible(false);
		engine.addBattleListener(new BattleAdaptor() {
			double sum, score;
			public void onBattleCompleted(final BattleCompletedEvent e) {
				BattleResults[] list = e.getIndexedResults();
				assert list.length == 2;
				
				sum = Arrays.stream(list)
						.mapToInt(a -> a.getScore())
						.sum();
				
				for (BattleResults br: list) {
					System.out.println(br.getTeamLeaderName());
					if (br.getTeamLeaderName().startsWith(ROBOT_NAME))
						score = br.getScore();
				}
				fitness = score/sum;
			}
		});
		battlefield = new BattlefieldSpecification(800, 600);
		final RobotSpecification[] selectedRobots = engine.getLocalRepository(String.join(",", "sample.SpinBot", ROBOT_NAME_COMPOSED));
		final BattleSpecification battleSpec = new BattleSpecification(15, battlefield, selectedRobots);
		
		System.out.println("beginning battle...");
		
		engine.runBattle(battleSpec, true);
		engine.close();
		
		System.out.println("battle complete");
		
		System.out.println("fitness: " + fitness);
		return Math.abs(fitness);
	}

	public static void createRobotFromChromossome(IGPProgram a_subject) {
		System.out.println("--------------------------------");
		System.out.println("compiling...");
		String sourceCode = String.join("\n",
				"package ufcg;",
				"import robocode.*;",
				"import java.util.*;",
				"public class Detonator extends AdvancedRobot {",
				"  static double enemyFirePower;",
				"  public void run() {",
				"    while(true) {",
				"      setTurnRight(10000);",
				"      setMaxVelocity(5);",
				"      ahead(10000);",
				"    }",
				"  }",
				"  public void onScannedRobot(ScannedRobotEvent e) {",
				"    %s",
				"  }",
				"  public void onHitByBullet(HitByBulletEvent e) {",
				"    %s",
				"  }",
				"  public void onHitWall(HitWallEvent e) {",
				"    %s",
				"  }",
				"  public void onHitRobot(HitRobotEvent e) {",
				"    %s",
				"  }",
				"}"
		);
		
		String scannedCode = "", bulletCode = "", wallCode = "", hitCode = "";
		
		scannedCode = a_subject.execute_object(0, new Object[]{}).toString();
		
		bulletCode = a_subject.execute_object(1, new Object[]{}).toString();
		
		wallCode = a_subject.execute_object(2, new Object[]{}).toString();
		
		hitCode = a_subject.execute_object(3, new Object[]{}).toString();
		
		System.out.println("generated code (onScannedRobot): " + scannedCode);
		System.out.println("generated code (onHitBullet): " + bulletCode);
		System.out.println("generated code (onHitWall): " + wallCode);
		System.out.println("generated code (onHitRobot): " + hitCode);
		
		try {
			FileWriter fstream = new FileWriter(ROBOT_FILE);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(String.format(sourceCode, scannedCode, bulletCode, wallCode, hitCode));
			out.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		
		DiagnosticListener<JavaFileObject> diagnosticListener = new DiagnosticCollector<JavaFileObject>();
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnosticListener, null, null);
		
		Iterable<String> options = Arrays.asList("-d", ROBOTS_DIR);
		Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjects(ROBOT_FILE);
		
		boolean result = compiler.getTask(null, fileManager, diagnosticListener, options, null, compilationUnits).call();
		
        if (result) {
            System.out.println("compilation done");
        } else {
        	System.err.println("compilation failed");
        }
	}

}
