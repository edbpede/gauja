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

import app.gauja.core.api.models.GetDiscoverGenresliderMovie200ResponseInner
import app.gauja.core.api.models.GetDiscoverMovies200Response
import app.gauja.core.api.models.GetDiscoverMoviesGenreByGenreId200Response
import app.gauja.core.api.models.GetDiscoverMoviesLanguageByLanguage200Response
import app.gauja.core.api.models.GetDiscoverMoviesStudioByStudioId200Response
import app.gauja.core.api.models.GetDiscoverTv200Response
import app.gauja.core.api.models.GetDiscoverTvGenreByGenreId200Response
import app.gauja.core.api.models.GetDiscoverTvLanguageByLanguage200Response
import app.gauja.core.api.models.GetDiscoverTvNetworkByNetworkId200Response
import app.gauja.core.api.models.GetSearch200Response
import app.gauja.core.api.models.GetSearchCompany200Response
import app.gauja.core.api.models.GetSearchKeyword200Response
import app.gauja.core.api.models.GetUserByUserIdWatchlist200Response

interface SearchApi {
    /**
     * GET discover/genreslider/movie
     * Get genre slider data for movies
     * Returns a list of genres with backdrops attached
     * Responses:
     *  - 200: Genre slider data returned
     *
     * @param language  (optional)
     * @return [kotlin.collections.List<GetDiscoverGenresliderMovie200ResponseInner>]
     */
    @GET("discover/genreslider/movie")
    suspend fun getDiscoverGenresliderMovie(@Query("language") language: kotlin.String? = null): Response<kotlin.collections.List<GetDiscoverGenresliderMovie200ResponseInner>>

    /**
     * GET discover/genreslider/tv
     * Get genre slider data for TV series
     * Returns a list of genres with backdrops attached
     * Responses:
     *  - 200: Genre slider data returned
     *
     * @param language  (optional)
     * @return [kotlin.collections.List<GetDiscoverGenresliderMovie200ResponseInner>]
     */
    @GET("discover/genreslider/tv")
    suspend fun getDiscoverGenresliderTv(@Query("language") language: kotlin.String? = null): Response<kotlin.collections.List<GetDiscoverGenresliderMovie200ResponseInner>>

    /**
     * GET discover/keyword/{keywordId}/movies
     * Get movies from keyword
     * Returns list of movies based on the provided keyword ID a JSON object.
     * Responses:
     *  - 200: List of movies
     *
     * @param keywordId 
     * @param page  (optional, default to 1.0)
     * @param language  (optional)
     * @return [GetDiscoverMovies200Response]
     */
    @GET("discover/keyword/{keywordId}/movies")
    suspend fun getDiscoverKeywordByKeywordIdMovies(@Path("keywordId") keywordId: kotlin.Double, @Query("page") page: kotlin.Double? = 1.0, @Query("language") language: kotlin.String? = null): Response<GetDiscoverMovies200Response>

