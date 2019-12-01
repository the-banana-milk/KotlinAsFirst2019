@file:Suppress("UNUSED_PARAMETER", "ConvertCallChainIntoSequence")

package lesson6.task1

import lesson2.task2.daysInMonth
import java.lang.NumberFormatException
import kotlin.math.pow

/**
 * Пример
 *
 * Время представлено строкой вида "11:34:45", содержащей часы, минуты и секунды, разделённые двоеточием.
 * Разобрать эту строку и рассчитать количество секунд, прошедшее с начала дня.
 */
fun timeStrToSeconds(str: String): Int {
    val parts = str.split(":")
    var result = 0
    for (part in parts) {
        val number = part.toInt()
        result = result * 60 + number
    }
    return result
}

/**
 * Пример
 *
 * Дано число n от 0 до 99.
 * Вернуть его же в виде двухсимвольной строки, от "00" до "99"
 */
fun twoDigitStr(n: Int) = if (n in 0..9) "0$n" else "$n"

/**
 * Пример
 *
 * Дано seconds -- время в секундах, прошедшее с начала дня.
 * Вернуть текущее время в виде строки в формате "ЧЧ:ММ:СС".
 */
fun timeSecondsToStr(seconds: Int): String {
    val hour = seconds / 3600
    val minute = (seconds % 3600) / 60
    val second = seconds % 60
    return String.format("%02d:%02d:%02d", hour, minute, second)
}

/**
 * Пример: консольный ввод
 */
fun main() {
    println("Введите время в формате ЧЧ:ММ:СС")
    val line = readLine()
    if (line != null) {
        val seconds = timeStrToSeconds(line)
        if (seconds == -1) {
            println("Введённая строка $line не соответствует формату ЧЧ:ММ:СС")
        } else {
            println("Прошло секунд с начала суток: $seconds")
        }
    } else {
        println("Достигнут <конец файла> в процессе чтения строки. Программа прервана")
    }
}


/**
 * Средняя
 *
 * Дата представлена строкой вида "15 июля 2016".
 * Перевести её в цифровой формат "15.07.2016".
 * День и месяц всегда представлять двумя цифрами, например: 03.04.2011.
 * При неверном формате входной строки вернуть пустую строку.
 *
 * Обратите внимание: некорректная с точки зрения календаря дата (например, 30.02.2009) считается неверными
 * входными данными.
 */
fun dateStrToDigit(str: String): String {
    val map = mapOf<String, Int>(
        "января" to 1, "февраля" to 2, "марта" to 3, "апреля" to 4, "мая" to 5, "июня" to 6,
        "июля" to 7, "августа" to 8, "сентября" to 9, "октября" to 10, "ноября" to 11, "декабря" to 12
    )
    val input = str.split(" ")
    if (input.size != 3) return ""
    val num1 = map[input[1]]
    if (num1 == null) return ""
    val day = input[0].toIntOrNull()
    val year = input[2].toIntOrNull()
    if (day == null || year == null) return ""
    val a = daysInMonth(num1, year)
    return if ((day <= a) && (day > 0)) String.format("%02d.%02d.%d", day, num1, year) else ""
}

/**
 * Средняя
 *
 * Дата представлена строкой вида "15.07.2016".
 * Перевести её в строковый формат вида "15 июля 2016".
 * При неверном формате входной строки вернуть пустую строку
 *
 * Обратите внимание: некорректная с точки зрения календаря дата (например, 30 февраля 2009) считается неверными
 * входными данными.
 *
 */
fun dateDigitToStr(digital: String): String {
    val inf = listOf<String>(
        "января", "февраля", "марта", "апреля", "мая", "июня",
        "июля", "августа", "сентября", "октября", "ноября", "декабря"
    )
    val input = digital.split(".")
    if (input.size != 3) return ""
    val month = input[1].toIntOrNull()
    if (month !in 1..12 || month == null) return ""
    val name = inf[month - 1]
    val day = input[0].toIntOrNull()
    val year = input[2].toIntOrNull()
    if ((day == null) || (year == null)) return ""
    val a = daysInMonth(month, year)
    return if ((day <= a) && (day > 0)) String.format("%d %s %d", day, name, year) else ""
}

