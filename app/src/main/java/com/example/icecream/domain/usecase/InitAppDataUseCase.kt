package com.example.icecream.domain.usecase

import com.example.icecream.domain.repository.ExtraRepository
import com.example.icecream.domain.repository.IceCreamRepository
import javax.inject.Inject

class InitAppDataUseCase @Inject constructor(
    private val iceCreamRepository: IceCreamRepository,
    private val extraRepository: ExtraRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return try {
            iceCreamRepository.fetchAndStoreIceCreams()
            extraRepository.fetchAndStoreExtras()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
