package woowacourse.movie.feature.home.theater

import woowacourse.movie.data.movie.MovieRepository

class TheaterSelectionPresenter(private val view: TheaterSelectionContract.View) :
    TheaterSelectionContract.Presenter {
    override fun loadTheaters(movieId: Long) {
        val movie =
            runCatching {
                MovieRepository.getMovieById(movieId)
            }.getOrElse {
                view.showToastInvalidMovieIdError(it)
                return
            }

        view.setUpTheaterAdapter(movie.theaters)
    }
}