/**
 * Средняя
 *
 * Номер телефона задан строкой вида "+7 (921) 123-45-67".
 * Префикс (+7) может отсутствовать, код города (в скобках) также может отсутствовать.
 * Может присутствовать неограниченное количество пробелов и чёрточек,
 * например, номер 12 --  34- 5 -- 67 -89 тоже следует считать легальным.
 * Перевести номер в формат без скобок, пробелов и чёрточек (но с +), например,
 * "+79211234567" или "123456789" для приведённых примеров.
 * Все символы в номере, кроме цифр, пробелов и +-(), считать недопустимыми.
 * При неверном формате вернуть пустую строку.
 *
 * PS: Дополнительные примеры работы функции можно посмотреть в соответствующих тестах.
 *
 */
fun flattenPhoneNumber(phone: String): String {
    val newphone = phone.filter { it != ' ' && it != '-' }
    if (newphone.matches(Regex(pattern = """(\+[0-9]+)?(\([0-9]+\))?[0-9]+"""))) {
        val need = phone.filter { it in '0'..'9' || it == '+' }
        return need
    } else return ""
}

/**
 * Средняя
 *
 * Результаты спортсмена на соревнованиях в прыжках в длину представлены строкой вида
 * "706 - % 717 % 703".
 * В строке могут присутствовать числа, черточки - и знаки процента %, разделённые пробелами;
 * число соответствует удачному прыжку, - пропущенной попытке, % заступу.
 * Прочитать строку и вернуть максимальное присутствующее в ней число (717 в примере).
 * При нарушении формата входной строки или при отсутствии в ней чисел, вернуть -1.
 */
fun bestLongJump(jumps: String): Int {
    val newJumps = jumps.split(" ")
    var max = -1
    for (num in newJumps) {
        if (num == "-" || num == "%" || num.matches(Regex("""\d+"""))) {
            if (num.matches(Regex("""\d+"""))) {
                val k = num.toInt()
                if (k > max) max = k
            }
        } else return -1
    }
    return max
}


/**
 * Сложная
 *
 * Результаты спортсмена на соревнованиях в прыжках в высоту представлены строкой вида
 * "220 + 224 %+ 228 %- 230 + 232 %%- 234 %".
 * Здесь + соответствует удачной попытке, % неудачной, - пропущенной.
 * Высота и соответствующие ей попытки разделяются пробелом.
 * Прочитать строку и вернуть максимальную взятую высоту (230 в примере).
 * При нарушении формата входной строки, а также в случае отсутствия удачных попыток,
 * вернуть -1.
 */
fun bestHighJump(jumps: String): Int {
    val div = jumps.split(" ")
    var rem = 0
    var max = -1
    if (div.size % 2 != 0) return -1
    div.forEachIndexed { index, value
        ->
        when {
            (value.matches(Regex("""\d+""")) && index % 2 == 0) ->
                rem = value.toInt()
            (value.matches(Regex("""[%\-+]+""")) && index % 2 != 0) ->
                if ("+" in value) {
                    if (rem > max) max = rem
                }
            else -> return -1
        }
    }
    return max
}

/**
 * Сложная
 *
 * В строке представлено выражение вида "2 + 31 - 40 + 13",
 * использующее целые положительные числа, плюсы и минусы, разделённые пробелами.
 * Наличие двух знаков подряд "13 + + 10" или двух чисел подряд "1 2" не допускается.
 * Вернуть значение выражения (6 для примера).
 * Про нарушении формата входной строки бросить исключение IllegalArgumentException
 *
 */
fun plusMinus(expression: String): Int {
    val list = expression.split(" ")
    var willChange = 0
    var action = ""
    var check = 0
    var rem = 0
    if (list.size % 2 == 0) throw IllegalArgumentException()
    if (expression.isEmpty()) throw IllegalArgumentException("Only signed numbers are allowed")
    for ((check, i) in list.withIndex()) {
        when {
            (i.matches(Regex("""\d+""")) && action.isEmpty() && (check % 2 == 0)) -> willChange = i.toInt()
            (i.matches(Regex("""[+-]""")) && (check % 2 != 0)) -> action = i
            (i.matches(Regex("""\d+""")) && ((action == "+") || (action == "-")) && (check % 2 == 0)) -> {
                rem = i.toInt()
                when {
                    (action == "+") -> willChange += rem
                    (action == "-") -> willChange -= rem
                    else -> throw IllegalArgumentException()
                }
            }
            else -> throw IllegalArgumentException()
        }
    }
    return willChange
}

