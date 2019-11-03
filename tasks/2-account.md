
# Zad 2. Zaimplementuj konto rozliczeń pomiędzy uczestnikami

## Problem dziedzinowy
W każdym momencie musimy znać bierzące saldo rozliczeń pomiędzy uczestnikami.
Ulega ono zmianie w wyniku dwóch operacji:

1. Po udzieleniu pożyczki saldo między uczestnikami ulega zmianie.
2. Zwrot zadłużenia pomniejsza zadłużenie,
   Zwrot może być częściowy, całkowity, bądź z nadpłatą.
   W przypadku nadpłaty, zadłużenie obciąża stronę, która pierwotnie udzielała pożyczki.

Obiekt konta rozliczeń pomiędzy uczestnikami reaguje na:
- fakt dokonania tranzakcji (z zadania pierwszego)
- analogiczny fakt zwrotu pieniędzy

W każdym momencie powinno być możliwe ustalenie kto, komu ile jest winny.

## Podpowiedzi
Uczestnik nie ma "swojego konta", na którym trzyma pieniądze.

Istnieje jedno saldo pomiędzy każdymi dwoma uczestnikami systemu.
Jeżeli nie dokonywali żadnych tranzakcji saldo wynosi 0.

Spisz 3-4 proste scenariuszy uwzględniających 1-2 tranzakcje i zweryfukuj finalne saldo.

Wyzwaniem jest wiedzieć dokładnie kto komu jest winien i nie pomylić stron.
