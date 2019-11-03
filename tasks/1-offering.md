
# Zad 1. Zaimplementuj negocjacje pożyczki pomiędzy uczestnikami:

## Problem dziedzinowy
Inicjalna akcja może wyjść od dowolnego uczestnika:
- Poproś o pożyczkę
- Zaoferuj kwotę
wskazując kwotę oraz drugiego uczestnika.

Drugi uczestnik może przyjąć / potwierdzić transakcje, dowolny uczestnik może odrzucić / zakończyć ofertę.

Scenariusze:
1. Oferuje pieniądze koledze, on przyjmuje ofertę
2. Oferuje pieniądze koledze, on odrzuca ofertę
3. Oferuje pieniądze koledze, on nie reaguje
4. Kolega pyta mnie o pożyczkę, przyjmuję ofertę
5. Kolega pyta mnie o pożyczkę, odrzucam ofertę
6. Kolega pyta mnie o pożyczkę, nie odpowiadam

Zadbaj by podczas uzgadniania oferty pilnowane były następujące reguły:
- interakcja może nastąpić wyłącznie z ofertami aktywnymi "nie przyjęte" i "nie odrzucone" 
- przyjęcie oferty musi być dokonane przez drugiego uczestnika "counter party", nie przez autora oferty.

Zdefiniuj dodatkowe scenariusze weryfikujące powyższe invarianty.

## Podpowiedzi
Programowany obiekt uczestniczy w dwóch requestach do systemu:
- inicjalna akcja (poproś o pożyczkę / zaoferuj kwotę)
- odpowiedź (przyjmuje ofertę / odrzuca ofertę)

Pomiędzy tymi requestami należy NIE ZGUBIĆ informacji kto komu pożycza oraz kto może zaakceptować.

Jak zacząć:
1. Naszkicuj pierwsze scenariusze testowe zasczynając od when i then, pomiń given.
2. Rozpocznij implementację zachowań metod.
3. Wyłaniaj wewnętrzne struktury agregatów "co muszę pamiętać".
    Zaproponuj value object-y potrzebne do zaimplementowania reguł (invariants).
4. Uzupełnij testy o wywołanie konstruktora.
5. Wewnętrzny refactoring.

Proponowany szablon obiektu realizującego negocjację:

````java
class OfferNegotiation {
    
    // state of object
    
    OfferCreated propose(UserId debtor, Money money) {}

    OfferCreated ask(UserId creditor, Money money) {}

    TransactionRecorded accept(UserId accepting) {}

    Rejected reject(UserId rejecting) {}
}
````
