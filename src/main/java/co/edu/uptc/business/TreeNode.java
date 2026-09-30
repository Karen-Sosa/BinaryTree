package co.edu.uptc.business;

import org.w3c.dom.Node;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TreeNode{
    Node rigth;
    Node left;
    String data;
}