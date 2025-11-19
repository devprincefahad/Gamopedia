package dev.prince.gamopedia.repo

interface GamesRepository {

    fun helloWorld() : String

}

class GamesRepositoryImpl : GamesRepository {
    override fun helloWorld() : String{
        return "Hello World"
    }
}