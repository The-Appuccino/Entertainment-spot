package com.appuccino.entertainment_spot


/**
 * EntertainmentSearchUseCase
 *
 * Acts as the **orchestration layer** for all search-related operations in the app.
 *
 * This class contains the **business logic** for search. It sits between:
 *  - the UI layer (`EntertainmentSearchFragment`)
 *  - the data layer (`EntertainmentSearchRepository`)
 *
 * Responsibilities:
 *  - Accept raw search input (typed text or voice-transcribed text)
 *  - Optionally pass input through a query parser (keywords or LLM-based)
 *  - Decide *what* to search (movies, series, or both)
 *  - Decide *how* to search (title, genre, year, rating, etc.)
 *  - Combine and normalize results into a UI-friendly format
 *  - Apply rules such as filtering, ranking, or fallback strategies
 *
 */


class EntertainmentSeachUseCase {
}