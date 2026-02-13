# Θέμα 2: Todo List REST API
**Μάθημα:** Υπηρεσιοστρεφές Λογισμικό 2025-2026

## Περιγραφή
Κατασκευή ενός πλήρους REST API με χρήση Ruby on Rails για τη διαχείριση λίστας εκκρεμοτήτων (Todos) και των αντικειμένων τους (Todo Items).

## API Endpoints
| Method | Endpoint | Functionality |
| :--- | :--- | :--- |
| POST | `/signup` | Εγγραφή χρήστη |
| POST | `/auth/login` | Σύνδεση |
| GET | `/todos` | Λίστα όλων των todos |
| POST | `/todos` | Δημιουργία νέου todo |
| GET | `/todos/:id` | Λήψη συγκεκριμένου todo |
| PUT | `/todos/:id` | Ενημέρωση todo |
| DELETE | `/todos/:id` | Διαγραφή todo & items |

## Testing & Documentation
* **TDD:** Η ανάπτυξη έγινε με Test Driven Development (RSpec).
* **Manual Testing:** Δοκιμή των endpoints με χρήση **httpie**.
* **Documentation:** Το εγχειρίδιο χρήσης ακολουθεί το πρότυπο **OpenAPI/Swagger**.

## Οδηγίες Χρήσης
Τρέξτε τα tests με την εντολή:
`bundle exec rspec`
