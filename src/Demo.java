import java.util.Scanner;

public class Demo {
    public static void main(String[] args){
        int i = 1;
        String value;
        RedBlackTree redBlackTree = new RedBlackTree();
        Scanner scanner = new Scanner(System.in);
        System.out.println("press 0 to exit");
        System.out.println("press 1 to print tree elements");
        System.out.println("press 2 to get root");
        System.out.println("press 3 to clear tree");
        System.out.println("press 4 to check element is in tree or not");
        System.out.println("press 5 to search for element");
        System.out.println("press 6 to insert element");
        System.out.println("press 7 to delete element");
        System.out.println("press 8 to get size of tree");
        System.out.println("press 9 to get tree height");
        System.out.println("press 10 to know if tree is empty or not");
        while(i != 0) {
            i = scanner.nextInt();
            scanner.nextLine();
            switch (i) {
                case 0:{
                    break;
                }
                case 1: {
                    // print tree
                    redBlackTree.printInOrderTraversal(redBlackTree.getRoot());
                    break;
                }
                case 2: {
                    // get root value
                    if(redBlackTree.getRoot() != null)
                        System.out.println(redBlackTree.getRoot().getValue());
                    else
                        System.out.println("tree is empty");
                    break;
                }
                case 3: {
                    // clear tree
                    redBlackTree.clear();
                    break;
                }
                case 4: {
                    // contain
                    value = scanner.nextLine();
                    System.out.println(redBlackTree.contains(value));
                    break;
                }
                case 5: {
                    // search
                    value = scanner.nextLine();
                    if (redBlackTree.contains(value)) System.out.println(redBlackTree.search(value).getValue());
                    else System.out.println("not found");
                    break;
                }
                case 6: {
                    // insert
                    value = scanner.nextLine();
                    System.out.println(redBlackTree.insert(value));
                    break;
                }
                case 7: {
                    // delete
                    value = scanner.nextLine();
                    System.out.println(redBlackTree.delete(value));
                    break;
                }
                case 8: {
                    // get size
                    System.out.println(redBlackTree.getTreeSize());
                    break;
                }
                case 9: {
                    // get tree height
                    System.out.println(redBlackTree.getTreeHeight());
                    break;
                }
                case 10:{
                    // isEmpty
                    System.out.println(redBlackTree.isEmpty());
                    break;
                }
                default:{
                    System.out.println("invalid input");
                }
            }
        }
    }
}
