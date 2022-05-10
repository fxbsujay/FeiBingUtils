package com.susu.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>Description: Tree structure tool class</p>
 * <p>树形结构工具类</p>
 * @author sujay
 * @version 13:21 2022/5/10
 * @see TreeNode
 * @since JDK1.8
 */
public class TreeUtils {

    /**
     * <p>Description: According to PID, build tree node</p>
     * <p>根据pid，构建树节点</p>
     */
    public static <T extends TreeNode<T>> List<T> build(List<T> nodeList, Integer pid) {
        if (pid == null) {
            throw new RuntimeException("pid is null, build tree failed");
        }
        List<T> treeList = new ArrayList<>();
        for(T node : nodeList) {
            if (pid.equals(node.getDefineId())) {
                node.setLevel(1);
                treeList.add(findChildren(nodeList, node));
            }
        }
        return treeList;
    }

    /**
     * <p>Description: Recursively find child nodes</p>
     * <p>递归查找子节点</p>
     */
    private static <T extends TreeNode<T>> T findChildren(List<T> nodeList, T rootNode) {
        for(T node : nodeList) {
            if(rootNode.getId().equals(node.getDefineId())) {
                int level = rootNode.getLevel() + 1;
                node.setLevel(level);
                if (rootNode.getChildren() == null){
                    rootNode.setChildren(new ArrayList<T>());
                }
                rootNode.getChildren().add(findChildren(nodeList, node));
            }
        }
        return rootNode;
    }

    /**
     * <p>Description: Build tree node</p>
     * <p>构建树节点</p>
     */
    public static <T extends TreeNode<T>> List<T> build(List<T> nodeList) {
        List<T> result = new ArrayList<>();
        Map<Integer, T> nodeMap = new LinkedHashMap<>(nodeList.size());
        for(T node : nodeList){
            nodeMap.put(node.getId(), node);
        }
        for(T node : nodeMap.values()) {
            T parent = nodeMap.get(node.getDefineId());
            if(parent != null && !(node.getId().equals(parent.getId()))){
                parent.getChildren().add(node);
                continue;
            }
            result.add(node);
        }
        return result;
    }

}

/**
 * <p>Description: Tree node. All that need to implement the tree node need to inherit this class</p>
 * <p>树节点，所有需要实现树节点的，都需要继承该类</p>
 */
class TreeNode<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * <p>Description: Primary key</p>
     * <p>主键</p>
     */
    private Integer id;

    /**
     * <p>Description: Parent ID</p>
     * <p>上级ID</p>
     */
    private Integer defineId;

    /**
     * <p>Description: level</p>
     * <p>层级</p>
     */
    private int level;

    /**
     * <p>Description: Child node list</p>
     * <p>子节点列表</p>
     */
    private List<T> children = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDefineId() {
        return defineId;
    }

    public void setDefineId(Integer defineId) {
        this.defineId = defineId;
    }

    public List<T> getChildren() {
        return children;
    }

    public void setChildren(List<T> children) {
        this.children = children;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}

