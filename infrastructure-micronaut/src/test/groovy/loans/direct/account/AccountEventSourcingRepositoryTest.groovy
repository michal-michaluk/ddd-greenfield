package loans.direct.account

import io.micronaut.test.annotation.MicronautTest
import loans.direct.Money
import loans.direct.RandomUsersTesting
import loans.direct.TransactionDetails
import loans.direct.UserId
import loans.direct.account.AccountEventSourcingRepository
import loans.direct.account.AccountKey
import spock.lang.Specification

import javax.inject.Inject
import java.time.Instant

@MicronautTest
class AccountEventSourcingRepositoryTest extends Specification implements RandomUsersTesting {

    @Inject
    AccountEventSourcingRepository repo

    def "changes are persistent"() {
        given:
        def key = new AccountKey(someUser(), someUser())
        def loan = details(key.from, key.to, Money.of(25))

        when:
        def account = repo.get(key)
        def balance = account.loan(loan)
        repo.save(account)

        then:
        repo.get(key).balance().of(key.from) == Money.of(25)


        when:
        def payback = details(key.to, key.from, Money.of(25))
        account = repo.get(key)
        balance = account.loan(payback)
        repo.save(account)

        then:
        repo.get(key).balance().of(key.from) == Money.of(0)
    }

    def details(UserId creditor, UserId debtor, Money amount) {
        new TransactionDetails(Instant.now(), creditor, debtor, amount)
    }
}