/**
 * Сложная
 *
 * Строка состоит из набора слов, отделённых друг от друга одним пробелом.
 * Определить, имеются ли в строке повторяющиеся слова, идущие друг за другом.
 * Слова, отличающиеся только регистром, считать совпадающими.
 * Вернуть индекс начала первого повторяющегося слова, или -1, если повторов нет.
 * Пример: "Он пошёл в в школу" => результат 9 (индекс первого 'в')
 *
 */
fun firstDuplicateIndex(str: String): Int {
    val words = str.toLowerCase().split(" ")
    var ind = 0
    for (i in 0 until words.size - 1) {
        if (words[i] == words[i + 1]) return ind
        ind += words[i].length + 1
    }
    return -1
}

/**
 * Сложная
 *
 * Строка содержит названия товаров и цены на них в формате вида
 * "Хлеб 39.9; Молоко 62; Курица 184.0; Конфеты 89.9".
 * То есть, название товара отделено от цены пробелом,
 * а цена отделена от названия следующего товара точкой с запятой и пробелом.
 * Вернуть название самого дорогого товара в списке (в примере это Курица),
 * или пустую строку при нарушении формата строки.
 * Все цены должны быть больше либо равны нуля.
 */
fun mostExpensive(description: String): String {
    val listOfgoods = description.split(" ", "; ")
    var max = 0.0
    var name = ""
    var rem = 0.0
    listOfgoods.forEachIndexed { index, s ->
        if (s.matches(Regex("""([^\ ]+)|([0-9]*[.,]?[0-9]+)"""))) {
            when {
                s.matches(Regex("""[0-9]*[.,]?[0-9]+""")) && index % 2 != 0 -> {
                    rem = s.toDouble()
                    if (rem >= max) {
                        max = rem
                        name = listOfgoods[index - 1]
                    }
                }
            }
        } else return ""
    }
    return name
}

/**
 * Сложная
 *
 * Перевести число roman, заданное в римской системе счисления,
 * в десятичную систему и вернуть как результат.
 * Римские цифры: 1 = I, 4 = IV, 5 = V, 9 = IX, 10 = X, 40 = XL, 50 = L,
 * 90 = XC, 100 = C, 400 = CD, 500 = D, 900 = CM, 1000 = M.
 * Например: XXIII = 23, XLIV = 44, C = 100
 *
 * Вернуть -1, если roman не является корректным римским числом
 */
fun fromRoman(roman: String): Int {
    val map = mapOf<String, Int>(
        "M" to 1000,
        "CM" to 900,
        "D" to 500,
        "CD" to 400,
        "C" to 100,
        "XC" to 90,
        "L" to 50,
        "XL" to 40,
        "X" to 10,
        "IX" to 9,
        "V" to 5,
        "IV" to 4,
        "I" to 1
    )
    var newRoman = roman
    var len = newRoman.length
    var n = 0
    if (!roman.matches(Regex("""[IVXLCDM]+"""))) return -1
    for ((str, value) in map) {
        val strlen = str.length
        if (len != 1) {
            while ((str == newRoman[0].toString() || str == newRoman.substring(0, 2))) {
                newRoman = newRoman.removePrefix(str)
                n += value
                len = newRoman.length
                if (len == 0) return n
            }
        } else {
            if (str == newRoman) {
                n += value
                return n
            }
        }
    }
    return -1
}



