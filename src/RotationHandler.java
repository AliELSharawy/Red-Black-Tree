public class RotationHandler {

    public void update(Node node){
        int leftHeight, rightHeight;

        if(node.getLeft() == null) leftHeight = -1;
        else leftHeight = node.getLeft().getHeight();

        if(node.getRight() == null) rightHeight = -1;
        else rightHeight = node.getRight().getHeight();

        node.setHeight(1 + Math.max(leftHeight,rightHeight));
        node.setBalanceFactor(leftHeight - rightHeight);
    }

    public Node rightRotation(Node x) {
        Node y = x.getLeft();
        Node z = y.getRight();
        x.setLeft(z);
        y.setRight(x);
        if(z != null){
            z.setParent(x);
        }
        return setParents(x, y);
    }

    public Node leftRotation(Node x) {
        Node y = x.getRight();
        Node z = y.getLeft();
        x.setRight(z);
        y.setLeft(x);
        if(z != null) {
            z.setParent(x);
        }
        return setParents(x, y);
    }

    private Node setParents(Node x, Node y) {
        Node temp = x.getParent();
        x.setParent(y);
        y.setParent(temp);
        if(y.getParent() != null) {
            if (y.getParent().getLeft() != null) {
                if (y.getParent().getLeft().getValue() == x.getValue())
                    y.getParent().setLeft(y);
                else
                    y.getParent().setRight(y);
            }
            else{
                y.getParent().setRight(y);
            }
        }
        update(x);
        update(y);
        while (temp != null){
            update(temp);
            temp = temp.getParent();
        }
        return y;
    }

    public Node leftLeftCase(Node node) {
        return rightRotation(node);
    }

    public Node rightRightCase(Node node) {
        return leftRotation(node);
    }

    public Node leftRightCase(Node node) {
        return rightRotation(leftRotation(node.getLeft()).getParent());
    }

    public Node rightLeftCase(Node node) {
        return leftRotation(rightRotation(node.getRight()).getParent());
    }
}
