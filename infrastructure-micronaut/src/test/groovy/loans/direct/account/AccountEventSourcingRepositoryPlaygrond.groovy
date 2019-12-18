package loans.direct.account

import io.micronaut.test.annotation.MicronautTest
import loans.direct.UserId
import spock.lang.Ignore
import spock.lang.Specification

import javax.inject.Inject

@Ignore
@MicronautTest
class AccountEventSourcingRepositoryPlaygrond extends Specification {

    @Inject
    AccountEventSourcingRepository repo
    Random random = new Random()

    def "sql playgrond"() {
        given:
        def key = new AccountKey(new UserId(random.nextLong()), new UserId(random.nextLong()))

        when:
        def id = repo.initAccount(key)
        repo.saveEvents(id, '{"@type":"Operation.v1","kind":"LOAN","details":{"timestamp":"2019-11-13T19:17:38.973688Z","from":{"id":1},"to":{"id":2},"amount":{"amount":25,"negative":false}},"balance":{"from":{"id":1},"to":{"id":2},"balance":{"amount":25,"negative":false}}}')
        def event = repo.findLastOperation(key)

        then:
        event.isEmpty()
    }
}
