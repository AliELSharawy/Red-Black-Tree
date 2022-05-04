import java.util.ArrayList;
import java.util.List;

public class Demo2 {
    public static void main(String[] args) {

        RedBlackTree redBlackTree = new RedBlackTree();
        StringGenerator stringGenerator = new StringGenerator();
        List<String> stringCollector = new ArrayList();
        String currentString;

        Long startTime = System.currentTimeMillis();
        for (int i = 0; i < 10000000; i++) {
            currentString = stringGenerator.generateRandomString(40);
            stringCollector.add(currentString);
            redBlackTree.insert(currentString);
        }
        System.out.print("Time Consumed to insert random inputs 10000000 = ");
        System.out.println(System.currentTimeMillis() - startTime);

        startTime = System.currentTimeMillis();
        for(String s : stringCollector){
            redBlackTree.delete(s);
        }
        System.out.print("Time Consumed to delete random inputs 10000000 = ");
        System.out.println(System.currentTimeMillis() - startTime);


    }
}
