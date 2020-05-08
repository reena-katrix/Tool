
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class TreeNode<T> implements Iterable<TreeNode<T>> {

    public T data;
    public TreeNode<T> parent;
    public List<TreeNode<T>> children;

    public boolean isRoot() {
        return parent == null;
    }

    public boolean isLeaf() {
        return children.size() == 0;
    }

    private List<TreeNode<T>> elementsIndex;

    public TreeNode(T data) {
        this.data = data;
        this.children = new LinkedList<TreeNode<T>>();
        this.elementsIndex = new LinkedList<TreeNode<T>>();
        this.elementsIndex.add(this);
    }

    public TreeNode<T> addChild(T child) {
        TreeNode<T> childNode = new TreeNode<T>(child);
        childNode.parent = this;
        this.children.add(childNode);
        this.registerChildForSearch(childNode);
        return childNode;
    }

    public int getLevel() {
        if (this.isRoot())
            return 0;
        else
            return parent.getLevel() + 1;
    }

    public List<TreeNode<T>> getElementsIndex() {
        return this.elementsIndex;
    }
    private void registerChildForSearch(TreeNode<T> node) {
        elementsIndex.add(node);
        if (parent != null)
            parent.registerChildForSearch(node);
    }

//    List<TreeNode<T>> elementsIndex;
    public TreeNode<T> findTreeNode(Comparable<T> cmp) {
        
//        for(int i = 0 ; i < this.elementsIndex.size() ; i ++ ) {
//            TreeNode<T>deviceClassObj = this.elementsIndex.get(i);
//            if(deviceClassObj.data)
//        }
        
        for (TreeNode<T> element : this.elementsIndex) {
            T elData = element.data;
//            System.out.println(elData.getSeriesNo());
            if (cmp.compareTo(elData) == 0)
                return element;
        }

        return null;
    }

    
    @Override
    public String toString() {
        return data != null ? data.toString() : "[data null]";
    }

    @Override
    public Iterator<TreeNode<T>> iterator() {
        TreeNodeIter<T> iter = new TreeNodeIter<T>(this);
        return iter;
    }

}


//
//import java.util.Iterator;
//import java.util.LinkedList;
//import java.util.List;
//
//public class TreeNode<T> implements Iterable<TreeNode<T>> {
//
//    public T data;
//    public TreeNode<T> parent;
//    public List<TreeNode<T>> children;
//
//    public boolean isRoot() {
//        return parent == null;
//    }
//
//    public boolean isLeaf() {
//        return children.size() == 0;
//    }
//
//    private List<TreeNode<T>> elementsIndex;
//
//    public TreeNode(T data) {
//        this.data = data;
//        this.children = new LinkedList<TreeNode<T>>();
//        this.elementsIndex = new LinkedList<TreeNode<T>>();
//        this.elementsIndex.add(this);
//    }
//
//    public TreeNode<T> addChild(T child) {
//        TreeNode<T> childNode = new TreeNode<T>(child);
//        childNode.parent = this;
//        this.children.add(childNode);
//        this.registerChildForSearch(childNode);
//        return childNode;
//    }
//
//    public int getLevel() {
//        if (this.isRoot())
//            return 0;
//        else
//            return parent.getLevel() + 1;
//    }
//
//    //to get children devices
//    public List<TreeNode<T>> getChildren(){
//        return this.children;
//    }
//    private void registerChildForSearch(TreeNode<T> node) {
//        elementsIndex.add(node);
//        if (parent != null)
//            parent.registerChildForSearch(node);
//    }
//
//    public TreeNode<T> findTreeNode(Comparable<T> cmp) {
//        for (TreeNode<T> element : this.elementsIndex) {
//            T elData = element.data;
////            System.out.println(elData.getSeriesNo());
//            if (cmp.compareTo(elData) == 0)
//                return element;
//        }
//
//        return null;
//    }
//    
//   
//    
//
//    @Override
//    public String toString() {
//        return data != null ? data.toString() : "[data null]";
//    }
//
//    @Override
//    public Iterator<TreeNode<T>> iterator() {
//        TreeNodeIter<T> iter = new TreeNodeIter<T>(this);
//        return iter;
//    }
//
//}






//
//
//import java.util.Iterator;
//import java.util.LinkedList;
//import java.util.List;
//
//
//public class TreeNode<DeviceClass> implements Iterable<TreeNode<DeviceClass>> {
//
//    public DeviceClass data;
//    public TreeNode<DeviceClass> parent;
//    public List<TreeNode<DeviceClass>> children;
//    public boolean isRoot() {
//        return parent == null;
//    }
//
//    public boolean isLeaf() {
//        return children.size() == 0;
//    }
//
//    private List<TreeNode<DeviceClass>> elementsIndex;
//
//    public TreeNode(DeviceClass data) {
//        this.data = data;
//        this.children = new LinkedList<TreeNode<DeviceClass>>();
//        this.elementsIndex = new LinkedList<TreeNode<DeviceClass>>();
//        this.elementsIndex.add(this);
//    }
//
//    public TreeNode<DeviceClass> addChild(DeviceClass child) {
//        TreeNode<DeviceClass> childNode = new TreeNode<DeviceClass>(child);
//        childNode.parent = this;
//        this.children.add(childNode);
//        this.registerChildForSearch(childNode);
//        return childNode;
//    }
//
//    public int getLevel() {
//        if (this.isRoot())
//            return 0;
//        else
//            return parent.getLevel() + 1;
//    }
//
////  //to get children devices
//  public List<TreeNode<DeviceClass>> getChildren(){
//      return this.children;
//  }
//    
//    private void registerChildForSearch(TreeNode<DeviceClass> node) {
//        elementsIndex.add(node);
//        if (parent != null)
//            parent.registerChildForSearch(node);
//    }
//
//    public TreeNode<DeviceClass> findTreeNode(String series) {
//        for (TreeNode<DeviceClass> element : this.elementsIndex) {
//            System.out.println(element.data.getClass());
//            DeviceClass elData = element.data;
//            
////            if (elData.getSeriesNo().equals(series))
////                return element;
//        }
//
//        return null;
//    }
//
//    
//    public TreeNode<DeviceClass> findTreeNode2(String input) {
//        for (TreeNode<DeviceClass> element : this.elementsIndex) {
//            DeviceClass treeData = element.data;
////            System.out.println(elData.getSeriesNo());
//            
//            if(treeData .equals(input)) {
//              System.out.println("You entered DeviceGroup Name:" + input);
//              nodeOk = treeData.getSeriesNo().equals(input);
//          }
//          else if(treeData.getSeriesNo().equals(input)) {
//              System.out.println("You entered DeviceGroup Product Series Number:" + input);
//              nodeOk = treeData.getSeriesNo().equals(input);
//          }
//          else {
//              List<TreeNode<DeviceClass>> children = TreeNode.this.children();
//          }
//            
//            if (cmp.compareTo(elData) == 0)
//                return element;
//        }
//
//        return null;
//    }
//
//    @Override
//    public String toString() {
//        
//        return data != null ? data.toString() : "[data null]";
//    }
//
//    @Override
//    public Iterator<TreeNode<DeviceClass>> iterator() {
//        TreeNodeIter<DeviceClass> iter = new TreeNodeIter<DeviceClass>(this);
//        return iter;
//    }
//
//}
//
