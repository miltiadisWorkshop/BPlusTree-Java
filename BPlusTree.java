import java.util.ArrayList;
import java.util.List;

import java.util.ArrayList;
import java.util.List;

public class BPlusTree<Key extends Comparable<Key>> {
   private Node root;
   private int degree;

   // Inner Node class
   private class Node {
       private List<Key> keys;
       private List<Node> children;
       private boolean isLeaf;

       public Node(boolean isLeaf) {
           this.isLeaf = isLeaf;
           keys = new ArrayList<>();
           children = new ArrayList<>();
       }
   }

   public BPlusTree(int degree) {
       if (degree <= 1) {
           throw new IllegalArgumentException("Degree must be greater than 1");
       }
       this.degree = degree;
       root = new Node(true);
   }

   public void insert(Key key) {
       Node node = root;
       if (node.keys.size() == 2 * degree - 1) {
           Node newRoot = new Node(false);
           newRoot.children.add(node);
           splitChild(newRoot, 0);
           root = newRoot;
           node = root;
       }
       insertNonFull(node, key);
   }

   private void insertNonFull(Node node, Key key) {
       int index = node.keys.size() - 1;
       if (node.isLeaf) {
           while (index >= 0 && key.compareTo(node.keys.get(index)) < 0) {
               index--;
           }
           node.keys.add(index + 1, key);
       } else {
           while (index >= 0 && key.compareTo(node.keys.get(index)) < 0) {
               index--;
           }
           index++;
           if (node.children.get(index).keys.size() == 2 * degree - 1) {
               splitChild(node, index);
               if (key.compareTo(node.keys.get(index)) > 0) {
                   index++;
               }
           }
           insertNonFull(node.children.get(index), key);
       }
   }

   private void splitChild(Node parentNode, int childIndex) {
       Node childNode = parentNode.children.get(childIndex);
       Node siblingNode = new Node(childNode.isLeaf);
       parentNode.keys.add(childIndex, childNode.keys.get(degree - 1));
       parentNode.children.add(childIndex + 1, siblingNode);
       for (int i = 0; i < degree - 1; i++) {
           siblingNode.keys.add(childNode.keys.remove(degree));
       }
       if (!childNode.isLeaf) {
           for (int i = 0; i < degree; i++) {
               siblingNode.children.add(childNode.children.remove(degree));
           }
       }
   }

   public boolean search(Key key) {
       return search(root, key);
   }

   private boolean search(Node node, Key key) {
       int index = 0;
       while (index < node.keys.size() && key.compareTo(node.keys.get(index)) > 0) {
           index++;
       }
       if (index < node.keys.size() && key.equals(node.keys.get(index))) {
           return true;
       } else if (node.isLeaf) {
           return false;
       } else {
           return search(node.children.get(index), key);
       }
   }
}
