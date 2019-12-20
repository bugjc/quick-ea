package com.bugjc.ea.opensdk.http.core.component.monitor.metric.util;

import static java.lang.Double.isInfinite;
import static java.lang.Double.isNaN;

/**
 * A ratio of one quantity to another.
 * @author aoki
 * @date 2019/12/20
 * **/
public class Ratio {
    /**
     * Creates a new ratio with the given numerator and denominator.
     *
     * @param numerator   the numerator of the ratio
     * @param denominator the denominator of the ratio
     * @return {@code numerator:denominator}
     */
    public static Ratio of(double numerator, double denominator) {
        return new Ratio(numerator, denominator);
    }

    private final double numerator;
    private final double denominator;

    private Ratio(double numerator, double denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    /**
     * Returns the ratio, which is either a {@code double} between 0 and 1 (inclusive) or
     * {@code NaN}.
     *
     * @return the ratio
     */
    public double getValue() {
        final double d = denominator;
        if (isNaN(d) || isInfinite(d) || d == 0) {
            return Double.NaN;
        }
        return numerator / d;
    }

    @Override
    public String toString() {
        return numerator + ":" + denominator;
    }
}
