package edu.utsa.fileflow.analysis;

import edu.utsa.fileflow.cfg.FlowPoint;
import edu.utsa.fileflow.cfg.FlowPointContext;
import edu.utsa.fileflow.cfg.FlowPointContextType;
import edu.utsa.fileflow.cfg.FlowPointEdge;

import java.util.Stack;

public class Analyzer<D extends AnalysisDomain<D>, A extends Analysis<D>> {

    public static boolean VERBOSE = true;
    public static boolean CONTINUE_ON_ERROR = false;

    private D domain;
    private A analysis;
    private D exitDomain;

    public Analyzer(D domain, A analysis) {
        this.domain = domain;
        this.analysis = analysis;
        if (domain.bottom() == null)
            throw new RuntimeException("Analysis Error: " + domain.getClass().getSimpleName() + ".bottom() cannot return null.");
        if (domain.clone() == null)
            throw new RuntimeException("Analysis Error: " + domain.getClass().getSimpleName() + ".clone() cannot return null.");
    }

    /**
     * @param cfg The FlowPoint that represents the start of the control flow
     *            graph.
     * @return the result of the AnalysisDomain after Analysis.onFinish(). This
     * can also be thought of as the result of the domain at time of
     * program exits.
     */
    @SuppressWarnings("unchecked")
    public D analyze(FlowPoint cfg) throws AnalysisException {

        // initialize all nodes to bottom
        cfg.getAllFlowPoints().forEach(fp -> fp.setDomain(domain.bottom()));
        analysis.onBegin(domain, cfg);

        // initialize the workset
        Stack<FlowPoint> workset = new Stack<>();

        // add the start node to the workset
        workset.add(cfg);

        while (!workset.isEmpty()) {
            FlowPoint flowpoint = workset.pop();

            // add all children to the workset
            for (FlowPointEdge edge : flowpoint.getOutgoingEdgeList()) {
                FlowPoint child = edge.getTarget();
                // for each outgoing edge, compute y (new domain)
                // then check if y is different from the old domain
                // if so, update domain and target to workset
                if (VERBOSE)
                    System.out.printf("(%s.java): %s => %s\n", Analyzer.class.getSimpleName(), flowpoint, child);
                D y = updateAnalysis(flowpoint, child);
                if (y.compareTo((D) child.getDomain(y.getClass())) != 0 || !child.getAnalyzed()) {
                    child.setDomain(y);
                    child.setAnalyzed(true);
                    workset.add(child);
                }
            }
        }

        // this is null because analysis is not reaching prog exit
        if (exitDomain != null)
            analysis.onFinish(exitDomain);
        else
            System.out.println("Analysis Warning: Analysis did not reach PROG_EXIT.");

        // reset cfg flow points for next analysis
        cfg.reset();

        return exitDomain;
    }

    /**
     * This method is called each iteration of the fixed point
     * algorithm. It represents a visit to a single flow point in the control
     * flow graph.
     *
     * @param source The source {@link FlowPoint} that the transfer function
     *               originates from. A clone of this {@link FlowPoint} will be
     *               created and {@code source} will not be modified.
     * @param target The target the {@link FlowPoint} that will be visited.
     * @return a modified clone of the input domain.
     */
    @SuppressWarnings("unchecked")
    private D updateAnalysis(final FlowPoint source, final FlowPoint target) throws AnalysisException {
        FlowPointContext fpctx = target.getContext();
        FlowPointContextType type = fpctx.getType();

        // merge previous parent flow points before visiting
        D originalDomain = mergeParents((D) source.getDomain(domain.getClass()), source, target);
        target.setOriginalDomain(originalDomain);

        D inputDomain = originalDomain.clone();

        try {
            // call this method before visiting the flow point
            analysis.onBefore(inputDomain, fpctx);

            // visit the node depending on its type
            switch (type) {
                case ProgEnter:
                    analysis.enterProg(inputDomain, fpctx);
                    break;
                case ProgExit:
                    exitDomain = analysis.exitProg(inputDomain, fpctx);
                    break;
                case FunctionCall:
                    if (fpctx.getText().startsWith("touch"))
                        analysis.touch(inputDomain, fpctx);
                    else if (fpctx.getText().startsWith("mkdir"))
                        analysis.mkdir(inputDomain, fpctx);
                    else if (fpctx.getText().startsWith("rmr"))
                        analysis.removeRecursive(inputDomain, fpctx);
                    else if (fpctx.getText().startsWith("rm"))
                        analysis.remove(inputDomain, fpctx);
                    else if (fpctx.getText().startsWith("cp"))
                        analysis.copy(inputDomain, fpctx);
                    else if (fpctx.getText().startsWith("cd"))
                        analysis.cd(inputDomain, fpctx);
                    else if (fpctx.getText().startsWith("assert"))
                        analysis.assertFunc(inputDomain, fpctx);
                    break;
                case WhileStatement:
                    analysis.enterWhileStatement(inputDomain, fpctx);
                    break;
                case IfStat:
                    // TODO: implement exitIfStat
                    analysis.enterIfStat(inputDomain, fpctx);
                    break;
                case ElseIfStat:
                    analysis.enterElseIfStat(inputDomain, fpctx);
                    break;
                case ElseStat:
                    analysis.enterElseStat(inputDomain, fpctx);
                    break;
                case Assignment:
                    analysis.enterAssignment(inputDomain, fpctx);
                    break;
                case FlowPoint:
                    if (fpctx.getText().equals("EXIT_WHILE")) {
                        // TODO: make exitWhile enum
                        analysis.exitWhileStatement(inputDomain, fpctx);
                    }
                    break;
                default:
                    throw new AnalysisException(getClass().getSimpleName() + ".java: Not implemented: " + target);
            }
            // call this method after visiting the flow point
            analysis.onAfter(inputDomain, fpctx);
        } catch (AnalysisException ae) {
            if (CONTINUE_ON_ERROR) {
                System.out.println(ae.getMessage());
            } else {
                throw ae;
            }
        }

        return inputDomain;
    }

    /**
     * Merges all parent domains of {@code target} into {@code inputDomain}.
     *
     * @param inputDomain The domain from the cloned {@code source}. All parents'
     *                    domains of {@code target} should be merged into this domain
     *                    (except for the parent that {@code source} was cloned from).
     * @param source      The original {@link FlowPoint} that the transfer function
     *                    originates from.
     * @param target      The {@link FlowPoint} that all previous flow points' domains
     *                    should be merged into eventually.
     * @return the modified {@code inputDomain}
     */
    @SuppressWarnings("unchecked")
    private D mergeParents(D inputDomain, final FlowPoint source, final FlowPoint target) {
        target.getIncomingEdgeList().forEach((e) -> {
            D domain = (D) e.getSource().getDomain(inputDomain.getClass());
            if (e.getSource() != source)
                inputDomain.merge(domain);
        });
        return inputDomain;
    }

}
