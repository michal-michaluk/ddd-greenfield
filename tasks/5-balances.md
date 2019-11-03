
# Zad 5. Zaimplementuj read model

## Problem dziedzinowy
Lista rozliczeń (pożyczek / zadłużeń) użytkownika z innymi użytkownikami
Przedstawia:
- każdy element na liście odpowiada jednemy kontu, w kórym występuje użytkowniki
  i ma opis:
  - kto: imię / pseudonim innego użytkownika,
  - ile: aktualne saldo z perspektywy użytkownika,
  - data: ostatnia tranzakcja
- lista powinna byc w 3 grupach
  - pożyczki,
  - zadłużenia,
  - rozliczone, czyli saldo == 0
- podsumowanie lisy: sumę pożyczek, sumę zadłużeń, całkowite saldo użytkownika = suma pożyczek - suma zadłużeń

## Podpowiedzi
Zastosuj elementy:
- stan: zbiór value object-ów opisujący poszczególne informacje (przechowywane w bazie jako dokumenty)
- projekcje: zestaw funkcji twożących nowy stan w oparciu i obecny stan + nowy event i ewentualnie inne read modele
- odczyt: funkcję transformującą obecny stan i zwracającą finalny odczyt, może podsumowywać / mapować

stan + projekcje + odczyt można zrealizować jako jeden obiekt.