/**
 * Очень сложная
 *
 * Имеется специальное устройство, представляющее собой
 * конвейер из cells ячеек (нумеруются от 0 до cells - 1 слева направо) и датчик, двигающийся над этим конвейером.
 * Строка commands содержит последовательность команд, выполняемых данным устройством, например +>+>+>+>+
 * Каждая команда кодируется одним специальным символом:
 *	> - сдвиг датчика вправо на 1 ячейку;
 *  < - сдвиг датчика влево на 1 ячейку;
 *	+ - увеличение значения в ячейке под датчиком на 1 ед.;
 *	- - уменьшение значения в ячейке под датчиком на 1 ед.;
 *	[ - если значение под датчиком равно 0, в качестве следующей команды следует воспринимать
 *  	не следующую по порядку, а идущую за соответствующей следующей командой ']' (с учётом вложенности);
 *	] - если значение под датчиком не равно 0, в качестве следующей команды следует воспринимать
 *  	не следующую по порядку, а идущую за соответствующей предыдущей командой '[' (с учётом вложенности);
 *      (комбинация [] имитирует цикл)
 *  пробел - пустая команда
 *
 * Изначально все ячейки заполнены значением 0 и датчик стоит на ячейке с номером N/2 (округлять вниз)
 *
 * После выполнения limit команд или всех команд из commands следует прекратить выполнение последовательности команд.
 * Учитываются все команды, в том числе несостоявшиеся переходы ("[" при значении под датчиком не равном 0 и "]" при
 * значении под датчиком равном 0) и пробелы.
 *
 * Вернуть список размера cells, содержащий элементы ячеек устройства после завершения выполнения последовательности.
 * Например, для 10 ячеек и командной строки +>+>+>+>+ результат должен быть 0,0,0,0,0,1,1,1,1,1
 *
 * Все прочие символы следует считать ошибочными и формировать исключение IllegalArgumentException.
 * То же исключение формируется, если у символов [ ] не оказывается пары.
 * Выход за границу конвейера также следует считать ошибкой и формировать исключение IllegalStateException.
 * Считать, что ошибочные символы и непарные скобки являются более приоритетной ошибкой чем выход за границу ленты,
 * то есть если в программе присутствует некорректный символ или непарная скобка, то должно быть выброшено
 * IllegalArgumentException.
 * IllegalArgumentException должен бросаться даже если ошибочная команда не была достигнута в ходе выполнения.
 *
 */
fun computeDeviceCells(cells: Int, commands: String, limit: Int): List<Int> {
    val cellsList = mutableListOf<Int>()
    var ind = cells / 2
    var indexOfCom = 0
    var lim = limit
    for (i in 1..cells) cellsList.add(0)
    if (!commands.matches(Regex("""[+\-<>\[\]\ ]*"""))) throw IllegalArgumentException()
    val com = commands.split("").toMutableList()
    if (com[0] == "" && com[com.size - 1] == "") {
        com.remove(com[0])
        com.remove(com[com.size - 1])
    }
    val size = com.size
    var countOfbrackets = 0
    while ((indexOfCom in 0 until size) || (lim != 0)) {
        if (com[indexOfCom] == ">") {
            ind += 1
            indexOfCom += 1
        } else if (com[indexOfCom] == "<") {
            ind -= 1
            indexOfCom += 1
        } else if (com[indexOfCom] == "+") {
            cellsList[ind] += 1
            indexOfCom += 1
        } else if (com[indexOfCom] == "-") {
            cellsList[ind] -= 1
            indexOfCom += 1
        } else if (com[indexOfCom] == "[") {
            if (cellsList[ind] == 0) {
                countOfbrackets += 1
                while (countOfbrackets != 0 && (indexOfCom == com.size - 1) && com[indexOfCom] != "]") {
                    indexOfCom += 1
                    if (com[indexOfCom] == "[") {
                        countOfbrackets += 1
                    } else if (com[indexOfCom] == "]" && countOfbrackets != 0) {
                        countOfbrackets -= 1
                    } else if (com[indexOfCom] == "]" && countOfbrackets == 1) countOfbrackets -= 1
                    if (countOfbrackets > 0 && ((lim == 0) || (indexOfCom == com.size - 1))) throw IllegalArgumentException()
                }
                indexOfCom += 1
            } else {
                indexOfCom += 1
            }
        } else if (com[indexOfCom] == "]") {
            if (cellsList[ind] != 0) {
                countOfbrackets -= 1
                while (countOfbrackets != 0 && (indexOfCom == com.size - 1) && com[indexOfCom] != "[") {
                    indexOfCom -= 1
                    if (com[indexOfCom] == "]") {
                        countOfbrackets -= 1
                    } else if (com[indexOfCom] == "[" && countOfbrackets != 0) {
                        countOfbrackets += 1
                    }
                    if (countOfbrackets > 0 && ((lim == 0) || (indexOfCom == com.size - 1))) throw IllegalArgumentException()
                }
            } else {
                indexOfCom += 1
            }
        } else if (com[indexOfCom] == " ") {
            indexOfCom += 1
        }
        lim -= 1
        if (ind !in 0 until cellsList.size) throw IllegalStateException()
        if (countOfbrackets > 0 && ((lim == 0) || (indexOfCom == com.size - 1))) throw IllegalArgumentException()
    }
    return cellsList
}
