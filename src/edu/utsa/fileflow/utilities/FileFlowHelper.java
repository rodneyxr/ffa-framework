package edu.utsa.fileflow.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import edu.utsa.fileflow.antlr.FileFlowLexer;
import edu.utsa.fileflow.antlr.FileFlowParser;
import edu.utsa.fileflow.cfg.FileFlowListenerImpl;
import edu.utsa.fileflow.cfg.FlowPoint;

public class FileFlowHelper {

	/**
	 * @param file
	 *            The .ffa source file.
	 * @return The flow point representing the root of the control flow graph
	 *         for the .ffa file passed in.
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static FlowPoint generateControlFlowGraphFromScript(File file) throws FileNotFoundException, IOException {
		CharStream input = new ANTLRInputStream(new FileInputStream(file));
		TokenStream tokens = new CommonTokenStream(new FileFlowLexer(input));
		FileFlowParser parser = new FileFlowParser(tokens);
		ParseTree tree = parser.prog();
		FileFlowListenerImpl listener = new FileFlowListenerImpl();
		ParseTreeWalker.DEFAULT.walk(listener, tree);
		return listener.getControlFlowGraph();
	}

}
