package com.example.oamkhub.data.repository

import com.example.oamkhub.data.model.FoundComment
import com.example.oamkhub.data.model.LostProduct
import com.example.oamkhub.data.network.RetrofitInstance
import retrofit2.Response

class LostFoundRepository {
    suspend fun getAllLostProducts(token: String): Response<List<LostProduct>> {
        return RetrofitInstance.api.getAllLostProducts("Bearer $token")
    }

    suspend fun getFoundComments(lostProductId: String, token: String): Response<List<FoundComment>> {
        return RetrofitInstance.api.getFoundComments(lostProductId, "Bearer $token")
    }
}
