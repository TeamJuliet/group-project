package uk.ac.cam.cl.intelligentgamedesigner.testing;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.ScalarCombinedMetric;
import uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.ScalarGameMetric;
import uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.ScalarGamePotential;

/**
 * 
 * Simple tests that check the order of the scalar metrics is correct.
 *
 */
public class ScalarMetricsTests {

    @Test
    public void testScalarGameMetricOrder() {
        ScalarGameMetric metric1 = new ScalarGameMetric(1.0);
        ScalarGameMetric metric2 = new ScalarGameMetric(2.0);
        ScalarGameMetric metric3 = new ScalarGameMetric(3.0);
        
        List<ScalarGameMetric> metrics = new ArrayList<ScalarGameMetric>();
        metrics.add(metric1);
        metrics.add(metric2);
        metrics.add(metric3);

        Collections.sort(metrics);

        assertTrue(metrics.get(0).score == 1.0);
        assertTrue(metrics.get(1).score == 2.0);
        assertTrue(metrics.get(2).score == 3.0);
    }

    @Test
    public void testScalarGamePotentialOrder() {
        ScalarGamePotential metric1 = new ScalarGamePotential(1.0);
        ScalarGamePotential metric2 = new ScalarGamePotential(2.0);
        ScalarGamePotential metric3 = new ScalarGamePotential(3.0);
        
        List<ScalarGamePotential> metrics = new ArrayList<ScalarGamePotential>();
        metrics.add(metric1);
        metrics.add(metric2);
        metrics.add(metric3);

        Collections.sort(metrics);

        assertTrue(metrics.get(0).score == 1.0);
        assertTrue(metrics.get(1).score == 2.0);
        assertTrue(metrics.get(2).score == 3.0);
    }
    
    @Test
    public void testScalarCombinedMetricOrder() {
        ScalarCombinedMetric metric1 = new ScalarCombinedMetric(1.0);
        ScalarCombinedMetric metric2 = new ScalarCombinedMetric(2.0);
        ScalarCombinedMetric metric3 = new ScalarCombinedMetric(3.0);
        
        List<ScalarCombinedMetric> metrics = new ArrayList<ScalarCombinedMetric>();
        metrics.add(metric1);
        metrics.add(metric2);
        metrics.add(metric3);

        Collections.sort(metrics);

        assertTrue(metrics.get(0).score == 1.0);
        assertTrue(metrics.get(1).score == 2.0);
        assertTrue(metrics.get(2).score == 3.0);
    }
    
}
