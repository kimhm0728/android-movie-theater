package woowacourse.movie.feature.result

import woowacourse.movie.model.MovieTicket

interface MovieResultContract {
    interface View {
        fun displayMovieTicket(movieTicketData: MovieTicket?)
    }

    interface Presenter {
        fun loadMovieTicket(
            id: Long,
            date: String,
            time: String,
            count: Int,
            seats: String,
            theaterName: String,
        )
    }
}
