package clicker

import java.awt.MouseInfo
import java.awt.Point
import java.awt.Robot
import java.awt.event.InputEvent

class Main {

    private static Map<String, Point> pointMap = [:]
    private static Robot robot = new Robot()

    static void main(String... args){
        println('Number of tickets: ')
        int numberOfTickets = System.in.newReader().readLine() as int

        List<String> pointNames = ['current_tickets', 'fill', 'p1', 'p15', 'complete']

        pointNames.each{
            println("Put pointer on $it button")
            waitFor(3)
            pointMap.put(it, MouseInfo.getPointerInfo().getLocation())
        }
        addNumberPointsToPointMap(pointMap.get('p1'), pointMap.get('p15'))

        println('Tickets filling will start at 10 seconds')
        waitFor(10)

        goToPointAndPressIt('current_tickets')
        waitFor(2)

        fillTickets(getUniqueCombinations(numberOfTickets))

    }

    private static void fillTickets(Set<Set<Integer>> combinations){
        combinations.each {Set<Integer> combination ->
            goToPointAndPressIt('fill')
            waitFor(2)
            combination.each {Integer number ->
                goToPointAndPressIt("p$number")
                waitFor(1)
            }
            goToPointAndPressIt('complete')
            waitFor(2)
        }
    }

    private static goToPointAndPressIt(String pointName){
        Point point = pointMap.get(pointName)
        robot.mouseMove(point.x as int, point.y as int)
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK)
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK)
    }

    private static Set<Set<Integer>> getUniqueCombinations(int size){
        Set<SortedSet<Integer>> combinations = []
        Random random = Random.newInstance()
        while(combinations.size()<size){
            Set<Integer> combination = []
            while(combination.size()<4){
                combination.add(random.nextInt(14)+1)
            }
            combinations.add(combination as SortedSet)
        }
        combinations
    }

    private static void addNumberPointsToPointMap(Point firstNumber, Point lastNumber){
        int firstNumberX = firstNumber.x
        int firstNumberY = firstNumber.y
        int xStep = (lastNumber.x - firstNumberX)/4
        int yStep = (lastNumber.y - firstNumberY)/2
        (0..2).each { int yPosition ->
            (0..4).each { int xPosition ->
                String buttonName = "p${(yPosition*5)+1+xPosition}"
                Point buttonPosition = new Point(firstNumberX + xPosition*xStep, firstNumberY + yPosition*yStep)
                pointMap.put(buttonName, buttonPosition)
            }
        }
    }

    private static void waitFor(int seconds){
        try {
            Thread.sleep(seconds * 1000)
        } catch (InterruptedException e) {
            e.printStackTrace()
        }
    }

}
