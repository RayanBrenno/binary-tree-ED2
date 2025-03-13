package br.com.gomide.binary_tree;

public class BinaryTree<T extends Comparable<T>> implements IBinaryTree<T> {

	public Node<T> createTree(T element) {
		Node<T> rootNode = new Node<>();
		rootNode.setValue(element);
		return rootNode;
	}

	public Node<T> createTree(T[] elements) {
		Node<T> rootNode = new Node<>();
		rootNode.setValue(elements[0]);

		for (int i = 1; i < elements.length; i++) {
			insert(rootNode, elements[i]);
		}

		return rootNode;
	}

	public void insert(Node<T> rootNode, T element) {

		if (rootNode.getValue().compareTo(element) > 0) {
			if (rootNode.getLeft() == null) {
				rootNode.setLeft(new Node<>());
				rootNode.getLeft().setValue(element);
			} else {
				insert(rootNode.getLeft(), element);
			}
		} else if (rootNode.getValue().compareTo(element) < 0) {
			if (rootNode.getRight() == null) {
				rootNode.setRight(new Node<>());
				rootNode.getRight().setValue(element);
			} else {
				insert(rootNode.getRight(), element);
			}
		}

	}

	public String toString(Node<T> rootNode) {
		if (rootNode == null)
			return "";
		else {
			String res = "root:" + rootNode.getValue() + " ";

			boolean valAux = (rootNode.getLeft() == null && rootNode.getRight() == null);
			if (valAux)
				return res;

			res += toStringRec(rootNode.getLeft(), "left", "");
			res += toStringRec(rootNode.getRight(), "right", "");

			return res + ")";
		}
	}

	public String toStringRec(Node<T> rootNode, String aux, String result) {
		if (rootNode == null)
			return result;

		boolean valAux = (rootNode.getLeft() != null || rootNode.getRight() != null);
		boolean valSoDireita = (rootNode.getLeft() == null && rootNode.getRight() != null);

		if (aux.equals("left")) {
			result += "(left:" + rootNode.getValue() + " ";
		} else if (aux.equals("right")) {
			result += "right:" + rootNode.getValue() + " ";
		}

		if (valSoDireita)
			result += "(";

		result = toStringRec(rootNode.getLeft(), "left", result);
		result = toStringRec(rootNode.getRight(), "right", result);

		if (valAux)
			result += ")";

		return result;
	}

	public Integer degree(Node<T> rootNode, T nodeElement) {
		if (rootNode == null || nodeElement == null)
			return null;

		if (rootNode.getValue().compareTo(nodeElement) > 0) {
			return degree(rootNode.getLeft(), nodeElement);
		} else if (rootNode.getValue().compareTo(nodeElement) < 0) {
			return degree(rootNode.getRight(), nodeElement);
		} else {
			Integer res = 0;
			if (rootNode.getLeft() != null)
				res += 1;
			if (rootNode.getRight() != null)
				res += 1;
			return res;
		}
	}

	public Integer calculateNodeLevel(Node<T> rootNode, T nodeElement) {
		if (nodeElement == null || rootNode == null)
			return 10;

		if (rootNode.getValue().compareTo(nodeElement) > 0) {
			Integer aux = calculateNodeLevel(rootNode.getLeft(), nodeElement);
			if (aux == null) {
				return null; 
			} else {
				return 1 + aux; 
			}
		} 

		if (rootNode.getValue().compareTo(nodeElement) < 0) {
			Integer aux = calculateNodeLevel(rootNode.getRight(), nodeElement);
			if (aux == null) {
				return null; 
			} else {
				return 1 + aux; 
			}
		}

		return 0; 
	}

	public Node<T> getByElement(Node<T> rootNode, T element) {
		if (rootNode == null || element == null)
			return null;

		if (rootNode.getValue().compareTo(element) > 0) {
			return getByElement(rootNode.getLeft(), element);
		} else if (rootNode.getValue().compareTo(element) < 0) {
			return getByElement(rootNode.getRight(), element);
		} else {
			return rootNode;
		}
	}

