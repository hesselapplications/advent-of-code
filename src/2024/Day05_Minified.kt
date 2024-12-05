import kotlin.io.path.Path
import kotlin.io.path.readLines

fun main() {
val i=Path("5").readLines();val r=i.filter{"|" in it}.map{it.split("|")};val u=i.filter{"," in it}.map{it.split(",")}
fun List<String>.b(n: String)=r.filter{it[1] == n&&it[0] in this&&it[1] in this}.map{it[0]}
fun Collection<List<String>>.m()=println(sumOf{it[it.size / 2].toInt()})
val c=u.filter{u->u.all{n->u.takeWhile{it!=n}.toSet()==u.b(n).toSet()}}.also{it.m()}
(u-c).map{it.sortedBy{n->it.b(n).size}}.m()
}