    /**
     * GET discover/movies
     * Discover movies
     * Returns a list of movies in a JSON object.
     * Responses:
     *  - 200: Results
     *
     * @param page  (optional, default to 1.0)
     * @param language  (optional)
     * @param genre  (optional)
     * @param studio  (optional)
     * @param keywords  (optional)
     * @param excludeKeywords Comma-separated list of keyword IDs to exclude from results (optional)
     * @param sortBy  (optional)
     * @param primaryReleaseDateGte  (optional)
     * @param primaryReleaseDateLte  (optional)
     * @param withRuntimeGte  (optional)
     * @param withRuntimeLte  (optional)
     * @param voteAverageGte  (optional)
     * @param voteAverageLte  (optional)
     * @param voteCountGte  (optional)
     * @param voteCountLte  (optional)
     * @param watchRegion  (optional)
     * @param watchProviders  (optional)
     * @param certification Exact certification to filter by (used when certificationMode is &#39;exact&#39;) (optional)
     * @param certificationGte Minimum certification to filter by (used when certificationMode is &#39;range&#39;) (optional)
     * @param certificationLte Maximum certification to filter by (used when certificationMode is &#39;range&#39;) (optional)
     * @param certificationCountry Country code for the certification system (e.g., US, GB, CA) (optional)
     * @param certificationMode Determines whether to use exact certification matching or a certification range (internal use only, not sent to TMDB API) (optional)
     * @return [GetDiscoverMovies200Response]
     */
    @GET("discover/movies")
    suspend fun getDiscoverMovies(@Query("page") page: kotlin.Double? = 1.0, @Query("language") language: kotlin.String? = null, @Query("genre") genre: kotlin.String? = null, @Query("studio") studio: kotlin.Double? = null, @Query("keywords") keywords: kotlin.String? = null, @Query("excludeKeywords") excludeKeywords: kotlin.String? = null, @Query("sortBy") sortBy: kotlin.String? = null, @Query("primaryReleaseDateGte") primaryReleaseDateGte: kotlin.String? = null, @Query("primaryReleaseDateLte") primaryReleaseDateLte: kotlin.String? = null, @Query("withRuntimeGte") withRuntimeGte: kotlin.Double? = null, @Query("withRuntimeLte") withRuntimeLte: kotlin.Double? = null, @Query("voteAverageGte") voteAverageGte: kotlin.Double? = null, @Query("voteAverageLte") voteAverageLte: kotlin.Double? = null, @Query("voteCountGte") voteCountGte: kotlin.Double? = null, @Query("voteCountLte") voteCountLte: kotlin.Double? = null, @Query("watchRegion") watchRegion: kotlin.String? = null, @Query("watchProviders") watchProviders: kotlin.String? = null, @Query("certification") certification: kotlin.String? = null, @Query("certificationGte") certificationGte: kotlin.String? = null, @Query("certificationLte") certificationLte: kotlin.String? = null, @Query("certificationCountry") certificationCountry: kotlin.String? = null, @Query("certificationMode") certificationMode: kotlin.String? = null): Response<GetDiscoverMovies200Response>

    /**
     * GET discover/movies/genre/{genreId}
     * Discover movies by genre
     * Returns a list of movies based on the provided genre ID in a JSON object.
     * Responses:
     *  - 200: Results
     *
     * @param genreId 
     * @param page  (optional, default to 1.0)
     * @param language  (optional)
     * @return [GetDiscoverMoviesGenreByGenreId200Response]
     */
    @GET("discover/movies/genre/{genreId}")
    suspend fun getDiscoverMoviesGenreByGenreId(@Path("genreId") genreId: kotlin.String, @Query("page") page: kotlin.Double? = 1.0, @Query("language") language: kotlin.String? = null): Response<GetDiscoverMoviesGenreByGenreId200Response>

    /**
     * GET discover/movies/language/{language}
     * Discover movies by original language
     * Returns a list of movies based on the provided ISO 639-1 language code in a JSON object.
     * Responses:
     *  - 200: Results
     *
     * @param language 
     * @param page  (optional, default to 1.0)
     * @param language2  (optional)
     * @return [GetDiscoverMoviesLanguageByLanguage200Response]
     */
    @GET("discover/movies/language/{language}")
    suspend fun getDiscoverMoviesLanguageByLanguage(@Path("language") language: kotlin.String, @Query("page") page: kotlin.Double? = 1.0, @Query("language") language2: kotlin.String? = null): Response<GetDiscoverMoviesLanguageByLanguage200Response>

    /**
     * GET discover/movies/studio/{studioId}
     * Discover movies by studio
     * Returns a list of movies based on the provided studio ID in a JSON object.
     * Responses:
     *  - 200: Results
     *
     * @param studioId 
     * @param page  (optional, default to 1.0)
     * @param language  (optional)
     * @return [GetDiscoverMoviesStudioByStudioId200Response]
     */
    @GET("discover/movies/studio/{studioId}")
    suspend fun getDiscoverMoviesStudioByStudioId(@Path("studioId") studioId: kotlin.String, @Query("page") page: kotlin.Double? = 1.0, @Query("language") language: kotlin.String? = null): Response<GetDiscoverMoviesStudioByStudioId200Response>

    /**
     * GET discover/movies/upcoming
     * Upcoming movies
     * Returns a list of movies in a JSON object.
     * Responses:
     *  - 200: Results
     *
     * @param page  (optional, default to 1.0)
     * @param language  (optional)
     * @return [GetDiscoverMovies200Response]
     */
    @GET("discover/movies/upcoming")
    suspend fun getDiscoverMoviesUpcoming(@Query("page") page: kotlin.Double? = 1.0, @Query("language") language: kotlin.String? = null): Response<GetDiscoverMovies200Response>

