package taxipark

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> =
        allDrivers.filter { driver ->
            trips.none { it.driver == driver }
        }.toSet()

/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> =
        allPassengers.filter { p ->
            trips.count { it.passengers.contains(p) } >= minTrips
        }.toSet()

/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> =
        this.allPassengers.filter { passenger ->
            this.trips.filter { it.passengers.contains(passenger) && it.driver == driver }
                    .groupBy { it.driver }
                    .filterValues { list -> list.size > 1 }
                    .any()
        }.toSet()

/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> =
        this.allPassengers.filter { passenger ->
            val (withDiscount, withoutDiscount) = trips
                    .filter { it.passengers.contains(passenger) }
                    .partition { it.discount != null }
            withDiscount.size > withoutDiscount.size
        }.toSet()

/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? {
    val key = trips.groupBy { it.duration / 10 }
            .maxBy { (_, list) -> list.size }
            ?.key?.times(10)
    return if (key != null) {
        IntRange(key, key + 9)
    } else {
        null
    }
}

/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean {
    val numberMax = (allDrivers.size / 100.0 * 20).toInt()
    val sumNumberOfPeople = trips.groupBy { it.driver }
            .map { (driver, trips) -> driver to trips.sumByDouble { it.cost } }
            .sortedByDescending { (_, income) -> income }
            .take(numberMax)
            .sumByDouble { (_, x) -> x }
    val sumAll = trips.sumByDouble { it.cost }
    return (sumAll / 100 * 80) <= sumNumberOfPeople
}
