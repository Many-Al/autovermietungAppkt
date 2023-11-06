


import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.system.exitProcess


class CarRentalApp {
    private val accountManager = AccountManager()
    private var loggedInUser: User? = null
    private val cars: List<Car> = listOf(
        Car(1, "BMW", 70.0, "4.0L 8-cylinder", "gas", 2),
        Car(2, "Honda Civic", 45.0, "2.0L 4-cylinder", "diesel", 2),
        Car(3, "Tesla Model S", 100.0, "Electric", "electric", 2),
        Car(4, "Toyota Camry", 60.0, "2.5L 4-cylinder", "gas", 4),
        Car(5, "Ford Fusion", 55.0, "2.0L 4-cylinder", "diesel", 4),
        Car(6, "Volkswagen Passat", 58.0, "2.0L 4-cylinder", "electric", 4),
        Car(7, "Chevrolet Express", 80.0, "6.0L V8", "gas", 9),
        Car(8, "Ford Transit", 85.0, "3.5L V6", "diesel", 9),
        Car(9, "Mercedes-Benz Sprinter", 90.0, "3.0L V6", "electric", 9),
        Car(10, "Toyota Corolla", 40.0, "2.0L 4-cylinder", "gas", 2),
        Car(11, "Nissan Altima", 55.0, "2.5L 4-cylinder", "gas", 4),
        Car(12, "Toyota Sienna", 65.0, "3.5L V6", "gas", 4),
        Car(13, "Audi A3", 80.0, "2.0L 4-cylinder", "gas", 2),
        Car(14, "Ford Mustang", 120.0, "5.0L V8", "gas", 2),
        Car(15, "Kia Sportage", 50.0, "2.4L 4-cylinder", "gas", 4),
        Car(16, "Honda Pilot", 70.0, "3.5L V6", "gas", 4),
        Car(17, "Mercedes-Benz GLS", 110.0, "4.0L V8", "diesel", 4),
        Car(18, "Chevrolet Suburban", 95.0, "5.3L V8", "gas", 9),
        Car(19, "Ford E350", 85.0, "5.4L V8", "gas", 9),
        Car(20, "Tesla Model X", 130.0, "Electric", "electric", 9),
        Car(21, "Volkswagen Atlas", 70.0, "3.6L V6", "gas", 4),
        Car(22, "Toyota Land Cruiser", 95.0, "5.7L V8", "gas", 9),
        Car(23, "Lexus GX", 90.0, "4.6L V8", "gas", 7)
    )
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")


    fun start() {
        println("Welcome to the Car Rental App")

        if (loggedInUser == null) {
            loggedInUser = login()
        } else {
            loggedInUser!!.rentalInfo = RentalInfo(
                "",
                "",
                LocalDateTime.now(),
                0,
                LocalDateTime.now(),
                0.0,
                "",
                0,
                0.0,
                0.0,
                0.0
            )
        }
        val selectedCar = enterRentalDetails(loggedInUser!!)
        payForRental(selectedCar, loggedInUser!!.rentalInfo)

        while (true) {
            println("Do you want to continue?")
            println("1. Yes")
            println("2. No")
            print("Enter your choice (1/2): ")
            when (readlnOrNull()?.toIntOrNull()) {
                1 -> {
                    start()
                }

                2 -> {
                    println("Thank you for using the Car Rental App.")
                    exitProcess(0)
                }

                else -> {
                    println("Invalid Choice. please enter 1 or 2 only.")
                }
            }
        }
    }

    private fun login(): User {
        var loggedInUser: User? = null

        while (loggedInUser == null) {
            println("Please log in to your account")
            print("Enter your email: ")
            val email = readlnOrNull() ?: ""
            print("Enter your password: ")
            val password = readlnOrNull() ?: ""

            val account = accountManager.login(email, password)
            if (account != null) {
                println("Logged in as ${account.name}")
                loggedInUser = User(
                    account,
                    RentalInfo(
                        "",
                        "",
                        LocalDateTime.now(),
                        0,
                        LocalDateTime.now(),
                        0.0,
                        "",
                        0,
                        0.0,
                        0.0,
                        0.0
                    )
                )
            } else {
                println("Login failed. Please try again.")
            }
        }

        return loggedInUser
    }

