import java.io.File
fun main(){
val(r,p)=File("5").readText().split("\n\n")
val a=mutableListOf(0,0)
p.lines().forEach{
val n=it.split(",")
val s=n.sortedWith{x,y->if("$x|$y" in r)-1 else 1}
a[if(n==s) 0 else 1]+=s[s.size/2].toInt()
}
println(a)
}