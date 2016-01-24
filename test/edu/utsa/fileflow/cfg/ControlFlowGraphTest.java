package edu.utsa.fileflow.cfg;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import edu.utsa.fileflow.antlr.FileFlowLexer;
import edu.utsa.fileflow.antlr.FileFlowParser;

/**
 * This class tests the functionality of the FlowPoint class.
 * 
 * @author Rodney Rodriguez
 *
 */
public class ControlFlowGraphTest {

	@Rule
	public ExpectedException expectException = ExpectedException.none();

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testControlFlowGraph() throws Exception {
		CharStream input = new ANTLRInputStream("touch 'a';touch 'b';if (other) {touch 'c';}");
		TokenStream tokens = new CommonTokenStream(new FileFlowLexer(input));
		FileFlowParser parser = new FileFlowParser(tokens);
		ParseTree tree = parser.prog();
		FileFlowListenerImpl listener = new FileFlowListenerImpl();
		ParseTreeWalker.DEFAULT.walk(listener, tree);

		// ControlFlowGraph cfg = listener.cfg;
		// cfg.print();
	}

}