    /**
     * GET discover/trending
     * Trending movies and TV
     * Returns a list of movies and TV shows in a JSON object.
     * Responses:
     *  - 200: Results
     *
     * @param page  (optional, default to 1.0)
     * @param language  (optional)
     * @param mediaType  (optional, default to "all")
     * @param timeWindow  (optional, default to "day")
     * @return [GetSearch200Response]
     */
    @GET("discover/trending")
    suspend fun getDiscoverTrending(@Query("page") page: kotlin.Double? = 1.0, @Query("language") language: kotlin.String? = null, @Query("mediaType") mediaType: kotlin.String? = "all", @Query("timeWindow") timeWindow: kotlin.String? = "day"): Response<GetSearch200Response>

    /**
     * GET discover/tv
     * Discover TV shows
     * Returns a list of TV shows in a JSON object.
     * Responses:
     *  - 200: Results
     *
     * @param page  (optional, default to 1.0)
     * @param language  (optional)
     * @param genre  (optional)
     * @param network  (optional)
     * @param keywords  (optional)
     * @param excludeKeywords Comma-separated list of keyword IDs to exclude from results (optional)
     * @param sortBy  (optional)
     * @param firstAirDateGte  (optional)
     * @param firstAirDateLte  (optional)
     * @param withRuntimeGte  (optional)
     * @param withRuntimeLte  (optional)
     * @param voteAverageGte  (optional)
     * @param voteAverageLte  (optional)
     * @param voteCountGte  (optional)
     * @param voteCountLte  (optional)
     * @param watchRegion  (optional)
     * @param watchProviders  (optional)
     * @param status  (optional)
     * @param certification Exact certification to filter by (used when certificationMode is &#39;exact&#39;) (optional)
     * @param certificationGte Minimum certification to filter by (used when certificationMode is &#39;range&#39;) (optional)
     * @param certificationLte Maximum certification to filter by (used when certificationMode is &#39;range&#39;) (optional)
     * @param certificationCountry Country code for the certification system (e.g., US, GB, CA) (optional)
     * @param certificationMode Determines whether to use exact certification matching or a certification range (internal use only, not sent to TMDB API) (optional)
     * @return [GetDiscoverTv200Response]
     */
    @GET("discover/tv")
    suspend fun getDiscoverTv(@Query("page") page: kotlin.Double? = 1.0, @Query("language") language: kotlin.String? = null, @Query("genre") genre: kotlin.String? = null, @Query("network") network: kotlin.Double? = null, @Query("keywords") keywords: kotlin.String? = null, @Query("excludeKeywords") excludeKeywords: kotlin.String? = null, @Query("sortBy") sortBy: kotlin.String? = null, @Query("firstAirDateGte") firstAirDateGte: kotlin.String? = null, @Query("firstAirDateLte") firstAirDateLte: kotlin.String? = null, @Query("withRuntimeGte") withRuntimeGte: kotlin.Double? = null, @Query("withRuntimeLte") withRuntimeLte: kotlin.Double? = null, @Query("voteAverageGte") voteAverageGte: kotlin.Double? = null, @Query("voteAverageLte") voteAverageLte: kotlin.Double? = null, @Query("voteCountGte") voteCountGte: kotlin.Double? = null, @Query("voteCountLte") voteCountLte: kotlin.Double? = null, @Query("watchRegion") watchRegion: kotlin.String? = null, @Query("watchProviders") watchProviders: kotlin.String? = null, @Query("status") status: kotlin.String? = null, @Query("certification") certification: kotlin.String? = null, @Query("certificationGte") certificationGte: kotlin.String? = null, @Query("certificationLte") certificationLte: kotlin.String? = null, @Query("certificationCountry") certificationCountry: kotlin.String? = null, @Query("certificationMode") certificationMode: kotlin.String? = null): Response<GetDiscoverTv200Response>

    /**
     * GET discover/tv/genre/{genreId}
     * Discover TV shows by genre
     * Returns a list of TV shows based on the provided genre ID in a JSON object.
     * Responses:
     *  - 200: Results
     *
     * @param genreId 
     * @param page  (optional, default to 1.0)
     * @param language  (optional)
     * @return [GetDiscoverTvGenreByGenreId200Response]
     */
    @GET("discover/tv/genre/{genreId}")
    suspend fun getDiscoverTvGenreByGenreId(@Path("genreId") genreId: kotlin.String, @Query("page") page: kotlin.Double? = 1.0, @Query("language") language: kotlin.String? = null): Response<GetDiscoverTvGenreByGenreId200Response>

