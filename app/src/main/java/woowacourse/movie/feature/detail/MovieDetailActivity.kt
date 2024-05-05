package woowacourse.movie.feature.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.StringRes
import woowacourse.movie.R
import woowacourse.movie.data.MovieRepository
import woowacourse.movie.databinding.ActivityMovieDetailBinding
import woowacourse.movie.feature.detail.ui.MovieDetailUiModel
import woowacourse.movie.feature.seatselection.MovieSeatSelectionActivity
import woowacourse.movie.model.Movie
import woowacourse.movie.util.BaseActivity
import woowacourse.movie.util.MovieIntentConstant.INVALID_VALUE_MOVIE_ID
import woowacourse.movie.util.MovieIntentConstant.INVALID_VALUE_THEATER_INDEX
import woowacourse.movie.util.MovieIntentConstant.KEY_MOVIE_ID
import woowacourse.movie.util.MovieIntentConstant.KEY_SELECTED_THEATER_INDEX

class MovieDetailActivity :
    BaseActivity<MovieDetailContract.Presenter>(),
    MovieDetailContract.View {
    private lateinit var binding: ActivityMovieDetailBinding

    private val movieId: Long by lazy { intent.getLongExtra(KEY_MOVIE_ID, INVALID_VALUE_MOVIE_ID) }
    private val selectedTheaterPosition: Int by lazy {
        intent.getIntExtra(KEY_SELECTED_THEATER_INDEX, INVALID_VALUE_THEATER_INDEX)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeView()
    }

    override fun initializePresenter() = MovieDetailPresenter(this)

    private fun initializeView() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        presenter.loadMovieDetail(movieId)
        setUpButtonListener()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        val screeningDateSpinnerPosition =
            savedInstanceState.getInt(SCREENING_DATE_SPINNER_POSITION_KEY)
        binding.spScreeningDate.setSelection(screeningDateSpinnerPosition)

        val screeningTimeSpinnerPosition =
            savedInstanceState.getInt(SCREENING_TIME_SPINNER_POSITION_KEY)
        binding.spScreeningTime.setSelection(screeningTimeSpinnerPosition)

        val movieCount = savedInstanceState.getInt(MOVIE_COUNT_KEY)
        presenter.updateReservationCount(movieCount)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

    private fun setUpButtonListener() {
        binding.btnPlus.setOnClickListener {
            presenter.plusReservationCount()
        }

        binding.btnMinus.setOnClickListener {
            presenter.minusReservationCount()
        }

        binding.btnSeatSelection.setOnClickListener {
            presenter.reserveMovie(
                movieId,
                binding.spScreeningDate.selectedItem.toString(),
                binding.spScreeningTime.selectedItem.toString(),
            )
        }
    }

    override fun displayMovieDetail(movie: Movie) {
        binding.movie = MovieDetailUiModel.of(movie, selectedTheaterPosition)
    }

    override fun updateCountView(count: Int) {
        binding.movieCount = count
    }

    override fun navigateToSeatSelectionView(
        id: Long,
        date: String,
        time: String,
        count: Int,
    ) {
        val movie = MovieRepository.getMovieById(movieId)
        val theaterName = movie.theaters[selectedTheaterPosition].name

        val intent = MovieSeatSelectionActivity.newIntent(this, id, date, time, count, theaterName)
        startActivity(intent)
    }

    override fun showToastInvalidMovieIdError(throwable: Throwable) {
        Log.e(TAG, "invalid movie id - ${throwable.message}")
        showToast(R.string.invalid_movie_id)
        finish()
    }

    private fun showToast(
        @StringRes stringResId: Int,
    ) {
        Toast.makeText(this, resources.getString(stringResId), Toast.LENGTH_SHORT).show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val screeningDateSpinnerPosition = binding.spScreeningDate.selectedItemPosition
        outState.putInt(SCREENING_DATE_SPINNER_POSITION_KEY, screeningDateSpinnerPosition)

        val screeningTimeSpinnerPosition = binding.spScreeningTime.selectedItemPosition
        outState.putInt(SCREENING_TIME_SPINNER_POSITION_KEY, screeningTimeSpinnerPosition)

        val movieCount = binding.movieCount
        outState.putInt(MOVIE_COUNT_KEY, movieCount)

        super.onSaveInstanceState(outState)
    }

    companion object {
        private val TAG = MovieDetailActivity::class.simpleName
        private const val SCREENING_DATE_SPINNER_POSITION_KEY = "screeningDateSpinnerPosition"
        private const val SCREENING_TIME_SPINNER_POSITION_KEY = "screeningTimeSpinnerPosition"
        private const val MOVIE_COUNT_KEY = "movieCount"

        fun newIntent(
            context: Context,
            movieId: Long,
            selectedTheaterIndex: Int,
        ): Intent {
            return Intent(context, MovieDetailActivity::class.java).apply {
                putExtra(KEY_MOVIE_ID, movieId)
                putExtra(KEY_SELECTED_THEATER_INDEX, selectedTheaterIndex)
            }
        }
    }
}
