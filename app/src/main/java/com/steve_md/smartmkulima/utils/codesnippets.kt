package com.steve_md.smartmkulima.utils


//////// combine
/**
 * suspend fun loadScreenData(isRefreshedGames; Boolean, date: String) = withContext(ioDispatcher) {
 *     try {
 *         viewModelScope.launch(coroutineExceptionHandler) {
 *             val todayOnlyGamesFlow = repository.getGames(localDate.format(date)).map { it.games }
 *             val anyDayGamesFlow = repository.getGames(_selectedDate.value).map { it.games }
 *             val teamsFlow = repository.getStandingsNow().map { it }
 *
 *             combine(todayOnlyGamesFlow, anyDayGamesFlow, teamsFlow) { todayGames, anyDayGames, teams ->
 *                 GamesUiState.Success(
 *                     games = if (withRefreshedGames) anyDayGames else todayGames,
 *                     teams = teams
 *                 )
 *             }.collect { uiState ->
 *                 _uiState.emit(uiState)
 *             }
 *         }
 *     } catch (e: Throwable) {
 *         _uiState.emit(GamesUiState.Error(e))
 *     }
 * }
 */