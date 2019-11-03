
# Zad 3. Zaimplementuj proces gotówkowego zwrotu pieniędzy

## Problem dziedzinowy
To transakcji wymiany pieniędzy poza aplikacja, w aplikacji jedynie rejestrujemy fakt zwrotu.

Tylko strona, która jest na minusie, może inicjować akcje, przeciwna strona musi potwierdzić operacje.
Przy poprawnym potwierdzeniu operacji zwróc zdażenie, którego oczekuje konto rozliczeń (klasa z zadania 2.)

Zaimplementuj ten obiekt w nowym pakiecie, na wzór klasy Offer.

Po operacji saldo konta rozliczeń (z zadania 2.) ulega zmianie odpowiednio.

Zaimplementuj dodatkowo jeden scenariusz end-2-end uwzględniający ofertę, konto rozliczeń, proces zwrotu pieniędzy,
zweryfikuj finalne konto rozliczeń.
