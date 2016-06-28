package edu.utsa.fileflow.cfg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import edu.utsa.fileflow.utilities.FileFlowHelper;

/**
 * This class tests the functionality of the FlowPoint class.
 * 
 * @author Rodney Rodriguez
 *
 */
public class FlowPointTest {

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
	public void testAddFlowPoint() throws Exception {
		FlowPoint flowpoint1 = new FlowPoint("fp1");
		FlowPoint flowpoint2 = new FlowPoint("fp2");

		flowpoint1.addFlowPoint(flowpoint2);
		assertTrue("flowpoint1's outgoing target should be flowpoint2",
				flowpoint1.getOutgoingEdgeList().getEdge(0).getTarget() == flowpoint2);
		assertTrue("flowpoint2's incoming source should be flowpoint1",
				flowpoint2.getIncomingEdgeList().getEdge(0).getSource() == flowpoint1);
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
		FlowPoint cfg = FileFlowHelper.generateControlFlowGraphFromScript(new File(test1));
		int initialSize = cfg.getAllFlowPoints().size();
		int secondSize = cfg.getAllFlowPoints().size();
		assertEquals("getAllFlowPoints() should not modify the CFG", initialSize, secondSize);
	}

}
