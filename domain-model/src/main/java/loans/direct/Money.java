package loans.direct;

import lombok.Value;

import java.util.stream.Collector;

@Value
public class Money implements Comparable<Money> {
    private static final Money ZERO = of(0);
    long amount;

    public static Money of(long amount) {
        return new Money(amount);
    }

    public static Money zero() {
        return ZERO;
    }

    public static Collector<Money, long[], Money> summing() {
        return Collector.of(
                () -> new long[1],
                (a, t) -> a[0] += t.amount,
                (a, b) -> {
                    a[0] += b[0];
                    return a;
                },
                (a) -> of(a[0])
        );
    }

    public Money add(Money operand) {
        return Money.of(amount + operand.amount);
    }

    public Money subtract(Money operand) {
        return of(amount - operand.amount);
    }

    public boolean isNegative() {
        return amount < 0;
    }

    public Money negate() {
        return of(-amount);
    }

    public Money abs() {
        if (amount >= 0) {
            return this;
        } else {
            return negate();
        }
    }

    @Override
    public String toString() {
        return String.valueOf(amount);
    }

    @Override
    public int compareTo(Money money) {
        return Long.compare(amount, money.amount);
    }
}
