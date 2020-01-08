@file:Suppress("UNUSED_PARAMETER")

package lesson8.task1

import lesson1.task1.sqr
import kotlin.math.*

/**
 * Точка на плоскости
 */
data class Point(val x: Double, val y: Double) {
    /**
     * Пример
     *
     * Рассчитать (по известной формуле) расстояние между двумя точками
     */
    fun distance(other: Point): Double = sqrt(sqr(x - other.x) + sqr(y - other.y))
}

/**
 * Треугольник, заданный тремя точками (a, b, c, см. constructor ниже).
 * Эти три точки хранятся в множестве points, их порядок не имеет значения.
 */
@Suppress("MemberVisibilityCanBePrivate")
class Triangle private constructor(private val points: Set<Point>) {

    private val pointList = points.toList()

    val a: Point get() = pointList[0]

    val b: Point get() = pointList[1]

    val c: Point get() = pointList[2]

    constructor(a: Point, b: Point, c: Point) : this(linkedSetOf(a, b, c))

    /**
     * Пример: полупериметр
     */
    fun halfPerimeter() = (a.distance(b) + b.distance(c) + c.distance(a)) / 2.0

    /**
     * Пример: площадь
     */
    fun area(): Double {
        val p = halfPerimeter()
        return sqrt(p * (p - a.distance(b)) * (p - b.distance(c)) * (p - c.distance(a)))
    }

    /**
     * Пример: треугольник содержит точку
     */
    fun contains(p: Point): Boolean {
        val abp = Triangle(a, b, p)
        val bcp = Triangle(b, c, p)
        val cap = Triangle(c, a, p)
        return abp.area() + bcp.area() + cap.area() <= area()
    }

    override fun equals(other: Any?) = other is Triangle && points == other.points

    override fun hashCode() = points.hashCode()

    override fun toString() = "Triangle(a = $a, b = $b, c = $c)"
}

/**
 * Окружность с заданным центром и радиусом
 */
data class Circle(val center: Point, val radius: Double) {
    /**
     * Простая
     *
     * Рассчитать расстояние между двумя окружностями.
     * Расстояние между непересекающимися окружностями рассчитывается как
     * расстояние между их центрами минус сумма их радиусов.
     * Расстояние между пересекающимися окружностями считать равным 0.0.
     */
    fun distance(other: Circle): Double {
        val lineBetweenCenter = center.distance(other.center)
        val sum = radius + other.radius
        if (lineBetweenCenter <= sum) return 0.0
        return lineBetweenCenter - sum
    }

    /**
     * Тривиальная
     *
     * Вернуть true, если и только если окружность содержит данную точку НА себе или ВНУТРИ себя
     */
    fun contains(p: Point): Boolean = sqr(p.x - center.x) + sqr(p.y - center.y) <= sqr(radius)
}

/**
 * Отрезок между двумя точками
 */
data class Segment(val begin: Point, val end: Point) {
    override fun equals(other: Any?) =
        other is Segment && (begin == other.begin && end == other.end || end == other.begin && begin == other.end)

    override fun hashCode() =
        begin.hashCode() + end.hashCode()
}

/**
 * Средняя
 *
 * Дано множество точек. Вернуть отрезок, соединяющий две наиболее удалённые из них.
 * Если в множестве менее двух точек, бросить IllegalArgumentException
 */
fun diameter(vararg points: Point): Segment {
    var remOne: Point = Point(0.0, 0.0)
    var remTwo: Point = Point(0.0, 0.0)
    var maxLen = 0.0
    val len = points.size
    if (len < 2) throw IllegalArgumentException()
    for (i in 0 until len) {
        for (j in i + 1 until len) {
            val currentLen = points[i].distance(points[j])
            if (currentLen >= maxLen) {
                maxLen = currentLen
                remOne = points[i]
                remTwo = points[j]
            }
        }
    }
    return Segment(remOne, remTwo)
}

/**
 * Простая
 *
 * Построить окружность по её диаметру, заданному двумя точками
 * Центр её должен находиться посередине между точками, а радиус составлять половину расстояния между ними
 */
fun circleByDiameter(diameter: Segment): Circle {
    val lenBetweenOX = diameter.begin.x - diameter.end.x
    val lenBetweenOY = diameter.begin.y - diameter.end.y
    val rad = sqrt(sqr(lenBetweenOX) + sqr(lenBetweenOY)) / 2
    val centerX = (diameter.begin.x + diameter.end.x) / 2
    val centerY = (diameter.begin.y + diameter.end.y) / 2
    return Circle(Point(centerX, centerY), rad)
}

/**
 * Прямая, заданная точкой point и углом наклона angle (в радианах) по отношению к оси X.
 * Уравнение прямой: (y - point.y) * cos(angle) = (x - point.x) * sin(angle)
 * или: y * cos(angle) = x * sin(angle) + b, где b = point.y * cos(angle) - point.x * sin(angle).
 * Угол наклона обязан находиться в диапазоне от 0 (включительно) до PI (исключительно).
 */
