
# Zad 4. Zaimplementuj część query read model-u

## Problem dziedzinowy
Historia operacji na pojedynczym koncie z perspektywy jednego z użytkowników.
Każdy element na liście ma tytuł operacji jak poniżej:
- Maciek pożyczył od Ciebie kwotę 25 PLN, saldo po operacji 25 PLN
- Maciek zwrócił Ci kwotę 20 PLN, saldo po operacji 5 PLN
- Maciek pożyczył Ci kwotę 15 PLN, saldo po operacji -10 PLN
- Maciek otrzymał od Ciebie kwotę 10 PLN, saldo po operacji 0 PLN

## Podpowiedzi
Zaimplementuj funkję mapującą pojedyczy event Operation na powyższy tekst, zacznij od testów powyższych przykladów.
Zaimplementuj odczytu, które dla podanego w parametrach użytkownia, wskazanego konta i zakresu dat wylicza listę zawierającą:
- datę operacji
- tytuł operacji
Niech klasa odczytu ma w polach dostęp do "listy eventów" Operation dla wskazaych użytkowników:

```java
class HistoryQuery {

    private UserDetails names;
    private Collection<Operation> operations;

    Stream<OperationDescription> history(AccountKey account, Instant from, Instant to) {
        // ...
    }
}
```

Taki read model nie musi być materializowany, ale gdybyśmy chcieli,
to istniały by dwie jego instancje dla każdego konta rozliczeń pomiędzy uczestnikami,
ze względu na treści, które są skierowane do jednego z uczestników.
