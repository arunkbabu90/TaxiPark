package taxipark

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> = allDrivers.filter { it !in trips.map { trip -> trip.driver } }.toSet()

/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> =
        allPassengers.filter { trips.count { trip -> it in trip.passengers } >= minTrips }.toSet()

/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> =
        allPassengers.filter { trips.count { trip -> trip.driver == driver && it in trip.passengers } > 1 }.toSet()

/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> =
        allPassengers
                .filter { passenger ->
                    trips.count { tripDisc -> tripDisc.discount != null && passenger in tripDisc.passengers } >
                            trips.count { tripNoDisc -> tripNoDisc.discount == null && passenger in tripNoDisc.passengers }
                }.toSet()

/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? {
    return if (trips.isEmpty()) {
        null
    } else {
        val minPeriod = trips.groupBy { it.duration / 10 }.maxBy { map -> map.value.count() }?.key?.times(10)
        val maxPeriod = minPeriod?.plus(9)
        maxPeriod?.let { minPeriod.rangeTo(it) }
    }
}

/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean {
    return if (trips.isEmpty()) {
        // If the taxi park contains no trips, the result should be false
        false
    } else {
        var driversCount = 0
        var successfulIncome = 0.0
        val successfulDriversCount = (allDrivers.size * 0.2).toInt()
        val totalIncome = trips.sumByDouble { it.cost }
        val incomeSortedByDriver = trips.groupBy { it.driver }.map { (_, driverTrips) -> driverTrips.sumByDouble { it.cost }}.sortedDescending()

        for (income in incomeSortedByDriver) {
            successfulIncome += income
            driversCount++
            if (successfulIncome >= (totalIncome * 0.8)) break
        }
        driversCount <= successfulDriversCount
    }
}

/*  :: Alternate Solution ::
fun TaxiPark.checkParetoPrinciple(): Boolean {
    if (trips.isEmpty()) return false

    val totalIncome = trips.sumByDouble(Trip::cost)
    val sortedDriversIncome: List<Double> = trips
            .groupBy(Trip::driver)
            .map { (_, tripsByDriver) -> tripsByDriver.sumByDouble(Trip::cost) }
            .sortedDescending()

    val numberOfTopDrivers = (0.2 * allDrivers.size).toInt()
    val incomeByTopDrivers = sortedDriversIncome
            .take(numberOfTopDrivers)
            .sum()

    return incomeByTopDrivers >= 0.8 * totalIncome
}
 */