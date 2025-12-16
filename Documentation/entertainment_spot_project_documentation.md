# Entertainment Spot – Project Documentation

## Overview
**Entertainment Spot** is an Android application for discovering movies and TV series. The app integrates **TMDb** (metadata, posters, cast, trailers) and **Watchmode** (IMDb ratings, streaming platforms), merges the data, stores it in **Firebase Cloud Firestore**, and displays it through a modern, fragment-based UI.

The app follows a **single-activity + multiple-fragment** architecture with a bottom navigation bar.

---

## App Architecture

### High-level Flow
```
TMDb API ──┐
           ├─> FirestoreDataUploader ──> Firebase Firestore ──> UI Fragments
Watchmode ─┘
```

### UI Flow
```
SplashActivity
   ↓
MainActivity
   ├─ Movies (EntertainmentListFragment)
   ├─ Series (EntertainmentListFragment)
   ├─ Search (EntertainmentSearchFragment)
   ↓
EntertainmentDetailFragment
```

---

## Kotlin Source Files

### Activities

#### SplashActivity.kt
Displays a splash screen on app launch, waits briefly using a coroutine, then launches `MainActivity`. Prevents back navigation to splash.

#### MainActivity.kt
Single activity host for the app. Contains:
- `BottomNavigationView`
- `FragmentContainerView` (`nav_host`)
- Fragment switching logic for Movies, Series, and Search

---

### Fragments

#### EntertainmentListFragment.kt
Displays a grid of either movies or TV series.
- Reads data from Firestore (`movies` or `series` collections)
- Uses a 3-column `RecyclerView`
- Dynamically switches adapters based on content type
- Navigates to `EntertainmentDetailFragment` on item tap

#### EntertainmentDetailFragment.kt
Displays detailed information for a selected movie or series:
- Poster & backdrop images
- Title, genres, release date, runtime/seasons
- IMDb rating
- Overview/summary
- Streaming platforms
- Cast carousel (horizontal RecyclerView)
- Embedded YouTube trailer player

Receives data via JSON arguments (`ARG_MOVIE` / `ARG_SERIES`).

#### EntertainmentSearchFragment.kt
Scaffolding for search functionality:
- Search bar UI
- Toggle between Movies and TV Shows
- Placeholder container for search results

---

### RecyclerView Adapters

#### EntertainmentMovieAdapter.kt
Adapter for movie cards:
- Binds movie title, poster, and IMDb rating
- Uses `item_entertainment_card.xml`
- Handles click navigation

#### EntertainmentSeriesAdapter.kt
Adapter for TV series cards:
- Similar to movie adapter
- Binds series-specific fields

#### EntertainmentCastAdapter.kt
Horizontal carousel adapter for cast members:
- Displays actor headshot
- Actor name and character name
- Uses TMDb image URLs

#### EntertainmentAdapter.kt (Legacy)
Deprecated generic adapter from an earlier design using `EntertainmentItem`. No longer used.

---

### Networking & Data Layer

#### RetrofitClient.kt
Central Retrofit configuration:
- Kotlinx Serialization
- Separate Retrofit instances for TMDb and Watchmode
- `ignoreUnknownKeys = true`

#### TmdbApiService.kt
Defines TMDb endpoints:
- Popular movies & series
- Details
- Credits
- Videos (trailers)
- Release dates / certifications

#### WatchmodeApiService.kt
Defines Watchmode endpoints:
- Search by TMDb ID
- Title details (IMDb rating)
- Streaming platform sources

---

### Firestore Integration

#### FirestoreDataUploader.kt
Background data ingestion and enrichment engine:
1. Fetches popular movies/series from TMDb
2. Fetches details, cast, videos, release info
3. Enriches data using Watchmode (IMDb rating, platforms)
4. Uploads enriched documents to Firestore

Collections:
- `movies`
- `series`

---

### Data Models

#### EntertainmentItem.kt
Core data models used throughout the app:
- `Movie`
- `Series`
- `CastMember`
- TMDb brief list models

These models represent the final Firestore document structure.

#### TmdbResponses.kt
TMDb-specific response DTOs:
- List wrappers
- Genre responses
- Credits
- Videos
- Release date certifications

#### WatchmodeResponses.kt
Watchmode-specific response DTOs:
- Search results
- Title details
- Streaming source objects

---

## XML Layout Files

### Activities

#### activity_spash_screen.xml
Splash screen UI with centered icon and background color.

#### activity_main.xml
Main activity layout:
- `FragmentContainerView` (`nav_host`)
- `BottomNavigationView`

---

### Fragments

#### fragment_entertainment_list.xml
Contains a full-screen `RecyclerView` for movie/series grids.

#### fragment_entertainment_detail.xml
Complex detail screen layout including:
- Backdrop image
- Rounded poster image
- Metadata rows
- Summary section
- Cast carousel
- Streaming platforms
- YouTube trailer player

#### fragment_entertainment_search.xml
Search screen layout:
- Search bar
- Movie/TV toggle buttons
- Results container

---

### RecyclerView Items

#### item_entertainment_card.xml
Card used in grid lists:
- Poster image
- Title
- IMDb star rating row

#### item_entertainment_cast.xml
Cast carousel item:
- Circular headshot
- Actor name
- Character name

---

## Data Lifecycle Summary

```
TMDb + Watchmode
      ↓
FirestoreDataUploader
      ↓
Firebase Firestore
      ↓
Fragments → RecyclerViews → User
```

---

## Future Enhancements
- Full-text Firestore search
- User favorites & watchlists
- Offline caching
- Pagination & infinite scroll
- Profile-based recommendations

---

## Notes
This documentation reflects the current architecture and file responsibilities. As features evolve (search, personalization, caching), new modules and adapters can be added without restructuring the core app.

