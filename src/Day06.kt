fun main() {
    check(day6("mjqjpqmgbljsphdztnvjfqwrcgsmlb") == 7)
    check(day6("bvwbjplbgvbhsrlpgdmjqwftvncz") == 5)
    check(day6("nppdvjthqldpwncqszvftbrmjlhg") == 6)
    check(day6("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg") == 10)
    check(day6("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw") == 11)


    println(day6(readInput("Day06").first(), checkLength = 14))
}

fun day6(input: String, checkLength: Int = 4) =
    input.toList().fold(emptyList<Char>()) { l, v ->
        if ((l + v).takeLast(checkLength).distinct().size == checkLength)
            return (l + v).size
        l + v
    }.size


