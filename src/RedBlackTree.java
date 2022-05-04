public class RedBlackTree{

    private int treeSize;
    private Node root;
    private RotationHandler rotationHandler;
    private Node insertedNode;

    public RedBlackTree() {
        treeSize = 0;
        rotationHandler = new RotationHandler();
    }

    private boolean isBlack(Node node) {
        return node == null || !node.isRedFlag();
    }

    public Node getRoot(){ return root; }

    public void clear(){
        root = null;
        treeSize = 0;
    }

    public int getTreeSize() {
        return treeSize;
    }

    public int getTreeHeight(){
        if(root != null)
            return root.getHeight();
        return 0;
    }

    public boolean isEmpty() {
        return treeSize == 0;
    }

    public boolean contains(String value){
        return search(value) != null;
    }

    public Node search(String value){
        return search(value,root);
    }

    private Node search(String value, Node node){
        if(node == null) return null;
        if(value.compareTo(node.getValue()) < 0) return search(value, node.getLeft());
        else if(value.compareTo(node.getValue()) > 0) return search(value, node.getRight());
        return node;
    }

    public boolean insert(String value){
        if(contains(value))
            return false;
        // if tree is empty ; insert root and make color black
        if(root == null){
            root = new Node(value,false);
            treeSize++;
            rotationHandler.update(root);
            return true;
        }
        // else use private insert method and call validate fn after insertion
        root =  insert(value,false, root);
        treeSize++;
        rotationHandler.update(root);
        validate(insertedNode);
        return true;
    }

    private Node insert(String value, boolean insert, Node node){
        if(insert){
            // base case is passing true insert flag in recursion
            // we create new node and insert it then update it
            // we keep inserted node to call validate function after insertion
            Node inserted = new Node(value,true);
            inserted.setParent(node);
            if(value.compareTo(node.getValue()) < 0) node.setLeft(inserted);
            else node.setRight(inserted);
            rotationHandler.update(inserted);
            insertedNode = inserted;
            return node;
        }

        // we search till we find null position then we pass true insert flag
        // by recursion we update all nodes affected by insertion while back tracking
        if(value.compareTo(node.getValue()) < 0) {
            if(node.getLeft() == null)
                 rotationHandler.update(insert(value, true, node));
            else
                rotationHandler.update(insert(value,false,node.getLeft()));
        }
        else {
            if(node.getRight() == null)
                rotationHandler.update(insert(value, true, node));
            else
                rotationHandler.update(insert(value, false, node.getRight()));
        }
        return node;
    }

    private void validate(Node node){
        // check if node is root or not or has red parent
        if(node.getValue() == root.getValue()) return;
        if(!node.getParent().isRedFlag()) return;

        // we get parent sibbling to check it
        Node parentSibbling = getParentSibbling(node);
        if(parentSibbling == null){
            // if parent sibbling is null
            blackValidate(node.getParent().getParent());
        }
        else {
            if (!parentSibbling.isRedFlag()) {
                // parent sibbling is black
                blackValidate(node.getParent().getParent());
            } else {
                // parent sibbling is red
                redValidate(node.getParent(), parentSibbling);
            }
        }
    }

    private void blackValidate(Node node){
            Node newGrandParent;
            // rotation and recoloring
            if (node.getBalanceFactor() >= 2) {
                if (node.getLeft().getBalanceFactor() >= 0) {
                    newGrandParent = rotationHandler.leftLeftCase(node);
                }
                else {
                    newGrandParent = rotationHandler.leftRightCase(node);
                }
                newGrandParent.recolor();
                newGrandParent.getRight().recolor();
                // check after rotation if new grandparent or not
                if(newGrandParent.getParent() == null)
                    root = newGrandParent;
            }
            else if (node.getBalanceFactor() <= -2) {
                if(node.getRight().getBalanceFactor() <= 0) {
                    newGrandParent = rotationHandler.rightRightCase(node);
                }
                else {
                    newGrandParent = rotationHandler.rightLeftCase(node);
                }
                newGrandParent.recolor();
                newGrandParent.getLeft().recolor();

                // check after rotation if new grandparent or not
                if(newGrandParent.getParent() == null)
                    root = newGrandParent;
            }
    }

    private void redValidate(Node parentNode,Node parentSibblingNode){
        // recoloring parent and parent sibbling
        parentNode.recolor();
        parentSibblingNode.recolor();
        // check if grandparent is root or not to recolor it
        if(parentNode.getParent().getValue() != root.getValue())
            parentNode.getParent().recolor();
        // validate parent
        validate(parentNode.getParent());
    }

    private Node getParentSibbling(Node node){
        if(node == null || node.getParent() == null) return null;
        return getSibbling(node.getParent());
//        Node parentSibbling;
//        if(node.getParent().getParent().getLeft() != null) {
//            if (node.getParent().getParent().getLeft() == node.getParent())
//                // parent sibbling is right
//                parentSibbling = node.getParent().getParent().getRight();
//
//            else
//                // parent sibbling left
//                parentSibbling = node.getParent().getParent().getLeft();
//        }
//        else
//            parentSibbling = null;
//        return parentSibbling;
    }

    private Node getSibbling(Node node){
        if(node == null || node.getParent() == null) return null;
        if (node.getParent().getLeft() == node)
            return node.getParent().getRight();
        else
            return node.getParent().getLeft();
    }

    public void printInOrderTraversal(Node node){
        if (node == null)
            return;
        printInOrderTraversal(node.getLeft());
        if(node.isRedFlag())
            System.out.println(node.getValue() + " Red Node");
        else
            System.out.println(node.getValue() + " Black Node");
        printInOrderTraversal(node.getRight());
    }

    private Node minNode(Node node) {
        while (node.getLeft() != null) node = node.getLeft();
        return node;
    }

    private Node maxNode(Node node) {
        while (node.getRight() != null) node = node.getRight();
        return node;
    }

    public boolean delete(String value){
        Node deletedNode = search(value);
        if(deletedNode == null) return false;
        treeSize--;
        return delete(value, deletedNode);
    }

    private boolean delete(String value, Node node) {
        boolean deletedColor;
        Node replacedNode;
        // 0 or 1 child
        if (node.getLeft() == null || node.getRight() == null) {
            replacedNode = deleteZeroOneChild(node);
            deletedColor = node.isRedFlag();
        }
        // 2 children
        else {
            Node successor = minNode(node.getRight());
            node.setValue(successor.getValue());
            replacedNode = deleteZeroOneChild(successor);
            deletedColor = successor.isRedFlag();
        }
        // case 1,2: if root is null or deleted node is red then we are done
        if(!deletedColor && root != null) {
            fixDeletion(replacedNode);
        }
        return true;
    }

    private Node deleteZeroOneChild(Node node){
        // left child
        if(node.getLeft() != null){
            if(node.getParent() == null) {
                root = node.getLeft();
                root.setParent(null);
                if(root.isRedFlag()) root.setRedFlag(false);
            }
            else{
                if(node.getParent().getLeft() == node)
                    node.getParent().setLeft(node.getLeft());
                else
                    node.getParent().setRight(node.getLeft());
                node.getLeft().setParent(node.getParent());
                Node updateBalance = node.getParent();
                while(updateBalance != null){
                    rotationHandler.update(updateBalance);
                    updateBalance = updateBalance.getParent();
                }
            }
            return node.getLeft();
        }
        // right child
        else if(node.getRight() != null){
            if(node.getParent() == null){
                root = node.getRight();
                root.setParent(null);
                if(root.isRedFlag()) root.setRedFlag(false);
            }
            else{
                if(node.getParent().getRight() == node)
                    node.getParent().setRight(node.getRight());
                else
                    node.getParent().setLeft(node.getRight());
                node.getRight().setParent(node.getParent());
                Node updateBalance = node.getParent();
                while(updateBalance != null){
                    rotationHandler.update(updateBalance);
                    updateBalance = updateBalance.getParent();
                }
            }
            return node.getRight();
        }
        // no children
        else{
            // delete root and it has no children
            if(node.getParent() == null) root = null;
                Node newChild = !node.isRedFlag() ? new NilNode() : null;
                if(node.getParent().getLeft() == node)
                    node.getParent().setLeft(newChild);
                else if(node.getParent().getRight() == node)
                    node.getParent().setRight(newChild);
                if(newChild != null) newChild.setParent(node.getParent());
                return newChild;
            }
        }

    private void fixDeletion(Node node) {

        if (node == root) return;

        Node sibbling = getSibbling(node);

        if (!isBlack(sibbling)) {
            boolean tempColor = sibbling.isRedFlag();
            sibbling.setRedFlag(node.getParent().isRedFlag());
            node.getParent().setRedFlag(tempColor);
            if (node == node.getParent().getLeft())
                rotationHandler.leftRotation(node.getParent());
            else
                rotationHandler.rightRotation(node.getParent());
            sibbling = getSibbling(node);
        }

        if(sibbling!=null && (isBlack(sibbling.getLeft()) && isBlack(sibbling.getRight()))){
            sibbling.setRedFlag(true);
            if (node.getParent().isRedFlag())
                node.getParent().setRedFlag(false);
            else
                fixDeletion(node.getParent());
        }
        else{
            if(sibbling!= null && (node.getParent().getLeft() == node && isBlack(sibbling.getRight()))){
                sibbling.getLeft().setRedFlag(false);
                sibbling.setRedFlag(true);
                rotationHandler.rightRotation(sibbling);
                sibbling = node.getParent().getRight();
            }

            else if(sibbling!= null && node.getParent().getLeft() != node && isBlack(sibbling.getLeft())){
                sibbling.getRight().setRedFlag(false);
                sibbling.setRedFlag(true);
                rotationHandler.leftRotation(sibbling);
                sibbling = node.getParent().getLeft();
            }
            if(sibbling != null)
                sibbling.setRedFlag(node.getParent().isRedFlag());
            if(node.getParent()!=null)
                node.getParent().setRedFlag(false);
            Node temp;
            if(sibbling!=null && node.getParent().getLeft() == node){
                sibbling.getRight().setRedFlag(false);
                temp = rotationHandler.leftRotation(node.getParent());
            }
            else{
                if(sibbling != null && sibbling.getLeft()!=null)
                    sibbling.getLeft().setRedFlag(false);
                if(node.getParent().getLeft() != null)
                    temp = rotationHandler.rightRotation(node.getParent());
            }
        }
    }
}