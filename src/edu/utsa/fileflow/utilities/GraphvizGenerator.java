package edu.utsa.fileflow.utilities;

import edu.utsa.fileflow.cfg.FlowPoint;
import edu.utsa.fileflow.cfg.FlowPointEdge;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.nio.file.Paths;
import java.util.ArrayList;

public class GraphvizGenerator {
    public static boolean SAVE_AS_UML;
    public static String PATH_PREFIX = "";

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
            dot.append(fp.id);
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
                dot.append(fp.id);
                dot.append(" -> ");
                dot.append(edge.getTarget().id);
                dot.append(";\n");
            }
        }

        dot.append("}");
        return dot.toString();
    }

    /**
     * Creates a DOT file within the dot/ directory for testing purposes. The
     * {@code filepath} is relative to the 'dot/' directory.
     *
     * @param dot      A String representing the dot file.
     * @param filepath The file that the dot file should be created in. This file is
     *                 relative to the dot directory.
     */
    public static void saveDOTToFile(String dot, String filepath) {
        if (SAVE_AS_UML) {
            dot = "@startuml\n" + dot + "\n@enduml";
        }
        if (PATH_PREFIX.length() > 0) {
            filepath = Paths.get(PATH_PREFIX, filepath).toString();
        }
        File dotFilepath = Paths.get("dot", filepath).toFile();
        dotFilepath.getParentFile().mkdirs();
        try (PrintStream ps = new PrintStream(dotFilepath)) {
            ps.println(dot);
        } catch (FileNotFoundException e) {
            System.err.println(e);
        }
    }
}