	public Node<T> getFather(Node<T> rootNode, T nodeElement) {
		if (rootNode == null || nodeElement == null)
			return null;

		if (rootNode.getLeft() != null && rootNode.getLeft().getValue().compareTo(nodeElement) == 0) {
			return rootNode;
		} else if (rootNode.getRight() != null && rootNode.getRight().getValue().compareTo(nodeElement) == 0) {
			return rootNode;
		}

		if (rootNode.getValue().compareTo(nodeElement) > 0) {
			return getFather(rootNode.getLeft(), nodeElement);
		} else if (rootNode.getValue().compareTo(nodeElement) < 0) {
			return getFather(rootNode.getRight(), nodeElement);
		} else { 
			return null;
		} 
		
	}

	public Node<T> getBrother(Node<T> rootNode, T nodeElement) {
		if (rootNode == null || nodeElement == null)
			return null;

		if (rootNode.getLeft() != null && rootNode.getLeft().getValue().compareTo(nodeElement) == 0) {
			return rootNode.getRight();
		}

		if (rootNode.getRight() != null && rootNode.getRight().getValue().compareTo(nodeElement) == 0) {
			return rootNode.getLeft();
		}

		if (rootNode.getValue().compareTo(nodeElement) > 0) {
			return getBrother(rootNode.getLeft(), nodeElement);
		} else if (rootNode.getValue().compareTo(nodeElement) < 0) {
			return getBrother(rootNode.getRight(), nodeElement);
		} else {
			return null;
		}

	}


	@Override
	public boolean remove(Node<T> rootNode, T nodeElement) {
		if (rootNode == null) return false;

		// raíz
		if (rootNode.getValue().equals(nodeElement)) {
			if (rootNode.getLeft() == null && rootNode.getRight() == null) {
				return false;
			}

			// Encontra o maior filho
			Node<T> largestChild = (rootNode.getRight() != null &&
					(rootNode.getLeft() == null || rootNode.getRight().getValue().compareTo(rootNode.getLeft().getValue()) > 0))
					? rootNode.getRight()
					: rootNode.getLeft();

			Node<T> leftSubtree = rootNode.getLeft();

			rootNode.setValue(largestChild.getValue());

			if (largestChild == rootNode.getRight()) {
				rootNode.setRight(largestChild.getRight());
				rootNode.setLeft(largestChild.getLeft());
			} else {
				rootNode.setLeft(largestChild.getLeft());
			}

			if (leftSubtree != null) {
				if (rootNode.getLeft() == null) {
					rootNode.setLeft(leftSubtree);
				} else {
					Node<T> temp = rootNode.getLeft();
					while (temp.getLeft() != null) {
						temp = temp.getLeft();
					}
					temp.setLeft(leftSubtree);
				}
			}

			return true;
		}

		Node<T> parent = null;
		Node<T> current = rootNode;

		while (current != null && !current.getValue().equals(nodeElement)) {
			parent = current;
			if (nodeElement.compareTo(current.getValue()) < 0) {
				current = current.getLeft();
			} else {
				current = current.getRight();
			}
		}

		// nó não encontrado
		if (current == null) {
			return false;
		}

		// folha
		if (current.getLeft() == null && current.getRight() == null) {
			if (parent.getLeft() == current) {
				parent.setLeft(null);
			} else {
				parent.setRight(null);
			}
		}
		// um filho
		else if (current.getLeft() == null || current.getRight() == null) {
			Node<T> child = (current.getLeft() != null) ? current.getLeft() : current.getRight();
			if (parent.getLeft() == current) {
				parent.setLeft(child);
			} else {
				parent.setRight(child);
			}
		}
		// dois filhos
		else {
			Node<T> largestChild = (current.getRight().getValue().compareTo(current.getLeft().getValue()) > 0)
					? current.getRight()
					: current.getLeft();

			T largestChildValue = largestChild.getValue();
			remove(rootNode, largestChildValue);
			current.setValue(largestChildValue);
		}

		return true;
	}

	public Integer calculateTreeDepth(Node<T> rootNode) {
		if (rootNode == null) {
			return -1;
		}

		int maxEsquerda = 1 + calculateTreeDepth(rootNode.getLeft());
		int maxDireita = 1 + calculateTreeDepth(rootNode.getRight());

		return Math.max(maxEsquerda, maxDireita);

	}

}