    private fun enterRentalDetails(loggedInUser: User): Car {
        println("Enter rental details:")
        print("Pick-up location: ")
        val pickUpLocation = readlnOrNull() ?: ""
        print("Drop-off location: ")
        val dropOffLocation = readlnOrNull() ?: ""

        var startDate: LocalDateTime?
        while (true) {
            print("Enter the start date and time (e.g., ${LocalDateTime.now().format(formatter)}): ")
            val startDateStr = readlnOrNull() ?: ""
            try {
                startDate = LocalDateTime.parse(startDateStr, formatter)
                break
            } catch (e: Exception) {
                println("Invalid input. Please enter a valid date and time in the format yyyy-MM-dd HH:mm.")
            }
        }

        var rentalDays = 0
        while (rentalDays !in setOf(1, 2, 3, 4, 5, 6, 7)) {
            println("Enter how many days you want to rent (1 to 7): ")
            val userInput = readlnOrNull()?.toIntOrNull() ?: 0

            if (userInput in setOf(1, 2, 3, 4, 5, 6, 7)) {
                rentalDays = userInput
            } else {
                println("Invalid input. Please enter from 1 to 7 only.")
            }
        }

        val endDate = startDate?.plusDays(rentalDays.toLong())

        var insuranceAmount = 0.0
        while (true) {
            println("Do you want to insure a car this may increase the amount by $50?")
            println("1. Yes")
            println("2. No")
            print("Enter your choice (1/2): ")
            when (readlnOrNull()?.toIntOrNull()) {
                1 -> {
                    insuranceAmount = 50.0
                    break
                }

                2 -> {
                    break
                }

                else -> println("Invalid input. Please enter 1 or 2 only.")
            }
        }

        var withDriverCost = 0.0

        while (true) {
            println("Do you want a driver? This may increase the amount by $30.")
            println("1. Yes")
            println("2. No")
            print("Enter your choice (1/2): ")

            when (readlnOrNull()?.toIntOrNull()) {
                1 -> {
                    withDriverCost = 30.0
                    break
                }

                2 -> {
                    while (true) {
                        println("Do you older than 22 years?")
                        println("1. Yes")
                        println("2. No")
                        print("Enter your choice (1/2): ")
                        when (readlnOrNull()?.toIntOrNull()) {
                            1 -> {
                                while (true) {
                                    println("Do you have a driver's license?")
                                    println("1. Yes")
                                    println("2. No")
                                    print("Enter your choice (1/2): ")
                                    when (readlnOrNull()?.toIntOrNull()) {
                                        1 -> {
                                            break
                                        }

                                        2 -> {
                                            println("You must have a valid driver's license to rent without a driver.")
                                            exitProcess(0)
                                        }

                                        else -> {
                                            println("Invalid choice. please enter 1 or 2 only.")
                                        }
                                    }
                                }
                                break
                            }

                            2 -> {
                                println("You must be at least 22 years old to rent without a driver.")
                                exitProcess(0)
                            }

                            else -> {
                                println("Invalid choice. please enter 1 or 2 only.")
                            }
                        }
                    }
                    break
                }

                else -> {
                    println("Invalid input. please enter 1 or 2 only.")
                }
            }
        }

        var carCategory: String? = null
        while (carCategory == null) {
            println("Choose the car category")
            println("1. gas")
            println("2. diesel")
            println("3. electric")
            print("Enter your choice (1/2/3): ")

            when (readlnOrNull()?.toIntOrNull()) {
                1 -> carCategory = "gas"
                2 -> carCategory = "diesel"
                3 -> carCategory = "electric"
                else -> println("Invalid input. Please enter 1, 2, or 3 only.")
            }
        }


        var numberOfSeats = 0

        while (numberOfSeats !in setOf(2, 4, 9)) {
            print("Choose the number of seats (2, 4, 9): ")
            val userInput = readlnOrNull()?.toIntOrNull() ?: 0

            if (userInput in setOf(2, 4, 9)) {
                numberOfSeats = userInput
            } else {
                println("Invalid input. Please enter from 2, 4, or 9 only.")
            }
        }

        var additionalSeatsCost = 0.0
        while (true) {
            println("Do you want additional seats for kids this may increase the amount by $20?")
            println("1. Yes")
            println("2. No")
            when (readlnOrNull()?.toIntOrNull()) {
                1 -> {
                    additionalSeatsCost = 20.0
                    break
                }

                2 -> {
                    break
                }

                else -> {
                    println("Invalid input. Please enter 1 or 2 only.")
                }
            }
        }


        val carPackage = 500
        print("Enter the kilometers you will ride: ")
        val kilometers = readlnOrNull()?.toDoubleOrNull() ?: 0.0
        val excessKilometers = maxOf(0.0, kilometers - carPackage)
        val excessKilometersCost = excessKilometers * 0.1

        val rentalInfo = RentalInfo(
            pickUpLocation,
            dropOffLocation,
            startDate!!,
            rentalDays,
            endDate!!,
            withDriverCost,
            carCategory,
            numberOfSeats,
            additionalSeatsCost,
            excessKilometersCost,
            insuranceAmount
        )

        loggedInUser.rentalInfo = rentalInfo
        return chooseCar(rentalInfo)
    }

