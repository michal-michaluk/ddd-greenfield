package loans.direct.offering;

import loans.direct.Event;

import java.time.Clock;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class OfferNegotiationEventSourcingInMemoryRepo implements OfferNegotiationRepository {

    private Clock clock;
    private Map<Long, List<Event>> eventlog = new ConcurrentHashMap<>();

    @Override
    public OfferNegotiation get(OfferId id) {
        Map<? extends Class<? extends Event>, Optional<Event>> latest = eventlog.getOrDefault(id.getId(), List.of()).stream()
                .collect(Collectors.groupingBy(
                        Event::getClass,
                        Collectors.reducing((first, second) -> second)
                ));

        OfferCreated initiated = (OfferCreated) latest.get(OfferCreated.class).orElse(null);
        TransactionRecorded transaction = (TransactionRecorded) latest.get(TransactionRecorded.class).orElse(null);
        Rejected reject = (Rejected) latest.get(Rejected.class).orElse(null);

        if (initiated == null) {
            throw new IllegalArgumentException("no object for id " + id);
        }
        return new OfferNegotiation(
                id,
                initiated,
                transaction,
                reject,
                new ArrayList<>(),
                clock
        );
    }

    @Override
    public void save(OfferNegotiation object) {
        eventlog.computeIfAbsent(object.id.getId(), id -> new ArrayList<>())
                .addAll(object.events);
    }
}
