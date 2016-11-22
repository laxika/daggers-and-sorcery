package com.morethanheroic.math;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Methods to calculate percentages.
 */
@Service
@RequiredArgsConstructor
public class PercentageCalculator {

    private final RandomCalculator randomCalculator;

    public double calculatePercentage(double actualValue, double maximumValue) {
        return actualValue * 100 / maximumValue;
    }

    public boolean calculatePercentageHit(final int valueToHit) {
        return valueToHit >= randomCalculator.randomNumberBetween(1, 100);
    }
}
