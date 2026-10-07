package co.edu.uptc.business;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TreeNode{
    private TreeNode right;
    private TreeNode left;
    private String data;

    public TreeNode(String data){
        this.data = data;
        this.left = null;
        this.right = null;
    }

    public boolean isLeaf() {
        return left == null && right == null;
    }
}