#PROGRAM[1]=BubbleSort
#PROGRAM[2]=BinaryTree
#PROGRAM[3]=Factorial
#PROGRAM[4]=LinearSearch
#PROGRAM[5]=LinkedList
#PROGRAM[6]=MoreThan4
#PROGRAM[7]=QuickSort
#PROGRAM[8]=TreeVisitor

PROGRAMSTR="BubbleSort BinaryTree Factorial LinearSearch LinkedList MoreThan4 QuickSort TreeVisitor"
PROGRAM=($PROGRAMSTR)

i=0
while [ $i -ne 8 ]
do
	PROG=${PROGRAM[$i]}
	echo -e $PROG".s\n"
	java -jar cc.jar < ucla/Minijava/$PROG.java > $PROG.s 
	i=$(($i+1))
done

