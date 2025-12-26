package com.appuccino.entertainment_spot

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import android.util.Log


/**
 * Responsible for all the functions that is used for creating and managing the favorite list.
 */

object FirestoreFavoriteListExchange {

    private const val TAG = "FirestoreFavoriteListExchange"

    /**
     * FirebaseAuth singleton (lazy).
     * - "by lazy" means: don't create it until the first time it's actually used.
     * - This avoids some Android Studio lint warnings about static context references.
     */
    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private val db: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    //Gets the current user's UID, or null if not signed in.
    private fun uidOrNull(): String? = auth.currentUser?.uid

    //Returns the CollectionReference for: users/{uid}/favorites
    private fun favoritesCol(uid: String) =
        db.collection("users").document(uid).collection("favorites")


    /**favoriteDocId()
     * Builds a consistent favorite document ID, for example:
     * - movie_12345
     * - series_67890
     *
     * This makes toggling easy because you can:
     * - set() the doc to add favorite
     * - delete() the same doc to remove favorite
     */
    // ^ String interpolation: inserts type and tmdbId into a single string.
    fun favoriteDocId(type: String, tmdbId: Int): String = "${type}_${tmdbId}"


    /**
     * Adds a favorite document under the current user:
     * users/{uid}/favorites/{type_tmdbId}
     *
     * @param item The FavoriteItem you want to save.
     * @param onComplete Callback you get when finished:
     *        - success = true/false
     *        - error = Exception if something went wrong
     *
     * The default callback { _, _ -> } means "do nothing" if caller doesn't care.
     */
    fun addFavorite(
        item: FavoriteItem,
        onComplete: (success: Boolean, error: Exception?) -> Unit = { _, _ -> }
    ) {
        // Get the current user uid from FirebaseAuth.
        val uid = uidOrNull()
        if (uid == null) {
            // Return failure to the caller (UI) so it can react appropriately.
            onComplete(false, IllegalStateException("No signed-in user"))
            return
        }

        // Build the doc id like "movie_12345" or "series_67890"
        val docId = favoriteDocId(item.type, item.tmdbId)

        // Ensure createdAt is filled in.
        // - If createdAt is 0L, we set it to "now" using epoch millis.
        // - Otherwise we keep the existing createdAt (if you already set one).
        val toSave = item.copy(
            createdAt = if (item.createdAt == 0L) System.currentTimeMillis() else item.createdAt
        )

        // Get the document reference: users/{uid}/favorites/{docId}
        favoritesCol(uid).document(docId)
            // Write the document. set(toSave) will create or overwrite the doc.
            .set(toSave)
            // If the write succeeds...
            .addOnSuccessListener {
                Log.d(TAG, "addFavorite success: $docId") // log for debugging
                onComplete(true, null) // inform the caller it worked
            }
            // If the write fails...
            .addOnFailureListener { e ->
                Log.w(TAG, "addFavorite failed: $docId", e) // log warning + exception
                onComplete(false, e) // inform caller it failed
            }
    }


    /**
     * Removes a favorite document:
     * users/{uid}/favorites/{type_tmdbId}
     *
     * @param type "movie" or "series"
     * @param tmdbId the TMDb id
     * @param onComplete callback for success/failure
     */
    fun removeFavorite(
        type: String,
        tmdbId: Int,
        onComplete: (success: Boolean, error: Exception?) -> Unit = { _, _ -> }
    ) {
        val uid = uidOrNull()
        if (uid == null) {
            onComplete(false, IllegalStateException("No signed-in user"))
            return
        }

        // Rebuild the same docId you used when saving.
        val docId = favoriteDocId(type, tmdbId)

        // Get the doc reference and delete it.
        favoritesCol(uid).document(docId)
            .delete()
            .addOnSuccessListener {
                Log.d(TAG, "removeFavorite success: $docId")
                onComplete(true, null)
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "removeFavorite failed: $docId", e)
                onComplete(false, e)
            }
    }

    /**
     * Real-time listener for "is this specific item favorited?"
     *
     * It listens to: users/{uid}/favorites/{type_tmdbId}
     * - if the document exists => favorited
     * - if it doesn't exist => not favorited
     *
     * Returns ListenerRegistration so the caller can stop listening later:
     *   val reg = listenIsFavorited(...)
     *   reg?.remove()   // IMPORTANT when fragment stops/destroys view
     */
    fun listenIsFavorited(
        type: String,
        tmdbId: Int,
        onChanged: (isFavorited: Boolean) -> Unit,
        onError: (Exception) -> Unit = {}
    ): ListenerRegistration? {
        // If not signed in, we can't know per-user favorites. Return null = no listener.
        val uid = uidOrNull() ?: return null

        // Compute docId for the specific item.
        val docId = favoriteDocId(type, tmdbId)

        // Add a real-time listener to that single favorite document.
        return favoritesCol(uid).document(docId)
            .addSnapshotListener { snap, err ->
                // "err" means Firestore couldn't read (permission, network, etc.)
                if (err != null) {
                    onError(err)
                    return@addSnapshotListener // exit this callback early
                }

                // snap?.exists() == true means the doc exists (favorited).
                // If snap is null or doesn't exist, this becomes false.
                onChanged(snap?.exists() == true)
            }
    }


    /**
     * Real-time listener for ALL favorites (movies + series together)
     *
     * Listens to: users/{uid}/favorites (entire collection)
     * Whenever something is added/removed/changed, onChanged gets a fresh List<FavoriteItem>.
     *
     * Returns ListenerRegistration so you can remove it in onStop/onDestroyView.
     */
    fun listenFavorites(
        onChanged: (List<FavoriteItem>) -> Unit,
        onError: (Exception) -> Unit = {}
    ): ListenerRegistration? {
        val uid = uidOrNull() ?: return null

        return favoritesCol(uid)
            .addSnapshotListener { snap, err ->
                if (err != null) {
                    onError(err)
                    return@addSnapshotListener
                }

                val items = snap?.documents?.mapNotNull { doc ->
                    val t = doc.getString("type") ?: return@mapNotNull null
                    val tmdbId = (doc.getLong("tmdbId") ?: return@mapNotNull null).toInt()
                    val title = doc.getString("title") ?: ""
                    val posterUrl = doc.getString("posterUrl") ?: ""
                    val imdbRating = doc.getDouble("imdbRating")
                    val createdAt = doc.getLong("createdAt") ?: 0L

                    FavoriteItem(
                        type = t,
                        tmdbId = tmdbId,
                        title = title,
                        posterUrl = posterUrl,
                        imdbRating = imdbRating,
                        createdAt = createdAt
                    )
                } ?: emptyList()

                onChanged(items)
            }
    }


}