package edu.utsa.fileflow;

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

/**
 * This class is a temporary main class for the development of the Control Flow
 * Graph implementation for the File Flow Analysis project.
 * 
 * @author Rodney Rodriguez
 *
 */
public class Main2 {

	static final String TEST_SCRIPT = "scripts/ffa/script1.ffa";

	public static void main(String[] args) throws FileNotFoundException, IOException {
		CharStream input = new ANTLRInputStream(new FileInputStream(TEST_SCRIPT));
		TokenStream tokens = new CommonTokenStream(new FileFlowLexer(input));
		FileFlowParser parser = new FileFlowParser(tokens);
		ParseTree tree = parser.prog();
		FileFlowListenerImpl listener = new FileFlowListenerImpl();
		ParseTreeWalker.DEFAULT.walk(listener, tree);

		listener.cfg.print();
	}
}
