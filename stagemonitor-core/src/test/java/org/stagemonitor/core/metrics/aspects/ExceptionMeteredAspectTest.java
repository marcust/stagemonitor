package org.stagemonitor.core.metrics.aspects;

import static org.junit.Assert.assertEquals;

import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.annotation.ExceptionMetered;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.stagemonitor.core.Stagemonitor;

public class ExceptionMeteredAspectTest {

	private TestObject testObject = new TestObject();

	private static class TestObject {
		@ExceptionMetered
		private void exceptionMeteredDefault() {
			throw null;
		}

		@ExceptionMetered
		private void exceptionMeteredPrivate() {
			throw null;
		}

		@ExceptionMetered(absolute = true)
		public void exceptionMeteredAbsolute() {
			throw null;
		}

		@ExceptionMetered(name = "myExceptionMeteredName")
		public void exceptionMeteredName() {
			throw null;
		}

		@ExceptionMetered(name = "myExceptionMeteredNameAbsolute", absolute = true)
		public void exceptionMeteredNameAbsolute() {
			throw null;
		}

		@ExceptionMetered(cause = NullPointerException.class, absolute = true)
		public void exceptionMeteredCauseExact() {
			throw null;
		}

		@ExceptionMetered(cause = RuntimeException.class, absolute = true)
		public void exceptionMeteredCauseSubtype() {
			throw null;
		}

		@ExceptionMetered(cause = RuntimeException.class, absolute = true)
		public void exceptionMeteredCauseSupertype() throws Exception {
			throw new Exception();
		}

		@ExceptionMetered(cause = Exception.class)
		public void exceptionMeteredCauseNoException() {
		}

		@ExceptionMetered
		public void exceptionMeteredNoException() {
		}
	}

	@BeforeClass
	public static void attachProfiler() {
		Stagemonitor.init();
	}

	@Before
	@After
	public void clearMetricRegistry() {
		Stagemonitor.getMetricRegistry().removeMatching(MetricFilter.ALL);
	}

	@AfterClass
	public static void resetStagemonitor() {
		Stagemonitor.reset();
	}

	@Test
	public void testExceptionMeteredAspectDefault() {
		try {
			testObject.exceptionMeteredDefault();
		} catch (Exception e) {
			// ignore
		}
		assertOneTimerExists("meter.ExceptionMeteredAspectTest$TestObject#exceptionMeteredDefault.exceptions");
	}

	@Test
	public void testExceptionMeteredAspectexceptionMeteredPrivate() {
		try {
			testObject.exceptionMeteredPrivate();
		} catch (Exception e) {
			// ignore
		}
		assertEquals(1, Stagemonitor.getMetricRegistry().getMeters().size());
	}

	@Test
	public void testExceptionMeteredAspectAbsolute() {
		try {
			testObject.exceptionMeteredAbsolute();
		} catch (Exception e) {
			// ignore
		}
		assertOneTimerExists("meter.exceptionMeteredAbsolute.exceptions");
	}

	@Test
	public void testExceptionMeteredName() {
		try {
			testObject.exceptionMeteredName();
		} catch (Exception e) {
			// ignore
		}
		assertOneTimerExists("meter.ExceptionMeteredAspectTest$TestObject#myExceptionMeteredName.exceptions");
	}

	@Test
	public void testExceptionMeteredNameAbsolute() {
		try {
			testObject.exceptionMeteredNameAbsolute();
		} catch (Exception e) {
			// ignore
		}
		assertOneTimerExists("meter.myExceptionMeteredNameAbsolute.exceptions");
	}

	@Test
	public void testExceptionMeteredCauseExact() {
		try {
			testObject.exceptionMeteredCauseExact();
		} catch (Exception e) {
			// ignore
		}
		assertOneTimerExists("meter.exceptionMeteredCauseExact.exceptions");
	}

	@Test
	public void testExceptionMeteredCauseSubtype() {
		try {
			testObject.exceptionMeteredCauseSubtype();
		} catch (Exception e) {
			// ignore
		}
		assertOneTimerExists("meter.exceptionMeteredCauseSubtype.exceptions");
	}

	@Test
	public void testExceptionMeteredCauseSupertype() {
		try {
			testObject.exceptionMeteredCauseSupertype();
		} catch (Exception e) {
			// ignore
		}
		final MetricRegistry metricRegistry = Stagemonitor.getMetricRegistry();
		assertEquals(0, metricRegistry.getMeters().size());
	}

	@Test
	public void testExceptionMeteredCauseNoException() {
		try {
			testObject.exceptionMeteredCauseNoException();
		} catch (Exception e) {
			// ignore
		}
		final MetricRegistry metricRegistry = Stagemonitor.getMetricRegistry();
		assertEquals(0, metricRegistry.getMeters().size());
	}

	@Test
	public void testExceptionMeteredNoException() {
		try {
			testObject.exceptionMeteredNoException();
		} catch (Exception e) {
			// ignore
		}
		final MetricRegistry metricRegistry = Stagemonitor.getMetricRegistry();
		assertEquals(0, metricRegistry.getMeters().size());
	}

	private void assertOneTimerExists(String timerName) {
		final MetricRegistry metricRegistry = Stagemonitor.getMetricRegistry();
		assertEquals(1, metricRegistry.getMeters().size());
		assertEquals(timerName, metricRegistry.getMeters().keySet().iterator().next());
	}

}
