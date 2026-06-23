package mx.utng.ccdm.memorymatch.data.repository

import mx.utng.ccdm.memorymatch.data.datasource.BestTimeDataSource
import mx.utng.ccdm.memorymatch.domain.repository.BestTimeRepository

class BestTimeRepositoryImpl(private val ds: BestTimeDataSource) : BestTimeRepository {
    override suspend fun getBestTime() = ds.getBestTime()
    override suspend fun saveBestTime(s: Long) = ds.saveBestTime(s)
}
