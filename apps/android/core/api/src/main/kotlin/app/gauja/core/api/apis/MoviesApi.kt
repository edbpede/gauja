// SPDX-FileCopyrightText: 2026 Gauja contributors
// SPDX-License-Identifier: AGPL-3.0-or-later
// GENERATED — do not edit. Run tools/codegen/generate.sh.
package app.gauja.core.api.apis

import app.gauja.core.api.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

import app.gauja.core.api.models.GetDiscoverMovies200Response
import app.gauja.core.api.models.GetMovieByMovieIdRatings200Response
import app.gauja.core.api.models.GetMovieByMovieIdRatingscombined200Response
import app.gauja.core.api.models.MovieDetails

interface MoviesApi {
    /**
     * GET movie/{movieId}
     * Get movie details
     * Returns full movie details in a JSON object.
     * Responses:
     *  - 200: Movie details
     *
     * @param movieId 
     * @param language  (optional)
     * @return [MovieDetails]
     */
    @GET("movie/{movieId}")
    suspend fun getMovieByMovieId(@Path("movieId") movieId: kotlin.Double, @Query("language") language: kotlin.String? = null): Response<MovieDetails>

    /**
     * GET movie/{movieId}/ratings
     * Get movie ratings
     * Returns ratings based on the provided movieId in a JSON object.
     * Responses:
     *  - 200: Ratings returned
     *
     * @param movieId 
     * @return [GetMovieByMovieIdRatings200Response]
     */
    @GET("movie/{movieId}/ratings")
    suspend fun getMovieByMovieIdRatings(@Path("movieId") movieId: kotlin.Double): Response<GetMovieByMovieIdRatings200Response>

    /**
     * GET movie/{movieId}/ratingscombined
     * Get RT and IMDB movie ratings combined
     * Returns ratings from RottenTomatoes and IMDB based on the provided movieId in a JSON object.
     * Responses:
     *  - 200: Ratings returned
     *
     * @param movieId 
     * @return [GetMovieByMovieIdRatingscombined200Response]
     */
    @GET("movie/{movieId}/ratingscombined")
    suspend fun getMovieByMovieIdRatingscombined(@Path("movieId") movieId: kotlin.Double): Response<GetMovieByMovieIdRatingscombined200Response>

    /**
     * GET movie/{movieId}/recommendations
     * Get recommended movies
     * Returns list of recommended movies based on provided movie ID in a JSON object.
     * Responses:
     *  - 200: List of movies
     *
     * @param movieId 
     * @param page  (optional, default to 1.0)
     * @param language  (optional)
     * @return [GetDiscoverMovies200Response]
     */
    @GET("movie/{movieId}/recommendations")
    suspend fun getMovieByMovieIdRecommendations(@Path("movieId") movieId: kotlin.Double, @Query("page") page: kotlin.Double? = 1.0, @Query("language") language: kotlin.String? = null): Response<GetDiscoverMovies200Response>

    /**
     * GET movie/{movieId}/similar
     * Get similar movies
     * Returns list of similar movies based on the provided movieId in a JSON object.
     * Responses:
     *  - 200: List of movies
     *
     * @param movieId 
     * @param page  (optional, default to 1.0)
     * @param language  (optional)
     * @return [GetDiscoverMovies200Response]
     */
    @GET("movie/{movieId}/similar")
    suspend fun getMovieByMovieIdSimilar(@Path("movieId") movieId: kotlin.Double, @Query("page") page: kotlin.Double? = 1.0, @Query("language") language: kotlin.String? = null): Response<GetDiscoverMovies200Response>

}
