package edu.utsa.fileflow.utilities;

import edu.utsa.fileflow.analysis.AnalysisException;
import edu.utsa.fileflow.antlr.FileFlowLexer;
import edu.utsa.fileflow.antlr.FileFlowParser;
import edu.utsa.fileflow.cfg.FileFlowListenerImpl;
import edu.utsa.fileflow.cfg.FlowPoint;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.*;

public class FileFlowHelper {

    /**
     * @param file The .ffa source file.
     * @return The flow point representing the root of the control flow graph
     * for the .ffa file passed in.
     * @throws IOException
     * @throws FileNotFoundException
     */
    public static FlowPoint generateControlFlowGraphFromFile(File file) throws AnalysisException, IOException {
        return generateCFG(new FileInputStream(file));
    }

    /**
     * @param script The source code represented by a String object.
     * @return The flow point representing the root of the control flow graph
     * for the .ffa file passed in.
     * @throws IOException
     * @throws FileNotFoundException
     */
    public static FlowPoint generateControlFlowGraphFromScript(String script)
            throws Exception {
        return generateCFG(new ByteArrayInputStream(script.getBytes()));
    }

    private static FlowPoint generateCFG(InputStream inputStream) throws AnalysisException, IOException {
        CharStream input = new ANTLRInputStream(inputStream);
        TokenStream tokens = new CommonTokenStream(new FileFlowLexer(input));
        FileFlowParser parser = new FileFlowParser(tokens);
        ParseTree tree = parser.prog();
        if (parser.getNumberOfSyntaxErrors() > 0) {
            throw new AnalysisException("ffa syntax error");
        }
        FileFlowListenerImpl listener = new FileFlowListenerImpl();
        ParseTreeWalker.DEFAULT.walk(listener, tree);
        return listener.getControlFlowGraph();
    }

}
