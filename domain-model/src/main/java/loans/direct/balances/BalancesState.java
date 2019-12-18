package loans.direct.balances;

import loans.direct.Money;
import loans.direct.Operation;
import loans.direct.UserId;
import loans.direct.users.UserDetails;
import lombok.AllArgsConstructor;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
class BalancesState {

    BalancesId id;
    final UserId user;
    final Map<UserId, BalancesSummary.Entry> last;
    UserDetails users;

    void apply(Operation operation) {
        UserId counterParty = operation.getDetails().getCounterParty(user);
        last.put(counterParty, toEntry(counterParty, operation));
    }

    BalancesSummary get() {
        var grouped = last.entrySet().stream()
                .sorted(Comparator.comparing(e -> e.getValue().getBalance()))
                .collect(Collectors.groupingBy(
                        e -> Group.classify(e.getValue().getBalance()),
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())
                ));
        Money credits = Group.sumCredits(grouped);
        Money debits = Group.sumDebits(grouped);
        return new BalancesSummary(
                user,
                new BalancesSummary.Header(credits, debits, credits.add(debits)),
                Group.getCredits(grouped), Group.getDebits(grouped), Group.getCleared(grouped)
        );
    }

    private BalancesSummary.Entry toEntry(UserId counterParty, Operation operation) {
        return new BalancesSummary.Entry(
                users.get(counterParty),
                operation.getBalance().of(user),
                operation.getDetails().getTimestamp()
        );
    }

    private enum Group {
        CREDITS, DEBITS, CLEARED;

        static Group classify(Money balance) {
            if (balance.equals(Money.zero())) {
                return Group.CLEARED;
            } else if (balance.isNegative()) {
                return Group.DEBITS;
            } else {
                return Group.CREDITS;
            }
        }

        static List<BalancesSummary.Entry> getCredits(Map<Group, List<BalancesSummary.Entry>> grouped) {
            return get(grouped, CREDITS);
        }

        static List<BalancesSummary.Entry> getDebits(Map<Group, List<BalancesSummary.Entry>> grouped) {
            return get(grouped, DEBITS);
        }

        static List<BalancesSummary.Entry> getCleared(Map<Group, List<BalancesSummary.Entry>> grouped) {
            return get(grouped, CLEARED);
        }

        static Money sumCredits(Map<Group, List<BalancesSummary.Entry>> grouped) {
            return sum(getCredits(grouped));
        }


        static Money sumDebits(Map<Group, List<BalancesSummary.Entry>> grouped) {
            return sum(getDebits(grouped));
        }

        private static List<BalancesSummary.Entry> get(Map<Group, List<BalancesSummary.Entry>> grouped, Group credits) {
            var list = grouped.getOrDefault(credits, Collections.emptyList());
            Comparator<BalancesSummary.Entry> comparing = Comparator.comparing(entry -> entry.getBalance().abs());
            list.sort(comparing.reversed());
            return list;
        }

        private static Money sum(List<BalancesSummary.Entry> credits) {
            return credits.stream().map(BalancesSummary.Entry::getBalance).collect(Money.summing());
        }
    }
}