    private fun chooseCar(rentalInfo: RentalInfo): Car {
        println("Available cars:")
        val filteredCars = filterCars(rentalInfo)

        filteredCars.forEachIndexed { index, car ->
            println("${index + 1}. ${car.name} - Price per day: $${"%.2f".format(car.pricePerDay)}")
            println("   Engine: ${car.engineInfo}")
        }

        print("Choose a car (enter the car number): ")
        val carNumber = readlnOrNull()?.toIntOrNull() ?: 0

        return if (carNumber in 1..filteredCars.size) {
            filteredCars[carNumber - 1]
        } else {
            println("Invalid car selection. Please try again.")
            chooseCar(rentalInfo)
        }
    }

    private fun payForRental(car: Car, rentalInfo: RentalInfo) {
        val totalCost = totalCost(car, rentalInfo)
        displayInvoice(car, rentalInfo, totalCost)
        println("Choose a payment method:")
        println("1. Visa")
        println("2. PayPal")
        print("Enter the payment method number: ")

        when (readlnOrNull()) {
            "1" -> {
                val visa = VisaPayment()
                visa.processPayment(totalCost)
                println("Payment successful. Enjoy your rental!")
            }

            "2" -> {
                val paypal = PayPalPayment()
                paypal.processPayment(totalCost)
                println("Payment successful. Enjoy your rental!")
            }

            else -> {
                println("Invalid payment method choice. Please try again.")
                payForRental(car, loggedInUser!!.rentalInfo)
            }
        }
    }


    private fun filterCars(rentalInfo: RentalInfo): List<Car> {
        return cars.filter { car ->
            car.category.equals(rentalInfo.carCategory, ignoreCase = true) &&
                    car.numberOfSeats == rentalInfo.numberOfSeats
        }
    }

    private fun displayInvoice(car: Car, rentalInfo: RentalInfo, totalCost: Double) {
        println("Invoice:")
        println("Car: ${car.name}")
        println("Car category: ${rentalInfo.carCategory}")
        println("Number of seats: ${rentalInfo.numberOfSeats}")
        println("Rental price for ${rentalInfo.rentalDays} days : $${"%.2f".format((car.pricePerDay * rentalInfo.rentalDays))}")
        println("Pick-up location: ${rentalInfo.pickUpLocation}")
        println("Drop-off location: ${rentalInfo.dropOffLocation}")
        println("Start date: ${rentalInfo.startDate.format(formatter)}")
        println("End date: ${rentalInfo.endDate.format(formatter)}")

        if (rentalInfo.withDriverCost > 0.0) {
            println("With driver cost: $${"%.2f".format(rentalInfo.withDriverCost)}")
        }

        if (rentalInfo.additionalSeatsCost > 0.0) {
            println("Additional seats cost: $${"%.2f".format(rentalInfo.additionalSeatsCost)}")
        }

        if (rentalInfo.excessKilometersCost > 0.0) {
            println("Excess kilometers cost: $${"%.2f".format(rentalInfo.excessKilometersCost)}")
        }

        if (rentalInfo.insuranceAmount > 0.0) {
            println("Insurance amount: $${"%.2f".format(rentalInfo.insuranceAmount)}")
        }

        println("Total cost: $${"%.2f".format(totalCost)} (including a $200 fee)")
    }

    private fun totalCost(car: Car, rentalInfo: RentalInfo): Double =
        (car.pricePerDay * rentalInfo.rentalDays) + rentalInfo.withDriverCost + rentalInfo.excessKilometersCost + rentalInfo.insuranceAmount + rentalInfo.additionalSeatsCost + 200.0

}