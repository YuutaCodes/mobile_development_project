# AGENTS.md

Instructions for any AI assistant (and humans) working in this repo. Read before changing code.

## Project

OddBite: a Too Good To Go-style Android app (Kotlin, package `com.ap.oddbite`). Two roles: **customer** (find, reserve and pick up surprise bags) and **merchant** (register a store, offer bags, handle reservations). Course assignment, deadline **8 Jan 2027**. Requirements are tagged A1..G4 as **Must** or **Should**; see the planning in `README.md`.

Do not use the name, logo or visual identity of Too Good To Go.

## Design

You can find the general design here: [Figma](https://www.figma.com/design/xmNthc7z01NzCI87pmkSb0/OddBite-%E2%80%93-app-design?node-id=0-1&p=f&t=3IYimYKgnNxlQCVa-0)
This design can change overtime to make improvements

## Git workflow

- **Never push or commit to `main`.** Every feature gets its own branch, merged via pull request with green CI.
- Branch names: `feat/<short-name>`, `fix/<short-name>`, `chore/<short-name>`, `docs/<short-name>`.
- Preferably one feature or requirement per branch (e.g. `feat/auth`, `feat/reverse-geocoding`).
- Small, regular, clear commits. Git counts toward the grade and every group member must contribute.
- No co-author lines in commit messages or merges.

## Architecture

MVVM with strict separation. **Composables contain UI only**: no Firebase, Room or Retrofit code, no business logic.

```
com.ap.oddbite
├── data
│   ├── remote      Firestore/Auth data sources, Retrofit API (Nominatim)
│   ├── local       Room database, entities, DAOs
│   ├── model       Domain models (User, Store, Bag, Reservation)
│   └── repository  Combines remote + local, exposes Flows to ViewModels
├── service         Reusable services (location, geocoding, image encoding, pickup code)
├── ui
│   ├── navigation  NavHost, routes, bottom navigation bar (customer)
│   ├── theme
│   ├── auth | discover | map | detail | orders | favorites | store | bags
│   └── components  Shared composables
└── MainActivity.kt
```

Rules:

- Every screen has a `ViewModel` exposing `StateFlow<…UiState>`; the screen only observes state and sends events.
- Logic lives in repositories and services, not in ViewModels or composables. Services get their dependencies by constructor so they can be unit tested.
- Async work in `viewModelScope`; use `await()` (kotlinx-coroutines-play-services) instead of callbacks.
- Lists come in real time from Firestore `snapshots()` as `Flow`.
- Heavy work (image resize, Base64, decoding) on `Dispatchers.Default`, not on the main thread.
- No hardcoded UI text: use `strings.xml`.
- State must survive rotation (keep it in the ViewModel or `rememberSaveable`).
- Handle no internet, no location, empty lists and invalid input with a clear message. Show a loading indicator during network operations.

## Technical constraints from the assignment

- **Firebase**: Auth (email/password) and Cloud Firestore only. No Firebase Storage. Security rules: only signed-in users read/write, users edit only their own profile. Never leave the database in test mode.
- **Firestore collections**: `users/{uid}`, `stores/{storeId}`, `bags/{bagId}`, `reservations/{id}` with the required fields from the assignment (extra fields allowed).
- **Photos**: resize to max 800 px longest side, JPEG quality about 70, `Base64.NO_WRAP`, store in `bags.photoBase64`. A Firestore document is max 1 MiB.
- **Room** (single `@Database` singleton), at least: `FavoriteStore`, `CachedReservation`, `CachedAddress`. The orders screen reads only from Room so it works offline; every Firestore change is written to Room.
- **Reverse geocoding**: Nominatim `/reverse` via Retrofit (`lat`, `lon`, `format=json`, `zoom`, `addressdetails=1`). `zoom=18` for a street address, `zoom=10` for the municipality. Send an identifying `User-Agent` (no fake browser), max 1 request per second, cache results in `CachedAddress` keyed by coordinates rounded to 4 decimals.
- **Map**: OSMDroid, based on the OSM_starter config (User-Agent, cache dir, MapView lifecycle). Distances via `GeoPoint.distanceToAsDouble` (meters).
- **Permissions**: request `ACCESS_FINE_LOCATION` together with `ACCESS_COARSE_LOCATION`, only after an explanation and a user action. Handle precise, approximate-only, denied and permanently denied (link to app settings). If denied, the user can pick a location with a pin on the map.
- UI language may be Dutch or English.

## Code style

- Match the surrounding code. Keep changes minimal and focused on the task.
- Comments only where code needs explaining for other developers; keep them short.
- Every student must be able to explain their own code. Do not generate code nobody on the team understands.

## Definition of done

- Builds and CI is green.
- Covers the relevant requirement IDs and does not crash on edge cases.
- Pull request opened from the feature branch, not pushed to `main`.
