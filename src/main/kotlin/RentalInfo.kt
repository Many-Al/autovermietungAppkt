
import java.time.LocalDateTime

data class RentalInfo(
    val pickUpLocation: String,
    val dropOffLocation: String,
    val startDate: LocalDateTime,
    val rentalDays: Int,
    val endDate : LocalDateTime,
    val withDriverCost: Double,
    val carCategory: String,
    val numberOfSeats: Int,
    val additionalSeatsCost: Double,
    val excessKilometersCost: Double,
    val insuranceAmount: Double
)