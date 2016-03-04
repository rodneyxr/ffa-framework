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
import edu.utsa.fileflow.cfg.FlowPoint;

public class Main2 {

	static final String TEST_SCRIPT = "scripts/test.ffa";

	public static void main(String... args) throws FileNotFoundException, IOException {
		CharStream input = new ANTLRInputStream(new FileInputStream(TEST_SCRIPT));
		TokenStream tokens = new CommonTokenStream(new FileFlowLexer(input));
		FileFlowParser parser = new FileFlowParser(tokens);
		ParseTree tree = parser.prog();
		FileFlowListenerImpl listener = new FileFlowListenerImpl();
		ParseTreeWalker.DEFAULT.walk(listener, tree);

		FlowPoint cfg = listener.cfg;
		cfg.print();
	}

}
