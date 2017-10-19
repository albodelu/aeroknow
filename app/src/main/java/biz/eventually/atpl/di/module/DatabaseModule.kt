package biz.eventually.atpl.di.module

import android.arch.persistence.room.Room
import android.content.Context
import android.graphics.Bitmap
import android.os.Debug
import biz.eventually.atpl.BuildConfig
import biz.eventually.atpl.data.DataProvider
import biz.eventually.atpl.data.dao.SourceDao
import biz.eventually.atpl.ui.source.SourceRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


/**
 * Created by Thibault de Lambilly on 17/10/17.
 */
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(context: Context) : AppDatabase {
        val builder = Room.databaseBuilder(context, AppDatabase::class.java,  "aeroknow.db").fallbackToDestructiveMigration()

        if (Debug.isDebuggerConnected()) {
            builder.allowMainThreadQueries()
        }
        return builder.build()
    }

    @Singleton
    @Provides
    fun provideSourceDao(db: AppDatabase) : SourceDao {
        return db.sourceDao()
    }

    @Singleton
    @Provides
    fun provideSourceRepository(dataProvider: DataProvider, dao: SourceDao): SourceRepository {
        return SourceRepository(dataProvider, dao)
    }
}