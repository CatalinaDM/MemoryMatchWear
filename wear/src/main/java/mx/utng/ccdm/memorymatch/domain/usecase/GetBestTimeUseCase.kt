package mx.utng.ccdm.memorymatch.domain.usecase


import mx.utng.ccdm.memorymatch.domain.repository.BestTimeRepository

class GetBestTimeUseCase(private val repository: BestTimeRepository) {
    suspend operator fun invoke(): Long = repository.getBestTime()
}