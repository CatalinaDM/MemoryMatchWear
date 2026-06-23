package mx.utng.ccdm.memorymatch.domain.usecase


import mx.utng.ccdm.memorymatch.domain.repository.BestTimeRepository

class SaveBestTimeUseCase(private val repository: BestTimeRepository) {
    suspend operator fun invoke(seconds: Long) = repository.saveBestTime(seconds)
}