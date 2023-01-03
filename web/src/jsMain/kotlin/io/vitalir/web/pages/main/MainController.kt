package io.vitalir.web.pages.main

import io.vitalir.web.common.Loadable
import io.vitalir.web.data.RepositoriesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainController(
    private val repositoriesRepository: RepositoriesRepository,
) {

    private val innerState = MutableStateFlow(MainState())
    val state = innerState.asStateFlow()

    suspend fun refreshRepositories() {
        val user = state.value.user.valueOrNull ?: return
        val result = repositoriesRepository.get(user.userId)
        result.fold(
            onSuccess = { repositories ->
                innerState.update { lastState ->
                    lastState.copy(
                        repositories = Loadable.Loaded(repositories),
                    )
                }
            },
            onFailure = {
                innerState.update { lastState ->
                    lastState.copy(
                        repositories = Loadable.Error(IllegalStateException("Cannot fetch repositories")),
                    )
                }
            }
        )
    }
}
