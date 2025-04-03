package pl.edu.pjatk.s25819.przeterminarz.repositories

import pl.edu.pjatk.s25819.przeterminarz.data.InMemoryGoodsRepository

class RepositoryLocator {

    val goodsRepository : GoodsRepository = InMemoryGoodsRepository
}