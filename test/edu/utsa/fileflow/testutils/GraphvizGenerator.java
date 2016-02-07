package edu.utsa.fileflow.testutils;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;

import edu.utsa.fileflow.cfg.FlowPoint;
import edu.utsa.fileflow.cfg.FlowPointEdge;

public class GraphvizGenerator {

	// Example:
	// digraph G {
	// ... 0 [label="CFG_ENTER"];
	// ... 1 [label="node 1"];
	// ... 2 [label="CFG_EXIT"];
	//
	// ... 0 -> 1;
	// ... 1 -> 2;
	// }
	public static String generateDOT(FlowPoint cfg) {
		ArrayList<FlowPoint> flowPoints = cfg.getAllFlowPoints();

		StringBuilder dot = new StringBuilder("digraph G {\n");

		// first define all nodes
		for (FlowPoint fp : flowPoints) {
			dot.append("    ");
			dot.append(fp.getID());
			dot.append("[label=\"");
			dot.append(fp.getContext().getText().replaceAll("\"", "\\\""));
			dot.append("\"");

			switch (fp.getContext().getType()) {
			case FlowPoint:
				dot.append(",shape=box");
				break;
			case ProgEnter:
				dot.append(",shape=mbox,fillcolor=green,style=filled");
				break;
			case ProgExit:
				dot.append(",shape=mbox,fillcolor=red,style=filled");
				break;
			case IfStat:
			case ElseIfStat:
				dot.append(",shape=diamond,fillcolor=yellow,style=filled");
				break;
			case ElseStat:
				dot.append(",shape=circle,fillcolor=yellow,style=filled");
				break;
			case WhileStatement:
				dot.append(",shape=diamond,fillcolor=orange,style=filled");
				break;
			default:
				dot.append(",shape=box,fillcolor=gray,style=filled");
				break;
			}

			dot.append("];\n");
		}

		dot.append("\n");

		// wire all parents to their children
		for (FlowPoint fp : flowPoints) {
			for (FlowPointEdge edge : fp.getOutgoingEdgeList()) {
				// fp -> child
				dot.append("    ");
				dot.append(fp.getID());
				dot.append(" -> ");
				dot.append(edge.getTarget().getID());
				dot.append(";\n");
			}
		}

		dot.append("}");
		return dot.toString();
	}

	// https://github.com/abstratt/eclipsegraphviz
	public static void saveDOTToFile(String dot, String filepath) {
		try (PrintStream ps = new PrintStream(filepath)) {
			ps.println(dot);
		} catch (FileNotFoundException e) {
			System.err.println(e);
		}
	}
}
