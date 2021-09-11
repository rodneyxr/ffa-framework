package edu.utsa.fileflow.cfg;

import edu.utsa.fileflow.utilities.FileFlowHelper;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * This class tests the functionality of the FlowPoint class.
 *
 * @author Rodney Rodriguez
 */
public class FlowPointTest {

    @Test
    public void testAddFlowPoint() throws Exception {
        FlowPoint flowpoint1 = new FlowPoint("fp1");
        FlowPoint flowpoint2 = new FlowPoint("fp2");

        flowpoint1.addFlowPoint(flowpoint2);
        assertSame(flowpoint1.getOutgoingEdgeList().getEdge(0).getTarget(), flowpoint2, "flowpoint1's outgoing target should be flowpoint2");
        assertSame(flowpoint2.getIncomingEdgeList().getEdge(0).getSource(), flowpoint1, "flowpoint2's incoming source should be flowpoint1");
    }

    /**
     * Make sure that FlowPoint.getAllFlowPoints() does not modify the graph in
     * any way.
     *
     * @throws Exception
     */
    @Test
    public void testGetAllFlowPoints() throws Exception {
        String test1 = "scripts/tests/cfg/test1.ffa";
        FlowPoint cfg = FileFlowHelper.generateControlFlowGraphFromFile(new File(test1));
        int initialSize = cfg.getAllFlowPoints().size();
        int secondSize = cfg.getAllFlowPoints().size();
        assertEquals(initialSize, secondSize, "getAllFlowPoints() should not modify the CFG");
    }

}
