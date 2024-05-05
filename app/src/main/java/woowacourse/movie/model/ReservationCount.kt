package woowacourse.movie.model

class ReservationCount(val count: Int = DEFAULT_COUNT) {
    init {
        require(count in MIN_COUNT..MAX_COUNT) { INVALID_COUNT_MESSAGE }
    }

    fun update(count: Int): ReservationCount {
        return ReservationCount(count)
    }

    operator fun dec(): ReservationCount {
        if (count == MIN_COUNT) return this
        return ReservationCount(count - OFFSET_COUNT)
    }

    operator fun inc(): ReservationCount {
        if (count == MAX_COUNT) return this
        return ReservationCount(count + OFFSET_COUNT)
    }

    companion object {
        private const val DEFAULT_COUNT = 1
        private const val MIN_COUNT = 1
        private const val MAX_COUNT = 10
        private const val OFFSET_COUNT = 1
        private const val INVALID_COUNT_MESSAGE = "예매 가능 인원은 ${MIN_COUNT}명 ~ ${MAX_COUNT}명 입니다."
    }
}
