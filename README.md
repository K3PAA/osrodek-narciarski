# Symulacja Ośrodka Narciarskiego

Projekt zaliczeniowy z przedmiotu Programowanie Obiektowe zrealizowany w roku akademickim 2025/26.
Program jest złożoną symulacją ruchu narciarskiego na stokach, w całości opartą o mechanizm kolejki zdarzeń.

## Funkcjonalności

- Modelowanie ośrodka jako silnie spójnego grafu skierowanego, w którym węzłami są stacje, a krawędziami trasy oraz wyciągi.
- Symulacja w wirtualnym czasie od 9:00 do 15:00 za pomocą mechanizmu kolejki zdarzeń.
- Rozbudowany system wyboru tras uwzględniający trzy typy zachowań (Lokalny, Zachłanny, Kolekcjoner), w których sportowcy układają plany przejazdów z wykorzystaniem algorytmu przeszukiwania wszerz (BFS).
- Model oceny atrakcyjności tras, biorący pod uwagę indywidualne predyspozycje narciarzy (dopasowanie trudności), dynamiczne zużycie trasy w trakcie dnia oraz rosnące znudzenie trasą oparte na modelu wygładzania wykładniczego.
- Generowanie zaawansowanych statystyk dla wyciągów (np. procent zajętych miejsc, średnia długość kolejki) oraz tras.
- Generowanie wizualizacji ośrodka narciarskiego i obciążeń tras w formacie plików `.tex` (konwertowanych do PDF) z mapami przedstawiającymi parametry tras i statystyki przejazdów śledzonych sportowców.

## Technologie i Architektura

- **Środowisko:** Java 21.
- **Testowanie:** Przypadki testowe zaimplementowane w JUnit, weryfikujące poprawność obsługi kolejek do wyciągów oraz algorytmów na grafach.
