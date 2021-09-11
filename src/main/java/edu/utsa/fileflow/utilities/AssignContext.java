package edu.utsa.fileflow.utilities;

import edu.utsa.fileflow.analysis.AnalysisException;
import edu.utsa.fileflow.antlr.FileFlowParser;
import edu.utsa.fileflow.antlr.FileFlowParser.ArrayValueContext;
import edu.utsa.fileflow.antlr.FileFlowParser.AssignmentContext;
import edu.utsa.fileflow.antlr.FileFlowParser.ValueContext;
import edu.utsa.fileflow.antlr.FileFlowParser.VarValueContext;
import edu.utsa.fileflow.cfg.FlowPointContext;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 * This is a helper class to simplify working with the EnterAssignment FlowContext.
 */
public class AssignContext {
    public final String var0;
    public final String var1;
    public final String var2;
    public final String literal;
    public final boolean isConcat;
    public final boolean isArray;
    public final boolean isEmptyArray;
    public final boolean isInput;

    public AssignContext(FlowPointContext fpctx) throws AnalysisException {
        String $var0 = null;
        String $var1 = null;
        String $var2 = null;
        String $literal = null;
        boolean $isConcat = false;
        boolean $isArray;
        boolean $isEmptyArray;
        boolean $isInput;

        AssignmentContext ctx = (AssignmentContext) fpctx.getContext();
        VarValueContext var = ctx.varValue();
        ArrayValueContext arr = ctx.arrayValue();

        // true if variable on left side is an array
        $isArray = (ctx.Index() != null);

        FileFlowParser.ExpressionContext expr = null;
        TerminalNode input = null;
        TerminalNode emptyArray = null;

        // if assignment variable is array and has index
        if ($isArray) {
            if (arr.varValue() == null) {
                emptyArray = arr.EmptyValue();
            } else {
                expr = arr.varValue().expression();
                input = arr.varValue().Input();
            }
        } else {
            // if assignment is a regular variable with no index
            expr = var.expression();
            input = var.Input();
        }

        // variable name (key to update)
        String key = ctx.Variable().getText();
        if ($isArray)
            key = key + ctx.Index().getText();
        $var0 = key;

        // if array variable is assigned to empty array
        $isEmptyArray = emptyArray != null;

        // if user input is required
        $isInput = input != null;

        if (!$isEmptyArray && !$isInput) {
            // get the first term
            ValueContext term1 = expr.value(0);
            if (term1.Variable() != null) {
                // term1 is a variable
                String text = term1.Variable().getText();
                if (term1.Index() != null)
                    text += term1.Index().getText();
                $var1 = text;
            } else {
                // term1 is a $literal
                $literal = term1.String().getText();
            }

            // check for concatenation
            if (expr.value().size() == 2) {
                ValueContext term2 = expr.value(1);
                if (term2.Variable() != null) {
                    // term2 is a variable
                    String text = term2.Variable().getText();
                    if (term2.Index() != null)
                        text += term2.Index().getText();
                    $var2 = text;
                } else {
                    // term2 is a string
                    throw new AnalysisException("Error: '" + term2.String().getText() + "' cannot be a literal.");
                }

                // concatenate $var1 and $var2
                $isConcat = true;
            }
        }

        this.var0 = $var0;
        this.var1 = $var1;
        this.var2 = $var2;
        this.literal = $literal;
        this.isConcat = $isConcat;
        this.isArray = $isArray;
        this.isEmptyArray = $isEmptyArray;
        this.isInput = $isInput;
    }

}
