package com.example.legocatalog.legoset.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.legocatalog.data.resultLiveData
import com.example.legocatalog.testing.OpenForTesting
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository module for handling data operations.
 */
@Singleton
@OpenForTesting
class LegoSetRepository @Inject constructor(private val dao: LegoSetDao,
                                            private val legoSetRemoteDataSource: LegoSetRemoteDataSource) {

    //unfortunately this api not working so we have to fetch data from database only that stored in assets
    fun observePagedSets(connectivityAvailable: Boolean, themeId: Int? = null,
                         coroutineScope: CoroutineScope) =
          /*  if (connectivityAvailable) observeRemotePagedSets(themeId, coroutineScope)
            else*/ observeLocalPagedSets(themeId)

    private fun observeLocalPagedSets(themeId: Int? = null): LiveData<PagedList<LegoSet>> {
        val dataSourceFactory =
                if (themeId == null) dao.getPagedLegoSets()
                else dao.getPagedLegoSetsByTheme(themeId)

        return LivePagedListBuilder(dataSourceFactory,
                LegoSetPageDataSourceFactory.pagedListConfig()).build()
    }

    private fun observeRemotePagedSets(themeId: Int? = null, ioCoroutineScope: CoroutineScope)
            : LiveData<PagedList<LegoSet>> {
        val dataSourceFactory = LegoSetPageDataSourceFactory(themeId, legoSetRemoteDataSource,
                dao, ioCoroutineScope)
        return LivePagedListBuilder(dataSourceFactory,
                LegoSetPageDataSourceFactory.pagedListConfig()).build()
    }

    //data would be fetch from remote and added to db if data are different then previous and will be change only if data are diff
    fun observeSet(id: String) = resultLiveData(
            databaseQuery = { dao.getLegoSet(id) },
            networkCall = { legoSetRemoteDataSource.fetchSet(id) },
            saveCallResult = { dao.insert(it) })
            .distinctUntilChanged()

    fun observeSetsByTheme(themeId: Int) = resultLiveData(
            databaseQuery = { dao.getLegoSets(themeId) },
            networkCall = { legoSetRemoteDataSource.fetchSets(1, PAGE_SIZE, themeId) },
            saveCallResult = { dao.insertAll(it.results) })

    companion object {

        const val PAGE_SIZE = 100

        // For Singleton instantiation
        @Volatile
        private var instance: LegoSetRepository? = null

        fun getInstance(dao: LegoSetDao, legoSetRemoteDataSource: LegoSetRemoteDataSource) =
                instance ?: synchronized(this) {
                    instance
                            ?: LegoSetRepository(dao, legoSetRemoteDataSource).also { instance = it }
                }
    }
}
