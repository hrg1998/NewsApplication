package com.hrg.myapplication.utils.typeConverter

import androidx.room.TypeConverter
import com.hrg.myapplication.domain.models.Source
import com.hrg.myapplication.utils.extensions.toConvertModelToJson
import com.hrg.myapplication.utils.extensions.toConvertStringJsonToModel

class DBTypeConverter {
    @TypeConverter
    fun fromSourceToString(source: Source?): String {
        return source?.toConvertModelToJson(Source::class.java) ?: ""
    }

    @TypeConverter
    fun fromStringToSource(str: String): Source {
        return if (str.isEmpty())
            Source(null, null)
        else
            str.toConvertStringJsonToModel(Source::class.java)
    }
}