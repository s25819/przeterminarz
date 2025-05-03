PrzeTerminarz

Celem zadania jest stworzenie aplikacji mobilnej, która umożliwi użytkownikom zbieranie i 
zarządzanie informacjami o terminach ważności różnych produktów w kategoriach: 
- Produkty spożywcze, 
- Leki
- Kosmetyki. 

Aplikacja ma pomóc w redukcji marnowania produktów poprzez śledzenie ich dat ważności i ułatwienie ich zużycia przed upływem terminu.

# Ekran listy:

DONE

- element listy powinny zawierać: zdjęcie produktu, nazwę produktu, termin ważności, kategorię,
  ilość (jeśli została określona) oraz informację czy produkt został wyrzucony (jeśli wygasła jego
  data przydatności) [2p]
- aplikacja powinna wyświetlać listę wprowadzonych produktów z możliwością filtrowania według kategorii oraz stanu produktu (ważny/przeterminowany) [2p] (DONE)
- lista powinna być automatycznie sortowana według daty ważności produktów, od najkrótszej do najdłuższej [1p] (DONE)
- ekran powinien zawierać podsumowanie dla puli prezentowanych elementów (ich liczbę) [1p] (DONE)
- wybór elementu listy umożliwi jego podgląd i edycję, tylko jeśli produkt jest nadal przydatny, w przeciwnym wypadku wyświetli stosowną informację o braku możliwości edycji takiego produktu  (DONE)
- dłuższe przytrzymanie spowoduje pokazanie alertu z zapytaniem o usunięcie elementu z listy (jeśli produkt jest dalej ważny). Jeśli użytkownik zatwierdzi usunięcie wpis powinien zniknąć z listy, a podsumowanie się aktualizować. Jeśli produkt jest nie ważny w ten sposób można go oznaczyć jako wyrzucony. [2p] (DONE)
- ekran powinien zawierać również przycisk umożliwiający dodanie nowego elementu (DONE)
- lista powinna być zrealizowana implementacja komponentu graficznego RecyclerView [brak spełnienia kryterium: -2p] (DONE)


# Ekran dodawania/edycji produktu [6p]:

DONE
- zdjęcie produktu powinno być wybierane z galerii zdjęć i zapisywane w bazie z produktami jako miniaturka [brak spełnienia kryterium: -2p]
- Aplikacja powinna posiadać zadbaną szatę graficzną i responsywny układ (np: ikony zamiast tekstu na przyciskach oraz logo na ekranie listy, brak najeżdżających na siebie przycisków, lista przewijana, itp.). Dodatkowo wykorzystane w aplikacji teksty powinny być umieszone w plikach z zasobami.[2p]
- Przed poddaniem projektu ocenie należy przygotować zestaw danych przykładowych wczytywany
  każdorazowo podczas tworzenia bazy danych, w celu zaprezentowania wszystkich funkcjonalności. (
  najlepiej dane te umieścić bezpośrednio w kodzie aplikacji) [brak spełnienia kryterium: -2p]

- Zapisywanie danych na urządzeniu należy zrealizować z wykorzystaniem mechanizmu lokalnej bazy danych np: SQLite/Room. [3p]
- uruchamia się w następstwie kliknięcia przycisku dodającego lub w przypadku edycji istniejącego wpisu na liście (DONE)
- ekran ten umożliwia nadanie/zmianę danych wpisu oraz zawiera  przycisk zapisujący dokonane zmiany/wprowadzone dane  (DONE)
- kategoria wybierana przy pomocy rozwijanej listy wyboru lub radio buttonów [brak spełnienia kryterium: -1p] (DONE)
- data ważności wprowadzana przy pomocy kontrolki date picker [brak spełnienia kryterium: -1p] (DONE)
- ekran waliduje wprowadzane dane (data nie może być przeszła, nazwa pusta, jeśli użytkownik zdecyduje się na wprowadzenie ilości to ilość musi być wartością liczbową, a jednostka nie może być pominięta) [brak spełnienia kryterium: -1p] (DONE)
- edycja produktu nie może odbywać poprzez usunięcie starego wpisu i dodanie go ponownie ze zmienioną zawartością [brak spełnienia kryterium: -2p] (DONE)
