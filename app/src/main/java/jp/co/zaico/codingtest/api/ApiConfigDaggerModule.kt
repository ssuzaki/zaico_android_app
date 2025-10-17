package jp.co.zaico.codingtest.api

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jp.co.zaico.codingtest.R
import javax.inject.Named

/**
 * API設定を提供するDaggerモジュール
 */
@Module
@InstallIn(SingletonComponent::class)
object ApiConfigDaggerModule {

    @Provides
    @Named("BASE_URL")
    fun provideBaseUrl(@ApplicationContext ctx: Context): String {
        val raw = ctx.getString(R.string.api_endpoint)
        return if (raw.endsWith("/")) raw else "$raw/"
    }

    @Provides
    @Named("API_TOKEN")
    fun provideApiToken(@ApplicationContext ctx: Context): String =
        ctx.getString(R.string.api_token)
}
