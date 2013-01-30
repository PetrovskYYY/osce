package ch.unibas.medizin.osce.client.scaffold.ui;

import java.math.BigDecimal;

import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;

/**
 * A simple renderer of Float values.
 */
public class BigDecimalRenderer extends AbstractRenderer<BigDecimal> {
	private static BigDecimalRenderer INSTANCE;

	/**
	 * @return the instance
	 */
	public static Renderer<BigDecimal> instance() {
		if (INSTANCE == null) {
			INSTANCE = new BigDecimalRenderer();
		}
		return INSTANCE;
	}

	protected BigDecimalRenderer() {
	}

	public String render(BigDecimal object) {
		if (object == null) {
			return "";
		}

		return object.toString();
	}
}