class Line private constructor(val b: Double, val angle: Double) {
    init {
        require(angle >= 0 && angle < PI) { "Incorrect line angle: $angle" }
    }

    constructor(point: Point, angle: Double) : this(point.y * cos(angle) - point.x * sin(angle), angle)

    /**
     * Средняя
     *
     * Найти точку пересечения с другой линией.
     * Для этого необходимо составить и решить систему из двух уравнений (каждое для своей прямой)
     */
    fun crossPoint(other: Line): Point {
        val b1 = cos(angle)
        val a1 = -sin(angle)
        val b2 = cos(other.angle)
        val a2 = -sin(other.angle)
        val x = -(((-b) * b2 - (-other.b) * b1) / (a1 * b2 - a2 * b1))
        val y = -(((-other.b) * a1 - (-b) * a2) / (a1 * b2 - a2 * b1))
        return Point(x, y)
    }

    override fun equals(other: Any?) = other is Line && angle == other.angle && b == other.b

    override fun hashCode(): Int {
        var result = b.hashCode()
        result = 31 * result + angle.hashCode()
        return result
    }

    override fun toString() = "Line(${cos(angle)} * y = ${sin(angle)} * x + $b)"
}

/**
 * Средняя
 *
 * Построить прямую по отрезку
 */
fun lineBySegment(s: Segment): Line {
    val segmOX = abs(s.end.x - s.begin.x)
    val segmOY = abs(s.end.y - s.begin.y)
    var ang = atan(segmOY / segmOX)
    if (s.begin.x > s.end.x && s.begin.y < s.end.y) {
        ang = PI - ang
    } else if (s.end.x > s.begin.x && s.end.y < s.begin.y) {
        ang = PI - ang
    }
    var pointX = 0.0
    var b = 0.0
    if (segmOX != 0.0) b = (s.end.x * s.begin.y - s.end.y * s.begin.x) / (s.end.x - s.begin.x)
    if (ang == PI / 2) {
        pointX = s.end.x
        b = 0.0
    }
    if (ang == 0.0 || ang == PI) {
        pointX = 0.0
        b = s.end.y
    }
    return Line(Point(pointX, b), ang)
}

/**
 * Средняя
 *
 * Построить прямую по двум точкам
 */
fun lineByPoints(a: Point, b: Point): Line {
    val segmOX = a.x - b.x
    val segmOY = a.y - b.y
    var ang = atan(segmOY / segmOX)
    if (ang < 0) ang += PI
    else if (segmOX < 0.0 && segmOY < 0.0) ang = PI - ang
    var py = 0.0
    var px = 0.0
    if (segmOX != 0.0) py = (b.x * a.y - b.y * a.x) / segmOX
    if (ang == PI / 2) {
        px = a.x
        py = 0.0
    }
    if (ang == 0.0 || ang == PI) {
        px = 0.0
        py = a.y
    }
    return Line(Point(px, py), ang)
}

/**
 * Сложная
 *
 * Построить серединный перпендикуляр по отрезку или по двум точкам
 */
fun bisectorByPoints(a: Point, b: Point): Line {
    val findPointY = (a.y + b.y) / 2
    val findPointX = (a.x + b.x) / 2
    val segmOX = a.x - b.x
    val segmOY = a.y - b.y
    var ang = atan(segmOY / segmOX)
    if (ang < PI / 2) ang += PI / 2
    else ang -= PI / 2
    return (Line(Point(findPointX, findPointY), ang))
}

/**
 * Средняя
 *
 * Задан список из n окружностей на плоскости. Найти пару наименее удалённых из них.
 * Если в списке менее двух окружностей, бросить IllegalArgumentException
 */
fun findNearestCirclePair(vararg circles: Circle): Pair<Circle, Circle> = TODO()

/**
 * Сложная
 *
 * Дано три различные точки. Построить окружность, проходящую через них
 * (все три точки должны лежать НА, а не ВНУТРИ, окружности).
 * Описание алгоритмов см. в Интернете
 * (построить окружность по трём точкам, или
 * построить окружность, описанную вокруг треугольника - эквивалентная задача).
 */
fun circleByThreePoints(a: Point, b: Point, c: Point): Circle = TODO()

/**
 * Очень сложная
 *
 * Дано множество точек на плоскости. Найти круг минимального радиуса,
 * содержащий все эти точки. Если множество пустое, бросить IllegalArgumentException.
 * Если множество содержит одну точку, вернуть круг нулевого радиуса с центром в данной точке.
 *
 * Примечание: в зависимости от ситуации, такая окружность может либо проходить через какие-либо
 * три точки данного множества, либо иметь своим диаметром отрезок,
 * соединяющий две самые удалённые точки в данном множестве.
 */
fun minContainingCircle(vararg points: Point): Circle = TODO()

