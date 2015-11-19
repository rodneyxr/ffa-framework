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

import edu.utsa.fileflow.antlr.FileFlowBaseListener;
import edu.utsa.fileflow.antlr.FileFlowLexer;
import edu.utsa.fileflow.antlr.FileFlowListener;
import edu.utsa.fileflow.antlr.FileFlowParser;

public class Main2 {

	static final String TEST_SCRIPT = "scripts/ffa/test.ffa";

	public static void main(String[] args) throws FileNotFoundException, IOException {
		CharStream input = new ANTLRInputStream(new FileInputStream(TEST_SCRIPT));
		TokenStream tokens = new CommonTokenStream(new FileFlowLexer(input));
		FileFlowParser parser = new FileFlowParser(tokens);
		ParseTree tree = parser.prog();
		ParseTreeWalker walker = new ParseTreeWalker();
		FileFlowListener listener = new FileFlowListener1();
		ParseTreeWalker.DEFAULT.walk(listener, tree);
		// System.out.println(listener);

	}

}

class FileFlowListener1 extends FileFlowBaseListener {
	@Override
	public void enterBlock(FileFlowParser.BlockContext ctx) {
		System.out.println(ctx.getText());
	}
}