    /**
     * GET discover/tv/language/{language}
     * Discover TV shows by original language
     * Returns a list of TV shows based on the provided ISO 639-1 language code in a JSON object.
     * Responses:
     *  - 200: Results
     *
     * @param language 
     * @param page  (optional, default to 1.0)
     * @param language2  (optional)
     * @return [GetDiscoverTvLanguageByLanguage200Response]
     */
    @GET("discover/tv/language/{language}")
    suspend fun getDiscoverTvLanguageByLanguage(@Path("language") language: kotlin.String, @Query("page") page: kotlin.Double? = 1.0, @Query("language") language2: kotlin.String? = null): Response<GetDiscoverTvLanguageByLanguage200Response>

    /**
     * GET discover/tv/network/{networkId}
     * Discover TV shows by network
     * Returns a list of TV shows based on the provided network ID in a JSON object.
     * Responses:
     *  - 200: Results
     *
     * @param networkId 
     * @param page  (optional, default to 1.0)
     * @param language  (optional)
     * @return [GetDiscoverTvNetworkByNetworkId200Response]
     */
    @GET("discover/tv/network/{networkId}")
    suspend fun getDiscoverTvNetworkByNetworkId(@Path("networkId") networkId: kotlin.String, @Query("page") page: kotlin.Double? = 1.0, @Query("language") language: kotlin.String? = null): Response<GetDiscoverTvNetworkByNetworkId200Response>

    /**
     * GET discover/tv/upcoming
     * Discover Upcoming TV shows
     * Returns a list of upcoming TV shows in a JSON object.
     * Responses:
     *  - 200: Results
     *
     * @param page  (optional, default to 1.0)
     * @param language  (optional)
     * @return [GetDiscoverTv200Response]
     */
    @GET("discover/tv/upcoming")
    suspend fun getDiscoverTvUpcoming(@Query("page") page: kotlin.Double? = 1.0, @Query("language") language: kotlin.String? = null): Response<GetDiscoverTv200Response>

    /**
     * GET discover/watchlist
     * Get the Plex watchlist.
     * 
     * Responses:
     *  - 200: Watchlist data returned
     *
     * @param page  (optional, default to 1.0)
     * @return [GetUserByUserIdWatchlist200Response]
     */
    @GET("discover/watchlist")
    suspend fun getDiscoverWatchlist(@Query("page") page: kotlin.Double? = 1.0): Response<GetUserByUserIdWatchlist200Response>

    /**
     * GET search
     * Search for movies, TV shows, or people
     * Returns a list of movies, TV shows, or people a JSON object.
     * Responses:
     *  - 200: Results
     *
     * @param query 
     * @param page  (optional, default to 1.0)
     * @param language  (optional)
     * @return [GetSearch200Response]
     */
    @GET("search")
    suspend fun getSearch(@Query("query") query: kotlin.String, @Query("page") page: kotlin.Double? = 1.0, @Query("language") language: kotlin.String? = null): Response<GetSearch200Response>

    /**
     * GET search/company
     * Search for companies
     * Returns a list of TMDB companies matching the search query. (Will not return origin country)
     * Responses:
     *  - 200: Results
     *
     * @param query 
     * @param page  (optional, default to 1.0)
     * @return [GetSearchCompany200Response]
     */
    @GET("search/company")
    suspend fun getSearchCompany(@Query("query") query: kotlin.String, @Query("page") page: kotlin.Double? = 1.0): Response<GetSearchCompany200Response>

    /**
     * GET search/keyword
     * Search for keywords
     * Returns a list of TMDB keywords matching the search query
     * Responses:
     *  - 200: Results
     *
     * @param query 
     * @param page  (optional, default to 1.0)
     * @return [GetSearchKeyword200Response]
     */
    @GET("search/keyword")
    suspend fun getSearchKeyword(@Query("query") query: kotlin.String, @Query("page") page: kotlin.Double? = 1.0): Response<GetSearchKeyword200Response>

}
