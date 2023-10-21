package com.sean.cmm.plugin;

public class BinaryTree {

    static class Node {
        int value;
        Node left;
        Node right;

        Node(int value) {
            this.value = value;
            left = null;
            right = null;
        }
    }

    Node root;

    BinaryTree() {
        root = null;
    }

    void insert(int value) {
        root = insertRecursively(root, value);
    }

    Node insertRecursively(Node currentNode, int value) {
        if (currentNode == null) {
            currentNode = new Node(value);
            return currentNode;
        }

        if (value < currentNode.value) {
            currentNode.left = insertRecursively(currentNode.left, value);
        } else if (value > currentNode.value) {
            currentNode.right = insertRecursively(currentNode.right, value);
        }

        return currentNode;
    }

    // 先序遍历
    void preorderTraversal(Node node) {
        if (node != null) {
            System.out.print(node.value + " ");
            preorderTraversal(node.left);
            preorderTraversal(node.right);
        }
    }

    // 中序遍历
    void inorderTraversal(Node node) {
        if (node != null) {
            inorderTraversal(node.left);
            System.out.print(node.value + " ");
            inorderTraversal(node.right);
        }
    }

    // 后序遍历
    void postorderTraversal(Node node) {
        if (node != null) {
            postorderTraversal(node.left);
            postorderTraversal(node.right);
            System.out.print(node.value + " ");
        }
    }

    public static void main(String[] args) {
        BinaryTree tree = new BinaryTree();

        tree.insert(50);
        tree.insert(30);
        tree.insert(20);
        tree.insert(40);
        tree.insert(70);
        tree.insert(60);
        tree.insert(80);

        System.out.println("先序遍历：");
        tree.preorderTraversal(tree.root);

        System.out.println("\n中序遍历：");
        tree.inorderTraversal(tree.root);

        System.out.println("\n后序遍历：");
        tree.postorderTraversal(tree.root);
    }
}
