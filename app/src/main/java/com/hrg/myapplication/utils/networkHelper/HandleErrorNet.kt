package com.hrg.myapplication.utils.networkHelper

import com.hrg.myapplication.domain.models.CheckErrorApiClass
import com.hrg.myapplication.domain.models.NewsErrorModel
import com.hrg.myapplication.utils.extensions.toConvertStringJsonToModel

object HandleErrorNet {

    fun mapNetworkThrowable(throwable: Throwable): String? {
        return throwable.message
    }

    fun nullBodyException(): String {
        return "body is null"
    }

    fun parseCustomError(
        responseMessage: String,
        responseCode: Int,
        errorBodyJson: String
    ): CheckErrorApiClass? {
        if (errorBodyJson.isEmpty()) {
            return CheckErrorApiClass(responseCode, responseMessage, null)

        } else if (errorBodyJson.isNotEmpty()) {
            val errorApiModel: NewsErrorModel =
                errorBodyJson.toConvertStringJsonToModel(NewsErrorModel::class.java)
            return CheckErrorApiClass(
                code = responseCode,
                userMessage = errorApiModel.message ?: "",
                errorModel = errorApiModel
            )
        }
        return null
    }
}