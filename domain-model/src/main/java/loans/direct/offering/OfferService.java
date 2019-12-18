package loans.direct.offering;

import loans.direct.Money;
import loans.direct.UserId;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class OfferService {

    private OfferNegotiationFactory factory;
    private OfferNegotiationRepository repository;

    public OfferId propose(UserId debtor, Money money) {
        OfferNegotiation object = factory.propose(debtor, money);
        repository.save(object);
        return object.id;
    }

    public OfferId ask(UserId creditor, Money money) {
        OfferNegotiation object = factory.ask(creditor, money);
        repository.save(object);
        return object.id;
    }

    public void accept(OfferId id, UserId accepting) {
        OfferNegotiation object = repository.get(id);
        object.accept(accepting);
        repository.save(object);
    }

    public void reject(OfferId id, UserId rejecting) {
        OfferNegotiation object = repository.get(id);
        object.reject(rejecting);
        repository.save(object);
    }

    public void expire(OfferId id) {
        OfferNegotiation object = repository.get(id);
        object.expire();
        repository.save(object);
    }
}
