package woowacourse.movie.feature.setting

import android.content.Context
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.slot
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import woowacourse.movie.data.notification.NotificationRepository
import woowacourse.movie.data.ticket.FakeTicketRepository
import woowacourse.movie.data.ticket.TicketRepository
import woowacourse.movie.data.ticket.entity.Ticket
import woowacourse.movie.feature.movieId
import woowacourse.movie.feature.screeningDate
import woowacourse.movie.feature.screeningTime
import woowacourse.movie.feature.selectedSeats
import woowacourse.movie.feature.theaterName
import woowacourse.movie.model.notification.TicketAlarm

class SettingPresenterTest {
    private lateinit var view: SettingContract.View
    private lateinit var applicationContext: Context
    private lateinit var notificationRepository: NotificationRepository
    private lateinit var ticketAlarm: TicketAlarm
    private lateinit var ticketRepository: TicketRepository
    private lateinit var presenter: SettingContract.Presenter
    private val ticketCount = 3

    @BeforeEach
    fun setUp() {
        view = mockk()
        applicationContext = mockk()
        notificationRepository = mockk()
        ticketAlarm = mockk()
        ticketRepository = FakeTicketRepository()
        presenter = SettingPresenter(view, applicationContext, notificationRepository, ticketAlarm)
        repeat(ticketCount) {
            ticketRepository.save(
                movieId,
                screeningDate,
                screeningTime,
                selectedSeats,
                theaterName,
            )
        }
    }

    @Test
    fun `알림 수신 여부를 불러온다`() {
        // given
        val isGrantSlot = slot<Boolean>()
        every { notificationRepository.isGrant() } returns true
        every { view.initializeSwitch(capture(isGrantSlot)) } just runs

        // when
        presenter.loadNotificationGrant()

        // then
        val actual = isGrantSlot.captured
        assertThat(actual).isTrue
        verify { view.initializeSwitch(any()) }
    }

    @Test
    fun `알림 수신 여부를 업데이트한다`() {
        // given
        val isGrantSlot = slot<Boolean>()
        every { notificationRepository.update(capture(isGrantSlot)) } just runs

        // when
        presenter.updateNotificationGrant(true)

        // then
        val actual = isGrantSlot.captured
        assertThat(actual).isTrue
        verify { notificationRepository.update(actual) }
    }

    @Test
    fun `예매한 티켓들의 알림을 설정한다`() {
        // given
        val ticketSlot = slot<List<Ticket>>()
        every { ticketAlarm.setReservationAlarms(capture(ticketSlot)) } just runs

        // when
        presenter.setTicketsAlarm(ticketRepository)

        // then
        val actual = ticketSlot.captured
        assertThat(actual).hasSize(3)
        verify { ticketAlarm.setReservationAlarms(actual) }
    }

    @Test
    fun `예매한 티켓들의 알림을 취소한다`() {
        // given
        val ticketSlot = slot<List<Ticket>>()
        every { ticketAlarm.cancelReservationAlarms(capture(ticketSlot)) } just runs

        // when
        presenter.cancelTicketsAlarm(ticketRepository)

        // then
        val actual = ticketSlot.captured
        assertThat(actual).hasSize(3)
        verify { ticketAlarm.cancelReservationAlarms(actual) }
    }
}
